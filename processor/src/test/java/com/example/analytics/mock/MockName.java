package com.example.analytics.mock;

import javax.lang.model.element.Name;

/**
 * Created by wyao on 3/10/16.
 */
public class MockName implements Name {

    private String name;

    public MockName(String name){
        this.name = name;
    }

    @Override
    public boolean contentEquals(CharSequence cs) {
        if( name == cs ){
            return true;
        }

        return name != null && (cs != null && name.equals(cs.toString()));
    }

    @Override
    public int length() {
        return name.length();
    }

    @Override
    public char charAt(int index) {
        return name.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return name;
    }

    @Override
    public String toString() {
        return name == null ? "null" : name;
    }
}
