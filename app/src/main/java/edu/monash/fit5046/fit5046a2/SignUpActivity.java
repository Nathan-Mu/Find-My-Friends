package edu.monash.fit5046.fit5046a2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    protected CountryDBManager dbManager;
    private int year, month, date;
    private Button bnSetDate, bnNext;
    private TextView dateDisplay;
    private EditText etFN, etLN, etAddress, etFMoive, etCurrentJob;
    private RadioButton rbMale, rbFemale, rbPartTime, rbFullTime;
    private String datePick = "";
    private Spinner spCountry, spCourse, spSuburb, spNativeLanguage, spFSport, spFUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("Sign up");

        SharedPreferences sharedPreferences = getSharedPreferences("countryNameExist", Context.MODE_PRIVATE);
        Boolean countryNameExist = sharedPreferences.getBoolean("countryNameExist", false);

        dbManager = new CountryDBManager(this);

        if (!countryNameExist) {
            try {
                dbManager.open();
                dbManager.initialize();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("countryNameExist", true);
                editor.commit();
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
            finally {
                dbManager.close();
            }
        }

        bnSetDate = (Button) findViewById(R.id.bnSetDate);
        bnNext = (Button) findViewById(R.id.bnNext);
        dateDisplay = (TextView) findViewById(R.id.tvDob);
        etFN = (EditText) findViewById(R.id.etFN);
        etLN = (EditText) findViewById(R.id.etLN);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etFMoive = (EditText) findViewById(R.id.etFavouriteMoive);
        etCurrentJob = (EditText) findViewById(R.id.etCurrentJob);
        spCourse = (Spinner) findViewById(R.id.spCourse);
        spFSport = (Spinner) findViewById(R.id.spFavouriteSport);
        spFUnit =(Spinner) findViewById(R.id.spFavouriteUnit);
        spSuburb = (Spinner) findViewById(R.id.spSuburb);
        spNativeLanguage = (Spinner) findViewById(R.id.spNativeLanguage);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFullTime = (RadioButton) findViewById(R.id.rbFullTime);
        rbPartTime = (RadioButton) findViewById(R.id.rbPartTime);

        add_list();

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        date = c.get(Calendar.DAY_OF_MONTH);

        bnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignUpActivity.this, dateListener, year, month, date).show();
            }
        });

        bnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String firstName = etFN.getText().toString();
                String lastName = etLN.getText().toString();
                String dob = datePick;
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

                if (firstName.isEmpty() || firstName.length() > 20) {
                    showText("First name cannot be empty or longer than 20 characters");
                } else if (lastName.isEmpty() || lastName.length() > 20){
                    showText("Last name cannot be empty or longer than 20 characters");
                } else if (dob.isEmpty()) {
                    showText("Please choose date of birth");
                } else if (gender.isEmpty()) {
                    showText("Please choose gender");
                } else if (!spinnerChosen(course)) {
                    showText("Please choose course");
                } else if (studyMode.isEmpty()) {
                    showText("Please choose study mode");
                } else if (address.isEmpty() || address.length() > 255) {
                    showText("Address cannot be empty or longer than 255 characters");
                } else if (!spinnerChosen(suburb)) {
                    showText("Please choose a suburb");
                } else if (!spinnerChosen(nationality)) {
                    showText("Please choose a nationality");
                } else if (!spinnerChosen(nativeLanguage)) {
                    showText("Please choose a native language");
                } else if (!spinnerChosen(fSport)) {
                    showText("Please choose a favourite sport");
                } else if (fMoive.isEmpty() || fMoive.length() > 255) {
                    showText("Favourite movie cannot be empty or longer than 255 characters");
                } else if (!spinnerChosen(fUnit)) {
                    showText("Please choose a favourite unit");
                } else if (currentJob.length() > 255) {
                    showText("Current Job cannot be longer than 255 characters");
                } else {
                    Student student = new Student(firstName, lastName, Time.toString(Time.toDate(dob,"dd/MM/yyyy"), Time.JSON_DATE_FORMAT), gender, course, studyMode,
                            address, suburb, nationality, nativeLanguage, fSport, fMoive, fUnit, currentJob);

                    Intent intent = new Intent(SignUpActivity.this, SignUp2Activity.class);
                    intent.putExtra("studentSetFirstPage", student);
                    startActivity(intent);
                }
            }
        });
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
        Toast.makeText(SignUpActivity.this, string, Toast.LENGTH_SHORT).show();
    }

    private void add_list() {
        spCountry = (Spinner) findViewById(R.id.spNationality);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, readCountryNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCountry.setAdapter(adapter);
    }

    public boolean spinnerChosen(String string)
    {
        if (string.equals("----Please Choose----"))
            return false;
        else
            return true;
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
