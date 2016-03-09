package com.example.analytics;

import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * Adapter for {@link java.lang.reflect.Field} and {@link javax.lang.model.element.VariableElement}
 *
 * Created by wyao on 3/9/16.
 */
public class AnalyticFieldAdapter {

    private final Field field;
    private final VariableElement variableElement;

    public AnalyticFieldAdapter(Field field){
        this.field = field;
        this.variableElement = null;
    }

    public AnalyticFieldAdapter(VariableElement variableElement){
        this.field = null;
        this.variableElement = variableElement;
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotationType){
        if( field != null ){
            return field.getAnnotation(annotationType);
        } else {
            return variableElement.getAnnotation(annotationType);
        }
    }

    public String getFullName(){
        if( field != null ){
            return field.getClass().getCanonicalName() + "#" + field.getName();
        } else {
            return variableElement.getEnclosingElement().getClass().getCanonicalName() + "#" + variableElement
                    .getSimpleName();
        }
    }

    public String getSimpleName(){
        if( field != null ){
            return field.getName();
        } else {
            return variableElement.getSimpleName().toString();
        }
    }

    public Object getValue() throws IllegalAccessException {
        if( field != null ){
            return field.get(null);
        } else {
            return variableElement.getConstantValue();
        }
    }

    /**
     * Returns the Java language modifiers for the field represented
     * by this {@code Field} object, as an integer. The {@code Modifier} class should
     * be used to decode the modifiers.
     *
     * @see Modifier
     */
    public int getModifiers(){
        if( field != null ){
            return field.getModifiers();
        } else {
            int modifier = 0;
            final Set<javax.lang.model.element.Modifier> modifiers = variableElement.getModifiers();
            if( modifiers.contains(javax.lang.model.element.Modifier.PUBLIC)){
                modifier |= Modifier.PUBLIC;
            }

            if ( modifiers.contains(javax.lang.model.element.Modifier.STATIC)) {
                modifier |= Modifier.STATIC;
            }
            return modifier;
        }
    }

    public VariableElement getVariableElement() {
        return variableElement;
    }


}
