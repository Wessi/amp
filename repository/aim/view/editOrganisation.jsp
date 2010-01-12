<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/aim"prefix="aim"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<digi:ref href="css/new_styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript" type="text/javascript">
    <jsp:include page="scripts/calendar.js.jsp" flush="true" />
</script>
<jsp:include page="scripts/newCalendar.jsp" flush="true" />
<style type="text/css">
    .selectStyle {
        Font-size:11px;
        font-family:Arial;
        width:210px;
    }
    .tableEven {
        background-color:#dbe5f1;
        border-left:none;
        border-right:none;
        font-size: 10px;
        font-family:Arial;
    }

    .tableOdd {
        background-color:#FFFFFF;
        border-left:none;
        border-right:none;
        font-size: 11px;
        font-family:Arial;
       !important

    }
    .tableHeader {
        background-color:#222e5d;
        color:white;
        padding:2px;
    }
    .Hovered {
        background-color:#a5bcf2;
    }
    input,textArea{
        font-family:Arial;
        font-size: 11px;
    }
    .tdClass{
        font-family:Arial;
        font-size: 11px;
    }
    .tdBoldClass{
        font-family:Arial;
        font-size: 11px;
        font-weight:bold;
    }
     .legendClass{
        font-family:Arial;
        font-size: 13px;
        font-weight:bold;
        color:#0000FF;
    }
</style>

<jsp:include page="/repository/aim/view/addEditOrganizationsPopin.jsp" flush="true" />
<jsp:include page="/repository/aim/view/addOrganizationPopin.jsp" flush="true" />
<jsp:include page="/repository/aim/view/components/contactScripts.jsp" flush="true" />

