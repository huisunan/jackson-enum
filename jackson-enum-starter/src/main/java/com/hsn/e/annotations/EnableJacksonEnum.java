package com.hsn.e.annotations;

import com.hsn.e.JacksonEnumAutoConfiguration;
import com.hsn.e.JacksonEnumSelector;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({JacksonEnumSelector.class, JacksonEnumAutoConfiguration.class})
public @interface EnableJacksonEnum {
    /**
     * 枚举值注解
     */
    Class<? extends Annotation> value();

    /**
     * 枚举描述注解
     */
    Class<? extends Annotation> label() default EnumLabel.class;
}
