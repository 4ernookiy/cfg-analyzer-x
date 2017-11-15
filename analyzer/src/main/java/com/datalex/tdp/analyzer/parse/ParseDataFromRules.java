package com.datalex.tdp.analyzer.parse;

import com.datalex.tdp.analyzer.collect.rule.BusinessCategoryInfo;
import com.datalex.tdp.analyzer.collect.rule.CategoryInfo;
import com.datalex.tdp.analyzer.collect.rule.RuleInfo;
import com.datalex.tdp.analyzer.config.Utils;
import com.datalex.tdp.analyzer.jxpath.XpathEvaluate;
import com.datalex.tdp.analyzer.reporting.TimeExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.datalex.tdp.analyzer.config.TdpXpathExpression.ATTR;

public class ParseDataFromRules
{
    private static final Logger logger = LoggerFactory.getLogger(ParseDataFromRules.class);
    private XpathEvaluate evaluate = new XpathEvaluate();
    private Map<String, BusinessCategoryInfo> mapBRE = new HashMap<>();
    private final Node nodeAllRules = Utils.getAllRulesAsNode();

    public Map<String, BusinessCategoryInfo> parseAndCollect()
    {
//        logger.info("handle all xpathClients and fill data about categories and xpathClients in ones");
        final Node node = nodeAllRules;
        TimeExecution te = new TimeExecution();
        // todo move to xpath expressions
        List<Node> servicesSummary = evaluate.retrieveListByXpath(node, "BusinessRules/ServiceSummary");

        for (Node summary : servicesSummary)
        {
            String path = evaluate.getAttribute(summary.getParentNode(), "path");
            String fileName = evaluate.getAttribute(summary.getParentNode(), "name");
            logger.trace("file: {} {}", fileName, path);
            final Node cloneSummary = summary.cloneNode(true);

            String serviceName = evaluate.getAttribute(cloneSummary, ATTR.SERVICE_NAME);
            if ("".equals(serviceName))
            {
                logger.error("service name is empty, is it possible ? {}");
            }
            BusinessCategoryInfo info = getBRE(serviceName);
            // todo move to xpath expressions
            List<Node> categoriesInSrv = evaluate
                .retrieveListByXpath(cloneSummary, "Collection[@type=\"CategorySummary\"]/CategorySummary");

            for (Node categoryNode : categoriesInSrv)
            {
                String catName = evaluate.getAttribute(categoryNode, "category");
                CategoryInfo category = info.getCategoryInfo(catName);
                if (category == null)
                {
                    category = new CategoryInfo();
                    category.name = catName;
                    info.categories.add(category);
                }
                category.executionOrder = evaluate.getAttribute(categoryNode, ATTR.EXECUTION_ORDER);

                final Node cloneCategoryNode = categoryNode.cloneNode(true);

                List<Node> rulesInCategory = evaluate.retrieveListByXpath(cloneCategoryNode, "Collection[@type=\"Rule\"]/Rule");

                for (Node ruleInCategory : rulesInCategory)
                {
                    RuleInfo ruleInfo = new RuleInfo();
                    ruleInfo.fileName = fileName;
                    ruleInfo.pointOfSale = evaluate.getAttribute(ruleInCategory, ATTR.POINT_OF_SALE);
                    ruleInfo.name = evaluate.getAttribute(ruleInCategory, ATTR.NAME);
                    ruleInfo.executionOrder = evaluate.getAttribute(categoryNode, ATTR.EXECUTION_ORDER);
                    category.rules.add(ruleInfo);
                }

            }

        }

        logger.info("end handle rules - total time:{} ms",te.getTotal());
        return mapBRE;
    }

    private BusinessCategoryInfo getBRE(String serviceName)
    {
        BusinessCategoryInfo bre = null;
        if (!"".equals(serviceName))
        {
            bre = mapBRE.get(serviceName);
            if (bre == null)
            {
                bre = new BusinessCategoryInfo();
                bre.serviceName = serviceName;
                mapBRE.put(serviceName, bre);
            }
        }
        else
        {
            throw new RuntimeException("!!!");
        }
        return bre;
    }

}

