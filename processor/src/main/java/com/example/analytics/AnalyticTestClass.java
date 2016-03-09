package com.example.analytics;

import example.android.wenhui.annotation.AnalyticTest;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

/**
 * Created by wyao on 3/8/16.
 */
public class AnalyticTestClass {

    private TypeElement typeElement;

    private Class varClass;

    private TypeElement varTypeElement;

    private String simpleName;

    private String qualifiedName;

    public AnalyticTestClass(TypeElement typeElement){
        this.typeElement = typeElement;

        this.simpleName = typeElement.getSimpleName().toString();
        this.qualifiedName = typeElement.getQualifiedName().toString();

        final AnalyticTest annotation = typeElement.getAnnotation(AnalyticTest.class);
        try {
            // We have the class, it usually comes from the compiled library
            varClass = annotation.varClass();
        } catch (MirroredTypeException mte) {
            // Class is not yet compiling, this happens when the class is not a library class
            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
            varTypeElement = (TypeElement) classTypeMirror.asElement();
        }
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public Class getVarClass() {
        return varClass;
    }

    public TypeElement getVarTypeElement() {
        return varTypeElement;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }
}
