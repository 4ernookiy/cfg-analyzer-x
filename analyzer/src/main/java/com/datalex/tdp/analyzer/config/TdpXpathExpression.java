package com.datalex.tdp.analyzer.config;

public class TdpXpathExpression
{

    /**
     * get all bean
     * <Policy
     * <Bean impl="com.datalex.servants.processors.ProcessorManagerStatsImpl"
     */
    public final static String ALL_BEANS = "BeanPolicies/Policy/Bean[@remoteImpl]";

    /**
     * get all bean that attribute impl match JXPathRulesClient
     * <Bean impl="com.datalex.matrix.xpathClients.jxpath.JXPathRulesClient"
     */
    public final static String ALL_RULES = "BeanPolicies/Policy/Bean[matches(@impl,\'JXPathRulesClient\')]";
    public final static String ALL_PROC_STAT = "BeanPolicies/Policy/Bean[contains(@impl,\'com.datalex.servants.processors\')]";
    private final static String BEAN_AS_SERVICE = "Bean[@remoteImpl]/Reference[contains(@bean,\"%s\")]";
    private final static String BEAN = "Bean/Reference[matches(@bean,\"%s\")]";

    public static String insertIntoXpath(String xpath, Object... typeOfBean)
    {
        return String.format(xpath, typeOfBean);
    }

    public static String getXpathForJEEService(String typeOfBean)
    {
        return String.format(BEAN_AS_SERVICE, typeOfBean);
    }

    /**
     * Attributes
     */
    public interface ATTR
    {

        String TYPE = "type";
        String IMPL = "impl";
        String REMOTE_IMPL = "remoteImpl";
        String SERVICE_NAME = "serviceName";
        String POINT_OF_SALE = "pointOfSale";
        String NAME = "name";
        String EXECUTION_ORDER = "executionOrder";

    }

}
