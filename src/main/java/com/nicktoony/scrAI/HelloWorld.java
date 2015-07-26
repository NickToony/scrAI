package com.nicktoony.scrAI;

import com.nicktoony.helpers.module;
import org.stjs.javascript.Global;

/**
 * Created by nick on 26/07/15.
 *
 * var stjs = require("stjs");
 * var StringProvider =  require("StringProvider");
 */

public class HelloWorld {
    private String hello;
    private int world;
    private String helloWorld;
    private String getting;

    public HelloWorld(String hello, int world) {
        this.hello = hello;
        this.world = world;
        this.helloWorld = hello + world;
        this.getting = new StringProvider().getString();

        int int1 = 10;
        int int2 = 10;
        if (int1 == int2) {
            Global.console.log(hello);
        }
    }

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    public int getWorld() {
        return world;
    }

    public void setWorld(int world) {
        this.world = world;
    }

    static { module.exports = HelloWorld.class; }
}