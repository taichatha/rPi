package uva.cs4720.ms3;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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

import java.text.DecimalFormat;

public class SetObjectActivity extends Activity {
    private GoogleMap googleMap;
    Marker theMarker;
    MarkerOptions destinationMarker;
    Marker userPosition;
    public static LatLng start;
    public static LatLng destination;
    public static double distance;
    boolean intialized;
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_object);

        try {
            // Loading map
            intialized = true;
            initializeMap();



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initializeMap() {
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
        googleMap.getUiSettings().setZoomGesturesEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setMyLocationEnabled(true);
        double latitude = MainActivity.currLocLat;
        double longitude = MainActivity.currLocLong;

// create marker



        Location location = googleMap.getMyLocation();
        LatLng myLocation = new LatLng(latitude, longitude);
        if(location != null){
            myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        }
        myLocation = new LatLng(latitude, longitude);
        start = myLocation;





        MarkerOptions marker = new MarkerOptions().position(myLocation).title("Your Location");
        // ROSE color icon
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        userPosition = googleMap.addMarker(marker);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(21).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
// adding marker






        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                if(theMarker == null) {
                    System.out.println("Should only happen once!");
                    destination = point;
                    theMarker = googleMap.addMarker(new MarkerOptions().position(point));
                    distance = CalculationByDistance(start, destination);

                }
                else{
                    theMarker.remove();
                    destination = point;
                    theMarker = googleMap.addMarker(new MarkerOptions().position(point));
                    distance = CalculationByDistance(start, destination);

                }
            }
        });


        System.out.println(distance);



    }
    @Override
    protected void onResume() {
        super.onResume();
        if(!intialized)
            initializeMap();
    }
    @Override
    protected void onPause(){
        super.onPause();
    }

//http://stackoverflow.com/questions/14394366/find-distance-between-two-points-on-map-using-google-map-api-v2
    public static double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius=6371;//radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult= Radius*c;
        double km=valueResult/1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec =  Integer.valueOf(newFormat.format(km));
        double meter=valueResult%1000;
        int  meterInDec= Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec + " Meter   " + meterInDec);

        return Radius * c;
    }


}


