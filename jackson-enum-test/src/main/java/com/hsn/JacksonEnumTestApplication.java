package com.hsn;

import com.hsn.e.annotations.EnableJacksonEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableJacksonEnum(EnumValue.class)
public class JacksonEnumTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(JacksonEnumTestApplication.class, args);
    }

}
