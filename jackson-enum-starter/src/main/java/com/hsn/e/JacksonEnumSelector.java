package com.hsn.e;

import com.hsn.e.annotations.EnableJacksonEnum;
import com.hsn.e.util.AnnotationInfo;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.MultiValueMap;

public class JacksonEnumSelector implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        MultiValueMap<String, Object> attributes =
                importingClassMetadata.getAllAnnotationAttributes(EnableJacksonEnum.class.getName());
        if (attributes == null) {
            return;
        }
        BeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName(AnnotationInfo.class.getName());
        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
        propertyValues.add("labelAnnotation", attributes.getFirst("label"));
        propertyValues.add("valueAnnotation", attributes.getFirst("value"));
        registry.registerBeanDefinition("annotationInfo", beanDefinition);
    }

}
