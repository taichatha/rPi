package uva.cs4720.ms3;

import android.app.Activity;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;

import com.google.android.gms.maps.model.LatLng;


public class PlayActivity extends Activity {
    TextView distanceView;
    public static LatLng updatedLocation;
    public static double ratio;
    public static double newDistance;

    public static double currLocLong;
    public static double currLocLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener mlocListener = new MyLocationListener();
        mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

        Handler mHandler = new Handler(){

            public void handleMessage(String txt){
                distanceView.setText(txt);

            }

        };
        distanceView = (TextView) findViewById(R.id.distanceView);

        Timer timer = new Timer();
        TimerTask timerTask;
        timerTask = new TimerTask(){
            @Override
            public void run(){
                if(updatedLocation!=null){
                    newDistance = SetObjectActivity.CalculationByDistance(updatedLocation, SetObjectActivity.destination);
                    ratio = newDistance/SetObjectActivity.distance;
                }
                if(ratio!= 0){
                    mHandler.handleMessage(""+ratio);
                }
            }
        };
        timer.schedule(timerTask, 0, 5000);







    }


    public class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc){
            currLocLat= loc.getLatitude();
            currLocLong=loc.getLongitude();
            updatedLocation = new LatLng(currLocLat, currLocLong);
            String Text = "Latitude: " + loc.getLatitude() + " Longitude:  " + loc.getLongitude();
//            Toast.makeText( getApplicationContext(),Text, Toast.LENGTH_SHORT).show();

        }


        @Override
        public void onProviderDisabled(String provider){
            Toast.makeText(getApplicationContext(),
                    "Gps Disabled",

                    Toast.LENGTH_SHORT).show();

        }


        @Override
        public void onProviderEnabled(String provider){
            Toast.makeText( getApplicationContext(),"Gps Enabled",Toast.LENGTH_SHORT).show();
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){
        }

    }/* End of Class MyLocationListener */
//    http://stackoverflow.com/questions/4776514/updating-textview-every-n-seconds
//    private Timer timer;
//    private TimerTask timerTask;
//
//    public void onPause(){
//        super.onPause();
//        timer.cancel();
//    }
//
//    public void onResume(){
//        super.onResume();
//        try {
//            timer = new Timer();
//            timerTask = new TimerTask() {
//                @Override
//                public void run() {
//                    //Download file here and refresh
//                }
//            };
//            timer.schedule(timerTask, 30000, 30000);
//        } catch (IllegalStateException e){
//            android.util.Log.i("Damn", "resume error");
//        }
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.play, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
