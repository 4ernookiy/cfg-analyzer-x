package com.datalex.tdp.analyzer.jxpath;

import com.datalex.tdp.analyzer.config.Config;
import com.datalex.tdp.analyzer.text.RemoveInvisibleContent;
import com.datalex.tdp.analyzer.config.Utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.datalex.tdp.analyzer.data.CallableReadFile;
import com.datalex.tdp.analyzer.parse.BeanPoliciesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlTransform
{
    public static final String XML_PARENT = "<all></all>";
    private static final Logger logger = LoggerFactory.getLogger(XmlTransform.class);

    private DocumentBuilderFactory dbf;

    public XmlTransform()
    {
        dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
    }

    public Node parse(String xml)
    {
        DocumentBuilder docBuilder = null;
        Node records = null;
        try
        {

            docBuilder = dbf.newDocumentBuilder();
            InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
            records = docBuilder.parse(inputStream).getDocumentElement();
        }
        catch (ParserConfigurationException | SAXException | IOException e)
        {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        return records;
    }

    public String parse(Node node)
    {
        StringWriter sw = new StringWriter();
        try
        {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        }
        catch (TransformerException e)
        {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        return sw.toString();
    }

    public Node loadFile(Path pathToFile)
    {
        String strXml = Utils.readFile(pathToFile);
        Node contentOfFile = null;
        try
        {
            contentOfFile = parse(strXml);
        }
        catch (Exception e)
        {
            if (e.getCause() instanceof SAXParseException)
            {
                SAXParseException spe = (SAXParseException) e.getCause();
                if (spe.getLineNumber() == 1 && spe.getMessage().contains("is not allowed"))
                {
                    logger.warn(
                        "file contain invisible symbols in the beginning of file. try to clean and handle again :{}",
                        pathToFile);
                    strXml = new RemoveInvisibleContent().replace(strXml);
                    try
                    {
                        contentOfFile = parse(strXml);
                        logger.info("file was loaded after remove invisible symbols:{}", pathToFile);
                    }
                    catch (Exception e1)
                    {
                        logger.error("skip file:{}", pathToFile);
                        logger.error("cause why we skip:{}", e1);
                    }
                }
            }
            else
            {
                logger.error("skip file:{}", pathToFile);
                logger.error("cause why we skip:{}", e);
            }
        }
        return contentOfFile;
    }

    public Node mergeAllXmlFiles(List<Path> files)
    {

        Long start = System.currentTimeMillis();
        logger.debug("start load multi files:");
        ExecutorService executor = Executors.newFixedThreadPool(Config.THREAD_LOAD_DATA_COUNT);
        List<CallableReadFile> tasks = new ArrayList<>();
        for (Path p : files)
        {
            CallableReadFile crf = new CallableReadFile(p);
            tasks.add(crf);
            executor.submit(crf);
        }

        BeanPoliciesHelper helper = new BeanPoliciesHelper();
        Node mergedNode = parse(XML_PARENT);
        executor.shutdown();

        try
        {
            for (CallableReadFile task : tasks)
            {
                Node contentOfFile = task.call();
                if (null != contentOfFile)
                {
                    Node imported = mergedNode.getOwnerDocument().importNode(contentOfFile, true);
                    if (imported.getNodeType() == Node.ELEMENT_NODE)
                    {
                        Element eElement = (Element) imported;
                        String shortName = helper.getShortNameBean(task.getPath().toString(), null, File.separator);
                        eElement.setAttribute(Config.ATR_PATH, task.getPath().toString());
                        eElement.setAttribute(Config.ATR_NAME, shortName);
                    }
                    mergedNode.appendChild(imported);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Long end = System.currentTimeMillis();
        logger.debug("finished loading "+files.size()+" files:"+(end-start)+" ms");

        return mergedNode;
    }
}
