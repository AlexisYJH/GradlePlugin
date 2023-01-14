package com.test.plugin

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @author AlexisYin
 */
@Retention(RetentionPolicy.CLASS)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
annotation class ASMTest