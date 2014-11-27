package uva.cs4720.ms3;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class SetObjectActivity extends Activity {
    private GoogleMap googleMap;
    Marker theMarker;
    Marker userPosition;
    public LatLng start;
    public LatLng destination;
    public double distance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_object);

        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }

        }
        double latitude = MainActivity.currLocLat;
        double longitude = MainActivity.currLocLong;

// create marker


        theMarker = null;

        googleMap.setMyLocationEnabled(true);
        Location location = googleMap.getMyLocation();
        LatLng myLocation = new LatLng(latitude, longitude);
        if(location != null){
            myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        }
        CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(21).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        MarkerOptions marker = new MarkerOptions().position(myLocation).title("Your Location");
        // ROSE color icon
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
// adding marker
        userPosition = googleMap.addMarker(marker);

        googleMap.getUiSettings().setZoomGesturesEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                if(theMarker == null) {
                    System.out.println("Should only happen once!");
                    destination = point;
                    theMarker = googleMap.addMarker(new MarkerOptions().position(point));
                }
                else{
                    theMarker.remove();
                    destination = point;
                    theMarker = googleMap.addMarker(new MarkerOptions().position(point));
                }
            }
        });

        //Need to add distance. Very simple.



    }
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }



}


