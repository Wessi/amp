<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<HTML>
	<digi:base />
	<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
	<digi:context name="digiContext" property="context"/>

	<HEAD>
		<TITLE>AMP<tiles:getAsString name="title"/></TITLE>
		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">
		<META HTTP-EQUIV="Expires" CONTENT="0">
		<META HTTP-EQUIV="Cache-Control" CONTENT="private">		
		<script language="javascript">
		function quitRnot()
		{
		}
		</script>
		
	</HEAD>

	<BODY leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top" align="left">
			<TBODY>
			<TR>
				<TD width="100%" bgColor=#323232 vAlign="center" align="left" height="40">
					<digi:insert attribute="headerTop" />
				</TD>
			</TR>
			<TR>
				<TD width="100%" align="center" vAlign="top" bgcolor="#5a5a58">
					<TABLE cellSpacing=0 cellPadding=0 width="98%" border=0 vAlign="center" bgcolor="#5a5a58">
						<TBODY>
						  	<TR bgColor=#5a5a58 height="28">
						   	<TD align="left" vAlign="center" height="28">
									<digi:insert attribute="headerMiddle" />
								</TD>	
							  	<TD align="right" vAlign="top" height="28">
									<digi:insert attribute="dropdownLangSwitch" />
								</TD>
							</TR>
						</TBODY>
					</TABLE>
				</TD>
			</TR>
			<TR>
				<TD width="100%" vAlign="top" align="left">
					<jsp:include page="../../../repository/aim/view/teamPagesHeader.jsp" flush="true" />				
				</TD>
			</TR>
			<TR>
				<TD width="100%" vAlign="top" align="left">
					<TABLE bgColor=#ffffff cellPadding=0 cellSpacing=0 width="99%" vAlign="top" align="left" border=0>
						<TR>
							<TD class=r-dotted-lg width="10">&nbsp;</td>
							<TD align=center vAlign=top>
								<TABLE width="100%" cellPadding=0 cellSpacing=0 vAlign="top" align="left" border="0">
									<TR>
										<TD vAlign="top" align="left" width="75%" class=r-dotted-lg>
											<digi:insert attribute="body" />
										</TD>
										<TD vAlign="top" align="left" width="25%" class=r-dotted-lg bgcolor="#f4f4f2">
											<TABLE width="100%" cellPadding=0 cellSpacing=0 vAlign="top" align="left" border="0">
												<TR><TD vAlign="top" align="left">
													<digi:insert attribute="myReports" />
												</TD></TR>
												<bean:define id="lead" property="teamHead" name="currentMember" scope="session"/>
												<c:if test="${lead == true}">
												<TR><TD vAlign="top" align="left">
													<digi:insert attribute="myTasks" />
												</TD></TR>
												</c:if>
												<TR><TD vAlign="top" align="left">
													<digi:insert attribute="myLinks" />
												</TD></TR>
												<TR><TD vAlign="top" align="left">
													<digi:insert attribute="myTeamMembers" />
												</TD></TR>
											</TABLE>
										</TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
					</TABLE>
				</TD>
			</TR>
			<TR>
				<TD width="100%"  bgcolor="#323232">
				   <digi:insert attribute="footer" />
				</TD>
			</TR>
			</TBODY>
		</TABLE>
	</BODY>
</HTML>
