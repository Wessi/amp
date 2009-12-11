<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>


<%@page import="org.digijava.module.aim.helper.Constants"%><script type="text/javascript" language="JavaScript" src="<digi:file src="module/message/script/yahoo-dom-event.js"/>"></script>
<script type="text/javascript" language="JavaScript" src="<digi:file src="module/message/script/animation-min.js"/>"></script>
<script type="text/javascript" language="JavaScript" src="<digi:file src="module/message/script/autocomplete-min.js"/>"></script>

<!--begin custom header content for this example-->
<style type="text/css">
<!--

.yui-skin-sam .yui-ac{position:relative;font-family:arial;font-size: 100%}
.yui-skin-sam .yui-ac-input{position:absolute;width:100%;font-size: 100%}
.yui-skin-sam .yui-ac-container{position:absolute;top:1.6em;width:100%;}
.yui-skin-sam .yui-ac-content{position:absolute;width:100%;border:1px solid #808080;background:#fff;overflow:hidden;z-index:9050;}
.yui-skin-sam .yui-ac-shadow{position:absolute;margin:.3em;width:100%;background:#000;-moz-opacity:0.10;opacity:.10;filter:alpha(opacity=10);z-index:9049;}
.yui-skin-sam .yui-ac-content ul{margin:0;padding:0;width:100%;}
.yui-skin-sam .yui-ac-content li{margin:0;padding:2px 5px;cursor:default;white-space:nowrap;FONT-SIZE: 100%;}
.yui-skin-sam .yui-ac-content li.yui-ac-prehighlight{background:#B3D4FF;}
.yui-skin-sam .yui-ac-content li.yui-ac-highlight{background:#426FD9;color:#FFF;}

#myAutoComplete .yui-ac-content { 
    max-height:16em;overflow:auto;overflow-x:hidden; /* set scrolling */ 
    _height:16em; /* ie6 */ 
} 


#myAutoComplete ul {
	list-style: square;
	padding-right: 0px;
	padding-bottom: 2px;
}

#myAutoComplete div {
	padding: 0px;
	margin: 0px; 
}

#myAutoComplete,
#myAutoComplete2 {
    width:15em; /* set width here */
    padding-bottom:2em;
}
-->
</style>

<script language="JavaScript">

function OnBudgetRules ( textboxId,  messageElId, numOfCharsNeeded) {
	this.textboxEl		= null;
	this.messageEl		= null;
	this.textboxId					= textboxId;
	this.messageElId				= messageElId;
	this.numOfCharsNeeded	= numOfCharsNeeded;
}
OnBudgetRules.prototype.init		= function () {
	this.textboxEl		= document.getElementById( this.textboxId );
	this.messageEl		= document.getElementById( this.messageElId );
}
OnBudgetRules.prototype.check		= function () {
	if ( this.textboxEl == null ) {
		this.init();
	}
	var numOfChars		= this.textboxEl.value.length;
	if ( numOfChars < this.numOfCharsNeeded ){
		this.messageEl.innerHTML	= "<font color='red'> <digi:trn>Characters to add</digi:trn>: " + (this.numOfCharsNeeded-numOfChars) + "</font>" ;
	}
	else{
		this.textboxEl.value				= this.textboxEl.value.substr(0, this.numOfCharsNeeded);
		this.messageEl.innerHTML	= "<font color='green'><digi:trn>Ok</digi:trn></font>";
	}
}

imputationRules				= new OnBudgetRules ( "ImputationField", "ImputationSpan", <%= Constants.NUM_OF_CHARS_IMPUTATION%> );
codeChapitreRules			= new OnBudgetRules ( "CodeChapitreField", "CodeChapitreSpan", <%= Constants.NUM_OF_CHARS_CODE_CHAPITRE %> );

function doBudgetRulesCheck() {
	imputationRules	.check();
	codeChapitreRules.check();
}

YAHOOAmp.util.Event.addListener(window, "load", doBudgetRulesCheck ) ;


