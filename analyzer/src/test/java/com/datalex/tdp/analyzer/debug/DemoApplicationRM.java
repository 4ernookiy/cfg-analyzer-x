package com.datalex.tdp.analyzer.debug;

import com.datalex.tdp.analyzer.CollectAllInfo;
import com.datalex.tdp.analyzer.collect.rule.BusinessCategoryInfo;
import com.datalex.tdp.analyzer.config.Messages;
import com.datalex.tdp.analyzer.data.MenuItemVO;
import com.datalex.tdp.analyzer.parse.ParseDataFromMenusXml;
import com.datalex.tdp.analyzer.parse.ParseDataFromRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DemoApplicationRM
{
    private static final Logger logger = LoggerFactory.getLogger(DemoApplicationRM.class);

    public static void main(String[] args)
    {
        ParseDataFromRules parseDataFromRules = new ParseDataFromRules();
        Map<String, BusinessCategoryInfo> rulesData = parseDataFromRules.parseAndCollect();

        ParseDataFromMenusXml parseMenus = new ParseDataFromMenusXml();
        List<MenuItemVO> list = parseMenus.parseAndCollect();

        for (Map.Entry<String, BusinessCategoryInfo> entry : rulesData.entrySet())
        {
            String serviceName = entry.getKey();
            List<MenuItemVO> listForName = itemsForName(serviceName, list);
            BusinessCategoryInfo bci = entry.getValue();
            bci.menuItems = listForName;
        }

        CollectAllInfo.logJson(rulesData);
//        System.out.println(list);

    }

    public static List<MenuItemVO> itemsForName(String name, List<MenuItemVO> list)
    {
        List<MenuItemVO> listForName = new ArrayList<>();
        if (name == null)
        {
            return listForName;
        }
        for (MenuItemVO item : list)
        {
            if (name.equals(item.serviceName))
            {
                listForName.add(item);
            }
        }
        return listForName;
    }

}
