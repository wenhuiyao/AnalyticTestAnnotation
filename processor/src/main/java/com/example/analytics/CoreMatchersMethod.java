package com.example.analytics;

import com.squareup.javapoet.ParameterSpec;

/**
 * Created by wyao on 3/8/16.
 */
public interface CoreMatchersMethod {

    /**
     * The type of the object expected to be compared, such as, String.class, Object.class
     * @return
     */
    Class objectType();

    boolean isParameterVarargs();

    ParameterSpec[] parameters();

    /**
     * The actual method block withOut the methodName(), e.g.
     * @return
     */
    String methodBlock();
}
