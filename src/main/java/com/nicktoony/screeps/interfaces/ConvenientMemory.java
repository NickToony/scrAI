package com.nicktoony.screeps.interfaces;

import org.stjs.javascript.JSCollections;

import java.util.Map;

/**
 * Created by nick on 10/08/15.
 */
public interface ConvenientMemory {
    public Map<String, Object> memory = (Map<String, Object>) JSCollections.$map();
}
