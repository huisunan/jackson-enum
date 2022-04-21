package com.hsn.e.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hsn.e.util.EnumInfo;
import com.hsn.e.util.EnumUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.annotation.Annotation;

@RequiredArgsConstructor
public class EnumSerializer extends JsonSerializer<Enum<?>> {

    /**
     * 开启枚举翻译
     */
    private final Boolean openLabelWrite;

    /**
     * 翻译出的字段格式
     */
    private final String format;

    private final Class<? extends Annotation> valueAnnotation;

    private final Class<? extends Annotation> labelAnnotation;


    @Override
    @SneakyThrows
    public void serialize(Enum<?> value, JsonGenerator gen, SerializerProvider serializers) {
        Class<?> aClass = value.getClass();
        EnumInfo enumInfo = EnumUtil.getEnumInfo(aClass, valueAnnotation, labelAnnotation);
        gen.writeObject(enumInfo.getValueMethod().invoke(value));
        if (openLabelWrite && enumInfo.getLabelMethod() != null) {
            JsonStreamContext outputContext = gen.getOutputContext();
            String currentName = outputContext.getCurrentName();
            String translateName = String.format(format, currentName);
            if (!outputContext.inArray()) {
                gen.writeObjectField(translateName, enumInfo.getLabelMethod().invoke(value));
            }
            //列表枚举序例化待处理
        }
    }
}
