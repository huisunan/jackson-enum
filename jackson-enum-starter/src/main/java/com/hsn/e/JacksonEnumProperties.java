package com.hsn.e;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jackson.enum")
public class JacksonEnumProperties {
    /**
     * 开启枚举翻译
     */
    private Boolean openLabelWrite = true;
    /**
     * 翻译格式
     */
    private String format = "{}Name";

}
