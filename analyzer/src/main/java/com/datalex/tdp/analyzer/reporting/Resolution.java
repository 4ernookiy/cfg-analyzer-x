package com.datalex.tdp.analyzer.reporting;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Resolution
{
    public ProcessingStaus status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String actual;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String expected;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String explanation;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String suppose;
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    public String errCode;
}