function toggleElement ( elementId, show ) {
	var displayValue;
	if ( show )
		displayValue	= '';
	else
		displayValue	= 'none';

 	var el			= document.getElementById( elementId );
 	if ( el != null )
 		el.style.display=displayValue;
}

function toggleBudgetFields( show ) {

	toggleElement("Imputation", show);
	toggleElement("CodeChapitre", show);
	toggleElement("FY", show);
	toggleElement("Vote", show);
	toggleElement("Sub-Vote", show);
	toggleElement("Sub-Program", show);
	toggleElement("ProjectCode", show);
	toggleElement("financial", show);

	toggleElement("Imputation1", show);
	toggleElement("CodeChapitre1", show);
	toggleElement("FY1", show);
	toggleElement("Vote1", show);
	toggleElement("Sub-Vote1", show);
	toggleElement("Sub-Program1", show);
	toggleElement("ProjectCode1", show);

	toggleElement("Imputation2", show);
	toggleElement("CodeChapitre2", show);
	toggleElement("FY2", show);
	toggleElement("Vote2", show);
	toggleElement("Sub-Vote2", show);
	toggleElement("Sub-Program2", show);
	toggleElement("ProjectCode2", show);
}

document.getElementsByTagName('body')[0].className='yui-skin-sam';
	function budgetCheckboxClick()
	{
		
		if (document.getElementById("budget") != null) {
			if((document.getElementById("budget").checked==false))
			{
					var hbudgetEl	= 	document.getElementById("hbudget");
					if ( hbudgetEl != null )
							hbudgetEl.value="false";
					
				 	toggleBudgetFields( false );
			 }
			else if(document.getElementById("budget").checked==true)
			{
				var hbudgetEl	= 	document.getElementById("hbudget");
				if ( hbudgetEl != null )
						hbudgetEl.value="true";

				 toggleBudgetFields ( true );
			}
		}
	}

function InitBud(){
	if(document.getElementById("hbudget").value=="true"){
		var budgetEl		= 	document.getElementById("budget");
		if ( budgetEl != null )
				budgetEl.checked=true;

		 toggleBudgetFields ( true );
	}
	else{
		var budgetEl		= 	document.getElementById("budget");
		if ( budgetEl != null )
				budgetEl.checked=false;

		 toggleBudgetFields ( false );
	}
}

function disableSelection(element) {

//alert("asd");
    element.onselectstart = function() {
      return false;
    };
    element.unselectable = "on";
    element.style.MozUserSelect = "none";
    element.style.cursor = "default";
}


function disableSelection1(target){

	for (i=0;i<target.childNodes.length;i++){
			//alert("ad "+i);
			nownode=target.childNodes[i];
			alert("ad "+i+" ::"+nownode);
			if (typeof nownode.onselectstart!="undefined") //IE route
				nownode.onselectstart=function(){return false}
			else if (typeof nownode.style.MozUserSelect!="undefined") //Firefox route
					nownode.style.MozUserSelect="none"
				else //All other route (ie: Opera)
			nownode.onmousedown=function(){return false}
			
	}

if (typeof target.onselectstart!="undefined") //IE route
	target.onselectstart=function(){return false}
else if (typeof target.style.MozUserSelect!="undefined") //Firefox route
	target.style.MozUserSelect="none"
else //All other route (ie: Opera)
	target.onmousedown=function(){return false}
	//alert("Ad");
target.style.cursor = "default"
}

</script>


