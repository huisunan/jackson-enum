package com.hsn;

import com.hsn.e.annotations.EnumLabel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
