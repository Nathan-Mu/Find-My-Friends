package edu.monash.fit5046.fit5046a2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import static edu.monash.fit5046.fit5046a2.JSONReader.*;

/**
 * Created by nathan on 1/5/17.
 */

public class GoogleSearch {
    public static String search(String keyword, String param)
    {
        String API_key = "AIzaSyAJ7Nj5HIHItRXXHLI7vNotQXBblBqJtXs";
        String SEARCH_ID_cx = "014497452370399362648:63kaqy0ae9k";
        keyword = keyword.replace(" ", "+");
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        String path = "https://www.googleapis.com/customsearch/v1?key=" + API_key + "&cx=" + SEARCH_ID_cx + "&q=" + keyword + "&num=1" + param;
        try {
            url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine())
            {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return textResult;
    }

    public static String getMovieImagePath(String keyword)
    {
        String path = "";
        String response = search(keyword, "&fields=items(pagemap(cse_image))");
        JSONObject json = toJSONObject(response);
        JSONArray itemsArray = getJSONArrayFromJSONObjectByName(json, "items");
        JSONObject pagemapObject = getJSONObjectFromJSONObjectByName(getFirstFromArray(itemsArray), "pagemap");
        JSONArray cseImageArray = getJSONArrayFromJSONObjectByName(pagemapObject, "cse_image");
        path = getStringFromJSONObject(getFirstFromArray(cseImageArray), "src");
        return path;
    }

    public static String[] getMovieIntro(String keyword) {
        String[] array = {};
        String response = search(keyword, "&fields=items(snippet,link)");
        JSONObject json = toJSONObject(response);
        JSONArray itemsArray = getJSONArrayFromJSONObjectByName(json, "items");
        String snippet = getStringFromJSONObject(getFirstFromArray(itemsArray), "snippet");
        snippet = snippet.replace("\n", "");
        String link = getStringFromJSONObject(getFirstFromArray(itemsArray), "link");
        array = new String[] {snippet, link};
        return array;
    }

    public static String getAddress(Double lat, Double lon) {
        String API_key = "AIzaSyAJ7Nj5HIHItRXXHLI7vNotQXBblBqJtXs";
        String SEARCH_ID_cx = "014497452370399362648:63kaqy0ae9k";
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        String path = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lon +"&key=AIzaSyAJ7Nj5HIHItRXXHLI7vNotQXBblBqJtXs&fields=results(formatted_address)";
        try {
            url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine())
            {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        JSONObject json = getFirstFromArray(getJSONArrayFromJSONObjectByName(toJSONObject(textResult), "results"));
        return getStringFromJSONObject(json, "formatted_address");
    }

}
