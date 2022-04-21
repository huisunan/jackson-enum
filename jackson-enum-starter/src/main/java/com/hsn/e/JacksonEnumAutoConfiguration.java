package com.hsn.e;

import com.hsn.e.annotations.EnableJacksonEnum;
import com.hsn.e.jackson.EnumDeserializer;
import com.hsn.e.jackson.EnumSerializer;
import com.hsn.e.util.AnnotationInfo;
import com.hsn.e.web.EnumConvertFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@AutoConfigureBefore(JacksonAutoConfiguration.class)
@EnableConfigurationProperties(JacksonEnumProperties.class)
public class JacksonEnumAutoConfiguration implements WebMvcConfigurer {

    private final JacksonEnumProperties jacksonEnumProperties;

    private final AnnotationInfo annotationInfo;

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer objectMapper() {
        //此方法会替换Jackson自带的枚举序例化和反序列化方法
        return builder -> {
            builder.deserializerByType(Enum.class, new EnumDeserializer(annotationInfo.getValueAnnotation(), annotationInfo.getLabelAnnotation()));
            builder.serializerByType(
                    Enum.class,
                    new EnumSerializer(
                            jacksonEnumProperties.getOpenLabelWrite(),
                            jacksonEnumProperties.getFormat(),
                            annotationInfo.getValueAnnotation(),
                            annotationInfo.getLabelAnnotation()
                    )
            );
        };
    }

    /**
     * 适配Get方法入参转换成枚举
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new EnumConvertFactory(annotationInfo.getValueAnnotation(), annotationInfo.getLabelAnnotation()));
    }

}