<digi:instance property="aimEditActivityForm" />

										<table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
											<bean:define id="contentDisabled">false</bean:define>
											<c:set var="contentDisabled"><field:display name="Project Title" feature="Identification">false</field:display>
											</c:set>
											<c:if test="${contentDisabled==''}">
												<c:set var="contentDisabled">true</c:set>
											</c:if>
											<field:display name="Project Title" feature="Identification"></field:display>
											<tr bgcolor="#ffffff">											
												<td valign="top" align="left">
													<FONT color=red>*</FONT>
													<a title="<digi:trn key="aim:TitleInDonorsOrMoFEDInternalSystems">Title used in donors or MoFED internal systems</digi:trn>">
													<digi:trn key="aim:projectTitle">Project Title</digi:trn>
													</a>
												
												</td>
												<td valign="top" align="left">
													<a title="<digi:trn key="aim:TitleInDonorsOrMoFEDInternalSystems">
													Title used in donors or MoFED internal systems
													</digi:trn>">
													<html:textarea name="aimEditActivityForm" property="identification.title" cols="60" rows="2" styleClass="inp-text"  disabled="${contentDisabled}"/>
													</a>
												</td>											
											</tr>
											
											<field:display name="Project Comments" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:CommentsOfProject">Comments realted to the whole project</digi:trn>">
												<digi:trn key="aim:projectComments">Project Comments</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
												<table cellPadding="0" cellSpacing="0">
													<tr>
														<td>
															<bean:define id="projcomKey">
																<c:out value="${aimEditActivityForm.identification.projectComments}"/>
															</bean:define>
															<digi:edit key="<%=projcomKey%>"/>
														</td>
													</tr>
													<tr>
														<td>
															<a href="javascript:edit('${projcomKey}','Project Comments')">
															<digi:trn key="aim:edit">Edit</digi:trn></a>															
														</td>
													</tr>
												</table>
											</td></tr>
											</field:display>
																						
											<field:display name="Objective" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:ObjectivesAndComponentsofProject">The key objectives and main components of the project</digi:trn>">
												<digi:trn key="aim:objective">Objective</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
												<table cellPadding=0 cellSpacing=0>
													<tr>
														<td>
															<bean:define id="objKey">
																<c:out value="${aimEditActivityForm.identification.objectives}"/>
															</bean:define>
															<digi:edit key="<%=objKey%>"/>
												
														</td>
													</tr>
													<tr>
														<td>
															<%--
															<a href="<c:out value="${aimEditActivityForm.context}"/>/editor/showEditText.do?id=<%=objKey%>&referrer=<c:out value="${aimEditActivityForm.context}"/>/aim/addActivity.do?edit=true">Edit</a>
															--%>
															
																<a href="javascript:edit('<%=objKey%>','objective')">
																<digi:trn key="aim:edit">Edit</digi:trn></a>	
																&nbsp;
															
														<field:display name="Objectively Verifiable Indicators" feature="Identification">
															<a href="javascript:commentWin('objObjVerIndicators')" id="CommentObjObjVerIndicators"><digi:trn key="aim:addEditObjVerIndicators">Add/Edit Objectively Verifiable Indicators</digi:trn></a>
															&nbsp;
														</field:display>
														<field:display name="Assumptions" feature="Identification">
															<a href="javascript:commentWin('objAssumption')" id="CommentObjAssumption"><digi:trn key="aim:addEditAssumption">Add/Edit Assumption</digi:trn></a>
															&nbsp;
														</field:display>
														<field:display name="Verifications" feature="Identification">
															<a href="javascript:commentWin('objVerification')" id="CommentObjVerification"><digi:trn key="aim:addEditVerification">Add/Edit Verification</digi:trn></a>
														</field:display>		
														</td>
													</tr>
												</table>												
											</td></tr>
											</field:display>
											
											<field:display name="Description" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:DescriptionofProject">Information describing the project</digi:trn>">
												<digi:trn key="aim:description">Project Description</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
												<table cellPadding=0 cellSpacing=0>
													<tr>
														<td>
															<bean:define id="descKey">
																<c:out value="${aimEditActivityForm.identification.description}"/>
															</bean:define>
			
															<digi:edit key="<%=descKey%>"/>
														</td>
													</tr>
													<tr>
														<td>
															<%--
															<a href="<c:out value="${aimEditActivityForm.context}"/>/editor/showEditText.do?id=<%=descKey%>&referrer=<c:out value="${aimEditActivityForm.context}"/>/aim/addActivity.do?edit=true">Edit</a>
															--%>
																<a href="javascript:edit('<%=descKey%>','Project Description')">
																<digi:trn key="aim:edit">Edit</digi:trn></a>															
														</td>
													</tr>
												</table>
											</td></tr>
											</field:display>
											
											<!-- Purpose -->
											<field:display name="Purpose" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:PurposeofProject">Purpose of the project</digi:trn>">
												<digi:trn key="aim:purpose">
												Purpose
												</digi:trn>
												</a>
											</td>
											
											<td valign="top" align="left">
												<table cellPadding=0 cellSpacing=0>
												
													<tr>
														<td>
															<bean:define id="purpKey">
																<c:out value="${aimEditActivityForm.identification.purpose}"/>
															</bean:define>
			
															<digi:edit key="<%=purpKey%>"/>
														</td>
													</tr>
													<tr>
														<td>
															<%--
															<a href="<c:out value="${aimEditActivityForm.context}"/>/editor/showEditText.do?id=<%=descKey%>&referrer=<c:out value="${aimEditActivityForm.context}"/>/aim/addActivity.do?edit=true">Edit</a>--%>
																<a href="javascript:edit('<%=purpKey%>','Purpose')">
																<digi:trn key="aim:edit">Edit</digi:trn></a>
																&nbsp;
															<field:display name="Purpose Verifiable Indicators" feature="Identification">
																<a href="javascript:commentWin('purpObjVerIndicators')" id="CommentPurpObjVerInd"><digi:trn key="aim:addEditObjVerIndicators">Add/Edit Objectively Verifiable Indicators</digi:trn></a>
																&nbsp;
															</field:display>
															<field:display name="Purpose Assumptions" feature="Identification">
																<a href="javascript:commentWin('purpAssumption')" id="CommentPurpAssumption"><digi:trn key="aim:addEditAssumption">Add/Edit Assumption</digi:trn></a>
																&nbsp;
															</field:display>
															<field:display name="Purpose Verifications" feature="Identification">
																<a href="javascript:commentWin('purpVerification')" id="CommentPurpVerification"><digi:trn key="aim:addEditVerification">Add/Edit Verification</digi:trn></a>
															</field:display>
														</td>
													</tr>
												</table>
											</td></tr>
											</field:display>

											<field:display name="Results" feature="Identification">
											<!-- Results -->
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:ResultsofProject">Results of the project</digi:trn>">
												<digi:trn key="aim:results">
												Results
												</digi:trn>
												</a>
											</td>

											<td valign="top" align="left">
												<table cellPadding=0 cellSpacing=0>
													<tr>
														<td>
															<bean:define id="resKey">
																<c:out value="${aimEditActivityForm.identification.results}"/>
															</bean:define>
			
															<digi:edit key="<%=resKey%>"/>
														</td>
													</tr>
													<tr>
														<td>
															<%--
															<a href="<c:out value="${aimEditActivityForm.context}"/>/editor/showEditText.do?id=<%=descKey%>&referrer=<c:out value="${aimEditActivityForm.context}"/>/aim/addActivity.do?edit=true">Edit</a>--%>
			
															<a href="javascript:edit('<%=resKey%>','Results')">
															<digi:trn key="aim:edit">Edit</digi:trn></a>
																&nbsp;
															<field:display name="Results Verifiable Indicators" feature="Identification">
																<a href="javascript:commentWin('resObjVerIndicators')" id="CommentResObjVerInd"><digi:trn key="aim:addEditObjVerIndicators">Add/Edit Objectively Verifiable Indicators</digi:trn></a>
															&nbsp;
															</field:display>
															<field:display name="Results Assumptions" feature="Identification">
																<a href="javascript:commentWin('resAssumption')" id="CommentResAssumption"><digi:trn key="aim:addEditAssumption">Add/Edit Assumption</digi:trn></a>
																&nbsp;
															</field:display>
															<field:display name="Results Verifications" feature="Identification">
																<a href="javascript:commentWin('resVerification')" id="CommentResVerification"><digi:trn key="aim:addEditVerification">Add/Edit Verification</digi:trn></a>
															</field:display>
														</td>
													</tr>
												</table>
											</td></tr>
											</field:display>
											
											<bean:define id="largeTextFeature" value="Identification" toScope="request"/>
										
											<bean:define id="largeTextLabel" value="Lessons Learned" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.identification.lessonsLearned}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyEdit.jsp"/>
			
											<bean:define id="largeTextLabel" value="Project Impact" toScope="request"/>
											
												<bean:define id="largeTextKey" toScope="request">
													<c:out value="${aimEditActivityForm.identification.projectImpact}"/>
												</bean:define>
											<field:display name="Project Impact" feature="Identification">
											<tr bgcolor="#ffffff">
												<td valign="top" align="left">
												
										            <a title="<digi:trn key="aim:EditProjectImpact">Edit Project Impact</digi:trn>">
													     <digi:trn key="aim:${largeTextLabel}">${largeTextLabel}</digi:trn> 
													</a>
									            
												</td>
												<td valign="top" align="left">
												<table cellPadding=0 cellSpacing=0>
													<tr>
														<td><digi:edit key="${largeTextKey}" /></td>
													</tr>
													<tr>
														<td><a href="javascript:edit('${largeTextKey}','Project Impact')"><digi:trn key="aim:edit">Edit</digi:trn></a></td>
													</tr>
												</table>
												</td>
											</tr>
											</field:display>											





											<bean:define id="largeTextLabel" value="Activity Summary" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.identification.activitySummary}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyEdit.jsp"/>
			
											<bean:define id="largeTextLabel" value="Contracting Arrangements" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.identification.contractingArrangements}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyEdit.jsp"/>
			
											<bean:define id="largeTextLabel" value="Conditionality and Sequencing" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.identification.condSeq}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyEdit.jsp"/>
			
											<bean:define id="largeTextLabel" value="Linked Activities" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.identification.linkedActivities}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyEdit.jsp"/>
			
											<bean:define id="largeTextLabel" value="Conditionalities" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.identification.conditionality}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyEdit.jsp"/>
			
											<bean:define id="largeTextLabel" value="Project Management" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.identification.projectManagement}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyEdit.jsp"/>
											
											
											
																		
											
											<field:display name="Accession Instrument" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:DescriptionOfAccessionInstrument">Accession Instrument of the project</digi:trn>">
												<digi:trn key="aim:AccessionInstrument">
												Accession Instrument
												</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
													<c:set var="translation">
														<digi:trn key="aim:addActivityAccInstrFirstLine">Please select from below</digi:trn>
													</c:set>
													
													<category:showoptions firstLine="${translation}" name="aimEditActivityForm" property="identification.accessionInstrument" categoryName="<%= org.digijava.module.categorymanager.util.CategoryConstants.ACCESSION_INSTRUMENT_NAME %>" styleClass="inp-text" />
											</td></tr>	
											</field:display>

											<field:display name="Project Category" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:DescriptionOfProjectCategory">Project Category</digi:trn>">
												<digi:trn key="aim:ProjectCategory">
												Project Category
												</digi:trn>
												</a>
											</td>
											
											<td valign="top" align="left">
													<c:set var="translation">
														<digi:trn key="aim:addActivityAccInstrFirstLine">Please select from below</digi:trn>
													</c:set>
													<category:showoptions firstLine="${translation}" name="aimEditActivityForm" property="identification.projectCategory" categoryName="<%= org.digijava.module.categorymanager.util.CategoryConstants.PROJECT_CATEGORY_NAME %>" styleClass="inp-text" />													
											</td></tr>	
											</field:display>


											<field:display name="Government Agreement Number" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:step1:GovernmentAgreementNumTooltip">Government Agreement Number</digi:trn>">
												<digi:trn key="aim:step1:GovernmentAgreementNumTitle">
												Government Agreement Number
												</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
												<html:text name="aimEditActivityForm" property="identification.govAgreementNumber"/>
											</td></tr>	
											</field:display>
											
											
											<field:display name="Budget Code Project ID" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:step1:BudgetCodeProjectID">Budget Code Project ID</digi:trn>">
												<digi:trn key="aim:step1:BudgetCodeProjectID">
												Budget Code Project ID
												</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
											<div id="myAutoComplete">
												<html:text name="aimEditActivityForm" styleId="myInput" property="identification.budgetCodeProjectID" style="font-size: 9pt"/>
												<div id="myContainer"></div>
											</div>	
											</td></tr>	
											</field:display>
											
											<field:display name="A.C. Chapter" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:DescriptionofACChapter">A.C. Chapter of the project</digi:trn>">
												<digi:trn key="aim:acChapter">
												A.C. Chapter
												</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
													<c:set var="translation">
														<digi:trn key="aim:addActivityAcChapterFirstLine">Please select from below</digi:trn>
													</c:set>
													<category:showoptions firstLine="${translation}" name="aimEditActivityForm" property="identification.acChapter" categoryName="<%= org.digijava.module.categorymanager.util.CategoryConstants.ACCHAPTER_NAME %>" styleClass="inp-text" />
											</td></tr>											
											</field:display>
											
											<feature:display name="Budget" module="Project ID and Planning">
											
											<field:display name="On/Off Budget" feature="Budget">	
											<tr bgcolor="#ffffff">
												<td valign="top" align="left">

													<a title="<digi:trn key="aim:DescriptionofProject">Summary information describing the project</digi:trn>">
														<digi:trn key="aim:actBudget">Activity Budget</digi:trn>
													</a>
												</td>
											<td>
											<table cellpadding="7" cellspacing="5">

											 <tr>
												<td valign="top" align="left" colspan="9">	
													<html:checkbox styleId="budget" property="identification.budget"  onclick="budgetCheckboxClick();">
													<digi:trn key="aim:actBudgeton">Activity is On Budget</digi:trn>
													</html:checkbox>
													<html:hidden property="identification.budgetCheckbox" styleId="hbudget"/>
												</td>
											</tr>
											 <tr>
											
											<field:display name="Imputation" feature="Budget">
											<td valign="top" id="Imputation" align="center"  >
												<a title="<digi:trn>Imputation</digi:trn>">
												<digi:trn>
													Imputation
												</digi:trn>
												</a>
											</td>
											</field:display>
											
											<field:display name="Code Chapitre" feature="Budget">
											<td valign="top" id="FY" align="center"  >
												<a title="<digi:trn>Code Chapitre</digi:trn>">
												<digi:trn>
													Code Chapitre
												</digi:trn>
												</a>
											</td>
											</field:display>
											
											<field:display name="FY" feature="Budget">
											<td valign="top" id="FY" align="center" >
												<a title="<digi:trn key="aim:FY">FY</digi:trn>">
												<digi:trn key="aim:actFY">
												FY
												</digi:trn>
												</a>
											</td>
											</field:display>
											
										<field:display name="Vote" feature="Budget" >
											<td valign="top"  id="Vote" align="center" >
												<a title="<digi:trn key="aim:Vote">Vote</digi:trn>">
												<digi:trn key="aim:actVote">
												Vote
												</digi:trn>
												</a>
											</td>	
											</field:display>
											
											<field:display name="Sub-Vote" feature="Budget">
											<td valign="top" id="Sub-Vote" align="center" >
												<a title="<digi:trn key="aim:Sub-Vote">Sub-Vote</digi:trn>">
												<digi:trn key="aim:actSub-Vote">
												Sub-Vote
												</digi:trn>
												</a>
											</td>
											</field:display>
											
	
											
									<field:display name="Sub-Program" feature="Budget">
										<td valign="top" id="Sub-Program" align="center">
											<a title="<digi:trn key="aim:Sub_Program">Sub-Program</digi:trn>">
											<digi:trn key="aim:actSubProgram">
												Sub-Program
												</digi:trn>
												</a>
										</td>
									</field:display>
											
								<field:display name="Project Code" feature="Budget">
										<td valign="top" id="ProjectCode" align="center" >
											<a title="<digi:trn key="aim:ProjectCode">Project Code</digi:trn>">
											<digi:trn key="aim:actProjectCode">
												Project Code
												</digi:trn>
												</a>
									</td>
								</field:display>	
									<field:display name="Code Chapitre" feature="Budget"></field:display>
									</tr>
										<tr>
											<field:display name="Imputation" feature="Budget">
												<td valign="top"  id="Imputation1" align="center"  >
														<html:text property="identification.FY" size="22" styleId="ImputationField" onkeyup="imputationRules.check();"/>
												</td>
											</field:display>
										
											<field:display name="Code Chapitre" feature="Budget">
												<td valign="top"  id="CodeChapitre1" align="center">
														<html:text property="identification.projectCode" size="11" styleId="CodeChapitreField" onkeyup="codeChapitreRules.check();"/>
												</td>
											</field:display>
										
											<field:display name="FY" feature="Budget">
												<td valign="top"  id="FY1" align="center"  >
														<html:text property="identification.FY" size="12"/>
												</td>
											</field:display>
											
											<field:display name="Vote" feature="Budget">
												<td valign="top"  id="Vote1" align="center">
													<html:text property="identification.vote" size="12"/>
												</td>	
											</field:display>
											<field:display name="Sub-Vote" feature="Budget">
											<td valign="top"  id="Sub-Vote1" align="center" >
												<html:text property="identification.subVote" size="12"/>
											</td>
											</field:display>
											<field:display name="Sub-Program" feature="Budget">
												<td valign="top" id="Sub-Program1" align="center" >
													<html:text property="identification.subProgram" size="12"/>
												</td>
											</field:display>
											<field:display name="Project Code" feature="Budget">
												<td valign="top" id="ProjectCode1" align="center" >
													<html:text property="identification.projectCode" size="12"/>
												</td>
											</field:display>	
										</tr>
										<tr>
											<field:display name="Imputation" feature="Budget">
												<td valign="top"  id="Imputation2" align="center" >
														<span id="ImputationSpan">&nbsp;</span>
												</td>
											</field:display>
											
											<field:display name="Code Chapitre" feature="Budget">
												<td valign="top"  id="CodeChapitre2" align="center">
													<span id="CodeChapitreSpan">&nbsp;</span>
												</td>	
											</field:display>
											
											<field:display name="FY" feature="Budget">
												<td valign="top"  id="FY2" align="center"  >
														<span id="FYSpan">&nbsp;</span>
												</td>
											</field:display>
											
											<field:display name="Vote" feature="Budget">
												<td valign="top"  id="Vote2" align="center">
													<span id="VoteSpan">&nbsp;</span>
												</td>	
											</field:display>
											<field:display name="Sub-Vote" feature="Budget">
											<td valign="top"  id="Sub-Vote2" align="center">
												<span id="SubVoteSpan">&nbsp;</span>
											</td>
											</field:display>
											<field:display name="Sub-Program" feature="Budget">
												<td valign="top" id="Sub-Program2" align="center">
													<span id="SubProgramSpan">&nbsp;</span>
												</td>
											</field:display>
											<field:display name="Project Code" feature="Budget">
												<td valign="top" id="ProjectCode2" align="center">
													<span id="ProjectCodeSpan">&nbsp;</span>
												</td>
											</field:display>	
										</tr>
								</table>
								</td></tr>	
								</field:display>
									
								
								
								</feature:display>
								
								<field:display name="Financial Instrument" feature="Budget">
										<tr bgcolor="#ffffff" id="financial"><td valign="top" align="left" >
											<a title="<digi:trn key="aim:GBS">Financial Instrument</digi:trn>">
											<digi:trn key="aim:actGBS">
												Financial Instrument
												</digi:trn>
												</a>
											</td>
										<td valign="top" align="left" >
											<category:showoptions listView="false" name="aimEditActivityForm" property="identification.gbsSbs" categoryName="<%=org.digijava.module.categorymanager.util.CategoryConstants.FINANCIAL_INSTRUMENT_NAME %>" styleClass="inp-text" />
										</td>
									</tr>
								</field:display>	
								<field:display name="Government Approval Procedures" feature="Budget">
										<tr bgcolor="#ffffff"><td valign="top" align="left">
											<a title="<digi:trn key="aim:governmentApprovalProcedures">Government Approval Procedures </digi:trn>">
											<digi:trn key="aim:actGovernmentApprovalProcedures">
												Government Approval Procedures 
												</digi:trn>
												</a>
											</td>
										<td valign="top" align="left">
												<digi:trn key="aim:yes">Yes</digi:trn>
												<html:radio name="aimEditActivityForm" property="identification.governmentApprovalProcedures" value="true"/> &nbsp;&nbsp;<digi:trn key="aim:no">No</digi:trn>
												<html:radio name="aimEditActivityForm" property="identification.governmentApprovalProcedures" value="false"/>
									</td></tr>
								</field:display>	
								
								<field:display name="Joint Criteria" feature="Budget">
										<tr bgcolor="#ffffff"><td valign="top" align="left">
											<a title="<digi:trn key="aim:jointCriteria">Joint Criteria</digi:trn>">
											<digi:trn key="aim:actJointCriteria">
												Joint Criteria 
												</digi:trn>
												</a>
											</td>
										<td valign="top" align="left">
												<digi:trn key="aim:yes">Yes</digi:trn><html:radio property="identification.jointCriteria" value="true"/> &nbsp;&nbsp;<digi:trn key="aim:no">No</digi:trn><html:radio property="identification.jointCriteria" value="false"/>
									</td></tr>
								</field:display>
								
								<field:display name="Humanitarian Aid" feature="Identification">
									<tr bgcolor="#ffffff">
										<td valign="top" align="left">
											<a title="<digi:trn key="aim:humanitarianAid">Humanitarian Aid</digi:trn>">
												<digi:trn key="aim:humanitarianAid">
													Humanitarian Aid 
												</digi:trn>
											</a>
										</td>
										<td valign="top" align="left">
												<digi:trn key="aim:yes">Yes</digi:trn><html:radio property="identification.humanitarianAid" value="true"/> &nbsp;&nbsp;<digi:trn key="aim:no">No</digi:trn><html:radio property="identification.humanitarianAid" value="false"/>
										</td>
									</tr>
								</field:display>	

								<field:display name="Cris Number" feature="Identification">
									<tr bgcolor="#ffffff">
										<td valign="top" align="left">
											<a title="<digi:trn key="aim:crisNumber">Cris Number</digi:trn>">
												<digi:trn key="aim:crisNumber">
													Cris Number 
												</digi:trn>
											</a>
										</td>
										<td valign="top" align="left">
										    <html:text name="aimEditActivityForm" property="identification.crisNumber" size="12"/>
										</td>
									</tr>
								</field:display>	

								</table>
									<script>
										InitBud();
									</script>

<script type="text/javascript">
	var myArray = [
	   	<c:forEach var="relAct" items="${aimEditActivityForm.identification.budgetCodes}">
			"<bean:write name="relAct" filter="true"/>",
		</c:forEach>     
	];

	YAHOO.example.ACJSArray = new function() {
		// Instantiate JS Array DataSource
	    this.oACDS = new YAHOO.widget.DS_JSArray(myArray);
	    // Instantiate AutoComplete
	    this.oAutoComp = new YAHOO.widget.AutoComplete("myInput", "myContainer", this.oACDS);
	    //this.oAutoComp.prehighlightClassName = "yui-ac-prehighlight";    
	    this.oAutoComp.useShadow = true;
	    //this.oAutoComp.forceSelection = true;
        this.oAutoComp.maxResultsDisplayed = myArray.length; 
	    this.oAutoComp.formatResult = function(oResultItem, sQuery) {
	        var sMarkup = oResultItem[0];
	        return (sMarkup);
	    };
	}; 

</script>
									