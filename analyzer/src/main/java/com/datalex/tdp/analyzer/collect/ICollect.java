package com.datalex.tdp.analyzer.collect;

import java.util.Map;

public interface ICollect
{
    TdpServiceInfo getByRemoteImpl(String remoteImpl);

    Map<String, TdpServiceInfo> getInfo();
//    void put(TdpServiceInfo tdpServiceInfo);

}
