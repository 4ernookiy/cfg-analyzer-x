package com.datalex.tdp.analyzer.collect.rule;

import java.util.ArrayList;
import java.util.List;

public class CategoryInfo
{
    public String name;
    public String executionOrder;

    public List<RuleInfo> rules = new ArrayList<>();

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CategoryInfo that = (CategoryInfo) o;

        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode()
    {
        return name != null ? name.hashCode() : 0;
    }
}
