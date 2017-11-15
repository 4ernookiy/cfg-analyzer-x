package com.datalex.tdp.analyzer.api;

import com.datalex.tdp.analyzer.collect.TdpServiceInfo;
import com.datalex.tdp.analyzer.collect.rule.BusinessCategoryInfo;
import com.datalex.tdp.analyzer.data.MenuItemVO;

import java.util.List;
import java.util.Map;

public interface IFactory
{
    List<TdpServiceInfo> getInfo();

    List<MenuItemVO> getMenuItems();

    Map<String, BusinessCategoryInfo> getRulesInfo();

    boolean applyConfig(String path);
}
