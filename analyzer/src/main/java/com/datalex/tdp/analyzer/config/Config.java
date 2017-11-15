package com.datalex.tdp.analyzer.config;

import com.datalex.tdp.analyzer.api.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static com.datalex.tdp.analyzer.config.PropertyReader.getProperty;

public class Config
{
    private static final Logger logger = LoggerFactory.getLogger(PropertyReader.class);

    static
    {
        if (Factory.necessaryToLoadConfig){
            logger.info("Try to load You can see all settings in {}", Config.class.getCanonicalName());
            PropertyReader.loadProperties("application.properties");
        }
        logger.info("You can see all settings in {}", Config.class.getCanonicalName());
    }

    public final static String APP_DELIMETR = ",";

//    public final static String PATH_CONFIG = getProperty("path.tdp.config");
//    public final static String PATH_OUTPUT_JAR = getProperty("path.tdp.output.jar");
//    public final static String PATH_OUTPUT_EJB = getProperty("path.tdp.output.ejb");
    public final static String PATH_MENUSXML = getProperty("path.tdp.menusxml");
    public final static String PATH_BEANPOLICIES = getProperty("path.tdp.beanpolicies");
    public final static String PATH_BEANPOLICIES_EXT = getProperty("path.tdp.beanpolicies.ext", "cfg");
    public final static Boolean PATH_BEANPOLICIES_RECURSIVELY = getProperty("path.tdp.beanpolicies.recursively", true);
    public final static String PATH_RULES = getProperty("path.tdp.rules");
    public final static String PATH_RULES_EXT = getProperty("path.tdp.rules.ext", "rules");
    public final static Boolean PATH_RULES_RECURSIVELY = getProperty("path.tdp.xpathClients.recursively", true);

    public final static Integer THREAD_LOAD_DATA_COUNT = getProperty("parsingapp.thread.count", 13);

    public final static List<String> TDP_PRE_PROCESSORS = Arrays
        .asList(getProperty("tdp.beanpolicies.processors.pre", "PreProcessorManager").split(APP_DELIMETR));

    public final static List<String> TDP_POST_PROCESSORS = Arrays
        .asList(getProperty("tdp.beanpolicies.processors.post", "PostProcessorManager").split(APP_DELIMETR));

    public final static List<String> TDP_IMPL_PROCESSORS = Arrays
        .asList(getProperty("tdp.beanpolicies.processors.impl",
            "GenericProcessorManagerImpl,ProcessorManagerStatsImpl,ProcessorManagerImpl,ProcessorManagerProcessor")
            .split(APP_DELIMETR));

    public final static List<String> TDP_IMPL_PROCESSORS_FOR_CALL_JXPATHCLIENT = Arrays
        .asList(getProperty("tdp.beanpolicies.processors.xpath",
            "JXPathRulesProcessor,JXPathRulesPostProcessor,JXPathRulesPreProcessor,JXPathRulesProcessorBase")
            .split(APP_DELIMETR));


    public final static String ATR_NAME = "name";
    public final static String ATR_PATH = "path";


}
