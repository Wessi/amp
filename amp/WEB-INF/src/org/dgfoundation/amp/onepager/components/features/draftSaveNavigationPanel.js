var saveAsDraftPanel;
function showDraftPanel(){
	saveAsDraftPanel = new YAHOOAmp.widget.Panel("saveAsDraftPanel", {
		width : "350px",
		fixedcenter : true,
		constraintoviewport : true,
		underlay : "shadow",
		close : true,
		visible : true,
		modal : true,
		draggable : false
	});
	$("#saveAsDraftPanel").show();
	saveAsDraftPanel.render();
	 return false;
}
function hideDraftPanel(){
	if(saveAsDraftPanel!=null){
		saveAsDraftPanel.hide();
	}
}
 