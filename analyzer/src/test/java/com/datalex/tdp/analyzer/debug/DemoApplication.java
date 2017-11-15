package com.datalex.tdp.analyzer.debug;

import com.datalex.tdp.analyzer.CollectAllInfo;
import com.datalex.tdp.analyzer.collect.Collected;
import com.datalex.tdp.analyzer.collect.TdpServiceInfo;
import com.datalex.tdp.analyzer.collect.rule.BusinessCategoryInfo;
import com.datalex.tdp.analyzer.collect.rule.RuleDefPolicy;
import com.datalex.tdp.analyzer.config.Messages;
import com.datalex.tdp.analyzer.data.MenuItemVO;
import com.datalex.tdp.analyzer.parse.ParseDataFromBeanPolicies;
import com.datalex.tdp.analyzer.parse.ParseDataFromMenusXml;
import com.datalex.tdp.analyzer.parse.ParseDataFromRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DemoApplication {
   private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

   public static void main(String[] args) {

      ParseDataFromBeanPolicies parseBeanPolicies = new ParseDataFromBeanPolicies();
      parseBeanPolicies.createStats();
//      Path menuxmlFile = Utils.getMenusXml();
//
//      List<Path> rulesFiles = Utils.getRulesFiles();
//      Node allRules = FactoryChecks.mergeAllXmlFiles(rulesFiles);
//      FillDataFromRules dataFromRules = new FillDataFromRules(allRules);
//      FactoryChecks.listRulesForChecks.add(dataFromRules);

//      List<Path> beanPoliciesFiles = Utils.getBeanPoliciesFiles();

//      Node allPolicies = Utils.getAllRulesAsNode()
//      getProcessors(allPolicies);
////        checkPolicyHaveOnlyOneServiceDefenition(allPolicies);
//      FactoryChecks.handleBenPolicy(allPolicies);
      ParseDataFromMenusXml parseMenus = new ParseDataFromMenusXml();
      List<MenuItemVO> menus = parseMenus.parseAndCollect();


      ParseDataFromRules parseDataFromRules = new ParseDataFromRules();
      Map<String, BusinessCategoryInfo> rulesData = parseDataFromRules.parseAndCollect();

      for (TdpServiceInfo srv : Collected.getStore().getInfo().values()) {
         for (RuleDefPolicy ruleDefPolicy : srv.xpathClients) {
            String serviceName = ruleDefPolicy.serviceName;
            BusinessCategoryInfo bre = rulesData.get(serviceName);
            ruleDefPolicy.categoriesInfo = bre;
            if (bre != null){
               List<MenuItemVO> lm = getMenu(serviceName, menus);
               bre.menuItems = lm;
            } else {
               System.out.println(serviceName);
            }
         }
      }



      List<TdpServiceInfo> list = new ArrayList<>(Collected.getStore().getInfo().values());
      Comparator<TdpServiceInfo> byName =
          (TdpServiceInfo o1, TdpServiceInfo o2) -> o1.remoteImpl.compareTo(o2.remoteImpl);
      Collections.sort(list, byName);
//        Map<String, Object> about = new HashMap<>();
//        about.put("serviceCount", list.size());
//        Messages.logJson(about);
      CollectAllInfo.logJson(list);
      System.out.println(Collected.getStore().getInfo().size());
   }

   private static List<MenuItemVO> getMenu(String serviceName, List<MenuItemVO> list){

      List<MenuItemVO> lm =new ArrayList<>();
      if ( null == serviceName || serviceName.isEmpty()){
         return lm;
      }
      for(MenuItemVO m: list){
         if (serviceName.equals(m.serviceName)){
            lm.add(m);
         }
      }
      return lm;
   }



}
