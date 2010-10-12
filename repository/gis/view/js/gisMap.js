	
	
	var canvasWidth = 500;
	var canvasHeight = 500;
	
	var canvasContainerWidth = 500;
	var canvasContainerHeight = 500;
	
	var navigationWidth = 300;
	var navigationHeight = 300;
	
	
	
	var navCursorWidth = 1;
	var navCursorHeight = 1;
	
	var currentZoomRatio = 1;
	var currentZoomObj = document.getElementById("mapZoom10");
	
	var mouseX, mouseY;
	var evt;
	var xmlhttp = null;
	
	
	var imageMapLoaded = false;
	
	var fundingDataByRegion = new Array();
	var indicatorDataByRegion = new Array();
	var selIndicatorUnit = "N/A";

	var totalCommitmentFund = "0";
	var totalDisbursementFund = "0";
	var totalExpenditureFund = "0";

	var selSector = 0;
	
	var getIndValuesAction = false;
	
	
	
	//Action progress flag
	var actionImgLoading = false;
	var actionGetImageMap = false;
	var actionSectorData = false;
	var actionGetIndicators = false;
	var actionGetIndicatorValues = false;
	var actionGetSubgroups = false;
	var actionGetYears = false;
  var actionGetDonors = false;
  var actionChangeFundType = false;
  var getIndValuesAction = false;
  var getFundDataValues = false;
	
	$(document).ready(function(){
		actionImgLoading = false;
		$().mousemove(function(e){
			$("#tooltipContainer").css({"left" : e.pageX + 2 + "px"});
			$("#tooltipContainer").css({"top" : e.pageY +2 + "px"});
		});
		
		jQuery.fn.getRadioValue = function() {
			var mapLevel = $("input[name='mapLevelRadio']:checked").val();
			return mapLevel;
		}
		
		jQuery.fn.getImageMap = function() {
			var mapLevel = jQuery.fn.getRadioValue();
      if (mapLevel == null) mapLevel = 2;
      		var indYear = $("#indicatorYearCombo").val();
			var requestURL = "../../gis/getFoundingDetails.do?action=getImageMap&mapCode=TZA&mapLevel=" + mapLevel + "&indYear=" + indYear + "&width=" + canvasWidth + "&height=" + canvasHeight;
			if (!imageMapLoaded) {
				actionGetImageMap = true;
				if (mapLevel == null) {
					mapLevel = 2;
				}
				$.get(requestURL, addImageMap, "xml");
				imageMapLoaded = false;
			}
		}
		
		jQuery.fn.chekIndicatorValues = function() {
			if (getIndValuesAction) {
				getIndValuesAction = false;
				window.setTimeout(jQuery.fn.geIndicatorsValues, 0);
			}
		}
		
		jQuery.fn.chekFundDataValues = function() {
			if (getFundDataValues) {
				getFundDataValues = false;
				window.setTimeout(jQuery.fn.getDataForSectorFin, 0);
			}
		}
		
		jQuery.fn.geIndicatorsValues = function() {
			var mapLevel = jQuery.fn.getRadioValue();
			var indYear = $("#indicatorYearCombo").val();
		if (mapLevel == null) {
				mapLevel = 2;
			}
			var requestURL = "../../gis/getFoundingDetails.do?action=getIndicatorValues&mapCode=TZA&mapLevel=" + mapLevel + "&indYear=" + indYear;
			
			$.get(requestURL, jQuery.fn.indicatorsValuesReady, "xml");
			actionGetIndicatorValues = true;
		}
		
		jQuery.fn.indicatorsValuesReady = function(data, textStatus) {
			
			actionGetIndicatorValues = false;
			selIndicatorUnit  = data.getElementsByTagName('indicatorData')[0].attributes.getNamedItem("indUnit").value;
			
			if (selIndicatorUnit == null) {
				selIndicatorUnit = "N/A";
			}
			
			var regionIndicatorList = data.getElementsByTagName('indVal');
			indicatorDataByRegion = new Array();
			var regIndex = 0;
			
			
			
			for (regIndex = 0; regIndex < regionIndicatorList.length; regIndex ++) {
				var indData = regionIndicatorList[regIndex];
				var regionIndicatorMap = new Array();
				regionIndicatorMap[0] = indData.attributes.getNamedItem("reg").value;
				regionIndicatorMap[1] = indData.attributes.getNamedItem("val").value;
				regionIndicatorMap[2] = indData.attributes.getNamedItem("src").value;

				indicatorDataByRegion[indicatorDataByRegion.length] = regionIndicatorMap;
			}
	}
		
		
		
		
		jQuery.fn.mapLevelChange = function(newVal) {
			setBusy(true);
			var sect = null;
			if (showDevinfo) {
				sect = $("#sectorsMapCombo").val();
			} else {
				sect = $("#sectorsMapComboFin").val();
			}
			
			jQuery.fn.initIndicatorCombo();
			jQuery.fn.initSubgroupCombo();
			jQuery.fn.initYearCombo();
			actionImgLoading = true;
			imageMapLoaded = false;
			
			if (showDevinfo) {
				jQuery.fn.sectorSelected(sect);
			} else {
				jQuery.fn.sectorSelectedFin(sect);
			}
			var navCursorMapUrl = $("#navCursorMap").attr('src'); 
			var newURL =  jQuery.fn.modifyMapLevelURL (navCursorMapUrl, newVal);
			
			$("#navCursorMap").attr({src: newURL});
			
			if (newVal==2) {
				$("#reg_district_caption").html("Region");
				$("#reg_district_caption_for").html("For this region");
			} else if (newVal==3) {
				$("#reg_district_caption").html("District");
				$("#reg_district_caption_for").html("For this district");
			}
			
			jQuery.fn.getImageMap();
		}
				
		jQuery.fn.initIndicatorCombo = function(){
			var noneOpt = $("<option value='-1'>"+selectIndicatorTxt+"</option>");
			$("#indicatorsCombo").html("");
			$("#indicatorsCombo").append(noneOpt);
		}
		jQuery.fn.initSubgroupCombo = function(){
		    var noneOpt = $("<option value='-1'>"+selectSubgroupTxt+"</option>");
		    $("#indicatorSubgroupCombo").html("");
		    $("#indicatorSubgroupCombo").append(noneOpt);
		}
		jQuery.fn.initYearCombo = function() {
			var noneOpt = $("<option value='-1'>"+selectYearTxt+"</option>");
			$("#indicatorYearCombo").html("");
			$("#indicatorYearCombo").append(noneOpt);
		}
		jQuery.fn.initIndicatorValues = function() {
			indicatorDataByRegion = new Array();
			selIndicatorUnit = "N/A";
			
		}
		jQuery.fn.sectorSelected = function(sect){
			var selSector = sect;
			var mapLevel = jQuery.fn.getRadioValue();
			var fromYear = document.getElementsByName('selectedFromYear')[0].value;
			var toYear = document.getElementsByName('selectedToYear')[0].value;
			var indYear = $("#indicatorYearCombo").val();
			var donorId = document.getElementById('donorsCombo').value;
			var uniqueStr = (new Date()).getTime();
			jQuery.fn.initIndicatorCombo();
			jQuery.fn.initSubgroupCombo();
			jQuery.fn.initYearCombo();
			if (mapLevel == null) {
				mapLevel = 2;
			}
			//setBusy(true);
			var newUrl = "../../gis/getFoundingDetails.do?action=getDataForIndicator&mapCode=TZA&mapLevel=" + mapLevel + "&fromYear=" + fromYear + "&donorId=" + donorId + "&toYear=" + toYear + "&indYear=" + indYear + "&sectorId=" + sect + "&indicatorId=-1" + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight;
			
			$("#testMap").attr({src: newUrl});
			if (sect > 0) {
				jQuery.fn.getDataForSector(sect);
			} else {
				fundingDataByRegion = new Array();
			}
		}
		
        jQuery.fn.sectorSelectedFin = function(sect){
            var selSector = sect;
            var mapLevel = jQuery.fn.getRadioValue();
            var fromYear = document.getElementsByName('selectedFromYear')[0].value;
            var toYear = document.getElementsByName('selectedToYear')[0].value;
            var fundingType = document.getElementById('fundingType').value;
            var donorId = document.getElementById('donorsCombo').value;
            var uniqueStr = (new Date()).getTime();
            if (mapLevel == null) {
                mapLevel = 2;
            }
            //setBusy(true);
            var newUrl = "../../gis/getFoundingDetails.do?action=getDataForSectorFin&mapCode=TZA&mapLevel=" + mapLevel + "&donorId=" + donorId + "&fromYear=" + fromYear + "&toYear=" + toYear + "&sectorId=" + sect + "&fundingType=" + fundingType + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight;

            $("#testMap").attr({src: newUrl});
            if (sect > -2) {
            	getFundDataValues = true;
            	actionImgLoading = true;
            	setBusy(true);
//                jQuery.fn.getDataForSectorFin(sect);
            } else {
                fundingDataByRegion = new Array();
            }
            
//            window.setTimeout(jQuery.fn.getSectorDonors, 0);
        }
        
        jQuery.fn.donorSelectedFin = function(donorId){
            var selSector = $("#sectorsMapComboFin").val();
            var mapLevel = jQuery.fn.getRadioValue();
            var fromYear = document.getElementsByName('selectedFromYear')[0].value;
            var toYear = document.getElementsByName('selectedToYear')[0].value;
            var fundingType = document.getElementById('fundingType').value;
            
            var uniqueStr = (new Date()).getTime();
            jQuery.fn.initIndicatorCombo();
            if (mapLevel == null) {
                mapLevel = 2;
            }
            //setBusy(true);
            var newUrl = "../../gis/getFoundingDetails.do?action=getDataForSectorFin&mapCode=TZA&mapLevel=" + mapLevel + "&fromYear=" + fromYear + "&toYear=" + toYear + "&sectorId=" + selSector + "&donorId=" + donorId + "&fundingType=" + fundingType + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight;
            $("#testMap").attr({src: newUrl});
            getFundDataValues = true;
            actionImgLoading = true;
            setBusy(true);
//            jQuery.fn.getDataForSectorFin(selSector);
        }
        
        jQuery.fn.fundTypeSelectedFin = function(fundingType){
            var selSector = $("#sectorsMapComboFin").val();
            
            if (selSector > 0) {
                var mapLevel = jQuery.fn.getRadioValue();
                var fromYear = document.getElementsByName('selectedFromYear')[0].value;
                var toYear = document.getElementsByName('selectedToYear')[0].value;
                var donorId = document.getElementById('donorsCombo').value;
                
                var uniqueStr = (new Date()).getTime();
                jQuery.fn.initIndicatorCombo();
                if (mapLevel == null) {
                    mapLevel = 2;
                }
                //setBusy(true);
                var newUrl = "../../gis/getFoundingDetails.do?action=getDataForSectorFin&mapCode=TZA&mapLevel=" + mapLevel + "&fromYear=" + fromYear + "&toYear=" + toYear + "&sectorId=" + selSector + "&donorId=" + donorId + "&fundingType=" + fundingType + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight;
                
                $("#testMap").attr({src: newUrl});
                //jQuery.fn.getDataForSectorFin(selSector);
                            getFundDataValues = true;
		            actionImgLoading = true;
		            setBusy(true);
            }
            
        }
        
        jQuery.fn.getDataForSectorFin = function() {
        	
        		var sect = $("#sectorsMapComboFin").val();
            var mapLevel = jQuery.fn.getRadioValue();
            if (mapLevel == null) {
                mapLevel = 2;
            }
            var fromYear = document.getElementsByName('selectedFromYear')[0].value;
            var toYear = document.getElementsByName('selectedToYear')[0].value;
            var uniqueStr = (new Date()).getTime();
            var donorId = document.getElementById('donorsCombo').value;
            
            var requestURL = "../../gis/getFoundingDetails.do?action=getFinansialDataXML&mapCode=TZA&mapLevel=" + mapLevel + "&fromYear=" + fromYear + "&toYear=" + toYear + "&sectorId=" + sect + "&donorId=" + donorId + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight;
            
            $.get(requestURL, jQuery.fn.dataForSectorReadyFin, "xml");
            actionSectorData = true;
        }
        
        jQuery.fn.dataForSectorReadyFin = function(data, textStatus) {
                actionSectorData = false;
                if (data.getElementsByTagName('funding')[0].attributes.getNamedItem("totalCommitment")){
                	totalCommitmentFund = data.getElementsByTagName('funding')[0].attributes.getNamedItem("totalCommitment").value;
                }
                if (data.getElementsByTagName('funding')[0].attributes.getNamedItem("totalDisbursement")){
                	totalDisbursementFund = data.getElementsByTagName('funding')[0].attributes.getNamedItem("totalDisbursement").value;
                }
                if (data.getElementsByTagName('funding')[0].attributes.getNamedItem("totalExpenditure")){
                	totalExpenditureFund = data.getElementsByTagName('funding')[0].attributes.getNamedItem("totalExpenditure").value;
                }
                
                
                var regionDataList = data.getElementsByTagName('region');
                fundingDataByRegion = new Array();
                var regIndex = 0;
                for (regIndex = 0; regIndex < regionDataList.length; regIndex ++) {
                    var regData = regionDataList[regIndex];
                    var regionDataMap = new Array();
                    regionDataMap[0] = regData.attributes.getNamedItem("reg-code").value;
                    regionDataMap[1] = regData.attributes.getNamedItem("fundingCommitment").value;
                    regionDataMap[2] = regData.attributes.getNamedItem("fundingDisbursement").value;
                    regionDataMap[3] = regData.attributes.getNamedItem("fundingExpenditure").value;

                    fundingDataByRegion[fundingDataByRegion.length] = regionDataMap;
                }
        }
        
        jQuery.fn.getSectorDonors = function() {
            var mapLevel = jQuery.fn.getRadioValue()
            if (mapLevel == null) {
                mapLevel = 2;
            }
            var fromYear = document.getElementsByName('selectedFromYear')[0].value;
            var toYear = document.getElementsByName('selectedToYear')[0].value;
            var selSector = $("#sectorsMapComboFin").val();
            var uniqueStr = (new Date()).getTime();
            
            var requestURL = "../../gis/getFoundingDetails.do?action=getSectorDonorsXML&mapCode=TZA&mapLevel=" + mapLevel + "&fromYear=" + fromYear + "&toYear=" + toYear + "&sectorId=" + selSector + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight;
            $.get(requestURL, jQuery.fn.sectorDonorsReady, "xml");
        }
        
        jQuery.fn.sectorDonorsReady = function(data, textStatus) {
                actionGetIndicators = false;
                var donors = data.getElementsByTagName('donor');
                var indIndex = 0;
                var donorCmb = $("#donorsCombo");
                donorCmb.html("");
                var noneOpt = $("<option value='-1' selected='true'>All</option>");
                donorCmb.append(noneOpt);
                
                for (index = 0; index < donors.length; index ++) {
                    var donorData = donors[index]; 
                    var valueId = donorData.attributes.getNamedItem("id").value;
                    var valueName  = donorData.attributes.getNamedItem("name").value;
                    var donorOption = $("<option value='"+valueId+"'>"+valueName+"</option>");
                    donorCmb.append(donorOption);
                }
                
                donorCmb[0].selectedIndex = 0;
                
        }
        
        
        
        
		jQuery.fn.getDataForSector = function(sect) {
			var mapLevel = jQuery.fn.getRadioValue();
			if (mapLevel == null) {
				mapLevel = 2;
			}
			var indYear = $("#indicatorYearCombo").val();
			var fromYear = document.getElementsByName('selectedFromYear')[0].value;
			var toYear = document.getElementsByName('selectedToYear')[0].value;
			var uniqueStr = (new Date()).getTime();

			var requestURL = "../../gis/getFoundingDetails.do?action=getSectorDataXML&mapCode=TZA&mapLevel=" + mapLevel + "&fromYear=" + fromYear + "&toYear=" + toYear + "&indYear=" + indYear + "&sectorId=" + sect + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight;
			
			$.get(requestURL, jQuery.fn.dataForSectorReady, "xml");
			actionSectorData = true;
	    }
		
		jQuery.fn.dataForSectorReady = function(data, textStatus) {
				actionSectorData = false;
				if (data.getElementsByTagName('funding')[0].attributes.getNamedItem("totalCommitment").value){
					totalCommitmentFund = data.getElementsByTagName('funding')[0].attributes.getNamedItem("totalCommitment").value;
				}
				if(data.getElementsByTagName('funding')[0].attributes.getNamedItem("totalDisbursement").value){
					totalDisbursementFund = data.getElementsByTagName('funding')[0].attributes.getNamedItem("totalDisbursement").value;
				}
				if(data.getElementsByTagName('funding')[0].attributes.getNamedItem("totalExpenditure").value){
					totalExpenditureFund = data.getElementsByTagName('funding')[0].attributes.getNamedItem("totalExpenditure").value;
				}
				
				
				var regionDataList = data.getElementsByTagName('region');
				fundingDataByRegion = new Array();
				var regIndex = 0;
				for (regIndex = 0; regIndex < regionDataList.length; regIndex ++) {
					var regData = regionDataList[regIndex];
					var regionDataMap = new Array();
					regionDataMap[0] = regData.attributes.getNamedItem("reg-code").value;
					regionDataMap[1] = regData.attributes.getNamedItem("fundingCommitment").value;
					regionDataMap[2] = regData.attributes.getNamedItem("fundingDisbursement").value;
					regionDataMap[3] = regData.attributes.getNamedItem("fundingExpenditure").value;

					fundingDataByRegion[fundingDataByRegion.length] = regionDataMap;
				}
				jQuery.fn.initIndicatorValues();
				actionGetIndicators = true;
				window.setTimeout(jQuery.fn.getSectorIndicators, 0);
			
		}
		
		jQuery.fn.getSectorIndicators = function() {
			var mapLevel = jQuery.fn.getRadioValue()
			if (mapLevel == null) {
				mapLevel = 2;
			}
			var indYear = $("#indicatorYearCombo").val();
			var selSector = $("#sectorsMapCombo").val();
			var uniqueStr = (new Date()).getTime();
			
			var requestURL = "../../gis/getFoundingDetails.do?action=getIndicatorNamesXML&mapCode=TZA&mapLevel=" + mapLevel + "&indYear=" + indYear + "&sectorId=" + selSector;
			$.get(requestURL, jQuery.fn.sectorIndicatorsReady, "xml");
			
			actionGetIndicators = true;
	}
		
	    jQuery.fn.sectorIndicatorsReady = function(data, textStatus) {
				actionGetIndicators = false;
				var indicators = data.getElementsByTagName('indicator');
				var indIndex = 0;
				var selectCmb = $("#indicatorsCombo");
				selectCmb.html("");
				var noneOpt = $("<option value='-1'>"+selectIndicatorTxt+"</option>");
				selectCmb.append(noneOpt);
				
				for (indIndex = 0; indIndex < indicators.length; indIndex ++) {
					var indicatorData = indicators[indIndex]; 
					var valueId = indicatorData.attributes.getNamedItem("id").value;
					var valueName  = indicatorData.attributes.getNamedItem("name").value;
					var indicatorOption = $("<option value='"+valueId+"'>"+valueName+"</option>");
					selectCmb.append(indicatorOption);
					if (indicatorData.attributes.getNamedItem("enbl").value=="false") {
						indicatorOption.addClass("dsbl");
					} else {
						indicatorOption.addClass("enbl");
					}
				}
				selectCmb[0].selectedIndex = 0;
				jQuery.fn.initIndicatorValues();
		}
        
		jQuery.fn.indicatorSelected = function (ind) {
			
			jQuery.fn.initSubgroupCombo();
			jQuery.fn.initYearCombo();
		
			selIndicator = ind;
			var mapLevel = jQuery.fn.getRadioValue();
			if (mapLevel == null) {
				mapLevel = 2;
			}
			var sec = $("#sectorsMapCombo").val();
			var fromYear = document.getElementsByName('selectedFromYear')[0].value;
			var toYear = document.getElementsByName('selectedToYear')[0].value;
			
			
			var requestURL = "../../gis/getFoundingDetails.do?action=getAvailIndicatorSubgroups&mapCode=TZA&mapLevel=" + mapLevel + "&fromYear=" + fromYear + "&toYear=" + toYear + "&sectorId=" + sec + "&indicatorId=" + selIndicator + "&width=" + canvasWidth + "&height=" + canvasHeight;
			$.get(requestURL, jQuery.fn.availSubgroupsReady, "xml");
			actionGetSubgroups = true;
			
			actionImgLoading = true;
			document.getElementById("testMap").src = "../../gis/getFoundingDetails.do?action=getDataForIndicator&mapCode=TZA&mapLevel=" + mapLevel + "&fromYear=" + fromYear + "&toYear=" + toYear + "&sectorId=" + sec + "&indicatorId=-1&width=" + canvasWidth + "&height=" + canvasHeight;
			jQuery.fn.initIndicatorValues();
		}
		
		jQuery.fn.availSubgroupsReady = function(data, textStatus) {
				actionGetSubgroups = false;
				actionImgLoading = false;
				var availSubgroupList = data.getElementsByTagName('subgroup');
				var subgroupIndex = 0;
				
				var selectCmb = $("#indicatorSubgroupCombo");
				selectCmb.html("");
				var noneOpt = $("<option value='-1'>Select subgroup</option>");
				selectCmb.append(noneOpt);
				
				
				for (subgroupIndex = 0; subgroupIndex < availSubgroupList.length; subgroupIndex ++) {
					var subgroupData = availSubgroupList[subgroupIndex];
					var subgroupId = subgroupData.attributes.getNamedItem("id").value;
					var subgroupText = subgroupData.attributes.getNamedItem("name").value;
					var opt = $("<option value='"+subgroupId+"'>"+subgroupText+"</option>");
					selectCmb.append(opt);				
				}
				selectCmb[0].selectedIndex = 0;
			
		}
		
		jQuery.fn.subgroupSelected = function(sbgr) {
			
			jQuery.fn.initYearCombo();
		
			selSubgroup = sbgr;

			var mapLevel = $("input[name='mapLevelRadio']:checked").val();
			if (mapLevel == null) {
				mapLevel = 2;
			}
			var sec = $("#sectorsMapCombo").val();
			var selIndicator = $("#indicatorsCombo").val();
			var fromYear = document.getElementsByName('selectedFromYear')[0].value;
			var toYear = document.getElementsByName('selectedToYear')[0].value;

			
			var requestURL = "../../gis/getFoundingDetails.do?action=getAvailIndicatorYears&mapCode=TZA&mapLevel=" + mapLevel + "&fromYear=" + fromYear + "&toYear=" + toYear + "&subgroupId=" + selSubgroup + "&sectorId=" + sec + "&indicatorId=" + selIndicator + "&width=" + canvasWidth + "&height=" + canvasHeight;
			$.get(requestURL,  jQuery.fn.availYearsReady, "xml");
			
			actionGetYears = true;
			
			document.getElementById("testMap").src = "../../gis/getFoundingDetails.do?action=getDataForIndicator&mapCode=TZA&mapLevel=" + mapLevel + "&fromYear=" + fromYear + "&toYear=" + toYear + "&sectorId=" + sec + "&indicatorId=-1&width=" + canvasWidth + "&height=" + canvasHeight;
			jQuery.fn.initIndicatorValues();
			
		}
	    
		 jQuery.fn.availYearsReady = function(data, textStatus) {
				actionGetYears = false;
				var availYearList = data.getElementsByTagName('interval');
				var yearIndex = 0;
				
				var selectCmb = $("#indicatorYearCombo");
				selectCmb.html("");
				var noneOpt = $("<option value='-1'>Select year</option>");
				selectCmb.append(noneOpt);		
				
				for (yearIndex = 0; yearIndex < availYearList.length; yearIndex ++) {
					var yearData = availYearList[yearIndex];
					var yearId = yearData.attributes.getNamedItem("start-value").value + "-" + yearData.attributes.getNamedItem("end-value").value;
					var yearText = yearData.attributes.getNamedItem("start-caption").value + " - " + yearData.attributes.getNamedItem("end-caption").value;
					var opt = $("<option value='"+yearId+"'>"+yearText+"</option>");
					selectCmb.append(opt);			
				}
				selectCmb[0].selectedIndex = 0;
		}
		
		//Year functions
	jQuery.fn.yearSelected = function(year){
		//setBusy(true);
		var mapLevel = getRadioValue("mapLevelRadio");
		if (mapLevel == null) {
			mapLevel = 2;
		}
		var ind = document.getElementById("indicatorsCombo").value;
		var subgroupId = document.getElementById("indicatorSubgroupCombo").value;
		var fromYear = document.getElementsByName('selectedFromYear')[0].value;
		var toYear = document.getElementsByName('selectedToYear')[0].value;
		var sec = document.getElementById("sectorsMapCombo").value;
		
		var uniqueStr = (new Date()).getTime();
		
		actionImgLoading = true;
		getIndValuesAction = true;
		setBusy(true);
		document.getElementById("testMap").src = "../../gis/getFoundingDetails.do?action=getDataForIndicator&mapCode=TZA&mapLevel=" + mapLevel + "&fromYear=" + fromYear + "&toYear=" + toYear + "&subgroupId=" + subgroupId + "&indYear=" + year + "&sectorId=" + sec + "&indicatorId=" + ind + "&uniqueStr=" + uniqueStr + "&width=" + canvasWidth + "&height=" + canvasHeight;
		
	}

		 jQuery.fn.modifyMapLevelURL  = function(url, newLevel) {
				var retVal = null;
				
				var levelStartPos = url.indexOf ("mapLevel");
				var levelEndtPos = url.indexOf ("&", levelStartPos);
				if (levelEndtPos == -1) {
					levelEndtPos = url.length;
				}
					retVal = url.substring(0, levelStartPos) +
					"mapLevel=" + newLevel + url.substring(levelEndtPos, url.length);
					actionImgLoading =false;
					setBusy(false);
				return retVal;
			}
		 jQuery.fn.initNavCursorSize = function() {
				navCursorWidth = Math.round(navigationWidth * canvasContainerWidth / canvasWidth) - 14;
				navCursorHeight = Math.round(navigationHeight * canvasContainerHeight / canvasHeight) - 14;
				$("#navCursor").css({'width' : navCursorWidth + "px"});
				$("#navCursor").css({'height': navCursorHeight + "px"});
				
				navAreaLeft=0;
				navAreaTop=0;
				jQuery.fn.scrollMap(0, 0);
				$("#navCursor").css({'left' : 23 + "px"});
				$("#navCursor").css({'top' : 43 + "px"});
			}
			
		jQuery.fn.scrollMap = function(x, y){
				
				var deltaCanvasWidth = canvasWidth - canvasContainerWidth;
				var deltaCursorWidth = navigationWidth - 14 - navCursorWidth;
				
				var deltaCanvasHeight = canvasHeight - canvasContainerHeight;
				var deltaCursorHeight = navigationHeight - 14 - navCursorHeight;
				
				
				$("#mapCanvasContainer").scrollLeft(x * deltaCanvasWidth / deltaCursorWidth);
				$("#mapCanvasContainer").scrollTop(y * deltaCanvasHeight / deltaCursorHeight);
			}
		/*
		jQuery.fn.mapYearChanged = function (){
			jQuery.fn.sectorSelected(sec);
		}
		  */

		
		$("#testMap").getImageMap();
		$("#testMap").chekIndicatorValues();
		$("input[name='mapLevelRadio']").click(function(){
			var mapLevel = $("input[name='mapLevelRadio']:checked").val();
			jQuery.fn.mapLevelChange(mapLevel);
		});
		$("#sectorsMapCombo").change(function(){
			var sector = $("#sectorsMapCombo option:selected").val();
			jQuery.fn.sectorSelected(sector);
			actionImgLoading = false;
		});
        
        
        $("#sectorsMapComboFin").change(function(){
            var sector = $("#sectorsMapComboFin option:selected").val();
            jQuery.fn.sectorSelectedFin(sector);
            actionImgLoading = false;
        });
        
        $("#donorsCombo").change(function(){
            var donor = $("#donorsCombo option:selected").val();
            jQuery.fn.donorSelectedFin(donor);
            actionImgLoading = false;
        });
        
        $("#fundingType").change(function(){
            var fndType = $("#fundingType option:selected").val();
            jQuery.fn.fundTypeSelectedFin(fndType);
            actionImgLoading = false;
        });
        
    
    $("#testMap").load(function(){
			jQuery.fn.chekIndicatorValues();
			jQuery.fn.chekFundDataValues();
			actionImgLoading = false;
		});    
        
        
		$("#indicatorsCombo").change(function(){
			var inic = $("#indicatorsCombo option:selected").val();
			jQuery.fn.indicatorSelected(inic);
		});
		$("#indicatorSubgroupCombo").change(function() {
			var subgr = $("#indicatorSubgroupCombo option:selected").val();
			jQuery.fn.subgroupSelected(subgr);
		});
		$("#selectedFromYear").change(function(){
            
            if ($("#sectorsMapCombo")[0] != null) {
                jQuery.fn.sectorSelected($("#sectorsMapCombo option:selected").val());
            } else if ($("#sectorsMapComboFin")[0] != null) {
                jQuery.fn.sectorSelectedFin($("#sectorsMapComboFin option:selected").val());
            }
			actionImgLoading = false;
		});
		$("#selectedToYear").change(function(){
			if ($("#sectorsMapCombo") != null) {
                jQuery.fn.sectorSelected($("#sectorsMapCombo option:selected").val());
            } else if ($("#sectorsMapComboFin") != null) {
                jQuery.fn.sectorSelectedFin($("#sectorsMapComboFin option:selected").val());
            }
			actionImgLoading = false;
		});
		
		$("#indicatorYearCombo").change(function(){
			var year = $("#indicatorYearCombo option:selected").val();
			jQuery.fn.yearSelected(year);
		});
        
        
		
		$("#mapNav").toggle(
			function(){
				$("#mapNav").addClass("navVisible");
				$("#mapNav").removeClass("navHiden");
				$("#ctrlContainer").show();	
			},
			function(){
				$("#mapNav").addClass("navHiden");
				$("#mapNav").removeClass("navVisible")
				$("#ctrlContainer").hide();	
			}
			);
		
		$("#mapZoom10").click(function() {
			var mapSrc = $("#testMap").attr("src");
				canvasWidth = 500;
				canvasHeight = 500;
			
			var newUrl = modifyZoomedMapURL (mapSrc, canvasWidth, canvasHeight);
			
			jQuery.fn.initNavCursorSize();
			imageMapLoaded = false;
			$("#testMap").attr({'src': newUrl});
			jQuery.fn.getImageMap();
			$("#mapZoom15").removeClass("navVisible");
			$("#mapZoom15").addClass("navHiden");
			$("#mapZoom10").removeClass("navHiden");
			$("#mapZoom10").addClass("navVisible");
			$("#mapZoom20").removeClass("navVisible");
			$("#mapZoom20").addClass("navHiden");
			$("#mapZoom30").removeClass("navVisible");
			$("#mapZoom30").addClass("navHiden");
			actionImgLoading = false;
			
		});
		
		$("#mapZoom15").click(function() {
			var mapSrc = $("#testMap").attr("src");
				canvasWidth = 750;
				canvasHeight = 750;
			
			var newUrl = modifyZoomedMapURL (mapSrc, canvasWidth, canvasHeight);
			
			jQuery.fn.initNavCursorSize();
			imageMapLoaded = false;
			$("#testMap").attr({'src': newUrl});
			jQuery.fn.getImageMap();
			$("#mapZoom10").removeClass("navVisible");
			$("#mapZoom10").addClass("navHiden");
			$("#mapZoom15").removeClass("navHiden");
			$("#mapZoom15").addClass("navVisible");
			$("#mapZoom20").removeClass("navVisible");
			$("#mapZoom20").addClass("navHiden");
			$("#mapZoom30").removeClass("navVisible");
			$("#mapZoom30").addClass("navHiden");
			actionImgLoading = false;
			
			
		});
		
		$("#mapZoom20").click(function() {
			var mapSrc = $("#testMap").attr("src");
				canvasWidth = 1000;
				canvasHeight = 1000;
			
			var newUrl = modifyZoomedMapURL (mapSrc, canvasWidth, canvasHeight);
			
			jQuery.fn.initNavCursorSize();
			imageMapLoaded = false;
			$("#testMap").attr({'src': newUrl});
			jQuery.fn.getImageMap();
			$("#mapZoom10").removeClass("navVisible");
			$("#mapZoom10").addClass("navHiden");
			$("#mapZoom20").removeClass("navHiden");
			$("#mapZoom20").addClass("navVisible");
			$("#mapZoom15").removeClass("navVisible");
			$("#mapZoom15").addClass("navHiden");
			$("#mapZoom30").removeClass("navVisible");
			$("#mapZoom30").addClass("navHiden");
			actionImgLoading = false;
			
		});
		
		$("#mapZoom30").click(function() {
			var mapSrc = $("#testMap").attr("src");
				canvasWidth = 1500;
				canvasHeight = 1500;
			
			var newUrl = modifyZoomedMapURL (mapSrc, canvasWidth, canvasHeight);
			
			jQuery.fn.initNavCursorSize();
			imageMapLoaded = false;
			$("#testMap").attr({'src': newUrl});
			jQuery.fn.getImageMap();
			$("#mapZoom10").removeClass("navVisible");
			$("#mapZoom10").addClass("navHiden");
			$("#mapZoom30").removeClass("navHiden");
			$("#mapZoom30").addClass("navVisible");
			$("#mapZoom15").removeClass("navVisible");
			$("#mapZoom15").addClass("navHiden");
			$("#mapZoom20").removeClass("navVisible");
			$("#mapZoom20").addClass("navHiden");
			actionImgLoading = false;
			
		});
		
	
		$("#busyIndicator").ajaxStart(function(){
			   setBusy(true);
			 });
			 
	   $("#busyIndicator").ajaxStop(function(){
			   setBusy(false);
		 });;
	});

	function addImageMap(data, textStatus) {
			
			imageMapLoaded = true;
			document.getElementById("imageMapContainer").innerHTML = null;
			document.getElementById("imageMapContainer").innerHTML = generateImageMap (data);
			
			document.getElementById("testMap").removeAttribute("useMap");
			document.getElementById("testMap").setAttribute("useMap", "#areaMap");
			actionGetImageMap = false;
			
			if (!showDevinfo) {
				sect = $("#sectorsMapComboFin").val();
				jQuery.fn.sectorSelectedFin(sect);
			}
			
	}
	
	
	function generateImageMap (XmlObj) {
		var retVal = "<map name=\"areaMap\">";
		var segments = XmlObj.getElementsByTagName('segment');
		var segmentIndex = 0;
		for (segmentIndex = 0; segmentIndex < segments.length; segmentIndex ++) {
			var segment = segments[segmentIndex];
			var shapeIndex = 0;
			for (shapeIndex = 0; shapeIndex < segment.childNodes.length; shapeIndex ++) {
				var shape = segment.childNodes[shapeIndex];
				retVal += "<area shape=\"polygon\" coords=\""
				var pointIndex = 0;
				for (pointIndex = 0; pointIndex < shape.childNodes.length; pointIndex ++) {
					var point = shape.childNodes[pointIndex];
					retVal += point.attributes.getNamedItem("x").value;
					retVal += ",";
					retVal += point.attributes.getNamedItem("y").value;
					if (pointIndex < shape.childNodes.length - 1) {
						retVal += ",";
					}
				}
				retVal += "\"";
				retVal += " onMouseOut=\"hideRegionTooltip()\"";
				retVal += " onMouseOver=\"showRegionTooltip('" + segment.attributes.getNamedItem("code").value + "','" + segment.attributes.getNamedItem("name").value + "')\"";
				retVal += " onClick=\"showRegionReport('" + segment.attributes.getNamedItem("code").value + "','" + segment.attributes.getNamedItem("name").value + "')\">";
			}
		}
		retVal += "</map>";
		return retVal;
	}
	
	function showRegionTooltip(regCode, regName) {
		if (regCode.indexOf("Lake")<0){
			var mouseEvent = null;
			document.getElementById("tooltipRegionContainer").innerHTML = regName;
            if (document.getElementById("tooltipDonorContainer") != null) {
                var selIdx = document.getElementById("donorsCombo").selectedIndex;
                var selected_text = document.getElementById("donorsCombo").options[selIdx].text;

                document.getElementById("tooltipDonorContainer").innerHTML = selected_text;
            }
        
            if(document.getElementById("tooltipTotalCommitmentContainer")){
            	document.getElementById("tooltipTotalCommitmentContainer").innerHTML = totalCommitmentFund;
            }
            if(document.getElementById("tooltipTotalDisbursementContainer")){
				document.getElementById("tooltipTotalDisbursementContainer").innerHTML = totalDisbursementFund;
            }
			if (document.getElementById("tooltipTotalExpenditureContainer")){
				document.getElementById("tooltipTotalExpenditureContainer").innerHTML = totalExpenditureFund;
			}
		
			var regData = getRegFounding(regCode);
		
			var note="";
			if (!validatedRegPercentage || !displayeRegPercentage) {
				note="<font color='red'> (*)</font>";
			}
		
			if(document.getElementById("tooltipCurrentCommitmentContainer")){
				document.getElementById("tooltipCurrentCommitmentContainer").innerHTML = regData[0] + note;
			}
			if (document.getElementById("tooltipCurrentDisbursementContainer")){
				document.getElementById("tooltipCurrentDisbursementContainer").innerHTML = regData[1] + note;
			}
			if(document.getElementById("tooltipCurrentExpenditureContainer")){
				document.getElementById("tooltipCurrentExpenditureContainer").innerHTML = regData[2] + note;
			}
			
			
            if (document.getElementById("tooltipIndUnit") != null) {
			    document.getElementById("tooltipIndUnit").innerHTML = selIndicatorUnit;
			    document.getElementById("tooltipIndVal").innerHTML = getRegIndicatorValue(regCode);
			    document.getElementById("tooltipIndSrc").innerHTML = getRegIndicatorSource(regCode);
            }
			
		
			
		
			document.getElementById("tooltipContainer").style.display = "block";
			
			//Set year caption
			var fromYear = getComboSelectedText(document.getElementsByName('selectedFromYear')[0]);
			var toYear = getComboSelectedText(document.getElementsByName('selectedToYear')[0]);
            var selectedCurrency=document.getElementById("selCurr").value;
			var newCapt = "("+selectedCurrency+") " + fromYear + " - " + toYear;
			document.getElementById('tooltipCurencyYearRange').innerHTML = newCapt;
			
		}
	}
    

	
	function hideRegionTooltip() {
		document.getElementById("tooltipContainer").style.display = "none";
	}
	
	function getRegFounding (regCode) {
		var retVal = new Array (0, 0, 0);
		var dataIndex = 0;
		for (dataIndex = 0; dataIndex < fundingDataByRegion.length; dataIndex ++) {
			var dataItem = fundingDataByRegion[dataIndex];
			if (dataItem[0] == regCode) {
				retVal = new Array (dataItem[1], dataItem[2], dataItem[3]);
				break;
			}
		}
		return retVal;
	}
	
	function getRegIndicatorValue (regCode) {
		var retVal = "N/A";
		var dataIndex = 0;
		for (dataIndex = 0; dataIndex < indicatorDataByRegion.length; dataIndex ++) {
			var dataItem = indicatorDataByRegion[dataIndex];
			if (dataItem[0] == regCode) {
				retVal = dataItem[1];
				break;
			}
		}
		return retVal;
	}
	
	function getRegIndicatorSource (regCode) {
		var retVal = "N/A";
		var dataIndex = 0;
		for (dataIndex = 0; dataIndex < indicatorDataByRegion.length; dataIndex ++) {
			var dataItem = indicatorDataByRegion[dataIndex];
			if (dataItem[0] == regCode) {
				retVal = dataItem[2];
				break;
			}
		}
		return retVal;
	}
	
	

	
	function modifyUniqueStringURL (url) {
		var uniqueStartPos = url.indexOf ("uniqueStr");
		var uniqueEndtPos = url.indexOf ("&", uniqueStartPos);
		if (uniqueEndtPos == -1) {
			uniqueEndtPos = url.length;
		}
			retVal = url.substring(0, uniqueStartPos) +
			"uniqueStr=" + (new Date()).getTime() + url.substring(uniqueEndtPos, url.length);
		
		
		
		return retVal;
	}
	
	
	//end of Map level functions
	
	
	
	function modifyYearURL (url, newYear) {
		var retVal = null;
		
		var yearStartPos = url.indexOf ("indYear");
		var yearEndtPos = url.indexOf ("&", yearStartPos);
		if (yearEndtPos == -1) {
			yearEndtPos = url.length;
		}
			retVal = url.substring(0, yearStartPos) +
			"indYear=" + newYear + url.substring(yearEndtPos, url.length);
		
		return retVal;
	}
	
	
	function mapYearChanged(){
        if (document.getElementById('sectorsMapCombo') != null) {
		    		jQuery.fn.sectorSelected(document.getElementById('sectorsMapCombo').value);
        } else if ((document.getElementById('sectorsMapComboFin') != null)) {
            jQuery.fn.sectorSelectedFin(document.getElementById('sectorsMapComboFin').value);
        }
	}
	
	function getComboSelectedText(obj) {
		var retVal = null;
		var optIndex = 0;
		for (optIndex = 0; optIndex < obj.options.length; optIndex++) {
			if (obj.options[optIndex].selected) {
				retVal = obj.options[optIndex].text;
				break;
			}
		}
		return retVal;
	}
	
	function modifyZoomedMapURL (url, newWidth, newHeight) {
		var retVal = null;
		
		var widthStartPos = url.indexOf ("width");
		var widthEndtPos = url.indexOf ("&", widthStartPos);
		if (widthEndtPos == -1) {
			widthEndtPos = url.length;
		}
		
		var heightStartPos = url.indexOf ("height");
		var heightEndPos = url.indexOf ("&", heightStartPos);
		if (heightEndPos == -1) {
			heightEndPos = url.length;
		}

		if (widthEndtPos < heightStartPos) { 
			retVal = url.substring(0, widthStartPos) +
			"width=" + newWidth + url.substring(widthEndtPos, heightStartPos) + 
			"height=" + newHeight;
		}
		
		return retVal;
	}
	
	function setBusy(busy) {
		    /*
			alert (actionImgLoading + " - " +
				   actionGetImageMap  + " - " +
				   actionSectorData  + " - " +
				   actionGetIndicators + " - " +
				   actionGetIndicatorValues + " - " +
				   actionGetSubgroups+ " - " +
				   actionGetYears + " - " +
                   actionGetDonors + " - " +
                   actionChangeFundType); 
                   
                   alert(busy);*/
			
		if (busy) {
			document.getElementById("busyIndicator").style.visibility = "visible";

			if (document.getElementsByName("mapLevelRadio")[0] != null) {
				document.getElementsByName("mapLevelRadio")[0].disabled = true;
				document.getElementsByName("mapLevelRadio")[1].disabled = true;
			}

            if (document.getElementById("sectorsMapCombo") != null) {
			    document.getElementById("sectorsMapCombo").disabled = true;
			    document.getElementById("indicatorsCombo").disabled = true;
			    document.getElementById("indicatorSubgroupCombo").disabled = true;
			    document.getElementById("indicatorYearCombo").disabled = true;
            }
            
            if (document.getElementById("sectorsMapComboFin") != null) {
                document.getElementById("sectorsMapComboFin").disabled = true;
                document.getElementById("fundingType").disabled = true;
                document.getElementById("donorsCombo").disabled = true;
            }
			
		} else if (!actionImgLoading &&
				   !actionGetImageMap &&
				   !actionSectorData &&
				   !actionGetIndicators &&
				   !actionGetIndicatorValues &&
				   !actionGetSubgroups &&
				   !actionGetYears &&
                   !actionGetDonors &&
                   !actionChangeFundType) {
                       
			document.getElementById("busyIndicator").style.visibility = "hidden";

			if (document.getElementsByName("mapLevelRadio")[0] != null) {
				document.getElementsByName("mapLevelRadio")[0].disabled = false;
				document.getElementsByName("mapLevelRadio")[1].disabled = false;
			}
            
            if (document.getElementById("sectorsMapCombo") != null) {
			    document.getElementById("sectorsMapCombo").disabled = false;
			    document.getElementById("indicatorsCombo").disabled = false;
			    document.getElementById("indicatorSubgroupCombo").disabled = false;
			    document.getElementById("indicatorYearCombo").disabled = false;
            }
            
            if (document.getElementById("sectorsMapComboFin") != null) {
                document.getElementById("sectorsMapComboFin").disabled = false;
                document.getElementById("fundingType").disabled = false;
                document.getElementById("donorsCombo").disabled = false;
            }
		}
		
	}
	
	function mapScroll(obj) {
		
	}

	var navAreaLeft=0;
	var navAreaTop=0;

	var isKeyPressed = false;
	var initialOffsetX, initialOffsetY;
	var initialClientOffsetX, initialClientOffsetY;

	function navCursorKeyDown (evt) {
		if (evt == null) {
			evt = window.event;
		} else {
			evt.stopPropagation();
			evt.preventDefault();
		}
		isKeyPressed = true;
		
		if (evt.offsetX != null) {
			initialOffsetX = evt.offsetX;
			initialOffsetY = evt.offsetY;
			
		} else {
			initialOffsetX = evt.layerX + 10;
			initialOffsetY = evt.layerY + 10;
			

		}
	}
	
	function navCursorKeyUp (evt) {
		isKeyPressed = false;
	}
	
	
	
	function navCursorOnMove (evt) {
		var containerPos = getDocumentOffsetX(document.getElementById("navMapContainer"));				
		var containerXPos = containerPos.left;
		var containerYPos = containerPos.top;
		

		if (evt == null) {
			evt = window.event;
		} else {
			evt.stopPropagation();
			evt.preventDefault();
		}

		
		if (isKeyPressed) {
			var pLeft = 0;
			var pTop = 0;

			if (evt.x != null) {
					pLeft = evt.clientX + document.body.scrollLeft - initialOffsetX - 20;// + document.getElementById("navMapContainer").style.left; 
					pTop = evt.clientY + document.body.scrollTop - initialOffsetY - 120;
			} else {
					pLeft = evt.pageX - initialOffsetX;// + document.getElementById("navMapContainer").style.left;
					pTop = evt.pageY - initialOffsetY - 100;
				evt.stopPropagation();
			}

			if (pLeft >= containerXPos - 7 && pLeft - containerXPos + 7 + navCursorWidth <= navigationWidth - 14) {
				navAreaLeft = pLeft - containerXPos + 7;
			} else if (pLeft < containerXPos - 7) {
				navAreaLeft = 0;
			} else {
				navAreaLeft = navigationWidth - navCursorWidth - 14;
			}
			
			if (pTop >= containerYPos - 109 && pTop - containerYPos + 109 + navCursorHeight <= navigationHeight - 14) {
				navAreaTop = pTop - containerYPos + 109;
			} else if (pTop < containerYPos - 109) {
				navAreaTop = 0;
			} else {
				navAreaTop = navigationHeight - navCursorHeight - 14;
			}

			document.getElementById("navCursor").style.left = navAreaLeft + 23 + "px";
			document.getElementById("navCursor").style.top = navAreaTop + 43 + "px";
			
			scrollMap (navAreaLeft , navAreaTop);
			
		}
	}
	
	function initNavCursorEvents() {
		var navCursorObj = document.getElementById("navCursor");
		navCursorObj.onmousedown = navCursorKeyDown;
		navCursorObj.onmouseup = navCursorKeyUp;
		navCursorObj.onmousemove = navCursorOnMove;
		navCursorObj.onmouseout = navCursorKeyUp;
		navCursorObj.onclick = navCursorKeyUp;
	}	
		
	function initNavCursorSize() {
		navCursorWidth = Math.round(navigationWidth * canvasContainerWidth / canvasWidth) - 14;
		navCursorHeight = Math.round(navigationHeight * canvasContainerHeight / canvasHeight) - 14;
		document.getElementById("navCursor").style.width = navCursorWidth + "px";
		document.getElementById("navCursor").style.height = navCursorHeight + "px";
		
		navAreaLeft=0;
		navAreaTop=0;
		scrollMap(0, 0);
		document.getElementById("navCursor").style.left = 23 + "px";
		document.getElementById("navCursor").style.top = 43 + "px";
	}
	
	function scrollMap(x, y){
		
		var deltaCanvasWidth = canvasWidth - canvasContainerWidth;
		var deltaCursorWidth = navigationWidth - 14 - navCursorWidth;
		
		var deltaCanvasHeight = canvasHeight - canvasContainerHeight;
		var deltaCursorHeight = navigationHeight - 14 - navCursorHeight;
		
		
		document.getElementById("mapCanvasContainer").scrollLeft = x * deltaCanvasWidth / deltaCursorWidth;
		document.getElementById("mapCanvasContainer").scrollTop = y * deltaCanvasHeight / deltaCursorHeight;
	}
	
	initNavCursorSize();
	initNavCursorEvents();
	
	
	function getRadioValue(radioName) {
		var retVal = null;
		var radioGroup = document.getElementsByName(radioName);
		
		if (radioGroup != null) {
			var iterIndex = 0;
			for (iterIndex = 0; iterIndex < radioGroup.length; iterIndex ++) {
				if (radioGroup[iterIndex].checked) {
					retVal = radioGroup[iterIndex].value;
					break;
				}
			}
		}
		return retVal;
	}
	
	//Region popup report
	function showRegionReport(regCode, regName) {
		var mapLevel = getRadioValue("mapLevelRadio");
		if (mapLevel == null) {
			mapLevel = 2;
		}
		var sec = document.getElementById("sectorsMapCombo") != null ? document.getElementById("sectorsMapCombo").value:document.getElementById("sectorsMapComboFin").value;
		var fromYear = document.getElementsByName('selectedFromYear')[0].value;
		var toYear = document.getElementsByName('selectedToYear')[0].value;
		var donorId = document.getElementById('donorsCombo').value;
	
		var regRepUrl = "/gis/ShowRegionReport.do?regCode=" + regCode + "&regName=" + regName + "&mapLevel=" + mapLevel + "&sectorId=" + sec + "&startYear=" + fromYear + "&endYear=" + toYear + "&donorid=" + donorId;
		//alert(regRepUrl);
		var popup = window.open(regRepUrl, null, "height=500,width=750,status=yes,resizable=yes,toolbar=no,menubar=no,location=no");
		
		popup.focus();
	}
	
	var logged = false

	function getDocumentOffsetX (domObject) {
		var pos = {top: 0, left: 0}

		var retX = 0;
		var retY = 0;
		
		while (domObject.offsetParent != null) {
			retX += domObject.offsetLeft;
			retY += domObject.offsetTop;
			
			domObject = domObject.offsetParent;
		}
		logged = true;
    pos.left = retX;
    pos.top = retY;
		return pos;
	}
