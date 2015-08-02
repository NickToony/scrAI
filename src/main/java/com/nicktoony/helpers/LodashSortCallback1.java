package com.nicktoony.helpers;

import org.stjs.javascript.annotation.JavascriptFunction;

/**
 * Created by nick on 26/07/15.
 */
@JavascriptFunction
public interface LodashSortCallback1<T> extends LodashSortCallback {
    int invoke(T variable);
}
