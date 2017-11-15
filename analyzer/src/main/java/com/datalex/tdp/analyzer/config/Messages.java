package com.datalex.tdp.analyzer.config;

import com.datalex.tdp.analyzer.collect.TdpServiceInfo;
import com.datalex.tdp.analyzer.collect.rule.GroupOfRule;
import com.datalex.tdp.analyzer.collect.rule.RuleDefPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Messages
{
    private Messages()
    {
    }

    public static final String ERR_SERVICE_NOT_EXIST = "service not exist";
    public static final String ERR_1 = "err 1: we didn't find reference for type %s in policy %s ; possible inline rule with bad definition: in class file is used (JXPathRulesClient) BeanFactory.create(\"RBDFilterRulesClient\", \"AirChangeFlightSearchSv\"); but need like this (JXPathRulesClient) getReference(\"AirDetailsRulesClient\");";
    public static final String ERR_2 = "err 2: bad reference: it looks redundant";
    public static final String ERR_3_INCORECT_DEFINITION = "err 3: incorrect definition: policyId not equal homneImpl: this mean that this bean is used in other policy";

    private static final Logger csvErrror = LoggerFactory.getLogger("com.datalex.policy.csv.error");
    private static final Logger csvRulesStats = LoggerFactory.getLogger("com.datalex.policy.stats.csv");
    private static final Logger json = LoggerFactory.getLogger("com.datalex.policy.stats.json");
    private static final Logger logger = LoggerFactory.getLogger(Messages.class);
    private static final String DELIMETER_CSV = ";";

    static
    {
        csvErrror.info("sep=;");
        csvErrror.info("policy.id;policy.extends;type;groupOfRule;impl;serviceName;categories");
        csvRulesStats.info("sep=;");
//        csvRulesStats.info("idPolicy;extendPolicy;typeOfBean;TypeOfRule;additional");
    }

    private static String getCsv(String idPolicy, String extendPolicy, String typeOfBean, String someData, String... additional)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(idPolicy).append(DELIMETER_CSV);
        sb.append(extendPolicy).append(DELIMETER_CSV);
        sb.append(typeOfBean).append(DELIMETER_CSV);
        sb.append(someData).append(DELIMETER_CSV);
        for (String data : additional)
        {
            sb.append(data).append(DELIMETER_CSV);
        }
        return sb.toString();
    }

    private static String getCsv(RuleDefPolicy ri)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(ri.policyWhereRuleIsDefined.id).append(DELIMETER_CSV);
        sb.append(ri.policyWhereRuleIsDefined.extend).append(DELIMETER_CSV);
        sb.append(ri.type).append(DELIMETER_CSV);
        sb.append(ri.groupOfRule).append(DELIMETER_CSV);
        sb.append(ri.impl).append(DELIMETER_CSV);
        sb.append(ri.serviceName).append(DELIMETER_CSV);
        sb.append(ri.categories).append(DELIMETER_CSV);
        if (ri.resolution != null){
            sb.append(ri.resolution.status).append(DELIMETER_CSV);
            sb.append(ri.resolution.actual).append(DELIMETER_CSV);
            sb.append(ri.resolution.expected).append(DELIMETER_CSV);
            sb.append(ri.resolution.explanation).append(DELIMETER_CSV);
            sb.append(ri.resolution.suppose).append(DELIMETER_CSV);
        }
        return sb.toString();
    }

    private static String getCsv(TdpServiceInfo serviceInfo)
    {
        StringBuilder sb = new StringBuilder();

        for (RuleDefPolicy ri : serviceInfo.xpathClients)
        {
            sb.append(serviceInfo.policyOwnerService.id).append(DELIMETER_CSV);
            sb.append(serviceInfo.policyOwnerService.extend).append(DELIMETER_CSV);
            sb.append(serviceInfo.remoteImpl).append(DELIMETER_CSV);

            sb.append(getCsv(ri));
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void addStats(TdpServiceInfo serviceInfo)
    {
        csvRulesStats.info(getCsv(serviceInfo));
    }

    public static void addError(String msg, RuleDefPolicy ruleDefPolicy)
    {
        csvErrror.info(msg + ";" + getCsv(ruleDefPolicy));
    }

    public static void addError(TdpServiceInfo serviceInfo)
    {
        csvErrror.info(getCsv(serviceInfo));
    }

    public static void addError(String idPolicy, String extendPolicy, String typeOfBean, String problemWithIt, String... additional)
    {
        csvErrror.info(getCsv(idPolicy, extendPolicy, typeOfBean, problemWithIt, additional));
    }

    public static void addStats(String idPolicy, String extendPolicy, String typeOfBean, GroupOfRule typeOfRule,
        String... additional)
    {
        csvRulesStats.info(getCsv(idPolicy, extendPolicy, typeOfBean, typeOfRule.toString(), additional));
    }

    public static String getMessage(String message, Object... value)
    {
        return String.format(message, value);
    }



}
