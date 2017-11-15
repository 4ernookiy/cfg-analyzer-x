package com.datalex.tdp.analyzer.debug;

import com.datalex.tdp.analyzer.collect.rule.BusinessCategoryInfo;
import com.datalex.tdp.analyzer.parse.ParseDataFromRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DemoApplicationRulesParse
{
    private static final Logger logger = LoggerFactory.getLogger(DemoApplicationRulesParse.class);

    public static void main(String[] args)
    {
        ParseDataFromRules parseDataFromRules = new ParseDataFromRules();
        Map<String, BusinessCategoryInfo> rulesData = parseDataFromRules.parseAndCollect();
        System.out.println("");

    }

}
