package com.datalex.tdp.analyzer.debug;

import com.datalex.tdp.analyzer.CollectAllInfo;
import com.datalex.tdp.analyzer.collect.Collected;
import com.datalex.tdp.analyzer.collect.TdpServiceInfo;
import com.datalex.tdp.analyzer.collect.rule.BusinessCategoryInfo;
import com.datalex.tdp.analyzer.collect.rule.RuleDefPolicy;
import com.datalex.tdp.analyzer.config.Utils;
import com.datalex.tdp.analyzer.data.MenuItemVO;
import com.datalex.tdp.analyzer.jxpath.XmlTransform;
import com.datalex.tdp.analyzer.jxpath.XpathEvaluate;
import com.datalex.tdp.analyzer.parse.ParseDataFromBeanPolicies;
import com.datalex.tdp.analyzer.parse.ParseDataFromMenusXml;
import com.datalex.tdp.analyzer.parse.ParseDataFromRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DemoApplication2
{
    private static final Logger logger = LoggerFactory.getLogger(DemoApplication2.class);

    public static void main(String[] args)
    {
        Path tmplPath = Paths.get("c:\\git.my\\cfg-analyzer-x\\analyzer\\tmplt\\curn.xml");
        String tmplStr = Utils.readFile(tmplPath);

        Path posPath = Paths.get("c:\\git.my\\cfg-analyzer-x\\analyzer\\tmplt\\PointOfSaleManageSvRQ_LH_A-LON_MKI-K_BY.xml");
        String posStr = Utils.readFile(posPath);

        Document posNode = parse(posStr);
//        Node posNode = posDoc;
        XmlTransform xt = new XmlTransform();
        Node tmplNode = xt.loadFile(tmplPath);
//        Node clonedNode = tmplNode.cloneNode(true);

        XpathEvaluate evaluate = new XpathEvaluate();
        Node nodeLS = evaluate.retrieveByXpath(posNode, "//PointOfSale/UISettings/LocaleSettings");

        if (nodeLS != null)
        {
            String currency = evaluate.getAttribute(nodeLS, "DefaultCurrency");
            if (currency.equals("EUR"))
            {
                setAttribute(nodeLS, "DefaultCurrency", "BYN");
            }
            String usStr = xt.parse(nodeLS);
            System.out.println(usStr);
        }
        else
        {
            Node nodePOS = evaluate.retrieveByXpath(posNode, "//PointOfSale");
            if (nodePOS != null)
            {
                posNode.importNode(tmplNode, true);
            }
        }

        String newContent = xt.parse(posNode);

        System.out.println(newContent);
    }

    public static void setAttribute(Node owner, String idAttribute, String value)
    {
        if (owner.getNodeType() == Node.ELEMENT_NODE)
        {
            Element.class.cast(owner).setAttribute(idAttribute, value);
        }
    }

    public static Document parse(String xml)
    {
        DocumentBuilderFactory dbf;

        dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);

        DocumentBuilder docBuilder = null;
        Document records = null;
        try
        {

            docBuilder = dbf.newDocumentBuilder();
            InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
            records = docBuilder.parse(inputStream);
        }
        catch (ParserConfigurationException | SAXException | IOException e)
        {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        return records;
    }
}
