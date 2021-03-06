package com.example.analytics;

import example.android.wenhui.annotation.AnalyticVar;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static example.android.wenhui.annotation.AnalyticMatchers.EQUAL_TO;
import static example.android.wenhui.annotation.AnalyticMatchers.NOT_NULL_VALUE;
import static example.android.wenhui.annotation.AnalyticMatchers.NULL_VALUE;

/**
 * Created by wyao on 3/8/16.
 */
public class AnalyticVarField {

    private String value;

    private Set<String> hamcrestMatchers = new HashSet<>(4);

    private String fieldName;

    public AnalyticVarField(VariableElement variableElement) throws ProcessingException {
        validateVariable(variableElement);

        fieldName = variableElement.getSimpleName().toString();
        AnalyticVar annotation = variableElement.getAnnotation(AnalyticVar.class);

        String[] matches = annotation.matchers();

        // equalTo, notNullValue, nullValue, will always be created
        hamcrestMatchers.add(EQUAL_TO);
        hamcrestMatchers.add(NULL_VALUE);
        hamcrestMatchers.add(NOT_NULL_VALUE);

        if( matches != null && matches.length > 0 )
            hamcrestMatchers.addAll(Arrays.asList(matches));

        value = getValue(variableElement);
    }

    private String getValue(VariableElement variableElement) throws ProcessingException {
        // Make sure the type is String type
        Object v = variableElement.getConstantValue();

        if (v == null) {
            throw new ProcessingException(variableElement, "Expect %s to be final, and non null", variableElement.getSimpleName());
        }

        if (!(v instanceof String)) {
            throw new ProcessingException(variableElement, "Expect %s to be final and a String type", variableElement
                    .getSimpleName());
        }

        return (String)v;
    }

    public String getValue(){
        return value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Set<String> getHamcrestMatchers() {
        return hamcrestMatchers;
    }

    /**
     * Make sure the field is a static final constant field field
     */
    private void validateVariable(VariableElement variableElement) throws ProcessingException {
        // Make sure it is and static
        final Set<Modifier> modifiers = variableElement.getModifiers();
        if (!modifiers.contains(Modifier.STATIC)) {
            throw new ProcessingException(variableElement, "The field @%s must be static field", AnalyticVar.class
                    .getSimpleName());
        }
    }
}
