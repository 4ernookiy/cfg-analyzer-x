package com.datalex.tdp.analyzer;

import com.datalex.tdp.analyzer.api.Factory;
import com.datalex.tdp.analyzer.collect.TdpServiceInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CollectAllInfo
{
   private static final Logger logger = LoggerFactory.getLogger(CollectAllInfo.class);
   private static final Logger json = LoggerFactory.getLogger("com.datalex.policy.stats.json");


   public static void main(String[] args) {
      Factory.getInstance().applyConfig("application.properties");
//      PropertyReader.loadProperties("application.properties");
//      PropertyReader.loadProperties("application2.properties");
//      logger.info("{}", PropertyReader.getProperty("path.tdp.beanpolicies"));


      logger.info("collecting all info. wait....");
      List<TdpServiceInfo> list = Factory.getInstance().getInfo();
      logger.info("save to log");
      logJson(list);
   }
   private static ObjectMapper mapper = new ObjectMapper();

   public static void logJson(Object obj)
   {
      String jsonStr = null;
      try
      {
         jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
         json.info(jsonStr);
      }
      catch (JsonProcessingException e)
      {
         logger.error(e.getLocalizedMessage());
      }

   }
}
