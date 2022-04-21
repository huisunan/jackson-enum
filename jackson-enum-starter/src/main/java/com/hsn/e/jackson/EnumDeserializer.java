package com.hsn.e.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.hsn.e.util.EnumInfo;
import com.hsn.e.util.EnumUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 枚举反序列化
 */
@Slf4j
@RequiredArgsConstructor
public class EnumDeserializer extends JsonDeserializer<Enum<?>> {

    private final Class<? extends Annotation> valueAnnotation;
    private final Class<? extends Annotation> labelAnnotation;

    private static final Map<String, Class<?>> cacheMap = new ConcurrentHashMap<>();

    public static Class<?> getCache(Class<?> aClass, String currentName) {
        return getCache(aClass, currentName, false);
    }

    public static Class<?> getCache(Class<?> aClass, String currentName, boolean isList) {
        String key = aClass.getName() + "#" + currentName;
        if (!cacheMap.containsKey(key)) {
            Method method = Arrays.stream(aClass.getMethods()).filter(i -> i.getName().equals(EnumUtil.SET + EnumUtil.firstUp(currentName))).findFirst().orElse(null);
            Class<?> resClass = null;
            if (method != null) {
                if (isList) {
                    Type[] genericParameterTypes = method.getGenericParameterTypes();
                    if (genericParameterTypes.length > 0) {
                        ParameterizedType genericParameterType = (ParameterizedType) genericParameterTypes[0];
                        Type[] actualTypeArguments = genericParameterType.getActualTypeArguments();
                        if (actualTypeArguments.length > 0) {
                            resClass = (Class<?>) actualTypeArguments[0];
                        }
                    }
                } else {
                    Class<?>[] parameterTypes;
                    if ((parameterTypes = method.getParameterTypes()).length > 0) {
                        resClass = parameterTypes[0];
                    }
                }
            }
            cacheMap.put(key, resClass);
        }
        return cacheMap.get(key);
    }

    @SneakyThrows
    @Override
    public Enum<?> deserialize(JsonParser p, DeserializationContext ctxt) {
        JsonStreamContext parsingContext = p.getParsingContext();
        String currentName;
        Class<?> enumClass;
        if (parsingContext.inArray()) {
            JsonStreamContext parent = parsingContext.getParent();
            Object currentValue = parent.getCurrentValue();
            Class<?> aClass = currentValue.getClass();
            currentName = parent.getCurrentName();
            enumClass = getCache(aClass, currentName, true);
        } else {
            currentName = parsingContext.getCurrentName();
            Class<?> aClass = parsingContext.getCurrentValue().getClass();
            enumClass = getCache(aClass, currentName);
        }
        if (enumClass == null) {
            log.warn("not found {}", currentName);
            return null;
        }

        EnumInfo enumInfo = EnumUtil.getEnumInfo(enumClass,valueAnnotation,labelAnnotation);
        Class<?> type = enumInfo.getValueField().getType();
        Object currentValue;
        if (Integer.class.equals(type)) {
            currentValue = p.getValueAsInt();
        } else if (Double.class.equals(type)) {
            currentValue = p.getValueAsDouble();
        } else if (Boolean.class.equals(type)) {
            currentValue = p.getValueAsBoolean();
        } else if (Long.class.equals(type)) {
            currentValue = p.getValueAsLong();
        } else if (String.class.equals(type)) {
            currentValue = p.getValueAsString();
        } else {
            String text = p.getText();
            //尝试获取valueOf方法
            Method valueOf = type.getDeclaredMethod("valueOf", String.class);
            currentValue = valueOf.invoke(null, text);
        }
        for (Field staticField : enumInfo.getStaticFields()) {
            Enum<?> o = (Enum<?>) staticField.get(null);
            Object value = enumInfo.getValueMethod().invoke(o);
            if (value.equals(currentValue)) {
                return o;
            }
        }
        return null;

    }
}
