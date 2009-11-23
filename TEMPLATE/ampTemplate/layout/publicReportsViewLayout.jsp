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

		<TITLE>
			<%
			String title=(String)((org.apache.struts.tiles.ComponentContext) request.getAttribute("org.apache.struts.taglib.tiles.CompContext")).getAttribute("title");
			String key=(title.replaceAll(" ",""));
			%>
				<c:set var="key">aim:pagetitle:<%=key%></c:set>
				<digi:trn key="aim:pagetitle:amp">AMP </digi:trn> 
				<digi:trn key="${key}">
					<%=title%>
				</digi:trn>
		</TITLE>



		<script language="javascript">

		function quitRnot1(message)
		{

		}

		</script>



	</HEAD>



	<BODY leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">

		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top" align="left">

			<TBODY>

			<TR>

				<TD width="100%" bgColor=#323232 vAlign="center" align="left" height="15">

					<digi:insert attribute="headerTop" />

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

