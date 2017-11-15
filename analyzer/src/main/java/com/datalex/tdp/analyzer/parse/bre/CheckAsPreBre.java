package com.datalex.tdp.analyzer.parse.bre;

import com.datalex.tdp.analyzer.collect.Collected;
import com.datalex.tdp.analyzer.collect.PolicyLink;
import com.datalex.tdp.analyzer.collect.TdpServiceInfo;
import com.datalex.tdp.analyzer.collect.rule.GroupOfRule;
import com.datalex.tdp.analyzer.collect.rule.RuleDefPolicy;
import com.datalex.tdp.analyzer.config.Config;
import com.datalex.tdp.analyzer.reporting.TimeExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import java.util.Arrays;
import java.util.List;

public class CheckAsPreBre extends CheckBase
{
    private static final Logger logger = LoggerFactory.getLogger(CheckAsPreBre.class);

    // need move to config
    private List<String> processors = Arrays
        .asList("JXPathRulesProcessor", "JXPathRulesPostProcessor", "JXPathRulesPreProcessor", "JXPathRulesProcessorBase");

    public TdpServiceInfo search(Node xpathClient, Node allBeanPolicies)
    {
        TdpServiceInfo tdpServiceInfo = null;
        // first parent - policy, second - file
        Node filePolicy = xpathClient.getParentNode().getParentNode();
        String path = evaluate.getAttribute(filePolicy, Config.ATR_PATH);
        String name = evaluate.getAttribute(filePolicy, Config.ATR_NAME);

        Node checkedPolicy = xpathClient.getParentNode();

        // rule
        RuleDefPolicy ruleDefPolicy = new RuleDefPolicy();
        ruleDefPolicy.policyWhereRuleIsDefined = helper.getPolicyLInk(checkedPolicy);;

        ruleDefPolicy = helper.getRuleInfo(xpathClient, ruleDefPolicy);

//        String xpathReference = String.format("//Bean/Reference[contains(@bean,\"%s\")]", ruleDefPolicy.type);
        String xpathReference = String.format("Bean/Reference[contains(@bean,\"%s\")]", ruleDefPolicy.type);
        Node referenceToImpl2 = evaluate.retrieveByXpathFast(checkedPolicy, xpathReference);
        TimeExecution te = new TimeExecution("bre",10l);
        Node referenceToXPathProccessor = helper.retrievWithinHierarchy(checkedPolicy, xpathReference, allBeanPolicies);
        te.logMessage(logger, "time(Hierarchy) - {} ms :" + xpathReference);
        if (null != referenceToXPathProccessor ) // this mean
        {
            Node beanJxpathProcessor = referenceToXPathProccessor.getParentNode();
            String idOfProcessor = evaluate.getAttribute(beanJxpathProcessor, "type");
            String implOfProcessor = evaluate.getAttribute(beanJxpathProcessor, "impl");

            boolean isBre = isBre(implOfProcessor); // this indicate that it not just processor, but wrapper for call BRE

            if (isBre){
                // now we must be find this name in the list of processors
                // but firstly we must to search implementation for ProcessorImpl
                String template = "Bean[contains(@impl,\"%s\")]";
                Node processorsExecutor = evaluate.getNodeByListXpath(checkedPolicy, template, Config.TDP_IMPL_PROCESSORS);
                if ( processorsExecutor != null){
                    String typeOfProcessorOrBre = evaluate.getAttribute(processorsExecutor, "type");
                    String propStr = getProperty(processorsExecutor, "processors");
                    if (propStr.contains(idOfProcessor)){
                        // this mean we found bre ant it was enable for processing.
                        GroupOfRule ruleType = getTypeOfRule(typeOfProcessorOrBre);

                        PolicyLink policyWhereRuleIsUsed = helper.getPolicyLInk(processorsExecutor.getParentNode());


                        String xpathForJEEService = "Bean[@remoteImpl]";
                        Node clazzJEEService = evaluate.retrieveByXpathFast(processorsExecutor.getParentNode(), xpathForJEEService);
                        String valueOfRemoteImpl;
                        if (clazzJEEService == null){
                            valueOfRemoteImpl = policyWhereRuleIsUsed.id;
                            // todo defenition )))
//                            ruleDefPolicy.resolution = ManagerResolution.getWrongDefenition(policyWhereRuleIsUsed, ruleDefPolicy);
                            ruleDefPolicy.groupOfRule = GroupOfRule.NotRecognized;
                        } else {
                            valueOfRemoteImpl = helper.getBeanImplementation(clazzJEEService);
//                            ruleDefPolicy.resolution = ManagerResolution.OK_ALL_FINE;
                            ruleDefPolicy.groupOfRule = ruleType;
                        }

                        tdpServiceInfo = Collected.getStore().getByRemoteImpl(valueOfRemoteImpl);
                        tdpServiceInfo.policyOwnerService = policyWhereRuleIsUsed;
                        tdpServiceInfo.xpathClients.add(ruleDefPolicy);
                    }

                }

            }

        }
//        te.logMessage(logger, "getReferenceInHierarchy - {}");
        return tdpServiceInfo;
    }

    private boolean isValContainsInList(String val, List<String> list) {
        if (null == val || null == list ){
            throw new RuntimeException("Not supported empty value");
        }
        for(String impl: list){
            if (val.contains(impl)){
                return true;
            }
        }
        return false;
    }


    private boolean isBre(String simpleNameOfProcessor) {
        if (null == simpleNameOfProcessor){
            throw new RuntimeException("Not supported empty value");
        }
        return isValContainsInList(simpleNameOfProcessor, Config.TDP_IMPL_PROCESSORS_FOR_CALL_JXPATHCLIENT);
    }

    private GroupOfRule getTypeOfRule(String typeOfProcessorOrBre){

        if (Config.TDP_PRE_PROCESSORS.contains(typeOfProcessorOrBre)){
            return GroupOfRule.Pre;
        }
        if (Config.TDP_POST_PROCESSORS.contains(typeOfProcessorOrBre)){
            return GroupOfRule.Post;
        }
        throw new RuntimeException("we expected here correct type of processor");
    }

    public Node getNodeByListXpath(RuleDefPolicy ruleDefPolicy, Node checkedPolicy, Node allBeanPolicies){

        Node reference=null;
        for (String type: processors){
            String xpathReference = String.format("Bean[contains(@impl,\"%s\")]", ruleDefPolicy.type);
            // search within policy
            reference = helper.getReferenceInHierarchyOfPolicies(checkedPolicy, xpathReference, allBeanPolicies);
            if (reference != null){
                break;
            }
        }

        return reference;
    }
}
