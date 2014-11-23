define([ 'business/grid/columnsMapping', 'business/translations/translationManager', 'util/tabUtils' ], function(columnsMapping,
		TranslationManager, TabUtils) {

	"use strict";

	var gridBaseName = 'tab_grid_';
	var gridPagerBaseName = 'tab_grid_pager_';

	// This variable will contain the mappings between different column names
	// (tab structure vs report data).
	var headers = [];

	function GridManager() {
		if (!(this instanceof GridManager)) {
			throw new TypeError("GridManager constructor cannot be called as a function.");
		}
	}

	function getURL(id) {
		return '/rest/data/report/' + id + '/result/jqGrid';
	}

	GridManager.prototype = {
		constructor : GridManager
	};

	/**
	 * Apply filters and refresh the grid.
	 */
	GridManager.filter = function(id, jsonFilters, settings) {
		var grid = jQuery("#" + gridBaseName + id);
		jQuery(grid).jqGrid('clearGridData');
		jQuery(grid).jqGrid('setGridParam', {
			url : getURL(id)
		});

		// Do this or new filters will be merged with old postData.
		jQuery(grid).jqGrid('setGridParam', {
			postData : null
		});

		var data = {
			page : 1,
			regenerate : true,
			filters : jsonFilters,
			settings : settings
		};
		jQuery(grid).jqGrid('setGridParam', {
			postData : data
		});
		jQuery(grid).jqGrid().trigger('reloadGrid', [ {
			page : 1
		} ]);

	};

	GridManager.populateGrid = function(id, dynamicLayoutView, firstContent) {
		var TableSectionView = Marionette.ItemView.extend({
			template : '#grid-template'
		});
		var tableSectionView = new TableSectionView();
		dynamicLayoutView.results.show(tableSectionView);

		// Define grid structure.
		var tableStructure = extractMetadata(firstContent);
		var grouping = (tableStructure.hierarchies.length > 0) ? true : false;
		var grid = jQuery("#tab_grid");
		jQuery(grid).attr("id", gridBaseName + id);
		var pager = jQuery("#tab_grid_pager");
		jQuery(pager).attr("id", gridPagerBaseName + id);

		// IMPORTANT NOTE: We need to call jqgrid this way because using the
		// standar require way will generate side effects like grouping buttons
		// stop working, the drawback is the client can not cache the jqgrid
		// library.
		jQuery.ajaxSetup({
			cache : true
		});
		jQuery.getScript("/TEMPLATE/ampTemplate/tabs/js/lib/one_place/jqgrid-all.js", function(data, textStatus, jqxhr) {
			var rowNum = 0;
			var colModel = columnsMapping.createJQGridColumnModel(tableStructure);
			var grandTotals = null;
			jQuery(grid).jqGrid(
					{
						caption : false,
						/* url : '/rest/data/report/' + id + '/result/', */
						url : getURL(id),
						datatype : 'json',
						mtype : 'POST',
						ajaxGridOptions : {
							contentType : 'application/json; charset=utf-8'
						},
						postData : {
							page : 1,
							regenerate : true,
							filters : null
						},
						serializeGridData : function(postData) {
							return JSON.stringify(postData);
						},
						jsonReader : {
							repeatitems : false,
							root : function(obj) {
								// rowNum = obj.recordsPerPage;
								jQuery(grid).jqGrid('setGridParam', {
									rowNum : obj.page.recordsPerPage
								});
								grandTotals = extractGrandTotals(obj, colModel);
								return transformData(obj, grouping, tableStructure.hierarchies);
							},
							page : function(obj) {
								return obj.page.currentPageNumber;
							},
							total : function(obj) {
								return obj.page.totalPageCount;
							},
							records : function(obj) {
								// console.log(obj);
								return obj.page.totalRecords;
							}
						},
						colNames : columnsMapping.createJQGridColumnNames(tableStructure, grouping),
						colModel : colModel,
						height : (jQuery(window).height() * 0.50),
						autowidth : true,
						shrinkToFit : true,
						forceFit : false,
						viewrecords : true,
						loadtext : "<span data-i18n='tabs.common:loading'>Loading...</span>",
						headertitles : true,
						gridview : true,
						rownumbers : false,
						rowNum : rowNum,
						pager : "#" + gridPagerBaseName + id,
						emptyrecords : "<span data-i18n='tabs.common:noRecordsToView'>No records to view</span>",
						grouping : grouping,
						groupingView : columnsMapping.createJQGridGroupingModel(tableStructure, grouping),
						footerrow : true,
						loadBeforeSend : function(xhr, settings) {
							TranslationManager.searchAndTranslate();
						},
						gridComplete : function() {
							// background colors.
							jQuery(grid).find(">tbody>tr.jqgrow:odd").addClass("myAltRowClassEven");
							jQuery(grid).find(">tbody>tr.jqgrow:even").addClass("myAltRowClassOdd");

							// Change row color depending the status.
							var cRows = this.rows.length, iRow, row, className;
							
							var teamid = app.TabsApp.settings.attributes.teamid.name;
							var crossteamvalidation = (app.TabsApp.settings.attributes.crossteamenable.name === 'true');
							var teamlead = (app.TabsApp.settings.attributes.teamlead.name  === 'true');
							var validator = (app.TabsApp.settings.attributes.validator.name === 'true');
							var teamtype = app.TabsApp.settings.attributes.accestype.name;
							
							for (iRow = 0; iRow < cRows; iRow++) {
								row = this.rows[iRow];
								className = row.className;
								// Ignore grouped rows.
								if (jQuery.inArray('jqgrow', className.split(' ')) > 0) {
									// Set font color according to status.
									var draft = row.cells[3].textContent;
									var approvalStatus = row.cells[2].textContent;
									var activityteamid = row.cells[4].textContent;
								
									// Status Mapping
									var statusMapping = {
										New_Draft : '0',
										New_Unvalidated : '1',
										Existing_Draft : '2',
										Validated_Activities : '3',
										Existing_Unvalidated : '4',
										Approved : '5',
										Rejected : 6

									};

									// Calculated status based on draft and
									// approval status.
									var getApprovalStatus = function(draft, approvalStatus) {
										if (draft == 'true') {
											if (approvalStatus == '2' || approvalStatus == '1') {
												return statusMapping.Existing_Draft;
											} else {
												row.cells[4].textContent = '* ' + row.cells[4].textContent;
												return statusMapping.New_Draft;
											}
										} else {
											switch (approvalStatus) {
											case '1':
												return statusMapping.Approved;
												break;
											case '2': case '5': case '6':
												return statusMapping.Existing_Unvalidated;
												break;
											case '3':
												row.cells[4].textContent = '* ' + row.cells[4].textContent;
												return statusMapping.Approved;
												break;
											case '4':
												row.cells[4].textContent = '* ' + row.cells[4].textContent;
												return statusMapping.New_Unvalidated;
												break;
											default:
												break;
											}
										}
									};

									// Assign colors for each row.
									// TODO: Missing colors for rejected and not approved.
									// TODO: Check this public view id needed.
									var x = getApprovalStatus(draft, approvalStatus);
									var iconedit = "<img src='/TEMPLATE/ampTemplate/tabs/css/images/ico_edit.gif'/></a>";
									var iconvalidated = "<img src='/TEMPLATE/ampTemplate/tabs/css/images/validate.png'/></a>";
									var link = "<a href='/wicket/onepager/activity/" + id + "'>";
									
									if (x == statusMapping.Approved ) {
										row.className = className + ' status_1';
										// Create link to edit activity.
										if (teamtype != "Management"){
											jQuery(row.cells[0]).html(iconedit+link);
										}else{
											jQuery(row.cells[0]).html(link);
										}
										
									} else if (x == statusMapping.Existing_Draft || x == statusMapping.New_Draft) {
										row.className = className + ' status_2';
										jQuery(row.cells[0]).html(iconedit + link);

									} else if (x == statusMapping.Existing_Unvalidated || x == statusMapping.New_Unvalidated) {
										row.className = className + ' status_3';
										//Cross team enable team lead and validators able to validate show icon.
										if (crossteamvalidation && (teamlead || validator)){
											if (teamtype != "Management"){
												jQuery(row.cells[0]).html(link + iconvalidated);
											}else{
												jQuery(row.cells[0]).html(link);
											}
										//Cross team disable team lead and validators able to validate only if the activity belongs to the workspace.	
										}else if (!crossteamvalidation && activityteamid ==  teamid &&(teamlead || validator)){
											jQuery(row.cells[0]).html(link + iconvalidated);
										}else{
											jQuery(row.cells[0]).html(iconedit + link);
										}	
									}

									
									var id = row.cells[1].textContent;

									// Create link to preview activity on first
									// not grouped column.
									var colIndex = -1;
									jQuery(jQuery(grid).jqGrid('getGridParam', 'colModel')).each(function(i, item) {
										if (colIndex == -1 && item.hidden == false && i > 0) {
											colIndex = i;
										}
									});
									var newContent = "<span style='cursor: pointer;' onclick = \x22openPreviewPage(" + id + ")\x22>"
											+ jQuery(row.cells[colIndex]).html() + "</span>";
									jQuery(row.cells[colIndex]).html(newContent);
								}
							}
							TranslationManager.searchAndTranslate();
						},
						loadComplete : function() {
							// Calculate footer totals row for this page.
							var colData = {};
							var totalColumnIndex = colModel.length - 1;
							jQuery.each(colModel, function(i, item) {
								if (item.reportColumnType == 'MEASURE') {
									// TODO: Replace this js sum with data from
									// the endpoint.
									var sum = jQuery(grid).jqGrid('getCol', item.name, false, 'sum');
									colData[item.name] = TabUtils.numberToString(sum, app.TabsApp.numericFormatOptions);
									totalColumnIndex--;
								}
							});
							colData[colModel[totalColumnIndex].name] = TranslationManager.getTranslated('Page Total:');
							jQuery(grid).jqGrid('footerData', 'set', colData);

							// Create an additional footer row for grand-totals
							// (not natively supported by jqgrid).
							jQuery("#grand_total_row_" + id).empty();
							jQuery("#grand_total_row_" + id).remove();
							var pageFooterRow = jQuery("#main-dynamic-content-region_" + id + " .ui-jqgrid-ftable .footrow-ltr");
							var grandTotalFooterRow = jQuery(pageFooterRow).clone();
							jQuery(grandTotalFooterRow).attr("id", "grand_total_row_" + id);
							var grandTotalText = TranslationManager.getTranslated('Grand Total:');
							jQuery(grandTotalFooterRow).find(
									"[aria-describedby='tab_grid_" + id + "_" + colModel[totalColumnIndex].name + "']")
									.text(grandTotalText).attr("title", grandTotalText);
							jQuery.each(grandTotals, function(i, item) {
								jQuery(grandTotalFooterRow).find("[aria-describedby='tab_grid_" + id + "_" + item.columnName + "']").text(
										item.displayedValue).attr("title", item.displayedValue);
							});
							jQuery(grandTotalFooterRow).insertAfter(pageFooterRow);
						}
					});
			app.TabsApp.currentGrid = jQuery(grid);
		});
	};

	function extractMetadata(content) {
		var Metadata = Backbone.DocumentModel.extend({
			defausts : {
				columns : [],
				measures : [],
				hierarchies : []
			}
		});
		var metadata = new Metadata();
		var metadataJson = content.get('reportMetadata').get('reportSpec');
		metadata.columns = metadataJson.get('columns');
		metadata.hierarchies = metadataJson.get('hierarchies');
		metadata.measures = metadataJson.get('measures');
		return metadata;
	}

	/*
	 * Before trying to render the data from server we need to make some
	 * transformations and cleanups.
	 */
	function transformData(data, grouping, hierarchies) {
		var rows = [];
		// Process the headers for later usage.
		if (data.headers != null) {
			jQuery.each(data.headers, function(i, item) {
				headers.push({
					columnName : item["columnName"],
					originalColumnName : item["originalColumnName"],
					hierarchicalName : item["hierarchicalName"]
				});
			});

			getContentRecursively(data.page.pageArea, rows, null);
			if (grouping) {
				postProcessHierarchies(rows, hierarchies);
			}
		}
		// console.log(rows);
		return rows;
	}

	/*
	 * The data from server uses a hierarchy format where not all values are set
	 * in all subnodes (children), so we have to add them manually before
	 * rendering.
	 */
	function postProcessHierarchies(rows, hierarchies) {
		jQuery.each(rows, function(i, row) {
			jQuery.each(hierarchies.models, function(j, hierarchy) {
				if (row[hierarchy.get('columnName')] != undefined) {
					hierarchy.set('lastValue', row[hierarchy.get('columnName')]);
				} else {
					row[hierarchy.get('columnName')] = hierarchy.get('lastValue');
				}
			});
		});
	}

	function findInMapByColumnName(name, property) {
		var ret = undefined;
		jQuery.each(headers, function(i, item) {
			if (item[property] == name) {
				ret = item;
			}
		});
		return ret;
	}

	function getContentRecursively(obj, rows, parent) {
		if (obj != undefined && obj != null) {
			if (obj.children == null || obj.children.length == 0) {
				// console.log(obj.contents);
				var row = {
					id : 0
				};
				jQuery.each(obj.contents, function(key, element) {
					var colName = null;
					if (findInMapByColumnName(key, 'hierarchicalName') != undefined) {
						// TODO: compare the 3 options with the values from
						// hierarchies to see if one matches and use that.
						colName = findInMapByColumnName(key, 'hierarchicalName').originalColumnName;
					}
					if (colName != undefined && colName != null) {
						if (element.displayedValue != null && element.displayedValue.toString().length > 0) {
							row[colName] = element.displayedValue;
						} else {
							row[colName] = getParentContent(key, parent);
						}
						row['id'] = Math.random();
					}
				});
				// console.log(row);
				rows.push(row);
			} else {
				jQuery(obj.children).each(function(i, item) {
					getContentRecursively(item, rows, obj.contents);
				});
			}
		}
	}

	/*
	 * The endpoint doesnt fill the hierarchical values after the 1st node so we
	 * need to take some values from the parent by recursion.
	 */
	function getParentContent(key, parent) {
		if (parent != undefined && parent != null) {
			if (parent[key].displayedValue != null && parent[key].displayedValue.indexOf(' Totals') > 0) {
				return parent[key].displayedValue.substring(0, parent[key].displayedValue.indexOf(' Totals'));
			} else {
				getParentContent(key, parent.parent);
			}
		} else {
			return 'NULL';
		}
	}

	/*
	 * Extract report grand totals (ignoring pagination) from the endpoint data.
	 */
	function extractGrandTotals(data, colModel) {
		var ret = [];
		jQuery.each(colModel, function(i, item) {
			if (item.reportColumnType == 'MEASURE') {
				var col = {};
				col.columnName = item.name;
				if (data.page != null && data.page.pageArea != null) {
					try {
						col.value = data.page.pageArea.contents["[" + item.name + "]"].value;
						col.displayedValue = data.page.pageArea.contents["[" + item.name + "]"].displayedValue;
						ret.push(col);
					} catch (err) {
						console.error(err + item.name);
					}
				}
			}
		});
		return ret;
	}

	return GridManager;
});