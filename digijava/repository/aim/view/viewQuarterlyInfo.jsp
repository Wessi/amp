<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<html:errors/>
<digi:instance property="aimQuarterlyInfoForm" />
<digi:context name="digiContext" property="context"/>

<logic:equal name="aimQuarterlyInfoForm" property="sessionExpired" value="true">
	<jsp:include page="../../../repository/aim/view/sessionExpired.jsp" flush="true" />
</logic:equal>

<logic:equal name="aimQuarterlyInfoForm" property="sessionExpired" value="false">

<jsp:useBean id="urlSubTabs" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlSubTabs}" property="ampActivityId">
	<bean:write name="aimQuarterlyInfoForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlSubTabs}" property="ampFundingId">
	<bean:write name="aimQuarterlyInfoForm" property="ampFundingId"/>
</c:set>
<c:set target="${urlSubTabs}" property="tabIndex" value="1"/>
<c:set target="${urlSubTabs}" property="transactionType" value="0"/>

<jsp:useBean id="urlFinancialOverview" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlFinancialOverview}" property="ampActivityId">
	<bean:write name="aimQuarterlyInfoForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlFinancialOverview}" property="ampFundingId">
	<bean:write name="aimQuarterlyInfoForm" property="ampFundingId"/>
</c:set>
<c:set target="${urlFinancialOverview}" property="tabIndex">
	<bean:write name="aimQuarterlyInfoForm" property="tabIndex"/>
</c:set>

<jsp:useBean id="urlAll" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlAll}" property="ampActivityId">
	<bean:write name="aimQuarterlyInfoForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlAll}" property="ampFundingId">
	<bean:write name="aimQuarterlyInfoForm" property="ampFundingId"/>
</c:set>
<c:set target="${urlAll}" property="tabIndex" value="1"/>

<jsp:useBean id="urlQuarterlyGrouping" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlQuarterlyGrouping}" property="ampActivityId">
	<bean:write name="aimQuarterlyInfoForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlQuarterlyGrouping}" property="ampFundingId">
	<bean:write name="aimQuarterlyInfoForm" property="ampFundingId"/>
</c:set>
<c:set target="${urlQuarterlyGrouping}" property="tabIndex" value="1"/>
<c:set target="${urlQuarterlyGrouping}" property="transactionType">
	<bean:write name="aimQuarterlyInfoForm" property="transactionType"/>
</c:set>
<jsp:useBean id="urlDiscrepancy" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlDiscrepancy}" property="ampActivityId">
	<bean:write name="aimQuarterlyInfoForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlDiscrepancy}" property="tabIndex" value="1"/>
<c:set target="${urlDiscrepancy}" property="transactionType">
	<bean:write name="aimQuarterlyInfoForm" property="transactionType"/>
</c:set>

<digi:form action="/viewQuarterlyInfoFilter.do" name="aimQuarterlyInfoForm" 
type="org.digijava.module.aim.form.QuarterlyInfoForm" method="post">

<html:hidden property="ampActivityId" />
<html:hidden property="ampFundingId" />
<html:hidden property="transactionType" />
<html:hidden property="tabIndex" />

