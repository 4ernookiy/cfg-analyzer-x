package com.datalex.tdp.analyzer.collect;

import java.util.HashMap;
import java.util.Map;

public class Collected implements ICollect
{
    private Map<String, TdpServiceInfo> info = new HashMap<>();

    private static final ICollect INSTANCE = new Collected();

    public static final ICollect getStore()
    {
        return INSTANCE;
    }


    private Collected()
    {
    }

    @Override
    public TdpServiceInfo getByRemoteImpl(String remoteImpl)
    {
        if (remoteImpl == null || remoteImpl.length()==0){
            throw new RuntimeException("not supported");
        }
        TdpServiceInfo tdpServiceInfo = info.get(remoteImpl);

        if (tdpServiceInfo == null)
        {
            tdpServiceInfo = new TdpServiceInfo(remoteImpl);
            info.put(remoteImpl, tdpServiceInfo);
        }
        return tdpServiceInfo;
    }

    @Override
    public Map<String, TdpServiceInfo> getInfo()
    {
        return info;
    }

//    @Override
    public void put(TdpServiceInfo tdpServiceInfo)
    {
        if (tdpServiceInfo != null)
        {
            info.put(tdpServiceInfo.remoteImpl, tdpServiceInfo);
        }

    }
}
