<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@taglib uri="/taglib/struts-html" prefix="html"%>
<%@taglib uri="/taglib/digijava" prefix="digi"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/category" prefix="category" %>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>


<link rel="stylesheet" type="text/css" href="<digi:file src="module/aim/css/tree.css"/>">
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/tree/yahoo.js"/>" ></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/tree/event.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/tree/treeview.js"/>" ></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/tree/jktreeview.js"/>" ></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<link rel="stylesheet" type="text/css" href="<digi:file src="module/aim/css/container.css"/>">
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/yahoo-dom-event.js"/>" ></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/container-min.js"/>" ></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/connection-min.js"/>" ></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/dragdrop-min.js"/>" ></script>

<style type="text/css">
	a { text-decoration: underline; color: #46546C; }
	a:hover { text-decoration: underline; color: #4d77c3; }
	#tree {width:250px;padding: 10px;float:left;}
	.treeIndShow{
		display : block;
		height : 1%;
		vertical-align: top;
	}
	.treeIndHide{
		display : none;
		height : 1%;
		vertical-align: top;
	}
</style>

<digi:form action="/nationalPlaningDashboard.do">

<c:set var="noProgSelected">
	<digi:trn key="aim:npd:noProgSelected">
			Please select a program before selecting a filter !
	</digi:trn>
</c:set>

<script language="javascript" type="text/javascript">

	var ptree;
	var curProgId;
	var curProgNodeIndex;
	var curNodeId;
	var curProgramName;
	var curGraphURL = null;
	var lastTreeUpdate=0;
	var line=new Array();
	var lineIter=0;
	var openNodes=new Array();
	var treeXML=null;
	var activityXML=null;
	var p1d='?';
	var pd='&';
	var curIndicatorIDs=[];
	var curIndicatorNames=[];
	var selIndicatorIDs=[];
	var selYear =[];
	var selActStatus = null;
	var selActDonors = null;
	var selActYearTo = null;
	var selActYearFrom = null;
	var progIdHistory = [];
	var progNodeHistory = [];
    var pr;
    var lastTimeStamp;
	var strNoActivities='<digi:trn key="aim:NPD:noActivitisLabel">No Activities</digi:trn>';
	var strTotal='<digi:trn key="aim:NPD:totalLabels">Totals:</digi:trn>';
	var strThousands='<digi:trn key="aim:NPD:thousandsLabel">All amounts are in thousands (000)</digi:trn>';
	var strPlanned='<digi:trn key="aim:NPD:sumplaned">Planned</digi:trn>';
	var strActual='<digi:trn key="aim:NPD:sumactual">Actual</digi:trn>';
	var strProposed='<digi:trn key="aim:NPD:sumproposed">Proposed</digi:trn>';

	function changeOptions(indics,years,locations){
        selIndicatorIDs=new Array();
        for (var i = 0; i < indics.length; i++) {
          selIndicatorIDs[i]=indics[i];
        }

		selYear=new Array();
        for (var i = 0; i < years.length; i++) {
          selYear[i]=years[i];
        }

		getNewGraph();
	}

    function openOptionsWindow(){
      if(curProgId==null){
        alert('please first select program in the tree');
      }else{
        var url=addActionToURL('npdOptions.do');
        url+= p1d+'programId='+curProgId;

        if (selIndicatorIDs != null && selIndicatorIDs.length > 0 ){
          for (var i=0; i<selIndicatorIDs.length; i++){
            url+= pd + 'selIndicators='+ selIndicatorIDs[i];
          }
        }

        if(selYear!=null){
          for (var y=0; y<selYear.length; y++){
            url += pd + 'selYears=' + selYear[y];
          }
        }
        var win=openURLinResizableWindow(url,600,400);
      }
    }

	function getInidcatorsParam(){
		var params = p1d + 'programId='+curProgId;
		if(selYear!=null){
			for (var y=0; y<selYear.length; y++){
				params += pd + 'selYears=' + selYear[y];
			}
		}
		return params;
	}

	function openGridWindow(showGraph){
		if (curProgId !=null){
			var url=addActionToURL('npdGrid.do');
			var params = getInidcatorsParam();
			if (showGraph == true ){
				params += pd + 'mode=1';
			}
			url += params;
			var win = openURLinResizableWindow(url,600,600);
		}
	}

	function getNewGraph(){
		//var now = new Date().getTime();
		lastTimeStamp = new Date().getTime();
		var url=constructGraphUrl(lastTimeStamp);
		var graphTag=document.getElementById('graphImage');
		graphTag.src=url;
	}

	function constructGraphUrl(timestmp){
		var url=addActionToURL('npdGraph.do');
		url+=p1d+'actionMethod=displayChart';
		url+=pd+'timestamp='+timestmp;
		if (curProgId != null){
			url += pd + 'currentProgramId=' + curProgId;
			if ( (selIndicatorIDs != null) && (selIndicatorIDs.length > 0)){
				for (var i=0; i<selIndicatorIDs.length; i++){
					url += pd + 'selectedIndicators=' + selIndicatorIDs[i];
				}
			}
			if ( selYear != null && selYear.length>0) {
				for (var y=0; y<selYear.length; y++) {
					url += pd + 'selectedYears=' + selYear[y];
				}
			}
		}
		curGraphURL = url;
		return url;
	}

	function mapCallBack(status, statusText, responseText, responseXML){
		updateMap(responseText);
	}

	function updateMap(resp){
		var mapHolder= document.getElementById('graphMapPlace');
		var map= document.getElementById('npdChartMap');
		var image = document.getElementById('graphImage');
		image.removeAttribute('usemap');
		mapHolder.innerHTML=resp;
		image.setAttribute('usemap','#npdChartMap');
	}

	function constructMapUrl(timestmp){
		var url=addActionToURL('getNpdGraphMap.do');
		url+=p1d+'timestamp='+timestmp;
		return url;
	}

	function getGraphMap(timestamp){
		var url=constructMapUrl(timestamp);

		var async=new Asynchronous();
		async.complete=mapCallBack;
		async.call(url);
	}

	function graphLoaded(){
			getGraphMap(lastTimeStamp);
			setGraphVisibility(true);
	}

	function setGraphVisibility(show){
		var loadingDiv=document.getElementById('divGraphLoading');
		var graphDiv=document.getElementById('divGraphImage');
		if(show){
			loadingDiv.style.display='none';
			graphDiv.style.display='block';
		}else{
			loadingDiv.style.display='block';
			graphDiv.style.display='none';
		}
	}

	function setCurProgData(progId,nodeId){
		if (curProgId == null || curProgId != progId ){
			//var newNode=YAHOO.widget.TreeView.getTree('tree').collapseAll();
			progIdHistory[progIdHistory.length]=curProgId;
			progNodeHistory[progNodeHistory.length]=curNodeId;
			setTreeIndicatorsVisibility(curProgId,false);

			var prog=findProgWithID(treeXML,progId);
			var gHeader=document.getElementById('graphHeader');
			var aListHeader = document.getElementById('actListProgname');
			gHeader.innerHTML=prog.getAttribute('name');
			aListHeader.innerHTML=prog.getAttribute('name');
			curIndicatorIDs=[];
			curIndicatorNames=[];
			selIndicatorIDs=[];
			setAllIndicatorsOf(prog,true);
			setTreeIndicatorsVisibility(progId,true);
		}
		curProgId=progId;
		curNodeId=nodeId;
		//var newNode=YAHOO.widget.TreeView.getNode('tree',nodeId);
		//newNode.expand();
	}

	function setAllIndicatorsOf(prog, recursive){
		var allIndics=null;
		var subNodes=prog.childNodes;
		if (subNodes != null){
			var children=null;
			//check all subnodes, they may be children or indicators
			for (var i=0;i<subNodes.length;i++){
				//if indicators, then add all to the indicators array
				if (subNodes[i].tagName == 'indicators'){
					var myIndicators=subNodes[i].childNodes;
					for (var ind=0; ind<myIndicators.length; ind++){
						if (myIndicators[ind].tagName=='indicator'){
							curIndicatorIDs[curIndicatorIDs.length]=myIndicators[ind].getAttribute('id');
							curIndicatorNames[curIndicatorNames.length]=myIndicators[ind].getAttribute('name');
							//this is temporary
							selIndicatorIDs[selIndicatorIDs.length]=myIndicators[ind].getAttribute('id');
						}
					}

				}
				if (subNodes[i].tagName == 'children'){
					//just store children progs for later use
					children = subNodes[i].childNodes;
				}
			}
			//if recursive then also run on all children progs
			if (recursive && children != null) {
				for(var ch=0;ch<children.length;ch++){
					if (children[ch].tagName=='program'){
						setAllIndicatorsOf(children[ch],recursive);
					}
				}
			}
		}
	}

	function getAllIndicatorsOf(prog, recursive){
		var result=[];
		var subNodes=prog.childNodes;
		if (subNodes != null){
			var children=null;
			//check all subnodes, they may be children or indicators
			for (var i=0;i<subNodes.length;i++){
				//if indicators, then add all to the indicators array
				if (subNodes[i].tagName == 'indicators'){
					var myIndicators=subNodes[i].childNodes;
					for (var ind=0; ind<myIndicators.length; ind++){
						if (myIndicators[ind].tagName=='indicator'){
							result[result.length]=myIndicators[ind];
						}
					}

				}
				if (subNodes[i].tagName == 'children'){
					//just store children progs for later use
					children = subNodes[i].childNodes;
				}
			}
			//if recursive then also run on all children progs
			if (recursive && children != null) {
				for(var ch=0;ch<children.length;ch++){
					if (children[ch].tagName=='program'){
						setAllIndicatorsOf(children[ch],recursive);
					}
				}
			}
		}
		return result;
	}

	/* ========  Tree view methods START ======== */

	/* Node label click */
	function browseProgram(programId, nodeId) {
		setGraphVisibility(false);
		setCurProgData(programId,nodeId);
		//setTreeIndicators(nodeId);
		getNewGraph();
		getActivities();
		//setGraphVisibility(true);
		//return false;
	}

	function setTreeIndicatorsVisibility(progId,visible){
		var node=document.getElementById('indTreeList'+progId);
		if (node !=null){
			if (visible){
				node.style.display='block';
				//node.className='treeIndShow';
			}else{
				node.style.display='none';
				//node.className='treeIndHide';
				//var par1 = node.parentNode.parentNode.parentNode.parentNode.parentNode;
				//redrowOld(par1);
			}
		}
	}

	function redrowOld(elem){
		var ch = (elem!=null)?elem.childNodes : null;
		if (ch !=null){
			for(var i=0;i<ch.length;i++){
				ch[i].style.display='none';
			}
			for(var i=0;i<ch.length;i++){
				if (ch[i]!=elem){
					ch[i].style.display='block';
				}
			}
		}
	}

	/* finds programs with specified ID */
	function findProgWithID(progTree, toFindID){
		if (toFindID==null) return null;
		var allProgs=progTree.getElementsByTagName("program");
		for(var i=0;i<allProgs.length;i++){
			var id=allProgs[i].getAttribute("id");
			if(id==toFindID) return allProgs[i];
		}
	}

	/* fill array with program and its parents*/
	function fillLine(progTree, curProg){
		if (curProg==null){
			return;
		}

		line[lineIter]=curProg.getAttribute("id");

		lineIter++;
		var parentID=curProg.getAttribute("parentID");
		if (parentID!=-1){
			var parentProg=findProgWithID(progTree,parentID);
			fillLine(progTree,parentProg);
		}
	}


	/*determines if node should be openned.
	Nod should be opened if it is parent of the current node.
	This function uses array that was filled with fillLine() functioin */
	function toBeOpened(progID){
		for(var i=0;i<line.length;i++){
			if (line[i]==progID) return true;
		}
//		return false;
	}

	function treeNodeLabelWasClicked(node){
		var pId=node.programId;
		return browseProgram(pid);
	}

	/* creates tree view object from XML. this is called form callback*/
	function updateTree(myXML){
		if (myXML==null) {
			return;
		}

		var targetDiv=document.getElementById("tree");

		var progListRoot=myXML.getElementsByTagName("progTree")[0];

		if (progListRoot==null){
			targetDiv.innerHTML=" "+myXML;
			return;
		}

		clearTree(targetDiv);
		var programList=progListRoot.childNodes;

		if (programList==null || programList.length==0){
			targetDiv.innerHTML="<i>No Programs</i>";
			return;
		}

		//store XML of tree;
		treeXML=progListRoot;

		// this value now is uddated not from form but by click on the tree node
		//curProgId=document.getElementsByName('currentProgramId')[0].value;
		var curProg=findProgWithID(progListRoot,curProgId);

		//determine which programs should be openned
		fillLine(progListRoot,curProg);

		//create TreeView Object for specified with ID HTML object.
		ptree=new jktreeview("tree");

		//setup click event
		ptree.labelCkick=treeNodeLabelWasClicked;

		//build nodes
		buildTree(programList,ptree,"");

		//draw tree
		ptree.treetop.draw();

		openNodeIter=0;

		//open nodes
		goToCurrentNode();

		//highlight current Node
		highlightNode(curProgNodeIndex);
		
		setNumOfPrograms (myXML);
		addRootListener();
		addEventListeners();
		
		createPanel("Info","<i>info</i>");
	}

	/* recursivly builds tree nodes */
	function buildTree(programs,treeObj,target){
		if(programs==null || programs.length==0 || target==null){
			return null;
		}
		//for every nod in this level:
		for(var i=0;i<programs.length;i++){
			var prg=programs[i];
			if(prg.tagName=="program"){
				//prepare node data
				var prgID=prg.getAttribute("id");
				var prgName=prg.getAttribute("name");
				var prgURL="../../aim/nationalPlaningDashboard.do";//URL is not used currently //"javascript:browseProgram('"+prgID+"')";
				var prgParent=target;
				//create tree view node object
				var thisNode=treeObj.addItem(prgName,prgParent,prgURL);
				//set new field, this is used in node HTML geeration
				thisNode.programId=prgID;
				//save node if it is parent of current or current program
				if(toBeOpened(prgID)==true){
					openNodes[openNodes.length]=thisNode;
				}
				//save index in tree for current program
				if(prgID==curProgId){
					curProgNodeIndex=thisNode.index;
				}
				thisNode.indicators=getIndicatorsHTML(prg);
				var subNodes=prg.childNodes;
				//recurs on children programs
				if(subNodes != null){
					for (var j=0;j<subNodes.length;j++){
						if (subNodes[j].tagName=="children"){
							var subProgs=subNodes[j].childNodes;
							buildTree(subProgs,treeObj,thisNode);
						}
					}
				}
			}
		}
	}



	/* opens previously saved nodes */
	function goToCurrentNode(){
		for(var i=0;i<openNodes.length;i++){
			openNodes[i].toggle();
		}
	}

	function getIndicatorsHTML(prog){
		var indics=getAllIndicatorsOf(prog,false);
		var result='No Indicators';
		if (indics.length>0){
			result='<table border="0" cellpadding="0" cellspacing="0">';
			for(var i=0;i<indics.length;i++){
				result+= '<tr><td>';
				//result+= '<input type="checkbox" name="selectedIndicators" onchange="return doFilter()" value="'+indIdArr[i]+'" ';
				//if(isSelectedIndicator(indIdArr[i])){
				//	result+='checked ';
				//}
				//result+=' >';
				var name = indics[i].getAttribute('name');
				result+='&gt;&nbsp;'+name+'</td>';//<td>&nbsp;&nbsp;</td>';
				result+='</tr>';
			}
			result+='</table>';
		}
		return result;
	}


	/* highlights node for current program 	*/
	function highlightNode(nodeIndex){
		if(nodeIndex!=null){
			var nodTD=document.getElementById('ygtvlabelel'+nodeIndex);
			if(nodTD!=null){
				nodTD.style.fontWeight='bolder';
//				var color=getRealColor(nodTD);
//				nodTD.style.color=nodTD.style.backgrundColor;
//				nodTD.style.backgrundColor=color;
			}
		}

	}

	function getRealColor(element){
		if (element==null) return;
		if (element.style.color==''){
			if (element.parentNode!=null){
				return getRealColor(element.parentNode);
			}else{
				return;
			}
		}else {
			return element.style.color;
		}
	}


	function isSelectedIndicator(indicId){
		for(var i=0;i<selIndics.length;i++){
			if (selIndics[i]==indicId) return true;
		}
		return false;
	}

	function clearTree(myTree){
		while (myTree.childNodes.length>0){
			myTree.removeChild(myTree.childNodes[0]);
		}
	}

	function addActionToURL(actionName){
		var fullURL=document.URL;
		var lastSlash=fullURL.lastIndexOf("/");
		var partialURL=fullURL.substring(0,lastSlash);
		return partialURL+"/"+actionName;
	}

	/* callback function which is registered for asynchronouse responce.
		this function should have special signature to be used as callback in the asynchronous class
	*/
	function treeCallBack(status, statusText, responseText, responseXML){
		updateTree(responseXML);
	}

	/* ========  Tree view methods END ========= */



	/* ========  Activities list methods START ======== */

	function getActivities(){
		if (curProgId == null) {
			alert('${noProgSelected}');
			return;
		}
		var actList=document.getElementById('activityListPlace');
		//actList.innerHTML="<i>Loading...</i>"
		setActivityLoading(actList);
		var url=getActivitiesURL();	
		var async=new Asynchronous();
		async.complete=activitiesCallBack;
		async.call(url);
	}

	function getActivitiesURL(){
		var result = addActionToURL('getActivities.do');
		if (curProgId != null ){
			result+=p1d+'programId='+curProgId;
		}
		if (selActStatus != null && selActStatus != '0'){
			result += pd + 'statusId='+ selActStatus;
		}
		if(selActDonors !=null && selActDonors != -1){
			result+= pd+ 'donorId='+selActDonors;
		}
		if (selActYearTo != null && selActYearTo != -1){
			result+= pd + 'endYear='+selActYearTo;
		}
		if (selActYearFrom != null && selActYearFrom != -1){
			result+= pd + 'startYear='+selActYearFrom;
		}
		return result;
	}

	function activitiesCallBack(status, statusText, responseText, responseXML){
		activityXML=responseXML;
		setUpActivityList(responseXML);
	}

	function setUpActivityList(xml){
		var tr= document.getElementById('activityListPlace');
		var tbl= tr.parentNode;

		clearActivityTable(tr);

		var root=xml.getElementsByTagName('activityList')[0];
		if(root==null){
			root=xml.getElementsByTagName('error')[0];
			if (root!=null){
				showError(root,tbl);
			}else{
				var newTR=document.createElement('TR');
				newTR.innerHTML='<td colspan="8">Unknown Error</td>';
				tbl.appendChild(newTR);
			}
			return;
		}
		//get activities array
		var actList = root.childNodes;
		if (actList == null || actList.length == 0){
			var newTR=document.createElement('TR');
			newTR.innerHTML='<td colspan="8">'+strNoActivities+'</td>';
			tr.parentNode.appendChild(newTR);
			return;
		}
		for (var i=0; i< actList.length; i++) {
			if (actList[i].tagName=='activity'){
				var actTR = document.createElement('TR');
				//name
				var actTDname = document.createElement('TD');
				var actAname = document.createElement('a');
				actAname.innerHTML=actList[i].getAttribute('name');
				var actURL = addActionToURL('showPrinterFriendlyPage.do~edit=true~activityid=');
				actURL+=actList[i].getAttribute('id');
				actAname.href=actURL;
				actAname.target='_blank';
				actTDname.appendChild(actAname);
				actTR.appendChild(actTDname);
				//status
				var actTDstatus = document.createElement('TD');
				actTDstatus.innerHTML=actList[i].getAttribute('status');
				actTR.appendChild(actTDstatus);
				//donor
				var actTDdonor = document.createElement('TD');
				getDonorsHTML(actList[i].childNodes,actTDdonor);
				actTR.appendChild(actTDdonor);
				//sart year
				var actTDfromYear = document.createElement('TD');
				actTDfromYear.colSpan=2;
				actTDfromYear.innerHTML=actList[i].getAttribute('date');
				actTR.appendChild(actTDfromYear);
				//amount
				var actTDproposedAmount = document.createElement('TD');
				actTDproposedAmount.innerHTML = actList[i].getAttribute('proposedAmount');
				actTR.appendChild(actTDproposedAmount);

				var actTDplannedAmount = document.createElement('TD');
				actTDplannedAmount.innerHTML = actList[i].getAttribute('plannedAmount');
				actTR.appendChild(actTDplannedAmount);

				var actTDActualAmount = document.createElement('TD');
				actTDActualAmount.innerHTML = actList[i].getAttribute('actualAmount');
				actTR.appendChild(actTDActualAmount);


				//row to table
				tbl.appendChild(actTR);
			}
		}//for


		//show Sum

		if (strTotal==null || strTotal==''){
			//if no translations
			strTotal='Total:';
		}
		//totals tr
		var lastTR = document.createElement('TR');

		var lastTD = document.createElement('TD');
		lastTD.colSpan=5;
		lastTD.align='right';
		lastTD.innerHTML='<strong>'+strTotal+' </strong>';
		lastTR.appendChild(lastTD);

		var propSumTD = document.createElement('TD');
		propSumTD.innerHTML= root.getAttribute('proposedSum');
		lastTR.appendChild(propSumTD);

		var planSumtTD = document.createElement('TD');
		planSumtTD.innerHTML=root.getAttribute('plannedSum');
		lastTR.appendChild(planSumtTD);

		var actSumTD = document.createElement('TD');
		actSumTD.innerHTML=root.getAttribute('actualSum');
		lastTR.appendChild(actSumTD);

		tbl.appendChild(lastTR);

		//sum labels
		var labelsTR1 = document.createElement('TR');
		var labelsTD0 = document.createElement('TD');
		labelsTD0.colSpan=5;
		labelsTD0.align='right';
		labelsTD0.innerHTML='&nbsp;';
		labelsTR1.appendChild(labelsTD0);

		var labelTD1 = document.createElement('TD');
		labelTD1.innerHTML='<strong>'+strProposed+' </strong>';
		labelsTR1.appendChild(labelTD1);

		var labelTD2 = document.createElement('TD');
		labelTD2.innerHTML='<strong>'+strPlanned+' </strong>';
		labelsTR1.appendChild(labelTD2);

		var labelTD3 = document.createElement('TD');
		labelTD3.innerHTML='<strong>'+strActual+' </strong>';
		labelsTR1.appendChild(labelTD3);

		tbl.appendChild(labelsTR1);


		//tousands label
		if (strThousands==null || strThousands==''){
			strThousands='All amounts are in thousands (000)';
		}
		var lastTR1 = document.createElement('TR');
		var lastTD1 = document.createElement('TD');
		lastTD1.colSpan=8;
		lastTD1.align='right';
		lastTD1.innerHTML='<font color="blue">'+strThousands+' </font>';
		lastTR1.appendChild(lastTD1);
		tbl.appendChild(lastTR1);

	}

	function getDonorsHTML(donors,target){
		if (donors !=null && donors.length >0 && donors[0].tagName=='donors'){
			var donorList = donors[0].childNodes;
			if (donorList !=null && donorList.length>0){
				var donorTable = document.createElement('table');
				donorTable.cellSpacing=0;
				for (var i=0; i<donorList.length; i++){
					var donorTr = document.createElement('tr');
					var donorTd = document.createElement('td');
					donorTd.innerHTML = donorList[i].getAttribute('name');
					donorTr.appendChild(donorTd);
					donorTable.appendChild(donorTr);
				}
				target.appendChild(donorTable);
			}else{
				target.innerHTML = ' ';
			}
		}else{
			target.innerHTML = ' ';
		}
	}

	function showError(stackList,where){
		if (stackList !=null && stackList.childNodes !=null){
			for (var i=0; i<stackList.childNodes.length; i++){
				if (stackList.childNodes[i].tagName=='frame'){
					var tr=document.createElement('TR');
					var td=document.createElement('TD');
					td.colSpan=6;
					td.innerHTML=stackList.childNodes[i].textContent;

					tr.appendChild(td);
					where.appendChild(tr);
				}
			}
		}
	}

	function clearActivityTable(firstTR){
		var par=firstTR.parentNode;
		while(firstTR.nextSibling != null){
			par.removeChild(firstTR.nextSibling);
		}
	}

	function setActivityLoading(firstTR){
		var par=firstTR.parentNode;
		clearActivityTable(firstTR);
		var tr=document.createElement('tr');
		var str='<td align="center" colspan="6"><img src="/TEMPLATE/ampTemplate/images/amploading.gif" alt="loading..."/>loading...</td>';
		tr.innerHTML=str;
		par.appendChild(tr);
	}

	function filterStatus(){
		var stat = document.getElementsByName('selectedStatuses')[0];
		selActStatus = stat.value;
		getActivities();
	}

	function filterDonor(){
		var donors = document.getElementsByName('selectedDonors')[0];
		selActDonors = donors.value;
		getActivities();
	}

	function filterFromYear(){
		var stat = document.getElementsByName('yearFrom')[0];
		selActYearFrom = stat.value;
		getActivities();
	}

	function filterToYear(){
		var stat = document.getElementsByName('yearTo')[0];
		selActYearTo = stat.value;
		getActivities();
	}

	/* ========  Activities list methods END ========= */



	/* function to be called at page load to asynchronously call server for tree data.
		Later we may convert all page to asynchronous style to not refresh page at all.
	 */
	function initTree(){
		var treeList=document.getElementById('tree');
		treeList.innerHTML="<i>Loading...</i>"

		var url=addActionToURL("getThemeTreeNode.do");
		var async=new Asynchronous();
		async.complete=treeCallBack;
		async.call(url);
	}

	function setupYears(){
		var ys=document.getElementsByName('myYears'); //document.forms['aimNPDForm'].selYears;
		for (var i=0; i<ys.length; i++){
			selYear[selYear.length]=ys[i].value;
		}
	}

	function loadInitial(){

		setupYears();
		initTree();
	}
	
	/**
	*The code below is related to the DHTML panel
	*
	*/
	var numOfPrograms;				// Number of programs (themes) displayed
	var informationPanel;			// The panel on which the information is displayed
	var themeArray	= new Array();  // Array containing the formatted information for the themes

	/*Gets total number of programs from the xml tree */
	function setNumOfPrograms (xml) {
		var elements	=	xml.getElementsByTagName("program");
		numOfPrograms	= elements.length;
		//alert ("xml: " + numOfPrograms);
	}
	/* When clicking on a '+' to expand the tree the listeners for the other elements are refreshed */
	function addRootListener () {
		var tree		= document.getElementById('tree');
		YAHOO.util.Event.addListener(tree, "click", addEventListeners);
	}
	/* Adds listeners for all elemets in the tree */
	function addEventListeners () {
		for(var j=1;j<=numOfPrograms;j++){
							var n	= document.getElementById('ygtvlabelel'+j);
							YAHOO.util.Event.addListener(n, "mouseover", eventFunction);
							YAHOO.util.Event.addListener(n, "mouseout", hidePanel);
		}
	}
	/* Function that is executed when mouse over an element */
	function eventFunction(e) {
		//alert('S-a apelat eventFunction	' + this.href + '||' + getIdFromHref(this.href) );
		showPanel(this.innerHTML, getIdFromHref(this.href), e.clientX, e.clientY);
		
	}
	/* Extracts the id (database id of AmpTheme) from the href property */
	function getIdFromHref( href ) {
		var start	= href.indexOf("('");
		var end		= href.indexOf("',");
		return href.substring(start+2, end);
 	}
 	/* Creates the panel used to show information */
 	function createPanel(headerText, bodyText) {
 		//YAHOO.namespace("amp.container");
 		//alert('Create Panels');
 		//YAHOO.amp.container.panel2 = new YAHOO.widget.Panel("panel2", { width:"300px", visible:true, draggable:false, close:true } );
		//YAHOO.amp.container.panel2.setHeader("Panel #2 from Script");
		//YAHOO.amp.container.panel2.setBody("This is a dynamically generated Panel.");
		//YAHOO.amp.container.panel2.setFooter("End of Panel #2");
		//YAHOO.amp.container.panel2.render(document.body);
		
		//YAHOO.amp.container.panel2.moveTo(50,50);
		
		
		informationPanel	= new YAHOO.widget.Panel("infoPanel", { width:"300px", visible:false, draggable:false, close:true } );
		informationPanel.setHeader(headerText);
		informationPanel.setBody(bodyText);
		informationPanel.render(document.body);
		
		infoPanelObj	= document.getElementById('infoPanel');
		
		YAHOO.util.Event.addListener(infoPanelObj, "mouseover", makePanelVisible);
		YAHOO.util.Event.addListener(infoPanelObj, "mouseout", hidePanel);
		
 	}
 	/* Updates the panels header, body and position and makes it visible */
 	function showPanel(headerText, id, posX, posY) {
 		informationPanel.setHeader(headerText);
		informationPanel.setBody(themeArray[id]);
		informationPanel.moveTo(posX+2, posY+2);
		informationPanel.show();
 	}
 	/* Just makes the panel visible */
 	function makePanelVisible() {
 		informationPanel.show();
 	}
 	/* Just makes the panel invisible */
 	function hidePanel() {
 		informationPanel.hide();
 	}
 	/* Adds the information for a theme to the themeArray array in the corresponding position (=pid). */
 	function addProgramInformation(pid, programName, description, leadAgency, programCode, programType, targetGroups,
 				background, objectives, outputs, beneficiaries, environmentConsiderations) {
 			var panelBody =	"";
 			panelBody += '<table border="0">';
 			panelBody += '<tr><td align="left"><b><digi:trn key="aim:NPD:ProgramName">Program Name</digi:trn>:</b>&nbsp;</td><td>'+ programName +'</td></tr>';
 			panelBody += '<tr><td align="left"><b><digi:trn key="aim:NPD:Description">Description</digi:trn>:</b>&nbsp;</td><td>'+ description +'</td></tr>';
 			panelBody += '<tr><td align="left"><b><digi:trn key="aim:NPD:LeadAgency">Lead Agency</digi:trn>:</b>&nbsp;</td><td>'+ leadAgency +'</td></tr>';
 			panelBody += '<tr><td align="left"><b><digi:trn key="aim:NPD:ProgramCode">Program Code</digi:trn>:</b>&nbsp;</td><td>'+ programCode +'</td></tr>';
 			panelBody += '<tr><td align="left"><b><digi:trn key="aim:NPD:ProgramType">Program Type</digi:trn>:</b>&nbsp;</td><td>'+ programType +'</td></tr>';
 			panelBody += '<tr><td align="left"><b><digi:trn key="aim:NPD:TargetGroups">Target Groups</digi:trn>:</b>&nbsp;</td><td>'+ targetGroups +'</td></tr>';
 			panelBody += '<tr><td align="left"><b><digi:trn key="aim:NPD:Background">Background</digi:trn>:</b>&nbsp;</td><td>'+ background +'</td></tr>';
 			panelBody += '<tr><td align="left"><b><digi:trn key="aim:NPD:Objectives">Objectives</digi:trn>:</b>&nbsp;</td><td>'+ objectives +'</td></tr>';
 			panelBody += '<tr><td align="left"><b><digi:trn key="aim:NPD:Outputs">Outputs</digi:trn>:</b>&nbsp;</td><td>'+ outputs +'</td></tr>';
 			panelBody += '<tr><td align="left"><b><digi:trn key="aim:NPD:Beneficiaries">Beneficiaries</digi:trn>:</b>&nbsp;</td><td>'+ beneficiaries +'</td></tr>';
 			panelBody += '<tr><td align="left"><b><digi:trn key="aim:NPD:Environment">Environment Considerations</digi:trn>:</b>&nbsp;</td><td>'+ environmentConsiderations +'</td></tr>';
 			panelBody += '</table>'; 
 			
 			themeArray[pid]	= panelBody;
 	
 	}
	window.onload=loadInitial;;
</script>
<script language="javascript" type="text/javascript">
	<digi:instance property="aimNPDForm" />
	<logic:iterate id="theme" name="aimNPDForm" property="allThemes" type="org.digijava.module.aim.dbentity.AmpTheme" >
		addProgramInformation(	'<bean:write name="theme" property="ampThemeId" />',
								'<bean:write name="theme" property="name" />',
								'<bean:write name="theme" property="description" />',
								'<bean:write name="theme" property="leadAgency" />',
								'<bean:write name="theme" property="themeCode" />',
								'<bean:write name="theme" property="typeCategoryValue.value" />',
								'<bean:write name="theme" property="targetGroups" />',
								'<bean:write name="theme" property="background" />',
								'<bean:write name="theme" property="objectives" />',
								'<bean:write name="theme" property="outputs" />',
								'<bean:write name="theme" property="beneficiaries" />',
			 					'<bean:write name="theme" property="environmentConsiderations" />'
		);
	</logic:iterate>
</script>
<input type="hidden" id="hdYears" value=""/>
<input type="hidden" id="hdIndicators" value=""/>
<c:forEach var="sys" items="${aimNPDForm.selYears}">
	<html:hidden property="myYears" value="${sys}"/>
</c:forEach>
<table border="0" id="mainBodyTable" width="100%">
	<tr>
		<td>
			<table id="topParttable" border="0" width="100%">
				<tr>
					<td>
						<table cellpadding="0" cellspacing="0" border="0">
							<tr>
								<TD bgColor="#c9c9c7" class="box-title" width="80">                    &nbsp;
									<digi:link href="/viewPortfolioDashboard.do~actId=-1~indId=-1">
										<digi:trn key="aim:portfolioDashboard">Dashboard</digi:trn>
									</digi:link>
								</TD>
								<TD background="module/aim/images/corner-r.gif" height="17" width="17">                  </TD>
								<TD bgColor="#c9c9c7" class="box-title" width="220" >                    &nbsp;
								<digi:trn key="aim:npDashboard">National Planing Dashboard</digi:trn>
								</TD>
								<TD background="module/aim/images/corner-r.gif" height="17" width="17">                  </TD>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<table id="topLeftTable" border="0" width="100%" cellspacing="0" cellpadding="5">
							<tr bgcolor="silver">
								<td>
									<span id="graphHeader">&nbsp;</span>
								</td>
							</tr>
							<tr>
								<td valign="top">
									<div id="divGraphLoading" style="vertical-align: middle; display: none; width: ${aimNPDForm.graphWidth}px; height: ${aimNPDForm.graphHeight}px" align="center">
										<digi:img src="images/amploading.gif"/><digi:trn key="aim:NPD:loadingGraph">Loading...</digi:trn>
									</div>
									<div id="divGraphImage" style="display: block">
										<digi:context name="showChart" property="/module/moduleinstance/npdGraph.do"/>
				                        <c:url var="fullShowChartUrl" scope="page" value="${showChart}">
				                          <c:param name="actionMethod" value="displayChart" />
				                          <c:param name="currentProgramId" value="${aimNPDForm.programId}" />
				                          <c:forEach var="selVal" items="${aimNPDForm.selIndicators}">
				                            <c:param name="selectedIndicators" value="${selVal}" />
				                          </c:forEach>
				                        </c:url>
				                        <img id="graphImage" onload="graphLoaded()" alt="chart" src="${fullShowChartUrl}" width="${aimNPDForm.graphWidth}" height="${aimNPDForm.graphHeight}" usemap="#npdChartMap" border="0"/>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<table>
										<tr>
											<td nowrap="nowrap">
												<!-- input class="buton" type="button" onclick="openOptionsWindow()" name="addOrgs" value="Add Organization"/ -->
												<a href="JavaScript:openOptionsWindow();">
													<digi:trn key="aim:NPD:changeOptionsLink">Change Options</digi:trn>
												</a>
											</td>
											<td>
												&nbsp;
											</td>
											<td nowrap="nowrap">
												<a href="JavaScript:openGridWindow(false);">
													<digi:trn key="aim:NPD:viewTable_Link">View table</digi:trn>
												</a>
											</td>
											<td>
												&nbsp;
											</td>
											<td nowrap="nowrap">
												<a href="JavaScript:openGridWindow(true);">
													<digi:trn key="aim:NPD:viewAllLink">View All</digi:trn>
												</a>
											</td>
											<td width="100%">
												&nbsp;
											</td>
											<td align="right" nowrap="nowrap">
												<digi:link href="/advancedReportManager.do~clear=true"><digi:trn key="aim:NPD:advancedReportsLink">Advanced Reports</digi:trn></digi:link>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
					<td valign="top" width="100%">
						<div id="tree" style="width: 100%;"></div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%" border="0" cellpadding="5" cellspacing="0">
				<tr id="activityListPlace" bgcolor="silver">
					<td width="100%">
						<digi:trn key="aim:npd:plannedFor">Planned and Ongoing Activites for:</digi:trn>
						&nbsp;<span id="actListProgname">&nbsp</span>
					</td>
					<td>
						<c:set var="translation">
							<digi:trn key="aim:npd:dropDownAnyStatus">Any Status</digi:trn>
						</c:set>
						<category:showoptions outeronchange="filterStatus()" firstLine="${translation}" name="aimNPDForm" property="selectedStatuses"  keyName="<%= org.digijava.module.aim.helper.CategoryConstants.ACTIVITY_STATUS_KEY %>"  />
					
					</td>
					<td>
						<html:select property="selectedDonors" onchange="filterDonor()">
							<option value="-1"><digi:trn key="aim:npd:dropDownAnyDonor">Any Donor</digi:trn></option>
							<html:optionsCollection name="aimNPDForm" property="donors" value="value" label="label" />
						</html:select>
					</td>
					<td>
						<html:select property="yearFrom" onchange="filterFromYear()">
							<option value="-1"><digi:trn key="aim:npd:dropDownFromYear">From Year</digi:trn></option>
							<html:optionsCollection name="aimNPDForm" property="years" value="value" label="label" />
						</html:select>
					</td>
					<td>
						<html:select property="yearTo" onchange="filterToYear()">
							<option value="-1"><digi:trn key="aim:npd:dropDownToYear">To Year</digi:trn></option>
							<html:optionsCollection name="aimNPDForm" property="years" value="value" label="label" />
						</html:select>
					</td>
					<td nowrap="nowrap" colspan="3">

						<digi:trn key="aim:NPD:AmountAndCurrency">Amount And currency</digi:trn>
					</td>
				</tr>
				<tr id="activityResultsPlace">
					<td colspan="8">
						&nbsp;
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<span id="graphMapPlace">
	<map name="npdChartMap" id="npdChartMap">
	</map>
</span>
</digi:form>
