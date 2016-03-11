package com.example.analytics;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterSpec;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by wyao on 3/8/16.
 */
public interface CoreMatchersMethod {

    /**
     * The type of the object expected to be compared, such as, String.class, Object.class
     *
     * Throw {@linkplain ProcessingException} if the expected type isn't the super type of the return type
     *
     * @param elementUtils
     * @param typeUtils
     *@param mapValueType The value class type in the map, the return type must be the same type or its subtype.  @return
     */
    TypeMirror expectedObjectType(Elements elementUtils, Types typeUtils, TypeMirror mapValueType) throws ProcessingException;

    boolean isParameterVarargs();

    ParameterSpec[] parameters();

    /**
     * The actual method block withOut the methodName(), e.g.
     *
     * "assertThat(expected, <b>CoreMatchers.equalTo(parameter)</b>)";
     *
     * This method should generate the part "equalTo(parameter)"
     * @return
     */
    CodeBlock methodBlock();
}
