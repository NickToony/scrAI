package com.nicktoony.scrAI;

import com.nicktoony.helpers.module;

/**
 * Created by nick on 26/07/15.
 * var stjs = require("stjs");
 */
public class Constants {
    public static int TIER_LOW = 1;
    public static int TIER_MEDIUM = 2;
    public static int TIER_HIGH = 3;

    public static int ALERT_STATUS_CRITICAL = 1; // 100% military, 0% economy
    public static int ALERT_STATUS_HIGH = 2; //
    public static int ALERT_STATUS_MEDIUM = 3; // 50% military, 50% ecconomy
    public static int ALERT_STATUS_LOW = 4; //
    public static int ALERT_STATUS_NONE = 5; // 0% military, 100% economy

    public static int OFFSET_ROOM_STORAGE = 100; // useful to stop the AI spending too much early on
    public static int DELAY_RANDOM = 20;
    public static int DELAY_PATH_SCAN = 100; // how many ticks between checking paths are okay
    public static int DELAY_ENERGY_SCAN = 20;
    public static int DELAY_ECONOMY_DECISION = 10;
    public static int DELAY_CONSTRUCTION_MANAGER = 50;
    public static int DELAY_STRUCTURE_MANAGER = 100;
    public static int DELAY_POPULATION_MANAGER = 150;
    public static int DELAY_SPAWN_MANAGER = 100;

    public static int PRIORITY_BUILD = 10;
    public static int PRIORITY_UPGRADE = 0;
    public static int PRIORITY_PICKUP = 100;
    public static int PRIORITY_DEPOSIT = 15;
    public static int PRIORITY_REPAIR = 50;

    // Creep ids
    public static String CREEP_MINER_ID = "1";
    public static String CREEP_MINER_NAME = "CreepMiner";
    public static String CREEP_WORKER_ID = "2";
    public static String CREEP_WORKER_NAME = "CreepWorker";

    // Creep settings (miner)
    public static int SETTING_MINER_PER_SPAWN = 2;

    // Source constants
    public static final int SOURCE_REGEN = 300;

    // Creep part costs
    public static final int MOVE_COST = 50;
    public static final int WORK_COST = 100;
    public static final int CARRY_COST = 50;
    public static final int ATTACK_COST = 80;
    public static final int RANGED_ATTACK_COST = 150;
    public static final int HEAL_COST = 250;
    public static final int TOUGH_COST = 10;

}
