package com.projeto.scribe.util;

import com.google.gson.Gson;

public class JsonUtil {

    public static final String converteJson(Object object) {
        return new Gson().toJson(object);
    }

}
