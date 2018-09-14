package edu.monash.fit5046.fit5046a2;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static edu.monash.fit5046.fit5046a2.Time.getCurrentDate;

/**
 * Created by nathan on 3/5/17.
 */

public class FriendsFragment extends Fragment {
    View vFriends;
    private Student user;
    private List<Student> friendsList = new ArrayList<>();
    private ListView lvFriends;
    private Button bnShowOnMap;
    private StudentAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vFriends = inflater.inflate(R.layout.fragment_friends, container, false);

        this.getActivity().setTitle("Friends");

        Intent intent = getActivity().getIntent();
        user = intent.getParcelableExtra("user");

        lvFriends = (ListView) vFriends.findViewById(R.id.lvFriends);
        bnShowOnMap = (Button) vFriends.findViewById(R.id.bnShowOnMap);

        new AsyncTask<Integer, Void, List<Student>>() {
            @Override
            protected List<Student> doInBackground(Integer... params) {
                return RestClient.getFriendsList(params[0]);
            }

            @Override
            protected void onPostExecute(List<Student> friends) {
                friendsList = friends;
                adapter = new StudentAdapter(vFriends.getContext(), friendsList);
                lvFriends.setAdapter(adapter);
            }
        }.execute(user.getStId());

        lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student student = friendsList.get(position);
                final Intent newIntent = new Intent(vFriends.getContext(), ViewActivity.class);
                newIntent.putExtra("user", user);
                newIntent.putExtra("selectedStudent", student);
                // because this student must be a friend. So here I don't get friendship.
                // If user want to delete or re-add friend, it will get friendship object
                // in next activity.
                Friendship friendship = new Friendship();
                newIntent.putExtra("friendship", friendship);
                newIntent.putExtra("isFromFriendFragment", true);
                startActivity(newIntent);
                getActivity().finish();
            }
        });

        bnShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!friendsList.isEmpty()) {
                    String stIds = user.getStId() + ",";
                    for (Student s : friendsList) {
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
                            Intent intent1 = new Intent(vFriends.getContext(), StudentMapActivity.class);
                            intent1.putParcelableArrayListExtra("locations", locations);
                            startActivity(intent1);
                        }
                    }.execute(stIds);

                } else {
                    showText("No student matched.");
                }
            }
        });

        return vFriends;
    }

    public void showText(String string)
    {
        Toast.makeText(vFriends.getContext(), string, Toast.LENGTH_SHORT).show();
    }

    public void deleteFriend() {

    }
}
