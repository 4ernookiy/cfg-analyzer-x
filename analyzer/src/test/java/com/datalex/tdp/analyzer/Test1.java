package com.datalex.tdp.analyzer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test1
{

    public static void main(String[] args)
        throws Exception
    {

        List l1 = new ArrayList();
        l1.add("test1");
        List l2 = l1;
        l1 = null;



        // Вытаскиваем IntegerCache через reflection
        Class usf = Class.forName("sun.misc.Unsafe");
        Field unsafeField = usf.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);
        Class<?> clazz = Class.forName("java.lang.Integer$IntegerCache");
        Field field = clazz.getDeclaredField("cache");
        Integer[] cache = (Integer[]) unsafe.getObject(unsafe.staticFieldBase(field), unsafe.staticFieldOffset(field));

        // Переписываем Integer cache
        for (int i = 0; i < cache.length; i++)
        {
            cache[i] = new Integer(
                new Random().nextInt(cache.length));
        }

        // Проверяем рандомность!
        for (int i = 0; i < 10; i++)
        {
            System.out.println(i + " - " + (Integer) i);
        }
    }
}