<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="760">
<TR>
	<TD vAlign="top" align="center">
		<!-- contents -->

			<TABLE width="760" cellSpacing=0 cellPadding=0 vAlign="top" align="center" bgcolor="#f4f4f4" class="box-border-nopadding">
			<TR><TD bgcolor="#f4f4f4">
			
			<TABLE width="760" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4" border=0>
				<TR bgColor=#222e5d height="20"><TD style="COLOR: #c9c9c7" height="20"> 	
				&nbsp;&nbsp;&nbsp;
					<bean:define id="translation">
						<digi:trn key="aim:clickToViewFinancialOverview">Click here to view Financial Overview</digi:trn>
					</bean:define>
					<digi:link href="/viewFinancialOverview.do" name="urlFinancialOverview" styleClass="sub-nav2" title="<%=translation%>" >
			  			<digi:trn key="aim:overview">OVERVIEW</digi:trn> 
			  		</digi:link> | 
			  		<logic:notEqual name="aimQuarterlyInfoForm" property="transactionType" value="0">
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewCommitments">Click here to view Commitments</digi:trn>
						</bean:define>
						<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="<%=translation%>" >
			  				<digi:trn key="aim:commitments">COMMITMENTS</digi:trn>
			  			</digi:link>
					</logic:notEqual>
					<logic:equal name="aimQuarterlyInfoForm" property="transactionType" value="0">
			      	<span class="sub-nav2-selected">
			      		<digi:trn key="aim:commitments">COMMITMENTS</digi:trn>
			      	</span>
			  		</logic:equal> | 
			  		<logic:notEqual name="aimQuarterlyInfoForm" property="transactionType" value="1">
			  			<c:set target="${urlSubTabs}" property="transactionType" value="1"/>
			<bean:define id="translation">
				<digi:trn key="aim:clickToViewDisbursements">Click here to view Disbursements</digi:trn>
			</bean:define>
			  			<digi:link href="/viewQuarterlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="<%=translation%>" >
			  				<digi:trn key="aim:disbursements">DISBURSEMENTS</digi:trn>
			  			</digi:link>
			  		</logic:notEqual>
			  		<logic:equal name="aimQuarterlyInfoForm" property="transactionType" value="1">
			      	<span class="sub-nav2-selected">
			      		<digi:trn key="aim:disbursements">DISBURSEMENTS</digi:trn>
			      	</span>
			  		</logic:equal>|
			  		<logic:notEqual name="aimQuarterlyInfoForm" property="transactionType" value="2"> 
			  			<c:set target="${urlSubTabs}" property="transactionType" value="2"/>
			<bean:define id="translation">
				<digi:trn key="aim:clickToViewExpenditures">Click here to view Expenditures</digi:trn>
			</bean:define>
			  			<digi:link href="/viewQuarterlyInfo.do" name="urlSubTabs" styleClass="sub-nav2" title="<%=translation%>" >
			  				<digi:trn key="aim:expenditures">EXPENDITURES</digi:trn>
			  			</digi:link>
			    	</logic:notEqual>
			    	<logic:equal name="aimQuarterlyInfoForm" property="transactionType" value="2">
			      	<span class="sub-nav2-selected">
			      		<digi:trn key="aim:expenditures">EXPENDITURES</digi:trn>
			      	</span>
			  		</logic:equal>	| 
			<bean:define id="translation">
				<digi:trn key="aim:clickToViewAll">Click here to view All</digi:trn>
			</bean:define>
			  		<digi:link href="/viewQuarterlyComparisons.do" name="urlAll" styleClass="sub-nav2" title="<%=translation%>" >
						<digi:trn key="aim:all">ALL</digi:trn>
					</digi:link> 
				</TD></TR>			
				<TR bgColor=#f4f4f2>
            	<TD align=left>

						<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left" vAlign="top">
							<TR>
								<TD align="left">
						<SPAN class=crumb>					
							<jsp:useBean id="urlFinancingBreakdown" type="java.util.Map" class="java.util.HashMap"/>
							<c:set target="${urlFinancingBreakdown}" property="ampActivityId">
								<bean:write name="aimQuarterlyInfoForm" property="ampActivityId"/>
							</c:set>
							<c:set target="${urlFinancingBreakdown}" property="tabIndex" value="1"/>
							<bean:define id="translation">
								<digi:trn key="aim:clickToViewFinancialProgress">Click here to view Financial Progress</digi:trn>
							</bean:define>
							<digi:link href="/viewFinancingBreakdown.do" name="urlFinancingBreakdown" styleClass="comment" title="<%=translation%>" >
								<digi:trn key="aim:financialProgress">Financial Progress</digi:trn>
							</digi:link>
							&gt; 
							<logic:equal name="aimQuarterlyInfoForm" property="transactionType" value="1">
            	        	<digi:trn key="quarterlyDisbursements">Quarterly Disbursements</digi:trn>
							</logic:equal>
							<logic:equal name="aimQuarterlyInfoForm" property="transactionType" value="2">
                     	<digi:trn key="quarterlyExpenditures">Quarterly Expenditures</digi:trn>
							</logic:equal>
							<logic:equal name="aimQuarterlyInfoForm" property="perspective" value="MA">
								&gt; MOFED Perspective
							</logic:equal>
							<logic:equal name="aimQuarterlyInfoForm" property="perspective" value="DN">
								&gt; Donor Perspective
							</logic:equal>
						</SPAN>
								</TD>
								<TD align="right">
									&nbsp;
								</TD>
							</TR>
						</TABLE>
					

					</TD>
				</TR>

				<TR bgColor=#f4f4f2>
					<TD vAlign="top" align="center" width="100%" bgColor="#f4f4f2">
						<TABLE cellSpacing=0 cellPadding=0 width="100%" align=center bgColor="#f4f4f2" border=0>
							<TR>
								<TD height="30">
									<TABLE cellSpacing=0 cellPadding=0 width="100%" align=center bgColor="#f4f4f2" border=0 height="30">
										<TR>
											<TD vAlign="bottom" align="left">
												<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F2">
         			               		<TR bgcolor="#F4F4F2"> 
                  			        			<TD nowrap bgcolor="#C9C9C7" class="box-title">&nbsp;
					                      			<logic:equal name="aimQuarterlyInfoForm" property="transactionType" value="1">
   	                  								<digi:trn key="quarterlyDisbursements">Quarterly Disbursements</digi:trn>
															</logic:equal>
															<logic:equal name="aimQuarterlyInfoForm" property="transactionType" value="2">
            	         								<digi:trn key="quarterlyExpenditures">Quarterly Expenditures</digi:trn>
															</logic:equal>
                  			          		</TD>
                          						<TD width="17" height="17" background="<%= digiContext %>/repository/aim/images/corner-r.gif">
			                          			</TD>
         			               		</TR>
                  			    		</TABLE>																			
											</TD>
											<TD vAlign="top" align="right">
			                  			<logic:equal name="aimQuarterlyInfoForm" property="perspectivePresent" value="true">
												<TABLE cellSpacing="2" cellPadding="0" vAlign="top" bgColor=#f4f4f2>
													<TR>
														<TD>
						                         	<STRONG>Perspective:</STRONG>
														</TD>
														<TD>
									      				<html:select property="perspective" styleClass="dr-menu" onchange="formSubmit()">
																<html:option value="MA">MOFED View</html:option>
																<html:option value="DN">Donor View</html:option>
															</html:select>
														</TD>								
													</TR>
												</TABLE>
												</logic:equal>											
											</TD>
										</TR>
									</TABLE>
								</TD>
							</TR>
							<TR bgcolor="#ffffff">
								<TD bgColor=#ffffff class=box-border width="100%" vAlign="top" align="left">
             					<TABLE cellSpacing=2 cellPadding=0 border=0 bgColor="#ffffff" width="100%" vAlign="top" align="left" 
									bgColor="#ffffff">
										<TR><TD bgColor="#ffffff">
											<logic:equal name="aimQuarterlyInfoForm" property="goButtonPresent" value="true">

											<TABLE cellSpacing=1 cellPadding=0 border=0 bgColor=#ffffff vAlign="top" align="left">
   		              				<TR> 
												<logic:equal name="aimQuarterlyInfoForm" property="currencyPresent" value="true">
      	    	       					<TD>
													<TABLE cellSpacing=2 cellPadding=0>	
														<TR>
															<TD><STRONG>Currency:</STRONG></TD>
															<TD>
		                           	 				<html:select property="currency" styleClass="dr-menu">
	   			                        	 			<html:optionsCollection name="aimQuarterlyInfoForm" property="currencies" 
																	value="currencyCode" label="currencyName"/>
																</html:select>															
															</TD>
														</TR>
													</TABLE>
                  	        			</TD>
               	           			</logic:equal>
         	                 			<logic:equal name="aimQuarterlyInfoForm" property="calendarPresent" value="true">
      	                    			<TD>
													<TABLE cellSpacing=2 cellPadding=0>	
														<TR>
															<TD><STRONG>Calendar Type:</STRONG></TD>
															<TD>
																<html:select property="fiscalCalId" styleClass="dr-menu">
																	<html:optionsCollection name="aimQuarterlyInfoForm" property="fiscalYears" 
																	value="ampFiscalCalId" label="name"/> 
																</html:select>
															</TD>
														</TR>
													</TABLE>
                  	        			</TD>
               	           			</logic:equal>
         	                 			<logic:equal name="aimQuarterlyInfoForm" property="yearRangePresent" value="true">
      	                    			<TD>
													<TABLE cellSpacing=2 cellPadding=0>	
														<TR>
															<TD><STRONG>Year&nbsp;&nbsp;</STRONG></TD>
															<TD><STRONG>From:</STRONG></TD>
															<TD>
							                      		<html:select property="fromYear" styleClass="dr-menu">
									                      	<html:optionsCollection name="aimQuarterlyInfoForm" property="years" 
																	value="year" label="year"/>
																</html:select>
															</TD>
															<TD><STRONG>To:</STRONG></TD>
															<TD>
						   	                     	<html:select property="toYear" styleClass="dr-menu">
																	<html:optionsCollection name="aimQuarterlyInfoForm" property="years" 
																	value="year" label="year"/>
																</html:select>
															</TD>															
														</TR>
													</TABLE>
                  	        			</TD>												
               	           			</logic:equal>
												<TD>
													<html:submit value="GO" styleClass="dr-menu"/>
			                        	</TD>
											</TR>
											</TABLE>
											</logic:equal>
										</TD></TR>
										<TR><TD>
						            	<TABLE cellSpacing=0 cellPadding=0 border=0 bgColor=#ffffff width="100%" vAlign="top" align="left">
												<TR><TD>
			                       	
													<logic:notEqual name="aimQuarterlyInfoForm" property="perspective" value="DI">
					                      	<TABLE width="100%"  border="0" cellpadding="4" cellspacing="1" class="box-border-nopadding">
			                     		   	<tr bgcolor="#DDDDDB" > 
			                          				<td bgcolor="#DDDDDB"  >
			   	                   					<div align="center">
					                          				<digi:trn key="aim:year">Year</digi:trn> 
				   	                       			</div>
			         		                 		</td>
			               		           		<td bgcolor="#DDDDDB">
			                     		     			<div align="center">
			                          						<digi:trn key="aim:dateDisbursed">Date Disbursed</digi:trn>
			                           				</div>
					                          		</td>
					                          		<td bgcolor="#DDDDDB">
			      		                    			<div align="center">
																	<FONT color="blue">*</FONT>
			            		              				<digi:trn key="aim:plannedAmount">Planned Amount</digi:trn> 
			                  		        			</div>
			                        		  		</td>
			                          				<td bgcolor="#DDDDDB">
					                          			<div align="center">
																	<FONT color="blue">*</FONT>																
					                          				<digi:trn key="aim:actualAmount">Actual Amount</digi:trn>
			   		                       			</div>
			         			              		</td>
			                  				  	</tr>
					                        	<logic:empty name="aimQuarterlyInfoForm" property="quarterlyInfo">
					                        		<tr valign="top"> 
			   		                       			<td colspan="5" align="center"><span class="note">No records!</td>
			         		                 		</tr>
			               		         	</logic:empty>
			                        			<logic:notEmpty name="aimQuarterlyInfoForm" property="quarterlyInfo">
														<logic:iterate name="aimQuarterlyInfoForm" property="quarterlyInfo" 
														id="qtr" type="org.digijava.module.aim.helper.QuarterlyInfo">
					                           	<logic:equal name="qtr" property="display" value="true">
						                          	<tr valign="top"> 
			      					              		<td valign="baseline" bgcolor="#F8F8F5">
				                     		     			<logic:equal name="qtr" property="aggregate" value="0">
			   	                       						<bean:write name="qtr" property="fiscalYear" />
			      	                    					</logic:equal>
						                          		</td>
			          			                		<td valign="baseline" bgcolor="#F8F8F5">
			                  			              	<logic:equal name="qtr" property="aggregate" value="1">
			                          							&nbsp;&nbsp;&nbsp;&nbsp;
				                          						<bean:write name="qtr" property="dateDisbursed" />
						                          			</logic:equal>
			   			                       			<logic:equal name="qtr" property="aggregate" value="0">
			            			              				<c:set target="${urlQuarterlyGrouping}" property="fiscalYrGrp">
																			<bean:write name="qtr" property="fiscalYear"/>
																		</c:set>
																		<c:set target="${urlQuarterlyGrouping}" property="fiscalQtrGrp">
																			<bean:write name="qtr" property="fiscalQuarter"/>
																		</c:set>
			         			                 				<logic:equal name="qtr" property="plus" value="true">
			                  		        						<c:set target="${urlQuarterlyGrouping}" property="clicked" value="plus"/>
							            						 		<digi:link href="/viewQuarterlyGrouping.do" name="urlQuarterlyGrouping"
																			style="TEXT-DECORATION: NONE">+</digi:link>
						                          			 	</logic:equal>
				              		            			 	<logic:equal name="qtr" property="plus" value="false">
			   	               		        			 		<c:set target="${urlQuarterlyGrouping}" property="clicked" value="minus"/>
				                        		  			 		<digi:link href="/viewQuarterlyGrouping.do" name="urlQuarterlyGrouping" 
																			style="TEXT-DECORATION: NONE">-</digi:link>
						                          			 	</logic:equal>
						                          			 	<logic:equal name="qtr" property="fiscalQuarter" value="0">
			   			                       			 		NA
			      	   		                 			 	</logic:equal>
			         	      		           			 	<logic:equal name="qtr" property="fiscalQuarter" value="1">
			            	         		     			 		1st quarter
			               	           					 	</logic:equal>
			                  	         				 	<logic:equal name="qtr" property="fiscalQuarter" value="2">
			                     	     			 				2nd quarter
					                  	        			 	</logic:equal>
					                     	     			 	<logic:equal name="qtr" property="fiscalQuarter" value="3">
			   		                     	  			 		3rd quarter
			         		                 				 	</logic:equal>
			               		           				 	<logic:equal name="qtr" property="fiscalQuarter" value="4">
			                     		     				 		4th quarter
			                          					 		</logic:equal>
							                          		</logic:equal>
						                          		</td>
			                   			       		<td valign="baseline" bgcolor="#F8F8F5">
			                          						<div align="right"><bean:write name="qtr" property="plannedAmount" /></div>
						                          		</td>
			                          					<td valign="baseline" bgcolor="#F8F8F5">
			                          						<div align="right"><bean:write name="qtr" property="actualAmount" /></div>
						                          		</td>
					                           	</tr>
			                           			</logic:equal>
														</logic:iterate>
														</logic:notEmpty>
					                        </TABLE>
					                        </logic:notEqual>
												</TD></TR>
												<TR><TD>
												
			                        		<logic:equal name="aimQuarterlyInfoForm" property="perspective" value="DI">
					                        <TABLE width="100%"  border="0" cellpadding="4" cellspacing="1" class="box-border-nopadding">
							            			<tr bgcolor="#DDDDDB" > 
				   			           				<td height="30" bgcolor="#DDDDDB">
				            			  					<div align="center">Year</div>
				                  					</td>
										              	<td bgcolor="#DDDDDB" width="11%">
										              		<div align="center">Quarter</div>
										              	</td>
							              				<td bgcolor="#DDDDDB">
							              					<div align="center"><p>Donor Planned</p></div>
							                			</td>
											            <td bgcolor="#DDDDDB">
											            	<div align="center">Impl. Agency Planned</div>
							              				</td>
										              	<td bgcolor="#DDDDDB">
										              		<div align="center">MOFED Planned </div>
							              				</td>
										              	<td bgcolor="#DDDDDB">
										              		<div align="center">Donor Actuals</div>
							              				</td>
										              	<td bgcolor="#DDDDDB">
										              		<div align="center">Impl. Agency Actuals</div>
										              	</td>
							      			        	<td bgcolor="#DDDDDB">
							              					<div align="center">MOFED Actuals</div>
							              				</td>
							            			</tr>
							            			<logic:empty name="aimQuarterlyInfoForm" property="discrepancies" >
			         	               		<tr valign="top"> 
			            	              			<td colspan="8" align="center"><span class="note">No records!</td>
			               	           		</tr>
			                  		      	</logic:empty>
				            						<logic:notEmpty name="aimQuarterlyInfoForm" property="discrepancies">
															<logic:iterate name="aimQuarterlyInfoForm" property="discrepancies" 
															id="discrepancy" type="org.digijava.module.aim.helper.QuarterlyDiscrepancy">	
												            <logic:equal name="discrepancy" property="aggregate" value="0">
							   						         <tr valign="top"> 
																		<td height="30" bgcolor="#F8F8F5">
																			<logic:equal name="discrepancy" property="fiscalYear" value="0">
				      	                  					  		NA
										                          	</logic:equal>
				         						                 	<logic:notEqual  name="discrepancy" property="fiscalYear" value="0">
																				<bean:write name="discrepancy" property="fiscalYear" />
																			</logic:notEqual>
																		</td>
																		<td bgcolor="#F8F8F5">
																			<logic:equal name="discrepancy" property="fiscalQuarter" value="0">
		                          			 							NA
																			</logic:equal>
																			<logic:equal name="discrepancy" property="fiscalQuarter" value="1">
																				1st quarter
																			</logic:equal>
																			<logic:equal name="discrepancy" property="fiscalQuarter" value="2">
																				2nd quarter
																			</logic:equal>
																			<logic:equal name="discrepancy" property="fiscalQuarter" value="3">
							                          			 		3rd quarter
		         						              			 	</logic:equal>
		                           							 	<logic:equal name="discrepancy" property="fiscalQuarter" value="4">
		                          			 							4th quarter
													              		</logic:equal>
																		</td>
																		<td bgcolor="#F8F8F5">
																			<div align="right">
																				<bean:write name="discrepancy" property="donorPlanned"/>
																			</div>
																		</td>
																		<td bgcolor="#F8F8F5">
																			<div align="center">
																				<bean:write name="discrepancy" property="implAgencyPlanned"/>
																			</div>
																		</td>
																		<td bgcolor="#F8F8F5">
																			<div align="right"> 
																				<bean:write name="discrepancy" property="mofedPlanned"/>
																			</div>
																		</td>
																		<td bgcolor="#F8F8F5">
																			<div align="right">
																				<bean:write name="discrepancy" property="donorActual"/>
																			</div>
																		</td>
																		<td bgcolor="#F8F8F5">
																			<div align="center">
																				<bean:write name="discrepancy" property="implAgencyActual"/>
																			</div>
																		</td>
																		<td bgcolor="#F8F8F5">
																			<div align="right"> 
																				<bean:write name="discrepancy" property="mofedActual"/>
																			</div>
																		</td>
																	</tr>
																</logic:equal>
															</logic:iterate>
														</logic:notEmpty>
							       				</TABLE>
				                        	</logic:equal>
												</TD></TR>
												<TR><TD align="right">
													<TABLE cellspacing="0" cellpadding="0" vAlign="top">
														<TR>
															<TD width="15">
																<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
															</TD>
															<TD>
									        					<c:set target="${urlSubTabs}" property="transactionType">
			        												<bean:write name='aimQuarterlyInfoForm' property='transactionType'/>
									        					</c:set>
			<bean:define id="translation">
				<digi:trn key="aim:clickToViewYearlyInfo">Click here to view Yearly Info</digi:trn>
			</bean:define>
			        											<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" title="<%=translation%>" >
				      	  										<STRONG>
																		Show Yearly 
																	</STRONG>
						   		     						</digi:link>															
															</TD>
														</TR>
													</TABLE>
												</TD></TR>
												<TR><TD>
													<FONT color=blue>*
													<digi:trn key="aim:allTheAmountsInThousands">	
													All the amounts are in thousands (000)</digi:trn></FONT>
												</TD></TR>												
											</TABLE>
										</TD></TR>
									</TABLE>
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>

			</TD></TR>

			</TABLE>
		<!-- end -->
	</TD>
</TR>
<TR><TD>&nbsp;</TD></TR>
</TABLE>

</digi:form>
</logic:equal>	
