YAHOO.namespace("YAHOO.amp.reportwizard");

YAHOO.amp.reportwizard.colIdToName		= new Array();
YAHOO.amp.reportwizard.fundingGroups	= new Array();
YAHOO.amp.reportwizard.fundingGroups["donor"]			= new Array(
				 'A.C. Chapter'
				,'Primary Sector Sub-Sector'
				,'Primary Sector Sub-Sub-Sector'
				,'Implementation Level'
				,'Accession Instrument'
				,'Executing Agency'
				,'Donor'
				,'Donor Group'
				,'Donor Agency'
				,'Primary Sector'
				,'Status', 'Region'
				,'Type Of Assistance'
				,'Financing Instrument'
				,'On/Off Budget'
				,'National Planning Objectives'
				,'National Planning Objectives Level 1'
				,'National Planning Objectives Level 2'
				,'National Planning Objectives Level 3'
				,'National Planning Objectives Level 4'
				,'National Planning Objectives Level 5'
				,'National Planning Objectives Level 6'
				,'National Planning Objectives Level 7'
				,'National Planning Objectives Level 8'
				,'Primary Program'
				,'Primary Program Level 1'
				,'Primary Program Level 2'
				,'Primary Program Level 3'
				,'Primary Program Level 4'
				,'Primary Program Level 5'
				,'Primary Program Level 6'
				,'Primary Program Level 7'
				,'Primary Program Level 8'
				,'Componente', 
				'Secondary Program'
				,'Secondary Program Level 1'
				,'Secondary Program Level 2'
				,'Secondary Program Level 3'
				,'Secondary Program Level 4'
				,'Secondary Program Level 5'
				,'Secondary Program Level 6'
				,'Secondary Program Level 7'
				,'Secondary Program Level 8'
				,'Donor Type'
				,'Credit/Donation'
				,'Beneficiary Agency'
				,'Implementing Agency'
				,'Component Type'
				,'Secondary Sector'
				,'Secondary Sector Sub-Sector'
				,'Responsible Organization'
				,'Activity Created By'
				,'Project Category'
				,'Funding Status'
			);
YAHOO.amp.reportwizard.fundingGroups["regional"]		= new Array(
				'Region','Status'
			);
YAHOO.amp.reportwizard.fundingGroups["component"]		= new Array(
				 'Component Type','Status'
			);
YAHOO.amp.reportwizard.fundingGroups["contribution"]	= new Array(
				'Costing Donor','Parent National Planning Objectives', 'National Planning Objectives', 'Primary Program', 
				'Secondary Program', 'Primary Sector Sub-Sector',
				'Status', 'Primary Sector', 'A.C. Chapter',
				'Accession Instrument', 'Donor', 'Secondary Sector', 'Secondary Sector Sub-Sector', 'Activity Created By'
			);

function insertColInfo (id, name) {
		YAHOO.amp.reportwizard.colIdToName[id]=name;
}

function checkIfColIsHierarchy(id) {
	var reportGroupDivEl		= document.getElementById("reportGroupDiv") ;
	var radios					= reportGroupDivEl.getElementsByTagName("input");
	var colName					= YAHOO.amp.reportwizard.colIdToName[id];
	if ( colName == null )
			return false;
	var fgArray;
	for (var i=0; i<radios.length; i++) {
		if ( radios[i].checked ) {
			fgArray	= YAHOO.amp.reportwizard.fundingGroups[ radios[i].value ];
		}
	}
	if (fgArray == null) return false;
	
	for (j=0; j<fgArray.length; j++) {
		if ( fgArray[j]==colName ) 
			return true;
	}
	return false;
}

function checkSelectedHierarchies() {
	var selHierEl		= document.getElementById("dest_hierarchies_ul") ;
	var selColEl		= document.getElementById("dest_col_ul") ;
	var cols			= selColEl.getElementsByTagName("li");
	var hiers			= selHierEl.getElementsByTagName("li");
	
	var srcHierEl		= document.getElementById("source_hierarchies_ul") ;
	var srcHiers		= srcHierEl.getElementsByTagName("li");
	
	for ( var i=0; i<hiers.length; i++ ) {
		var isInSelectedColumns = false;
		for ( var j=0; j<cols.length; j++ ) {
			if ( getColDbId(hiers[i]) == getColDbId(cols[j]) ) {
				isInSelectedColumns	= true;
				break;
			}
		}
		if ( !isInSelectedColumns ) {
			selHierEl.removeChild( hiers[i] );
			i=i-1;
		}
			
	}
	for ( var i=0; i<srcHiers.length; i++ ) {
		var isInSelectedColumns = false;
		for ( var j=0; j<cols.length; j++ ) {
			if ( getColDbId(srcHiers[i]) == getColDbId(cols[j]) ) {
				isInSelectedColumns	= true;
				break;
			}
		}
		if ( !isInSelectedColumns ) {
			srcHierEl.removeChild( srcHiers[i] );
			i=i-1;
		}
			
	}
	
}

function getColDbId( liEl ) {
	return (liEl.getElementsByTagName("input")[0]).value;
}

function generateHierarchies(e) {
	checkSelectedHierarchies();
	
	var newDDObj		= new MyDragAndDropObject();
	var ulEl			= document.getElementById("dest_col_ul") ;
	var hierUlEl		= document.getElementById("source_hierarchies_ul") ;
	var items			= ulEl.getElementsByTagName("li");
	for (var i=0; i<items.length; i++ ) {
		colId	= (items[i].getElementsByTagName("input")[0]).value;
		if ( checkIfColIsHierarchy(colId) && (document.getElementById('hier_li_'+colId)==null) ) {
				var childNodes				= items[i].childNodes;
				//var startHtml				= "<input type='checkbox' name='selectedColumns' id='hier_chk_"+colId+"' value='"+colId+"' />  ";
				//var newObj					= document.createElement("li");
				//newObj.innerHTML			= startHtml + childNodes[childNodes.length-1].data;
				//newObj.setAttribute('class','list1');
				//newObj.setAttribute('id', 'hier_li_'+colId);
				
				var newObj					= ColumnsDragAndDropObject.generateLi(
											"hier_li_"+colId, "list1", "hier_chk_"+colId, colId, "selectedHierarchies", childNodes[childNodes.length-1].data
											);
				hierUlEl.appendChild( newObj );
				var newDDProxy				= new YAHOO.util.DDProxy( newObj.id );
				newDDObj.addActions( newDDProxy );
				
		}
	}
	
}

