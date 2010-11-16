<%@ page language="java" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>


<!-- FOOTER START -->
	<div class="footer">
		<center>
			<img src="img_2/logo_footer.gif" />AMP 1.13.42 build 08.Feb.2010 - Developed in partnership with OECD, UNDP, WB, Government of Ethiopia and DGF
			<logic:notEmpty name="currentMember" scope="session">
				<digi:secure actions="ADMIN">
            		<a href='<digi:site property="url"/>/admin/'>Admin</a>
            		<a href='<digi:site property="url"/>/admin/switchDevelopmentMode.do'><digi:trn key="admin:devMode">User/Dev Mode</digi:trn></a>
       			</digi:secure>
			</logic:notEmpty>
		</center>
	 </div>
	 
<!-- FOOTER END  -->
	