<script language="JavaScript" type="text/javascript">
    function initScripts() {
        initSectorScript();
        initContactScript();
        initOrganizationScript();
    }
    addLoadEvent(initScripts);
  
    function refreshPage(){
        document.aimAddOrgForm.actionFlag.value='reload'
        document.aimAddOrgForm.submit();
    }

    
    
    function addOrganizations2Contact(){
        var params=getContactParams();
    <digi:context name="addCont" property="context/addAmpContactInfo.do?action=addOrganizations"/>;
            checkAndClose=true;
            var url="${addCont}"+"&"+params;
            YAHOOAmp.util.Connect.asyncRequest("POST", url, callback1);
        }
  
    function orgTypeChanged(){
    <digi:context name="typeChanged" property="context/module/moduleinstance/editOrganisation.do" />
            document.aimAddOrgForm.actionFlag.value="typeChanged";
            document.aimAddOrgForm.action = "${typeChanged}";
            document.aimAddOrgForm.target = "_self";
            document.aimAddOrgForm.submit();
        }
        function addStaff(){
            var year=document.aimAddOrgForm.selectedYear;
            var type=document.aimAddOrgForm.typeOfStaff;
            var number=document.aimAddOrgForm.numberOfStaff;
            var errorMsg= '<digi:trn jsFriendly="true">Please enter numeric value only</digi:trn>';
            if (isNaN(number.value)||number.value=='') {
                alert(errorMsg);
                number.value = "";
                return false;
            }

            if(year.value=='-1'){
                errorMsg='<digi:trn jsFriendly="true">Please select year!</digi:trn>';
                alert(errorMsg);
                return false;
            }
            if(type.value=='0'){
                errorMsg='<digi:trn jsFriendly="true">Please select type!</digi:trn>';
                alert(errorMsg);
                return false;
            }

    <digi:context name="addStaff" property="context/module/moduleinstance/editOrganisation.do" />
            document.aimAddOrgForm.actionFlag.value="addStaffInfo";
            document.aimAddOrgForm.action = "${addStaff}";
            document.aimAddOrgForm.target = "_self";
            document.aimAddOrgForm.submit();
        }
        function deleteStaff(staffId){
            if(staffId!=null&&staffId!=""&&typeof(staffId)!='undefined'){
                document.aimAddOrgForm.selectedStaffId.value=staffId;
            }
            else{
                document.aimAddOrgForm.selectedStaffId.value=null;
            }
    <digi:context name="deleteStaff" property="context/module/moduleinstance/editOrganisation.do" />
            document.aimAddOrgForm.actionFlag.value="deleteStaffInfo";
            document.aimAddOrgForm.action = "${deleteStaff}";
            document.aimAddOrgForm.target = "_self";
            document.aimAddOrgForm.submit();
        }
        function addOrgInfo(){
            var year=document.aimAddOrgForm.orgInfoSelectedYear;
            var type=document.aimAddOrgForm.orgInfoType;
            var percent=document.aimAddOrgForm.orgInfoPercent;
            var amount=document.aimAddOrgForm.orgInfoAmount;
            var currId=document.aimAddOrgForm.orgInfoCurrId;
            var errorMsg;
          

            if(year.value=='-1'){
                errorMsg='<digi:trn jsFriendly="true">Please select year!</digi:trn>';
                alert(errorMsg);
                return false;
            }
            if(currId.value=='-1'){
                errorMsg='<digi:trn jsFriendly="true">Please select currency!</digi:trn>';
                alert(errorMsg);
                return false;
            }
            if(type.value=='-1'){
                errorMsg='<digi:trn jsFriendly="true">Please select type!</digi:trn>';
                alert(errorMsg);
                return false;
            }

            if(type.value=='2'&&percent.value.trim()==''){
                errorMsg='<digi:trn jsFriendly="true">Please enter percent!</digi:trn>';
                alert(errorMsg);
                return false;
            }
            if (!(/^\d*\.?\d*%?$/.test(percent.value))) {
                errorMsg='<digi:trn jsFriendly="true">Please enter correct percent value!</digi:trn>';
                alert(errorMsg);
                percent.value = "";
                return false;
            }
            if (!(/^\d+\.?\d*$/.test(amount.value))) {
                errorMsg='<digi:trn jsFriendly="true">Please enter correct amount value!</digi:trn>';
                alert(errorMsg);
                amount.value = "";
                return false;
            }

    <digi:context name="addOrgInfo" property="context/module/moduleinstance/editOrganisation.do" />
            document.aimAddOrgForm.actionFlag.value="addOrgInfo";
            document.aimAddOrgForm.action = "${addOrgInfo}";
            document.aimAddOrgForm.target = "_self";
            document.aimAddOrgForm.submit();
        }
        function deleteOrgInfo(orginfoId){
            if(orginfoId!=null&&orginfoId!=""&&typeof(orginfoId)!='undefined'){
                document.aimAddOrgForm.selectedOrgInfoId.value=orginfoId;
            }
            else{
                document.aimAddOrgForm.selectedOrgInfoId.value=null;
            }
    <digi:context name="deleteOrgInfo" property="context/module/moduleinstance/editOrganisation.do" />
            document.aimAddOrgForm.actionFlag.value="deleteOrgInfo";
            document.aimAddOrgForm.action = "${deleteOrgInfo}";
            document.aimAddOrgForm.target = "_self";
            document.aimAddOrgForm.submit();
        }
        function setStyle(table,hasTitle){
            if(table!=null){
                table.className += " tableElement";
                setStripsTable(table.id, "tableEven", "tableOdd");
                setHoveredTable(table.id, hasTitle);
            }

        }
        function selectAll(className){
            $("."+className).each(function () {
                this.checked=true;
            });

        }
        function addSectors() {
            var sectorSchemeId=document.aimAddOrgForm.ampSecSchemeId;
            if(sectorSchemeId.value=="-1"){
                alert("Please select sector scheme");
            }
            else{
                myAddSectors("sectorScheme="+sectorSchemeId.value+"&configId=1");
            }
        }
        function removeSectors() {
    <digi:context name="removeSectors" property="context/module/moduleinstance/editOrganisation.do" />
            document.aimAddOrgForm.actionFlag.value="removeSector";
            document.aimAddOrgForm.action = "${removeSectors}";
            document.aimAddOrgForm.target = "_self";
            document.aimAddOrgForm.submit();
        }
        function addSector() {
    <digi:context name="addSectors" property="context/module/moduleinstance/editOrganisation.do" />
            document.aimAddOrgForm.actionFlag.value="addSector";
            document.aimAddOrgForm.action = "${addSectors}";
            document.aimAddOrgForm.target = "_self";
            document.aimAddOrgForm.submit();
        }
        function removeOrgs() {
    <digi:context name="remOrgs" property="context/module/moduleinstance/editOrganisation.do" />
            document.aimAddOrgForm.action = "${remOrgs}";
            document.aimAddOrgForm.actionFlag.value="removeRecipient";
            document.aimAddOrgForm.target = "_self"
            document.aimAddOrgForm.submit();
        }
        function selectLocation() {
            var params="implemLocationLevel="+document.getElementsByName("implemLocationLevel")[0].value+"&showLocLevelSelect=false";
            myAddLocation(params);
        }
        function removeSelLocations(){
    <digi:context name="remLocs" property="context/module/moduleinstance/editOrganisation.do" />
            document.aimAddOrgForm.action = "${remLocs}";
            document.aimAddOrgForm.target = "_self"
            document.aimAddOrgForm.actionFlag.value="deleteLocation";
            document.aimAddOrgForm.submit();
        }
        function removeAllLocations(){
            try{

                var checkedItems = document.aimAddOrgForm.selLocs;
                if(checkedItems.length > 0){
                    for(var a=0;a<checkedItems.length;a++){
                        checkedItems[a].checked = true;
                    }
                    removeSelLocations();
                }else if(checkedItems!=null){
                    checkedItems.checked = true;
                    removeSelLocations();
                }else{
                    return false;
                }
                return true;
            }
            catch(err){
                return false;

            }

        }
        function fnChk(frmContrl,exceedhundred){
    <c:set var="errMsgAddSectorNumericValue">
        <digi:trn>Please enter numeric value only</digi:trn>
    </c:set>

            if (isNaN(frmContrl.value)) {
                alert("${errMsgAddSectorNumericValue}");
                frmContrl.value = "";
                return false;
            }
            if (!exceedhundred&&frmContrl.value > 100) {

    <c:set var="errMsgAddSumExceed">
        <digi:trn key="aim:addRegionSumExceedErrorMessage">
                    Region percentage can not exceed 100
        </digi:trn>
    </c:set>
                alert("${errMsgAddSumExceed}");
                frmContrl.value = "";
                return false;
            }
            return true;
        }
        function removeContact(selContactId){
    		<digi:context name="remLocs" property="context/module/moduleinstance/editOrganisation.do" />
            document.aimAddOrgForm.action = "${remLocs}";
            document.aimAddOrgForm.target = "_self"
            document.aimAddOrgForm.actionFlag.value="deleteContact";
            document.aimAddOrgForm.selContactId.value=selContactId;
            document.aimAddOrgForm.submit();

        }
        function addPledge() {
    <digi:context name="addPledge" property="context/module/moduleinstance/editOrganisation.do" />
            document.aimAddOrgForm.action = "${addPledge}"
            document.aimAddOrgForm.actionFlag.value="addPledge";
            document.aimAddOrgForm.target = "_self";
            document.aimAddOrgForm.submit();
        }
        function removeFundingDetail(index) {
            var flag = confirm('<digi:trn  jsFriendly="true" key="aim:areYouSureRemoveTransaction">Are you sure you want to remove the selected transaction ?</digi:trn>');
            if(flag != false) {
    <digi:context name="deletePledge" property="context/module/moduleinstance/editOrganisation.do" />
                document.aimAddOrgForm.action = "${deletePledge}";
                document.aimAddOrgForm.actionFlag.value="deletePledge";
                document.aimAddOrgForm.target = "_self";
                document.aimAddOrgForm.transIndexId.value=index;
                document.aimAddOrgForm.submit();
            }
        }
        function msg() {
            if (confirm('<digi:trn  jsFriendly="true"  key="aim:organization:deleteQuestion">Are you sure about deleting this organization?</digi:trn>')) {
    <digi:context name="delete" property="context/module/moduleinstance/editOrganisation.do" />
                document.aimAddOrgForm.action = "${delete}";
                document.aimAddOrgForm.actionFlag.value = "delete";
                document.aimAddOrgForm.submit();
            }
            else
                return false;
        }

        function move() {
    <digi:context name="selectLoc" property="context/module/moduleinstance/organisationManager.do" />
            url = "<%= selectLoc %>?orgSelReset=true";
            document.location.href = url;
        }
        function check() {

            var type=document.aimAddOrgForm.type;
            var name = document.aimAddOrgForm.name.value;
            if ( name == null||name.length == 0) {
                alert('<digi:trn  jsFriendly="true">Please enter name for this Organization.</digi:trn>');
                document.aimAddOrgForm.name.focus();
                return false;
            }
            var acronym = document.aimAddOrgForm.acronym.value;
            if (acronym == null||acronym.length == 0) {
                alert('<digi:trn  jsFriendly="true">Please enter acronym for this Organization.</digi:trn>');
                document.aimAddOrgForm.acronym.focus();
                return false;
            }
            var ampOrgTypeId= document.aimAddOrgForm.ampOrgTypeId.value;
            if (ampOrgTypeId == '-1' || ampOrgTypeId == null) {
                alert('<digi:trn  jsFriendly="true">Please Select Organization Type.</digi:trn>');
                document.aimAddOrgForm.ampOrgTypeId.focus();
                return false;
            }
            var ampOrgGrpId= document.aimAddOrgForm.ampOrgGrpId.value;
            if (ampOrgGrpId == '-1' || ampOrgGrpId == null) {
                alert('<digi:trn  jsFriendly="true">Please Select Organization Group.</digi:trn>');
                document.aimAddOrgForm.ampOrgGrpId.focus();
                return false;
            }
            // We have different mandatory fields for NGOs and others....
            if(type.value=='NGO'){
                var orgPrimaryPurpose= document.aimAddOrgForm.orgPrimaryPurpose.value;
                if (orgPrimaryPurpose == null||orgPrimaryPurpose.length == 0 ) {
                    alert('<digi:trn  jsFriendly="true">Please enter primary purpose for this Organization.</digi:trn>');
                    document.aimAddOrgForm.orgPrimaryPurpose.focus();
                    return false;
                }
                var regNumbMinPlan= document.aimAddOrgForm.regNumbMinPlan.value;
                if (regNumbMinPlan == null||regNumbMinPlan.length == 0 ) {
                    alert('<digi:trn  jsFriendly="true">Please enter Registration Number in MinPlan for this Organization.</digi:trn>');
                    document.aimAddOrgForm.regNumbMinPlan.focus();
                    return false;
                }
                var minPlanRegDate= document.aimAddOrgForm.minPlanRegDate.value;
                if (minPlanRegDate == null||minPlanRegDate.length == 0 ) {
                    alert('<digi:trn  jsFriendly="true">Please enter Registration Date in MinPlan for this Organization.</digi:trn>');
                    document.aimAddOrgForm.minPlanRegDate.focus();
                    return false;
                }
                var fiscalCalId= document.aimAddOrgForm.fiscalCalId.value;
                if (fiscalCalId == null||fiscalCalId == '-1') {
                    alert('<digi:trn  jsFriendly="true">Please Select Fiscal Calendar.</digi:trn>');
                    document.aimAddOrgForm.fiscalCalId.focus();
                    return false;
                }

                var selSectors= document.getElementsByName("selSectors");
                if ( selSectors == null||selSectors.length == 0 ) {
                    alert('<digi:trn  jsFriendly="true">Please Select Sectors for this Organization.</digi:trn>');
                    return false;
                }
                var selRecipients= document.getElementsByName("selRecipients");
                if ( selRecipients == null||selRecipients.length == 0) {
                    alert('<digi:trn  jsFriendly="true">Please Select Recipients for this Organization.</digi:trn>');
                    return false;
                }

                var countryId= document.aimAddOrgForm.countryId.value;
                if (countryId == null||countryId == '-1') {
                    alert('<digi:trn  jsFriendly="true">Please Select Country of Origin.</digi:trn>');
                    document.aimAddOrgForm.countryId.focus();
                    return false;
                }
     
                var orgUrl= document.aimAddOrgForm.orgUrl.value;
                if (orgUrl == null||orgUrl.length == 0 ) {
                    alert('<digi:trn  jsFriendly="true">Please Enter URL for this Organization.</digi:trn>');
                    document.aimAddOrgForm.orgUrl.focus();
                    return false;
                }
                var address= document.aimAddOrgForm.address.value;
                if (address == null||address.length == 0 ) {
                    alert('<digi:trn  jsFriendly="true">Please Enter Address for this Organization.</digi:trn>');
                    document.aimAddOrgForm.address.focus();
                    return false;
                }
                var selLocs= document.getElementsByName("selLocs");
                if ( selLocs == null||selLocs.length == 0 ) {
                    alert('<digi:trn  jsFriendly="true">Please Select Locations for this Organization.</digi:trn>');
                    return false;
                }
                else{
                    var sum=0;
                    for(var i=0;i<selLocs.length;i++){
                        var locName="selectedLocs["+i+"].percent"
                        var location= document.getElementsByName(locName)[0];
                        if(location.value==null||parseFloat(location.value)==0){
                            alert('<digi:trn  jsFriendly="true">Please Enter Percent for Location.</digi:trn>');
                            location.focus();
                            return false;
                        }else{
                            sum+=parseFloat(location.value);
                        }
                    }
                    if(sum!=100){
                           alert('<digi:trn  jsFriendly="true">The sum Of percents must equal 100</digi:trn>');
                            return false;
                    }
                }



            }
            else{
                var orgCode= document.aimAddOrgForm.orgCode.value;
                if (orgCode == null||orgCode.length == 0 ) {
                    alert('<digi:trn  jsFriendly="true">Please enter code for this Organization.</digi:trn>');
                    document.aimAddOrgForm.orgCode.focus();
                    return false;
                }
                var budgetOrgCode= document.aimAddOrgForm.budgetOrgCode.value;
                if (budgetOrgCode == null||budgetOrgCode.length == 0 ) {
                    alert('<digi:trn  jsFriendly="true">Please enter Budget Code for this Organization.</digi:trn>');
                    document.aimAddOrgForm.budgetOrgCode.focus();
                    return false;
                }

            }
            return true;

        }
        function addDocumentsDM(documentsType, showTheFollowingDocuments) {
            if (showTheFollowingDocuments==null){
                showTheFollowingDocuments="ALL";
            }
            var url		= "/contentrepository/selectDocumentDM.do?documentsType="+documentsType+"&showTheFollowingDocuments="+showTheFollowingDocuments;
            var popupName	= 'my_popup';
            window.open(url, popupName, 'width=900, height=300');
            document.forms[0].action=url;
            document.forms[0].target=popupName;
            document.forms[0].submit();
        }
        function validateSaveOrg() {
            if(check()){
    <digi:context name="save" property="context/module/moduleinstance/editOrganisation.do" />
                document.aimAddOrgForm.action = "${delete}";
                document.aimAddOrgForm.actionFlag.value = "save";
                document.aimAddOrgForm.submit();
            }
        }

        function addGroup() {
            openNewWindow(600, 400);
    <digi:context name="selectLoc" property="context/module/moduleinstance/editOrgGroup.do" />
            var id = document.aimAddOrgForm.ampOrgId.value;
            url = "<%= selectLoc %>?action=createGroup";
            document.aimAddOrgForm.action = url;
            document.aimAddOrgForm.target = popupPointer.name;
            document.aimAddOrgForm.submit();
        }
        function setStripsTable(tableId, classOdd, classEven) {
            var tableElement = document.getElementById(tableId);
            if(tableElement)
            {
                tableElement.setAttribute("border","0");
                tableElement.setAttribute("cellPadding","0");
                tableElement.setAttribute("cellSpacing","0");
                rows = tableElement.getElementsByTagName('tr');
                for(var i = 0, n = rows.length; i < n; ++i) {
                    if(i%2 == 0)
                        rows[i].className = classEven;
                    else
                        rows[i].className = classOdd;
                }
                rows = null;
            }
        }
        function setHoveredTable(tableId, hasHeaders) {
            var tableElement = document.getElementById(tableId);
            if(tableElement){
                var className = 'Hovered',
                pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
                rows      = tableElement.getElementsByTagName('tr');

                var i = 0;
                if(hasHeaders){
                    rows[0].className += " tableHeader";
                    i = 1;
                    
                   

                }

                for(i, n = rows.length; i < n; ++i) {
                    rows[i].onmouseover = function() {
                        this.className += ' ' + className;
                    };
                    rows[i].onmouseout = function() {
                        this.className = this.className.replace(pattern, ' ');

                    };
                }
                rows = null;
            }



        }
        function expand(suffix){
		var imgId='#img_'+suffix;
		var imghId='#imgh_'+suffix;
		var divId='#div_container_'+suffix;
		$(imghId).show();
		$(imgId).hide();
		$(divId).show('fast');
	}

	function collapse(suffix){
		var imgId='#img_'+suffix;
		var imghId='#imgh_'+suffix;
		var divId='#div_container_'+suffix;
		$(imghId).hide();
		$(imgId).show();
		$(divId).hide('fast');
	}
        function expandAll(){
                $("img[@id^='img_']"+':visible').slideUp('fast');
		$("img[@id^='imgh_']"+':hidden').slideDown('fast');;
		$("div[@id^='div_container_']").slideDown('fast');

         }
         function collapseAll(){
              $("img[@id^='imgh_']"+':visible').slideUp('fast');
              $("img[@id^='img_']"+':hidden').slideDown('fast');
	      $("div[@id^='div_container_']"+':visible').slideUp('fast');
         }

         function exportGeneralInfo(){
            <digi:context name="generalInfo" property="/exportOrganizationToxsl.do?actionMethod=exportGeneralInfo" />;
             //user may click on the export icon before submitting,saving data, this is why we are collecting data manually.
             var url="${generalInfo}"+"&"+ getGeneralInfoParams();
                document.aimAddOrgForm.action = url;
                document.aimAddOrgForm.target = "_self";
                document.aimAddOrgForm.submit();

            }
            function exportInfo(method){
            <digi:context name="information" property="/exportOrganizationToxsl.do" />;
             // adding staff or budget or contact is submit, that is why we don't need to attach this data
              var url="${information}?actionMethod="+method+"&"+ "name="+document.getElementById('orgName').value;
                document.aimAddOrgForm.action = url;
                document.aimAddOrgForm.target = "_self";
                document.aimAddOrgForm.submit();

            }

              function getGeneralInfoParams(){
                      var params="";
                      params+="name="+document.getElementById('orgName').value;
                      if(document.getElementById('regNumbMinPlan')!=null){
                    	  params+="&regNumbMinPlan="+document.getElementById('regNumbMinPlan').value;
                      }
                      if(document.getElementById('minPlanRegDate')!=null){
                    	  params+="&minPlanRegDate="+document.getElementById('minPlanRegDate').value;
                      }
                      if(document.getElementById('fiscalCalId')!=null){
                          params+="&fiscalCalId="+document.getElementById('fiscalCalId').value;
                      }
						if(document.getElementById('orgUrl')!=null){
						     params+="&orgUrl="+document.getElementById('orgUrl').value;                     
						}
						if(document.getElementById('address')!=null){
						    params+="&address="+document.getElementById('address').value;
						}
						if(document.getElementById('addressAbroad')!=null){
						    params+="&addressAbroad="+document.getElementById('addressAbroad').value;
						}
						if(document.getElementById('legalPersonNum')!=null){
						    params+="&legalPersonNum="+document.getElementById('legalPersonNum').value;
						}
						if(document.getElementById('legalPersonRegDate')!=null){
							params+="&legalPersonRegDate="+document.getElementById('legalPersonRegDate').value;    
						}
						if(document.getElementById('countryId')!=null){
							params+="&countryId="+document.getElementById('countryId').value;    
						}
						if(document.getElementById('taxNumber')!=null){
							params+="&taxNumber="+document.getElementById('taxNumber').value;    
						}
						if(document.getElementById('implemLocationLevel')!=null){
							params+="&implemLocationLevel="+ document.getElementsByName("implemLocationLevel")[0].value;    
						}
                      return params;
              }
           
