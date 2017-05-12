package com.arkay.rajasthanquiz.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.arkay.rajasthanquiz.R;
import com.arkay.rajasthanquiz.customviews.CoordTileProvider;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;


public class PlaceMapFragment extends Fragment implements OnMapReadyCallback {
    private View view;
    private GoogleMap map;
    private String destLati, destLongi,title;
    private Button btnNavigate;
    private int STORAGE_PERMISSION_CODE = 23;
    private Listener mListener = null;

    public PlaceMapFragment() {
        // Required empty public constructor
    }

    public static PlaceMapFragment newInstance(Bundle bundle) {
        PlaceMapFragment fragment = new PlaceMapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setListener(Listener l) {
        mListener = l;
    }


    public interface Listener {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        container.removeAllViews();
        view = inflater.inflate(R.layout.fragment_project_map, container, false);

        btnNavigate = (Button) view.findViewById(R.id.btnNavigate);

        if (!isGooglePlayServicesAvailable()) {
            System.out.println("not available");
        }
        Bundle bundle = getArguments();
        destLati = bundle.getString(FragmentGkInDetail.LATITUDE);
        destLongi = bundle.getString(FragmentGkInDetail.LOGITUDE);
        title = bundle.getString(FragmentGkInDetail.TITLE);

        FragmentManager fmanager = getChildFragmentManager();
        Fragment fragment = fmanager.findFragmentById(R.id.map);

        SupportMapFragment supportmapfragment = (SupportMapFragment) fragment;
        supportmapfragment.getMapAsync(this);


        System.out.println("dest : " + destLati);
        btnNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  lat = destLati;

                String  lng = destLongi;
                System.out.println("det. : " + destLati);

                String format = "geo:0,0?q=" + lat + "," + lng + "( Location title)";

                Uri uri = Uri.parse(format);


                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);

        TileProvider coordTileProvider = new CoordTileProvider(getActivity());
        map.addTileOverlay(new TileOverlayOptions().tileProvider(coordTileProvider));

        if (map != null) {
            // Creating an instance of MarkerOptions
            MarkerOptions markerOptions = new MarkerOptions();
            // Setting position for the marker
            LatLng point = new LatLng(Double.parseDouble(destLati),  Double.parseDouble(destLongi));
            markerOptions.position(point);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pointer));
             markerOptions.title(title);
            map.addMarker(markerOptions);
            System.out.println("argdfg : " + destLati);
            System.out.println("argdfg : " + destLongi);
            // destLongi = project.getProject_logi();
            LatLng latLng = new LatLng(Double.parseDouble(destLati), Double.parseDouble(destLongi));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10
            ));
            map.animateCamera(CameraUpdateFactory.zoomIn());
            map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        }
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        Log.i("INFO", "PERMinstioan resutl" + requestCode);
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    map.setMyLocationEnabled(true);
                }
              //  showmapPoint();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
            return false;
        }
    }
}
