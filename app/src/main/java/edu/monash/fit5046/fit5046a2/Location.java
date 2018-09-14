package edu.monash.fit5046.fit5046a2;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.math.BigDecimal;

import static edu.monash.fit5046.fit5046a2.JSONReader.getDoubleFromJSONObject;
import static edu.monash.fit5046.fit5046a2.JSONReader.getIntegerFromJSONObject;
import static edu.monash.fit5046.fit5046a2.JSONReader.getJSONObjectFromJSONObjectByName;
import static edu.monash.fit5046.fit5046a2.JSONReader.getStringFromJSONObject;

/**
 * Created by nathan on 4/5/17.
 */

public class Location implements Parcelable{
    private Integer locationId = -1;
    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private String LDate = null;
    private String LTime = null;
    private String LName = null;
    private Student stId = null;

    public Location() {
        this.locationId = 0;
    }

    public Location(Double latitude, Double longitude, String LDate, String LTime, String LName, Student stId) {
        this.locationId = 1;
        this.latitude = latitude;
        this.longitude = longitude;
        this.LDate = LDate;
        this.LTime = LTime;
        this.LName = LName;
        this.stId = stId;
    }

    public Location(JSONObject jsonObject) {
        this.locationId = getIntegerFromJSONObject(jsonObject, "locationId");
        this.latitude = getDoubleFromJSONObject(jsonObject, "latitude");
        this.longitude = getDoubleFromJSONObject(jsonObject, "longitude");
        this.LDate = getStringFromJSONObject(jsonObject, "LDate");
        this.LTime = getStringFromJSONObject(jsonObject, "LTime");
        this.LName = getStringFromJSONObject(jsonObject, "LName");
        this.stId = new Student(getJSONObjectFromJSONObjectByName(jsonObject, "stId"));
    }

    protected Location(Parcel in)
    {
        locationId = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        LDate = in.readString();
        LTime = in.readString();
        LName = in.readString();
        stId = (Student) in.readValue(Student.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(locationId);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(LDate);
        parcel.writeString(LTime);
        parcel.writeString(LName);
        parcel.writeValue(stId);
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLDate() {
        return LDate;
    }

    public void setLDate(String LDate) {
        this.LDate = LDate;
    }

    public String getLTime() {
        return LTime;
    }

    public void setLTime(String LTime) {
        this.LTime = LTime;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }

    public Student getStId() {
        return stId;
    }

    public void setStId(Student stId) {
        this.stId = stId;
    }
}
