package com.pedroid.weather.api;

/**
 * Created by pedro on 5/21/15.
 */

import java.io.IOException;
import java.util.ArrayList;

import java.io.IOException;
import java.util.ArrayList;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    public static ObjectMapper OBJECT_MAPPER = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    public static String serialize(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String serialize(Object object, Class<?> viewCass) {
        try {
            return OBJECT_MAPPER.writerWithView(viewCass).writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T deserialize(String jsonString, Class<T> asClass) {
        try {
            return OBJECT_MAPPER.readValue(jsonString, asClass);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> ArrayList<T> deserializeList(String jsonString) {
        try {
            return OBJECT_MAPPER.readValue(jsonString, new TypeReference<ArrayList<T>>() {});
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
