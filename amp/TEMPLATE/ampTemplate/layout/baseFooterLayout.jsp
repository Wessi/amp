<%@ page language="java"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>

<div id="amp-footer" style="footer"></div>

<script type="text/javascript">
  var boilerplate = new window.boilerplate({
    showAdminFooter: true,
    showLogin: <digi:secure authenticated="true">false</digi:secure><digi:secure authenticated="false">true
    </digi:secure>,
    loginDropdown: true
  });
</script>