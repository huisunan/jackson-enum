package com.hsn.e.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@UtilityClass
public class EnumUtil {
    private final Map<String, EnumInfo> cacheMap = new ConcurrentHashMap<>();
    public static final String GET = "get";
    public static final String SET = "set";

    /**
     * 判断字段是不是枚举里列出的类型
     *
     * @param aClass class
     * @param field  字段
     * @return 结果
     */
    public boolean isEnumField(Class<?> aClass, Field field) {
        return aClass.equals(field.getDeclaringClass())
                && Modifier.isStatic(field.getModifiers())
                && Modifier.isFinal(field.getModifiers())
                && Modifier.isPublic(field.getModifiers());
    }

    @SneakyThrows
    public EnumInfo getEnumInfo(Class<?> aClass,
                                Class<? extends Annotation> valueAnnotation,
                                Class<? extends Annotation> labelAnnotation
    ) {
        String className = aClass.getName();
        if (!cacheMap.containsKey(className)) {
            Field[] declaredFields = aClass.getDeclaredFields();
            Field valueField = null, labelFiled = null;
            List<Field> staticFieldList = new ArrayList<>();
            for (Field declaredField : declaredFields) {
                if (declaredField.getAnnotation(valueAnnotation) != null) {
                    valueField = declaredField;
                }
                if (declaredField.getAnnotation(labelAnnotation) != null) {
                    labelFiled = declaredField;
                }
                if (EnumUtil.isEnumField(aClass, declaredField)) {
                    staticFieldList.add(declaredField);
                }
            }
            if (valueField == null || labelFiled == null) {
                log.error("not find @EnumValue field in class:{}", className);
                throw new RuntimeException("not find @EnumValue field in class");
            }
            String valueName = valueField.getName();
            Method valueMethod = aClass.getDeclaredMethod(GET + firstUp(valueName));
            String labelName = labelFiled.getName();
            Method labelMethod = aClass.getDeclaredMethod(GET + firstUp(labelName));

            cacheMap.put(className, new EnumInfo(valueMethod, labelMethod, staticFieldList, valueField, labelFiled));
        }
        return cacheMap.get(className);
    }

    public String firstUp(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


}
