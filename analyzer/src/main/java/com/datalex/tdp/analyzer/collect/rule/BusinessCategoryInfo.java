package com.datalex.tdp.analyzer.collect.rule;

import com.datalex.tdp.analyzer.data.MenuItemVO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BusinessCategoryInfo
{
    public String serviceName;
    public Set<CategoryInfo> categories = new HashSet<>();
    public List<MenuItemVO> menuItems;

    public CategoryInfo getCategoryInfo(String nameCategory)
    {
        for (CategoryInfo category : categories)
        {
            if (category.name.equals(nameCategory))
            {
                return category;
            }
        }
        return null;
    }
}
