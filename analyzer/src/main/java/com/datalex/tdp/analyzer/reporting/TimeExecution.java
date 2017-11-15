package com.datalex.tdp.analyzer.reporting;

import org.slf4j.Logger;

public class TimeExecution
{
    private String tag;
    private Long start;
    private Long end;
    private Long total = -1l;
    private Long maxTime = -1l;

    public TimeExecution()
    {
        start = System.currentTimeMillis();
    }

    public TimeExecution(String tag, Long maxTime)
    {
        this();
        this.tag = tag;
        this.maxTime = maxTime;
    }

    public Long getTotal()
    {
        if (total == -1l)
        {
            end = System.currentTimeMillis();
            total = end - start;
        }
        return total;
    }

    public void logMessage(Logger logger, String message)
    {
        if (getTotal() > maxTime || maxTime == -1l)
        {
            logger.debug(message, tag+" :"+ maxTime+": "+ getTotal());
//            logger.debug(getStackTrace());
        }
    }
    private String getStackTrace(){
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder();
        for( StackTraceElement e: elements){
            sb.append(e.getMethodName());
            sb.append(":");
            sb.append(e.getLineNumber());
            sb.append("; ");
        }
        return sb.toString();
    }
}
