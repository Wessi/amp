package org.digijava.module.esrigis.action;

import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.esrigis.form.StructuresImporterForm;
import org.digijava.module.esrigis.helpers.DbHelper;

import au.com.bytecode.opencsv.CSVReader;

public class StructuresImporter extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		StructuresImporterForm sform  = (StructuresImporterForm) form;
		
		if (request.getParameter("importPerform") != null && sform.getUploadedFile()!=null && sform.getUploadedFile().getFileSize()>0) {
			String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
    		String locale = RequestUtils.getNavigationLanguage(request).getCode();
			ActionMessages errors = new ActionMessages();
			
			if(! "text/csv".equals(sform.getUploadedFile().getContentType())){
				
				errors.add(
						ActionErrors.GLOBAL_MESSAGE,
						new ActionMessage(
								"error.aim.structureImporter.wrongContentType",
								TranslatorWorker
										.translateText("The file to import must be an text/csv file.")));
				saveErrors(request, errors);
			}else{
				try{
					InputStreamReader isr = new InputStreamReader(sform.getUploadedFile().getInputStream());
					CSVReader reader = new CSVReader(isr);
					String [] nextLine;
					Boolean firstLine = true;
			        ArrayList<String> errors2 = new ArrayList<String>();
					while ((nextLine = reader.readNext()) != null) {
						if(firstLine){
							firstLine = false;
				        }else if(nextLine.length>1){
					        sform.setErrors(errors2);
							AmpStructure st = new AmpStructure();
							st.setTitle(nextLine[1]);
							st.setLatitude(nextLine[2]);
							st.setLongitude(nextLine[3]);
                            st.setType(DbHelper.getStructureTypesByName(nextLine[4].trim()));
                            st.setActivities(DbHelper.getActivityByAmpId(nextLine[0].trim()));
                            st.setDescription(nextLine[5].trim());
							if (!"".equalsIgnoreCase(st.getTitle()) && st.getType()!=null && st.getActivities().size()!=0){
								DbHelper.saveStructure(st);
                            }else{
                                String errorline = ArrayUtils.toString(nextLine).replace("{", "");
                                errorline = errorline.replace("}","");
                                sform.getErrors().add(ArrayUtils.toString(errorline));
							}
							
						}
					}
					
					errors.add(
							ActionErrors.GLOBAL_MESSAGE,
							new ActionMessage(
									"aim.structureImporter.success",
									TranslatorWorker
											.translateText(
													"Structures import done successfully.")));
					//saved as error just to be shown in red
					saveErrors(request, errors);
					
					
				}catch(Exception e){
					errors.add(
							ActionErrors.GLOBAL_MESSAGE,
							new ActionMessage(
									"error.aim.structureImporter.error",
									TranslatorWorker
											.translateText(
													"An error occurred while processing the file.")));
					saveErrors(request, errors);
				}
				
			}
		}
		return mapping.findForward("forward");
		
	}
}
