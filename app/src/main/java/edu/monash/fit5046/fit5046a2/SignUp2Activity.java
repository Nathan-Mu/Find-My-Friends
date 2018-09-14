package edu.monash.fit5046.fit5046a2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp2Activity extends AppCompatActivity {

    private EditText etMonashEmail, etPassword, etConfirmPassword;
    private Button bnConfirm;
    private Student student;
    private String password, monashEmail, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        setTitle("Sign up");

        Intent intent = getIntent();
        student = intent.getParcelableExtra("studentSetFirstPage");

        etMonashEmail = (EditText) findViewById(R.id.etMonashEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        bnConfirm = (Button) findViewById(R.id.bnConfirm);

        bnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monashEmail = etMonashEmail.getText().toString();
                password = etPassword.getText().toString();
                confirmPassword = etConfirmPassword.getText().toString();

                new AsyncTask<String, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(String... params) {
                        return RestClient.checkEmailExist(params[0]);
                    }

                    @Override
                    protected void onPostExecute(Boolean emailAddressExist) {
                        if (emailAddressExist) {
                            showText("Email address has been registered");
                        }else if (password.isEmpty()) {
                            showText("Password cannot be empty");
                        } else if (password.length() < 8) {
                            showText("Password should be longer than 8 characters");
                        } else if (!password.equals(confirmPassword)) {
                            showText("Passwords not matched");
                        } else {
                            student.setSecondPageContent(monashEmail, password, Time.toString(Time.getCurrentDate(), Time.JSON_DATE_FORMAT),
                                    Time.toString(Time.getCurrentTime(), Time.JSON_DATE_FORMAT));
                            new AsyncTask<Student, Void, Boolean>() {
                                @Override
                                protected Boolean doInBackground(Student... params) {
                                    return RestClient.createStudent(params[0]);
                                }

                                @Override
                                protected void onPostExecute(Boolean isStudentCreated) {
                                    if (isStudentCreated) {
                                        Intent intent = new Intent(SignUp2Activity.this, MainActivity.class);
                                        intent.putExtra("username", monashEmail);
                                        intent.putExtra("password", password);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                        SharedPreferences spsNoticeJustSignUp = getSharedPreferences("noticeJustSignUp", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = spsNoticeJustSignUp.edit();
                                        editor.putBoolean("noticeJustSignUp", true);
                                        editor.commit();

                                        startActivity(intent);
                                        finish();
                                    } else {
                                        showText("Sign up failed. Please try again.");
                                    }
                                }
                            }.execute(student);
                        }
                    }
                }.execute(monashEmail);
            }
        });
    }

    public void showText(String string)
    {
        Toast.makeText(SignUp2Activity.this, string, Toast.LENGTH_SHORT).show();
    }
}
