package com.datalex.tdp.analyzer.collect.rule;

import com.datalex.tdp.analyzer.collect.PolicyLink;
import com.datalex.tdp.analyzer.collect.TdpReference;
import com.datalex.tdp.analyzer.reporting.Resolution;
import com.fasterxml.jackson.annotation.JsonInclude;

public class RuleDefPolicy
{
    public PolicyLink policyWhereRuleIsDefined;

    public String impl;
    public String type;
    public String serviceName;
    public String categories;
    public GroupOfRule groupOfRule;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Resolution resolution;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public TdpReference lastRef;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public BusinessCategoryInfo categoriesInfo;

}
