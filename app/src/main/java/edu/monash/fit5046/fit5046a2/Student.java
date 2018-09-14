package edu.monash.fit5046.fit5046a2;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static edu.monash.fit5046.fit5046a2.JSONReader.getIntegerFromJSONObject;
import static edu.monash.fit5046.fit5046a2.JSONReader.getStringFromJSONObject;

/**
 * Created by nathan on 29/4/17.
 */

public class Student implements Parcelable{
    private Integer stId = -1;
    private String FName = null;
    private String LName = null;
    private String dob = null;
    private String gender = null;
    private String course = null;
    private String studyMode = null;
    private String address = null;
    private String suburb = null;
    private String nationality = null;
    private String nativeLanguage = null;
    private String FSport = null;
    private String FMoive = null;
    private String FUnit = null;
    private String currentJob = null;
    private String monashEmail = null;
    private String password = null;
    private String subscriptionDate = null;
    private String subscriptionTime = null;

    public Student() {
        this.stId = 0;
    }

    public Student(String fName, String lName, String dob, String gender,
                                    String course, String studyMode, String address, String suburb,
                                    String nationality, String nativeLanguage, String fSport,
                                    String fMoive, String fUnit, String currentJob)
    {
        this.stId = 1;
        this.FName = fName;
        this.LName = lName;
        this.dob = dob;
        this.gender = gender;
        this.course = course;
        this.studyMode = studyMode;
        this.address = address;
        this.suburb = suburb;
        this.nationality = nationality;
        this.nativeLanguage = nativeLanguage;
        this.FSport = fSport;
        this.FMoive = fMoive;
        this.FUnit = fUnit;
        this.currentJob = currentJob;
        this.monashEmail = null;
        this.password = null;
        this.subscriptionDate = null;
        this.subscriptionTime = null;
    }

    public Student(JSONObject jsonObject)
    {
        this.stId = getIntegerFromJSONObject(jsonObject, "stId");
        this.FName = getStringFromJSONObject(jsonObject, "FName");
        this.LName = getStringFromJSONObject(jsonObject, "LName");
        this.dob = getStringFromJSONObject(jsonObject, "dob");
        this.gender = getStringFromJSONObject(jsonObject, "gender");
        this.course = getStringFromJSONObject(jsonObject, "course");
        this.studyMode = getStringFromJSONObject(jsonObject, "studyMode");
        this.address = getStringFromJSONObject(jsonObject, "address");
        this.suburb = getStringFromJSONObject(jsonObject, "suburb");
        this.nationality = getStringFromJSONObject(jsonObject, "nationality");
        this.nativeLanguage = getStringFromJSONObject(jsonObject, "nativeLanguage");
        this.FSport = getStringFromJSONObject(jsonObject, "FSport");
        this.FMoive = getStringFromJSONObject(jsonObject, "FMoive");
        this.FUnit = getStringFromJSONObject(jsonObject, "FUnit");
        this.currentJob = getStringFromJSONObject(jsonObject, "currentJob");
        this.monashEmail = getStringFromJSONObject(jsonObject, "monashEmail");
        this.password = getStringFromJSONObject(jsonObject, "password");
        this.subscriptionDate = getStringFromJSONObject(jsonObject, "subscriptionDate");
        this.subscriptionTime = getStringFromJSONObject(jsonObject, "subscriptionTime");
    }

    protected Student(Parcel in)
    {
        stId = in.readInt();
        FName = in.readString();
        LName = in.readString();
        dob = in.readString();
        gender = in.readString();
        course = in.readString();
        studyMode = in.readString();
        address = in.readString();
        suburb = in.readString();
        nationality = in.readString();
        nativeLanguage = in.readString();
        FSport = in.readString();
        FMoive = in.readString();
        FUnit = in.readString();
        currentJob = in.readString();
        monashEmail = in.readString();
        password = in.readString();
        subscriptionDate = in.readString();
        subscriptionTime = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(stId);
        parcel.writeString(FName);
        parcel.writeString(LName);
        parcel.writeString(dob);
        parcel.writeString(gender);
        parcel.writeString(course);
        parcel.writeString(studyMode);
        parcel.writeString(address);
        parcel.writeString(suburb);
        parcel.writeString(nationality);
        parcel.writeString(nativeLanguage);
        parcel.writeString(FSport);
        parcel.writeString(FMoive);
        parcel.writeString(FUnit);
        parcel.writeString(currentJob);
        parcel.writeString(monashEmail);
        parcel.writeString(password);
        parcel.writeString(subscriptionDate);
        parcel.writeString(subscriptionTime);
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public void setSecondPageContent(String monashEmail, String password,
                                     String subscriptionDate, String subscriptionTime)
    {
        this.monashEmail = monashEmail;
        this.password = Encryption.encryptToSHA(password);
        this.subscriptionDate = subscriptionDate;
        this.subscriptionTime = subscriptionTime;
    }

    public Integer getStId() {
        return stId;
    }

    public void setStId(Integer stId) {
        this.stId = stId;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getStudyMode() {
        return studyMode;
    }

    public void setStudyMode(String studyMode) {
        this.studyMode = studyMode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public String getFSport() {
        return FSport;
    }

    public void setFSport(String FSport) {
        this.FSport = FSport;
    }

    public String getFMoive() {
        return FMoive;
    }

    public void setFMoive(String FMoive) {
        this.FMoive = FMoive;
    }

    public String getFUnit() {
        return FUnit;
    }

    public void setFUnit(String FUnit) {
        this.FUnit = FUnit;
    }

    public String getCurrentJob() {
        return currentJob;
    }

    public void setCurrentJob(String currentJob) {
        this.currentJob = currentJob;
    }

    public String getMonashEmail() {
        return monashEmail;
    }

    public void setMonashEmail(String monashEmail) {
        this.monashEmail = monashEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(String subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public String getSubscriptionTime() {
        return subscriptionTime;
    }

    public void setSubscriptionTime(String subscriptionTime) {
        this.subscriptionTime = subscriptionTime;
    }
}

