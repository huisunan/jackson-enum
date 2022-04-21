package com.hsn.e.web;

import com.hsn.e.util.EnumInfo;
import com.hsn.e.util.EnumUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.lang.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 适配get方法入参
 */
@RequiredArgsConstructor
public class EnumConvertFactory implements ConverterFactory<String, Enum<?>> {

    private final Class<? extends Annotation> valueAnnotation;

    private final Class<? extends Annotation> labelAnnotation;

    @Override
    public <T extends Enum<?>> Converter<String, T> getConverter(@NonNull Class<T> aClass) {
        return new EnumConverter<>(aClass,valueAnnotation,labelAnnotation);
    }

    @RequiredArgsConstructor
    static class EnumConverter<T extends Enum<?>> implements Converter<String, T> {

        private final Class<T> tClass;

        private final Class<? extends Annotation> valueAnnotation;

        private final Class<? extends Annotation> labelAnnotation;


        @Override
        @SneakyThrows
        public T convert(String s) {
            EnumInfo enumInfo = EnumUtil.getEnumInfo(tClass,valueAnnotation,labelAnnotation);
            Field valueField = enumInfo.getValueField();
            Class<?> type = valueField.getType();
            Object value;
            if (String.class.equals(type)) {
                value = s;
            } else {
                Method valueOf = type.getDeclaredMethod("valueOf", String.class);
                value = valueOf.invoke(null, s);
            }
            for (Field staticField : enumInfo.getStaticFields()) {
                @SuppressWarnings("unchecked")
                T enumValue = (T) staticField.get(null);
                if (Objects.equals(value, enumInfo.getValueMethod().invoke(enumValue))) {
                    return enumValue;
                }
            }
            return null;
        }
    }
}
