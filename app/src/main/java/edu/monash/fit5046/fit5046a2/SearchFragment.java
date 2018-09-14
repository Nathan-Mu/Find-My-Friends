package edu.monash.fit5046.fit5046a2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nathan on 2/5/17.
 */

public class SearchFragment extends Fragment {
    View vSearch;
    private Button bnSpinner, bnShowMap;
    private List<String> chosenList = new ArrayList<>();
    private TextView tvAttributesChosen;
    private Student user;
    private StudentAdapter adapter;
    private List<Student> filteredStudentsList = new ArrayList<>();
    ListView lvFilteredStudents;

    private String[] attributes = {"first name", "last name", "date of birth", "gender", "course", "study mode", "address", "suburb", "nationality", "nativeLanguage", "favourite sport", "favourite movie", "favourite unit", "current job"};
    private String[] jsonAttributes = {"fName", "lName", "dob", "gender", "course", "studyMode", "address", "suburb", "nationality", "nativeLanguage", "fSport", "fMoive", "fUnit", "currentJob"};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vSearch = inflater.inflate(R.layout.fragment_search, container, false);

        this.getActivity().setTitle("Search");

        final Intent intent = getActivity().getIntent();
        user = intent.getParcelableExtra("user");

        bnSpinner = (Button) vSearch.findViewById(R.id.bnSpinner);
        bnShowMap = (Button) vSearch.findViewById(R.id.bnShowMap);
        tvAttributesChosen = (TextView) vSearch.findViewById(R.id.tvAttributes);

        lvFilteredStudents = (ListView) vSearch.findViewById(R.id.lvFilteredStudents);

        bnSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(vSearch.getContext());

                final boolean[] checked = new boolean[attributes.length];
                for (boolean b : checked)
                    b = false;
                //first param: attributes shown to choose
                //second param: which attribute should be checked at first
                //third param: listener
                builder.setMultiChoiceItems(attributes, checked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i, boolean isChecked) {
                        if (isChecked) {
                            checked[i] = true;
                        }
                    }
                });
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String chosenString = "";
                        chosenList = new ArrayList<String>();
                        for (int i = 0; i < checked.length; i++) {
                            if (checked[i]) {
                                chosenList.add(jsonAttributes[i]);
                                chosenString += attributes[i] + ", ";
                            }
                        }
                        chosenString = chosenString.substring(0, chosenString.length() - 2);
                        tvAttributesChosen.setText("Attributes chosen: " + chosenString);
                        bnSpinner.setText("Re-choose attributes");

                        new AsyncTask<List<String>, Void, List<Student>>() {

                            @Override
                            protected List<Student> doInBackground(List<String>... params) {
                                return RestClient.getFilteredStudent(user.getStId(), params[0]);
                            }

                            @Override
                            protected void onPostExecute(List<Student> studentList) {
                                filteredStudentsList = studentList;
                                adapter = new StudentAdapter(vSearch.getContext(), filteredStudentsList);
                                lvFilteredStudents.setAdapter(adapter);
                            }
                        }.execute(chosenList);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        bnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!filteredStudentsList.isEmpty()) {
                    String stIds = user.getStId() + ",";
                    for (Student s : filteredStudentsList) {
                        stIds += s.getStId() + ",";
                    }
                    stIds = stIds.substring(0, stIds.length() - 1);
                    new AsyncTask<String, Void, ArrayList<Location>>() {
                        @Override
                        protected ArrayList<Location> doInBackground(String... params) {
                            return RestClient.getLocation(params[0]);
                        }

                        @Override
                        protected void onPostExecute(ArrayList<Location> locations) {
                            Intent intent1 = new Intent(vSearch.getContext(), StudentMapActivity.class);
                            intent1.putParcelableArrayListExtra("locations", locations);
                            startActivity(intent1);
                        }
                    }.execute(stIds);

                } else {
                    showText("No student matched.");
                }
            }
        });

        lvFilteredStudents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student student = filteredStudentsList.get(position);
                final Intent newIntent = new Intent(vSearch.getContext(), ViewActivity.class);
                newIntent.putExtra("selectedStudent", student);
                newIntent.putExtra("user", user);
                new AsyncTask<Integer, Void, Friendship>() {
                    @Override
                    protected Friendship doInBackground(Integer... params) {
                        return RestClient.getFriendship(params[0], params[1]);
                    }

                    @Override
                    protected void onPostExecute(Friendship friendship) {
                        newIntent.putExtra("friendship", friendship);
                        newIntent.putExtra("isFromFriendFragment", false);
                        startActivity(newIntent);
                    }
                }.execute(user.getStId(), student.getStId());
            }
        });
        return vSearch;
    }

    public void showText(String string)
    {
        Toast.makeText(vSearch.getContext(), string, Toast.LENGTH_SHORT).show();
    }
}
