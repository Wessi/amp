package org.digijava.module.dataExchange.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.dataExchange.Exception.AmpExportException;
import org.digijava.module.dataExchange.type.AmpColumnEntry;

public class ExportHelper {


	public static String PATH_DELIM = ".";
	
	public static String renderHiddenElements(AmpColumnEntry tag){
//		<input type="hidden" name="activityTags[0].select" value="true">
	
		StringBuffer retValue = new StringBuffer();

		
		retValue.append("<input type=\"hidden\" ");

		Pattern pattern = Pattern.compile("[\\]\\[.]");
		Matcher matcher = pattern.matcher(tag.getKey());

		retValue.append("id=\"");
		retValue.append("id_"+ matcher.replaceAll(""));
		retValue.append("\" ");
		retValue.append("name=\"");
//			retValue.append("activityTags["+list.indexOf(tag)+"].select");
		retValue.append(tag.getKey());
		retValue.append("\" ");
		retValue.append("value=\"");
		if (tag.isSelect() || tag.isMandatory()){
			retValue.append("true");
		} else{
			retValue.append("false");
		}
		retValue.append("\" ");
		retValue.append("/>");
		retValue.append("\n");
		
		if (tag.getElements() != null){
			for (AmpColumnEntry subNode : tag.getElements()) {
				retValue.append(renderHiddenElements(subNode));
			}
		}

		
		
		return retValue.toString();
	}
	
	public static String renderActivityTree(AmpColumnEntry node) {

		
		StringBuffer retValue = new StringBuffer();

		retValue.append(renderActivityTreeNode(node, "tree.getRoot()"));

		return retValue.toString();
	}

	private static String renderActivityTreeNode(AmpColumnEntry node, String parentNode) {
		
		Pattern pattern = Pattern.compile("[\\]\\[.]");
		Matcher matcher = pattern.matcher(node.getKey());
		String key = matcher.replaceAll("");
		StringBuffer retValue = new StringBuffer();
		String nodeVarName = "atn_"+ key;
		retValue.append("var "+ nodeVarName +" = new YAHOOAmp.widget.TaskNode(\"" + node.getName() + "\", " + parentNode + ", ");
		retValue.append("false , ");
		retValue.append(Boolean.toString(node.isSelect()) + ", ");
		retValue.append(Boolean.toString(node.isMandatory()) + ", ");
		retValue.append("\""+key+"\"");
		retValue.append("); ");
		retValue.append("\n");

		if (node.getElements() != null){
			for (AmpColumnEntry subNode : node.getElements()) {
				retValue.append("\n");
				retValue.append(renderActivityTreeNode(subNode, nodeVarName));
				retValue.append("\n");
			}
		}			
		return retValue.toString();
	}

	public static AmpColumnEntry getActivityStruct(String name, String key, String path,  Class clazz, boolean requred, int param) {
		AmpColumnEntry retValue = new AmpColumnEntry(key + ".select", name, path);
		retValue.setSelect(requred);
		retValue.setMandatory(requred);

		Field[] fields = clazz.getDeclaredFields();
		int index = -1;
		
		for (Field field : fields) {
			XmlElement xmlElement = field.getAnnotation(XmlElement.class);
			boolean mandatory = false;
			if (xmlElement != null) {
				mandatory = xmlElement.required();
			}

			String newPath = path + PATH_DELIM + field.getName();
			String newKey = key + ".elements[" ;

			if (field.getGenericType() instanceof ParameterizedType) {
				ParameterizedType type = (ParameterizedType) field.getGenericType();
				Class claaa = (Class) (type.getActualTypeArguments()[0]);
				if (claaa.getName().startsWith("org.digijava.module.dataExchange.jaxb")) {
					if (param < 1)
						retValue.getList().add(getActivityStruct(field.getName(), newKey + (++index) + "]", newPath, claaa, mandatory, param+1));
				}
			} else {
				Class claaa = (Class) (field.getGenericType());
				if (claaa.getName().startsWith("org.digijava.module.dataExchange.jaxb")) {
					if (param < 1)
					retValue.getList().add(getActivityStruct(field.getName(), newKey + (++index) + "]", newPath, claaa, mandatory, param+1));
				}
			}
		}

		return retValue;
	}
	
	public static List<Message> getTranslations(String key, String body, String siteId) throws AmpExportException{

		List<Message> retValue = new ArrayList<Message>();
		TranslatorWorker tw = new TranslatorWorker();
		try{
			if (key != null && !key.isEmpty()){
				retValue = (List<Message>)tw.getAllTranslationsOfKey(key, siteId);
			}
			if (body != null && !body.isEmpty()){
				retValue = (List<Message>)tw.getAllTranslationOfBody(body, siteId);
			}
		} catch (WorkerException ex){
			throw new AmpExportException(ex, AmpExportException.ACTIVITY_TRANSLATION);
		}
		return retValue;

	}

	public static XMLGregorianCalendar getGregorianCalendar(Date date) throws AmpExportException {
		XMLGregorianCalendar retValue = null;
		if (date != null) {
			try {
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(date);
				retValue = DatatypeFactory.newInstance()
				.newXMLGregorianCalendar(cal);
			} catch (Exception e) {
				throw new AmpExportException(e, AmpExportException.ACTIVITY_FORMAT);
			}
		}
		return retValue;
	}
	
	
}
