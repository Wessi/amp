<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<digi:instance property="quartzJobManagerForm" />
<digi:form action="/quartzJobNamager.do" method="post">
<table>
  <tr>
    <td>
    ${quartzJobManagerForm.msText}
    </td>
  </tr>
</table>
</digi:form>
