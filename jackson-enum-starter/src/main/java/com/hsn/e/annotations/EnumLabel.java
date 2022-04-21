package com.hsn.e.annotations;

import java.lang.annotation.*;

/**
 * 枚举标签,用于标记枚举的描述字段
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EnumLabel {
}
