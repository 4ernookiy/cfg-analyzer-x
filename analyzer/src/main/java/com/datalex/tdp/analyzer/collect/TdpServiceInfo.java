package com.datalex.tdp.analyzer.collect;

import com.datalex.tdp.analyzer.collect.rule.RuleDefPolicy;

import java.util.ArrayList;
import java.util.List;

public class TdpServiceInfo
{


    public String remoteImpl;

    public PolicyLink policyOwnerService;

    public List<RuleDefPolicy> xpathClients = new ArrayList<>();

    protected TdpServiceInfo(String remoteImpl)// only Collected can create instance
    {
        this.remoteImpl = remoteImpl;
    }

    private TdpServiceInfo() // only Collected can create instance
    {
    }
}
