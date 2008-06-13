<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@page import="org.digijava.module.aim.util.FeaturesUtil"%>
<%@page import="org.digijava.module.aim.helper.GlobalSettingsConstants"%>
<HTML>

	<digi:base />

	<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

	<digi:context name="digiContext" property="context"/>



	<HEAD>

	<TITLE>
			<%
			String title=(String)((org.apache.struts.tiles.ComponentContext) request.getAttribute("org.apache.struts.taglib.tiles.CompContext")).getAttribute("title");
			String key=(title.replaceAll(" ",""));
			%>
			<c:set var="key">aim:pagetitle:<%=key%></c:set>
				<digi:trn key="aim:pagetitle:amp">AMP </digi:trn> 
				<digi:trn key="aim:pagetitle:${key}">
					<%=title%>
				</digi:trn>
		</TITLE>
		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">

		<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">

     	<META HTTP-EQUIV="EXPIRES" CONTENT="0">



		<SCRIPT type="text/javascript">

			function formSubmit()	{

				document.forms[0].submit();

			}

			function quitRnot1(message)
			{

			}

		</SCRIPT>

	</HEAD>



	<BODY leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">

		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top" align="left">

			<TBODY>

			<TR height="15">

				<TD width="100%" bgColor=#323232 vAlign="center" align="left" height="20">

					<digi:insert attribute="headerTop" />

				</TD>

			</TR>

			<TR height="15">

				<TD width="100%" align="center" bgcolor="#376091">
					<TABLE cellSpacing=0 cellPadding=0 width="98%">

						<TBODY>

						  	<TR>

						   	<TD align="left" vAlign="center">

									<digi:insert attribute="headerMiddle" />

								</TD>

							<td align="right" vAlign="top" height="15">

									<digi:insert attribute="loginWidget" />

							</td>


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

					<TABLE bgColor=#ffffff cellPadding=0 cellSpacing=0 width="770" vAlign="top" align="left" border=0>

						<TR>

							<TD class=r-dotted-lg width="10">&nbsp;</td>

							<TD align=center vAlign=top class=r-dotted-lg width="770">

								<TABLE width="760" cellPadding=0 cellSpacing=0 vAlign="top" align="left" border="0">

									<TR><TD vAlign="bottom" align="center" width="770">

										<digi:insert attribute="tabHeader" />

									</TD></TR>

									<TR><TD vAlign="top" align="center" width="770">

										<digi:insert attribute="tabBody" />

									</TD></TR>

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

