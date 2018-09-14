package edu.monash.fit5046.fit5046a2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nathan on 1/5/17.
 */

public class UpdateFragment extends Fragment {
    View vUpdate;
    protected CountryDBManager dbManager;
    private int year, month, date;
    private Button bnSetDate, bnNext;
    private TextView dateDisplay;
    private EditText etFN, etLN, etAddress, etFMoive, etCurrentJob;
    private RadioButton rbMale, rbFemale, rbPartTime, rbFullTime;
    private String datePick = "";
    private Spinner spCountry, spCourse, spSuburb, spNativeLanguage, spFSport, spFUnit;
    private Student user;
    private Intent intent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vUpdate = inflater.inflate(R.layout.activity_sign_up, container, false);

        this.getActivity().setTitle("Update");

        intent = getActivity().getIntent();
        user = intent.getParcelableExtra("user");

        bnSetDate = (Button) vUpdate.findViewById(R.id.bnSetDate);
        bnNext = (Button) vUpdate.findViewById(R.id.bnNext);
        dateDisplay = (TextView) vUpdate.findViewById(R.id.tvDob);
        etFN = (EditText) vUpdate.findViewById(R.id.etFN);
        etLN = (EditText) vUpdate.findViewById(R.id.etLN);
        etAddress = (EditText) vUpdate.findViewById(R.id.etAddress);
        etFMoive = (EditText) vUpdate.findViewById(R.id.etFavouriteMoive);
        etCurrentJob = (EditText) vUpdate.findViewById(R.id.etCurrentJob);
        spCourse = (Spinner) vUpdate.findViewById(R.id.spCourse);
        spFSport = (Spinner) vUpdate.findViewById(R.id.spFavouriteSport);
        spFUnit =(Spinner) vUpdate.findViewById(R.id.spFavouriteUnit);
        spSuburb = (Spinner) vUpdate.findViewById(R.id.spSuburb);
        spNativeLanguage = (Spinner) vUpdate.findViewById(R.id.spNativeLanguage);
        rbFemale = (RadioButton) vUpdate.findViewById(R.id.rbFemale);
        rbMale = (RadioButton) vUpdate.findViewById(R.id.rbMale);
        rbFullTime = (RadioButton) vUpdate.findViewById(R.id.rbFullTime);
        rbPartTime = (RadioButton) vUpdate.findViewById(R.id.rbPartTime);

        dbManager = new CountryDBManager(this.getActivity());
        add_list();

        etFN.setText(user.getFName());
        etLN.setText(user.getLName());
        dateDisplay.setText("Date of birth: " + Time.toString(Time.toDate(user.getDob(), "yyyy-MM-dd'T'hh:mm:ssZZZZZ"), "dd/MM/yyyy"));
        switch (user.getGender()) {
            case "f": rbFemale.setChecked(true); break;
            case "m": rbMale.setChecked(true); break;
            default: break;
        }
        spCourse.setSelection(SpinnerTools.getPosition(spCourse, user.getCourse()));
        switch (user.getStudyMode()) {
            case "f": rbFullTime.setChecked(true); break;
            case "p": rbPartTime.setChecked(true); break;
            default: break;
        }
        etAddress.setText(user.getAddress());
        spSuburb.setSelection(SpinnerTools.getPosition(spSuburb, user.getSuburb()));
        spCountry.setSelection(SpinnerTools.getPosition(spCountry, user.getNationality()));
        spNativeLanguage.setSelection(SpinnerTools.getPosition(spNativeLanguage, user.getNativeLanguage()));
        spFSport.setSelection(SpinnerTools.getPosition(spFSport, user.getFSport()));
        etFMoive.setText(user.getFMoive());
        spFUnit.setSelection(SpinnerTools.getPosition(spFUnit, user.getFUnit()));
        etCurrentJob.setText(user.getCurrentJob());
        bnNext.setText("Confirm");

        year = Integer.valueOf(Time.toString(Time.toDate(user.getDob(), "yyyy-MM-dd'T'hh:mm:ssZZZZZ"), "yyyy"));
        month = Integer.valueOf(Time.toString(Time.toDate(user.getDob(), "yyyy-MM-dd'T'hh:mm:ssZZZZZ"), "MM")) - 1;
        date = Integer.valueOf(Time.toString(Time.toDate(user.getDob(), "yyyy-MM-dd'T'hh:mm:ssZZZZZ"), "dd"));

