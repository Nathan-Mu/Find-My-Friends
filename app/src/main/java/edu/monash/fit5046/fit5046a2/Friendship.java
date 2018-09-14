package edu.monash.fit5046.fit5046a2;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import static edu.monash.fit5046.fit5046a2.JSONReader.getIntegerFromJSONObject;
import static edu.monash.fit5046.fit5046a2.JSONReader.getJSONObjectFromJSONObjectByName;
import static edu.monash.fit5046.fit5046a2.JSONReader.getStringFromJSONObject;

/**
 * Created by nathan on 4/5/17.
 */

public class Friendship implements Parcelable{
    // for friendshipId, -1 means it's a object without value and set up constructor with jsonObject parameter
    // 0 means this object is set up by default constructor
    // 1 means this object is set up by a non-default constructor
    // the same meaning with Class Student
    private int friendshipId = -1;
    private Student smallStId = new Student();
    private Student largeStId = new Student();
    private String startingDate = null;
    private String endingDate = null;

    public Friendship() {
        this.friendshipId = 0;
    }

    public Friendship(Student smallStId, Student largeStId, String startingDate, String endingDate) {
        this.friendshipId = 1;
        this.smallStId = smallStId;
        this.largeStId = largeStId;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
    }

    public Friendship(JSONObject jsonObject) {
        this.friendshipId = getIntegerFromJSONObject(jsonObject, "friendshipId");
        this.smallStId = new Student(getJSONObjectFromJSONObjectByName(jsonObject, "smallStId"));
        this.largeStId = new Student(getJSONObjectFromJSONObjectByName(jsonObject, "largeStId"));
        this.startingDate = getStringFromJSONObject(jsonObject, "startingDate");
        this.endingDate = getStringFromJSONObject(jsonObject, "endingDate");
    }

    protected Friendship(Parcel in)
    {
        friendshipId = in.readInt();
        smallStId = (Student) in.readValue(Student.class.getClassLoader());
        largeStId = (Student) in.readValue(Student.class.getClassLoader());
        startingDate = in.readString();
        endingDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(friendshipId);
        parcel.writeValue(smallStId);
        parcel.writeValue(largeStId);
        parcel.writeString(startingDate);
        parcel.writeString(endingDate);
    }

    public static final Creator<Friendship> CREATOR = new Creator<Friendship>() {
        @Override
        public Friendship createFromParcel(Parcel in) {
            return new Friendship(in);
        }

        @Override
        public Friendship[] newArray(int size) {
            return new Friendship[size];
        }
    };

    public int getFriendshipId() {
        return friendshipId;
    }

    public void setFriendshipId(int friendshipId) {
        this.friendshipId = friendshipId;
    }

    public Student getSmallStId() {
        return smallStId;
    }

    public void setSmallStId(Student smallStId) {
        this.smallStId = smallStId;
    }

    public Student getLargeStId() {
        return largeStId;
    }

    public void setLargeStId(Student largeStId) {
        this.largeStId = largeStId;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
    }
}
