package com.example.do_i_need_it.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.do_i_need_it.R;

public class MapFragment extends Fragment {


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);

        /*View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.google_map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        MarkerOptions markerOptions = new MarkerOptions();

                        markerOptions.position(latLng);

                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                        googleMap.clear();

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                        googleMap.addMarker(markerOptions);

                    }
                });

            }
        });

        return view;*/

    }
}
