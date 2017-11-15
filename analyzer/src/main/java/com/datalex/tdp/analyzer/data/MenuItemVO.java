package com.datalex.tdp.analyzer.data;

public class MenuItemVO
{
    public String displayFirstName;
    public String displaySecondName;
    public String serviceName;

    @Override
    public String toString()
    {
        return "\nMenuItem{" +
            "\n   serviceName='" + serviceName + '\'' +
            "\n   displayFirstName='" + displayFirstName + '\'' +
            "\n   displaySecondName='" + displaySecondName + '\'' +'}';
    }
}
