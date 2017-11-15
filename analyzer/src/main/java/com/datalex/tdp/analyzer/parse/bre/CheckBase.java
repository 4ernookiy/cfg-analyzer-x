package com.datalex.tdp.analyzer.parse.bre;

import com.datalex.tdp.analyzer.collect.TdpServiceInfo;
import com.datalex.tdp.analyzer.collect.rule.RuleDefPolicy;
import com.datalex.tdp.analyzer.config.TdpXpathExpression;
import com.datalex.tdp.analyzer.jxpath.XpathEvaluate;
import com.datalex.tdp.analyzer.parse.BeanPoliciesHelper;
import com.datalex.tdp.analyzer.reporting.TimeExecution;
import org.w3c.dom.Node;

public abstract class CheckBase
{
    protected XpathEvaluate evaluate = new XpathEvaluate();
    protected BeanPoliciesHelper helper = new BeanPoliciesHelper();

    abstract public TdpServiceInfo search(Node xpathClient, Node allBeanPolicies);

    public String getProperty(Node owner, String nameProperty)
    {
        String template = String.format("Property[@id=\'%s\']/@val", nameProperty);
        Node nodeNameProp = evaluate.retrieveByXpath(owner, template);
        return nodeNameProp == null ? "" : nodeNameProp.getNodeValue();
    }



}
