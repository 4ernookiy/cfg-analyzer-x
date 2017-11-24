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
            tdpdata: "tdpdata"

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

    function Api() {
        this.info();
        init();
    }

    Api.prototype = {
        constructor: Api,
        moduleName: _moduleName,
        info: function() {
            log(this.moduleName + " ver:" + this.version + " : " + _description);
            if (_debug) {
                log("дебаг режим для " + _moduleName);
            }
        },
        init: init,
        generate: generate
    };

    function init() {
        $.getJSON(_url_api_bp, function(data) {
            log("recived from url");
            _data = data;
            log(_data[0]);
            generate();
            collapse();
        });
    }

    function generate() {
        var tagdata = document.getElementById(_id.tdpdata);

        for (i = 0; i < _data.length; i++) { //srv			
            var srv = _data[i];
            var li = addTag(tagdata, "li");
            var span = addSpan(li, i, "srv_");
            var textBeanOrder = (i + 1) + " ";

            span.appendChild(getI_el("Service" + " " + textBeanOrder + " : ", "c.S"));
            span.appendChild(document.createTextNode(srv.remoteImpl));
       

            var cxc = getSpan(" " + srv.xpathClients.length, "xcCount");
            var ol = addTag(li, "ol");

            for (j = 0; j < srv.xpathClients.length; j++) { // xclients

                var xc = srv.xpathClients[j];
                var liX = addTag(ol, "li");
                var spanX = addSpan(liX, j, "xc_" + i + "_");

                spanX.appendChild(getI_el("XpathClientType : ", "c.X"));            
                var group = xc.groupOfRule;
                if (group != "NotRecognized") {
                	spanX.appendChild(document.createTextNode(xc.groupOfRule + " "));
                }
                spanX.appendChild(document.createTextNode(xc.type + " ", "xcName"));
                spanX.appendChild(document.createElement("br"));
                spanX.appendChild(document.createTextNode(" Categories : " + "'" + xc.categories + "'"));


                var olC = addTag(liX, "ol");
                var liC = addTag(olC, "li", "cgrySrvNm");
                liC.appendChild(getSpan("serviceName : ", "cProp"));
                liC.appendChild(getSpan(xc.serviceName, "cVal"));

                if (xc.categoriesInfo != null) {
                    if (xc.categoriesInfo.menuItems != null) {
                        for (var mii = 0; mii < xc.categoriesInfo.menuItems.length; mii++) {
                            var menuItem = xc.categoriesInfo.menuItems[mii];
                            var tmpWP = addTag(olC, "li", "cgryMI");
                            tmpWP.appendChild(getSpan("menuItem : ", "miProp"));
                            tmpWP.appendChild(getSpan(menuItem.displayFirstName, "miFN"));
                            tmpWP.appendChild(getSpan(menuItem.displaySecondName, "miSN"));
                            tmpWP.appendChild(getSpan(menuItem.serviceName, "miSrvN"));
                        }
                    }

                    var cs = xc.categoriesInfo.categories;

                    for (k = 0; k < cs.length; k++) { // categories
                        var categry = cs[k];
                        var liC = addTag(olC, "li");
                        var spanC = addSpan(liC, k, "ctgry_" + i + "_" + j + "_");

                        spanC.appendChild(getI_el("Category : ", "c.C"));

                        spanC.appendChild(document.createTextNode(categry.name + " ", "cgryName"));
                        spanC.appendChild(document.createElement("br"));
                        spanC.appendChild(document.createTextNode("executionOrder : " + categry.executionOrder + " ", "cgryExOrd"));
                        

                        var olR = addTag(liC, "ol");
                        for (r = 0; r < categry.rules.length; r++) {
                            var rule = categry.rules[r];
                            var liR = addTag(olR, "li", "file");
                            liR.setAttribute("def", "rule");                         
                            var aRule = addTag(liR, "a");
                            aRule.appendChild(getSpan("Rule name : " + rule.name, "rName"));
                            aRule.appendChild(getSpan("File name : " + rule.fileName, "rFileName"));
                        }
                    }
                } else {
                    cxc.className += " xcCountWrong";
                }
            } // end xclients
        } // srv
    }


    function test() {
        log("test");
    }

    function showMenuInfo(mi) {
        if (mi != null) {
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
        var tagText = document.createTextNode(text);
        tag.appendChild(tagText);
        tag.setAttribute("def", defTagText);
        tag.setAttribute("class", "glyphicon-minus");
        return tag;
    }


    function addTag(owner, tagType, tagClass) {
        var tag = document.createElement(tagType);
        owner.appendChild(tag);
        if (tagClass != null) {
            tag.setAttribute("class", tagClass);
        }
        return tag;
    }

    function addSpan(owner, order, prefix) {
        var span = addTag(owner, "span");
        var link = prefix + order;
        return span;
    }

    function collapse() {
        $('.tree li:has(ol)').addClass('parent_li').find(' > span').attr('title', 'Collapse this branch');
        $('.tree li.parent_li > span').on('click', function(e) {
            var children = $(this).parent('li.parent_li').find(' > ol > li');
            if (children.is(":visible")) {
                children.hide('fast');
                $(this).attr('title', 'Expand this branch').find(' > i').addClass('glyphicon glyphicon-plus').removeClass('glyphicon glyphicon-minus');
            } else {
                children.show('fast');
                $(this).attr('title', 'Collapse this branch').find(' > i').addClass('glyphicon glyphicon-minus').removeClass('glyphicon glyphicon-plus');
            }
            e.stopPropagation();
        });
    };

    return new Api();
    
}());

window.xclient = window["com.datalex.viewer.xclient"]; // alias

log(xclient);

$(function() {
	   $('#collapseAll').on('click', function(e) {
        var children = $('.tree li.parent_li > span').parent('li.parent_li').find(' > ol > li');
        if (children.is(":visible")) {
            children.hide('fast');
            $('.tree li.parent_li > span').attr('title', 'Expand this branch').find(' > i').addClass('glyphicon glyphicon-plus').removeClass('glyphicon glyphicon-minus');
            
        } 
        e.stopPropagation();
    });

	   $('#expandAll').on('click', function(e) {
        var children = $('.tree li.parent_li > span').parent('li.parent_li').find(' > ol > li');
        if (children.is(":hidden")) {
            children.show('fast');
            $('.tree li.parent_li > span').attr('title', 'Collapse this branch').find(' > i').addClass('glyphicon glyphicon-minus').removeClass('glyphicon glyphicon-plus');
        }
        e.stopPropagation();
    });
});