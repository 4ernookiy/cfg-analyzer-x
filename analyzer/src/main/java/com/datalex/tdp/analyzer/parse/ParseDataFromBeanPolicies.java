package com.datalex.tdp.analyzer.parse;

import com.datalex.tdp.analyzer.collect.TdpServiceInfo;
import com.datalex.tdp.analyzer.config.Config;
import com.datalex.tdp.analyzer.config.Utils;
import com.datalex.tdp.analyzer.jxpath.XpathEvaluate;
import com.datalex.tdp.analyzer.parse.bre.CheckAsInline;
import com.datalex.tdp.analyzer.parse.bre.CheckAsPreBre;
import com.datalex.tdp.analyzer.parse.bre.CheckBase;
import com.datalex.tdp.analyzer.reporting.TimeExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dzmitry_Charnavoki on 11/18/2016.
 */
public class ParseDataFromBeanPolicies
{
    private static final Logger logger = LoggerFactory.getLogger(ParseDataFromBeanPolicies.class);
    private XpathEvaluate evaluate = new XpathEvaluate();
    private final Node allBeanPolicies = Utils.getBeanPoliciesAsNode();

    public void createStats()
    {
        // todo добавить список остальных клиентов JXPathAirOfferRulesClient, JXPathAirOfferConfigurationRulesClient
        List<Node> xpathClients = evaluate
            .retrieveListByXpath(allBeanPolicies, "BeanPolicies/Policy/Bean[contains(@impl,\"JXPathRulesClient\")]");
        List<CheckBase> checks = new ArrayList<>();
        checks.add(new CheckAsPreBre());
        checks.add(new CheckAsInline());
        logger.info("will be handle {} xPathClients", xpathClients.size());
        for (Node xpathClient : xpathClients)
        {

            // first parent - policy, second - file
            Node filePolicy = xpathClient.getParentNode().getParentNode();
            String path = evaluate.getAttribute(filePolicy, Config.ATR_PATH);
            String name = evaluate.getAttribute(filePolicy, Config.ATR_NAME);

            for (CheckBase check : checks)
            {
                TdpServiceInfo tt = null;
                String typeClient = evaluate.getAttribute(xpathClient, "type");
                TimeExecution te = new TimeExecution("determine", 50l);
                try
                {
                    tt = check.search(xpathClient, allBeanPolicies);
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
                te.logMessage(logger, typeClient + " - {} ms :");
                if (tt != null)
                {
                    break;
//                    Collected.getStore().put(tt);
                }
            }

        }
    }

}

