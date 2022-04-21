package com.hsn.e.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class EnumInfo {
    private final Method valueMethod;
    private final Method labelMethod;
    private final List<Field> staticFields;
    private final Field valueField;
    private final Field labelFiled;
}