function changePrimaryState(){
	var orgConts= $("input[@id^='primary_']");
	var resetConts=resetPrimary(orgConts);
	if(resetConts==true){
		document.getElementById('primaryOrgCont').value=true;
	}else{
		document.getElementById('primaryOrgCont').value=false;
	}
}

function resetPrimary(contList){
    var retValue=true;
	for(var i=0;i<contList.length;i++){
		if(contList[i].checked){
			retValue=false;
			break;
		}
	}
	return retValue;
}

</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<digi:instance property="aimAddOrgForm" />
<digi:context name="digiContext" property="context" />
<digi:form action="/editOrganisation.do" method="post" onsubmit="changePrimaryState()">
    <html:hidden name="aimAddOrgForm" property="actionFlag"/>
    <html:hidden name="aimAddOrgForm" property="selectedStaffId"/>
    <html:hidden name="aimAddOrgForm" styleId="parentLocId" property="parentLocId" />
    <html:hidden name="aimAddOrgForm"  property="selContactId" />
    <html:hidden name="aimAddOrgForm"  property="type" />
    <html:hidden name="aimAddOrgForm"  property="ampOrgId" />
    <html:hidden name="aimAddOrgForm"  property="transIndexId" />
    <html:hidden name="aimAddOrgForm" property="selectedOrgInfoId"/>
    <html:hidden styleId="primaryOrgCont" value="${aimAddOrgForm.resetPrimaryOrgContIds}" name="aimAddOrgForm" property="resetPrimaryOrgContIds"/>

    <table bgColor=#ffffff cellPadding=5 cellSpacing=1 >
        <tr>
            <td class=r-dotted-lg width=14>&nbsp;</td>
            <td align=left class=r-dotted-lg vAlign=top width="800px">
                <table bgcolor="#ffffff" cellPadding=5 cellSpacing=0 width="100%">
                    <tr>
                        <!-- Start Navigation -->
                        <td height="33px">
                            <span class=crumb>
                                <digi:link href="/admin.do" styleClass="comment">
                                    <digi:trn key="aim:AmpAdminHome">
						Admin Home
                                    </digi:trn>
                                </digi:link>&nbsp;&gt;&nbsp; <digi:link
                                    href="/organisationManager.do?orgSelReset=true"
                                    styleClass="comment">
                                    <digi:trn key="aim:organizationManager">
                                        Organization Manager
                                    </digi:trn>
                                </digi:link>&nbsp;&gt;&nbsp;
                                <c:if test="${empty aimAddOrgForm.ampOrgId||aimAddOrgForm.ampOrgId==0}">
                                    <digi:trn key="aim:addOrganization">Add Organization</digi:trn>
                                </c:if>
                                <c:if test="${not empty aimAddOrgForm.ampOrgId&&aimAddOrgForm.ampOrgId!=0}">
                                    <digi:trn key="aim:editOrganization">Edit Organization</digi:trn>
                                </c:if>
                            </span></td>
                        <!-- End navigation -->
                    </tr>
                    <tr>
                        <td height="16px" vAlign=center width="700px">
                            <span class="subtitle-blue"> <digi:trn>Organization Manager </digi:trn> </span> <br/>
                        </td>
                    </tr>
                    <tr>
                        <td><digi:trn>All fields marked with <font size="2" color="#FF0000">*</font> are required.</digi:trn></td>
                    </tr>
                    <tr>
                        <td><digi:errors /></td>
                    </tr>
                    <tr>
                        <td>
                            <digi:link styleId="printWin" href="#" onclick="window.print(); return false;">

                                <digi:img width="17" height="20" hspace="2" vspace="2" src="/TEMPLATE/ampTemplate/imagesSource/common/printer.gif" border="0" alt="Print"/>
                            </digi:link>

                        </td>

                    </tr>
                    <tr>
                        <td>
                            <table border=0 bgColor=#f4f4f2>
                                 <tr>
                                    <td  align="right" colspan="2">
                                        &nbsp;
                                    </td>
                                </tr>
                                <tr>
                                    <td  align="right" colspan="2">
                                        <input  class="dr-menu" type="button" name="expandBtn" value="<digi:trn>Expand All</digi:trn>" onclick="expandAll()">
                                        <input  class="dr-menu" type="button" name="collapseBtn" value="<digi:trn>Collapse All</digi:trn>" onclick="collapseAll()">
                                    </td>

                                </tr>
                                 <tr>
                                    <td  align="right" colspan="2">
                                        &nbsp;
                                    </td>
                                </tr>
                                <tr>
                                    <td bgColor=#dddddb height="20" align="center"
                                        colspan="2" class="tdBoldClass" style="font-size:13px"> <c:if test="${empty aimAddOrgForm.ampOrgId||aimAddOrgForm.ampOrgId==0}">
                                            <digi:trn key="aim:addOrganization">Add Organization</digi:trn>
                                        </c:if> <c:if test="${not empty aimAddOrgForm.ampOrgId&&aimAddOrgForm.ampOrgId!=0}">
                                            <digi:trn key="aim:editOrganization">Edit Organization</digi:trn>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <table cellpadding="2" cellspacing="2" border="0" width="100%">
                                            <tr>
                                                <td style="text-align:left; "  class="tdBoldClass" nowrap>
                                                    <digi:trn>Organization Name</digi:trn>
                                                    <font size="2" color="#FF0000">*</font>
                                                </td>
                                                <td align="left">
                                                    <html:text name="aimAddOrgForm" property="name" size="54" styleId="orgName"/>
                                                </td>
                                                <td style="text-align:left; " class="tdBoldClass" nowrap>
                                                    <digi:trn>Organization Acronym</digi:trn>
                                                    <font size="2" color="#FF0000">*</font>
                                                </td>
                                                <td>    
                                                    <html:text name="aimAddOrgForm" property="acronym" size="20" styleId="acronym"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="text-align:left; "  class="tdBoldClass">
                                                    <digi:trn>Organization Type</digi:trn>
                                                    <font size="2" color="#FF0000">*</font>
                                                </td>
                                                <td>
                                                    <html:select property="ampOrgTypeId" onchange="return orgTypeChanged()" styleClass="selectStyle">
                                                        <c:set var="translation">
                                                            <digi:trn>Select Organization Type</digi:trn>
                                                        </c:set>
                                                        <html:option value="-1">-- ${translation} --</html:option>
                                                        <logic:notEmpty name="aimAddOrgForm"
                                                                        property="orgType">
                                                            <html:optionsCollection name="aimAddOrgForm" property="orgType" value="ampOrgTypeId" label="orgType" />
                                                        </logic:notEmpty>
                                                    </html:select>
                                                </td>
                                                <td style="text-align:left; "  class="tdBoldClass"><digi:trn>Organization Group</digi:trn>
                                                    <font size="2" color="#FF0000">*</font>
                                                </td>
                                                <td>
                                                    <html:select property="ampOrgGrpId" styleClass="selectStyle" styleId="orgGroup">
                                                        <c:set var="translation">
                                                            <digi:trn>Select Group</digi:trn>
                                                        </c:set>
                                                        <html:option value="-1">-- ${translation} --</html:option>
                                                        <logic:notEmpty name="aimAddOrgForm" property="orgGroup" >
                                                            <html:optionsCollection name="aimAddOrgForm" property="orgGroup" value="ampOrgGrpId" label="orgGrpName" />
                                                        </logic:notEmpty>
                                                    </html:select></td>
                                            </tr>
                                            <tr>
                                                <td align="right" height="2" colspan="2">&nbsp;</td>
                                                <td height="1" align="center" colspan="2"><digi:img
                                                        src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-014E86.gif" width="15"
                                                        height="10" /> <a href="javascript:addGroup()"> <digi:trn
                                                            key="aim:addOrganizationGroup">Add a Group</digi:trn>
                                                    </a></td>
                                            </tr>
                                        </table>
                                    </td>

                                </tr>

                                <c:if test="${aimAddOrgForm.type=='REGIONAL'}">
                                    <tr>
                                        <td style=" text-align:right"><digi:trn>Region</digi:trn></td>
                                        <td  height="30px" >
                                            <html:select property="regionId" >
                                                <c:set var="translation">
                                                    <digi:trn
                                                        key="aim:editOrganisationSelectSpecifyRegion">Specify Region</digi:trn>
                                                </c:set>
                                                <html:option value="-1">-- ${translation} --</html:option>
                                                <logic:notEmpty name="aimAddOrgForm"
                                                                property="region">
                                                    <html:optionsCollection name="aimAddOrgForm" property="region" value="id" label="name" />
                                                </logic:notEmpty>
                                            </html:select>
                                        </td>
                                    </tr>
                                </c:if>
                                <c:choose>
                                    <c:when test="${aimAddOrgForm.type=='NGO'}">
                                        <tr>
                                            <td  class="tdBoldClass" style="font-size:13px;" nowrap>
                                                <digi:trn>Organization Primary Purpose</digi:trn>
                                                <font size="2" color="#FF0000">*</font>
                                            </td>
                                            <td>
                                                <html:textarea name="aimAddOrgForm" property="orgPrimaryPurpose" cols="160" rows="4" styleId="orgPrimaryPurpose"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="100%" colspan="2">
                                                <div style="float:right">
                                                    <a href="javascript:exportGeneralInfo();" >
                                                        <digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/xls_icon.jpg" border="0"/>
                                                    </a>
                                                </div>
                                                        <fieldset><legend class="legendClass"><digi:trn>General Information</digi:trn></legend>
                                                      <img id="img_general" alt="" src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow_right.gif"  style="display : none;" onclick="expand('general')"/>
                                <img id="imgh_general" alt="" src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow_down.gif"  onclick="collapse('general')"/>
                                <div id="div_container_general">
                                    <table width="100%" cellpadding="5" cellspacing="5">
                                                    <tr>
                                                        <td valign="top" width="50%">
                                                            <table cellpadding="5" cellspacing="5">
                                                                <tr>
                                                                    <td nowrap style=" text-align:right" class="tdBoldClass"><digi:trn>Registration Number in MinPlan</digi:trn><font color="red">*</font></td>
                                                                    <td><html:text property="regNumbMinPlan" styleId="regNumbMinPlan" /></td>
                                                                </tr>
                                                                <tr>
                                                                    <td style=" text-align:right" class="tdBoldClass"><digi:trn>Registration Date in MinPlan</digi:trn><font color="red">*</font></td>
                                                                    <td>
                                                                        <html:text property="minPlanRegDate" size="10" styleId="minPlanRegDate" styleClass="inp-text" readonly="true" />
                                                                        <a id="clear1" href='javascript:clearDate(document.getElementById("minPlanRegDate"), "clear1")'>
                                                                            <digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0" alt="Delete this "/>
                                                                        </a>
                                                                        <a id="date1" href='javascript:pickDateWithClear("date1",document.getElementById("minPlanRegDate"),"clear1")'>
                                                                            <img src="/TEMPLATE/ampTemplate/imagesSource/calendar/show-calendar.gif" alt="Click to View Calendar" border=0>
                                                                        </a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td  style=" text-align:right" class="tdBoldClass"><digi:trn>Fiscal Calendar</digi:trn><font color="red">*</font></td>
                                                                    <td>
                                                                        <c:set var="translation">
                                                                            <digi:trn>Select the Fiscal Calendar</digi:trn>
                                                                        </c:set>

                                                                            <html:select property="fiscalCalId" styleClass="selectStyle" styleId="fiscalCalId">
                                                                            <html:option value="-1">-- ${translation} --</html:option>
                                                                            <html:optionsCollection property="fiscalCal" label="name" value="ampFiscalCalId"/>
                                                                        </html:select>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td  style=" text-align:right" class="tdBoldClass"><digi:trn>Sectors Scheme</digi:trn><font color="red">*</font></td>
                                                                    <td>
                                                                        <html:select property="ampSecSchemeId" styleClass="selectStyle">
                                                                            <c:set var="translation">
                                                                                <digi:trn>Sectors Scheme</digi:trn>
                                                                            </c:set>
                                                                            <html:option value="-1">-- ${translation} --</html:option>
                                                                            <logic:notEmpty name="aimAddOrgForm" property="sectorScheme">
                                                                                <html:optionsCollection name="aimAddOrgForm" property="sectorScheme" value="ampSecSchemeId" label="secSchemeName" />
                                                                            </logic:notEmpty>
                                                                        </html:select>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td style=" text-align:right" class="tdBoldClass"><digi:trn>Sector Preferences</digi:trn><font color="red">*</font></td>
                                                                    <td>
                                                                        <table cellSpacing="1" cellPadding="5" class="box-border-nopadding">
                                                                            <c:if test="${aimAddOrgForm.sectors != null}">
                                                                                <c:forEach var="sector" items="${aimAddOrgForm.sectors}">
                                                                                    <tr>
                                                                                        <td width="5px" align="right">
                                                                                            <html:multibox property="selSectors" >
                                                                                                <c:if test="${sector.subsectorLevel1Id == -1}">
                                                                                                    ${sector.sectorId}
                                                                                                </c:if>

                                                                                                <c:if test="${sector.subsectorLevel1Id != -1 && sector.subsectorLevel2Id == -1}">
                                                                                                    ${sector.subsectorLevel1Id}
                                                                                                </c:if>
                                                                                                <c:if test="${sector.subsectorLevel1Id != -1 && sector.subsectorLevel2Id != -1}">
                                                                                                    ${sector.subsectorLevel2Id}
                                                                                                </c:if>
                                                                                            </html:multibox>
                                                                                        </td>
                                                                                        <td>
                                                                                            [${sector.sectorScheme}]
                                                                                            <c:if test="${!empty sector.sectorName}">
                                                                                                [${sector.sectorName}]
                                                                                            </c:if>

                                                                                            <c:if test="${!empty sector.subsectorLevel1Name}">
	                                                                            [${sector.subsectorLevel1Name}]
                                                                                            </c:if>

                                                                                            <c:if test="${!empty sector.subsectorLevel2Name}">
	                                                                            [${sector.subsectorLevel2Name}]
                                                                                            </c:if>

                                                                                        </td>
                                                                                    </tr>
                                                                                </c:forEach>
                                                                            </c:if>

                                                                            <tr>
                                                                                <td colspan="2">
                                                                                    <input type="button" class="dr-menu" onclick="javascript:addSectors();" value='<digi:trn key="btn:addSectors">Add Sectors</digi:trn>' />
                                                                                    <c:if test="${not empty aimAddOrgForm.sectors}">
                                                                                        <input type="button" class="dr-menu" onclick="return removeSectors()" value='<digi:trn key="btn:removeSector">Remove Sector</digi:trn>' />
                                                                                    </c:if>
                                                                                </td>
                                                                            </tr>

                                                                        </table>
                                                                    </td>
                                                                </tr>

                                                                <tr>
                                                                    <td  style=" text-align:right" class="tdBoldClass"><digi:trn>Organization website</digi:trn><font color="red">*</font></td>
                                                                    <td>
                                                                        <html:text property="orgUrl" styleId="orgUrl"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td  style=" text-align:right" class="tdBoldClass"><digi:trn>Organization Headquarters Address</digi:trn><font color="red">*</font></td>
                                                                    <td>
                                                                        <html:textarea property="address"  cols="40" styleId="address"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td  style=" text-align:right" class="tdBoldClass"><digi:trn>Organization Address Abroad(Internation NGO)</digi:trn></td>
                                                                    <td>
                                                                        <html:textarea property="addressAbroad" cols="40"  styleId="addressAbroad"/>
                                                                    </td>
                                                                </tr>
                                                            </table>


                                                        </td>
                                                        <td valign="top">
                                                            <table cellpadding="5" cellspacing="5">
                                                                <tr>
                                                                    <td nowrap style=" text-align:right" class="tdBoldClass"><digi:trn>Legal Personality Number</digi:trn></td>
                                                                    <td>
                                                                        <html:text property="legalPersonNum" styleId="legalPersonNum"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap style=" text-align:right" class="tdBoldClass"><digi:trn>Legal Personality Registration Date</digi:trn></td>
                                                                    <td>
                                                                        <html:text property="legalPersonRegDate" size="10" styleId="legalPersonRegDate" styleClass="inp-text" readonly="true" />
                                                                        <a id="clear2" href='javascript:clearDate(document.getElementById("legalPersonRegDate"), "clear2")'>
                                                                            <digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0" alt="Delete this transaction"/>
                                                                        </a>
                                                                        <a id="date2" href='javascript:pickDateWithClear("date2",document.getElementById("legalPersonRegDate"),"clear2")'>
                                                                            <img src="/TEMPLATE/ampTemplate/imagesSource/calendar/show-calendar.gif" alt="Click to View Calendar" border=0>
                                                                        </a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td style=" text-align:right" class="tdBoldClass"><digi:trn>Recipients</digi:trn><font color="red">*</font></td>
                                                                    <td>
                                                                        <c:if test="${empty aimAddOrgForm.recipients}">
                                                                    <aim:addOrganizationButton refreshParentDocument="true" collection="recipients"  form="${aimAddOrgForm}" styleClass="dr-menu"><digi:trn>Add Organizations</digi:trn></aim:addOrganizationButton>
                                                                </c:if>
                                                                <c:if test="${not empty aimAddOrgForm.recipients}">
                                                                    <table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
                                                                        <c:forEach var="organization" items="${aimAddOrgForm.recipients}">
                                                                            <tr>

                                                                                <td width="3">
                                                                                    <html:multibox property="selRecipients">
                                                                                        <bean:write name="organization" property="ampOrgId" />
                                                                                    </html:multibox>
                                                                                </td>
                                                                                <td align="left">
                                                                                    <bean:write name="organization" property="name" />
                                                                                </td>

                                                                            </tr>
                                                                        </c:forEach>
                                                                        <tr>
                                                                            <td colspan="2">
                                                                        <aim:addOrganizationButton refreshParentDocument="true" collection="recipients"  form="${aimAddOrgForm}" styleClass="dr-menu"><digi:trn>Add Organizations</digi:trn></aim:addOrganizationButton>
                                                                        <input type="button" class="dr-menu" onclick="javascript:removeOrgs();" value='<digi:trn>Remove Organization(s)</digi:trn>' />
                                                                        </td>
                                                                        </tr>

                                                                    </table>
                                                                </c:if>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td style=" text-align:right" class="tdBoldClass"><digi:trn>Country Of Origin</digi:trn><font color="red">*</font></td>
                                                        <td>
                                                            <c:set var="translation">
                                                                <digi:trn>Select Country</digi:trn>
                                                            </c:set>
                                                            <html:select property="countryId" styleClass="selectStyle" styleId="countryId">
                                                                <html:option value="-1">-- ${translation} --</html:option>
                                                                <html:optionsCollection property="countries" label="name" value="id"/>
                                                            </html:select>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td style=" text-align:right" class="tdBoldClass"><digi:trn>Tax Number</digi:trn></td>
                                                        <td>
                                                            <html:text property="taxNumber" styleId="taxNumber"/>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td style=" text-align:right" class="tdBoldClass"><digi:trn>Organization Intervention Level</digi:trn><font color="red">*</font></td>
                                                        <td>
                                                            <c:set var="translation">
                                                                <digi:trn>Please select from below</digi:trn>
                                                            </c:set>
                                                    <category:showoptions multiselect="false" firstLine="${translation}" name="aimAddOrgForm" property="implemLocationLevel"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.IMPLEMENTATION_LOCATION_KEY %>" styleClass="selectStyle" />
                                                    <script language="Javascript">
                                                        var implemLocationLevelSelect = document.getElementsByName("implemLocationLevel")[0];
                                                        if(implemLocationLevelSelect!=null){
                                                            implemLocationLevelSelect.onchange=function() {
                                                                removeAllLocations();
                                                            }
                                                        }
                                                    </script>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style=" text-align:right" class="tdBoldClass"><digi:trn>Organization Intervention Location</digi:trn><font color="red">*</font></td>
                                            <td>

                                                <c:if test="${empty aimAddOrgForm.selectedLocs}">
                                                    <input type="button" class="dr-menu" onclick="javascript:selectLocation();" value='<digi:trn>Add Location</digi:trn>' />
                                                </c:if>

                                                <c:if test="${not empty aimAddOrgForm.selectedLocs}">
                                                    <table cellSpacing="1" cellPadding="5" class="box-border-nopadding">

                                                        <c:forEach var="selectedLocs" items="${aimAddOrgForm.selectedLocs}">

                                                            <tr>
                                                                <td width="5px" vAlign="center">
                                                                    <html:multibox property="selLocs" styleId="selLocs">
                                                                        <bean:write name="selectedLocs" property="locId" />
                                                                    </html:multibox>
                                                                </td>
                                                                <td>
                                                                    <c:forEach var="ancestorLoc" items="${selectedLocs.ancestorLocationNames}">
                                                                    	[${ancestorLoc}]
                                                                    </c:forEach>
                                                                </td>

                                                                <td align="right" nowrap="nowrap" class="tdBoldClass">
                                                                    <digi:trn>Percentage</digi:trn>:&nbsp;
                                                                    <html:text name="selectedLocs" indexed="true" property="percent" size="2"  maxlength="5" onkeyup="fnChk(this,false)"/>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                        <tr>
                                                            <td colspan="3">
                                                                <input type="button" class="dr-menu" onclick="javascript:selectLocation();" value='<digi:trn>Add Location</digi:trn>' />

                                                                <input type="button" class="dr-menu" onclick="javascript:removeSelLocations();" value='<digi:trn>Remove Location</digi:trn>' />

                                                            </td>
                                                        </tr>

                                                    </table>
                                                </c:if>
                                                &nbsp;

                                            </td>
                                        </tr>
                                    </table>

                                </td>
                            </tr>

                        </table>
                         </div>
                      </fieldset>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div style="float:right">
                        <a href="javascript:exportInfo('exportStaffInfo')" >
				<digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/xls_icon.jpg" border="0"/>
                        </a>
                         </div>
                        <fieldset>
                            <legend align="left" class="tdBoldClass" style="font-size:13px;color:#0000FF; "><digi:trn>Staff Information</digi:trn></legend>
                            <img id="img_staff" alt="" src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow_right.gif"  style="display : none;"  onclick="expand('staff')"/>
                            <img id="imgh_staff" alt="" src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow_down.gif"   onclick="collapse('staff')"/>
                            <div id="div_container_staff">
                            <table cellpadding="2" cellspacing="0" border="0">
                                <c:if test="${not empty aimAddOrgForm.staff}">
                                    <tr>
                                        <td colspan="5">
                                            <c:if test="${fn:length(aimAddOrgForm.staff)>1}">
                                                <div style="overflow-y: scroll; overflow-x: hidden; width: 100%; height: 100px;">
                                                </c:if>
                                                <table cellspacing="0" cellpadding="0" id="staffTable">
                                                    <c:forEach var="info" items="${aimAddOrgForm.staff}" >
                                                        <tr>
                                                            <td  style="width:40px;text-align:left;">
                                                                <html:multibox property="selectedStaff" styleClass="staffInfo">
                                                                    ${info.id}
                                                                </html:multibox>
                                                            </td>
                                                            <td class="tdClass" style="width:125px;text-align:center;">${info.year}</td>
                                                            <td class="tdClass" style="width:205px;text-align:center;" ><digi:trn>${info.type.value}</digi:trn></td>
                                                            <td class="tdClass" style="width:200px;text-align:center;">${info.staffNumber}</td>
                                                            <td class="tdClass" style="width:70px;text-align:center;"><a href="javascript:deleteStaff('${info.id}')"> <img alt="delete" src= "/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0"></a></td>
                                                        </tr>
                                                    </c:forEach>
                                                </table>
                                                <c:if test="${fn:length(aimAddOrgForm.staff)>1}">
                                                </div>
                                            </c:if>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="5" style="text-align:left;" class="tdBoldClass"><input type="checkbox"  onclick="selectAll('staffInfo')"><digi:trn>Select All</digi:trn>&nbsp;&nbsp;<input type="button" onclick="deleteStaff()" value="<digi:trn>Delete</digi:trn>"></td>
                                    </tr>
                                    </c:if>
                                     <tr>
                                    <td style="width:40px;text-align:center;font-weight:bold">
                                        &nbsp;
                                    </td>
                                    <td style="width:130px;text-align:center; "  class="tdBoldClass">
                                            <digi:trn>Year</digi:trn>
                                    </td>
                                    <td style="width:210px;text-align:center; "  class="tdBoldClass">
                                        <digi:trn>Type of staff</digi:trn>
                                    </td>
                                    <td style="width:150px;text-align:center; "  class="tdBoldClass">
                                        <digi:trn>Number of Staff</digi:trn>
                                    </td>
                                    <td style="width:90px;text-align:center;font-weight:bold">
                                        &nbsp;
                                    </td>
                                </tr>
                                <tr>
                                    <td> &nbsp;</td>
                                    <td align="center">
                                        <c:set var="translation">
                                            <digi:trn> Select Year</digi:trn>
                                        </c:set>

                                        <html:select name="aimAddOrgForm" property="selectedYear" styleClass="selectStyle" style="width:120px;">
                                            <html:option value="-1">-- ${translation} --</html:option>
                                            <html:optionsCollection name="aimAddOrgForm"  property="years" label="label" value="value"/>
                                        </html:select>
                                    </td>
                                    <td style="text-align:center">
                                        <c:set var="translation">
                                            <digi:trn>Please select a status from below</digi:trn>
                                        </c:set>
                                <category:showoptions firstLine="${translation}" name="aimAddOrgForm" property="typeOfStaff"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.ORGANIZATION_STAFF_INFO_KEY%>" styleClass="selectStyle" />
                                </td>
                                <td style="text-align:center"><html:text name="aimAddOrgForm" property="numberOfStaff"  onkeyup="fnChk(this,true)" styleClass="inp-text"/></td>

                                <td style="text-align:center"><input type="button" style="width:80px" onclick="addStaff()" value="<digi:trn>Add</digi:trn>" /></td>
                                </tr>
                            </table>
                            </div>
                        </fieldset>
                    </td>
                </tr>

                <tr>
                    <td colspan="2">
                        <div style="float:right">
                        <a href="javascript:exportInfo('exportBudgetInfo')" >
				<digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/xls_icon.jpg" border="0"/>
                        </a>
                         </div>
                        <fieldset>
                            <legend align="left" class="legendClass"><digi:trn>Budget Information</digi:trn></legend>
                                <img id="img_budget" alt="" src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow_right.gif"  style="display : none;" onclick="expand('budget')"/>
                                <img id="imgh_budget" alt="" src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow_down.gif"  onclick="collapse('budget')"/>
                                <div id="div_container_budget">
                                <table cellpadding="2" cellspacing="0" border="0">
                               
                                <c:if test="${not empty aimAddOrgForm.orgInfos}">

                                    <tr>
                                        <td colspan="7">
                                            <c:if test="${fn:length(aimAddOrgForm.orgInfos)>1}">
                                                <div style="overflow-y: scroll; overflow-x: hidden; width: 100%; height: 100px; min-height:100px">
                                                </c:if>
                                                <table width="100%" cellspacing="0" cellpadding="0" id="orgInfosTable">
                                                    <c:forEach var="orgInfo" items="${aimAddOrgForm.orgInfos}" >
                                                        <tr>
                                                            <td  style="width:40px;text-align:left;">
                                                                <html:multibox property="selectedOrgInfoIds" styleClass="selectedOrgInfoIds">
                                                                    ${orgInfo.id}
                                                                </html:multibox>
                                                            </td>
                                                            <td class="tdClass" style="width:100px;text-align:center;">${orgInfo.year}</td>
                                                            <td class="tdClass" style="width:205px;text-align:center;" ><digi:trn>${orgInfo.name}</digi:trn></td>
                                                            <td class="tdClass" style="width:150px;text-align:center;">${orgInfo.percent}<c:if test="${not empty orgInfo.percent}">%</c:if></td>
                                                            <td class="tdClass" style="width:150px;text-align:center;">${orgInfo.amount}</td>
                                                            <td class="tdClass" style="width:205px;text-align:center;">${orgInfo.currency.currencyCode}</td>
                                                            <td class="tdClass" style="width:70px;text-align:center;"><a href="javascript:deleteOrgInfo('${orgInfo.id}')"> <img alt="delete" src= "/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0"></a></td>
                                                        </tr>
                                                    </c:forEach>
                                                </table>
                                                <c:if test="${fn:length(aimAddOrgForm.orgInfos)>1}">
                                                </div>
                                            </c:if>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="7" class="tdBoldClass" style="text-align:left;"><input type="checkbox"  onclick="selectAll('selectedOrgInfoIds')"><digi:trn>Select All</digi:trn>&nbsp;&nbsp;<input type="button" onclick="deleteOrgInfo()" value="<digi:trn>Delete</digi:trn>"></td>
                                    </tr>
                                </c:if>
                                     <tr>
                                    <td style="width:40px;text-align:center; "  class="tdBoldClass">
                                        &nbsp;
                                    </td>
                                    <td style="width:130px;text-align:center; "  class="tdBoldClass">
                                        <digi:trn>Year</digi:trn>
                                    </td>
                                    <td style="width:210px;text-align:center; "  class="tdBoldClass">
                                        <digi:trn>Type of Organization</digi:trn>
                                    </td>
                                    <td style="width:150px;text-align:center; "  class="tdBoldClass">
                                        <digi:trn>Percent</digi:trn>
                                    </td>
                                    <td style="width:150px;text-align:center;  "  class="tdBoldClass">
                                        <digi:trn>Amount</digi:trn>
                                    </td>
                                    <td style="text-align:center;"  class="tdBoldClass">
                                        <digi:trn>Currency</digi:trn>
                                    </td>
                                     <td> &nbsp;</td>
                                </tr>
                                <tr>
                                    <td> &nbsp;</td>
                                    <td>
                                        <c:set var="translation">
                                            <digi:trn> Select Year</digi:trn>
                                        </c:set>

                                        <html:select name="aimAddOrgForm" property="orgInfoSelectedYear" styleClass="selectStyle" style="width:120px;">
                                            <html:option value="-1">-- ${translation} --</html:option>
                                            <html:optionsCollection name="aimAddOrgForm"  property="years" label="label" value="value"/>
                                        </html:select>
                                    </td>
                                    <td style="text-align:center">

                                        <html:select property="orgInfoType" name="aimAddOrgForm" styleClass="selectStyle">
                                            <html:option value="-1">-<digi:trn>Select Type</digi:trn>-</html:option>
                                            <html:option value="1"><digi:trn>Annual Budget of internal/administrative functioning</digi:trn></html:option>
                                            <html:option value="2"><digi:trn>Program Annual Budget</digi:trn></html:option>
                                        </html:select>
                                    </td>
                                    <td style="text-align:center"><html:text name="aimAddOrgForm" property="orgInfoPercent"  styleClass="inp-text"/></td>
                                    <td style="text-align:center"><html:text name="aimAddOrgForm" property="orgInfoAmount"  styleClass="inp-text"/></td>
                                    <td style="text-align:center">
                                        <c:set var="translation">
                                            <digi:trn>Select Currency</digi:trn>
                                        </c:set>
                                        <html:select property="orgInfoCurrId" styleClass="selectStyle"  style="width:150px">
                                            <html:option value="-1">-- ${translation} --</html:option>
                                            <html:optionsCollection property="currencies" label="currencyName" value="ampCurrencyId"/>
                                        </html:select>

                                    </td>

                                    <td style="text-align:center"><input type="button" style="width:80px" onclick="addOrgInfo()" value="<digi:trn>Add</digi:trn>" /></td>
                                </tr>
                            </table>
                            </div>
                        </fieldset>
                    </td>
                </tr>
            </c:when>
            <c:otherwise>
                <tr>
                    <td style=" text-align:right;" class="tdBoldClass"><digi:trn
                            key="aim:organizationDac">DAC Code</digi:trn></td>
                    <td width="500px" height="30px">
                    	<html:text property="dacOrgCode" size="15" styleId="dacOrgCode"/></td>
                </tr>
                <tr>
                    <td style=" text-align:right;" class="tdBoldClass"><digi:trn
                            key="aim:organizationIsoCode">ISO Code</digi:trn></td>
                    <td width="500px" height="30px" ><html:text
                            name="aimAddOrgForm" property="orgIsoCode" size="15" styleId="orgIsoCode"/>
                    </td>
                </tr>
                <tr>
                    <td style=" text-align:right;" class="tdBoldClass"><digi:trn
                            key="aim:organizationCode">Organization Code</digi:trn><font
                            size="2" color="#FF0000">*</font></td>
                    <td width="500px" height="30px"><html:text
                        property="orgCode" size="15" styleId="orgCode"/></td>
                </tr>

                <tr>
                    <td style=" text-align:right;" class="tdBoldClass"><digi:trn
                            key="aim:budgetOrganizationCode">Budget Organization Code</digi:trn><font
                            size="2" color="#FF0000">*</font></td>
                    <td width="500px" height="30px"><html:text
                        property="budgetOrgCode" size="15" styleId="budgetOrgCode"/></td>
                </tr>
                <tr>
                    <td style=" text-align:right;" class="tdBoldClass"><digi:trn
                            key="aim:fiscalCalendar">Fiscal Calendar</digi:trn></td>
                    <td width="500px" height="30px"><html:select
                            property="fiscalCalId" styleClass="selectStyle" styleId="fiscalCalId">
                            <c:set var="translation">
                                <digi:trn
                                    key="aim:editOrganisationSelectFiscalCalendar">Fiscal Calendar</digi:trn>
                            </c:set>
                            <html:option value="-1">-- ${translation} --</html:option>
                            <logic:notEmpty name="aimAddOrgForm"
                                            property="fiscalCal">
                                <html:optionsCollection name="aimAddOrgForm"
                                                        property="fiscalCal" value="ampFiscalCalId"
                                                        label="name" />
                            </logic:notEmpty>
                        </html:select>
                </tr>
                <tr>
                    <td style=" text-align:right;" class="tdBoldClass"><digi:trn>Sectors Scheme</digi:trn></td>
                    <td>
                        <html:select property="ampSecSchemeId" styleClass="selectStyle">
                            <c:set var="translation">
                                <digi:trn>Sectors Scheme</digi:trn>
                            </c:set>
                            <html:option value="-1">-- ${translation} --</html:option>
                            <logic:notEmpty name="aimAddOrgForm" property="sectorScheme">
                                <html:optionsCollection name="aimAddOrgForm" property="sectorScheme" value="ampSecSchemeId" label="secSchemeName" />
                            </logic:notEmpty>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td style=" text-align:right" class="tdBoldClass"><digi:trn>Sector Preferences</digi:trn></td>
                    <td>
                        <table cellSpacing="1" cellPadding="5" class="box-border-nopadding">
                            <c:forEach var="sector" items="${aimAddOrgForm.sectors}">
                                <tr>
                                    <td width="5px" align="right">
                                        <html:multibox property="selSectors" >
                                            <c:if test="${sector.subsectorLevel1Id == -1}">
                                                ${sector.sectorId}
                                            </c:if>

                                            <c:if test="${sector.subsectorLevel1Id != -1 && sector.subsectorLevel2Id == -1}">
                                                ${sector.subsectorLevel1Id}
                                            </c:if>
                                            <c:if test="${sector.subsectorLevel1Id != -1 && sector.subsectorLevel2Id != -1}">
                                                ${sector.subsectorLevel2Id}
                                            </c:if>
                                        </html:multibox>
                                    </td>
                                    <td>
                                        [${sector.sectorScheme}]
                                        <c:if test="${!empty sector.sectorName}">
                                            [${sector.sectorName}]
                                        </c:if>

                                        <c:if test="${!empty sector.subsectorLevel1Name}">
	                                                                            [${sector.subsectorLevel1Name}]
                                        </c:if>

                                        <c:if test="${!empty sector.subsectorLevel2Name}">
	                                                                            [${sector.subsectorLevel2Name}]
                                        </c:if>

                                    </td>
                                </tr>
                            </c:forEach>


                            <tr>
                                <td>
                                    <input type="button" class="dr-menu" onclick="javascript:addSectors();" value='<digi:trn key="btn:addSectors">Add Sectors</digi:trn>' />
                                </td>
                                <td>
                                    &nbsp;
                                    <c:if test="${not empty aimAddOrgForm.sectors}">
                                        <input type="button" class="dr-menu" onclick="return removeSectors()" value='<digi:trn key="btn:removeSector">Remove Sector</digi:trn>' />
                                    </c:if>
                                </td>

                            </tr>
                        </table>
                        &nbsp;
                    </td>
                </tr>
                <tr>
                    <td style=" text-align:right;" class="tdBoldClass">
                        <digi:trn>
                            Pledges
                        </digi:trn>
                    </td>
                    <td>
                        <%
       String tempIndexStr = "";
                        %> <%
                                                                                                                                               int tempIndex = 0;
                        %> <!-- ############################## -->
                        <table width="100%" border="0" bgcolor="#f4f4f2"
                               cellspacing="1" cellpadding="0"
                               class=box-border-nopadding>
                            <c:if test="${ aimAddOrgForm.fundingDetails != null}">
                                <c:set var="index" value="-1" />
                                <tr>
                                    <td align="center" valign="bottom" class="tdBoldClass"><digi:trn
                                            key="aim:org:program">Program</digi:trn></td>
                                    <td align="center" valign="bottom" class="tdBoldClass"><digi:trn
                                            key="aim:org:planned">Planned</digi:trn></td>
                                    <td align="center" valign="bottom" class="tdBoldClass"><digi:trn
                                            key="aim:org:amount">Amount</digi:trn></td>
                                    <td align="center" valign="bottom" class="tdBoldClass"><digi:trn
                                            key="aim:org:currency">Currency</digi:trn></td>
                                    <td align="center" valign="bottom" class="tdBoldClass"><digi:trn
                                            key="aim:org:date">Date</digi:trn></td>
                                </tr>
                                <c:forEach var="fundingDetail"
                                           items="${aimAddOrgForm.fundingDetails}">
                                    <tr>
                                        <td valign="bottom" class="tdClass"><html:text
                                                name="fundingDetail" indexed="true"
                                                property="program" styleClass="inp-text" size="10" />
                                        </td>
                                        <td valign="bottom" class="tdClass"><c:set var="index"
                                               value="${index+1}" /> <html:select
                                               name="fundingDetail" indexed="true"
                                               property="adjustmentType" styleClass="inp-text">
                                                <html:option value="0">
                                                    <digi:trn key="aim:Planned">Planned</digi:trn>
                                                </html:option>
                                            </html:select></td>
                                        <td valign="bottom" class="tdClass"><html:text
                                                name="fundingDetail" indexed="true"
                                                property="amount" size="17" styleClass="amt" /></td>
                                        <td valign="bottom" class="tdClass"><html:select
                                                name="fundingDetail" indexed="true"
                                                property="currencyCode" styleClass="inp-text">
                                                <html:optionsCollection name="aimAddOrgForm"
                                                                        property="currencies" value="currencyCode"
                                                                        label="currencyName" />
                                            </html:select></td>
                                        <td vAlign="bottom" class="tdClass">
                                            <table cellPadding=0 cellSpacing=0>
                                                <tr>
                                                    <td valign="bottom">
                                                        <%
                                                                tempIndexStr = "" + tempIndex;
                                                                                        tempIndex++;
                                                        %> <html:text name="fundingDetail" indexed="true"
                                                                      property="date" styleId="<%=tempIndexStr%>"
                                                                      styleClass="inp-text" readonly="true" size="10" />
                                                    </td>
                                                    <td align="left" vAlign="center">&nbsp; <a
                                                            id="transDate<%=tempIndexStr%>"
                                                            href='javascript:pickDateById("transDate<%=tempIndexStr%>",<%=tempIndexStr%>)'>
                                                            <img
                                                                src="/TEMPLATE/ampTemplate/imagesSource/calendar/show-calendar.gif"
                                                                alt="Click to View Calendar" border=0> </a></td>
                                                </tr>
                                            </table>
                                        </td>
                                        <td valign="bottom"><a
                                                href="javascript:removeFundingDetail(<bean:write name="fundingDetail" property="indexId"/>)">
                                                <digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif"
                                                          border="0" alt="Delete this transaction" /> </a></td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            <tr>
                                <td>&nbsp;</td>
                            </tr>
                            <tr valign="baseline">
                                <td colspan="1" width="10px"><input
                                        type="button" class="dr-menu" onclick="addPledge();"
                                        value='<digi:trn key="btn:addPledge">Add Pledge</digi:trn>' />
                                </td>
                                <td colspan="5" align="right">
                            <gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
                                <FONT color=blue>*
                                    <digi:trn key="aim:allTheAmountsInThousands">
																							All the amounts are in thousands (000)
                                </digi:trn> </FONT>
                            </gs:test>
                    </td>
                </tr>
            </table>

        </td>
    </tr>
