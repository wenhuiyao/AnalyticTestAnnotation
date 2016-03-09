package com.example.analytics;

import javax.lang.model.element.ExecutableElement;

/**
 * Created by wyao on 3/8/16.
 */
public class AnalyticMapMethod {

    private ExecutableElement element;

    private String simpleName;

    public AnalyticMapMethod(ExecutableElement element) {
        this.element = element;

        simpleName = element.getSimpleName().toString();
    }

    public String getSimpleName() {
        return simpleName;
    }
}
