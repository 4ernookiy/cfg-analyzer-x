package com.datalex.tdp.analyzer.debug;

import com.datalex.tdp.analyzer.api.Factory;
import com.datalex.tdp.analyzer.data.MenuItemVO;
import com.datalex.tdp.analyzer.parse.ParseDataFromMenusXml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.List;

public class DemoApplicationMenusXml
{
    private static final Logger logger = LoggerFactory.getLogger(DemoApplicationMenusXml.class);

    public static void main(String[] args)
    {
        List<MenuItemVO> list = Factory.getInstance().getMenuItems();
        System.out.println(list);

//        JAXBContext jaxbContext = null;
//        try
//        {
//            String s = "";
//            jaxbContext = JAXBContext.newInstance(MenuItemVO.class);
//            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//
//            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
////            jaxbMarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
//            StringWriter sw = new StringWriter();
//
//            jaxbMarshaller.marshal(list.get(0), sw);
//
//            String tmpString = sw.toString();
//
//            logger.info(tmpString);
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }

    }
}