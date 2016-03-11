package com.example.analytics;

import example.android.wenhui.annotation.AnalyticTest;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.util.Elements;

/**
 * Created by wyao on 3/8/16.
 */
public class AnalyticTestClass {

    private TypeElement typeElement;

    private String finalClassName;

    private TypeElement varTypeElement;

    private String simpleName;


    public AnalyticTestClass(TypeElement typeElement, Elements elementUtils){
        this.typeElement = typeElement;

        this.simpleName = typeElement.getSimpleName().toString();

        final AnalyticTest annotation = typeElement.getAnnotation(AnalyticTest.class);
        finalClassName = annotation.name();

        try {
            // We have the class, it usually comes from the compiled library
            Class varClass = annotation.varClass();
            varTypeElement = elementUtils.getTypeElement(varClass.getCanonicalName());
        } catch (MirroredTypeException mte) {
            // Class is not yet compiling, this happens when the class is not a library class
            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
            varTypeElement = (TypeElement) classTypeMirror.asElement();
        }
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public TypeElement getVarTypeElement() {
        return varTypeElement;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getFinalClassName() {
        return finalClassName;
    }
}