</c:otherwise>
</c:choose>
<tr>
    <td colspan="2">
        <div style="float:right">
            <a href="javascript:exportInfo('exportContactInfo')" >
                <digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/xls_icon.jpg" border="0"/>
            </a>
        </div>
        <fieldset>
            <legend align="left" class="legendClass"><digi:trn>Contact Information</digi:trn></legend>
                <img id="img_contact" alt="" src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow_right.gif"  style="display : none;" onclick="expand('contact')"/>
                <img id="imgh_contact" alt="" src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow_down.gif"   onclick="collapse('contact')"/>
                <div id="div_container_contact">
                <table cellpadding="2" cellspacing="0" border="0" width="100%">
                <c:if test="${not empty aimAddOrgForm.orgContacts}">
                    <tr>
                        <td colspan="2">
                            <c:if test="${fn:length(aimAddOrgForm.orgContacts)>1}">
                                <div style="overflow-y: scroll; overflow-x: hidden;width: 95%; height: 100px;">
                                </c:if>
                                    <table width="100%" cellSpacing="1" cellPadding="1" align="left" id="table_contact_content">
                                    <tr>
                                        <td>
                                            &nbsp;
                                        </td>
                                        <td class="tdBoldClass" style="color:#FFFFFF">
                                            <digi:trn>LAST NAME</digi:trn>
                                        </td>
                                        <td class="tdBoldClass" style="color:#FFFFFF">
                                            <digi:trn>FIRST NAME</digi:trn>
                                        </td>
                                        <td class="tdBoldClass" style="color:#FFFFFF">
                                            <digi:trn>EMAIL </digi:trn>
                                        </td>
                                        <td class="tdBoldClass" style="color:#FFFFFF">
                                            <digi:trn> TELEPHONE </digi:trn>
                                        </td>
                                        <td class="tdBoldClass" style="color:#FFFFFF">
                                            <digi:trn> FAX </digi:trn>
                                        </td>
                                        <td class="tdBoldClass" style="color:#FFFFFF">
                                            <digi:trn>TITLE </digi:trn>
                                        </td>
                                        <td>
                                        	<digi:trn>PRIMARY </digi:trn>
                                        </td>
                                        <td colspan="2">
                                            &nbsp;
                                        </td>
                                    </tr>
                                    <c:forEach var="orgCont" items="${aimAddOrgForm.orgContacts}" varStatus="stat">
                                        <c:set var="ampContactId">
                                            <c:choose>
                                                <c:when test="${empty orgCont.contact.id||orgCont.contact.id==0}">
                                                    ${orgCont.contact.temporaryId}
                                                </c:when>
                                                <c:otherwise>
                                                    ${orgCont.contact.id}
                                                </c:otherwise>
                                            </c:choose>
                                        </c:set>
                                        <tr>
                                            <td style="width:40px">
                                                <html:multibox property="selectedContactInfoIds" styleClass="selectedContactInfoIds">
                                                    ${ampContactId}
                                                </html:multibox>
                                            </td>
                                            <td class="tdClass" nowrap>
                                                ${orgCont.contact.lastname}
                                            </td>
                                            <td class="tdClass" nowrap>
                                                ${orgCont.contact.name}
                                            </td>
                                            <td class="tdClass" nowrap>
                                            	<c:forEach var="email" items="${orgCont.contact.properties}">
													<c:if test="${email.name=='contact email'}">
														<div>${email.value}</div>
													</c:if>
												</c:forEach>
                                            </td>
                                            <td class="tdClass">
                                            	<c:forEach var="phone" items="${orgCont.contact.properties}">
													<c:if test="${phone.name=='contact phone'}">
														<div>${phone.value}</div>
													</c:if>
												</c:forEach>
                                            </td>
                                            <td class="tdClass">
                                                <c:forEach var="phone" items="${orgCont.contact.properties}">
													<c:if test="${phone.name=='contact fax'}">
														<div>${phone.value}</div>
													</c:if>
												</c:forEach>
                                            </td>
                                            <td class="tdClass">
                                                <c:if test="${not empty orgCont.contact.title}">
                                                   <digi:trn> ${orgCont.contact.title.value}</digi:trn>
                                                </c:if>
                                            </td>
                                            <td>
	                                        	<html:multibox name="aimAddOrgForm" property="primaryOrgContIds" styleId="primary_${stat.index}" value="${ampContactId}" onchange="changePrimaryState()"/>
	                                        </td>
                                            <td>
		                                        <aim:editContactLink collection="orgContacts" form="${aimAddOrgForm}" contactId="${ampContactId}" addOrgBtn="hidden">
		                                            <img alt="edit" src= "/TEMPLATE/ampTemplate/imagesSource/common/application_edit.png" border="0"/>
		                                        </aim:editContactLink>
		                                        </td>
		                                        <td>
	                                            <a href="javascript:removeContact('${ampContactId}')">
	                                                <img alt="delete" src= "/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0"/>
	                                            </a>
	                                        </td>
                                        </tr>
                                    </c:forEach>
                                </table>
                                <c:if test="${fn:length(aimAddOrgForm.orgContacts)>1}">
                                	</div>
                            	</c:if>
                        </td>
                    </tr>
                      <tr>
                        <td colspan="2" class="tdBoldClass" style="text-align:left;"><input type="checkbox"  onclick="selectAll('selectedContactInfoIds')"><digi:trn>Select All</digi:trn>&nbsp;&nbsp;<input type="button" onclick="removeContact()" value="<digi:trn>Delete</digi:trn>"></td>
                    </tr>
                </c:if>
                <tr>
                    <td colspan="2"><aim:addContactButton collection="orgContacts" form="${aimAddOrgForm}" addOrgBtn="hidden"><digi:trn>Add contact</digi:trn></aim:addContactButton></td>
                </tr>

            </table>
                </div>
        </fieldset>
    </td>
