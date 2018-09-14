package edu.monash.fit5046.fit5046a2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static edu.monash.fit5046.fit5046a2.Time.getCurrentDate;

public class ViewActivity extends AppCompatActivity {

    private TextView tvName, tvDob, tvGender, tvCourse, tvStudyMode, tvAddress, tvSuburb, tvNationality, tvNativeLanguage, tvFSport, tvFMovie, tvFUnit, tvCurrentJob;
    private Button bn, bnLearnMore;
    private Student user;
    private Student student;
    private Friendship friendship;
    private boolean isFromFriendFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        setTitle("View");

        Intent intent = this.getIntent();
        user = intent.getParcelableExtra("user");
        student = intent.getParcelableExtra("selectedStudent");
        friendship = intent.getParcelableExtra("friendship");
        isFromFriendFragment = intent.getBooleanExtra("isFromFriendFragment", false);

        tvName = (TextView) findViewById(R.id.vName);
        tvDob = (TextView) findViewById(R.id.vDob);
        tvGender = (TextView) findViewById(R.id.vGender);
        tvCourse = (TextView) findViewById(R.id.vCourse);
        tvStudyMode = (TextView) findViewById(R.id.vStudyMode);
        tvAddress = (TextView) findViewById(R.id.vAddress);
        tvSuburb = (TextView) findViewById(R.id.vSuburb);
        tvNationality = (TextView) findViewById(R.id.vNationality);
        tvNativeLanguage = (TextView) findViewById(R.id.vNativeLanguage);
        tvFSport = (TextView) findViewById(R.id.vFSport);
        tvFMovie = (TextView) findViewById(R.id.vFMoive);
        tvFUnit = (TextView) findViewById(R.id.vFUnit);
        tvCurrentJob = (TextView) findViewById(R.id.vCurrentJob);
        bnLearnMore = (Button) findViewById(R.id.bnLearnMore);
        bn = (Button) findViewById(R.id.bn);

        setViewData();

        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if they are not friend
                if (friendship.getFriendshipId() == -1) {
                    final Friendship newFriendship;
                    if (user.getStId() > student.getStId()) {
                        newFriendship = new Friendship(student, user, Time.toString(getCurrentDate(), Time.JSON_DATE_FORMAT), null);
                    } else {
                        newFriendship = new Friendship(user, student, Time.toString(getCurrentDate(), Time.JSON_DATE_FORMAT), null);
                    }
                    addFriend(newFriendship);
                } else if (friendship.getFriendshipId() == 0) {
                    // not friend and last view is Friends screen
                    new AsyncTask<Integer, Void, Friendship>() {
                        @Override
                        protected Friendship doInBackground(Integer... params) {
                            return RestClient.getFriendship(params[0], params[1]);
                        }

                        @Override
                        protected void onPostExecute(Friendship existFriendship) {
                            friendship = existFriendship;
                            deleteFriend();
                        }
                    }.execute(user.getStId(), student.getStId());
                } else {
                    // already get Friendship data from server
                    deleteFriend();
                }
            }
        });

        bnLearnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ViewActivity.this, MovieActivity.class);
                intent1.putExtra("fMovie", student.getFMoive());
                startActivity(intent1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isFromFriendFragment) {
            Intent newIntent = new Intent(this, HomeActivity.class);
            newIntent.putExtra("restartFriendFragment", true);
            newIntent.putExtra("user", user);
            startActivity(newIntent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    public void showText(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    public void addFriend(final Friendship friendship) {
        new AsyncTask<Friendship, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Friendship... params) {
                return RestClient.createFriendship(params[0]);
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    friendship.setFriendshipId(1);
                    showText("Add friend successfully.");
                    bn.setText("Unfriend?");
                }
                else
                    showText("Failed. Please try again");
            }
        }.execute(friendship);
    }

    public void deleteFriend() {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                friendship.setEndingDate(Time.toString(getCurrentDate(), Time.JSON_DATE_FORMAT));
                return RestClient.endFriendship(friendship);
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    friendship = new Friendship();
                    friendship.setFriendshipId(-1);
                    showText("Delete successfully.");
                    bn.setText("Add friend?");
                }
                else
                    showText("Failed. Please try again");
            }
        }.execute();
    }

    public void setViewData() {
        if (friendship.getFriendshipId() != -1) {
            bn.setText("Unfriend?");
        }

        tvName.setText(student.getFName() + " " + student.getLName());
        tvDob.setText("Date of birth: " + Time.toString(Time.toDate(student.getDob(), "yyyy-MM-dd'T'HH:mm:ssZZZZZ"), "dd/MMM/yyyy"));
        if (student.getGender().equals("m")) {
            tvGender.setText("Gender: male");
        } else {
            tvGender.setText("Gender: female");
        }
        tvCourse.setText("Course: " + student.getCourse());
        if (student.getStudyMode().equals("f")) {
            tvStudyMode.setText("Study Mode: full-time");
        } else {
            tvStudyMode.setText("Study Mode: part-time");
        }
        tvAddress.setText("Address: " + student.getAddress());
        tvSuburb.setText("Suburb: " + student.getSuburb());
        tvNationality.setText("Nationality: " + student.getNationality());
        tvNativeLanguage.setText("Native language: " + student.getNativeLanguage());
        tvFSport.setText("Favourite sports: " + student.getFSport());
        tvFMovie.setText("Favourite movie: " + student.getFMoive());
        tvFUnit.setText("Favourite unit: " + student.getFUnit());
        tvCurrentJob.setText("Current job: " + student.getCurrentJob());
    }
}
