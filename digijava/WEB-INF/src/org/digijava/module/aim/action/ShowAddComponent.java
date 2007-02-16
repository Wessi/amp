/*
 * ShowAddComponent.java
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpPerspective;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.FundingValidator;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;


public class ShowAddComponent extends Action {
	
	private static Logger logger = Logger.getLogger(ShowAddComponent.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception{

		String action = request.getParameter("compFundAct");
		logger.debug("Action is " + action);

		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");		
		
		try
		{
			EditActivityForm eaForm = (EditActivityForm) form;
			eaForm.setStep("5");
			
			if( action != null && action.equalsIgnoreCase("show") )
			{
				logger.info(" in the show components..... ");
				//eaForm.setAllComps(ActivityUtil.getAllComponentNames());
				eaForm.setAllComps(ComponentsUtil.getAmpComponents());
				logger.debug("Forwarding to forward");
				eaForm.setComponentId(new Long(-1));
				eaForm.setComponentTitle(null);
				eaForm.setComponentDesc(null);
				if (tm.getAppSettings().getPerspective()
						.equalsIgnoreCase(Constants.DEF_DNR_PERSPECTIVE)) {
					request.setAttribute("defPerspective",Constants.DONOR);
				} else if (tm.getAppSettings().getPerspective().
						equalsIgnoreCase(Constants.DEF_MFD_PERSPECTIVE)) {
					request.setAttribute("defPerspective",Constants.MOFED);
				}
				String defCurr = CurrencyUtil.getCurrency(
						tm.getAppSettings().getCurrencyId()).getCurrencyCode();
				request.setAttribute("defCurrency",defCurr);				
				
				return mapping.findForward("forward");
			}
			else if( action != null && action.equalsIgnoreCase("showEdit") )
			{
				Iterator itr = eaForm.getSelectedComponents().iterator();
				String id = request.getParameter("fundId");
				long cId = Long.parseLong(id);
				while (itr.hasNext()) 
				{
					Components comp = (Components) itr.next();
					if (comp.getComponentId().longValue() == cId) 
					{
						eaForm.setComponentId(comp.getComponentId());
						eaForm.setComponentTitle(comp.getTitle());
						eaForm.setComponentDesc(comp.getDescription());
						break;
					}
				}
				if (tm.getAppSettings().getPerspective()
						.equalsIgnoreCase(Constants.DEF_DNR_PERSPECTIVE)) {
					request.setAttribute("defPerspective",Constants.DONOR);
				} else if (tm.getAppSettings().getPerspective().
						equalsIgnoreCase(Constants.DEF_MFD_PERSPECTIVE)) {
					request.setAttribute("defPerspective",Constants.MOFED);
				}
				String defCurr = CurrencyUtil.getCurrency(
						tm.getAppSettings().getCurrencyId()).getCurrencyCode();
				request.setAttribute("defCurrency",defCurr);				
				return mapping.findForward("forward");
			}
			else if( action != null && action.equalsIgnoreCase("update") )
			{
				Components compFund = new Components();
			
				if (eaForm.getComponentId() == null ||
									 eaForm.getComponentId().longValue() == -1) {
					compFund.setComponentId(new Long(System.currentTimeMillis()));
				} else {
					compFund.setComponentId(eaForm.getComponentId());
				}
				compFund.setTitle(eaForm.getComponentTitle());
				compFund.setAmount(eaForm.getComponentAmount());
				compFund.setDescription(eaForm.getComponentDesc());
				
				Enumeration paramNames = request.getParameterNames();
				String param = "";
				String val = "";
				Map comm = new HashMap();
				Map disb = new HashMap();
				Map exp = new HashMap();

				while( paramNames.hasMoreElements() )
				{
					param = (String) paramNames.nextElement();
					if( param.startsWith("comm_") )
					{
						val = request.getParameter( param );
						StringTokenizer st = new StringTokenizer( param, "_" );
						st.nextToken();
						int index = Integer.parseInt(st.nextToken());
						int num = Integer.parseInt(st.nextToken());

						if (comm.containsKey(new Integer(index)) == false) 
						{
							comm.put(new Integer(index),new FundingDetail());	
						}
						FundingDetail fd = (FundingDetail) comm.get(new Integer(index));

						if( fd != null )
						{
							switch( num )
							{
								case 1:
									fd.setAdjustmentType( Integer.parseInt(val) );
									if ( fd.getAdjustmentType() == 1 ) 
									{
										fd.setAdjustmentTypeName( "Actual" );
									} 
									else if ( fd.getAdjustmentType() == 0 ) 
									{
										fd.setAdjustmentTypeName( "Planned" );
									}
									break;
								case 2:
									fd.setTransactionAmount( val );
									break;
								case 3:
									fd.setCurrencyCode( val );
									break;
								case 4:
									fd.setTransactionDate( val );
									break;
								case 5:
									fd.setPerspectiveCode( val );
									Iterator itr1 = eaForm.getPerspectives().iterator();
									while( itr1.hasNext() ) 
									{
										AmpPerspective pers = ( AmpPerspective ) itr1.next();
										if ( pers.getCode().equals(val) ) 
										{
											fd.setPerspectiveName( pers.getName() );
										}
									}
							}
							comm.put( new Integer(index),fd );
						}
					}
					else if( param.startsWith("disb_") )
					{
						val = request.getParameter( param );
						StringTokenizer st = new StringTokenizer( param,"_" );
						st.nextToken();
						int index = Integer.parseInt( st.nextToken() );
						int num = Integer.parseInt( st.nextToken() );
					
						if ( disb.containsKey( new Integer( index ) ) == false ) 
						{
							disb.put( new Integer( index ),new FundingDetail() );	
						}

						FundingDetail fd = ( FundingDetail ) disb.get( new Integer( index ) );
						if ( fd != null ) 
						{
							switch ( num ) 
							{
								case 1:
									fd.setAdjustmentType( Integer.parseInt( val ) );
									logger.debug("Adjustment type = " + fd.getAdjustmentType());
									if ( fd.getAdjustmentType() == 1 ) 
									{
										fd.setAdjustmentTypeName( "Actual" );
									} 
									else if ( fd.getAdjustmentType() == 0 ) 
									{
										fd.setAdjustmentTypeName( "Planned" );
									}
									break;
								case 2:
									fd.setTransactionAmount( val );
									break;
								case 3:
									fd.setCurrencyCode( val );
									break;
								case 4:
									fd.setTransactionDate( val );
									break;
								case 5:
									fd.setPerspectiveCode( val );
									Iterator itr1 = eaForm.getPerspectives().iterator();
									while ( itr1.hasNext() ) 
									{
										AmpPerspective pers = ( AmpPerspective ) itr1.next();
										if ( pers.getCode().equals( val ) ) 
										{
											fd.setPerspectiveName( pers.getName() );
										}
									}
							}
							disb.put( new Integer( index ),fd );					
						}
					}
					else if ( param.startsWith( "expn_" ) ) 
					{
						val = request.getParameter( param );
						StringTokenizer st = new StringTokenizer( param, "_" );
						st.nextToken();
						int index = Integer.parseInt( st.nextToken() );
						int num = Integer.parseInt( st.nextToken() );
					
						if ( exp.containsKey( new Integer( index ) ) == false ) 
						{
							exp.put( new Integer( index ), new FundingDetail() );	
						}

						FundingDetail fd = ( FundingDetail ) exp.get( new Integer( index ) );
						if ( fd != null ) 
						{
							switch ( num ) 
							{
								case 1:
									fd.setAdjustmentType( Integer.parseInt( val ) );
									logger.debug( "Adjustment type = " + fd.getAdjustmentType() );
									if ( fd.getAdjustmentType() == 1 ) 
									{
										fd.setAdjustmentTypeName( "Actual" );
									} 
									else if ( fd.getAdjustmentType() == 0 ) 
									{
										fd.setAdjustmentTypeName( "Planned" );
									}
									break;
								case 2:
									fd.setTransactionAmount( val );
									break;
								case 3:
									fd.setCurrencyCode( val );
									break;
								case 4:
									fd.setTransactionDate( val );
									break;
								case 5:
									fd.setPerspectiveCode( val );
									Iterator itr1 = eaForm.getPerspectives().iterator();
									while ( itr1.hasNext() ) 
									{
										AmpPerspective pers = ( AmpPerspective ) itr1.next();
										if ( pers.getCode().equals( val ) ) 
										{
											fd.setPerspectiveName( pers.getName() );
										}
									}
							}
							exp.put( new Integer( index ), fd );					
						}					
					}
				}
	
				Iterator itrS = comm.keySet().iterator();
				while ( itrS.hasNext() ) 
				{
					Integer index = ( Integer ) itrS.next();
					FundingDetail fd = ( FundingDetail ) comm.get( index );
					if ( compFund.getCommitments() == null ) 
					{
						compFund.setCommitments( new ArrayList() );
					}
					compFund.getCommitments().add( fd );
				}
			
				itrS = disb.keySet().iterator();
				while ( itrS.hasNext() ) 
				{
					Integer index = ( Integer ) itrS.next();
					FundingDetail fd = ( FundingDetail ) disb.get( index );
					if ( compFund.getDisbursements() == null ) 
					{
						compFund.setDisbursements( new ArrayList() );
					}
					compFund.getDisbursements().add( fd );
				}
			
				itrS = exp.keySet().iterator();
				while ( itrS.hasNext() ) 
				{
					Integer index = ( Integer ) itrS.next();
					FundingDetail fd = ( FundingDetail ) exp.get( index );
					if ( compFund.getExpenditures() == null ) 
					{
						compFund.setExpenditures( new ArrayList() );
					}
					compFund.getExpenditures().add( fd );
				}			
			
				if (eaForm.getSelectedComponents() == null) 
				{
					eaForm.setSelectedComponents( new ArrayList() );
				}
				if ( eaForm.getSelectedComponents().contains( compFund ) ) 
				{
					Iterator itr = eaForm.getSelectedComponents().iterator();
					while (itr.hasNext()) {
						Components comp = (Components) itr.next();
						if (compFund.equals(comp)) {
							compFund.setPhyProgress(comp.getPhyProgress());
						}
					}
					eaForm.getSelectedComponents().remove( compFund );
				}
				
				List list = null;
				if (compFund.getCommitments() != null) {
					list = new ArrayList(compFund.getCommitments());
					Collections.sort(list,FundingValidator.dateComp);
				}
				compFund.setCommitments(list);
				list = null;
				if (compFund.getDisbursements() != null) {
					list = new ArrayList(compFund.getDisbursements());
					Collections.sort(list,FundingValidator.dateComp);
				}
				compFund.setDisbursements(list);
				list = null;
				if (compFund.getExpenditures() != null) {
					list = new ArrayList(compFund.getExpenditures());
					Collections.sort(list,FundingValidator.dateComp);
				}
				compFund.setExpenditures(list);				
				eaForm.getSelectedComponents().add( compFund );
			
				return mapping.findForward("updated");
			}
		} 
		catch ( Exception e ) 
		{
			logger.debug( "Exception" );
			e.printStackTrace( System.out );
		}
		return mapping.findForward("forward");
	}
}
 		




/*					
				
		if (request.getParameter("componentReset") != null && request.getParameter("componentReset").equals("false")) 
		{
			eaForm.setComponentReset(false);
		} 
		else 
		{
			eaForm.setComponentReset(true);
			eaForm.reset(mapping, request);
		}

		if (request.getParameter("id") != null) 
		{
			try 
			{
				long id = Long.parseLong(request.getParameter("id"));
				Long cId = new Long(id);
				Collection selComps = eaForm.getSelectedComponents();
				if (selComps != null && selComps.size() > 0) 
				{
					Iterator itr = selComps.iterator();
					while (itr.hasNext()) 
					{
						Components comp = (Components) itr.next();
						if (comp.getComponentId().equals(cId)) 
						{
							eaForm.setComponentTitle(comp.getTitle());
							eaForm.setComponentAmount(comp.getAmount());
							eaForm.setComponentDesc(comp.getDescription());
							eaForm.setComponentRepDate(comp.getReportingDate());
							eaForm.setComponentId(comp.getComponentId());
							eaForm.setCurrencyCode(comp.getCurrencyCode());
												
							break;
						}
					}
				}
			} 
			catch (Exception e) 
			{
				logger.error("Exception :" + e);
			}
		}
		eaForm.setCurrencies(CurrencyUtil.getAmpCurrency());
		return mapping.findForward("forward");
	}
}

*/



