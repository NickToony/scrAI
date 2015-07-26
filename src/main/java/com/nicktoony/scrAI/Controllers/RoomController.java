package com.nicktoony.scrAI.Controllers;

import com.nicktoony.helpers.module;
import com.nicktoony.scrAI.Managers.PopulationManager;
import com.nicktoony.scrAI.Managers.SourcesManager;
import com.nicktoony.screeps.Room;
import org.stjs.javascript.Global;

/**
 * Created by nick on 26/07/15.
 *  var stjs = require("stjs");
 *  var PopulationManager = require("PopulationManager");
 */
public class RoomController {

    private Room room;
    private PopulationManager populationManager;
    private SourcesManager sourcesManager;

    public RoomController(Room room) {
        this.room = room;
        populationManager = new PopulationManager(this);
        sourcesManager = new SourcesManager(this);
    }

    public void step() {

    }

    public Room getRoom() {
        return room;
    }

    static { module.exports = RoomController.class; }
}
