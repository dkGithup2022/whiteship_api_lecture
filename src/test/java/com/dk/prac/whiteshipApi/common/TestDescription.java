package com.dk.prac.whiteshipApi.common;


// 이 클래스는 junit4 에서만 쓰입니다. 5 에서는 그냥 DisplayName 쓰면 됨.

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface TestDescription {
    String value();
}
