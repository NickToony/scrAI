package com.nicktoony.helpers;

import org.stjs.javascript.annotation.JavascriptFunction;

/**
 * Created by nick on 26/07/15.
 */
@JavascriptFunction
public interface FilterCallback<T> {
    boolean invoke(T variable);
}
