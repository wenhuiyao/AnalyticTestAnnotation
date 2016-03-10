package com.example.analytics;

import example.android.wenhui.annotation.AnalyticVar;

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

    public AnalyticVarField(AnalyticFieldAdapter fieldAdapter) throws ProcessingException {
        fieldName = fieldAdapter.getSimpleName();
        AnalyticVar annotation = fieldAdapter.getAnnotation(AnalyticVar.class);

        String[] matches = annotation.matchers();

        // equalTo, notNullValue, nullValue, will always be created
        hamcrestMatchers.add(EQUAL_TO);
        hamcrestMatchers.add(NULL_VALUE);
        hamcrestMatchers.add(NOT_NULL_VALUE);

        if( matches != null && matches.length > 0 )
            hamcrestMatchers.addAll(Arrays.asList(matches));

        value = getValue(fieldAdapter);
    }

    private String getValue(AnalyticFieldAdapter fieldAdapter) throws ProcessingException {
        // Make sure the type is String type

        final VariableElement variableElement = fieldAdapter.getVariableElement();

        final String simpleName = fieldAdapter.getSimpleName();

        Object v;
        try {
            v = fieldAdapter.getValue();
        } catch (IllegalAccessException e) {
            throw new ProcessingException(variableElement, "Expect %s to be package or public access",
                    simpleName);
        }

        if (v == null) {
            throw new ProcessingException(variableElement, "Expect %s to be final, and non null", simpleName);
        }

        if (!(v instanceof String)) {
            throw new ProcessingException(variableElement, "Expect %s to be final and a String type", simpleName);
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
}
