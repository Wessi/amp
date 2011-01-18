package org.digijava.module.contentrepository.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.RepairDbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.dbentity.NodeLastApprovedVersion;
import org.digijava.module.contentrepository.dbentity.TeamNodePendingVersion;
import org.digijava.module.contentrepository.exception.CrException;
import org.digijava.module.contentrepository.form.DocumentManagerForm;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;
import org.digijava.module.contentrepository.util.DocumentManagerRights;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

public class DocumentManager extends Action {
	
	private static Logger logger		= Logger.getLogger(DocumentManager.class);
	private boolean showOnlyLinks		= false;
	private boolean showOnlyDocs		= false;

	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception{

		ActionErrors errors					= new ActionErrors();
		DocumentManagerForm myForm		= (DocumentManagerForm) form;
		
		request.setAttribute("ServletContext", this.getServlet().getServletContext() );		
		if ( request.getParameter(CrConstants.REQUEST_GET_SHOW_DOCS) != null )
			showOnlyDocs 	= true;
		else
			showOnlyDocs	= false;
		if ( request.getParameter(CrConstants.REQUEST_GET_SHOW_LINKS) != null )
			showOnlyLinks 	= true;
		else
			showOnlyLinks	= false;
				
		
		if (  myForm.getAjaxDocumentList() ) {
			ajaxDocumentList(request, myForm);
			return mapping.findForward("ajaxDocumentList");
		}
		
		if ( !isLoggeedIn(request) ) {
			return mapping.findForward("publicView");
		}
		
		//set years
		myForm.setYears(new ArrayList<Long>());
	    Long yearFrom = Long.parseLong(FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.YEAR_RANGE_START));
	    Long countYear = Long.parseLong(FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
	    for (long i = yearFrom; i <= (yearFrom + countYear); i++) {
	      	myForm.getYears().add(new Long(i));
	    }

		showContentRepository(request, myForm, errors);
		
		this.saveErrors(request, errors);
		
		return mapping.findForward("forward");
	}
	
