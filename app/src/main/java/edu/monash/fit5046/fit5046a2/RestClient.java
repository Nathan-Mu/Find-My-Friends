package edu.monash.fit5046.fit5046a2;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static edu.monash.fit5046.fit5046a2.JSONReader.*;

/**
 * Created by nathan on 28/4/17.
 */

public class RestClient {
    private static final String BASE_URI = "http://192.168.1.44:8080/MonashFriendDB/webresources";

    public static String getLoginInfo(String username, String password) {
        final String methodPath = "/restclient.stprofile/findByUserNameAndPassword/" + username + "/" + password;
        String URLString = BASE_URI + methodPath;
        return getResult(URLString);
    }

    public static ArrayList<Location> getLocation(String stIds) {
        ArrayList<Location> locations = new ArrayList<>();
        final String methodPath = "/restclient.stlocation/findSelectedStudents/" + stIds;
        String URLString = BASE_URI + methodPath;
        JSONArray locationArray = toJSONArray(getResult(URLString));
        for (int i = 0; i < locationArray.length(); i++)
        {
            try {
                JSONObject json = locationArray.getJSONObject(i);
                locations.add(new Location(json));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return locations;
    }

    public static String getFUnits()
    {
        final String methodPath = "/restclient.stprofile/findFUnit";
        String URLString = BASE_URI + methodPath;
        return getResult(URLString);
    }

    public static List<Student> getFilteredStudent(int stId, List<String> attributes){
        String attributesString = "";
        List<Student> students = new ArrayList<>();
        for (String s: attributes) {
            attributesString += s + ",";
        }
        attributesString = attributesString.substring(0, attributesString.length() - 1);
        final String methodPath = "/restclient.stprofile/findByKeyWords2/" + stId + "/" + attributesString;
        String URLString = BASE_URI + methodPath;
        JSONArray studentArray = toJSONArray(getResult(URLString));
        for (int i = 0; i < studentArray.length(); i++)
        {
            try {
                JSONObject json = studentArray.getJSONObject(i);
                students.add(new Student(json));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return students;
    }

    public static Friendship getFriendship(int stId1, int stId2){
        int smallStId;
        int largeStId;
        if (stId1 > stId2) {
            smallStId = stId2;
            largeStId = stId1;
        }
        else {
            largeStId = stId2;
            smallStId = stId1;
        }
        final String methodPath = "/restclient.stfriendship/findFriendship/" + smallStId + "/" + largeStId;
        String URLString = BASE_URI + methodPath;
        JSONObject jsonObject = getFirstFromArray(toJSONArray(getResult(URLString)));
        return new Friendship(jsonObject);
    }

    public static List<Student> getFriendsList(int stId) {
        final String methodPath = "/restclient.stfriendship/findFriendsByStId/" + stId;
        List<Student> students = new ArrayList<>();
        String URLString = BASE_URI + methodPath;
        JSONArray studentArray = toJSONArray(getResult(URLString));
        for (int i = 0; i < studentArray.length(); i++)
        {
            try {
                JSONObject json = studentArray.getJSONObject(i);
                students.add(new Student(json));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return students;
    }

    public static String getAutoLoginInfo(String stId, String password) {
        final String methodPath = "/restclient.stprofile/findByStIdAndPassword/" + stId + "/" + password;
        String URLString = BASE_URI + methodPath;
        return getResult(URLString);
    }

    public static boolean checkEmailExist(String emailAddress) {
        final String methodPath = "/restclient.stprofile/checkMonashEmailExist/" + emailAddress;
        String URLString = BASE_URI + methodPath;
        return Boolean.valueOf(getResult(URLString));
    }

    public static String getAvailableStId() {
        final String methodPath = "/restclient.stprofile/findNextStId";
        String URLString = BASE_URI + methodPath;
        return getResult(URLString);
    }

    public static String getResult(String URLString) {
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //Making HTTP request
        try {
            url = new URL(URLString);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //read the input steream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return textResult;
    }

    public static boolean createStudent(Student student) {
        final String methodPath = "/restclient.stprofile/";
        return postMethod(methodPath, student);
    }

    public static boolean updateStudent(Student student) {
        final String methodPath = "/restclient.stprofile/" + student.getStId();
        return putMethod(methodPath, student);
    }

    public static boolean postMethod(String methodPath, Object object) {
        URL url = null;
        int responseCode = 400;
        HttpURLConnection conn = null;
        try {
            Gson gson = new Gson();
            String stringJson = gson.toJson(object);
            url = new URL(BASE_URI + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(stringJson.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json");
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(stringJson);
            out.close();
            responseCode = conn.getResponseCode();
            Log.i("error", new Integer(responseCode).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        if (responseCode < 400)
            return true;
        else
            return false;
    }

    public static boolean putMethod(String methodPath, Object object) {
        URL url = null;
        int responseCode = 400;
        HttpURLConnection conn = null;
        try {
            Gson gson = new Gson();
            String stringJson = gson.toJson(object);
            url = new URL(BASE_URI + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(stringJson.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json");
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(stringJson);
            out.flush();
            out.close();
            responseCode = conn.getResponseCode();
            Log.i("error", new Integer(responseCode).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        if (responseCode < 400)
            return true;
        else
            return false;
    }

    public static boolean createFriendship(Friendship friendship) {
        final String methodPath = "/restclient.stfriendship/";
        return postMethod(methodPath, friendship);
    }

    public static boolean endFriendship(Friendship friendship) {
        final String methodPath = "/restclient.stfriendship/" + friendship.getFriendshipId();
        return putMethod(methodPath, friendship);
    }
}

