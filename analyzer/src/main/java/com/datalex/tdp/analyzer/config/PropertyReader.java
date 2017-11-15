package com.datalex.tdp.analyzer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class PropertyReader
{
    private static final Logger logger = LoggerFactory.getLogger(PropertyReader.class);

    private static Properties properties = new Properties();

    public static <T> T getProperty(String key, T defaultValue)
    {
        T value = null;
        String prop = properties.getProperty(key);
        if (prop != null)
        {
            if (defaultValue instanceof Boolean)
            {
                value = (T) Boolean.valueOf(prop);
            }
            if (defaultValue instanceof String || null == defaultValue)
            {
                value = (T) prop;
            }
            if (defaultValue instanceof Integer)
            {
                Integer v = (Integer) defaultValue;
                try
                {
                    v = Integer.parseInt(prop);
                } catch (NumberFormatException e){

                }
                value = (T) v ;
            }
        }
        else
        {
            value = defaultValue;
        }
        logger.info("pick up settings - {}: {}" , key, value);
        return value;
    }

    public static String getProperty(String key)
    {
        return getProperty(key, null);
    }

    public static Properties loadProperties(String nameFile)
    {
        Properties prop = new Properties();
        try (InputStream inputStream = PropertyReader.class.getClassLoader().getResourceAsStream(nameFile))
        {
            if (inputStream != null)
            {
                prop.load(inputStream);
                properties.putAll(prop);
            }
            else
            {
                throw new RuntimeException("property file '" + nameFile + "' not found in the classpath");
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return prop;
    }

}
