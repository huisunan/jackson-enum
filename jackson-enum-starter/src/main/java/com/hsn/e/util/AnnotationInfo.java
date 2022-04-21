package com.hsn.e.util;

import lombok.Data;

import java.lang.annotation.Annotation;

@Data
public class AnnotationInfo {
    private Class<? extends Annotation> labelAnnotation;
    private Class<? extends Annotation> valueAnnotation;
}
