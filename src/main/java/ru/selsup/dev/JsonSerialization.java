package ru.selsup.dev;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonSerialization<T> {

    public String serialization(T object) {
        ObjectMapper mapper = new ObjectMapper();
        String result = "";

        try {
            result =  mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            System.out.println("Ошибка серелизации объекта - " + e.getMessage());
        }

        return result;
    }

    public T deSerialization(String json,Class<T> value) {
        ObjectMapper mapper = new ObjectMapper();
        T result = null;

        try {
            result =  mapper.readValue(json, value);
        } catch (JsonParseException  e) {
            System.out.println("Ощибка преобразования JSON - " + e.getMessage());
        } catch (JsonMappingException e) {
            System.out.println("Ощибка преобразования JSON - " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Ощибка преобразования JSON - " + e.getMessage());
        }

        return result;
    }

}
