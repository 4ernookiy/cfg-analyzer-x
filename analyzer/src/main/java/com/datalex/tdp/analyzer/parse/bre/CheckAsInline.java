package com.datalex.tdp.analyzer.parse.bre;

import com.datalex.tdp.analyzer.collect.BeanImpl;
import com.datalex.tdp.analyzer.collect.Collected;
import com.datalex.tdp.analyzer.collect.PolicyLink;
import com.datalex.tdp.analyzer.collect.TdpServiceInfo;
import com.datalex.tdp.analyzer.collect.rule.GroupOfRule;
import com.datalex.tdp.analyzer.collect.rule.RuleDefPolicy;
import com.datalex.tdp.analyzer.config.Config;
import com.datalex.tdp.analyzer.config.TdpXpathExpression;
import com.datalex.tdp.analyzer.reporting.TimeExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

public class CheckAsInline extends CheckBase
{
    private static final Logger logger = LoggerFactory.getLogger(CheckAsInline.class);
//    private XpathEvaluate evaluate = new XpathEvaluate();
//    private BeanPoliciesHelper helper = new BeanPoliciesHelper();

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
        PolicyLink policyWhereRuleIsDefine = helper.getPolicyLInk(checkedPolicy);
        ruleDefPolicy.policyWhereRuleIsDefined = policyWhereRuleIsDefine;

        ruleDefPolicy = helper.getRuleInfo(xpathClient, ruleDefPolicy);

        String xpathReference = TdpXpathExpression.getXpathForJEEService(ruleDefPolicy.type);
//        String.format("//Bean[@remoteImpl]/Reference[contains(@bean,\"%s\")]", ruleDefPolicy.type);

        // search within policy
        TimeExecution te = new TimeExecution("inline",20l);
        Node reference = helper.getReferenceInHierarchyOfPolicies(checkedPolicy, xpathReference, allBeanPolicies);
        te.logMessage(logger, "getReferenceInHierarchy - {}");
        if (null != reference)
        {
            // this mean that we found reference within our policy
            // and this mean that we found not empty remoteImpl - this correct definition inline rule for service
            // but impossible to check is it used  - this why PosibleInside
            Node bean = reference.getParentNode();
            PolicyLink policyWhereRuleIsUsed = helper.getPolicyLInk(bean.getParentNode());
            BeanImpl beanImpl = helper.getBeanInfo(bean);
            String valueOfRemoteImpl = beanImpl.impl;

//            evaluate.getAttribute(bean, TdpXpathExpression.ATTR.REMOTE_IMPL);

            tdpServiceInfo = Collected.getStore().getByRemoteImpl(valueOfRemoteImpl);
            tdpServiceInfo.policyOwnerService = policyWhereRuleIsUsed;
            ruleDefPolicy.groupOfRule = GroupOfRule.PosibleInside;
            tdpServiceInfo.xpathClients.add(ruleDefPolicy);
        }
        return tdpServiceInfo;
    }
}
