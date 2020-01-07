package com.softwarica.googlemap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.softwarica.googlemap.model.LatitudeLongitude;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AutoCompleteTextView etCity;
    private Button btnSearch;
    private List<LatitudeLongitude> latitudeLongitudeList;
    Marker markerName;
    CameraUpdate center, zoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Obtain the SupportMapFragment and get notified when the map is aready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        etCity = findViewById(R.id.etCity);
        btnSearch = findViewById(R.id.btnSearch);

        fillArrayListAndSetAdapter();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etCity.getText().toString()))
                {
                    etCity.setError("Please enter a place name");
                    return;
                }

                //get the current location of the place
                int position = SearchArrayList(etCity.getText().toString());
                if(position > -1)
                    loadMap(position);
                else
                    Toast.makeText(SearchActivity.this, "Location not found by name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // This function will fill arraylist with static data and set autocompleteview with the marker
    private void fillArrayListAndSetAdapter()
    {
        latitudeLongitudeList = new ArrayList<>();
        latitudeLongitudeList.add(new LatitudeLongitude(27.7214325, 85.3619607, "Boudha Stupa"));
        latitudeLongitudeList.add(new LatitudeLongitude(27.722082, 85.3131371, "St.Xavier School" ));

        String[] data = new String[latitudeLongitudeList.size()];

        for (int i = 0; i < data.length; i++) {
            data[i] = latitudeLongitudeList.get(i).getMarker();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                SearchActivity.this,
                android.R.layout.simple_list_item_1,
                data
        );

        etCity.setAdapter(adapter); //setting marker values in autocompletetextview
        etCity.setThreshold(1);
    }

    //This function will check weather the location is in list or not
    public int SearchArrayList(String name) {
        for (int i = 0; i < latitudeLongitudeList.size(); i++) {
            if (latitudeLongitudeList.get(i).getMarker().contains(name)) {
                return 1;
            }
        }

        return -1;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        center = CameraUpdateFactory.newLatLng(new LatLng(27.706195, 85.3300396));
        zoom = CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    public void loadMap(int position) {
        //Remove old marker from map
        if(markerName!=null)
        {
            markerName.remove();
        }
        double latitude = latitudeLongitudeList.get(position).getLat();
        double longitude = latitudeLongitudeList.get(position).getLon();
        String marker = latitudeLongitudeList.get(position).getMarker();
        center = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
        zoom = CameraUpdateFactory.zoomTo(17);
        markerName = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude))
        .title(marker));

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}
