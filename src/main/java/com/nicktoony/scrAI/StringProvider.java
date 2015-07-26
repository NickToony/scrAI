package com.nicktoony.scrAI;

import com.nicktoony.helpers.module;

/**
 * Created by nick on 26/07/15.
 * var stjs = require("stjs");
 */
public class StringProvider {
    private String string = "HELLO";

    public StringProvider() {

    }

    public String getString() {
        return string;
    }

    static {
        module.exports = StringProvider.class; }
}