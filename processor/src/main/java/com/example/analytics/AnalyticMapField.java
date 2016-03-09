package com.example.analytics;

import javax.lang.model.element.VariableElement;

/**
 * Created by wyao on 3/8/16.
 */
public class AnalyticMapField {

    private VariableElement variableElement;

    private String variableName;

    public AnalyticMapField(VariableElement variableElement) {
        this.variableElement = variableElement;

        variableName = variableElement.getSimpleName().toString();

        variableElement.getKind();
    }

    public String getVariableName() {
        return variableName;
    }
}
