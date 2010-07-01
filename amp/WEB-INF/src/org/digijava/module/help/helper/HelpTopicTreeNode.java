package org.digijava.module.help.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.digijava.module.help.dbentity.HelpTopic;

/**
 * Helper for help topic tree node.
 * Currently used only for Hello Glossary rendering.
 * @author Irakli Kobiashvili ikobiashvili@dgfoundation.org
 *
 */
public class HelpTopicTreeNode {
	private static final long serialVersionUID = 1L;
	private Long parentNodeId;
	private HelpTopic origin;
	private List<HelpTopicTreeNode> children;
	private HelpTopicTreeNode parentNode;

	public HelpTopicTreeNode(){
		children = new ArrayList<HelpTopicTreeNode>();
	}

	public HelpTopicTreeNode(HelpTopic dbBean){
		this();
		this.origin = dbBean;
		if (dbBean.getParent()!=null){
			this.parentNodeId = this.origin.getParent().getHelpTopicId();
		}
	}
	
	public List<HelpTopicTreeNode> getNodeChildren() {
		return children;
	}
	
	public void setParentNode(HelpTopicTreeNode parentNode){
		this.parentNode = parentNode;
	}
	public HelpTopicTreeNode getParentNode(){
		return this.parentNode;
	}

	public Long getParentNodeId() {
		return parentNodeId;
	}
	
	/**
	 * Generates YahooUI treeView JS structure.
	 * @return
	 */
	public String getYahooJSdefinition(){
		
		//TODO translate!
		String trnTitle = this.origin.getTopicKey();
		
		StringBuffer buf=new StringBuffer("{ type: 'text', label: '");
		buf.append(trnTitle);
		buf.append("', ampHelpTopicId: '");
		buf.append(this.origin.getHelpTopicId());
		buf.append("'");
		if (this.origin.getBodyEditKey()!=null){
			buf.append(", ampEditorKey: '");
			buf.append(this.origin.getBodyEditKey());
			buf.append("'");
		}
		
		if (this.getNodeChildren().size()>0){
			buf.append(",children: [");
			List<HelpTopicTreeNode> children = getNodeChildren();
			for (Iterator<HelpTopicTreeNode> iter = children.iterator(); iter.hasNext();) {
				HelpTopicTreeNode child = iter.next();
				buf.append(child.getYahooJSdefinition());
				if (iter.hasNext()){
					buf.append(",");
				}
			}
			buf.append("]");
		}
		buf.append("}");
		return buf.toString();
	}
	
	public String getNodeHtml(){
		StringBuffer buff = new StringBuffer("\n");
		buff.append("<div id=\"AMP_HelpNodeDiv_");
		buff.append(this.origin.getHelpTopicId());
		buff.append("\">\n\t<a href=\"#\">");
		buff.append(this.origin.getTopicKey());
		buff.append("</a>\n</div>\n");
		return buff.toString();
	}

}
