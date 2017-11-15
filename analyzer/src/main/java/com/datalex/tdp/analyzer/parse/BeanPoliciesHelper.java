package com.datalex.tdp.analyzer.parse;

import com.datalex.tdp.analyzer.collect.BeanImpl;
import com.datalex.tdp.analyzer.collect.BeanImplType;
import com.datalex.tdp.analyzer.collect.PolicyLink;
import com.datalex.tdp.analyzer.collect.TdpReference;
import com.datalex.tdp.analyzer.collect.rule.GroupOfRule;
import com.datalex.tdp.analyzer.collect.rule.RuleDefPolicy;
import com.datalex.tdp.analyzer.config.TdpXpathExpression;
import com.datalex.tdp.analyzer.config.TdpXpathExpression.ATTR;
import com.datalex.tdp.analyzer.jxpath.XpathEvaluate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import java.util.List;

/**
 * Created by Dzmitry_Charnavoki on 11/17/2016.
 */
public class BeanPoliciesHelper
{
    private static final Logger logger = LoggerFactory.getLogger(BeanPoliciesHelper.class);

    private XpathEvaluate xpathEvaluate = new XpathEvaluate();

    public List<Node> getAllServices(final Node owner)
    {
        List<Node> list = xpathEvaluate.retrieveListByXpath(owner, TdpXpathExpression.ALL_BEANS);
        return list;
    }

    public Boolean isPolicy(Node node)
    {
        if ("Policy".equals(xpathEvaluate.getTagName(node)))
        {
            return true;
        }
        return false;

    }

    public String getShortNameSv(String homeImpl)
    {
        return getShortNameBean(homeImpl, "Home");
    }

    public String getShortNameBean(String remoteImpl)
    {
        return getShortNameBean(remoteImpl, null);
    }

    public String getShortNameBean(String remoteImpl, String suffix)
    {
        return getShortNameBean(remoteImpl, suffix, ".");
    }

    public String getShortNameBean(String homeImpl, String suffix, String delimetr)
    {
        int indexOfLastDot = homeImpl.lastIndexOf(delimetr);
        int indexHome;
        if (null != suffix)
        {
            indexHome = homeImpl.lastIndexOf(suffix);
        }
        else
        {
            indexHome = homeImpl.length();
        }
        String name = homeImpl.substring(indexOfLastDot + 1, indexHome);
        return name;
    }

