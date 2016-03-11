package com.example.analytics.mock;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;
import java.util.List;

/**
 * Created by wyao on 3/11/16.
 */
public class MockTypeMirror implements TypeMirror, DeclaredType {

    private Class type;
    private TypeKind kind = TypeKind.DECLARED;
    private List<? extends TypeMirror> typeArguments;

    public MockTypeMirror(Class type){
        this.type = type;
    }

    public void setKind(TypeKind kind){
        this.kind = kind;
    }

    @Override
    public TypeKind getKind() {
        return kind;
    }

    @Override
    public <R, P> R accept(TypeVisitor<R, P> v, P p) {
        return (R)TypeName.get(type);
    }

    @Override
    public String toString() {
        return type.getCanonicalName();
    }

    @Override
    public Element asElement() {
        return new MockTypeElement(type);
    }

    @Override
    public TypeMirror getEnclosingType() {
        return null;
    }

    public void setTypeArguments(List<? extends TypeMirror> typeArguments){
        this.typeArguments = typeArguments;
    }

    @Override
    public List<? extends TypeMirror> getTypeArguments() {
        return typeArguments;
    }

    public Class getType(){
        return type;
    }
}
