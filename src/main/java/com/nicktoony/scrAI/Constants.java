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

    // Creep settings (miner)
    public static int SETTING_MINER_PER_SPAWN = 2;

    // Source constants
    public static int OPTIMAL_WORK = 5; // 3000 energy, 300 regen. 10 per tick is optimal. WORK = 2 per tick
    public static final int SOURCE_REGEN = 300;

    public static final int MOVE_COST = 50;
    public static final int WORK_COST = 100;

}
