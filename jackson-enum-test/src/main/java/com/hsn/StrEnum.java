package com.hsn;

import com.hsn.e.annotations.EnumLabel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StrEnum {
    A("a", "字母a"),
    B("b", "字母b"),
    ;
    @EnumValue
    private final String code;
    @EnumLabel
    private final String desc;
}
