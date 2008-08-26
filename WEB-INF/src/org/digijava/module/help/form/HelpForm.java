package org.digijava.module.help.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.help.dbentity.HelpTopic;
import org.digijava.module.help.helper.HelpTopicsTreeItem;

public class HelpForm extends ActionForm {
	private List<HelpTopic> helpTopics=null;
	private Long helpTopicId=null;
	private String topicKey=null;
	private int wizardStep;
	private String bodyEditKey;
	private List<HelpTopic> firstLevelTopics=new ArrayList<HelpTopic>();
	private Long parentId;
	private String titleTrnKey;
	private String keywordsTrnKey;
	private Boolean edit=null;
	private String keywords;
	private Collection topicTree;
	private List<String> helpErrors;
	private Boolean blankPage;
	private String title;
	private String body;
	private Long childId;
	

	public Boolean getBlankPage() {
		return blankPage;
	}

	public void setBlankPage(Boolean blankPage) {
		this.blankPage = blankPage;
	}

	public List<String> getHelpErrors() {
		return helpErrors;
	}

	public void setHelpErrors(List<String> helpErrors) {
		this.helpErrors = helpErrors;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Boolean getEdit() {
		return edit;
	}

	public void setEdit(Boolean edit) {
		this.edit = edit;
	}

	public String getKeywordsTrnKey() {
		return keywordsTrnKey;
	}

	public void setKeywordsTrnKey(String keywordsTrnKey) {
		this.keywordsTrnKey = keywordsTrnKey;
	}

	public String getTitleTrnKey() {
		return titleTrnKey;
	}

	public void setTitleTrnKey(String titleTrnKey) {
		this.titleTrnKey = titleTrnKey;
	}

	

	public String getTopicKey() {
		return topicKey;
	}

	public void setTopicKey(String topicKey) {
		this.topicKey = topicKey;
	}

	public List<HelpTopic> getHelpTopics() {
		return helpTopics;
	}

	public void setHelpTopics(List<HelpTopic> helpTopics) {
		this.helpTopics = helpTopics;
	}

	public int getWizardStep() {
		return wizardStep;
	}

	public void setWizardStep(int wizardStep) {
		this.wizardStep = wizardStep;
	}

	public String getBodyEditKey() {
		return bodyEditKey;
	}

	public void setBodyEditKey(String bodyEditKey) {
		this.bodyEditKey = bodyEditKey;
	}

	public List<HelpTopic> getFirstLevelTopics() {
		return firstLevelTopics;
	}

	public void setFirstLevelTopics(List<HelpTopic> firstLevelTopics) {
		this.firstLevelTopics = firstLevelTopics;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Collection getTopicTree() {
		return topicTree;
	}

	public void setTopicTree(Collection topicTree) {
		this.topicTree = topicTree;
	}

	public Long getHelpTopicId() {
		return helpTopicId;
	}

	public void setHelpTopicId(Long helpTopicId) {
		this.helpTopicId = helpTopicId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Long getChildId() {
		return childId;
	}

	public void setChildId(Long childId) {
		this.childId = childId;
	}


}
