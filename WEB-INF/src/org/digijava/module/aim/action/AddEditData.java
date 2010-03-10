

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.IndicatorTheme;
import org.digijava.module.aim.form.ThemeForm;
import org.digijava.module.aim.helper.AmpPrgIndicatorValue;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.IndicatorValuesComparator;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class AddEditData extends Action {

    private static Logger logger = Logger.getLogger(AddEditData.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ThemeForm themeForm = (ThemeForm) form;

        String parent=request.getParameter("parent");
        if(parent!=null){
        	if(themeForm.getIndicatorName()!=null){
        		themeForm.setIndicatorName(null);
        	}
            Long parentId=Long.valueOf(parent);
            IndicatorTheme connection=IndicatorUtil.getConnectionToTheme(parentId);
            themeForm.setIndicatorName(connection.getIndicator().getName());
            if (connection.getValues()!=null && connection.getValues().size()>0){
                List<AmpIndicatorValue> sortedIndicatorValues = new ArrayList(connection.getValues());
                Collections.sort(sortedIndicatorValues,new IndicatorValuesComparator());
                List<AmpPrgIndicatorValue> indValuesList=new ArrayList<AmpPrgIndicatorValue>();
            	for (AmpIndicatorValue value :sortedIndicatorValues) {
                	AmpPrgIndicatorValue bean=new AmpPrgIndicatorValue();
					bean.setCreationDate(DateConversion.ConvertDateToString(value.getValueDate()));
					bean.setValAmount(value.getValue());
					bean.setValueType(value.getValueType());
					bean.setIndicatorValueId(value.getIndValId());
					bean.setLocation(value.getLocation());
                                        if(value.getIndicatorSource()!=null){
                                            bean.setSourceId(value.getIndicatorSource().getId());
                                        }
					indValuesList.add(bean);
				}
            	
                themeForm.setPrgIndValues(indValuesList);
            }else{
                themeForm.setPrgIndValues(null);
            }
//            Collection<AmpPrgIndicatorValue> indValCol=ProgramUtil.getThemeIndicatorValues(parentId);
//            if(indValCol!=null){
//                List<AmpPrgIndicatorValue> indValuesList=new ArrayList<AmpPrgIndicatorValue>(indValCol);
//                themeForm.setPrgIndValues(indValuesList);
//            }else{
//                themeForm.setPrgIndValues(null);
//            }
            themeForm.setParentId(parentId);
            themeForm.setCreationDate(null);
            themeForm.setValAmount(null);
            themeForm.setValueType(null);
            themeForm.setSourceId(null);
        }
        String event = request.getParameter("event");
        String action=request.getParameter("action");
       

        List<AmpPrgIndicatorValue> indValues = themeForm.getPrgIndValues();
        //WTF?
        if(indValues == null) {
            indValues = new ArrayList();
        } else if(themeForm.getCreationDate()!=null &&
                  themeForm.getValAmount()!=null &&
                  themeForm.getValueType()!=null){
            for(ListIterator iter = indValues.listIterator(); iter.hasNext(); ) {
                AmpPrgIndicatorValue item = (AmpPrgIndicatorValue) iter.next();
                
                item.setCreationDate(themeForm.getCreationDate()[iter.nextIndex() - 1]);
                item.setValAmount(themeForm.getValAmount()[iter.nextIndex() - 1].doubleValue());
                item.setValueType(themeForm.getValueType()[iter.nextIndex() - 1]);
                if(themeForm.getSourceId()!=null){
                     item.setSourceId(themeForm.getSourceId()[iter.nextIndex() - 1]);
                }
            }
        }

        if(action!=null && action.equalsIgnoreCase("justSubmit")){        	
        	return mapping.findForward("forward");
        }
        if(event!=null && event.equals("addIndValue")){
            AmpPrgIndicatorValue prgIndVal = getPrgIndicatorValue();
            prgIndVal.setValAmount(new Double(0));
            indValues.add(prgIndVal);
            themeForm.setPrgIndValues(indValues);
        }else if(event!=null && event.equals("delIndValue")){
            String index=request.getParameter("index");
            if(indValues!=null){
                indValues.remove(Integer.valueOf(index).intValue());
           }
            themeForm.setPrgIndValues(indValues);
        }else if(event!=null && event.equals("save")){
//            if (themeForm.getParentId() != null) {
//				for (Iterator indValIter = indValues.iterator(); indValIter	.hasNext();) {
//					AmpPrgIndicatorValue indVal = (AmpPrgIndicatorValue) indValIter.next();
//					if (indVal.getIndicatorValueId() != null && (indVal.getIndicatorValueId().longValue() < 0)) {
//						// ProgramUtil.deletePrgIndicatorValueById(themeForm.getParentId(),indVal.getIndicatorValueId());
//					}
//				}
//			}
			// AmpThemeIndicators
			// themeInd=ProgramUtil.getThemeIndicatorById(themeForm.getParentId());
			// AmpIndicator indId =
			// IndicatorUtil.getIndicatorById(themeForm.getParentId());
			// ProgramUtil.saveEditPrgIndValues(indValues,themeInd);
			// IndicatorUtil.saveEditPrgIndValues(indValues, indId);
            
            // TODO INDIC all "save" code above should be deleted.
            //And this code needs more refactoring to remove these program indicators!
            IndicatorTheme connection=IndicatorUtil.getConnectionToTheme(themeForm.getParentId());
            if (connection!=null){
            	connection.getValues().clear();
            	for (Iterator indValIter = indValues.iterator(); indValIter.hasNext();) {
					AmpPrgIndicatorValue prgValue = (AmpPrgIndicatorValue) indValIter.next();
					if(prgValue.getCreationDate()!=null && !prgValue.getCreationDate().equals("") && prgValue.getValAmount()!=null && !prgValue.getValAmount().equals("")){
						AmpIndicatorValue value=new AmpIndicatorValue();
						value.setValue(prgValue.getValAmount());
						value.setValueDate(DateConversion.getDateForIndicator(prgValue.getCreationDate()));
						value.setValueType(prgValue.getValueType());
						value.setLocation(prgValue.getLocation());
                        Long sourceId = prgValue.getSourceId();
                        if(sourceId!=null&&sourceId!=0){
                        	AmpCategoryValue source=CategoryManagerUtil.getAmpCategoryValueFromDb(sourceId);
                            value.setIndicatorSource(source);
                        }
						value.setIndicatorConnection(connection);
						connection.getValues().add(value);
					}
				}
            	try{
                	IndicatorUtil.updateThemeConnection(connection);
            	}catch(Exception ex){
            		logger.error(ex);
            		ex.printStackTrace();
            	}
            }
            //returning null because "delete" is called from already closed popup window.
            //returning normal forward causes digikernel exception.
            return mapping.findForward("backToIndicator");
        }
        return mapping.findForward("forward");
    }

    private AmpPrgIndicatorValue getPrgIndicatorValue() {
        AmpPrgIndicatorValue prgIndVal = new AmpPrgIndicatorValue();
        prgIndVal.setCreationDate(null);
        prgIndVal.setValAmount(null);        
        prgIndVal.setValueType(1);
        return prgIndVal;
    }
    
   
}
