<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>

<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<div style='position:relative;display:none;' id='legendPopup'> 
	<ul>
		<li>
			<font color="GREEN">* "green Text"</font> - <digi:trn key="aim:newUnvalidatedText">New Unvalidated. These are new activites that have never been approved by the workspace manager</digi:trn>
		</li>
		<li>
			<font color="GREEN">"green text"</font> - <digi:trn key="aim:existingUnvalidatedText">Existing Un-validated. These are activites that have been approved at least once by the workspace manager. These are edited actvities that are not approved.</digi:trn>
		</li>
		<li>
			<font color="RED">* "red"</font> - <digi:trn key="aim:newDraftText">New Draft. These are new actvities that have been saved as drafts.</digi:trn>
		</li>
		<li>
			<font color="RED">"red"</font> - <digi:trn key="aim:existingDraftsText">Existing Drafts. These are actvities that have been saved once and edited and saved as drafts.</digi:trn>
		</li>
	</ul>
</div>
<div align="left" width="2" style="display: inline"
	onMouseOver="stm(['Legend',document.getElementById('legendPopup').innerHTML],Style[0])" 
	onMouseOut="htm()" onclick="stm(['Legend',document.getElementById('legendPopup').innerHTML],Style[0])">
	<c:set var="translation">
		<digi:trn key="aim:clickToViewLegend">Click here to view Legend</digi:trn>
	</c:set>
	<span style="font-style: italic;color: #093678">
	<digi:trn key="aim:showLegend">Show Legend</digi:trn>
	</span>
</div>