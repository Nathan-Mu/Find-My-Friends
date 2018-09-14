package edu.monash.fit5046.fit5046a2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private Button bnLogin;
    private Button bnSignUp;
    private EditText etUserName, etPassword;
    private CheckBox cbAutoLogin;
    private Boolean autoLogin = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("spsMonashFriendFinder", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Boolean login = sharedPreferences.getBoolean("autoLogin", false);

        if (!login) {
            setContentView(R.layout.activity_main);

            setTitle("Monash Friend Finder");

            bnSignUp = (Button) findViewById(R.id.bnSignUp);
            bnLogin = (Button) findViewById(R.id.bnLogin);
            etUserName = (EditText) findViewById(R.id.etUserName);
            etPassword = (EditText) findViewById(R.id.etPasswordLogin);
            cbAutoLogin = (CheckBox) findViewById(R.id.cbAutoLogin);



            Boolean justSignUp = sharedPreferences.getBoolean("noticeJustSignUp", false);
            if (justSignUp)
            {
                Intent intent = getIntent();
                etUserName.setText(intent.getStringExtra("username"));
                etPassword.setText(intent.getStringExtra("password"));
                editor.putBoolean("noticeJustSignUp", false);
                editor.commit();
            }

            cbAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    autoLogin = isChecked;
                }
            });

            bnSignUp.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
            });

            bnLogin.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    String username = etUserName.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();
                    String encryptedPassword = Encryption.encryptToSHA(password);
                    new AsyncTask<String, Void, String>() {
                        @Override
                        protected String doInBackground(String... params) {
                            return RestClient.getLoginInfo(params[0], params[1]);
                        }
                        @Override
                        protected void onPostExecute(String loginUserInfo) {
                            if (!loginUserInfo.isEmpty()) {
                                if (autoLogin) {
                                    JSONArray jsonArray = JSONReader.toJSONArray(loginUserInfo);
                                    JSONObject jsonObject = JSONReader.getFirstFromArray(jsonArray);
                                    try {
                                        // Here I do not choose to use the above method again for autologin and
                                        // use stId instead of Monash email, because I think writing username and password
                                        // directly in sharedPreferences file is not good to the security of user account. It might
                                        // be easy to read sharedPreferences file by a file reader in Android device.
                                        // But for most of people, even they have known user's stId and hashed password, they cannot
                                        // get access to user's account.
                                        int i = jsonObject.getInt("stId");
                                        editor.putInt("autoLoginStId", jsonObject.getInt("stId"));
                                        editor.putString("autoLoginPassword", jsonObject.getString("password"));
                                        editor.putBoolean("autoLogin", true);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        editor.commit();
                                    }
                                }

                                JSONArray jsonArray = JSONReader.toJSONArray(loginUserInfo);
                                JSONObject json = JSONReader.getFirstFromArray(jsonArray);
                                Student student = new Student(json);

                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                intent.putExtra("user", student);
                                startActivity(intent);
                                finish();
                            } else {
                                etPassword.setText("");
                                showText("Username and password not matched.");
                            }
                        }
                    }.execute(new String[] {username, encryptedPassword});
                }
            });
        }
        else {
            String stId = String.valueOf(sharedPreferences.getInt("autoLoginStId", 0));
            String password = sharedPreferences.getString("autoLoginPassword", "");
            new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... params) {
                    return RestClient.getAutoLoginInfo(params[0], params[1]);
                }
                @Override
                protected void onPostExecute(String loginUserInfo) {
                    if (!loginUserInfo.isEmpty()) {
                        JSONArray jsonArray = JSONReader.toJSONArray(loginUserInfo);
                        JSONObject json = JSONReader.getFirstFromArray(jsonArray);
                        Student student = new Student(json);

                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra("user", student);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            editor.putInt("autoLoginStId", 0);
                            editor.putString("autoLoginPassword", "");
                            editor.putBoolean("autoLogin", false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            editor.commit();
                        }
                        showText("Your password has been updated. Please log in again.");
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }.execute(new String[] {stId, password});
        }
    }

    public void showText(String string) {
        Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
    }
}
