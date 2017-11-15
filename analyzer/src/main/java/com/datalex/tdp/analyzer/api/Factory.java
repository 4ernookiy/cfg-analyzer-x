package com.datalex.tdp.analyzer.api;

import com.datalex.tdp.analyzer.collect.Collected;
import com.datalex.tdp.analyzer.collect.TdpServiceInfo;
import com.datalex.tdp.analyzer.collect.rule.BusinessCategoryInfo;
import com.datalex.tdp.analyzer.collect.rule.RuleDefPolicy;
import com.datalex.tdp.analyzer.config.PropertyReader;
import com.datalex.tdp.analyzer.data.MenuItemVO;
import com.datalex.tdp.analyzer.parse.ParseDataFromBeanPolicies;
import com.datalex.tdp.analyzer.parse.ParseDataFromMenusXml;
import com.datalex.tdp.analyzer.parse.ParseDataFromRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class Factory implements IFactory
{
    private static final Logger logger = LoggerFactory.getLogger(Factory.class);
    private static final IFactory factory = new Factory();
    public static Boolean necessaryToLoadConfig = true;

    private Factory()
    {
    }

    public static final IFactory getInstance()
    {
        return factory;
    }

    @Override
    public List<TdpServiceInfo> getInfo()
    {
        List<TdpServiceInfo> list = null;
        try
        {
            ParseDataFromBeanPolicies parseBeanPolicies = new ParseDataFromBeanPolicies();
            parseBeanPolicies.createStats();

            List<MenuItemVO> menus = getMenuItems();

            Map<String, BusinessCategoryInfo> rulesData = getRulesInfo();

            for (TdpServiceInfo srv : Collected.getStore().getInfo().values())
            {
                for (RuleDefPolicy ruleDefPolicy : srv.xpathClients)
                {
                    String serviceName = ruleDefPolicy.serviceName;
                    BusinessCategoryInfo bre = rulesData.get(serviceName);
                    ruleDefPolicy.categoriesInfo = bre;
                    if (bre != null)
                    {
                        List<MenuItemVO> lm = getMenuForSrvName(serviceName, menus);
                        bre.menuItems = lm;
                    }
                    else
                    {
                        logger.info("bre is null : {}", serviceName);
//                        System.out.println(serviceName);
                    }
                }
            }

            list = new ArrayList<>(Collected.getStore().getInfo().values());
            Comparator<TdpServiceInfo> byName =
                (TdpServiceInfo o1, TdpServiceInfo o2) -> o1.remoteImpl.compareTo(o2.remoteImpl);
            Collections.sort(list, byName);
//        Map<String, Object> about = new HashMap<>();
//        about.put("serviceCount", list.size());
//        Messages.logJson(about);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<MenuItemVO> getMenuItems()
    {
        ParseDataFromMenusXml parseMenus = new ParseDataFromMenusXml();
        List<MenuItemVO> menus = parseMenus.parseAndCollect();
        return menus;
    }

    @Override
    public Map<String, BusinessCategoryInfo> getRulesInfo()
    {
        ParseDataFromRules parseDataFromRules = new ParseDataFromRules();
        Map<String, BusinessCategoryInfo> rulesData = parseDataFromRules.parseAndCollect();
        return rulesData;
    }

    private List<MenuItemVO> getMenuForSrvName(String serviceName, List<MenuItemVO> list)
    {

        List<MenuItemVO> lm = new ArrayList<>();
        if (null == serviceName || serviceName.isEmpty())
        {
            return lm;
        }
        for (MenuItemVO m : list)
        {
            if (serviceName.equals(m.serviceName))
            {
                lm.add(m);
            }
        }
        return lm;
    }

    @Override
    public boolean applyConfig(String path)
    {
        try
        {
            PropertyReader.loadProperties(path);
            necessaryToLoadConfig = false;
            return true;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return false;
    }
}
