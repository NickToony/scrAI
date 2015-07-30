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

    // Creep ids
    public static String CREEP_MINER_ID = "1";
    public static String CREEP_MINER_NAME = "CreepMiner";
    public static String CREEP_COLLECTOR_ID = "2";
    public static String CREEP_COLLECTOR_NAME = "CreepCollector";

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
