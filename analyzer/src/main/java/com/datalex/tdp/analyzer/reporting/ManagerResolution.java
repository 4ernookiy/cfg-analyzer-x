package com.datalex.tdp.analyzer.reporting;

import com.datalex.tdp.analyzer.collect.PolicyLink;
import com.datalex.tdp.analyzer.collect.rule.RuleDefPolicy;

@Deprecated
public final class ManagerResolution
{
    public static Resolution OK_ALL_FINE;
//    public static Resolution ERR1_WRONG_DEFINITION;

    private ManagerResolution()
    {
    }

    static
    {
        Resolution resolution = new Resolution();
//        resolution.status = ProcessingStaus.Warning;
//        resolution.actual = "incorrect definition policyId not equal " + TdpXpathExpression.ATR.REMOTE_IMPL;
////                        resolution.expected = "";
//        resolution.explanation = "this mean that this bean is used in other policy, and this looks weird ";
//        resolution.errCode = "1";
//        ERR1_WRONG_DEFINITION = resolution;

        resolution = new Resolution();
        resolution.status = ProcessingStaus.Ok;
        resolution.actual = "all looks fine";
        resolution.explanation = "we didn't find any bugs";
//        resolution.errCode = "0";
        OK_ALL_FINE = resolution;

    }

    public static Resolution getWrongDefenition(PolicyLink owner, RuleDefPolicy ruleDefPolicy)
    {
        Resolution resolution = new Resolution();
        resolution.status = ProcessingStaus.Warning;
        resolution.actual = "it looks like incorrect definition bean of rule";
        String exp = String
            .format("this mean that this bean of Rule is used in policy %s, but was described in %s, and this looks weird",
                owner.id, ruleDefPolicy.policyWhereRuleIsDefined.id);
        resolution.explanation = exp;
        resolution.expected = "we expected that policy for service and rule will be the same";
//        resolution.errCode = "1";
//        ERR1_WRONG_DEFINITION = resolution;
        return resolution;
    }

    public static Resolution getNotFoundUsageForProcessor(RuleDefPolicy ruleDefPolicy)
    {
        Resolution resolution = new Resolution();
        resolution.status = ProcessingStaus.Useless;
        resolution.actual = "we found definition for this rule and processor. But processor doesn't use.";
        resolution.expected = "we expected that the processor must used";
        return resolution;
    }


    public static Resolution getNotFoundDefenition(RuleDefPolicy ruleDefPolicy)
    {
        Resolution resolution = new Resolution();
        resolution.status = ProcessingStaus.Warning;
        resolution.actual = "we haven't found any reference for this bean";
        String s = String
            .format("we suppose that this rule used in bean %s or in other bean or even is redundant", ruleDefPolicy.policyWhereRuleIsDefined.id);
        resolution.suppose = s;
        resolution.expected = "we expected that policy have to reference this bean";
//        ERR1_WRONG_DEFINITION = resolution;
        return resolution;
    }

    public static void addResolutionForRule(PolicyLink owner, RuleDefPolicy ruleDefPolicy)
    {
        String policyIdOwner = owner.id;
        if (policyIdOwner.equals(ruleDefPolicy.policyWhereRuleIsDefined.id))
        {
//                ruleDefPolicy.resolution = OK_ALL_FINE;
        }
        else
        {
            ruleDefPolicy.resolution = getWrongDefenition(owner, ruleDefPolicy);;
        }
    }

//    public static void addResolutionForEachRule(TdpServiceInfo tdpServiceInfo)
//    {
//
//        for (RuleDefPolicy ruleInfo : tdpServiceInfo.rules)
//        {
//            addResolutionForRule(tdpServiceInfo.policyOwnerService, ruleInfo);
//        }
//
//    }

}
