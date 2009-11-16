/**
 * XmlPatcherService.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.core;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.service.AbstractServiceImpl;
import org.digijava.kernel.service.ServiceContext;
import org.digijava.kernel.service.ServiceException;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatch;
import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatchLog;
import org.digijava.module.xmlpatcher.jaxb.Patch;
import org.digijava.module.xmlpatcher.scheduler.NaturalOrderXmlPatcherScheduler;
import org.digijava.module.xmlpatcher.scheduler.XmlPatcherScheduler;
import org.digijava.module.xmlpatcher.util.XmlPatcherConstants;
import org.digijava.module.xmlpatcher.util.XmlPatcherUtil;
import org.digijava.module.xmlpatcher.worker.XmlPatcherWorker;
import org.hibernate.HibernateException;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
public class XmlPatcherService extends AbstractServiceImpl {
	private static Logger logger = Logger.getLogger(XmlPatcherService.class);

	/**
	 * The name of the scheduler as read from digi.xml. This will be later used
	 * to dynamically instantiate the scheduler
	 */
	private String schedulerName;
	
	/**
	 * The scheduler used by the patcher service. The default scheduler is set
	 * in the service constructor. However another scheduler may be provided in
	 * digi.xml, using the property name schedulerName. This scheduler
	 */
	private XmlPatcherScheduler scheduler;

	/**
	 * Useful properties that may be set in digi.xml file, for the use of the
	 * scheduler You may set them inside the service entity: &lt;property&gt;
	 * name="schedulerProperties(propertyKey)">propertyValue&lt;/property&gt;
	 */
	private Map<String, Object> schedulerProperties;

	/**
	 * @see org.digijava.kernel.service.ServiceManager Default constructor
	 *      invoked by ServiceManager
	 */
	public XmlPatcherService() {
		super();
		schedulerProperties = new HashMap<String, Object>();
		schedulerName = NaturalOrderXmlPatcherScheduler.class.getSimpleName();
	}

	
	/**
	 * Schedules the patch list for execution until the previous list is identical in size with the current list, meaning no more patches have been executed.
	 * This is required to have all patch dependencies fullfilled
	 * @param scheduledPatches
	 * @param serviceContext
	 * @return
	 * @throws DgException
	 * @see {@link XmlPatcherService#processUnclosedPatches(Collection, ServiceContext)}
	 */
	public int processAllUnclosedPatches(
			Collection<AmpXmlPatch> scheduledPatches,
			ServiceContext serviceContext) throws DgException {
	
		Collection<AmpXmlPatch> previouslyUnclosedPatches=null;
		Collection<AmpXmlPatch> currentUnclosedPatches=null;
		
		do {
			previouslyUnclosedPatches=currentUnclosedPatches;
			currentUnclosedPatches = processUnclosedPatches(scheduledPatches, serviceContext);
		}  while(previouslyUnclosedPatches==null || currentUnclosedPatches.size()!=previouslyUnclosedPatches.size());
		return currentUnclosedPatches.size();
	}
	
	/**
	 * Attempts to execute the given collection of unclosed patches. The
	 * collection is ordered using a scheduler that was given as a parameter in
	 * digi.xml. If the scheduler is missing then the default scheduler is used.
	 * <p>
	 * The collection is iterated and each patch is invoked. If the patch worker
	 * will return false it means the patch was not applied.
	 * 
	 * @param scheduledPatches
	 * @param serviceContext
	 * @throws DgException
	 */
	private Collection<AmpXmlPatch> processUnclosedPatches(
			Collection<AmpXmlPatch> scheduledPatches,
			ServiceContext serviceContext) throws DgException {
		Iterator<AmpXmlPatch> iterator = scheduledPatches.iterator();
		logger.info(scheduledPatches.size()+" patches scheduled for execution...");
		while (iterator.hasNext()) {
			AmpXmlPatch ampPatch = iterator.next();
			long timeStart = System.currentTimeMillis();
			AmpXmlPatchLog log = new AmpXmlPatchLog(ampPatch);
			logger.info("Reading patch: "+ampPatch.getPatchId());
			try {
				log.setFileChecksum(XmlPatcherUtil.getFileMD5(new File(
						XmlPatcherUtil.getXmlPatchAbsoluteFileName(ampPatch,
								serviceContext))));
			} catch (NoSuchAlgorithmException e) {
				logger.error(e);
				throw new RuntimeException(e);
			} catch (IOException e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
			Patch patch = XmlPatcherUtil.getUnmarshalledPatch(serviceContext,
					ampPatch, log);
			boolean success = false;
			if (patch != null) {
				if (ampPatch.getState().equals(
						XmlPatcherConstants.PatchStates.FAILED)
						&& !patch.isRetryOnFail()) {
					logger.info("Skipping failed patch "
							+ ampPatch.getPatchId());
					continue;
				}
			logger.info("Applying patch: "+ampPatch.getPatchId());
				XmlPatcherWorker<?, ?> patcherWorker = XmlPatcherWorkerFactory
						.createWorker(patch, null, log);
				success = patcherWorker.run();
			}
			if (success) {
				logger.info("Succesfully applied patch "
						+ ampPatch.getPatchId());
				ampPatch.setState(XmlPatcherConstants.PatchStates.CLOSED);
				iterator.remove();
				
			} else if (log.getError()) {
				logger.info("Failed to apply patch " + ampPatch.getPatchId());
				ampPatch.setState(XmlPatcherConstants.PatchStates.FAILED);
				iterator.remove();
			} else
				logger.info("Will not apply " + ampPatch.getPatchId()
						+ " due to conditions not met.");

			log.setElapsed(System.currentTimeMillis() - timeStart);
			XmlPatcherUtil.addLogToPatch(ampPatch, log);
			DbUtil.update(ampPatch);
		}
		logger.info(scheduledPatches.size()+" patches left unexecuted");
		return scheduledPatches;
	}

	@Override
	public void processInitEvent(ServiceContext serviceContext)
			throws ServiceException {
		try {
			performPatchDiscovery(serviceContext.getRealPath("/"));

			List<AmpXmlPatch> rawPatches = XmlPatcherUtil
					.getAllDiscoveredUnclosedPatches();
			scheduler = (XmlPatcherScheduler) Class.forName(
					XmlPatcherConstants.schedulersPackage + schedulerName)
					.getConstructors()[0].newInstance(new Object[] {
					schedulerProperties, rawPatches });
			Collection<AmpXmlPatch> scheduledPatches = scheduler
					.getScheduledPatchCollection();

			//running deprecation
			
			
			processAllUnclosedPatches(scheduledPatches, serviceContext);
			
			
		} catch(Exception e) {
			logger.error(e);
			throw new ServiceException(e);
		}
		logger.info("XML Patcher session finished");

	}

	/**
	 * Discovers new patches in the known locations (Digi modules directories or
	 * the generic patch directory).
	 * 
	 * @param serviceContext
	 * @throws DgException
	 * @throws SQLException
	 * @throws HibernateException
	 */
	public void performPatchDiscovery(String appPath) throws DgException,
			HibernateException, SQLException {
		// start by getting a complete list of patch locations:
		Set<File> patchDirs = XmlPatcherUtil.discoverPatchDirs(appPath);
		logger.info("Discovered XML Patch Directories: " + patchDirs);

		// get a full list of known patch files from the database
		Set<String> allDiscoveredPatchNames = XmlPatcherUtil
				.getAllDiscoveredPatchNames();

		Iterator<File> i = patchDirs.iterator();
		while (i.hasNext()) {
			File dir = i.next();
			XmlPatcherUtil.recordNewPatchesInDir(appPath, dir,
					allDiscoveredPatchNames);
		}
	}

	public String getSchedulerProperties(String key) {
		return (String) schedulerProperties.get(key);
	}

	public void setSchedulerProperties(String key, Object value) {
		schedulerProperties.put(key, value);
	}

	public Map<String, Object> getSchedulerProperties() {
		return schedulerProperties;
	}

	public void setSchedulerProperties(Map<String, Object> schedulerProperties) {
		this.schedulerProperties = schedulerProperties;
	}

	public String getSchedulerName() {
		return schedulerName;
	}

	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}

}
