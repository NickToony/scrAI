package com.nicktoony.scrAI;

import com.nicktoony.scrAI.Controllers.GlobalController;
import com.nicktoony.screeps.Game;
import org.stjs.javascript.Global;

/**
 * Created by nick on 26/07/15.
 *
 *  var stjs = require("stjs");
 *  var GlobalController = require("GlobalController");
 */
public class main {
    public static void main(String[] args) {
        new GlobalController();
        Global.console.log("Tick Finished, used CPU: " + Game.getUsedCpu());
    }
}