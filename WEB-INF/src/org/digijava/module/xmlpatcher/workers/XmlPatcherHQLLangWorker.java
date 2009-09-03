/**
 * XmlPatcherHQLLangWorker.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.workers;

import org.digijava.module.xmlpatcher.dbentity.XmlPatchLog;
import org.digijava.module.xmlpatcher.exception.XmlPatcherWorkerException;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org Provides support to
 *         execute Hibernte Query Language (HQL) lang-type scripts.
 * @see https://www.hibernate.org/hib_docs/nhibernate/html/queryhql.html
 */
public class XmlPatcherHQLLangWorker extends XmlPatcherLangWorker {

	/**
	 * @param entity
	 * @param log
	 */
	public XmlPatcherHQLLangWorker(Object entity, XmlPatchLog log) {
		super(entity, log);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.digijava.module.xmlpatcher.workers.XmlPatcherWorker#process()
	 */
	@Override
	protected boolean process() throws XmlPatcherWorkerException {
		// TODO Auto-generated method stub
		return false;
	}

}
