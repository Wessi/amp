
function scrollableTable(tableId,height){
this.debug=false;
this.usePercentage=false;
this.table=document.getElementById(tableId);
this.theader=null;
this.lastHeaderCell=null;
this.tbody=null;
this.maxRowDepth=-1;
this.headerValues=new Array();
this.useFixForDisplayNoneRows=false;


	this.scroll=function(){
		
		var isIE=navigator.appName.indexOf("Microsoft")!=-1;
		try{
			this.table.style.visibility="hidden";
			//find header rows
			for (i=0;i< this.table.childNodes.length;i++){
				var node=this.table.childNodes[i];
				if (node.nodeName=="THEAD"){
					this.theader=node;
					break;
				}
			}
	
	
			
			
			for (i=0;i< this.table.childNodes.length;i++){
				var node=this.table.childNodes[i];
				if (node.nodeName=="TBODY"){
					this.tbody=node;
					break;
				}
			}
			
			
				//check scroll % or px
			var scrollSize=(this.usePercentage)?(16*100)/this.table.offsetWidth:16;
			//set the body cells width =offsetWidth  set the last one =offsetWidth-scrollSize
		
			//get the body rows
			this.maxRowDepth=(this.maxRowDepth==-1)?this.tbody.rows.length:this.maxRowDepth;
			
			
			 this.setHeaderWidth=function(){
			//set header width = offsetwidth
				for (i=0;i < this.theader.rows.length;i++){
						var padding=0;
						var border=0;
						var perBorder=0;
						var perPadding=0;
						
						for (j=0;j < this.theader.rows[i].cells.length  ;j++){
							if(!isIE){
								var str=this.theader.rows[i].cells[j].style.paddingLeft.substr(0,this.theader.rows[i].cells[j].style.paddingLeft.length-2);
								if (str==""){str="0"};
								
								var padding=parseInt(str);
								str=this.theader.rows[i].cells[j].style.paddingRight.substr(0,this.theader.rows[i].cells[j].style.paddingRight.length-2);
								if (str==""){str="0"};
								 padding+=parseInt(str);
								 var perPadding=(padding*100)/this.table.offsetWidth;
							
								
								str=this.theader.rows[i].cells[j].style.borderRightWidth.substr(0,this.theader.rows[i].cells[j].style.borderRightWidth.length-2);
								if (str==""){str="0"};	
								var border=parseInt(str);
							
								str=this.theader.rows[i].cells[j].style.borderLeftWidth.substr(0,this.theader.rows[i].cells[j].style.borderLeftWidth.length-2);
								if (str==""){str="0"};	
								 border+=parseInt(str);
								 var perBorder=(border*100)/this.table.offsetWidth;
							} 
							 				
							var perValue=((this.theader.rows[i].cells[j].offsetWidth*100)/this.table.offsetWidth) -perPadding -perBorder;
							var pxValue=this.theader.rows[i].cells[j].offsetWidth-padding-border;
							
							this.theader.rows[i].cells[j].width=(this.usePercentage)?(perValue+"%"):pxValue;
							if (this.debug){
								this.theader.rows[i].cells[j].innerHTML=this.theader.rows[i].cells[j].width
							}
						}
					}
			}	
				
			//this function should be used only in the case that the rows are using display = none
			 this.setBodyForIE=function(){
						var nclone=this.tbody.rows[3].cloneNode(true);//the clone of the first  full row
						nclone.style.display='';
						
						nclone.setAttribute("id","ignoreToggle");
						this.tbody.appendChild(nclone);//appends the clone
					
					for (j=0;j <nclone.cells.length ;j++){
						
						var perValue=((nclone.cells[j].offsetWidth*100)/this.table.offsetWidth);
						var pxValue=nclone.cells[j].offsetWidth;
						//nclone.cells[j].innerHTML="";
						if (j==nclone.cells.length -1){
									nclone.cells[j].width=(this.usePercentage)?((perValue-scrollSize)+"%"):pxValue-scrollSize;
									nclone.cells[j].innerHTML="";
								if (this.debug){
									nclone.cells[j].innerHTML=nclone.cells[j].width;
								}
							}else{//no last row
									nclone.cells[j].width=(this.usePercentage)?(perValue+"%"):pxValue;
									nclone.cells[j].innerHTML="";
								if (this.debug){
									nclone.cells[j].innerHTML=nclone.cells[j].width;
								}
							}				
							
					}//end for cells
			 }//end function
			
			//this is the normal setbodyWidthFUnction
			 this.setBodyWidth=function(){
				for (i=0;i < this.maxRowDepth;i++){
						//set cells widths
						for (j=0;j < this.tbody.rows[i].cells.length ;j++){
							var padding=0;
							var border=0;
							var perBorder=0;
							var perPadding=0;
							if(!isIE){
								//calculate padding
								var str=this.tbody.rows[i].cells[j].style.paddingLeft.substr(0,this.tbody.rows[i].cells[j].style.paddingLeft.length-2);
								if (str==""){str="0"};
								var padding=parseInt(str);
								str=this.tbody.rows[i].cells[j].style.paddingRight.substr(0,this.tbody.rows[i].cells[j].style.paddingRight.length-2);
								if (str==""){str="0"};
								padding+=parseInt(str);
								var perPadding=(padding*100)/this.table.offsetWidth;
							
								//calculate border
								str=this.tbody.rows[i].cells[j].style.borderRightWidth.substr(0,this.tbody.rows[i].cells[j].style.borderRightWidth.length-2);
								if (str==""){str="0"};	
								var border=parseInt(str);
								str=this.tbody.rows[i].cells[j].style.borderLeftWidth.substr(0,this.tbody.rows[i].cells[j].style.borderLeftWidth.length-2);
								if (str==""){str="0"};	
								border+=parseInt(str);
								var perBorder=(border*100)/this.table.offsetWidth;
							}
								
							var perValue=((this.tbody.rows[i].cells[j].offsetWidth*100)/this.table.offsetWidth) - perPadding -perBorder;
							var pxValue=this.tbody.rows[i].cells[j].offsetWidth - padding -border;
							
							//last row
							if (j==this.tbody.rows[i].cells.length -1){
								if(pxValue >0){
									this.tbody.rows[i].cells[j].width=(this.usePercentage)?((perValue-scrollSize)+"%"):pxValue-scrollSize;
								}
								if (this.debug){
									this.tbody.rows[i].cells[j].innerHTML="last:"+this.tbody.rows[i].cells[j].width;
								}
							}else{//no last row
								if(pxValue >0){
									this.tbody.rows[i].cells[j].width=(this.usePercentage)?(perValue+"%"):pxValue;
								}
								if (this.debug){
									this.tbody.rows[i].cells[j].innerHTML=this.tbody.rows[i].cells[j].innerHTML+"-"+this.tbody.rows[i].cells[j].width;
								}
							}				
						
							this.tbody.rows[i].cells[j].style.overflow="hidden";
						
						}		
					//end for cells	
			}	
				///end for rows
			}		
		//end function	
		
			
		
		
			
		if(this.useFixForDisplayNoneRows && isIE){
			this.setBodyForIE();
		}
		this.setHeaderWidth();
		this.setBodyWidth();
		
		
		//remove the table header 
		this.table.removeChild(this.theader);
		
		//create the body table container DIV
		var divContent=document.createElement("div");
		
			//set container properties
		if (this.debug)
			divContent.style.border="1px solid red";
		divContent.style.overflow="scroll";
		divContent.style.overflowX="hidden";
		divContent.style.height=height+"px";
		divContent.style.marginBottom="3px";
		
		//insert the div  before the original table
		this.table.parentNode.insertBefore(divContent,this.table)
		
		//remove the original table
		this.table.parentNode.removeChild(this.table);
		
		//append the original table to the content div
		divContent.appendChild(this.table);
		
		//create a new table for teh header
		var newTable=document.createElement("table");
		 newTable.setAttribute("cellSpacing",this.table.getAttribute("cellSpacing")); 
		 newTable.setAttribute("cellPadding",this.table.getAttribute("cellPadding"));
		 newTable.setAttribute("width",this.table.getAttribute("width"));
		 newTable.setAttribute("class",this.table.getAttribute("class"));
		 newTable.appendChild(this.theader);
		 divContent.parentNode.insertBefore(newTable,divContent);
		 this.table.style.visibility="visible";
		}catch(e){
		//	alert(e);
		}
	}
	//end scroll function

}//end scrollable table
