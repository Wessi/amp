package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ecs.xhtml.li;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerRights;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

public class GetDesktopLinks extends TilesAction {

	public ActionForward execute(ComponentContext context, ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {

			HttpSession session = request.getSession();
			TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);

			
			Session jcrWriteSession = DocumentManagerUtil.getWriteSession(request);
			javax.jcr.Node userNode = DocumentManagerUtil.getUserPrivateNode(jcrWriteSession, tm);
			javax.jcr.Node teamNode = DocumentManagerUtil.getTeamNode(jcrWriteSession, tm);

			ArrayList<DocumentData> list = new ArrayList<DocumentData>();
			Collection<DocumentData> tmp = this.getPrivateDocuments(tm, jcrWriteSession.getRootNode(), request);
			if (tmp != null) {
			list.addAll(tmp);
			}
			
			
			tmp = this.getTeamDocuments(tm, jcrWriteSession.getRootNode(), request);
			// just keeps last 5
			list.addAll(tmp);
			Collections.sort(list);
			Collections.reverse(list);
	
			
			if (list.size() > 5) {
				ArrayList<DocumentData> reducedList=new ArrayList<DocumentData>();
				for(int i=0; i < 5;i++){
					reducedList.add(list.get(i));
					session.setAttribute(Constants.MY_LINKS,reducedList);	
				}
			}else{
				session.setAttribute(Constants.MY_LINKS,list);	
			}
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Collection<DocumentData> getPrivateDocuments(TeamMember teamMember, Node rootNode, HttpServletRequest request) {
		Node userNode;
		try {

			userNode = DocumentManagerUtil.getUserPrivateNode(rootNode.getSession(), teamMember);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return getDocuments(userNode, request);
	}

	private Collection<DocumentData> getTeamDocuments(TeamMember teamMember, Node rootNode, HttpServletRequest request) {
		Node teamNode;
		try {
			teamNode = DocumentManagerUtil.getTeamNode(rootNode.getSession(), teamMember);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return getDocuments(teamNode, request);
	}

	private Collection<DocumentData> getDocuments(Node node, HttpServletRequest request) {
		try {
			NodeIterator nodeIterator = node.getNodes();
			return getDocuments(nodeIterator, request);
		} catch (RepositoryException e) {
			e.printStackTrace();
			return null;
		}

	}

	private Collection<DocumentData> getDocuments(Iterator nodeIterator, HttpServletRequest request) {
		ArrayList<DocumentData> documents = new ArrayList<DocumentData>();
		HashMap<String, CrDocumentNodeAttributes> uuidMapOrg = CrDocumentNodeAttributes.getPublicDocumentsMap(false);
		HashMap<String, CrDocumentNodeAttributes> uuidMapVer = CrDocumentNodeAttributes.getPublicDocumentsMap(true);
		try {
			while (nodeIterator.hasNext()) {
				Node documentNode = (Node) nodeIterator.next();
				NodeWrapper nodeWrapper = new NodeWrapper(documentNode);
				Boolean hasViewRights = false;
				Boolean hasShowVersionsRights = false;
				Boolean hasVersioningRights = false;
				Boolean hasDeleteRights = false;
				Boolean hasMakePublicRights = false;
				Boolean hasDeleteRightsOnPublicVersion = false;

				String uuid = documentNode.getUUID();
				boolean isPublicVersion = uuidMapVer.containsKey(uuid);

				if (isPublicVersion) { // This document is public and exactly
					// this version is the public one
					hasViewRights = true;
				} else
					hasViewRights = DocumentManagerRights.hasViewRights(documentNode, request);

				if (hasViewRights == null || !hasViewRights.booleanValue()) {
					continue;
				}

				String fileName = nodeWrapper.getName();
				if (fileName == null && nodeWrapper.getWebLink() == null)
					continue;

				DocumentData documentData = new DocumentData();
				documentData.setName(fileName);
				documentData.setUuid(nodeWrapper.getUuid());
				documentData.setTitle(nodeWrapper.getTitle());
				documentData.setDescription(nodeWrapper.getDescription());
				documentData.setNotes(nodeWrapper.getNotes());
				documentData.setFileSize(nodeWrapper.getFileSizeInMegabytes());
				documentData.setCalendar(nodeWrapper.getDate());
				documentData.setVersionNumber(nodeWrapper.getVersionNumber());
				documentData.setContentType(nodeWrapper.getContentType());
				documentData.setWebLink(nodeWrapper.getWebLink());
				documentData.setCmDocTypeId(nodeWrapper.getCmDocTypeId());
				documentData.setDate(nodeWrapper.getCalendarDate());
				documentData.process(request);
				documentData.computeIconPath(true);

				if (!isPublicVersion) {
					hasShowVersionsRights = DocumentManagerRights.hasShowVersionsRights(documentNode, request);
					if (hasShowVersionsRights != null)
						documentData.setHasShowVersionsRights(hasShowVersionsRights);

					hasVersioningRights = DocumentManagerRights.hasVersioningRights(documentNode, request);
					if (hasVersioningRights != null) {
						documentData.setHasVersioningRights(hasVersioningRights.booleanValue());
					}
					hasDeleteRights = DocumentManagerRights.hasDeleteRights(documentNode, request);
					if (hasDeleteRights != null) {
						documentData.setHasDeleteRights(hasDeleteRights.booleanValue());
					}
					hasMakePublicRights = DocumentManagerRights.hasMakePublicRights(documentNode, request);
					if (hasMakePublicRights != null) {
						documentData.setHasMakePublicRights(hasMakePublicRights.booleanValue());
					}

					hasDeleteRightsOnPublicVersion = DocumentManagerRights.hasDeleteRightsOnPublicVersion(documentNode, request);
					if (hasDeleteRightsOnPublicVersion != null) {
						documentData.setHasDeleteRightsOnPublicVersion(hasDeleteRightsOnPublicVersion.booleanValue());
					}

					if (uuidMapOrg.containsKey(uuid)) {
						documentData.setIsPublic(true);

						// Verify if the last (current) version is the public
						// one.
						Node lastVersion = DocumentManagerUtil.getNodeOfLastVersion(uuid, request);
						String lastVerUUID = lastVersion.getUUID();
						if (uuidMapVer.containsKey(lastVerUUID)) {
							documentData.setLastVersionIsPublic(true);
						}

					} else
						documentData.setIsPublic(false);

				}
				// This is not the actual document node. It is the node of the
				// public version. That's why one shouldn't have
				// the above rights.
				else {
					documentData.setShowVersionHistory(false);
				}
				documents.add(documentData);
			}

			/* } */
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return documents;
	}

}
