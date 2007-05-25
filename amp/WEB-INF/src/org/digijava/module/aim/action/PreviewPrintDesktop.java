package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.form.DesktopForm;
import java.util.Collection;
import org.digijava.module.aim.dbentity.AmpModality;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.ArrayList;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.AmpSectors;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpStatus;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class PreviewPrintDesktop
    extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        DesktopForm eaform = (DesktopForm) form;
        ArrayList newColl = null;
        ArrayList oldColl = null;

        ListIterator itr = null;
        if(eaform.getFltrDonor() != null) {
            newColl=new ArrayList();
            oldColl = new ArrayList(eaform.getDonors());
            itr = oldColl.listIterator();
            while(itr.hasNext()) {
                AmpOrganisation org = (AmpOrganisation) itr.next();
                for(int i = 0; i < eaform.getFltrSector().length; i++) {
                    if(org.getAmpOrgId().longValue() == eaform.getFltrDonor()[i]) {
                        newColl.add(org);
                    }
                }
            }
            eaform.setSelDonors(newColl);
            newColl = null;
            oldColl = null;
            itr = null;

        }

        if(eaform.getFltrSector() != null) {
            newColl=new ArrayList();
            oldColl = new ArrayList(eaform.getSectors());
            itr = oldColl.listIterator();
            while(itr.hasNext()) {
                AmpSector sec = (AmpSector) itr.next();
                for(int i = 0; i < eaform.getFltrSector().length; i++) {
                    if(sec.getAmpSectorId().longValue() == eaform.getFltrSector()[i]) {
                        newColl.add(sec);
                    }
                }
            }
            eaform.setSelSectors(newColl);
            newColl = null;
            oldColl = null;
            itr = null;
        }

        if(eaform.getFltrStatus() != null) {
            newColl=new ArrayList();
            oldColl = new ArrayList(eaform.getStatus());
            itr = oldColl.listIterator();
            while(itr.hasNext()) {
                AmpStatus st = (AmpStatus) itr.next();
                for(int i = 0; i < eaform.getFltrStatus().length; i++) {
                    if(st.getAmpStatusId().longValue() == eaform.getFltrStatus()[i]) {
                        newColl.add(st);
                    }
                }
            }
            eaform.setSelStatus(newColl);
            newColl = null;
            oldColl = null;
            itr = null;
        }

        return mapping.findForward("forward");
    }
}
