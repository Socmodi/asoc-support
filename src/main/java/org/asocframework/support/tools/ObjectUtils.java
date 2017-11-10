package org.asocframework.support.tools;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ObjectUtils {
	private ObjectUtils() {
	}


	private static Map<String,BeanContext> contexts = new HashMap<String,BeanContext>();



	/**
	 *
	 * @param paramStr
	 * @param clazz
	 * @param <T>
     * @return
     */
	public static <T> T paramStr2Bean(String paramStr,Class<T> clazz) {
		BeanContext context = contexts.get(clazz.getName());
		try{
			if(context==null){
				context = fillBeanContext(clazz);
				contexts.put(clazz.getName(),context);
			}
			T obj = clazz.newInstance();
			String[] kvArr =  paramStr.split("&");
			for(int index=0;index<kvArr.length;index++){
				setValue(kvArr[index],context,obj);
			}
			return obj;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static BeanContext fillBeanContext(Class clazz) throws IntrospectionException {
		BeanContext context = new BeanContext(clazz.getName(),clazz);
		Field[] fields =  clazz.getDeclaredFields();
		for(int index = 0;index<fields.length;index++){
			Field field =  fields[index];
			context.getFieldCache().put(field.getName(),field);
		}
		return context;
	}

	public static void setValue(String kv,BeanContext context,Object obj) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, IntrospectionException {
		int  index =  kv.indexOf("=");
		String key = kv.substring(0,index);
		String value = kv.substring(index+1);
		Field field = context.getFieldCache().get(key);
		if(field!=null){
			FieldUtils.writeField(field,obj,trans(value,field),true);
		}
	}

	public static Object trans(String value,Field field){
		Class type = field.getType();
		if(String.class.isAssignableFrom(type)) {
			return  value;
		} else if(Date.class.isAssignableFrom(type)) {
			Date date = null;
			try {
				if(value.length()==14){
					date = DateUtils.parse(value);
				}else if(value.length()==19){
					date = DateUtils.parseToSecond(value);
				}
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
			return date;
		} else if(Boolean.class.isAssignableFrom(type)) {
			Boolean bool=true;

			if("false".equals(value)) {
				bool=false;
			}
			return bool;
		} else if(Integer.class.isAssignableFrom(type)) {
			return Integer.valueOf(value);
		} else if(Float.class.isAssignableFrom(type)) {
			return Float.valueOf(value);
		} else if(Long.class.isAssignableFrom(type)) {
			return Long.valueOf(value);
		} else if(Double.class.isAssignableFrom(type)) {
			return Double.valueOf(value);
		}
		return null;
	}

	public static boolean equal(Object before, Object after) {

		return before.hashCode()==after.hashCode();

	}

	/**
	 * 转化bean上下文，使用本地存储
	 */
	static class BeanContext{

		private String beanName;

		private Map<String,Field> fieldCache = new HashMap<String,Field> ();

		private Class clazz;

		public BeanContext(String beanName, Class clazz) {
			this.beanName = beanName;
			this.clazz = clazz;
		}

		public String getBeanName() {
			return beanName;
		}

		public void setBeanName(String beanName) {
			this.beanName = beanName;
		}

		public Class getClazz() {
			return clazz;
		}

		public void setClazz(Class clazz) {
			this.clazz = clazz;
		}

		public Map<String, Field> getFieldCache() {
			return fieldCache;
		}

		public void setFieldCache(Map<String, Field> fieldCache) {
			this.fieldCache = fieldCache;
		}
	}

}
