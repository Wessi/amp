package org.digijava.module.autopatcher.core;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.service.AbstractServiceImpl;
import org.digijava.kernel.service.ServiceContext;
import org.digijava.kernel.service.ServiceException;
import org.digijava.module.autopatcher.exceptions.InvalidPatchRepositoryException;

/**
 * AutopatcherService.java
 * (c) 2007 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since 2007 08 08
 * 
 */
public class AutopatcherService extends AbstractServiceImpl {

	private String patchesDir;
	private Collection appliedPatches;
	private static Logger logger = Logger.getLogger(AutopatcherService.class);

	
	protected void processInitEvent(ServiceContext serviceContext)
			throws ServiceException {
		
			Session session;
			appliedPatches=new ArrayList();
			try {
				session = PersistenceManager.getSession();
			String realRootPath=serviceContext.getRealPath("/"+patchesDir+"/");
			logger.info("Applying patches...");
			logger.info("Patch directory is "+realRootPath);
			Collection<File> allPatchesFiles = PatcherUtil.getAllPatchesFiles(serviceContext.getRealPath(patchesDir));
			Set allAppliedPatches = PatcherUtil.getAllAppliedPatches(session);
			Iterator i=allPatchesFiles.iterator();
			while (i.hasNext()) {
				File element = (File) i.next();
				String localPatchPath=element.getAbsolutePath().substring(realRootPath.length()+1,element.getAbsolutePath().length());
				if(allAppliedPatches.contains(localPatchPath)) continue;
				
				try {
				String delimiter=";";
				boolean firstLine=true;
				LineNumberReader bis=new LineNumberReader(new FileReader(element));
				StringBuffer sb= new StringBuffer();
				String s=bis.readLine();
				while(s!=null) {
				if(firstLine && s.length()>=11 && s.substring(0,9).equalsIgnoreCase("delimiter")) {
					delimiter=s.substring(10, 11);
					s = bis.readLine();
					continue;
				}
					
				 sb.append(s);
				 firstLine=false;
				 s = bis.readLine();					
				}
				bis.close();
							
				StringTokenizer stok=new StringTokenizer(sb.toString(),delimiter);
				logger.info("Applying patch "+element.getAbsolutePath());
				logger.info("Executing sql commands: "+sb.toString());
				
				
				Connection connection = PersistenceManager.getSession().connection();
				connection.setAutoCommit(false);
				
				try {				
				Statement st=connection.createStatement();
				
				
				

				while(stok.hasMoreTokens()) {
					
				String sqlCommand=stok.nextToken();
				if(sqlCommand.trim().equals("")) continue;
			
				st.addBatch(sqlCommand);
				
				
				}

				st.executeBatch();
				connection.commit();
				st.close();
				
				PatchFile pf=new PatchFile();
				pf.setAbsolutePatchName(localPatchPath);
				pf.setInvoked(new Timestamp(System.currentTimeMillis()));
				
				
				session.save(pf);
				
				appliedPatches.add(element.getAbsolutePath());
			
				
				}
				
				catch (BatchUpdateException e) {
					e.printStackTrace();
					logger.error(e);
					connection.rollback();
					
				} finally {
					connection.setAutoCommit(true);
					connection.close();
				}
				
				

				
				} catch (Exception e ) {
					logger.error(e);
					e.printStackTrace();
				}
		
			}
			session.close();
			
			} catch (HibernateException e1) {
				// TODO Auto-generated catch block
				throw new RuntimeException( "HibernateException Exception encountered", e1);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				throw new RuntimeException( "SQLException Exception encountered", e1);
			} catch (InvalidPatchRepositoryException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException( "InvalidPatchRepositoryException Exception encountered", e);
			}
			
			
			logger.info(this.toString());
			
		

	}
	
	public String toString() {
		return "Autopatcher: Patches applied : "+appliedPatches;		
	}

	public String getPatchesDir() {
		return patchesDir;
	}

	public void setPatchesDir(String patchesDir) {
		this.patchesDir = patchesDir;
	}
	}

