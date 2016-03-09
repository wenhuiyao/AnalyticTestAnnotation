package com.example.analytics;

import example.android.wenhui.annotation.AnalyticVar;

import javax.lang.model.element.VariableElement;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wyao on 3/8/16.
 */
public class AnalyticVarField {

    private String value;

    private Set<String> hamcrestMatchers = new HashSet<>(4);

    private String fieldName;

    public AnalyticVarField(VariableElement fieldElement) throws ProcessingException {
        fieldName = fieldElement.getSimpleName().toString();
        AnalyticVar annotation = fieldElement.getAnnotation(AnalyticVar.class);

        initAnnotation(annotation);

        // Safe to convert, since it is already checked
        value = (String)fieldElement.getConstantValue();
    }

    public AnalyticVarField(Field field) throws ProcessingException {
        fieldName = field.getName();

        AnalyticVar annotation = field.getAnnotation(AnalyticVar.class);
        initAnnotation(annotation);

        try {
            value = (String)field.get(null);
            System.out.println("Debug: value=" + value);
        } catch (IllegalAccessException e) {
            throw new ProcessingException(null, "Make sure the field %s is public and static", field.toString());
        }
    }

    private void initAnnotation(AnalyticVar annotation){
        String[] matches = annotation.matchers();

        // equalTo will always be created
        hamcrestMatchers.add("equalTo");
        if( matches != null && matches.length > 0 )
            hamcrestMatchers.addAll(Arrays.asList(matches));

        System.out.println("matchers: " + hamcrestMatchers);
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