</tr>
<c:if test="${aimAddOrgForm.type!='NGO'}">
    <tr>
        <td style=" text-align:right" class="tdBoldClass"><digi:trn>Organization URL</digi:trn></td>
        <td>
            <html:text property="orgUrl" styleId="orgUrl"/>
        </td>
    </tr>
    <tr>
        <td style=" text-align:right" class="tdBoldClass"><digi:trn>Address</digi:trn></td>
        <td width="500px" height="30px">
            <html:textarea property="address" styleId="address"/>
        </td>
    </tr>
    <tr>
        <td style=" text-align:right" class="tdBoldClass"><digi:trn>Description</digi:trn></td>
        <td width="500px" height="30px">
            <html:textarea property="description" styleId="description"/>
        </td>
    </tr>
    <tr>
        <td colspan="2" align="center"><c:set
                var="showTheFollowingDocuments" value="PUBLIC" /> <c:set
            var="documentsType"><%=org.digijava.module.aim.dbentity.AmpOrganisationDocument.SESSION_NAME%></c:set>
            <html:button styleClass="dr-menu" property="submitButton"
                         onclick="addDocumentsDM('${documentsType}','${showTheFollowingDocuments}')">
                <digi:trn key="btn:addDocumentsFromRepository">Add Documents From Repository</digi:trn>
            </html:button> <br />
            <br />
        </td>
    </tr>
