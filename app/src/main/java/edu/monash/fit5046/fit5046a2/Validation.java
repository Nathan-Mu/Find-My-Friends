package edu.monash.fit5046.fit5046a2;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by nathan on 1/5/17.
 */

public class Validation {
    public static boolean validatePersonalInfo(String firstName, String lastName, String dob, String gender,
                                        String course, String studyMode, String address, String suburb,
                                        String nationality, String nativeLanguage, String fSport,
                                        String fMoive, String fUnit, String currentJob, Context context)
    {
        Boolean isValid = false;
        if (firstName.isEmpty() || firstName.length() > 20) {
            showText("First name cannot be empty or longer than 20 characters", context);
        } else if (lastName.isEmpty() || lastName.length() > 20){
            showText("Last name cannot be empty or longer than 20 characters", context);
        } else if (dob.isEmpty()) {
            showText("Please choose date of birth", context);
        } else if (gender.isEmpty()) {
            showText("Please choose gender", context);
        } else if (!spinnerChosen(course)) {
            showText("Please choose course", context);
        } else if (studyMode.isEmpty()) {
            showText("Please choose study mode", context);
        } else if (address.isEmpty() || address.length() > 255) {
            showText("Address cannot be empty or longer than 255 characters", context);
        } else if (!spinnerChosen(suburb)) {
            showText("Please choose a suburb", context);
        } else if (!spinnerChosen(nationality)) {
            showText("Please choose a nationality", context);
        } else if (!spinnerChosen(nativeLanguage)) {
            showText("Please choose a native language", context);
        } else if (!spinnerChosen(fSport)) {
            showText("Please choose a favourite sport", context);
        } else if (fMoive.isEmpty() || fMoive.length() > 255) {
            showText("Favourite movie cannot be empty or longer than 255 characters", context);
        } else if (!spinnerChosen(fUnit)) {
            showText("Please choose a favourite unit", context);
        } else if (currentJob.length() > 255) {
            showText("Current Job cannot be longer than 255 characters", context);
        } else {
            isValid = true;
        }
        return isValid;
    }

    public static boolean spinnerChosen(String string)
    {
        if (string.equals("----Please Choose----"))
            return false;
        else
            return true;
    }

    public static void showText(String string, Context context)
    {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }
}
