package edu.monash.fit5046.fit5046a2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * Created by nathan on 1/5/17.
 */

public class JSONReader {

    public JSONReader()
    {}

    public static JSONObject toJSONObject(String string)
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONArray toJSONArray(String string)
    {
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = new JSONArray(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public static JSONObject getFirstFromArray(JSONArray jsonArray)
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = jsonArray.getJSONObject(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONArray getJSONArrayFromJSONObjectByName(JSONObject jsonObject, String name)
    {
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = jsonObject.getJSONArray(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public static String getStringFromJSONObject(JSONObject jsonObject, String name)
    {
        String string = "";
        try {
            string = jsonObject.getString(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    public static int getIntegerFromJSONObject(JSONObject jsonObject, String name)
    {
        int i = -1;
        try {
            i = jsonObject.getInt(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public static Double getDoubleFromJSONObject(JSONObject jsonObject, String name)
    {
        Double d = 0.0;
        try {
            d = jsonObject.getDouble(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    public static JSONObject getJSONObjectFromJSONObjectByName(JSONObject jsonObject, String name)
    {
        JSONObject json = new JSONObject();
        try {
            json = jsonObject.getJSONObject(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
