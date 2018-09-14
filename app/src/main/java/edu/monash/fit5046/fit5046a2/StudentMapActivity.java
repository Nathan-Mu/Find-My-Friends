package edu.monash.fit5046.fit5046a2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapquest.mapping.MapQuestAccountManager;
import com.mapquest.mapping.maps.OnMapReadyCallback;
import com.mapquest.mapping.maps.MapboxMap;
import com.mapquest.mapping.maps.MapView;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;

public class StudentMapActivity extends AppCompatActivity {

    private MapboxMap mMapboxMap;
    private MapView mMapView;
    private boolean init = false;
    private ArrayList<Location> allLocations;
    private ArrayList<Location> locations;
    private Icon iconUser, iconMale, iconFemale;
    private Spinner spDistance;
    private static final Integer[] DISTANCE_OPTIONS = {0, 1, 2, 5, 10, 50, 0};
    private static final String[] CHOOSE_OPTIONS = {"----Please choose----", "1km", "2km", "5km", "10km", "50km", "Any"};
    private ArrayAdapter<String> adapter;
    private LatLng position;
    private String currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(getApplicationContext());
        setContentView(R.layout.activity_student_map);

        setTitle("Maps");

        Intent intent = getIntent();
        allLocations = intent.getParcelableArrayListExtra("locations");
        locations = allLocations;

        IconFactory iconFactory = IconFactory.getInstance(StudentMapActivity.this);
        iconUser = iconFactory.fromResource(R.drawable.map_marker_red);
        iconMale = iconFactory.fromResource(R.drawable.map_marker_blue);
        iconFemale = iconFactory.fromResource(R.drawable.map_marker_pink);

        position = new LatLng(locations.get(0).getLatitude(), locations.get(0).getLongitude());
        currentPosition = locations.get(0).getLName();

        spDistance = (Spinner) findViewById(R.id.spDistance);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, CHOOSE_OPTIONS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDistance.setAdapter(adapter);
        mMapView = (MapView) findViewById(R.id.mapquestMapView);
        mMapView.onCreate(savedInstanceState);

        setMap();

        spDistance.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (init) {
                    init = false;
                } else if (position != 0 && position != 6) {
                    locations = new ArrayList<Location>();
                    locations.add(allLocations.get(0));
                    for (int i = 1; i < allLocations.size(); i++) {
                        Location l = allLocations.get(i);
                        Double distance = Distance.getDistance(l.getLatitude(), l.getLongitude(), locations.get(0).getLatitude(), locations.get(0).getLongitude());
                        if (distance <= (double) DISTANCE_OPTIONS[position])
                            locations.add(allLocations.get(i));
                    }
                } else {
                    locations = allLocations;
                }
                mMapboxMap.clear();
                setMap();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause()
    { super.onPause(); mMapView.onPause(); }

    @Override
    protected void onDestroy()
    { super.onDestroy(); mMapView.onDestroy(); }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    { super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    public void addMarker(MapboxMap mapboxMap, LatLng positionStudent, String locationName, String studentInfo, Icon icon)
    {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(positionStudent);
        markerOptions.title(locationName);
        markerOptions.snippet(studentInfo);
        markerOptions.icon(icon);
        mapboxMap.addMarker(markerOptions);
    }

    public void addAllStudentMarkers() {
        for (int i = 1; i < locations.size(); i++) {
            Student student = locations.get(i).getStId();
            LatLng position = new LatLng(locations.get(i).getLatitude(), locations.get(i).getLongitude());
            String dob = Time.toString(Time.toDate(student.getDob(), Time.JSON_DATE_FORMAT), "dd/MMM/yyyy");
            String name = student.getFName() + " " + student.getLName() + " (" + dob + ", " + student.getCourse() + ")";
            String locationName = locations.get(i).getLName();
            if (student.getGender().equals("m")) {
                addMarker(mMapboxMap, position, locationName, name, iconMale);
            } else {
                addMarker(mMapboxMap, position, locationName, name, iconFemale);
            }
        }
    }

    public void setMap() {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;
                mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
                addMarker(mMapboxMap, position, currentPosition, "Your Position", iconUser);
                addAllStudentMarkers();
            }
        });
    }
}
