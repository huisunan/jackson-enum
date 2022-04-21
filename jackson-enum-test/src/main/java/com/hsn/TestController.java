package com.hsn;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @GetMapping
    public String testGet(GenderEnum genderEnum) {
        return genderEnum.getDesc();
    }

    @PostMapping
    public String testBody(@RequestBody TestDto dto) {
        return dto.getGenderEnum().getDesc();
    }

    @GetMapping("return")
    public TestVo testVo() {
        TestVo testVo = new TestVo();
        testVo.setGenderEnum(GenderEnum.MALE);
        return testVo;
    }

    @PostMapping("extend")
    public String testExtend(@RequestBody TestSubDto subDto){
        return subDto.getGenderEnum().getDesc();
    }

    @GetMapping("str")
    public String testStr(StrEnum strEnum){
        return strEnum.getDesc();
    }
}

