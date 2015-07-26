package com.nicktoony.helpers;

import org.stjs.javascript.annotation.JavascriptFunction;

/**
 * Created by nick on 26/07/15.
 */
@JavascriptFunction
public interface LodashCallback2<T1, T2> extends LodashCallback {
    boolean invoke(T1 variable1, T2 variable2);
}
