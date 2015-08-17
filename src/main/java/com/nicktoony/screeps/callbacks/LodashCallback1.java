package com.nicktoony.screeps.callbacks;

import org.stjs.javascript.annotation.JavascriptFunction;

/**
 * Created by nick on 26/07/15.
 */
@JavascriptFunction
public interface LodashCallback1<T> extends LodashCallback {
    boolean invoke(T variable);
}
