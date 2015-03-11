package com.dgsd.sydtrip.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

abstract class BaseModel {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}
