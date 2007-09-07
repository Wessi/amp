<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<html:errors/>

<digi:instance property="aimRegionalFundingForm" />
<digi:form action="/viewRegFundDetails.do" method="post">

<html:hidden property="ampActivityId" />
<html:hidden property="regionId" />
<html:hidden property="tabIndex" />

<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
	<TR>
		<TD vAlign="top" align="center">
			<!-- contents -->
			<TABLE width="99%" cellSpacing=0 cellPadding=0 vAlign="top" align="center" bgcolor="#f4f4f4" class="box-border-nopadding">
				<TR>
					<TD bgcolor="#f4f4f4">
						<TABLE width="100%" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4">
							<TR bgColor=#f4f4f2>
      	      	<TD align=left>
									<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left" vAlign="top">
										<TR>
											<TD align="left">
												<SPAN class=crumb>					
													<jsp:useBean id="url" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${url}" property="ampActivityId">
														<bean:write name="aimRegionalFundingForm" property="ampActivityId"/>
													</c:set>
													<c:set target="${url}" property="tabIndex" value="4"/>
                   				<c:set target="${url}" property="regionId" value="-1"/>
													<c:set var="translation">
															<digi:trn key="aim:clickToViewRegionalFunding">Click here to view regional funding</digi:trn>
													</c:set>
													<digi:link href="/viewRegionalFundingBreakdown.do" name="url" 
													styleClass="comment" title="${translation}" >
													<digi:trn key="aim:regionalFunding">Regional Funding</digi:trn>
													</digi:link>&nbsp;&gt;&nbsp;
													<digi:trn key="aim:details">Details</digi:trn>&nbsp;&gt;&nbsp;
													<bean:write name="aimRegionalFundingForm" property="perspective"/>
												</SPAN>											
											</TD>
											<TD align="right">
												&nbsp;
											</TD>
										</TR>
									</TABLE>										
								</TD>
							</TR>

							<c:if test="${aimRegionalFundingForm.goButton == true}">
							<TR bgColor=#f4f4f2>
      	      	<TD align=left>
									<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left" vAlign="top">
										<TR>
											<TD align="left">
												<c:if test="${aimRegionalFundingForm.currFilter == true}">
												Currency :
												<html:select property="currFilterValue" styleClass="dr-menu">
  		       		        	<html:optionsCollection name="aimRegionalFundingForm" 
													property="currencies" value="currencyCode" label="currencyName"/>
												</html:select>&nbsp;&nbsp;&nbsp;
												</c:if>
												<c:if test="${aimRegionalFundingForm.calFilter == true}">
												Calendar :
												<html:select property="calFilterValue" styleClass="dr-menu">
  		       		        	<html:optionsCollection name="aimRegionalFundingForm" 
													property="fiscalCalendars" value="ampFiscalCalId" label="name"/>
												</html:select>												
												</c:if>
												<html:submit value="GO!" styleClass="dr-menu"/>
											</TD>
										</TR>
									</TABLE>										
								</TD>
							</TR>							
							</c:if>
							
							<c:if test="${! empty aimRegionalFundingForm.regionalFundings}">
							<c:forEach var="rd" items="${aimRegionalFundingForm.regionalFundings}">
							<c:if test="${rd.regionId == aimRegionalFundingForm.regionId}">
							<TR bgColor=#f4f4f2>
								<TD vAlign="top" align="center" width="750">
									<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2>
										<c:if test="${! empty rd.commitments}">
										<TR>
											<TD width="750" bgcolor="#F4F4F2">
												<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2">
                        	<TR bgcolor="#F4F4F2"> 
                          	<TD bgcolor="#C9C9C7" class="box-title" height="17">&nbsp;&nbsp;
															<digi:trn key="aim:commitments">Commitments</digi:trn>
														</TD>
	                          <TD background="module/aim/images/corner-r.gif" 
														height=17 width=17></TD>
													</TR>
												</TABLE>									
											</TD>
										</TR>
										<TR>
											<TD width="750" bgcolor="#F4F4F2" align="center" class="box-border-nopadding">
												<TABLE width="750"  border="0" cellpadding="4" cellspacing="1">
                 					<TR bgcolor="#DDDDDB" > 
	                        	<TD><digi:trn key="aim:actual/planned">Actual/Planned</digi:trn></TD>
														<TD><digi:trn key="aim:totalAmount">Total Amount</digi:trn></TD>
	                         	<TD><digi:trn key="aim:currency">Currency</digi:trn></TD>
														<TD><digi:trn key="aim:date">Date</digi:trn></TD>
														
														<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
	                         								<TD><digi:trn key="aim:perspective">Perspective</digi:trn></TD>
	                         							</logic:equal>
													</TR>
													<c:forEach var="comm" items="${rd.commitments}">
														<TR valign="top" bgcolor="#f4f4f2"> 
					    	           		<TD>
																<c:out value="${comm.adjustmentTypeName}"/>
															</TD>
							                 <TD><c:out value="${comm.transactionAmount}"/></TD>
							                 <TD><c:out value="${comm.currencyCode}"/></TD>
					      			         <TD><c:out value="${comm.transactionDate}"/></TD>
					      			          <logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
								               	<TD><c:out value="${comm.perspectiveName}"/></TD>
								              </logic:equal> 
														</TR>
													</c:forEach>
												</TABLE>
											</TD>
										</TR>
										<TR>
											<TD bgcolor="#F4F4F2">&nbsp;</TD>
										</TR>
										</c:if>

										<c:if test="${! empty rd.disbursements}">
										<TR>
											<TD width="750" bgcolor="#F4F4F2">
												<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2">
                        	<TR bgcolor="#F4F4F2"> 
                          	<TD bgcolor="#C9C9C7" class="box-title" height="17">&nbsp;&nbsp;
															<digi:trn key="aim:disbursements">Disbursements</digi:trn>
														</TD>
	                          <TD background="module/aim/images/corner-r.gif" 
														height=17 width=17></TD>
													</TR>
												</TABLE>									
											</TD>
										</TR>
										<TR>
											<TD width="750" bgcolor="#F4F4F2" align="center" class="box-border-nopadding">
												<TABLE width="750"  border="0" cellpadding="4" cellspacing="1">
                 					<TR bgcolor="#DDDDDB" > 
	                        	<TD><digi:trn key="aim:actual/planned">Actual/Planned</digi:trn></TD>
														<TD><digi:trn key="aim:totalAmount">Total Amount</digi:trn></TD>
	                         	<TD><digi:trn key="aim:currency">Currency</digi:trn></TD>
														<TD><digi:trn key="aim:date">Date</digi:trn></TD>
								<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
	                         		<TD><digi:trn key="aim:perspective">Perspective</digi:trn></TD>
	                         	</logic:equal>
													</TR>
													<c:forEach var="comm" items="${rd.disbursements}">
														<TR valign="top" bgcolor="#f4f4f2"> 
					    	           		<TD>
																<c:out value="${comm.adjustmentTypeName}"/>
															</TD>
							                 <TD><c:out value="${comm.transactionAmount}"/></TD>
							                 <TD><c:out value="${comm.currencyCode}"/></TD>
					      			         <TD><c:out value="${comm.transactionDate}"/></TD>
					      			         <logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
								               	<TD><c:out value="${comm.perspectiveName}"/></TD>
								             </logic:equal>
														</TR>
													</c:forEach>
												</TABLE>
											</TD>
										</TR>
										<TR>
											<TD bgcolor="#F4F4F2">&nbsp;</TD>
										</TR>										
										</c:if>

										<c:if test="${! empty rd.expenditures}">
										<TR>
											<TD width="750" bgcolor="#F4F4F2">
												<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2">
                        	<TR bgcolor="#F4F4F2"> 
                          	<TD bgcolor="#C9C9C7" class="box-title" height="17">&nbsp;&nbsp;
															<digi:trn key="aim:expenditures">Expenditures</digi:trn>
														</TD>
	                          <TD background="module/aim/images/corner-r.gif" 
														height=17 width=17></TD>
													</TR>
												</TABLE>									
											</TD>
										</TR>
										<TR>
											<TD width="750" bgcolor="#F4F4F2" align="center" class="box-border-nopadding">
												<TABLE width="750"  border="0" cellpadding="4" cellspacing="1">
                 					<TR bgcolor="#DDDDDB" > 
	                        	<TD><digi:trn key="aim:actual/planned">Actual/Planned</digi:trn></TD>
														<TD><digi:trn key="aim:totalAmount">Total Amount</digi:trn></TD>
	                         	<TD><digi:trn key="aim:currency">Currency</digi:trn></TD>
														<TD><digi:trn key="aim:date">Date</digi:trn></TD>
								<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">
	                         		<TD><digi:trn key="aim:perspective">Perspective</digi:trn></TD>
	                         	</logic:equal>
													</TR>
													<c:forEach var="comm" items="${rd.expenditures}">
														<TR valign="top" bgcolor="#f4f4f2"> 
					    	           		<TD>
																<c:out value="${comm.adjustmentTypeName}"/>
															</TD>
							                 <TD><c:out value="${comm.transactionAmount}"/></TD>
							                 <TD><c:out value="${comm.currencyCode}"/></TD>
					      			         <TD><c:out value="${comm.transactionDate}"/></TD>
					      					<logic:equal name="globalSettings" scope="application" property="perspectiveEnabled" value="true">	         
								               	<TD><c:out value="${comm.perspectiveName}"/></TD>
											</logic:equal>
														</TR>
													</c:forEach>
												</TABLE>
											</TD>
										</TR>
										<TR>
											<TD bgcolor="#F4F4F2">&nbsp;</TD>
										</TR>										
										</c:if>										
										
										<TR>
											<TD>
											
												<FONT color=blue>*
													<digi:trn key="aim:allTheAmountsInThousands">	
													All the amounts are in thousands (000)</digi:trn>
													<logic:present name="<%=org.dgfoundation.amp.ar.ArConstants.SELECTED_CURRENCY %>">
														<bean:define id="selCurrency" name="<%=org.dgfoundation.amp.ar.ArConstants.SELECTED_CURRENCY %>" />
														<digi:trn key="<%="aim:currency:" + ((String)selCurrency).toLowerCase().replaceAll(" ", "") %>"> 
															<%=selCurrency %>
														</digi:trn>
													</logic:present>
												</FONT>								
											</TD>
										</TR>
									</TABLE>
								</TD>
							</TR>
							</c:if>
							</c:forEach>
							</c:if>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR><TD>&nbsp;</TD></TR>
</TABLE>
</digi:form>



