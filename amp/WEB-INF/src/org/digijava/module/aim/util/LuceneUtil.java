package org.digijava.module.aim.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.lucene.LuceneWorker;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpLuceneIndexStamp;
import org.digijava.module.help.helper.HelpSearchData;
import org.digijava.module.help.util.HelpUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * !                                                                          !
 * ! The serial version needs to be changed if the index generating algorithm !
 * ! is changed, so index will be regenerated                                 !
 * !                                                                          !
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 
 * 
 * @author Alexandru Artimon
 * 
 */ 

public class LuceneUtil implements Serializable {
	/**
	 * We use the serialVersion to know the version of the lucene index
	 * saved on the disk, if versions mismatch then we need to increment
	 * the index
	 */
	private static final long serialVersionUID = 8L;
												
	private static Logger logger = Logger.getLogger(LuceneUtil.class);
    /**
     * StandardAnalyzer used to analyse text 
     */
    public final static Analyzer analyzer = new StandardAnalyzer();
    /**
     * 
     */
    public final static String ID_FIELD = "id";

    /**
     * LUCENE INDEX PATH: use the LUCENE_BASE_DIR + new 
     * 					  directory for your index
     */
    
    /**
     * lucene base directory
     */
    public final static String LUCENE_BASE_DIR = "lucene";
    public final static String LUCENE_STAMP_EXT = ".stamp";
    
    /**
     * name of help index directory
     */
    public final static String HELP_INDEX_SUFIX = "help";
    public final static String HELP_INDEX_DIRECTORY = LUCENE_BASE_DIR +"/" + HELP_INDEX_SUFIX;
    
    /**
     * name of the activity index directory
     * please don't use for other indexes
     * @author Arty
     */
    public final static String ACTIVITY_INDEX_SUFFIX = "activity";
    public final static String ACTVITY_INDEX_DIRECTORY = LUCENE_BASE_DIR + "/" + ACTIVITY_INDEX_SUFFIX;

	private static final int CHUNK_SIZE = 10000;
    
    public static AmpLuceneIndexStamp getIdxStamp(String name) throws Exception{
	  	String oql= "select idx from "+AmpLuceneIndexStamp.class.getName()+" idx"+
	  	  			" where idx.idxName=:theName";
	  	try {
	  		Session session=PersistenceManager.getRequestDBSession();
	  		
	  		org.hibernate.Query query=session.createQuery(oql);
	  		query.setString("theName", name);
	  		
	  		return ((AmpLuceneIndexStamp)query.uniqueResult());
	  	} catch (Exception e) {
	  		throw new Exception("Cannot get index stamp for:"+name,e);
	  	}
    }
    
    /**
     * Deletes all stamps from db with specified name.
     * @param name
     * @return true - at least one record has been deleted. false - nothing was deleted.
     * @throws DgException
     */
	@SuppressWarnings("unchecked")
	public static boolean deleteIdxStamps(String name) throws DgException {
		String oql = "select idx from " + AmpLuceneIndexStamp.class.getName()
				+ " idx" + " where idx.idxName=:theName";
		Transaction transaction = null;
		boolean oldRecordDeleted = false;
		try {
			Session session = PersistenceManager.getRequestDBSession();
			org.hibernate.Query query = session.createQuery(oql);
			query.setString("theName", name);
			List<AmpLuceneIndexStamp> stamps = query.list();
			// Delete all fund stamps for this module name.
			if (stamps != null && stamps.size() > 0) {
				transaction = session.beginTransaction();
				for (AmpLuceneIndexStamp stamp : stamps) {
					session.delete(stamp);
				}
				transaction.commit();
				oldRecordDeleted = true;
			}
		} catch (HibernateException e) {
			if (transaction != null) {
				try {
					transaction.rollback();
				} catch (HibernateException e1) {
					throw new DgException("Cannot rollback stamp deletion for "
							+ name, e1);
				}
			}
			throw new DgException("Cannot delete stams for " + name, e);
		}
		return oldRecordDeleted;
	}

