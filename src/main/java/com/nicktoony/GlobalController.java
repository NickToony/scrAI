package com.nicktoony;

import com.nicktoony.scrAI.controllers.RoomController;
import com.nicktoony.screeps.Game;
import com.nicktoony.screeps.Room;
import com.nicktoony.screeps.callbacks.Lodash;
import com.nicktoony.screeps.callbacks.LodashCallback2;
import org.stjs.javascript.Array;
import org.stjs.javascript.Global;
import org.stjs.javascript.functions.Callback1;

/**
 * Created by nick on 17/08/15.
 */
public class GlobalController {
    public static void main(String[] args) {
        // How long did it take to load?
        Global.console.log("Tick Started, used CPU: " + Game.getUsedCpu());

        new GlobalController();

        // How long to complete?
        Global.console.log("Tick Finished, used CPU: " + Game.getUsedCpu());
    }

    private Array<RoomController> roomControllers;

    public GlobalController() {
        roomControllers = new Array<RoomController>();
        // For each room in game
        Lodash.forIn(Game.rooms, new LodashCallback2<Room, String>() {
            @Override
            public boolean invoke(Room room, String name) {
                // Create a room controller
                roomControllers.push(new RoomController(room));
                return true;
            }
        }, this);

        // For each room controller
        roomControllers.forEach(new Callback1<RoomController>() {
            @Override
            public void $invoke(RoomController roomController) {
                // Perform its step
                roomController.step();
            }
        });
    }
}
