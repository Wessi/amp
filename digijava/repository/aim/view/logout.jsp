<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<table cellpadding="0" cellspacing="0" width="100%" background="module/aim/images/bg-home.jpg">
	<tr>
		<td valign="top">
			<table class=r-dotted height=93 cellSpacing=0 cellPadding=0 width=757 
			background="module/aim/images/bg-home.jpg" valign="top">
				<tr>
					<td>
						<table cellSpacing=0 cellPadding=0 width="100%">
							<tr>
								<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>	
								<td class=home-bg-header rowSpan=2 width="100%" valign="top">
									<P>
										<digi:trn key="aim:ampSiteTitle">
											MINISTRY OF FINANCE AND ECONOMIC DEVELOPMENT: ETHIOPIA
										</digi:trn>
									</P> 

								</td>
							</tr>      
							<tr>
							  	<td>&nbsp;</td>
							</tr>
							<tr>
							  	<td>&nbsp;</td>
								<td class=home-bg-text valign="top">
								<digi:trn key="aim:ampUserLogout">
								Log out
								</digi:trn>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<table bgColor=#ffffff border=0 cellPadding=0 cellSpacing=0 width=757>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;
		</td>
		<td align=left class=r-dotted-lg vAlign=top width=520><br>
			<table border=0 cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td width="3%">&nbsp;</td>				
					<td align=center class=f-names noWrap width="31%">
							<digi:trn key="aim:ampLogout">
							You have been logged out
							</digi:trn>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>				
					<td align=center>
					<bean:define id="translation">
						<digi:trn key="aim:clickToGoBackToHomePage">Click here to go back to the Home Page</digi:trn>
					</bean:define>
					<digi:link href="/" title="<%=translation%>" >
					<digi:trn key="aim:goBackToHomePage">
					Click here to go back to the home page
					</digi:trn>
					</digi:link>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>				
					<td>&nbsp;</td>
				</tr>				
				<tr>
					<td>&nbsp;</td>				
					<td>&nbsp;</td>
				</tr>				
				<tr>
					<td>&nbsp;</td>				
					<td>&nbsp;</td>
				</tr>				
				<tr>
					<td>&nbsp;</td>				
					<td>&nbsp;</td>
				</tr>				
				<tr>
					<td>&nbsp;</td>				
					<td>&nbsp;</td>
				</tr>				
				<tr>
					<td>&nbsp;</td>				
					<td>&nbsp;</td>
				</tr>				
				<tr>
					<td>&nbsp;</td>				
					<td>&nbsp;</td>
				</tr>				
				
			</table>
		</td>
	</tr>
</table>
