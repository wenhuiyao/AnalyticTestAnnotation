package com.example.analytics.mock;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * Created by wyao on 3/10/16.
 */
public class MockTypeElement implements TypeElement {

    private Class clazz;

    private Annotation annotation;

    public MockTypeElement(Class clazz){
        this.clazz = clazz;
    }

    @Override
    public List<? extends Element> getEnclosedElements() {
        return null;
    }

    @Override
    public <R, P> R accept(ElementVisitor<R, P> v, P p) {
        return null;
    }

    @Override
    public NestingKind getNestingKind() {
        return null;
    }

    @Override
    public Name getQualifiedName() {
        return new MockName(clazz.getCanonicalName());
    }

    @Override
    public TypeMirror asType() {
        return new MockTypeMirror(clazz);
    }

    @Override
    public ElementKind getKind() {
        return ElementKind.CLASS;
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

    @Override
    public Set<Modifier> getModifiers() {
        return null;
    }

    @Override
    public Name getSimpleName() {
        return new MockName(clazz.getSimpleName());
    }

    @Override
    public TypeMirror getSuperclass() {
        return null;
    }

    @Override
    public List<? extends TypeMirror> getInterfaces() {
        return null;
    }

    @Override
    public List<? extends TypeParameterElement> getTypeParameters() {
        return null;
    }

    @Override
    public Element getEnclosingElement() {
        return null;
    }
}
