package com.datalex.tdp.analyzer.config;

import com.datalex.tdp.analyzer.jxpath.XmlTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class Utils
{
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    private Utils()
    {
    }

    public static String readFile(Path pathToFile)
    {
        String content = new String();
        try
        {
            logger.trace("load file:{}", pathToFile);
            content = new String(Files.readAllBytes(pathToFile));
        }
        catch (IOException e)
        {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        return content;
    }

    private static List<Path> listFiles(Path path, String ext, boolean recursively)
    {
        List<Path> list = new ArrayList<>();
        if (ext.length() > 0 && !".".equals(ext.substring(0, 1)))
        {
            ext = "." + ext;
        }
        try
        {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path))
            {
                for (Path entry : stream)
                {
                    if (entry.toFile().isFile())
                    {
                        if (entry.toString().endsWith(ext))
                        {
                            list.add(entry);
                        }

                    }
                    else if (recursively && entry.toFile().isDirectory())
                    {
                        list.addAll(listFiles(entry, ext, recursively));
                    }
                }
            }
        }
        catch (IOException e)
        {
            logger.error(e.getLocalizedMessage());
            new RuntimeException(e);
        }
        return list;
    }

    public static List<Path> getBeanPoliciesFiles()
    {
        return listFiles(Paths.get(Config.PATH_BEANPOLICIES), Config.PATH_BEANPOLICIES_EXT, Config.PATH_BEANPOLICIES_RECURSIVELY);
    }

    public static List<Path> getRulesFiles()
    {
        return listFiles(Paths.get(Config.PATH_RULES), Config.PATH_RULES_EXT, Config.PATH_RULES_RECURSIVELY);
    }

    public static Node getAllRulesAsNode()
    {
        logger.debug("start loading rules");
        List<Path> rulesFiles = Utils.getRulesFiles();
        XmlTransform transformer = new XmlTransform();
        Node node = transformer.mergeAllXmlFiles(rulesFiles);
        logger.debug("ended loading rules");
        return node;
    }

    public static Path getMenusXml()
    {
        return Paths.get(Config.PATH_MENUSXML);
    }

    public static Node getMenusXmlAsNode(){
        Path menuxmlFile = Utils.getMenusXml();
        XmlTransform transformer = new XmlTransform();
        Node nodeMenusXml = transformer.loadFile(menuxmlFile);
        return nodeMenusXml;
    }

    public static Node getBeanPoliciesAsNode()
    {
        List<Path> files = Utils.getBeanPoliciesFiles();
        XmlTransform transformer = new XmlTransform();
        Node node = transformer.mergeAllXmlFiles(files);
        return node;
    }

}