	private boolean ajaxDocumentList(HttpServletRequest myRequest, DocumentManagerForm myForm) {
		// UGLY HACK. This needs to be re-written
		if ( myRequest.getHeader("referer")!=null && myRequest.getHeader("referer").contains("documentManager.do") ) {
			myRequest.setAttribute("checkBoxToHide", true);
		}
		
		boolean showActionsButtons=true;
		if(myForm.getShowActions()!=null && ! myForm.getShowActions()){
			showActionsButtons=false;
		}
		
		Session jcrWriteSession			= 	DocumentManagerUtil.getWriteSession(myRequest);
		if ( !isLoggeedIn(myRequest) || myRequest.getParameter(CrConstants.GET_PUBLIC_DOCUMENTS) != null ) {
			HashMap<String, CrDocumentNodeAttributes> uuidMap		= CrDocumentNodeAttributes.getPublicDocumentsMap(true);
			try {
				Collection<DocumentData> otherDocuments = this.getDocuments( uuidMap.keySet(), myRequest ,CrConstants.PUBLIC_DOCS_TAB,false,showActionsButtons);
				myForm.setOtherDocuments( otherDocuments );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (myForm.getDocListInSession() != null) {
			HashSet<String> UUIDs				= SelectDocumentDM.getSelectedDocsSet(myRequest, myForm.getDocListInSession(), true);
			Collection<DocumentData> tempCol	= TemporaryDocumentData.retrieveTemporaryDocDataList(myRequest);
			if (UUIDs != null)
				try {
					Collection<DocumentData> documents = this.getDocuments(UUIDs, myRequest,null,false,showActionsButtons);
					myForm.setOtherDocuments(documents);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				try{	
					if ( tempCol != null ) {
						if ( myForm.getOtherDocuments() == null ) {
							myForm.setOtherDocuments( tempCol );
						}
						else
							myForm.getOtherDocuments().addAll(tempCol);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		//for selectDocumentDM
		if ( myForm.getOtherUsername() != null && myForm.getOtherTeamId() != null ) {
			TeamMember	otherTeamMember		= null;
			Collection otherTeamMembers		= TeamMemberUtil.getTMTeamMembers( myForm.getOtherUsername() );
			
			Iterator iterator				= otherTeamMembers.iterator();
			
			while ( iterator.hasNext() ) {
				TeamMember someTeamMember	= (TeamMember) iterator.next(); 
				if ( someTeamMember.getTeamId().longValue() == myForm.getOtherTeamId().longValue() ) {
					otherTeamMember		= someTeamMember;
					break;
				}
			}
			
			if (otherTeamMember != null) {
				Node otherHomeNode				= DocumentManagerUtil.getUserPrivateNode(jcrWriteSession , otherTeamMember );
				myForm.setOtherDocuments( this.getDocuments(otherHomeNode, myRequest,CrConstants.PRIVATE_DOCS_TAB,false,showActionsButtons) );
			}
		}
		if ( myForm.getOtherUsername() == null && myForm.getOtherTeamId() != null ) {
			TeamMember otherTeamLeader			= TeamMemberUtil.getTMTeamHead( myForm.getOtherTeamId() );
			Node otherHomeNode					= DocumentManagerUtil.getTeamNode(jcrWriteSession, otherTeamLeader);
			myForm.setOtherDocuments( this.getDocuments(otherHomeNode, myRequest,CrConstants.TEAM_DOCS_TAB,false,showActionsButtons) );			
		}
		//shared documents
		if(myForm.getShowSharedDocs()!=null){
			myForm.setOtherDocuments( this.getSharedDocuments(getCurrentTeamMember(myRequest), myRequest,showActionsButtons));
		}
		return false;
	}
	
	private boolean showContentRepository(HttpServletRequest request, DocumentManagerForm myForm, ActionErrors errors) {
		try {
			
			HttpSession	httpSession		= request.getSession();
			TeamMember teamMember		= (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
			
			myForm.setTeamMember(teamMember);
			myForm.setTeamLeader( teamMember.getTeamHead() );
			myForm.setTeamMembers( TeamMemberUtil.getAllTeamMembers(teamMember.getTeamId()) );
			
			// AMP-8791: "resourcesTab" is set only when the document is
			// shared/unshared.
			// Then the attribute is deleted from session. If there is no
			// resourceTab and no type set then select the first tab.
			if (httpSession.getAttribute("resourcesTab") == null
					|| httpSession.getAttribute("resourcesTab").toString().equals("")) {
				if (myForm.getType() == null || myForm.getType().equals("")) {
					myForm.setType("private");
				}
			} else {
				myForm.setType(httpSession.getAttribute("resourcesTab").toString());
				httpSession.removeAttribute("resourcesTab");
			}
						
			if (teamMember == null) {
				throw new Exception("No TeamMember found in HttpSession !");
			}
			Session jcrWriteSession		= DocumentManagerUtil.getWriteSession(request);
			
			
			if ( myForm.getType() != null && myForm.getType().equals("private") ) {
				if (myForm.getFileData() != null || myForm.getWebLink() != null) {
					Node userHomeNode			= DocumentManagerUtil.getUserPrivateNode(jcrWriteSession, teamMember);
					NodeWrapper nodeWrapper		= new NodeWrapper(myForm, request, userHomeNode, false, errors);
					if ( nodeWrapper != null && !nodeWrapper.isErrorAppeared() )
							nodeWrapper.saveNode(jcrWriteSession);
				}
			}
			if ( myForm.getType() != null && myForm.getType().equals("team") && DocumentManagerRights.hasAddResourceToTeamResourcesRights(request) ) {
				
				if (myForm.getFileData() != null || myForm.getWebLink() != null) {
					Node teamHomeNode			= DocumentManagerUtil.getTeamNode(jcrWriteSession, teamMember);
					NodeWrapper nodeWrapper		= new NodeWrapper(myForm, request, teamHomeNode , false, errors);
					if ( nodeWrapper != null && !nodeWrapper.isErrorAppeared() ) {
						nodeWrapper.saveNode(jcrWriteSession);
					}
					//update team's last approved version id- If new team document is created,it's uuid is last approved
					createVersionApprovalStatus(request,true,nodeWrapper);
				}
			}
			if ( myForm.getType() != null && myForm.getType().equals("version") && myForm.getUuid() != null ) {
				if (myForm.getFileData() != null || myForm.getWebLink() != null) {
					Node vNode		= DocumentManagerUtil.getWriteNode(myForm.getUuid(), request);
					/**
					 * approval is not needed for version,if current member is TL, or he is creator of this node(base node,not version)
					 * or if tm's are allowed to add versions
					 */
					Boolean hasVersioningRightsWithoutApprovalNeeded=DocumentManagerRights.hasVersioningRights(vNode, request);
					NodeWrapper nodeWrapper		= new NodeWrapper(myForm, request, vNode , true, errors);
					if ( nodeWrapper != null && !nodeWrapper.isErrorAppeared() ) {
						nodeWrapper.saveNode(jcrWriteSession);
						if ( nodeWrapper.isTeamDocument() ) {
							myForm.setType("team");
							createVersionApprovalStatus(request,hasVersioningRightsWithoutApprovalNeeded,nodeWrapper);							
						}
					}					
				}
			}
			myForm.setYearOfPublication(null);
			myForm.setMyPersonalDocuments(  this.getPrivateDocuments(teamMember, jcrWriteSession.getRootNode(), request)  );
			myForm.setMyTeamDocuments( this.getTeamDocuments(teamMember, jcrWriteSession.getRootNode(), request) );
			myForm.setSharedDocuments(this.getSharedDocuments(teamMember, request,true));
		}catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void createVersionApprovalStatus(HttpServletRequest request,Boolean hasVersioningRightsWithoutApprovalNeeded,NodeWrapper nodeWrapper)
			throws UnsupportedRepositoryOperationException,	RepositoryException, CrException, Exception {
		if(hasVersioningRightsWithoutApprovalNeeded){
			//update team's last approved version id- If new team document is created,it's uuid is last approved
			String lastApprovedNodeVersionUUID=DocumentManagerUtil.getNodeOfLastVersion(nodeWrapper.getUuid(), request).getUUID();
			NodeLastApprovedVersion lastAppVersion=DocumentManagerUtil.getlastApprovedVersionOfTeamNode(nodeWrapper.getUuid());
			if(lastAppVersion!=null){
				lastAppVersion.setVersionID(lastApprovedNodeVersionUUID);
			}else{
				lastAppVersion=new NodeLastApprovedVersion(nodeWrapper.getUuid(), lastApprovedNodeVersionUUID);
			}					
			DbUtil.saveOrUpdateObject(lastAppVersion);
		}else{
			//version is unapproved
			String lastVersionOfTheNode=DocumentManagerUtil.getNodeOfLastVersion(nodeWrapper.getUuid(), request).getUUID();
			TeamNodePendingVersion pendingVersion=new TeamNodePendingVersion(nodeWrapper.getUuid(),lastVersionOfTheNode);
			DbUtil.saveOrUpdateObject(pendingVersion);
		}
	}

	private Collection getPrivateDocuments(TeamMember teamMember, Node rootNode, HttpServletRequest request) {
		ArrayList<DocumentData> documents = new ArrayList<DocumentData>();
		Node userNode;
		try {
			userNode	= DocumentManagerUtil.getUserPrivateNode(rootNode.getSession(), teamMember);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return getDocuments(userNode, request,CrConstants.PRIVATE_DOCS_TAB,false,true);
	}
	
	private Collection getTeamDocuments(TeamMember teamMember, Node rootNode, HttpServletRequest request) {
		Node teamNode;
		Collection<DocumentData> pendingResources=null;
		Collection<DocumentData> retVal=null;
		ArrayList<DocumentData> documents	= new ArrayList<DocumentData>();
		try {
			teamNode	= DocumentManagerUtil.getTeamNode(rootNode.getSession(), teamMember);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		//all docs that are originated by this team
		Collection<DocumentData> teamDocs=getDocuments(teamNode, request,CrConstants.TEAM_DOCS_TAB,false,true);
		//resources pending approval
		if(teamMember.getTeamHead()){ //should see all docs that are pending approval for this team
			List<String> pendingResourcesIds=DocumentManagerUtil.getSharedNodeUUIDs(teamMember.getTeamId(), CrConstants.PENDING_STATUS);
			if(pendingResourcesIds!=null && pendingResourcesIds.size()>0){
				pendingResources = getDocuments(pendingResourcesIds,request,CrConstants.TEAM_DOCS_TAB,true,true);				
			}
		}
				
		if(teamDocs!=null || pendingResources!=null){
			retVal=new HashSet<DocumentData>();
			if(teamDocs!=null){
				retVal.addAll(teamDocs);
			}
			if(pendingResources!=null){
				retVal.addAll(pendingResources);
			}
		}
		return retVal;
	}
	
	private Collection getSharedDocuments(TeamMember teamMember, HttpServletRequest request,boolean showActionButtons) {
		Collection<DocumentData> sharedDocs=null;		
		//get all nodes that are shared to this team
		List<String> allSharedDocsIds = DocumentManagerUtil.getSharedNodeUUIDs(teamMember.getTeamId(), CrConstants.SHARED_AMONG_WORKSPACES);		
		if(allSharedDocsIds!=null){
			sharedDocs=getDocuments(allSharedDocsIds,request,CrConstants.SHARED_DOCS_TAB,false,showActionButtons);
		}
		return sharedDocs;
	}
	
	
	private Collection getDocuments(Node node, HttpServletRequest request,String tabName,boolean isPending,boolean showActionButtons) {
		try {
			NodeIterator nodeIterator	= node.getNodes();
			return getDocuments(nodeIterator, request,tabName,isPending,showActionButtons);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private Collection<DocumentData> getDocuments(Iterator nodeIterator, HttpServletRequest request,String tabName,boolean isPending,boolean showActionButtons) {
		ArrayList<DocumentData> documents										= new ArrayList<DocumentData>();
		HashMap<String,CrDocumentNodeAttributes> uuidMapOrg		= CrDocumentNodeAttributes.getPublicDocumentsMap(false);
		HashMap<String,CrDocumentNodeAttributes> uuidMapVer		= CrDocumentNodeAttributes.getPublicDocumentsMap(true);
		try {
			while ( nodeIterator.hasNext() ) {
				Node documentNode	= (Node)nodeIterator.next();
				Node baseNode=documentNode; //in case document node last version should be hidden and another should be shown
				String documentNodeBaseVersionUUID=documentNode.getUUID();
				/**
				 * If this version of node is pending to be approved, then it should be visible only to the creator of the node or the TL
				 * other users should see last approved version of this node, so get version number from sharedDoc entry and load that version
				 */
				if(tabName!=null && !tabName.equals(CrConstants.PUBLIC_DOCS_TAB) //in public docs case documentNode is non-versionable,but public version of some node 
						&& DocumentManagerUtil.isGivenVersionPendingApproval(DocumentManagerUtil.getNodeOfLastVersion(documentNodeBaseVersionUUID, request).getUUID())!=null){
					
					NodeLastApprovedVersion nlpv	= DocumentManagerUtil.getlastApprovedVersionOfTeamNode(documentNodeBaseVersionUUID);
					String sharedVersionId			= null;
					if ( nlpv != null ) {
						sharedVersionId			= nlpv.getVersionID();
					}
					else {
						Node verNode				= DocumentManagerUtil.getLastVersionNotWaitingApproval(documentNodeBaseVersionUUID, request);
						if ( verNode != null )
							sharedVersionId			= verNode.getUUID();
						else {
							logger.error( "Code should never get here. There should be at least one version of a document not waiting approval." );
							continue;
						}
							
					}
					
					documentNode = DocumentManagerUtil.getReadNode(sharedVersionId, request);
				}
				
				if(isPending){ //getting documents that need approval to become team docs
					String sharedVersionId= DocumentManagerUtil.getCrSharedDoc(documentNodeBaseVersionUUID, getCurrentTeamMember(request).getTeamId(), CrConstants.PENDING_STATUS).getSharedNodeVersionUUID();
					Node documentNodesLastVersionId = DocumentManagerUtil.getNodeOfLastVersion(documentNode.getUUID(), request);
					/**
					 * If private document wasn't yet approved to become team doc and meanwhile TM added new version to his private doc,
					 * the version which he marked as shared should be visible and not the last version of the document.
					 */
					if(! documentNodesLastVersionId.getUUID().equals(sharedVersionId)){
						documentNode = DocumentManagerUtil.getReadNode(sharedVersionId, request);
					}
				}
				
				NodeWrapper nodeWrapper	= new NodeWrapper(documentNode);
				
				if ( nodeWrapper.getWebLink()!=null && showOnlyDocs ){
					continue;
				}					
				if ( nodeWrapper.getWebLink()==null && showOnlyLinks ){
					continue;
				}
				
				Boolean hasViewRights			= false;
				Boolean hasShowVersionsRights	= false;
				Boolean hasVersioningRights		= false;
				Boolean hasDeleteRights			= false;
				Boolean hasMakePublicRights		= false;
				Boolean hasDeleteRightsOnPublicVersion	= false;
				Boolean hasApproveVersionRights 		= false;
				Boolean hasAddParticipatingOrgRights	= false;
				
				String uuid						= documentNode.getUUID();
				boolean isPublicVersion		= uuidMapVer.containsKey(uuid);
								
				if ( isPublicVersion ) { // This document is public and exactly this version is the public one
						hasViewRights			= true;
				}else{
					hasViewRights	= DocumentManagerRights.hasViewRights(baseNode, request);
				}				
				
				if ( hasViewRights == null || !hasViewRights.booleanValue() ) {
					continue;
				}
				//fill node with data
				String fileName	=  nodeWrapper.getName();
				if ( fileName == null && nodeWrapper.getWebLink() == null ){
					continue;
				}
				DocumentData documentData		= new DocumentData();
				documentData.setName( fileName );
				documentData.setUuid( documentNodeBaseVersionUUID );
				documentData.setNodeVersionUUID(nodeWrapper.getUuid()); //if it's original,node then this value is equal to documentNodeBaseVersionUUID, otherwise it's node's visible version uuid
				documentData.setTitle( nodeWrapper.getTitle() );
				documentData.setDescription( nodeWrapper.getDescription() );
				documentData.setNotes( nodeWrapper.getNotes() );
				documentData.setFileSize( nodeWrapper.getFileSizeInMegabytes() );
				documentData.setCalendar( nodeWrapper.getDate() );
				documentData.setVersionNumber( nodeWrapper.getVersionNumber() );
				documentData.setContentType( nodeWrapper.getContentType() );
				documentData.setWebLink( nodeWrapper.getWebLink() );
				documentData.setCmDocTypeId( nodeWrapper.getCmDocTypeId() );
				documentData.setYearofPublication(nodeWrapper.getYearOfPublication());
								
				documentData.process(request);
				documentData.computeIconPath(true);
				
				if ( !CrConstants.PUBLIC_DOCS_TAB.equals(tabName) && showActionButtons) {
					/**
					 * resources that are pending approval to become team resources,shouldn't have possibility to view versions,
					 * add new versions e.t.c. 
					*/
					
					hasShowVersionsRights	= DocumentManagerRights.hasShowVersionsRights(baseNode, request);
					if ( hasShowVersionsRights != null )
						documentData.setHasShowVersionsRights(hasShowVersionsRights && !isPending); 
					
					hasVersioningRights		= DocumentManagerRights.hasViewAddNewVersioninsRights(baseNode, request); //just indicates whether add version button is visible or not
					if ( hasVersioningRights != null ) {
						documentData.setHasVersioningRights( hasVersioningRights.booleanValue() && !isPending);
					}
					hasDeleteRights			= DocumentManagerRights.hasDeleteRights(baseNode, request);
					if ( hasDeleteRights != null ) {
						documentData.setHasDeleteRights( hasDeleteRights.booleanValue() && !isPending);
					}
					hasMakePublicRights		= DocumentManagerRights.hasMakePublicRights(baseNode, request);
					if ( hasMakePublicRights != null ) {
						documentData.setHasMakePublicRights( hasMakePublicRights.booleanValue() && !isPending);
					}
					
					hasAddParticipatingOrgRights	= DocumentManagerRights.hasAddParticipatingOrgRights(documentNode, request);
					if (hasAddParticipatingOrgRights != null) {
						documentData.setHasAddParticipatingOrgRights(hasAddParticipatingOrgRights);
					}
					
					hasDeleteRightsOnPublicVersion			= DocumentManagerRights.hasDeleteRightsOnPublicVersion(baseNode, request);
					if ( hasDeleteRightsOnPublicVersion != null ) {
						documentData.setHasDeleteRightsOnPublicVersion( hasDeleteRightsOnPublicVersion.booleanValue() && !isPending);
					}
					
					//share rights ! this will be different according to settings
					if(tabName!=null && ! tabName.equals(CrConstants.PUBLIC_DOCS_TAB)){
						if(tabName.equals(CrConstants.PRIVATE_DOCS_TAB) || (tabName.equals(CrConstants.TEAM_DOCS_TAB) && isPending)){
							documentData.setShareWith(CrConstants.SHAREABLE_WITH_TEAM);
						}else{
							documentData.setShareWith(CrConstants.SHAREABLE_WITH_OTHER_TEAMS);
						}
					}					
					
					//version approval rights
					hasApproveVersionRights = DocumentManagerRights.hasApproveVersionRights(request);
					if(hasApproveVersionRights!=null){
						documentData.setHasApproveVersionRights(hasApproveVersionRights);
					}
					
					//if documentNode has pending status in sharedDocs,then it should be true
					boolean needsApproval=false;
					if(tabName!=null) {
						if(tabName.equalsIgnoreCase(CrConstants.TEAM_DOCS_TAB) ) 
							needsApproval	= DocumentManagerUtil.isResourcePendingtoBeShared(documentNodeBaseVersionUUID);
							
						if ( tabName.equalsIgnoreCase(CrConstants.PRIVATE_DOCS_TAB) )
							needsApproval	= 
								DocumentManagerUtil.isResourceVersionPendingtoBeShared(documentNodeBaseVersionUUID, nodeWrapper.getLastVersionUUID(request));
							
					}
					documentData.setNeedsApproval(needsApproval);   //should show "share" or "approve" link
					documentData.setHasShareRights(DocumentManagerRights.hasShareRights(documentNode, request, tabName));
					documentData.setHasUnshareRights(DocumentManagerRights.hasUnshareRights(documentNode, request, tabName));
					
					
					List<String> sharedNodeVersionId=new ArrayList<String>();
					if(tabName!=null){
						if(tabName.equals(CrConstants.PRIVATE_DOCS_TAB)){
							sharedNodeVersionId=DocumentManagerUtil.isPrivateResourceShared(documentNodeBaseVersionUUID);
						}else if(tabName.equalsIgnoreCase(CrConstants.TEAM_DOCS_TAB) && ! isPending){
							String retVal= DocumentManagerUtil.isTeamResourceSharedWithGivenWorkspace(documentNodeBaseVersionUUID,null);
							if(retVal!=null){
								sharedNodeVersionId.add(retVal);
							}						
						}
					}					
					
					
					/**
					 * In case of team doc, instead of lastVersion we need just firstly given documentData-s uuid
					 *  if it's some version of the node and not original last version node !
					 */
					Node lastVersion=null;
					String lastVerUUID=null;
					if(! documentNodeBaseVersionUUID.equals(uuid)){ //this means that document data version that was passed to function,was hidden and we fund it's last not hidden version
						lastVerUUID	=uuid;
					}else{
						lastVersion	= DocumentManagerUtil.getNodeOfLastVersion(uuid, request);
						lastVerUUID	= lastVersion.getUUID();
					}
					
					if(sharedNodeVersionId!=null && sharedNodeVersionId.size()>0){
						documentData.setIsShared(true);												
						
						if(sharedNodeVersionId.contains(lastVerUUID)){
							documentData.setLastVersionIsShared(true);
						}else{
							documentData.setLastVersionIsShared(false);
						}
					}
					//whether this document's any version is public and if is, then which one is public
					if ( uuidMapOrg.containsKey(documentNodeBaseVersionUUID) ) {
							documentData.setIsPublic(true);
							//Verify if the last (current) version is the public one.
							//Node lastVersion	= DocumentManagerUtil.getNodeOfLastVersion(documentNodeBaseVersionUUID, request);
							//String lastVerUUID	= lastVersion.getUUID();
														
							if ( uuidMapVer.containsKey(lastVerUUID) ) {
								documentData.setLastVersionIsPublic( true );
							}
					}else{
						documentData.setIsPublic(false);
					}
					
					// whether this document has any version, that needs to be approved to become team doc version
					List<TeamNodePendingVersion> pendingVersionsForBaseNode=null;
					if(tabName!=null && tabName.equals(CrConstants.TEAM_DOCS_TAB)){
						pendingVersionsForBaseNode=DocumentManagerUtil.getPendingVersionsForResource(documentNodeBaseVersionUUID);
					}
					if(pendingVersionsForBaseNode!=null && pendingVersionsForBaseNode.size()>0){
						documentData.setHasAnyVersionPendingApproval(true);
					}else{
						documentData.setHasAnyVersionPendingApproval(false);
					}
				}
				// This is not the actual document node. It is the node of the public version. That's why one shouldn't have 
				// the above rights.
				else {
					documentData.setShowVersionHistory(false); 
				}
				documents.add(documentData);
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return documents;
	}
	
	public Collection<DocumentData> getDocuments(Collection<String> UUIDs, HttpServletRequest myRequest,String tabName,boolean isPending,boolean showActionsButton) {
		ArrayList<Node> documents		= new ArrayList<Node>();
		Iterator<String> iter			= UUIDs.iterator();
		while (iter.hasNext()) {
			String uuid			= iter.next();
			Node documentNode	= DocumentManagerUtil.getReadNode(uuid, myRequest);
			
			/**
			 * If documentNode is null it means that there is no node with the specified uuid in the repository
			 * but the application still has some information about that node.
			 * It means that there is a problem in the logic of the application so we need to throw an 
			 * exception.
			 */
			if (documentNode == null) {
				try {
				  throw new Exception("Document with uuid '" + uuid + "' not found !");
				}
				catch (Exception e) {
					e.printStackTrace();
					RepairDbUtil.repairDocumentNoLongerInContentRepository(uuid, CrDocumentNodeAttributes.class.getName() );
					RepairDbUtil.repairDocumentNoLongerInContentRepository(uuid, AmpActivityDocument.class.getName() );
				}
				
			}
			else
				documents.add(documentNode);
		}
		Iterator iterator			= documents.iterator();
		if(tabName!=null && tabName.equals(CrConstants.SHARED_DOCS_TAB)){
			return getSharedDocuments(iterator, myRequest,showActionsButton);
		}else{
			return getDocuments(iterator, myRequest,tabName,isPending,showActionsButton);
		}	
		
	}
	
	//for shared Docs tab
	private Collection<DocumentData> getSharedDocuments(Iterator nodeIterator, HttpServletRequest request,boolean showActionsButton) {
		ArrayList<DocumentData> documents	= new ArrayList<DocumentData>();
		try {
			while ( nodeIterator.hasNext() ) {
				Node documentNode	= (Node)nodeIterator.next();								
				NodeWrapper nodeWrapper	= new NodeWrapper(documentNode);
				
				if ( nodeWrapper.getWebLink()!=null && showOnlyDocs ){
					continue;
				}					
				if ( nodeWrapper.getWebLink()==null && showOnlyLinks ){
					continue;
				}
				
				//fill node with data
				String fileName	=  nodeWrapper.getName();
				if ( fileName == null && nodeWrapper.getWebLink() == null ){
					continue;
				}
				
				DocumentData documentData		= new DocumentData();
				documentData.setName( fileName );
				documentData.setUuid( nodeWrapper.getUuid() );
				documentData.setTitle( nodeWrapper.getTitle() );
				documentData.setDescription( nodeWrapper.getDescription() );
				documentData.setNotes( nodeWrapper.getNotes() );
				documentData.setFileSize( nodeWrapper.getFileSizeInMegabytes() );
				documentData.setCalendar( nodeWrapper.getDate() );
				documentData.setVersionNumber( nodeWrapper.getVersionNumber() );
				documentData.setContentType( nodeWrapper.getContentType() );
				documentData.setWebLink( nodeWrapper.getWebLink() );
				documentData.setCmDocTypeId( nodeWrapper.getCmDocTypeId() );
				documentData.setYearofPublication(nodeWrapper.getYearOfPublication());
								
				documentData.process(request);
				documentData.computeIconPath(true);
				
				if(showActionsButton){
					documentData.setHasUnshareRights(DocumentManagerRights.hasUnshareRights(documentNode, request, CrConstants.SHARED_DOCS_TAB));
				}				
				documentData.setIsShared(true);
				
				documentData.setShowVersionHistory(false);				
				documents.add(documentData);	
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return documents;
	}
	
	private boolean isLoggeedIn(HttpServletRequest request) {
		if ( getCurrentTeamMember(request) != null) 
			return true;
		return false;
	}
	
	private TeamMember getCurrentTeamMember( HttpServletRequest request ) {
		HttpSession httpSession		= request.getSession();
		TeamMember teamMember		= (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
		return teamMember;
	}
}
