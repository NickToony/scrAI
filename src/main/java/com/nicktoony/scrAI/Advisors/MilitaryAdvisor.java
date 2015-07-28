package com.nicktoony.scrAI.Advisors;

import com.nicktoony.helpers.module;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.Creeps.CreepDefinition;

/**
 * Created by nick on 26/07/15.
 * var stjs = require("stjs");
 * var Constants = require('Constants');
 * var Advisor = require("Advisor");
 */
public class MilitaryAdvisor extends Advisor {
    private int tier;

    public MilitaryAdvisor(RoomController roomController) {
        super(roomController);
    }

    @Override
    public CreepDefinition step() {
        return null;
    }

}