        final Activity activity = this.getActivity();
        bnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(activity, dateListener, year, month, date).show();
            }
        });

        bnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String firstName = etFN.getText().toString();
                String lastName = etLN.getText().toString();
                String dob;
                if (datePick.isEmpty())
                    dob = Time.toString(Time.toDate(user.getDob(), "yyyy-MM-dd'T'hh:mm:ssZZZZZ"), "dd/MM/yyyy");
                else
                    dob = datePick;
                String gender;
                if (rbMale.isChecked()) {
                    gender = "m";
                } else if (rbFemale.isChecked()) {
                    gender = "f";
                } else {
                    gender = "";
                }
                String course = spCourse.getSelectedItem().toString();
                String studyMode;
                if (rbFullTime.isChecked()) {
                    studyMode = "f";
                } else if (rbPartTime.isChecked()) {
                    studyMode = "p";
                } else {
                    studyMode = "";
                }
                String address = etAddress.getText().toString();
                String suburb = spSuburb.getSelectedItem().toString();
                String nationality = spCountry.getSelectedItem().toString();
                String nativeLanguage = spNativeLanguage.getSelectedItem().toString();
                String fSport = spFSport.getSelectedItem().toString();
                String fMoive = etFMoive.getText().toString();
                String fUnit = spFUnit.getSelectedItem().toString();
                String currentJob = etCurrentJob.getText().toString();
                if (currentJob.isEmpty()) {
                    currentJob = "unemployed";
                }

                if (Validation.validatePersonalInfo(firstName, lastName, dob, gender, course, studyMode,
                        address, suburb, nationality, nativeLanguage, fSport, fMoive, fUnit, currentJob, activity)) {
                    user.setFName(firstName);
                    user.setLName(lastName);
                    user.setDob(Time.toString(Time.toDate(dob, "dd/MM/yyyy"), "yyyy-MM-dd'T'hh:mm:ssZZZZZ"));
                    user.setGender(gender);
                    user.setCourse(course);
                    user.setStudyMode(studyMode);
                    user.setAddress(address);
                    user.setSuburb(suburb);
                    user.setNationality(nationality);
                    user.setNativeLanguage(nativeLanguage);
                    user.setFSport(fSport);
                    user.setFMoive(fMoive);
                    user.setFUnit(fUnit);
                    user.setCurrentJob(currentJob);
                    new AsyncTask<Student, Void, Boolean>() {
                        @Override
                        protected Boolean doInBackground(Student... params) {
                            return RestClient.updateStudent(params[0]);
                        }

                        @Override
                        protected void onPostExecute(Boolean isStudentCreated) {
                            if (isStudentCreated) {
                                //change intent
                                intent.putExtra("user", user);
                                showText("Update successfully!");
                            } else {
                                showText("Update failed. Please try again.");
                            }
                        }
                    }.execute(user);
                }
            }
        });

        return vUpdate;
    }

    private void add_list() {
        spCountry = (Spinner) vUpdate.findViewById(R.id.spNationality);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, readCountryNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCountry.setAdapter(adapter);
    }

    public String[] readCountryNames() {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            dbManager.open();
            Cursor cursor = dbManager.getAllCountries();
            if(cursor.moveToFirst()) {
                do{
                    cursor.getString(1);
                    arrayList.add(cursor.getString(1));
                } while(cursor.moveToNext());
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            dbManager.close();
        }
        String[] array=new String[arrayList.size() + 1];
        array[0] = "----Please Choose----";
        for(int i = 0; i < arrayList.size(); i++){
            array[i + 1] = arrayList.get(i);
        }
        return array;
    }

    public void showText(String string)
    {
        Toast.makeText(this.getActivity(), string, Toast.LENGTH_SHORT).show();
    }

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int newYear, int newMonth, int newDate) {
            year = newYear;
            month = newMonth;
            date = newDate;
            datePick = date + "/" + Integer.toString(month + 1) + "/" + year;
            Date temp = Time.toDate(datePick, "dd/MM/yyyy");
            if (temp.after(Time.getCurrentDate()))
            {
                showText("Please choose a correct date of birth.");
            }
            else {
                dateDisplay.setText("Date of birth: " + datePick);
            }
        }
    };
}
