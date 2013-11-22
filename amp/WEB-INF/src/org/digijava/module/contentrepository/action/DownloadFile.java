package org.digijava.module.contentrepository.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.BoundedList;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

import org.springframework.util.FileCopyUtils;
/**
 * 
 * @author Alex Gartner
 *
 */
public class DownloadFile extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		String nodeUUID		= request.getParameter("uuid"); 
		
		if (nodeUUID != null) {
			Node node				= DocumentManagerUtil.getReadNode(nodeUUID, request);			
			Property contentType	= node.getProperty(CrConstants.PROPERTY_CONTENT_TYPE);
			Property name			= node.getProperty(CrConstants.PROPERTY_NAME);
			Property data			= node.getProperty(CrConstants.PROPERTY_DATA);
			
			if (request.getSession().getAttribute(Constants.MOST_RECENT_RESOURCES) == null)
			{
				Comparator<DocumentData> documentDataComparator = new Comparator<DocumentData>()
				{
					public int compare(DocumentData a, DocumentData b)
					{
						return a.getUuid().compareTo(b.getUuid());
					}
				};
				request.getSession().setAttribute(Constants.MOST_RECENT_RESOURCES, new BoundedList<DocumentData>(5, documentDataComparator));
			}
			
			NodeWrapper nodeWrapper = new NodeWrapper(node);
			DocumentData documentData = DocumentData.buildFromNodeWrapper(nodeWrapper, nodeWrapper.getName(), null, null, request);
			BoundedList<DocumentData> recentUUIDs = (BoundedList<DocumentData>)(request.getSession().getAttribute(Constants.MOST_RECENT_RESOURCES));
			recentUUIDs.add(documentData);
			
			if ( contentType != null && name != null && data != null) {
				writeFile(response, contentType.getString(), name.getString(), data.getStream());
			}
		}

		DocumentManagerUtil.logoutJcrSessions(request);
		return null;
	}
	
	public static void writeFile(HttpServletResponse response,
			String contentType, String fileName, InputStream istream) throws
			IOException {
		
		if (response == null) {
			throw new IllegalArgumentException(
			"response parameter must be not-null");
		}
		
		if (istream == null) {
			throw new IllegalArgumentException(
			"data parameter must be not-null");
		}
		
		ServletOutputStream output = response.getOutputStream();
		if (contentType != null && contentType.length() > 0)
			response.setContentType(contentType);
		if (fileName != null && fileName.length() > 0)
			response.setHeader("Content-Disposition",
					"attachment; filename=\"" + fileName + "\"");
		FileCopyUtils.copy(istream, output);
	}
}
