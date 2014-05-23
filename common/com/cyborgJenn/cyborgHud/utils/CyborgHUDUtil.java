package com.cyborgJenn.cyborgHud.utils;

import java.lang.reflect.Field;

public class CyborgHUDUtil {
	/**
     * Gets a protected/private field from a class using reflection.
     * @param <T> The return type of the field you are getting
     * @param <E> The class the field is in
     * @param classToAccess The ".class" of the class the field is in
     * @param instance The instance of the class
     * @param fieldNames comma seperated names the field may have (i.e. obfuscated, non obfuscated).
     * Obfustated field names can be found in fml/conf/fields.csv
     * @return
     */
    public static <T, E> T GetFieldByReflection(Class<? super E> classToAccess, E instance, String... fieldNames)
    {
		Field field = null;
		for(String fieldName : fieldNames)
		{
			try
			{
			     field = classToAccess.getDeclaredField(fieldName);
			}
			catch(NoSuchFieldException e){}
			
			if(field != null)
				break;
	    }
		
		if(field != null)
		{
			field.setAccessible(true);
		    T fieldT = null;
		    try
			{
		    	fieldT = (T) field.get(instance);
			}
		    catch (IllegalArgumentException e){}
		    catch (IllegalAccessException e){}
		
		    return fieldT;
		}
		
		return null;
    }
}