</c:if>

<tr>
    <td colspan="2" width="555" align="center" height="30">
        <table width="100%" >
            <tr>
                <td align="center"><html:button
                        styleClass="dr-menu" property="submitButton"
                        onclick="return validateSaveOrg()">
                        <digi:trn key="btn:save">Save</digi:trn>
                </html:button><input type="reset"
                                                   value='<digi:trn key="btn:reset">Reset</digi:trn>'
                                                   class="dr-menu">
                <input
                        type="button"
                        value="<digi:trn key="btn:cancel">Cancel</digi:trn>"
                        class="dr-menu" onclick="move()"></td>
            </tr>
            <c:if test="${not empty aimAddOrgForm.ampOrgId&&aimAddOrgForm.ampOrgId!=0}">
                <tr>
                    <td align="center">
                        <input type="button" value="<digi:trn key="btn:deleteThisOrganization">Delete this Organization</digi:trn>" onclick="return msg()">
                    </td>
                </tr>
            </c:if>
        </table>
    </td>
</tr>

</table>
</td>

</tr>
</table>
</td>
</tr>
</table>

</digi:form>
<script language="javascript"  type="text/javascript">
    setStyle(document.getElementById("staffTable"),false);
    setStyle(document.getElementById("orgInfosTable"),false);
    setStyle(document.getElementById("table_contact_content"),true);
</script>
