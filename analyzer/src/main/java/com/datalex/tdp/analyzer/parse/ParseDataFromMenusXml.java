package com.datalex.tdp.analyzer.parse;

import com.datalex.tdp.analyzer.config.Utils;
import com.datalex.tdp.analyzer.data.MenuItemVO;
import com.datalex.tdp.analyzer.jxpath.XpathEvaluate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class ParseDataFromMenusXml
{
    private static final Logger logger = LoggerFactory.getLogger(ParseDataFromMenusXml.class);
    private static final String ATTR_SERVICE_NAME = "ServiceName";
    private static final String ATTR_DISPLAY_NAME = "DisplayName";
    private XpathEvaluate xpathEvaluate = new XpathEvaluate();
    private final Node menusXmlNode = Utils.getMenusXmlAsNode();

    public List<MenuItemVO> parseAndCollect()
    {
        Node businessModelMenuNode = xpathEvaluate.retrieveByXpath(menusXmlNode, "//BusinessModelMenu");
        final Node node = businessModelMenuNode.cloneNode(true);
        List<Node> menuItemsAll = xpathEvaluate.retrieveListByXpath(node, "//*[@ServiceName]");
        List<MenuItemVO> data = new ArrayList<>();

        for (Node menuItem : menuItemsAll)
        {
            String mainName = xpathEvaluate.getAttribute(menuItem.getParentNode(), ATTR_DISPLAY_NAME);
            String subName = xpathEvaluate.getAttribute(menuItem, ATTR_DISPLAY_NAME);
            String srvName = xpathEvaluate.getAttribute(menuItem, ATTR_SERVICE_NAME);

            MenuItemVO item = new MenuItemVO();
            item.displayFirstName = mainName;
            item.displaySecondName = subName;
            item.serviceName = srvName;
            data.add(item);
        }
        if(data.size() != menuItemsAll.size() ){
            logger.error("data.size() != menuItemsAll.size() - it impossible: error in parsing data");
        }
        return data;
    }

}

