window["com.datalex.viewer.xclient"] = (function() {

	if (typeof ctx === 'undefined') {
		ctx = "";
	}

	var _moduleName = "com.datalex.viewer.xclient", 
		_version = "0.0.1-Snapshot", 
		_description = "Модуль отображения рул-клиентов", 
		_fileName = ctx + "/data/report.json", 
		_url_api_bp = ctx + "/api/bp",
		_debug = true, 
		_data = {}, 
		_id = { 
			tdpdata : "tdpdata"

	};

	window.log = function() {
		if (_debug) {
			try {
				return console.log.apply(console, arguments);
			} catch (_error) {
				console.log(_error);
			}
		}
	};

	/*
	 * if (typeof $ === 'undefined') { throw new Error(_moduleName + '
	 * JavaScript requires jQuery') }
	 * 
	 * 
	 * if (typeof noty === 'undefined') { console.log("noty нет - пробуем
	 * грузить"); $.getScript(
	 * "/webjars/noty/2.3.5/js/noty/packaged/jquery.noty.packaged.min.js?v1",
	 * function( data, textStatus, jqxhr ) { console.log( "Load noty was
	 * performed." ); }); //throw new Error(_moduleName + ' JavaScript requires
	 * noty') }
	 */

	function Api() {
		this.info();
		init();
	}

	Api.prototype = {
		constructor : Api,
		moduleName : _moduleName,
		info : function() {
			log(this.moduleName + " ver:" + this.version + " : " + _description);
			if (_debug) {
				log("дебаг режим для " + _moduleName);
			}
		},
		init : init,
		generate : generate
	/*
	 * , generateWithButton:generateWithButton, handleAjaxError:
	 * handleAjaxError, useTemplate: useTemplate, getTemplateAsString:
	 * getTemplateAsString, ajaxUserCreate: ajaxUserCreate
	 */
	};

	function init() {
		
//		$.getJSON(_fileName, function(data) {
//			_data = data;
//			log(_data[0]);
//			generate();
//		});

		$.getJSON(_url_api_bp, function(data) {
			log("recived from url");
			_data = data;
			log(_data[0]);
			generate();
		});
		
		

	}

	function generate() {
		var tagdata = document.getElementById(_id.tdpdata);
		  
		for (i = 0; i < _data.length; i++) { //srv
			var srv = _data[i];
			var li = addTag(tagdata, "li");
			
			
			var label = addLabel(li, i, "srv_");
			
			var textBeanOrder = i < 9 ? ("_" + (i+1) + " "): (i+1) + " ";
			
			label.appendChild(getI_el("S", "c.S"));
			
			label.appendChild(getSpan(textBeanOrder, "beanOrder"));

			label.appendChild(getSpan(srv.remoteImpl, "beanName"));
			
			var cxc = getSpan(" "+ srv.xpathClients.length, "xcCount");
			label.appendChild(cxc);

			var ol = addTag(li, "ol");			
			for (j =0 ; j < srv.xpathClients.length; j ++) { // xclients
				var xc = srv.xpathClients[j];
				
				var liX = addTag(ol, "li");
				
				var labelX = addLabel(liX, j, "xc_"+i+"_");
				
				labelX.appendChild(getI_el("X", "c.X"));				 
				labelX.appendChild( getSpan(xc.groupOfRule+" ", "xcType"));
				labelX.appendChild( getSpan(xc.type+" ", "xcName"));
				labelX.appendChild( getSpan("'"+xc.categories+"'", "xcCategory"));
				

				var olC = addTag(liX, "ol");		
				var liC = addTag(olC, "li", "cgrySrvNm");
				liC.appendChild( getSpan("serviceName : ", "cProp"));
				liC.appendChild( getSpan(xc.serviceName, "cVal"));

				
				if (xc.categoriesInfo != null){

					if (xc.categoriesInfo.menuItems != null){
						for (var mii = 0; mii < xc.categoriesInfo.menuItems.length;mii++){
							var menuItem = xc.categoriesInfo.menuItems[mii];
							var tmpWP = addTag(olC, "li", "cgryMI");
							tmpWP.appendChild( getSpan("menuItem : ", "miProp"));
							tmpWP.appendChild( getSpan(menuItem.displayFirstName, "miFN"));
							tmpWP.appendChild( getSpan(menuItem.displaySecondName, "miSN"));
							tmpWP.appendChild( getSpan(menuItem.serviceName, "miSrvN"));
							
						}
					}
					
					
					var cs = xc.categoriesInfo.categories;
					
					for (k = 0 ; k < cs.length; k++) { // categories
						var categry = cs[k];
						
						var liC = addTag(olC, "li");
						var labelC = addLabel(liC, k, "ctgry_"+i+"_"+j+"_");
						
						labelC.appendChild(getI_el("C", "c.C")); 
						labelC.appendChild( getSpan(categry.executionOrder+" ", "cgryExOrd"));
						labelC.appendChild( getSpan(categry.name+" ", "cgryName"));
						labelC.appendChild( getSpan(categry.rules.length+" ", "cgryRC"));
						
//						if (xc.categoriesInfo.menuItems != null){
//							labelC.onlick = showMenuInfo(event, xc.categoriesInfo.menuItems);	
//						}
						
						var olR = addTag(liC, "ol");
						for (r=0; r < categry.rules.length ; r++) {
							var rule = categry.rules[r];
							
							var liR = addTag(olR, "li", "file");
							liR.setAttribute("def", "rule");
							var aRule = addTag(liR, "a");
							//  + ":"+ rule.fileName
							
							// var aText = document.createTextNode(rule.name);
							aRule.appendChild(getSpan(rule.name, "rName"));
							aRule.appendChild(getSpan(rule.fileName, "rFileName"));
							
//							var aText = document.createTextNode(rule.fileName);
//							aRule.appendChild(aText);
						}
						
						
						
						
					}
					
					// end if
				}  else {
					cxc.className += " xcCountWrong";
				}
				
				
				
			} // end xclients
			

		} // srv
	}


	function test(){
		log("test");
	}
	
	function showMenuInfo(mi) {
		if ( mi != null){
			log(mi[0]);
		} else {
			log("hello");
		}
		
		return false;
	}
	
	function getSpan(text, className) {
		var span = document.createElement("span");
		var spanText = document.createTextNode(text);
		span.appendChild(spanText);
		span.setAttribute("class", className);
		return span;
	}

	function getI_el(text, defTagText) {
		var tag = document.createElement("i");
		tag.setAttribute("onMouseOver", "tooltip(this,'S: ServiceName " +
				"X: JXPathRulesClients C: Categories R: Rules')"); 
		tag.setAttribute("onMouseOut", "hide_info(this)"); 
		var tagText = document.createTextNode(text);
		tag.appendChild(tagText);
		tag.setAttribute("def", defTagText);
		return tag;
	}
	
	
	function addTag(owner, tagType, tagClass) {
		var tag = document.createElement(tagType);
		owner.appendChild(tag);
		if (tagClass != null ) {
			tag.setAttribute("class", tagClass);
		}
		return tag;
	}
	
	function addLabel(owner, order, prefix) {
		var label = addTag(owner, "label"); 
		var link = prefix+order;
		label.setAttribute("for", link);
		var input = addTag(owner, "input");
		input.setAttribute("id", link);
		input.setAttribute("type", "checkbox");
		return label;
	}

	return new Api();

}());

window.xclient = window["com.datalex.viewer.xclient"]; // alias

log(xclient);

/*
 * var jqxhr = $.getJSON( "report.json", function() { console.log( "success" ); })
 * .done(function() { console.log( "second success" ); }) .fail(function() {
 * console.log( "error" ); }) .always(function() { console.log( "complete" );
 * });
 */
