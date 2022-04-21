# jackson-enum

优雅的处理枚举

## 使用方式

### 自定义枚举值和枚举描述注解
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EnumValue {
}

```

枚举配置
使用@EnumValue标记枚举的值，该值应该唯一
使用@EnumLabel标记枚举的翻译，在序列化时，会翻译枚举
```java

@Getter
@RequiredArgsConstructor
public enum GenderEnum {
    MALE(1,"男"),
    FEMALE(2,"女"),
    ;
    @EnumValue
    private final Integer code;
    @EnumLabel
    private final String desc;
}
```

启动类传入注解

```java

@SpringBootApplication
@EnableJacksonEnum(EnumValue.class)
public class JacksonEnumTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(JacksonEnumTestApplication.class, args);
    }

}
```

### Get方法接参
```java
    @GetMapping
    public String testGet(GenderEnum genderEnum) {
        return genderEnum.getDesc();
    }
```

### Post方法接参
TestDto
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDto {
    private GenderEnum genderEnum;
}

```

```java
@PostMapping
    public String testBody(@RequestBody TestDto dto) {
        return dto.getGenderEnum().getDesc();
    }
```