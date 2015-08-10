package com.nicktoony.screeps.global;

import org.stjs.javascript.annotation.GlobalScope;

/**
 * Created by nick on 10/08/2015.
 */
@GlobalScope
public enum ResponseTypes {
    OK,
    ERR_INVALID_TARGET,
    ERR_INVALID_ARGS,
    ERR_RCL_NOT_ENOUGH,
    ERR_NAME_EXISTS
}
