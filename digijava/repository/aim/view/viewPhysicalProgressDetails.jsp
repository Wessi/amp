<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script type="text/javascript">
function login()
{
	<digi:context name="addUrl" property="context/module/moduleinstance/login.do" />
    document.aimPhysicalProgressForm.action = "<%=addUrl%>";
    document.aimPhysicalProgressForm.submit();
}
</script>
<digi:errors/>
<digi:instance property="aimPhysicalProgressForm" />
<digi:context name="digiContext" property="context"/>
<logic:equal name="aimPhysicalProgressForm" property="validLogin" value="false">
<digi:form action="/viewPhysicalProgress.do" name="aimPhysicalProgressForm" type="org.digijava.module.aim.form.PhysicalProgressForm" 
method="post">
<h3 align="center"> Invalid Login. Please Login Again. </h3><p align="center"><html:submit styleClass="dr-menu" value="Log In" onclick="login()" /></p>
</digi:form>
</logic:equal>

<logic:equal name="aimPhysicalProgressForm" property="validLogin" value="true">

<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
	<TR><TD vAlign="top" align="center">
		<!-- contents -->
		<TABLE width="760" cellSpacing=0 cellPadding=0 vAlign="top" align="center" bgcolor="#f4f4f4" 
		class="box-border-nopadding">
			<TR><TD bgcolor="#f4f4f4">
				<TABLE width="760" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4" border=0>
					<TR bgColor=#f4f4f2><TD align=left>
						<SPAN class=crumb>					
							<jsp:useBean id="urlPhysicalProgress" type="java.util.Map" class="java.util.HashMap"/>
							<c:set target="${urlPhysicalProgress}" property="ampActivityId">
								<bean:write name="aimPhysicalProgressForm" property="ampActivityId"/>
							</c:set>
							<c:set target="${urlPhysicalProgress}" property="tabIndex" value="2"/>
							<bean:define id="translation">
								<digi:trn key="aim:clickToViewPhysicalProgress">Click here to view Physical Progress</digi:trn>
							</bean:define>
							<digi:link href="/viewPhysicalProgress.do" name="urlPhysicalProgress" styleClass="comment" 
							title="<%=translation%>" >
								<digi:trn key="aim:physicalProgress">PhysicalProgress</digi:trn>
							</digi:link>
							&gt; Details &gt; 
							<bean:write name="aimPhysicalProgressForm" property="perspective"/> 
							Perspective
						</SPAN>
					</TD></TR>
					<TR bgColor=#f4f4f2><TD vAlign="top" align="center" width="100%">
						<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2>
							<TR><TD width="100%" bgcolor="#F4F4F2" height="17">
								<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2" height="17">
                          	<TR bgcolor="#F4F4F2" height="17">
										<TD bgcolor="#C9C9C7" class="box-title">&nbsp;&nbsp;
											<digi:trn key="aim:componentDetails">Component Details</digi:trn>
										</TD>
	                           <TD background="module/aim/images/corner-r.gif" height=17 width=17>
										</TD>
   	                     </TR>
      	               </TABLE>									
							</TD></TR>
							<TR><TD width="100%" bgcolor="#F4F4F2" align="center">
								<TABLE width="100%" cellPadding="2" cellSpacing="2" vAlign="top" align="center" bgColor=#f4f4f2
								class="box-border-nopadding">
									<TR><TD width="100%" vAlign="top" align="left">
										<TABLE width="100%" cellPadding="5" cellSpacing="1" vAlign="top" align="left" 
										bgcolor="#ffffff">
											<bean:define name="aimPhysicalProgressForm" property="component"
											id="component" />
											<TR bgcolor="#f4f4f2">
												<TD>
													<b><bean:write name="component" property="title"/></b>
												</TD>
											</TR>
											<TR bgcolor="#f4f4f2">
												<TD>
													&nbsp;&nbsp;&nbsp;<bean:write name="component" property="description"/>
												</TD>
											</TR>
											<TR bgcolor="#ffffff">
												<TD>
													&nbsp;
												</TD>
											</TR>
											<TR bgcolor="#f4f4f2">
												<TD>
													<b><digi:trn key="aim:fundingOfTheComponent">Finance of the component</digi:trn></b>
												</TD>
											</TR>											
											<TR bgcolor="#f4f4f2">
												<TD>
													<TABLE width="100%" cellpadding=2 cellspacing=1>
														<TR>
															<TD>
																<TABLE width="470" cellpadding=2 cellspacing=1 bgcolor="#999999">
																	<TR>
																		<TD colspan="5" class="textalb"> 
																			<digi:trn key="aim:commitments">Commitments</digi:trn>
																		</TD>
																	</TR>
																	<logic:iterate name="component" property="commitments" id="comm">
																	<TR bgcolor="#ffffff">
																		<TD width="70">
																			<bean:write name="comm" property="adjustmentTypeName"/>
																		</TD>
																		<TD width="80" align="center">
																			<bean:write name="comm" property="transactionDate"/>
																		</TD>
																		<TD width="150" align="right">
																			<bean:write name="comm" property="transactionAmount"/>
																		</TD>
																		<TD width="70">
																			<bean:write name="comm" property="currencyCode"/>
																		</TD>
																		<TD width="100">
																			<bean:write name="comm" property="perspectiveName"/>
																		</TD>
																	</TR>
																	</logic:iterate>
																</TABLE>
															</TD>
														</TR>
														<TR>
															<TD>
																<TABLE width="470" cellpadding=2 cellspacing=1 bgcolor="#999999">
																	<TR>
																		<TD colspan="5" class="textalb">
																			<digi:trn key="aim:disbursements">Disbursements</digi:trn>
																		</TD>
																	</TR>
																	<logic:iterate name="component" property="disbursements" id="disb">
																	<TR bgcolor="#ffffff">
																		<TD width="70">
																			<bean:write name="disb" property="adjustmentTypeName"/>
																		</TD>
																		<TD width="80" align="center">
																			<bean:write name="disb" property="transactionDate"/>
																		</TD>
																		<TD width="150" align="right">
																			<bean:write name="disb" property="transactionAmount"/>
																		</TD>
																		<TD width="70">
																			<bean:write name="disb" property="currencyCode"/>
																		</TD>
																		<TD width="100">
																			<bean:write name="disb" property="perspectiveName"/>
																		</TD>
																	</TR>
																	</logic:iterate>
																</TABLE>
															</TD>
														</TR>
														<TR>
															<TD>
																<TABLE width="470" cellpadding=2 cellspacing=1 bgcolor="#999999">
																	<TR>
																		<TD colspan="5" class="textalb">
																			<digi:trn key="aim:expenditures">Expenditures</digi:trn>
																		</TD>
																	</TR>
																	<logic:iterate name="component" property="expenditures" id="exp">
																	<TR bgcolor="#ffffff">
																		<TD width="70">
																			<bean:write name="exp" property="adjustmentTypeName"/>
																		</TD>
																		<TD width="80" align="center">
																			<bean:write name="exp" property="transactionDate"/>
																		</TD>
																		<TD width="150" align="right">
																			<bean:write name="exp" property="transactionAmount"/>
																		</TD>
																		<TD width="70">
																			<bean:write name="exp" property="currencyCode"/>
																		</TD>
																		<TD width="100">
																			<bean:write name="exp" property="perspectiveName"/>
																		</TD>
																	</TR>
																	</logic:iterate>
																</TABLE>
															</TD>
														</TR>														
													</TABLE>
												</TD>
											</TR>																						
											<TR bgcolor="#ffffff">
												<TD>
													&nbsp;												
												</TD>
											</TR>
											<TR bgcolor="#f4f4f2">
												<TD>
													<b><digi:trn key="aim:physicalProgressOfTheComponent">
													Physical progress of the component</digi:trn></b>
												</TD>
											</TR>
											<TR bgcolor="#f4f4f2">
												<TD>	
													<TABLE width="100%" cellpadding=2 cellspacing=1 bgcolor="#f4f4f2">
														<logic:iterate name="component" id="pp" property="phyProgress">
														<TR>
															<TD bgcolor="#fffffc">
																<bean:write name="pp" property="title"/> - 
																<bean:write name="pp" property="reportingDate"/><br>
																&nbsp;&nbsp;&nbsp;<i><bean:write name="pp" property="description"/></i>
															</TD>
														</TR>
														</logic:iterate>
													</TABLE>
												</TD>
											</TR>																						
										</TABLE>
									</TD></TR>
								</TABLE>
							</TD></TR>
						</TABLE>
					</TD></TR>				
				</TABLE>
				</TD></TR>
			</TABLE>
		<!-- end -->
	</TD></TR>
	<TR><TD>&nbsp;</TD></TR>
</TABLE>

</logic:equal>	
