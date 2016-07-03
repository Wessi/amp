nv.models.heatMapChart = function() {
	"use strict";

	//============================================================
    // Public Variables with Default Settings
    //------------------------------------------------------------	

    //var pie = nv.models.pie();
    //var legend = nv.models.legend().margin({top: 0, right: 0, bottom: 0, left: 0});

    var margin = {top: 30, right: 20, bottom: 20, left: 20}
    //var legendMargin = {top: 30, right: 20, bottom: 20, left: 20}
    var width = null;
	var height = null;
    var showLegend = false;
    var color = nv.utils.defaultColor();
    var tooltips = true;
    var tooltip = function(key, y, e, graph) {
    	return '<h3 style="background-color: '
        	+ e.color + '">' + key + '</h3>'
            + '<p>' +  y + '</p>';
        }
    var	state = nv.utils.state();
    var defaultState = null;
    var noData = "No Data Available.";
    var duration = 250;
    var dispatch = d3.dispatch('tooltipShow', 'tooltipHide', 'stateChange', 'changeState','renderEnd');

    //============================================================
    // Private Variables
    //------------------------------------------------------------

    var showTooltip = function(e, offsetElement) {
    	var tooltipLabel = pie.x()(e.point);
        var left = e.pos[0] + ( (offsetElement && offsetElement.offsetLeft) || 0 );
        var top = e.pos[1] + ( (offsetElement && offsetElement.offsetTop) || 0);
        var y = pie.valueFormat()(pie.y()(e.point));
        var content = tooltip(tooltipLabel, y, e, chart);
        nv.tooltip.show([left, top], content, e.value < 0 ? 'n' : 's', null, offsetElement);
    };

    var renderWatch = nv.utils.renderWatch(dispatch);

    var stateGetter = function(data) {
    	return function(){
    		return {
    			active: data.map(function(d) { return !d.disabled })
            };
        }
    };

    var stateSetter = function(data) {
        return function(state) {
            if (state.active !== undefined) {
                data.forEach(function (series, i) {
                    series.disabled = !state.active[i];
                });
            }
        }
    };
  
    var shortenText = function(text) {
    	var length = 17;
    	if (text.length > length) {
    		text = text.substring(0, length) + '...';
    	}
    	return text;
    };

    //============================================================
    // Chart function
    //------------------------------------------------------------

    function chart(selection) {
    	var _self = this;
    	//console.log('heatMapChart.chart');
        renderWatch.reset();
        //renderWatch.models(pie);

        selection.each(function(data) {//TODO: selection.each????
        	var container = d3.select(this);
            nv.utils.initSVG(container);

            var that = this;
            var availableWidth = (width || parseInt(container.style('width'), 10) || 960)
                    - margin.left - margin.right;
            var availableHeight = (height || parseInt(container.style('height'), 10) || 400)
                    - margin.top - margin.bottom;

            //chart.update = function() { container.transition().call(chart); }; //comented to avoid adding the chart again.
            chart.container = this;

            state.setter(stateSetter(data), chart.update)
                .getter(stateGetter(data))
                .update();

            //set state.disabled
            state.disabled = data.map(function(d) { return !!d.disabled });

            if (!defaultState) {
                var key;
                defaultState = {};
                for (key in state) {
                    if (state[key] instanceof Array)
                        defaultState[key] = state[key].slice(0);
                    else
                        defaultState[key] = state[key];
                }
            }

            //TODO: move these definitions to top.
            var innerMargin = {
            		top : 120,
        			right : 0,
        			bottom : 100,
        			left : 150
            };
        	var cubeSize = 30;
        	var width = 1024 - innerMargin.left - innerMargin.right;
        	var topSectionHeight = 180;
        	var legendSectionHeight = 20;
        	var height = topSectionHeight + (cubeSize * data[0].values.y.length) + legendSectionHeight;
        	var legendElementHeight = 22;
        	//var colors = [ "#cb213d", "#fca530", "#0e8466" ]; // alternatively colorbrewer.YlGnBu[9]
        	var noColor = '#FFFFFF';
        	var categories = [{min: -1, max: 0, color: noColor},
        	                  {min: 0, max: 1, color: "#D05151"},
        	                  {min: 1, max: 5, color: "#e68787"}, 
        	                  {min: 5, max: 10, color: "#e4883e"}, 
        	                  {min: 10, max: 15, color: "#f6b277"}, 
        	                  {min: 15, max: 20, color: "#adcd95"}, 
        	                  {min: 20, max: 101, color: "#7ba05f"}];
        	
        	$(container[0]).attr('height', height).attr('class', 'dash-chart nvd3-svg heatmap-chart');
        	
        	var svg = container
        		/*.select("#chart")*/
        		/*.append("svg")*/
        		/*.attr("width", width + innerMargin.left + innerMargin.right)*/
        		/*.attr("height", height + innerMargin.top + innerMargin.bottom)*/
        		.append("g")
        		.attr("transform", "translate(" + innerMargin.left + "," + innerMargin.top + ")");

        	// Rows container.
        	var yAxisLabelsContainer = svg        	
				.append("g")
				.attr("class", "heatmap-yAxis-container");
        	
        	// Rows.
        	var yLabels = yAxisLabelsContainer
        		.selectAll(".yLabel")
        		.data(data[0].values.y)
        		.enter()
        		.append("text")
        		.text(function(d) {
        			return shortenText(d);
        		})
        		.attr("x", 0)
        		.attr("y", function(d, i) {
        			return i * cubeSize;
        		})
        		.style("text-anchor", "end")
        		.attr("transform", "translate(-6," + cubeSize / 1.5 + ")")
        		.attr("class", function(d, i) {
        							return "yLabel mono axis nv-series";
        		})
        		.attr('data-title', function(d) {
        			return d;
        		});
        	
        		// Add Totals special row.
        		yAxisLabelsContainer.append("text")
        			.text(app.translator.translateSync("TOTALS"))
        			.attr("x", 0)
        			.attr("y", (data[0].values.y.length * cubeSize))
        			.attr("class", "yLabel mono axis nv-series heatmap-totals")
        			.style("text-anchor", "end")
        			.style("font-weight", "bold")
            		.attr("transform", "translate(-6," + cubeSize / 1.5 + ")");

        		// Columns container.
        		var xAxisLabelsContainer = svg
        			.append("g")
        			.attr("transform", "translate(18.5, -5)")
        			.attr("class", "heatmap-xAxis-container");

        		// Columns
        		var yAxisLabels = xAxisLabelsContainer
        			.selectAll(".xLabel")
        			.data(data[0].values.x)
        			.enter()
        			.append("text")
        			.text(function(d) {
        				return shortenText(d);
        			})
        			.attr("x", function(d, i) {
        				return i * cubeSize;
        			})
        			.attr("y", 0)
        			/*.style("text-anchor", "middle")*/
        			.attr("transform", function(d, i) {
        				return "rotate(270, " + (cubeSize * i) + ", 0)";
        			})
        			.attr("class", function(d, i) {
        							return "xLabel mono axis nv-series";
        			})
        			.attr('data-title', function(d) {
        				return d;
        			});
        		
        		// Add Totals special column.
        		xAxisLabelsContainer.append("text")
        			.text(app.translator.translateSync("TOTALS"))
        			.attr("x", cubeSize * data[0].values.x.length)
        			.attr("y", 0)
        			.attr("class", "xLabel mono axis nv-series heatmap-totals")
        			.style("font-weight", "bold")
            		.attr("transform", function(d, i) {
        				return "rotate(270, " + (cubeSize * data[0].values.x.length) + ", 0)";
        			});

        		// Cubes
        		var cubesContainer = svg
    				.append("g")
    				.attr("class", "heatmap-cubes-container");
        		for (var i = 0; i < data[0].values.length; i++) {
        			createCube(cubesContainer, data[0].values[i], cubeSize, noColor, categories);        			    				
        		}        
        		// Add total's row in the end.        		
        		for (var i = 0; i < data[0].values.x.length; i++) {
        			createCube(cubesContainer, {x: i + 1, y: data[0].values.y.length + 1, value: data[0].values.xPTotals[i], tooltip: data[0].values.xTotals[i]}, cubeSize, noColor, categories);
        		}
        		// Add total's column on the right side.
        		for (var j = 0; j < data[0].values.y.length; j++) {
        			createCube(cubesContainer, {x: data[0].values.x.length + 1 , y: j + 1, value: data[0].values.yPTotals[j], tooltip: data[0].values.yTotals[j]}, cubeSize, noColor, categories);
        		}
        		
        		// Add percentage legends.
        		createLegends(svg, data, cubeSize, categories, legendElementHeight);
        });

        renderWatch.renderEnd('pieChart immediate');
        return chart;
    }
    
    function createLegends(svg, data, cubeSize, categories, legendElementHeight) {
    	var legendsContainer = svg
			.append("g")
			.attr("transform", "translate(0, " + (((data[0].values.y.length + 1) * cubeSize) + 10) + ")")
			.attr("class", "heatmap-legends-container");
    	var legendsPool = [app.translator.translateSync("Less than 1%"),
    	                   app.translator.translateSync("Between 1% and 4.99%"),
    	                   app.translator.translateSync("Between 5% and 9.99%"),
    	                   app.translator.translateSync("Between 10% and 14.99%"),
    	                   app.translator.translateSync("Between 15% and 19.99%"),
    	                   app.translator.translateSync("More than 20%")];   	
    	var maxLegendTextWidth = 0;
    	for (var i = 0; i < legendsPool.length; i++) {
    		var auxWidth = calculateTextWidth(legendsPool[i]);
    		if (auxWidth > maxLegendTextWidth) {
    			maxLegendTextWidth = auxWidth;
    		}
    		$("#tempSpan").remove();
    	}
    	maxLegendTextWidth += 20;
    	
    	for (var i = 0; i < legendsPool.length; i++) {
    		var legends = legendsContainer
				.append("rect")
				.attr("x", (i * maxLegendTextWidth))
				.attr("width", maxLegendTextWidth)
				.attr("height", legendElementHeight)
				.attr("class", "bordered")
				.style("fill", categories[i + 1].color);
		
			var text = legendsContainer.append("text"); 
			text.attr('font-family', 'Arial')
				.attr('font-size', '11px')
				.attr("y", 15)
				.attr("x", ((i * maxLegendTextWidth) + ((maxLegendTextWidth - calculateTextWidth(legendsPool[i])) / 2)))
				.html(legendsPool[i]);
    	}
    }
    
    function createCube(cubesContainer, data, cubeSize, noColor, categories) {
    	var cube = cubesContainer
			.append("rect")
			.attr("x", ((data.x - 1) * cubeSize))
			.attr("y", ((data.y - 1) * cubeSize))
			.attr("rx", 4)
			.attr("ry", 4)
			.attr("class", "bordered")
			.attr("width", cubeSize)
			.attr("height", cubeSize)
			.style("fill", noColor);
		
		cube.transition()
			.duration(1000)
			.style("fill", calculateColorFromCategories(data.value, categories, noColor));
		
		var text = cubesContainer.append("text"); 
		text.attr('font-family', 'Arial')
			.attr('font-size', '11px')
			.attr("x", ((data.y - 1) * cubeSize))
			.attr("y", ((data.y - 1) * cubeSize) + 19)
			.attr("x", function() {
				var d = data;
				var auxVal = d.value;
				if (auxVal > 0 && auxVal < 1) {
					return ((d.x - 1) * cubeSize) + 4;
				} else if (auxVal < 10) {
					return ((d.x - 1) * cubeSize) + 8;
				} else if (auxVal == 100) {
					return ((d.x - 1) * cubeSize) + 1;	
				} else {
					return ((d.x - 1) * cubeSize) + 5;
				}					
			}).html(function() {
				var d = data;
				var auxVal = d.value;
				if (auxVal > -1) {
					if (auxVal > 0 && auxVal < 1) {
						return '<1%';
					} else {
						return auxVal + '%';
					}
				} else {
					return '';
				}
			});
		if (data.tooltip) {
			text.attr('data-title', data.tooltip)
			.attr("class", "nv-series");
		}
    }
    
    function calculateColor(value, colors) {
    	var color = "";
    	var cutPoint = 100 / colors.length;
    	for (var i = 0; i < colors.length ; i++) {
    		if ((value >= (i * cutPoint)) && (value < ((i + 1) * cutPoint))) {
    			color = colors[i];
    			break;
    		}
    	}    	
    	return color;
    }
    
    function calculateColorFromCategories(value, categories, noColor) {
    	var color = noColor;
    	for (var i = 0; i < categories.length; i++) {
    		if ((value >= categories[i].min) && (value < categories[i].max)) {
    			color = categories[i].color;
    			break;
    		}
    	}
    	return color;
    }
    
    function calculateTextWidth(text) {
    	$("body").append("<span id='tempSpan' class='invisible'>" + text + "</span>");
    	var auxWidth = $("#tempSpan").width();
    	$("#tempSpan").remove();
    	return auxWidth;
    }

    //============================================================
    // Event Handling/Dispatching (out of chart's scope)
    //------------------------------------------------------------

    /*dispatch.on('elementMouseover.tooltip', function(e) {
        e.pos = [e.pos[0] +  margin.left, e.pos[1] + margin.top];
        dispatch.tooltipShow(e);
    });

    dispatch.on('tooltipShow', function(e) {
        if (tooltips) showTooltip(e);
    });

    dispatch.on('tooltipHide', function() {
        if (tooltips) nv.tooltip.cleanup();
    });*/

    //============================================================
    // Expose Public Variables
    //------------------------------------------------------------

    // expose chart's sub-components
    chart.legend = {};/*legend;*/
    chart.dispatch = dispatch;
    chart.pie = {};/*pie;*/
    chart.options = nv.utils.optionsFunc.bind(chart);

    // use Object get/set functionality to map between vars and chart functions
    chart._options = Object.create({}, {    	
        // simple options, just get/set the necessary values
        noData:         {get: function(){return noData;},         set: function(_){noData=_;}},
        tooltipContent: {get: function(){return tooltip;},        set: function(_){tooltip=_;}},
        tooltips:       {get: function(){return tooltips;},       set: function(_){tooltips=_;}},
        showLegend:     {get: function(){return showLegend;},     set: function(_){showLegend=_;}},
        defaultState:   {get: function(){return defaultState;},   set: function(_){defaultState=_;}},
        // options that require extra logic in the setter
        color: {get: function(){return color;}, set: function(_){
            color = _;
            /*legend.color(color);
            pie.color(color);*/
        }},
        duration: {get: function(){return duration;}, set: function(_){
            duration = _;
            renderWatch.reset(duration);
        }},
        margin: {get: function(){return margin;}, set: function(_){
            margin.top    = _.top    !== undefined ? _.top    : margin.top;
            margin.right  = _.right  !== undefined ? _.right  : margin.right;
            margin.bottom = _.bottom !== undefined ? _.bottom : margin.bottom;
            margin.left   = _.left   !== undefined ? _.left   : margin.left;
        }},
        /*legendMargin: {get: function(){return legendMargin;}, set: function(_){
        	legendMargin.top    = _.top    !== undefined ? _.top    : legendMargin.top;
        	legendMargin.right  = _.right  !== undefined ? _.right  : legendMargin.right;
        	legendMargin.bottom = _.bottom !== undefined ? _.bottom : legendMargin.bottom;
        	legendMargin.left   = _.left   !== undefined ? _.left   : legendMargin.left;
        }},*/
    });
    
    chart.height = function(_) {
        if (!arguments.length) return height;
        height = _;
        return chart;
    };
    
    //nv.utils.inheritOptions(chart, pie);
    nv.utils.initOptions(chart);
    return chart;
};