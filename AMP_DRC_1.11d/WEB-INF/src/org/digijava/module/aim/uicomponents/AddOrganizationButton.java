package org.digijava.module.aim.uicomponents;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class AddOrganizationButton extends BodyTagSupport {
	private static final long serialVersionUID = 1L;
	private String collection = "";
	private String property = "";
	private String organisationType = "";
	private String callBackFunction;
	private Object form;
	private String useClient = "false";
	private String htmlvalueHolder = "";
	private String htmlNameHolder = "";
	private String delegateClass = "";
	private String refreshParentDocument = "";
	private String aditionalRequestParameters = "";

	public static final String PARAM_PARAM_FORM_NAME = "PARAM_PARAM_FORM_NAME";
	public static final String PARAM_COLLECTION_NAME = "PARAM_COLLECTION_NAME";
	public static final String PARAM_PROPERY_NAME = "PARAM_PROPERY_NAME";
	public static final String PARAM_CALLBACKFUNCTION_NAME = "PARAM_CALLBACKFUNCTION_NAME";
	public static final String PARAM_RESET_FORM = "PARAM_RESET_FORM";
	public static final String PARAM_REFRESH_PARENT = "PARAM_REFRESH_PARENT";
	public static final String PARAM_USE_CLIENT = "PARAM_USE_CLIENT";
	public static final String PARAM_VALUE_HOLDER = "PARAM_VALUE_HOLDER";
	public static final String PARAM_NAME_HOLDER = "PARAM_NAME_HOLDER";
	public static final String PARAM_NAME_DELEGATE_CLASS = "PARAM_NAME_DELEGATE_CLASS";
	public static final String ADITIONAL_REQUEST_PARAMS = "ADITIONAL_REQUEST_PARAMS";

	public int doStartTag() throws JspException {
		try {
			// add current form to session under a common param name
			if ("false".equalsIgnoreCase(useClient)) {
				pageContext.getSession().setAttribute(PARAM_PARAM_FORM_NAME, form);
			}

			JspWriter out = pageContext.getOut();
			StringBuffer html = new StringBuffer();
			html.append("<input type=\"button\" onclick=\"window.open('/aim/selectOrganizationComponent.do~edit=true~reset=true~");

			html.append(PARAM_RESET_FORM);
			html.append("=true~");

			if (!"".equalsIgnoreCase(refreshParentDocument)) {
				html.append(PARAM_REFRESH_PARENT);
				html.append("=");
				html.append(refreshParentDocument);
				html.append("~");
			}

			if (!"".equalsIgnoreCase(callBackFunction)) {
				html.append(PARAM_CALLBACKFUNCTION_NAME);
				html.append("=");
				html.append(callBackFunction);
				html.append("~");
			}
			if (!"".equalsIgnoreCase(collection)) {

				html.append(PARAM_COLLECTION_NAME);
				html.append("=");
				html.append(collection);
				html.append("~");
			}

			if (!"".equalsIgnoreCase(property)) {
				html.append(PARAM_PROPERY_NAME);
				html.append("=");
				html.append(property);
				html.append("~");
			}
			if ("true".equalsIgnoreCase(useClient)) {
				html.append(PARAM_USE_CLIENT);
				html.append("=");
				html.append("true");
				html.append("~");
			}
			if (!"".equalsIgnoreCase(htmlNameHolder)) {
				html.append(PARAM_NAME_HOLDER);
				html.append("=");
				html.append(htmlNameHolder);
				html.append("~");
			}

			if (!"".equalsIgnoreCase(htmlNameHolder)) {
				html.append(PARAM_VALUE_HOLDER);
				html.append("=");
				html.append(htmlvalueHolder);
				html.append("~");
			}

			if (!"".equalsIgnoreCase(delegateClass)) {
				html.append(PARAM_NAME_DELEGATE_CLASS);
				html.append("=");
				html.append(delegateClass);
				html.append("~");
			}

			if (!"".equalsIgnoreCase(aditionalRequestParameters)) {
				html.append(ADITIONAL_REQUEST_PARAMS);
				html.append("=");
				html.append(aditionalRequestParameters);
				html.append("~");
			}
			html.append("','addOrganisationWindows','height=400,width=600,scrollbars=yes,resizable=yes')\" ");

			out.write(html.toString());
			return super.doStartTag();
		} catch (IOException ioe) {

		}
		return (EVAL_BODY_INCLUDE);

	}

	@Override
	public int doAfterBody() throws JspException {
		// TODO Auto-generated method stub
		return super.doAfterBody();
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			pageContext.getOut().print("value=\"" + bodyContent.getString() + "\"/>");
		} catch (Exception e) {

		}

		return EVAL_PAGE;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getOrganisationType() {
		return organisationType;
	}

	public void setOrganisationType(String organisationType) {
		this.organisationType = organisationType;
	}

	public String getCallBackFunction() {
		return callBackFunction;
	}

	public void setCallBackFunction(String callBackFunction) {
		this.callBackFunction = callBackFunction;
	}

	public Object getForm() {
		return form;
	}

	public void setForm(Object form) {
		this.form = form;
	}

	public String getRefreshParentDocument() {
		return refreshParentDocument;
	}

	public void setRefreshParentDocument(String refreshParentDocument) {
		this.refreshParentDocument = refreshParentDocument;
	}

	public String getUseClient() {
		return useClient;
	}

	public void setUseClient(String useClient) {
		this.useClient = useClient;
	}

	public String getHtmlvalueHolder() {
		return htmlvalueHolder;
	}

	public void setHtmlvalueHolder(String htmlvalueHolder) {
		this.htmlvalueHolder = htmlvalueHolder;
	}

	public String getHtmlNameHolder() {
		return htmlNameHolder;
	}

	public void setHtmlNameHolder(String htmlNameHolder) {
		this.htmlNameHolder = htmlNameHolder;
	}

	public String getDelegateClass() {
		return delegateClass;
	}

	public void setDelegateClass(String delegateClass) {
		this.delegateClass = delegateClass;
	}

	public String getAditionalRequestParameters() {
		return aditionalRequestParameters;
	}

	public void setAditionalRequestParameters(String aditionalRequestParameters) {
		this.aditionalRequestParameters = aditionalRequestParameters;
	}

	
}
