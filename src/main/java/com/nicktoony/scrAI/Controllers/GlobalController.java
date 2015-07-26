package com.nicktoony.scrAI.Controllers;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.helpers.LodashCallback2;
import com.nicktoony.helpers.module;
import com.nicktoony.scrAI.Constants;
import com.nicktoony.screeps.Game;
import com.nicktoony.screeps.Room;
import org.stjs.javascript.Array;
import org.stjs.javascript.Global;
import org.stjs.javascript.functions.Callback1;
import org.stjs.javascript.functions.Callback2;

/**
 * Created by nick on 26/07/15.
 *  var stjs = require("stjs");
 *  var Constants = require("Constants");
 *  var RoomController = require("RoomController");
 *  var Lodash = require('lodash');
 */
public class GlobalController {
    private Array<RoomController> roomControllers;

    public GlobalController() {
        // Init array
        roomControllers = new Array<RoomController>();

        // For each room in game
        Lodash.forIn(Game.rooms, new LodashCallback2<Room, String>() {
            @Override
            public boolean invoke(Room room, String name) {
                // Create a room controller
                roomControllers.push(new RoomController(room));
                return false;
            }
        }, this);

        // For each room controller
        roomControllers.forEach(new Callback1<RoomController>() {
            @Override
            public void $invoke(RoomController roomController) {
                // If we have no spawns in this room
                if (roomController.getSpawnsManager().getSpawns().$length() == 0) {
                    // Immediately jump to alert
                    roomController.setAlertStatus(Constants.ALERT_STATUS_CRITICAL);
                    Global.console.log("CRITICAL STATUS: " + roomController.getRoom().name);
                } else {
                    roomController.setAlertStatus(Constants.ALERT_STATUS_LOW);
                }

                // Perform its step
                roomController.step();
            }
        });
    }

    static { module.exports = GlobalController.class; }
}
