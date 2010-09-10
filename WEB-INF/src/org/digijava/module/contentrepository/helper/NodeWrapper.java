package org.digijava.module.contentrepository.helper;

import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Workspace;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.helper.ActivityDocumentsUtil;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.exception.CrException;
import org.digijava.module.contentrepository.form.DocumentManagerForm;
import org.digijava.module.contentrepository.helper.template.PdfFileHelper;
import org.digijava.module.contentrepository.helper.template.WordOrPdfFileHelper;
import org.digijava.module.contentrepository.jcrentity.Label;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

public class NodeWrapper {
	
	private static Logger logger	= Logger.getLogger(NodeWrapper.class);
	
	private Node node;
	private boolean errorAppeared	= false;
	
	public NodeWrapper(Node node) {
		this.node	= node;
	}
	
	public NodeWrapper(DocumentManagerForm myForm, HttpServletRequest myRequest, Node parentNode,boolean isANewVersion, ActionMessages errors) {
		
		FormFile formFile		= myForm.getFileData();		
		boolean isAUrl			= false;
		if ( myForm.getWebLink() != null && myForm.getWebLink().length() > 0 )
			isAUrl				= true;
		
		try {
			TeamMember teamMember		= (TeamMember)myRequest.getSession().getAttribute(Constants.CURRENT_MEMBER);
			Node newNode 	= null;
			long docType = 0;
			if (isANewVersion){
				Property docTypeProp = parentNode.getProperty(CrConstants.PROPERTY_CM_DOCUMENT_TYPE);
				docType = docTypeProp.getLong();
				newNode		= parentNode;
				newNode.checkout();
			}
			else{
				String encTitle	= URLEncoder.encode(myForm.getDocTitle(), "UTF-8");
				docType = myForm.getDocType();
				newNode	= parentNode.addNode( encTitle );
				newNode.addMixin("mix:versionable");
			}
			
			
			if (isANewVersion){
				int vernum	= DocumentManagerUtil.getNextVersionNumber( newNode.getUUID(), myRequest);
				newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double)vernum);
			}
			else{
				newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double)1.0);
			}
			
			String contentType			= null;
			//HashMap errors = new HashMap();
			if ( isAUrl ){
				String link				= DocumentManagerUtil.processUrl(myForm.getWebLink(), myForm);
				if (link != null) {
					newNode.setProperty ( CrConstants.PROPERTY_WEB_LINK, link );
					contentType				= CrConstants.URL_CONTENT_TYPE;
				}
				else
					errorAppeared	= true;
			}
			else{
				//System.out.println("NodeWrapper.NodeWrapper() 1");
				if ( !DocumentManagerUtil.checkFileSize(formFile, errors) ) {
					errorAppeared	= true;
				}
				else {
					newNode.setProperty(CrConstants.PROPERTY_DATA, formFile.getInputStream());
					
					contentType				= formFile.getContentType();
					int uploadedFileSize	= formFile.getFileSize(); // This is in bytes
					
					newNode.setProperty( CrConstants.PROPERTY_NAME, new String(formFile.getFileName().getBytes("iso-8859-1"), "UTF8"));
					newNode.setProperty( CrConstants.PROPERTY_FILE_SIZE, uploadedFileSize );
				}
			}
			
			if ( !errorAppeared ) {
				Calendar yearOfPublicationDate=null;
				Long selYearOfPublication=myForm.getYearOfPublication();
				if(selYearOfPublication!=null && selYearOfPublication.intValue()!=-1){
					yearOfPublicationDate=Calendar.getInstance();
					yearOfPublicationDate.set(selYearOfPublication.intValue(), 1, 1);
				}				
				populateNode(isANewVersion, newNode, myForm.getDocTitle(), myForm.getDocDescription(), myForm.getDocNotes(), 
					contentType, docType , teamMember.getEmail(), teamMember.getTeamId(),yearOfPublicationDate);
			}
			
			this.node		= newNode;

		} catch(RepositoryException e) {
			ActionMessage error	= new ActionMessage("error.contentrepository.addFile:badPath");
			errors.add("title",error);
			e.printStackTrace();
			errorAppeared	= true;
		} 
		catch (Exception e) {
			e.printStackTrace();
			errorAppeared	= true;
		}		
	}
	
	/**
	 * create document from template
	 */
	public NodeWrapper(WordOrPdfFileHelper pdfOrWordFile,  HttpServletRequest myRequest, Node parentNode,boolean isANewVersion, ActionMessages errors) {
			
		try {
			TeamMember teamMember		= (TeamMember)myRequest.getSession().getAttribute(Constants.CURRENT_MEMBER);
			Node newNode 	= null;
			long docType = 0;
				String encTitle	= pdfOrWordFile.getDocTitle(); //URLEncoder.encode("Simple Test", "UTF-8");
				docType = new Long(0);
				newNode	= parentNode.addNode( encTitle );
				newNode.addMixin("mix:versionable");
			
			if (isANewVersion){
				int vernum	= DocumentManagerUtil.getNextVersionNumber( newNode.getUUID(), myRequest);
				newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double)vernum);
			}
			else{
				newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double)1.0);
			}
			
			newNode.setProperty(CrConstants.PROPERTY_DATA, pdfOrWordFile.getContent());
			int uploadedFileSize	=pdfOrWordFile.getFileSize();
			String fileName =pdfOrWordFile.getDocTitle()+"."+pdfOrWordFile.getFileType();
			newNode.setProperty( CrConstants.PROPERTY_NAME, new String(fileName.getBytes("iso-8859-1"), "UTF8"));
			newNode.setProperty( CrConstants.PROPERTY_FILE_SIZE, uploadedFileSize );
			
			if ( !errorAppeared ) {
				Calendar yearOfPublicationDate=null;								
				populateNode(isANewVersion, newNode, encTitle, null, null,pdfOrWordFile.getContentType(), docType , teamMember.getEmail(), teamMember.getTeamId(),yearOfPublicationDate);
			}
			
			this.node		= newNode;

		} catch(RepositoryException e) {
			ActionMessage error	= new ActionMessage("error.contentrepository.addFile:badPath");
			errors.add("title",error);
			e.printStackTrace();
			errorAppeared	= true;
		} 
		catch (Exception e) {
			e.printStackTrace();
			errorAppeared	= true;
		}
	}
	
	/**
	 * 
	 * @param myRequest
	 * @param parentNode
	 * @param originalNode from which resource we are making new copy
	 */
	public NodeWrapper(HttpServletRequest myRequest,Node parentNode,Node originalNode) {
		try {
//			TeamMember teamMember		= (TeamMember)myRequest.getSession().getAttribute(Constants.CURRENT_MEMBER);
//			long docType=originalNode.getProperty(CrConstants.PROPERTY_CM_DOCUMENT_TYPE).getLong();
//			String docTitle=originalNode.getProperty(CrConstants.PROPERTY_TITLE).getString();
//			
//			Node newNode=parentNode.addNode(docTitle);
//			newNode.addMixin("mix:versionable");
//			newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double)1.0);
//			//content type and content
//			String contentType			= originalNode.getProperty(CrConstants.PROPERTY_CONTENT_TYPE).getString();
//			if(originalNode.hasProperty(CrConstants.PROPERTY_WEB_LINK)){
//				newNode.setProperty ( CrConstants.PROPERTY_WEB_LINK, originalNode.getProperty(CrConstants.PROPERTY_WEB_LINK).getValue());
//			}else{
//				newNode.setProperty(CrConstants.PROPERTY_DATA, originalNode.getProperty(CrConstants.PROPERTY_DATA).getValue());
//				newNode.setProperty( CrConstants.PROPERTY_NAME, originalNode.getProperty(CrConstants.PROPERTY_NAME).getValue());
//				newNode.setProperty( CrConstants.PROPERTY_FILE_SIZE, originalNode.getProperty(CrConstants.PROPERTY_FILE_SIZE).getValue() );
//			}
//			String description=originalNode.getProperty(CrConstants.PROPERTY_DESCRIPTION).getString();
//			if(description!=null){
//				description=URLDecoder.decode(description, "UTF-8");
//			}			 
//			String docNotes=originalNode.getProperty(CrConstants.PROPERTY_NOTES).getString();
//			if(docNotes!=null){
//				description=URLDecoder.decode(docNotes, "UTF-8");
//			}
//			
//			//year of publication
//			Calendar yearOfPublication=null;
//			if(originalNode.hasProperty(CrConstants.PROPERTY_YEAR_OF_PUBLICATION)){
//				yearOfPublication=originalNode.getProperty(CrConstants.PROPERTY_YEAR_OF_PUBLICATION).getDate();
//			}
//			
//			populateNode(false, newNode, URLDecoder.decode(docTitle, "UTF-8"), description, docNotes,contentType, docType , teamMember.getEmail(), teamMember.getTeamId(),yearOfPublication );
//			this.node		= newNode;
			Node newNode=buildNewNode(myRequest, parentNode, originalNode, false);
			this.node		= newNode;
		} catch (Exception e) {
			e.printStackTrace();
			errorAppeared	= true;
		}		
	}
	
	private Node buildNewNode(HttpServletRequest myRequest, Node parentNode,Node originalNode,boolean isANewVersion) {
		try {
			TeamMember teamMember		= (TeamMember)myRequest.getSession().getAttribute(Constants.CURRENT_MEMBER);
			Node newNode 	= null;
			String docTitle=originalNode.getProperty(CrConstants.PROPERTY_TITLE).getString();
			long docType = 0;
			
			if (isANewVersion){
				Property docTypeProp = parentNode.getProperty(CrConstants.PROPERTY_CM_DOCUMENT_TYPE);
				docType = docTypeProp.getLong();
				newNode		= parentNode;
				newNode.checkout();
			}
			else{				
				docType = originalNode.getProperty(CrConstants.PROPERTY_CM_DOCUMENT_TYPE).getLong();
				newNode	= parentNode.addNode( docTitle );
				newNode.addMixin("mix:versionable");
			}			
			
			if (isANewVersion){
				int vernum	= DocumentManagerUtil.getNextVersionNumber( newNode.getUUID(), myRequest);
				newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double)vernum);
			}
			else{
				newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double)1.0);
			}
			
			//content type and content
			String contentType			= originalNode.getProperty(CrConstants.PROPERTY_CONTENT_TYPE).getString();
			if(originalNode.hasProperty(CrConstants.PROPERTY_WEB_LINK)){
				newNode.setProperty ( CrConstants.PROPERTY_WEB_LINK, originalNode.getProperty(CrConstants.PROPERTY_WEB_LINK).getValue());
			}else{
				newNode.setProperty(CrConstants.PROPERTY_DATA, originalNode.getProperty(CrConstants.PROPERTY_DATA).getValue());
				newNode.setProperty( CrConstants.PROPERTY_NAME, originalNode.getProperty(CrConstants.PROPERTY_NAME).getValue());
				newNode.setProperty( CrConstants.PROPERTY_FILE_SIZE, originalNode.getProperty(CrConstants.PROPERTY_FILE_SIZE).getValue() );
			}
			String description=originalNode.getProperty(CrConstants.PROPERTY_DESCRIPTION).getString();
			if(description!=null){
				description=URLDecoder.decode(description, "UTF-8");
			}			 
			String docNotes=originalNode.getProperty(CrConstants.PROPERTY_NOTES).getString();
			if(docNotes!=null){
				description=URLDecoder.decode(docNotes, "UTF-8");
			}
			
			//year of publication
			Calendar yearOfPublication=null;
			if(originalNode.hasProperty(CrConstants.PROPERTY_YEAR_OF_PUBLICATION)){
				yearOfPublication=originalNode.getProperty(CrConstants.PROPERTY_YEAR_OF_PUBLICATION).getDate();
			}
			
			populateNode(isANewVersion, newNode, URLDecoder.decode(docTitle, "UTF-8"), description, docNotes,contentType, docType , teamMember.getEmail(), teamMember.getTeamId(),yearOfPublication );
			return newNode;
			// this.node		= newNode;
		} catch (Exception e) {
			e.printStackTrace();
			errorAppeared	= true;
		}
		return null;
	}
	
	public NodeWrapper(HttpServletRequest myRequest, Node parentNode,Node originalNode,boolean isANewVersion) {
		try {
			Node newNode=buildNewNode(myRequest, parentNode, originalNode, isANewVersion);
			this.node		= newNode;
		} catch (Exception e) {
			e.printStackTrace();
			errorAppeared	= true;
		}
	}	
	
	
	public NodeWrapper(TemporaryDocumentData tempDoc, HttpServletRequest myRequest, Node parentNode,boolean isANewVersion, ActionMessages errors) {
		
		FormFile formFile		= tempDoc.getFormFile(); 
		
		boolean isAUrl			= false;
		if ( tempDoc.getWebLink()!=null && tempDoc.getWebLink().length()>0 ){
//			if (tempDoc.getWebLink().indexOf("http://") >= 0){
//				tempDoc.setWebLink(tempDoc.getWebLink().replaceFirst("http://", ""));
//			}
			if (tempDoc.getName().indexOf("http://") >= 0){
				tempDoc.setName(tempDoc.getName().replaceFirst("http://", ""));
			}
			if (tempDoc.getTitle().indexOf("http://") >= 0){
				tempDoc.setTitle(tempDoc.getTitle().replaceFirst("http://", ""));
			}
			isAUrl				= true;
		}
		
		try {
			TeamMember teamMember		= (TeamMember)myRequest.getSession().getAttribute(Constants.CURRENT_MEMBER);
			Node newNode 	= null;
			if (isANewVersion){
				newNode		= parentNode;
				newNode.checkout();
			}
			else{
				String encTitle	= URLEncoder.encode(tempDoc.getTitle(), "UTF-8");
				newNode	= parentNode.addNode(encTitle);
				newNode.addMixin("mix:versionable");
			}
			
			if (isANewVersion){
				int vernum	= DocumentManagerUtil.getNextVersionNumber( newNode.getUUID(), myRequest);
				newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double)vernum);
			}
			else{
				newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double)1.0);
			}
			
			String contentType			= null;
			//HashMap errors = new HashMap();
			if ( isAUrl ){
				contentType				= CrConstants.URL_CONTENT_TYPE;
				newNode.setProperty ( CrConstants.PROPERTY_WEB_LINK, tempDoc.getWebLink() );
			}
			else{
				//System.out.println("NodeWrapper.NodeWrapper() 2");
				if(formFile != null){
					
					if ( !DocumentManagerUtil.checkFileSize(formFile, errors) ) {
						errorAppeared	= true;
					}
					else {
						newNode.setProperty(CrConstants.PROPERTY_DATA, formFile.getInputStream());
						contentType				= formFile.getContentType();
						int uploadedFileSize	= formFile.getFileSize(); // This is in bytes
						//AMP-3468
						newNode.setProperty( CrConstants.PROPERTY_NAME, new String(formFile.getFileName().getBytes("iso-8859-1"), "UTF8") );
						newNode.setProperty( CrConstants.PROPERTY_FILE_SIZE, uploadedFileSize );
					}
				}
				else logger.error("Form file is null. It is ok if it imported using IDML");
			}
			
			if ( !errorAppeared ) {
				Calendar yearofPublicationDate=null;
				if(tempDoc.getYearofPublication()!=null){
					Integer yearofPublication = new Integer(tempDoc.yearofPublication);
					yearofPublicationDate= Calendar.getInstance();
					yearofPublicationDate.set(yearofPublication.intValue(), 1, 1);
				}
				populateNode(isANewVersion,newNode, tempDoc.getTitle(), tempDoc.getDescription(), tempDoc.getNotes(), 
					contentType, tempDoc.getCmDocTypeId(), teamMember.getEmail(), teamMember.getTeamId(), yearofPublicationDate );
			} 
			
			this.node		= newNode;

		} catch(RepositoryException e) {
			ActionMessage error	= 
				new ActionMessage("error.contentrepository.addFile.badPath", "Error adding new document. Please make sure you specify a valid path to the local file and the file is not empty."); 
			errors.add("title",error);
			e.printStackTrace();
			errorAppeared	= true;
		} 
		catch (Exception e) {
			e.printStackTrace();
			errorAppeared	= true;
		}
		
	}
	
	private void populateNode(boolean isANewVersion,Node newNode, String docTitle, String docDescr, String docNotes, String contentType, Long cmDocType, 
			String user, Long teamId,Calendar yearOfPublication) {
		try{
			if (!isANewVersion) {
				newNode.setProperty( CrConstants.PROPERTY_CREATOR, user );
				newNode.setProperty( CrConstants.PROPERTY_CREATOR_TEAM, teamId);
			}
			if ( docDescr == null )
				docDescr = "";
			if ( docNotes == null )
				docNotes = "";
			
			String encTitle		= URLEncoder.encode(docTitle, "UTF-8");
			String encDescr		= URLEncoder.encode(docDescr, "UTF-8");
			String encNotes		= URLEncoder.encode(docNotes, "UTF-8");
			
			newNode.setProperty( CrConstants.PROPERTY_TITLE, encTitle );
			newNode.setProperty( CrConstants.PROPERTY_DESCRIPTION, encDescr );
			newNode.setProperty( CrConstants.PROPERTY_NOTES, encNotes );
			newNode.setProperty( CrConstants.PROPERTY_CONTENT_TYPE, contentType );
			
			Node labelContainerNode	= newNode.addNode( CrConstants.LABEL_CONTAINER_NODE_NAME );
			labelContainerNode.addMixin("mix:versionable");
			
			if(cmDocType != null) newNode.setProperty( CrConstants.PROPERTY_CM_DOCUMENT_TYPE, cmDocType );
			else logger.error("Doctype is null. It is ok if the file is importing using IDML");
			newNode.setProperty( CrConstants.PROPERTY_ADDING_DATE, Calendar.getInstance());
			//year of publication
			if(yearOfPublication!=null){				
				newNode.setProperty(CrConstants.PROPERTY_YEAR_OF_PUBLICATION, yearOfPublication);
			}			
			newNode.setProperty( CrConstants.PROPERTY_VERSION_CREATOR, user );
			newNode.setProperty( CrConstants.PROPERTY_VERSION_CREATOR_TEAM, teamId);
		}
		catch (Exception e) {
			e.printStackTrace();
			errorAppeared	= true;
		}
	}

	public boolean isErrorAppeared() {
		return errorAppeared;
	}

	public void setErrorAppeared(boolean errorAppeared) {
		this.errorAppeared = errorAppeared;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}
	
	public boolean saveNode( Session jcrWriteSession ) {
		try {
			jcrWriteSession.save();
			node.checkin();
			return true;
		}
		catch (Exception E) {
			E.printStackTrace();
			return false;
		}

	}
	
	public String getUuid () {
		try {
			return node.getUUID();
		} catch (UnsupportedRepositoryOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String getTitle() {
		Property title		=  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_TITLE);
		if ( title != null ) {
			try {
				String ret	= URLDecoder.decode( title.getString() ,"UTF-8");
				return ret;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return null;
	}
	
	public String getDescription() {
		Property description	=  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_DESCRIPTION);
		if ( description != null ) {
			try {
				String ret	= URLDecoder.decode( description.getString() ,"UTF-8");
				return ret;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return null;
	}
	
	public String getNotes() {
		Property notes	=  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_NOTES);
		if ( notes != null ) {
			try {
				String ret	= URLDecoder.decode( notes.getString() ,"UTF-8");
				return ret;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return null;
	}
	public String getDate() {
		Property calProperty	=  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_ADDING_DATE);
		if ( calProperty != null ) {
			try {
				Calendar cal 	= calProperty.getDate();
				return DocumentManagerUtil.calendarToString(cal,false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return null;
	}
	
	public String getYearOfPublication() {
		Property calProperty	=  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_YEAR_OF_PUBLICATION);
		if ( calProperty != null ) {
			try {
				Calendar cal 	= calProperty.getDate();
				return DocumentManagerUtil.calendarToString(cal,true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return null;
	}
	
	public Calendar getCalendarDate() {
		Property calProperty	=  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_ADDING_DATE);
		if ( calProperty != null ) {
			try {
				Calendar cal 	= calProperty.getDate();
				return cal;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return null;
	}
	
	
	public double getFileSizeInMegabytes() {
		Property fileSize	=  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_FILE_SIZE);
		if ( fileSize != null ) {
			try {
				double size		= DocumentManagerUtil.bytesToMega( fileSize.getLong() );
				return size;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return 0;
	}
	
	public String getContentType() {
		Property contentType	=  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_CONTENT_TYPE);
		if ( contentType != null ) {
			try {
				return contentType.getString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return null;
	}
	
	public float getVersionNumber() {
		Property versionNumber		=  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_VERSION_NUMBER);
		if ( versionNumber != null ) {
			try {
				return (float)versionNumber.getDouble();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return 0;
	}
	
	public String getName() {
		Property name	=  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_NAME);
		if ( name != null ) {
			try {
				return name.getString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return null;
	}
	
	public String getWebLink() {
		Property webLinkProp		= DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_WEB_LINK);
		if ( webLinkProp != null ) {
			try{
				return webLinkProp.getString(); 
			}
			catch ( Exception E ) {
				E.printStackTrace();
			}
		}
		return null;
	}
	
	public Long getCmDocTypeId() {
		Property docType			= DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_CM_DOCUMENT_TYPE);
		if ( docType != null ) {
			try{
				return docType.getLong(); 
			}
			catch ( Exception E ) {
				E.printStackTrace();
			}
		}
		return null;
	}
	
	public Collection<KeyValue> getObjectsUsingThisDocument () throws Exception {
		Collection<KeyValue> ret	= new ArrayList<KeyValue>();
		if ( this.node == null )
			throw new Exception("Inner node not initialized");
		
		Collection<String> names	= ActivityDocumentsUtil.getNamesOfActForDoc( node.getUUID() );
		
		ret							= stringColToKeyValueCol("Activities", names);
		
		return ret;
		
		
	} 
	
	public String getCreator() {
		Property creator		=  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_CREATOR);
		if ( creator != null ) {
			try {
				return creator.getString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return null;
	}
	public Long getCreatorTeam() {
		Property creatorTeam		=  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_CREATOR_TEAM);
		if ( creatorTeam != null ) {
			try {
				return creatorTeam.getLong();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return null;
	}
	public List<Label> getLabels() {
		ArrayList<Label> labels		= new ArrayList<Label>();
		try {
			Node labelContainerNode		= node.getNode( CrConstants.LABEL_CONTAINER_NODE_NAME );
			PropertyIterator pIter		= labelContainerNode.getProperties();
			while ( pIter.hasNext() ) {
				Property p			= pIter.nextProperty();
				if ( p.getName().contains("ampdoc:label") ) {
					Node labelNode		= p.getNode();
					labels.add( new Label(labelNode) );
				}
			}
		} catch (Exception e) {
			logger.warn("Document " + this.getName() + " has no label container node");
			//e.printStackTrace();
		}
		return labels;
	}
	public void addLabel(Node label) {
		try {
			Node labelContainerNode		= node.getNode( CrConstants.LABEL_CONTAINER_NODE_NAME );
			PropertyIterator pIter		= labelContainerNode.getProperties();
			Long maxNumber				= 0L;
			while ( pIter.hasNext() ) {
				Property p			= pIter.nextProperty();
				try {
					if ( p.getName().contains("ampdoc:label") ) {
						Long number			= Long.parseLong( p.getName().substring("ampdoc:label".length() ) );
						maxNumber			= (number>maxNumber)?number:maxNumber;
					}
				}
				catch (NumberFormatException e) {
					logger.error("Was trying to parse " + p.getName() );
				}
			}
			maxNumber ++;
			labelContainerNode.checkout();
			labelContainerNode.setProperty("ampdoc:label" + maxNumber, label);
			labelContainerNode.save();
			labelContainerNode.checkin();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void removeLabel(String labelUUID) {
		try {
			Node labelContainerNode		= node.getNode( CrConstants.LABEL_CONTAINER_NODE_NAME );
			PropertyIterator pIter		= labelContainerNode.getProperties();
			while ( pIter.hasNext() ) {
				Property p			= pIter.nextProperty();
				if ( p.getName().contains("ampdoc:label") ) {
					Node labelNode		= p.getNode();
					if ( labelNode.getUUID().equals(labelUUID) ) {
						labelContainerNode.checkout();
						p.remove();
						break;
					}
				}
			}
			labelContainerNode.save();
			labelContainerNode.checkin();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Boolean isTeamDocument() {
		try {
			Workspace workspace			= node.getSession().getWorkspace();
			String path					= node.getCorrespondingNodePath( workspace.getName() );
			if ( path.contains(CrConstants.TEAM_PATH_ITEM) ) {
				return new Boolean(true);
			}
			else 
				return new Boolean(false);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public Boolean deleteNode(HttpServletRequest request) throws Exception  {
		String uuid		= node.getUUID();
		Boolean ret		= DocumentManagerUtil.deleteDocumentWithRightsChecking( uuid, request);
		
		DocumentManagerUtil.deleteObjectsReferringDocument(uuid, CrDocumentNodeAttributes.class.getName() );
		int delActivityDocs	= DocumentManagerUtil.deleteObjectsReferringDocument(uuid, AmpActivityDocument.class.getName() );
		if ( delActivityDocs > 0 ) {
			logger.error(delActivityDocs + " AmpActivityDocument object have been deleted on deletion of referring node. " +
					"Deletion of this node should not have been allowed.");
		}
		//delete all approved/unapproved versions and sharing records
		DocumentManagerUtil.deleteNodeStates(uuid);
		DocumentManagerUtil.deleteAllShareRecordsrelatedToResource(uuid);
		return ret;
	}
	
	public String getLastVersionUUID(HttpServletRequest request) {
		try {
			Node lv	=	DocumentManagerUtil.getNodeOfLastVersion(this.getUuid(), request);
			return lv.getUUID();
		} catch (Exception e) {
			return null;
		}
	}
	
	private static Collection<KeyValue> stringColToKeyValueCol(String key, Collection<String> names) {
		Collection<KeyValue> ret	= new ArrayList<KeyValue>(names.size());
		Iterator<String> iter	= names.iterator();
		while ( iter.hasNext() ) {
			KeyValue kv	= new KeyValue( key, iter.next() );
			ret.add(kv);
		}
		
		return ret;
	}
	

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeWrapper other = (NodeWrapper) obj;
		if (node == null) {
			if (other.node != null)
				return false;
		} else
			try {
				if (!node.getUUID().equals(other.node.getUUID()))
					return false;
			} catch (UnsupportedRepositoryOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return true;
	}
	
}
