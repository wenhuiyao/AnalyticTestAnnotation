package com.example.analytics.mock;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * Created by wyao on 3/10/16.
 */
public class MockVariableElement implements VariableElement {

    private Annotation annotation;
    private Name simpleName;
    private Object value;
    private Set<Modifier> modifiers;

    public void setConstantValue(Object value){
        this.value = value;
    }

    @Override
    public Object getConstantValue() {
        return value;
    }

    @Override
    public TypeMirror asType() {
        return null;
    }

    @Override
    public ElementKind getKind() {
        return null;
    }

    @Override
    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        return null;
    }

    public <T extends Annotation> void setAnnotation(T annotation){
        this.annotation = annotation;
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        return (A)annotation;
    }

    public void setModifiers(Set<Modifier> modifiers){
        this.modifiers = modifiers;
    }

    @Override
    public Set<Modifier> getModifiers() {
        return modifiers;
    }

    public void setSimpleName(Name name){
        this.simpleName = name;
    }

    @Override
    public Name getSimpleName() {
        return simpleName;
    }

    @Override
    public Element getEnclosingElement() {
        return null;
    }

    @Override
    public List<? extends Element> getEnclosedElements() {
        return null;
    }

    @Override
    public <R, P> R accept(ElementVisitor<R, P> v, P p) {
        return null;
    }
}
