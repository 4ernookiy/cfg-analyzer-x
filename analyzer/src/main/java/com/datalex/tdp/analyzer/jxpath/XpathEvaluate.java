package com.datalex.tdp.analyzer.jxpath;

import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.datalex.tdp.analyzer.collect.rule.RuleDefPolicy;
import com.datalex.tdp.analyzer.config.Config;
import com.datalex.tdp.analyzer.reporting.TimeExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XpathEvaluate
{
    private static final Logger logger = LoggerFactory.getLogger(XpathEvaluate.class);

    private XPathFactory xPathfactory = XPathFactory.newInstance();
    private XPath xpath = xPathfactory.newXPath();

    public Node retrieveByXpath(Node owner, String xPathStr)
    {
        TimeExecution te = new TimeExecution("retrive",2l);
        XPathExpression expression = null;
        Node node = null;
        try
        {
            expression = xpath.compile(xPathStr);
            node = (Node) expression.evaluate(owner, XPathConstants.NODE);
        }
        catch (XPathExpressionException e)
        {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        te.logMessage(logger, "time(one) - {} ms :" + xPathStr);
        return node;
    }
    public Node retrieveByXpathFast(Node owner, String xPathStr){
        Node cloned = owner.cloneNode(true);
        return retrieveByXpath(cloned, xPathStr);
    }

    public Node getNodeByListXpath(Node owner, String xPathTemplate, List<String> params){
        Node result=null;
        for (String param: params){
            String xpathReference = String.format(xPathTemplate, param);
            result = retrieveByXpathFast(owner, xpathReference);
            if (result != null){
                break;
            }
        }
        return result;
    }

    public List<Node> retrieveListByXpath(Node owner, String xPathStr)
    {
        TimeExecution te = new TimeExecution("retrieveList",50l);
        XPathExpression parentExpr = null;
        List<Node> nodes = new ArrayList<>();
        try
        {
            parentExpr = xpath.compile(xPathStr);
            NodeList parentNodes = (NodeList) parentExpr.evaluate(owner, XPathConstants.NODESET);
            for (int i = 0; i < parentNodes.getLength(); i++)
            {
                nodes.add(parentNodes.item(i));
            }
        }
        catch (XPathExpressionException e)
        {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        te.logMessage(logger, "time(list) - {} ms :" + xPathStr);
        return nodes;
    }

    public String getAttribute(Node owner, String idAttribute)
    {
        if (owner.getNodeType() == Node.ELEMENT_NODE)
        {
            return Element.class.cast(owner).getAttribute(idAttribute);
        }
        return null;
    }

    public String getTagName(Node owner)
    {
        if (owner.getNodeType() == Node.ELEMENT_NODE)
        {
            return Element.class.cast(owner).getTagName();
        }
        return null;
    }



}