	/**
	 * Creates new stamp record.
	 * @param name
	 * @param timestamp
	 * @throws DgException
	 */
	public static void createStamp(String name, long timestamp) throws DgException {
		AmpLuceneIndexStamp stamp = new AmpLuceneIndexStamp();
		stamp.setIdxName(name);
		stamp.setStamp(timestamp);
		Session session = PersistenceManager.getRequestDBSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(stamp);
			tx.commit();
		} catch (HibernateException e) {
			if(tx!=null){
				try {
					tx.rollback();
				} catch (HibernateException e1) {
					logger.error(e1);
					throw new DgException("Cannot rollback stamp creation for "+name,e1);
				}
			}
			throw new DgException("Cannot create stamp for "+name, e);
		}
		
	}
	
    static public boolean deleteDirectory(String path) {
    	File dir = new File(path);
    	return deleteDirectory(dir);
    }
    
    static public boolean deleteDirectory(File path) {
    	if(path.exists()) {
    		File[] files = path.listFiles();
    		for(int i=0; i<files.length; i++) {
    			if(files[i].isDirectory()) {
    				deleteDirectory(files[i]);
    			}
    			else {
    				files[i].delete();
    			}
    		}
    	}
    	return(path.delete());
    }


    public static void checkIndex(ServletContext sc){
    	logger.info("Lucene startup!");

    	File idxStamp = new File(sc.getRealPath("/") + LUCENE_BASE_DIR + "/" + ACTIVITY_INDEX_SUFFIX + LUCENE_STAMP_EXT);
    	File idxDir = new File(sc.getRealPath("/") + ACTVITY_INDEX_DIRECTORY);
    	boolean deleteIndex = false;
    	
    	checkStamp:{
    		if (idxStamp.exists()){
    			logger.info("Checking stamp ...");
    			
    			FileInputStream fis = null;
    			try {
    				fis = new FileInputStream(idxStamp);
    			} catch (FileNotFoundException e) {
        			logger.error("Stamp file missing:",e);
        			logger.info("Checking for index directory ...");
        			deleteIndex = true;
        			break checkStamp;
    			}
    			
    			DataInputStream dis = new DataInputStream(fis);
    			long serialId;
    			long dbId;
    			try {
    				serialId = dis.readLong();
    				dbId = dis.readLong();
    				dis.close();
    			} catch (IOException e) {
        			logger.error("Error while reading stamp file:",e);
        			logger.info("Checking for index directory ...");
        			deleteIndex = true;
        			break checkStamp;
    			}
    			
    			//checking if the indexing algorithm has changed - new source code?
    			if (serialVersionUID != serialId){
    				logger.warn("Algorithm serial ID mismatch ... regenerating index");
    				idxStamp.delete();
    				logger.info("Stamp deleted, looking for index directory ...");
    				deleteIndex = true;
    				break checkStamp;
    			}
    			else{
    				logger.info("Lucene Index algorithm serial ID is OK");
    			}
    			
    			//getting DB timestamp
    			long dbStamp = -1;
    			try {
					dbStamp = getIdxStamp(ACTIVITY_INDEX_SUFFIX).getStamp();
				} catch (Exception e) {
					logger.error(e);
				}
    			
    			//checking the database for timestamp
    			if (dbStamp != dbId){
    				logger.warn("Database time stamp mismatch ... regenerating index");
    				idxStamp.delete();
    				logger.info("Stamp deleted, looking for index directory ...");
    				deleteIndex = true;
    				break checkStamp;
    			}
    			else{
    				logger.info("Lucene Index database stamp is OK");
    			}
     		}
    		else{
    			logger.info("Stamp file missing, checking for index directory ...");
    			deleteIndex = true;
    		}
    	}
    	
    	if (deleteIndex){
    		if (idxDir.exists()){
    			logger.info("Found, deleting ...");
    			if (deleteDirectory(idxDir))
    				logger.info("Done");
    			else
    				logger.info("Can't delete!");
    		}
    		else{
    			logger.info("Not found ... will be generated!");
    		}
    	}
   	
    	if (!idxDir.exists()){ //we need to create the index from 0  
    		if (!idxDir.mkdirs()){
    			logger.error("**********************************************************************");
    			logger.error("*                                                                    *");
    			logger.error("*                           WARNING                                  *");
    			logger.error("*     Can't create Lucene Index directory, searching won't work!     *");
    			logger.error("*                                                                    *");
    			logger.error("**********************************************************************");
    			return;
    		}
    		
    		int mId = getMaxActivityId();
    		long startTime = System.currentTimeMillis();
    		try {
				IndexWriter fsWriter = new IndexWriter(idxDir, analyzer, true);
				
				int chunkNo = 0;
		
				while (createIndex(chunkNo, mId, fsWriter) != null){
					chunkNo++;
				}
				fsWriter.optimize();
				fsWriter.close();
				long stopTime = System.currentTimeMillis();
				
				try {
					AmpLuceneIndexStamp currentStamp = getIdxStamp(ACTIVITY_INDEX_SUFFIX);
					if (currentStamp != null)
						DbUtil.deleteAllStamps(ACTIVITY_INDEX_SUFFIX);
				} catch (Exception e1) {
				}
				
				AmpLuceneIndexStamp stamp = new AmpLuceneIndexStamp();
				stamp.setIdxName(ACTIVITY_INDEX_SUFFIX);
				stamp.setStamp(stopTime);
				
				try {
					Session session = PersistenceManager.getRequestDBSession();
					Transaction tx = session.beginTransaction();
					session.save(stamp);
					tx.commit();
					//PersistenceManager.releaseSession(session);
				}
				catch (Exception e) {
					logger.error("Error while saving lucene db stamp:", e);
				}

				if (idxStamp.exists()){
					idxStamp.delete();
				}
				
				try{
					FileOutputStream fos = new FileOutputStream(idxStamp);
					DataOutputStream dos = new DataOutputStream(fos);
					
					dos.writeLong(serialVersionUID);
					dos.writeLong(stopTime);
					dos.close();
				}
				catch (Exception e) {
					logger.error("Error while saving index stamp file:", e);
				}
				logger.info("Indexing took: " + (stopTime - startTime) + " ms");
			} catch (IOException e) {
				logger.error("Error while creting Lucene index:", e);
				deleteDirectory(idxDir); //no directory .. no index
				return;
			}
			
			logger.info("Lucene Index successfully created!");
    	}
    	else
    		logger.info("Lucene Index found, using saved one:" + idxDir.getAbsolutePath());
    }

    private static int getMaxActivityId(){
    	int ret = -1;
		try{
			Session session = PersistenceManager.getSession();
			Connection	conn	= session.connection();
			Statement st		= conn.createStatement();
			String qryStr		= "select max(amp_activity_id) mid from v_titles";

			ResultSet rs		= st.executeQuery(qryStr);
			
			rs.next();
			if(rs.getString("mid")==null) return 0;
			ret = Integer.parseInt(rs.getString("mid"));
		}
		catch(Exception ex){
			logger.error("Error while getting the max activity id:", ex);
		}
		return ret;
    }
    
    /**
     * Metod is used for first time index creation
     * @return
     */
    private static Integer createIndex(int chunkNo, int mId, IndexWriter indexWriter){
		//RAMDirectory index = new RAMDirectory();
		/*IndexWriter indexWriter = null;
		try {
			indexWriter = new IndexWriter(index, analyzer, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		int chunkStart	= chunkNo*CHUNK_SIZE, 
			chunkEnd	= (chunkNo+1)*CHUNK_SIZE;
		
		
		
		logger.info("Getting activity List for chunk no " + chunkNo + " !");
		Session session = null;
		String qryStr = null;

		try{
			session				= PersistenceManager.getSession();
			Connection	conn	= session.connection();
			
			Statement st		= conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY );
			qryStr 				= "select * from v_titles where amp_activity_id >= " + chunkStart + " and amp_activity_id < " + chunkEnd + " ";
			

			ResultSet rs		= st.executeQuery(qryStr);
			
			final class Items {
				int id;
				String amp_id;
				String title;
				String description;
				String objective;
				String purpose;
				String results;
				String numcont;
				String CRIS;
				String budgetNumber;
				ArrayList<String> componentcode=new ArrayList<String>();
			};
			
			HashMap list = new HashMap();
			
			Items x;
			
			rs.last();
			logger.info("Starting iteration of " + rs.getRow() + " activities!");
			boolean isNext = rs.first();
			
			if (!isNext){
				if ((mId != -1)&&(mId > chunkEnd)){
					return new Integer(1);
				}
				else
					if ((mId == -1) || (mId < chunkEnd))
						return null;
			}
						
			while (isNext){
				x = new Items();
				x.id = Integer.parseInt(rs.getString("amp_activity_id"));
				x.title = rs.getString("name");
				list.put(x.id, x);
				isNext = rs.next();
				//
			}
			//the correct view is v_amp_id, the view with name  v_ampid is not used 
			qryStr = "select * from v_amp_id where amp_activity_id >= " + chunkStart + " and amp_activity_id < " + chunkEnd + " ";
			rs = st.executeQuery(qryStr);
			rs.last();
			logger.info("Starting iteration of " + rs.getRow() + " project id's!");
			isNext = rs.first();
			while (isNext){
				int actId = Integer.parseInt(rs.getString("amp_activity_id"));
				x = (Items) list.get(actId);
				x.amp_id = rs.getString("amp_id");
				isNext = rs.next();
				//
			}

			qryStr = "select * from v_description where amp_activity_id >= " + chunkStart + " and amp_activity_id < " + chunkEnd + " ";
			rs = st.executeQuery(qryStr);
			rs.last();
			logger.info("Starting iteration of " + rs.getRow() + " descriptions!");
			isNext = rs.first();
			while (isNext){
				int actId = Integer.parseInt(rs.getString("amp_activity_id"));
				x = (Items) list.get(actId);
				//you can't use this "trim(dg_editor.body)" as column name .... 
				x.description = rs.getString("ebody");
				isNext = rs.next();
				//
			}
			
			qryStr = "select * from v_objectives where amp_activity_id >= " + chunkStart + " and amp_activity_id < " + chunkEnd + " ";
			rs = st.executeQuery(qryStr);
			rs.last();
			logger.info("Starting iteration of " + rs.getRow() + " objectives!");
			isNext = rs.first();
			while (isNext){
				int actId = Integer.parseInt(rs.getString("amp_activity_id"));
				x = (Items) list.get(actId);
				//you can't use "trim(dg_editor.body)" as column name .... 
				x.objective = rs.getString("ebody");
				isNext = rs.next();
				//
			}

			qryStr = "select * from v_purposes where amp_activity_id >= " + chunkStart + " and amp_activity_id < " + chunkEnd + " ";
			rs = st.executeQuery(qryStr);
			rs.last();
			logger.info("Starting iteration of " + rs.getRow() + " purposes!");
			isNext = rs.first();
			while (isNext){
				int actId = Integer.parseInt(rs.getString("amp_activity_id"));
				x = (Items) list.get(actId);
				//you can't use "trim(dg_editor.body)" as column name .... 
				x.purpose = rs.getString("ebody");
				isNext = rs.next();
				//
			}
			
			qryStr = "select * from v_results where amp_activity_id >= " + chunkStart + " and amp_activity_id < " + chunkEnd + " ";
			rs = st.executeQuery(qryStr);
			rs.last();
			logger.info("Starting iteration of " + rs.getRow() + " results!");
			isNext = rs.first();
			while (isNext){
				int actId = Integer.parseInt(rs.getString("amp_activity_id"));
				x = (Items) list.get(actId);
				//you can't use "trim(dg_editor.body)" as column name .... 
				x.results = rs.getString("ebody");
				isNext = rs.next();
				//
			}
			
			
			//Bolivia contract number
			qryStr = "select * from v_convenio_numcont where amp_activity_id >= " + chunkStart + " and amp_activity_id < " + chunkEnd + " ";
			rs = st.executeQuery(qryStr);
			rs.last();
			logger.info("Starting iteration of " + rs.getRow() + " results!");
			isNext = rs.first();
			while (isNext){
				int actId = Integer.parseInt(rs.getString("amp_activity_id"));
				x = (Items) list.get(actId);
				x.numcont = rs.getString("numcont");
				isNext = rs.next();
				//
			}
			
			
			//Bolivia component code
			qryStr = "select * from v_bolivia_component_code where amp_activity_id >= " + chunkStart + " and amp_activity_id < " + chunkEnd + " ";
			rs = st.executeQuery(qryStr);
			rs.last();
			logger.info("Starting iteration of " + rs.getRow() + " amp_activity_components!");
			isNext = rs.first();
				
			while (isNext){
		    	int currActId = rs.getInt("amp_activity_id");
		    	x = (Items) list.get(currActId);
		    	if (rs.getString("code")!=null){
		    	    x.componentcode.add(rs.getString("code"));
		    	}
				isNext = rs.next();
			}
			
			//New fields for Senegal.
			/*qryStr = "select * from v_senegal_cris_budget where amp_activity_id >= " + chunkStart
					+ " and amp_activity_id < " + chunkEnd + " ";
			rs = st.executeQuery(qryStr);
			rs.last();
			logger.info("Starting iteration of " + rs.getRow() + " amp_activity_components!");
			isNext = rs.first();

			while (isNext) {
				int currActId = rs.getInt("amp_activity_id");
				x = (Items) list.get(currActId);
				x.CRIS = rs.getString("cris_number");
				x.budgetNumber = rs.getString("budget_number");
				isNext = rs.next();
			}
	 	 	*/
			conn.close();

			logger.info("Building the index ");
			Iterator it = list.values().iterator();
			while (it.hasNext()) {
				Items el = (Items) it.next();
				Document doc = activity2Document(String.valueOf(el.id),el.amp_id, el.title, el.description, el.objective, el.purpose, el.results,el.numcont,el.componentcode, el.CRIS, el.budgetNumber);
				if (doc != null)
					indexWriter.addDocument(doc);
			}
			list.clear();
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		
		try {
			indexWriter.optimize();
			//indexWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Integer(1);
	}
	
	/**
	 * Add an activity to the index
	 * 
	 * @param request is used to retreive curent site and navigation language
	 * @param act the activity that will be added
	 */
    public static Document activity2Document(String actId, String projectId, String title, String description,
			String objective, String purpose, String results, String numcont, ArrayList<String> componentcodes,
			String CRIS, String budgetNumber) {
		Document doc = new Document();
		String all = new String("");
		if (actId != null){
			doc.add(new Field(ID_FIELD, actId, Field.Store.YES, Field.Index.UN_TOKENIZED));
			//all = all.concat(" " + actId);
		}
		if (projectId != null){
			doc.add(new Field("projectId", projectId, Field.Store.NO, Field.Index.TOKENIZED));
			all = all.concat(" " + projectId);
		}
		if (title != null){
			doc.add(new Field("title", title, Field.Store.NO, Field.Index.TOKENIZED));
			all = all.concat(" " + title);
		}
		if (description != null && description.length()>0){
			doc.add(new Field("description", description, Field.Store.NO, Field.Index.TOKENIZED));
			all = all.concat(" " + description);
		}
		if (objective != null && objective.length()>0){
			doc.add(new Field("objective", objective, Field.Store.NO, Field.Index.TOKENIZED));
			all = all.concat(" " + objective);
		}
		if (purpose != null && purpose.length()>0){
			doc.add(new Field("purpose", purpose, Field.Store.NO, Field.Index.TOKENIZED));
			all = all.concat(" " + purpose);
		}
		if (results != null && results.length()>0){
			doc.add(new Field("results", results, Field.Store.NO, Field.Index.TOKENIZED));
			all = all.concat(" " + results);
		}
		
		//
		if (numcont != null && numcont.length()>0){
			doc.add(new Field("numcont", numcont, Field.Store.NO, Field.Index.TOKENIZED));
			all = all.concat(" " + numcont);
		}
		
		if (CRIS != null && CRIS.length() > 0) {
			doc.add(new Field("CRIS", CRIS, Field.Store.NO, Field.Index.TOKENIZED));
			all = all.concat(" " + CRIS);
		}
		if (budgetNumber != null && budgetNumber.length() > 0) {
			doc.add(new Field("budgetNumber", budgetNumber, Field.Store.NO, Field.Index.TOKENIZED));
			all = all.concat(" " + budgetNumber);
		}
		
		int i =0;
		if (componentcodes != null && componentcodes.size()>0){
				
        		for (String value : componentcodes) {
        			if (value!=null){
        			doc.add(new Field("componentcode_"+String.valueOf(i), value, Field.Store.NO, Field.Index.TOKENIZED));
        			all = all.concat(" " + value);
        			}
        			i++;
        		}
			
		
		}
		
		if (all.length() == 0)
			return null;
		
		doc.add(new Field("all", all, Field.Store.NO, Field.Index.TOKENIZED));
		return doc;
	}
	
    public static void deleteActivity(String idx, String field, String search){
		Term term = new Term(field, search);
		IndexReader indexReader;
		try {
			indexReader = IndexReader.open(idx);
			indexReader.deleteDocuments(term);
			indexReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public static void addUpdateActivity(HttpServletRequest request, boolean update, Long id) {
		logger.info("Updating activity!");
		try {
			ServletContext sc = request.getSession().getServletContext();
			if (update) {
				deleteActivity(sc.getRealPath("/") + ACTVITY_INDEX_DIRECTORY, ID_FIELD, String.valueOf(id));
			}
			IndexWriter indexWriter = null;
			indexWriter = new IndexWriter(sc.getRealPath("/") + ACTVITY_INDEX_DIRECTORY, LuceneUtil.analyzer, false);
			AmpActivity act = ActivityUtil.loadActivity(id);
			Site site = RequestUtils.getSite(request);
			Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
			// Util.getEditorBody(site,act.getDescription(),navigationLanguage);
			Document doc = null;
			String projectid = act.getAmpId();
			ArrayList<String> componentsCode = new ArrayList<String>();
			Collection<AmpComponent> componentsList = act.getComponents();
			if (componentsList != null) {
				for (AmpComponent c : componentsList) {
					componentsCode.add(c.getCode());
				}
			}
			doc = activity2Document(String.valueOf(act.getAmpActivityId()), projectid, String.valueOf(act.getName()),
					Util.getEditorBody(site, act.getDescription(), navigationLanguage), Util.getEditorBody(site, act
							.getObjective(), navigationLanguage), Util.getEditorBody(site, act.getPurpose(),
							navigationLanguage), Util.getEditorBody(site, act.getResults(), navigationLanguage), Util
							.getEditorBody(site, act.getContactName(), navigationLanguage), componentsCode, act
							.getCrisNumber(), act.getBudgetCodeProjectID());

			if (doc != null) {
				try {
					indexWriter.addDocument(doc);
					indexWriter.optimize();
					indexWriter.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	/**
	 * Runs a search in the index and returns the results
	 * 
	 * @param index the index where the search will be done
	 * @param field the field where you do the search
	 * @param searchString
	 * 
	 * @return a Hits object that contains the results
	 */
	public static Hits search(String index, String field, String searchString){
		QueryParser parser = new QueryParser(field, analyzer);
		Query query = null;
		Hits hits = null;
		
		Searcher indexSearcher = null;
		try {
			indexSearcher = new IndexSearcher(index);
			searchString = searchString.trim();
			if (searchString.charAt(0) == '*')
				searchString = searchString.substring(1);
			//AMP-3806
			searchString = searchString.replace("+","\\+");
			searchString = searchString.replace("-","\\-");
			searchString = searchString.replace("&","\\&");
			searchString = searchString.replace("(","\\(");
			searchString = searchString.replace(")","\\)");
			searchString = searchString.replace("{","\\{");
			searchString = searchString.replace("{","\\}");
			searchString = searchString.replace("[","\\[");
			searchString = searchString.replace("]","\\]");
			
			query = parser.parse(searchString.trim()+"*");
			BooleanQuery bol = new BooleanQuery();
			bol.add(query,BooleanClause.Occur.MUST);
			bol.setMaxClauseCount(2000);
			hits = indexSearcher.search(bol);
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return hits;
	}
         
        
    /**
     * Uses {@link isDir()} method to determine whether 
     * index directory exists or not. 
     * If directory doesn't exist create new one using
     * {@link addUpdatehelp(boolean)}
     * 
     * @throws org.digijava.kernel.exception.DgException
     * 
     */
    public static void createHelp(ServletContext sc) throws  DgException{
            
		boolean createDir = LuceneUtil.checkHelpDir(sc);
	
		if(!createDir){
			logger.info("Building the help");
				  LuceneUtil.addUpdatehelp(false, sc);
		}	
	
    }

    /**
     * Converts html formatted help topics body to plain text format.
     * Creates lucene-index directory if it doesn't exist.
     * Adds or updates converted data to lucene-index directory
     * using {@link indexArticle(String,String,String)} method.
     * <p>
     * In update mode method updates only  data which have last 
     * modified date is greater than the  last modified date of lucene-index directory.
     * 
     * @param update if true then method is used to update the lucene-index directory
     * otherwise to create new lucene-index directory and add data to it.
     * 
     * @throws org.digijava.kernel.exception.DgException
     * 
     * @see org.digijava.module.help.helper.HelpSearchData
     */
    public static void addUpdatehelp(boolean update, ServletContext sc) throws DgException {

    	HelpSearchData item = new HelpSearchData();
    	DateFormat formatter ; 
    	Date date ; 

    	try{ 
    		Long lastLucModDay = IndexReader.lastModified(sc.getRealPath("/") + HELP_INDEX_DIRECTORY);

    		formatter  = new SimpleDateFormat();
    		String leastUpDate = formatter.format(lastLucModDay);
    		date = (Date)formatter.parse(leastUpDate);

    		Collection data =  HelpUtil.getAllHelpData();

    		for(Iterator<HelpSearchData> iter = data.iterator(); iter.hasNext(); ) {

    			item = (HelpSearchData) iter.next();

    			String article =  item.getBody();
    			//String title = item.getTopicKey();
    			String titTrnKey = item.getBodyKey();
    			String lang = item.getLang();
    			String title =HelpUtil.getTrn(item.getTopicKey(), lang,new Long(3));
    			// Converts html formatted help topics body to plain text format.
    			String newCode = article.replaceAll("\\<.*?\\>","");

    			if(update){
    					if(item.getLastModDate().after(date)){
	    					deleteHelp("title",title, sc);
	    					indexArticle(newCode, title,titTrnKey,lang,sc);
	    				}
    			}else if(!update){
    				indexArticle(newCode, title,titTrnKey,lang,sc);	
    			}
    		}
    	} catch (Exception ex) {
    		logger.error(ex);
    		throw new DgException(ex);
    	}
    }

 	
	
	
	
	 	
    /**
     * Searches searchString in the indexDirectory for fields.
     * Returns founded hits
     * 
     * @param field
     * @param searchString
     * @return founded hits
     */
    public static Hits helpSearch(String field, String searchString, ServletContext sc){
		
		QueryParser parser = new QueryParser(field, analyzer);
		Query query = null;
		Hits hits = null;
		Document document = new Document();
		
		Searcher indexSearcher = null;
		try {
			if(searchString != null){
			indexSearcher = new IndexSearcher(sc.getRealPath("/") + HELP_INDEX_DIRECTORY);
			searchString = searchString.trim();
			query = parser.parse("+"+searchString+"*");
		
			hits = indexSearcher.search(query);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	
		return hits;
	}
	
    /**
     * Returns highlighted object
     * 
     * @param field
     * @param searchString
     * @return highlight object
     * @throws java.io.IOException
     * @throws org.apache.lucene.queryParser.ParseException
     */
    public static Object highlighter(Field field,String searchString, ServletContext sc) throws IOException, ParseException{
		Query query = null;
		QueryParser parser = new QueryParser("article", analyzer);
	
		query = parser.parse(searchString);
		
		Object hA= highlight(field,query, sc);
		return hA;
	}

    /**
     * Returns highlighted object
     * 
     */
    private static Object highlight(Field field, Query query, ServletContext sc) throws IOException {

    	query.rewrite(IndexReader.open(sc.getRealPath("/") + HELP_INDEX_DIRECTORY));
    	QueryScorer scorer = new QueryScorer(query);
    	SimpleHTMLFormatter formatter =
    		new SimpleHTMLFormatter("<span class=\"highlight\">",
    		"</span>");
    	Highlighter highlighter = new Highlighter(formatter, scorer);
    	Fragmenter fragmenter = new SimpleFragmenter(50);
    	highlighter.setTextFragmenter(fragmenter);

    	String value = field.stringValue();
    	TokenStream tokenStream = new StandardAnalyzer()
    	.tokenStream(field.name(), new StringReader(value));

    	return highlighter.getBestFragments(tokenStream, value, 5, "...");
    }


    /**
     * Creates {@link Document} using {@link createDocument(String,String,String)}.
     * Adds newly created document to lucene help directory
     *  
     * @param article body of help topic
     * @param title title of help topic
     * @param titTrnKey translation key used to translate title
     * @throws java.lang.Exception
     * @deprecated instead use {@link LuceneWorker#addItemToIndex(Object, ServletContext, String)} method
     */
    public static void indexArticle(String article, String title,String titTrnKey, String lang,ServletContext sc)
    throws Exception {
    	Document document = LuceneUtil.createHelpDocument(article,title,titTrnKey,lang);
    	LuceneUtil.indexHelpDocument(document, sc);
    }	

    /**
     * Creates new {@link Document}. 
     * Adds fields title,titletrnKey,article 
     * to document using passed parameters.
     * 
     * @param article body of help topic
     * @param title title of help topic
     * @param titTrnKey translation key used to translate title
     * @return newly created document
     */
    public static Document createHelpDocument(String article, String title,String titTrnKey,String lang){

    	Document document = new Document();
    	document.add(new Field("title",title,Field.Store.YES,Field.Index.TOKENIZED));
    	document.add(new Field("titletrnKey",titTrnKey,Field.Store.YES,Field.Index.TOKENIZED));
    	document.add(new Field("article",article,Field.Store.YES,Field.Index.TOKENIZED));
    	document.add(new Field("lang",lang,Field.Store.YES,Field.Index.TOKENIZED));
    	return document;

    }


    /**
     * Shows whether lucene help
     * directory exists or no
     * 
     * @return true if lucene-index directory exists otherwise false
     */
    public static boolean checkHelpDir(ServletContext sc){
    	boolean createDir = IndexReader.indexExists(sc.getRealPath("/") + HELP_INDEX_DIRECTORY);
    	return createDir;
    }

    
    /**
     * Creates lucene help
     * directory if it doesn't exist.
     * Adds document to it 
     * 
     * @param document
     * @throws java.io.IOException
     */
    public static void indexHelpDocument(Document document, ServletContext sc) throws IOException {
    	try{

    		boolean createDir = IndexReader.indexExists(sc.getRealPath("/") + HELP_INDEX_DIRECTORY);

    		if(createDir == false){
    			createDir= true;
    		}else if (createDir == true){
    			createDir= false;
    		}

    		StandardAnalyzer analyzer  = new StandardAnalyzer();
    		IndexWriter writer = new IndexWriter(sc.getRealPath("/") + HELP_INDEX_DIRECTORY, analyzer, createDir);
    		writer.addDocument(document);
    		writer.optimize();
    		writer.close();
    	} catch (IOException e) {
    		logger.error(e);
    		throw e;
    	}
    }
    /**
     * 
     * @param field
     * @param search
     * @deprecated instead of this {@link LuceneWorker#deleteItemFromIndex(Object, ServletContext, String)} is used
     */
    @Deprecated
    public static void deleteHelp(String field, String search, ServletContext sc){
    	Term term = new Term(field,search);
    	Directory directory;
    	IndexReader indexReader;

    	try {
    		indexReader = IndexReader.open(sc.getRealPath("/") + HELP_INDEX_DIRECTORY);
    		Integer deleted = indexReader.deleteDocuments(term);
    		indexReader.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

}
