package com.nicktoony.scrAI.Advisors;

import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Creeps.CreepDefinition;

/**
 * Created by nick on 26/07/15.
 *
 * Military advisor makes decisions regarding military units
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