    //    public List<String> getNamesBeans(final Node owner)
//    {
//        List<String> names = new ArrayList<>();
//        List<Node> beans = xpathEvaluate.retrieveListByXpath(owner, TdpXpathExpression.ALL_BEANS);
//        for (Node bean : beans)
//        {
//            String service = xpathEvaluate.getAttribute(bean, ATTR.REMOTE_IMPL);
//            names.add(service);
//        }
//        return names;
//    }
//
    public boolean isJEEService(Node beanJEEService)
    {
        String remoteImplValue = xpathEvaluate.getAttribute(beanJEEService, ATTR.REMOTE_IMPL);
        if ("".equals(remoteImplValue))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
//
//    private static String xpathForJump = "BeanPolicies/Policy[matches(@id,\'%s\')]/Bean/Reference[matches(@bean,\"%s\")]";
//    private static String xpathForJump2 = "Bean/Reference[@bean=\"%s\" and @policy=\"%s\"]";
//    public Node jumpOnTheLinks(String typeOfBean, String policyId, final Node allPolicies)
//    {
//        String tmp = TdpXpathExpression.insertIntoXpath(xpathForJump2, typeOfBean, policyId);
//        Node reference = xpathEvaluate.retrieveByXpath(allPolicies, tmp);
//        if (reference != null)
//        {
//            Node bean = reference.getParentNode();
////            String remoteImplValue = getAttribute(bean, ATR.REMOTE_IMPL);
//            if (isRefToService(reference)) {
//                // if attr remoteImpl exist - it is service.
//                return reference; // return reference to service
//            } else {
//                String type = xpathEvaluate.getAttribute(bean, ATTR.TYPE);
//                if ("".equals(type)){
//                    throw new RuntimeException("what is it ?");
//                }
//                Node policy = bean.getParentNode();
//                PolicyLink owner = getPolicyLInk(policy);
//                Node nextRef = jumpOnTheLinks(type, owner.id, allPolicies);
//                if (null == nextRef) {
//                    return reference;
//                } else {
//                    return nextRef;
//                }
//            }
//        }
//        return reference;
//    }

    public Node getReferenceInHierarchyOfPolicies(final Node checkedPolicy, String xpathReference, final Node allPolicies)
    {
        Node reference = xpathEvaluate.retrieveByXpath(checkedPolicy, xpathReference);
        String xPathStr = null;
        if (reference == null)
        {
            PolicyLink policy = getPolicyLInk(checkedPolicy);
            if (!"".equals(policy.extend))
            {
                xPathStr = "BeanPolicies/Policy[contains(@id,'" + policy.extend + "')]";
                Node extPolicy = xpathEvaluate.retrieveByXpath(checkedPolicy, xPathStr);
                if (extPolicy != null)
                {
                    return getReferenceInHierarchyOfPolicies(extPolicy, xpathReference, allPolicies);
                }
            }
        }
        return reference;
    }

    public Node retrievWithinHierarchy(final Node checkedPolicy, String xpathReference, final Node allPolicies)
    {
        Node reference = xpathEvaluate.retrieveByXpathFast(checkedPolicy, xpathReference);
        String xPathStr = null;
        if (reference == null)
        {
            PolicyLink policy = getPolicyLInk(checkedPolicy);
            if (!"".equals(policy.extend))
            {
                xPathStr = "BeanPolicies/Policy[contains(@id,'" + policy.extend + "')]";
                Node extPolicy = xpathEvaluate.retrieveByXpath(allPolicies, xPathStr);
                if (extPolicy != null)
                {
                    return retrievWithinHierarchy(extPolicy, xpathReference, allPolicies);
                }
            }
        }
        return reference;
    }

//    public Node getReferenceWithinAllPolicy(final Node checkedPolicy, String xpathReference, final Node allPolicies)
//    {
//        Node reference = xpathEvaluate.retrieveByXpath(checkedPolicy, xpathReference);
//        if (reference == null)
//        {
//            return xpathEvaluate.retrieveByXpath(allPolicies, xpathReference);
//        }
//        return reference;
//    }

    public PolicyLink getPolicyLInk(Node node)
    {
        PolicyLink policyLink = null;
        if (isPolicy(node))
        {
            policyLink = new PolicyLink();
            policyLink.id = xpathEvaluate.getAttribute(node, "id");
            policyLink.extend = xpathEvaluate.getAttribute(node, "extends");
        }
        else
        {
            throw new RuntimeException("not supported");
        }
        return policyLink;
    }

    public String getProperty(Node owner, String nameProperty)
    {
        String template = String.format("Property[@id=\'%s\']/@val", nameProperty);
        Node nodeNameProp = xpathEvaluate.retrieveByXpath(owner, template);
        return nodeNameProp == null ? "" : nodeNameProp.getNodeValue();
    }

    public RuleDefPolicy getRuleInfo(Node node, RuleDefPolicy ruleDefPolicy)
    {
        final Node rule = node.cloneNode(true);
        String typeOfRule = xpathEvaluate.getAttribute(rule, "type");
        String implOfRule = xpathEvaluate.getAttribute(rule, "impl");
//        Node serviceNameProp = xpathEvaluate.retrieveByXpath(rule, "Property[@id=\'serviceName\']/@val");
//        Node categoriesProp = xpathEvaluate.retrieveByXpath(rule, "Property[@id=\'categories\']/@val");
//        String categories = categoriesProp == null ? "" : categoriesProp.getNodeValue();
        String serviceName = getProperty(rule, "serviceName");
        String categories = getProperty(rule, "categories");

        ruleDefPolicy.impl = implOfRule;
        ruleDefPolicy.type = typeOfRule;
        ruleDefPolicy.serviceName = serviceName;
        ruleDefPolicy.categories = categories;
        ruleDefPolicy.groupOfRule = GroupOfRule.NotRecognized;

        return ruleDefPolicy;
    }

    public TdpReference getTdpReference(Node nodeReference)
    {
        TdpReference reference = new TdpReference();
        reference.id = xpathEvaluate.getAttribute(nodeReference, "id");
        reference.bean = xpathEvaluate.getAttribute(nodeReference, "bean");
        reference.policy = xpathEvaluate.getAttribute(nodeReference, "policy");
        return reference;
    }


    public String getCorrespondingBeanImplementation(Node processorsExecutor, String policyId, RuleDefPolicy ruleDefPolicy){
        String xpathForJEEService = "Bean[@remoteImpl]";
        Node clazzJEEService = xpathEvaluate.retrieveByXpathFast(processorsExecutor.getParentNode(), xpathForJEEService);
        String valueOfRemoteImpl;
        if (clazzJEEService == null){
            valueOfRemoteImpl = policyId;
            // todo defenition )))
//                            ruleDefPolicy.resolution = ManagerResolution.getWrongDefenition(policyWhereRuleIsUsed, ruleDefPolicy);
            ruleDefPolicy.groupOfRule = GroupOfRule.NotRecognized;
        } else {
            valueOfRemoteImpl = getBeanImplementation(clazzJEEService);
        }
        return valueOfRemoteImpl;
    }

    public String getBeanImplementation(Node node)
    {

        String s = null;// = xpathEvaluate.getAttribute(node, ATTR.REMOTE_IMPL);
//        if (isJEEService(node))
//        {
//        }
        s = xpathEvaluate.getAttribute(node, ATTR.IMPL);
        if (s == null || s.isEmpty())
        {
            throw new RuntimeException("not supported");
        }
        return s;
    }

    public BeanImpl getBeanInfo(Node node)
    {

        String impl = null;// = xpathEvaluate.getAttribute(node, ATTR.REMOTE_IMPL);
        BeanImpl bean = new BeanImpl();
        BeanImplType type = BeanImplType.JustPOJO;
        if (isJEEService(node))
        {
            type = BeanImplType.JEEService;
        }
        impl = xpathEvaluate.getAttribute(node, ATTR.IMPL);
        if (impl == null || impl.isEmpty())
        {
            throw new RuntimeException("not supported");
        }
        bean.impl = impl;
        bean.type = type;
        return bean;
    }

}
