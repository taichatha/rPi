package uva.cs4720.ms3;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.view.View.OnClickListener;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.content.Intent;
import android.net.Uri;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.TextView;
///
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;
import android.widget.Toast;

///
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.facebook.AppEventsLogger;


//Referenced 'http://hmkcode.com/android-send-json-data-to-server/' for this milestone

/*
William Andrade - wla3ww
Taimoor Chatha - tuc4uw
 */
public class MainActivity extends FragmentActivity{
    private MainFragment mainFragment;
    public static double currLocLong;
    public static double currLocLat;
    //TextView mText;
    public static TextView coordinates;
//    TextView ipView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ipView = (TextView)findViewById(R.id.ipView);
//        ipView = (TextView)findViewById(R.id.ipView);

//        LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        LocationListener mlocListener = new MyLocationListener();
//        mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

//        if(ipView.getText().toString() != "NOT SET!"){
//            //make set button active
//        }
        setContentView(R.layout.activity_main);

        /*http://www.firstdroid.com/2010/04/29/android-development-using-gps-to-get-current-location-2/*/
        /* Use the LocationManager class to obtain GPS locations */





/**********************************************************************/
        /*get reference to views*/
        coordinates = (TextView)findViewById(R.id.coordinates);




//       public static String POST(){}

//        InputMethodManager imm = (InputMethodManager)getSystemService(
//                Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(mEdit.getWindowToken(), 0);
        /*add click listener to Button "sendIPbutton"*/
//        sendIPbutton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//


        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            mainFragment = new MainFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, mainFragment)
                    .commit();
        } else {
            // Or set the fragment from restored state info
            mainFragment = (MainFragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }
/*********************************************************************************/
    }

//
//    /* Class My Location Listener */
//    public class MyLocationListener implements LocationListener{
//        @Override
//        public void onLocationChanged(Location loc){
//            currLocLat= loc.getLatitude();
//            currLocLong=loc.getLongitude();
//            String Text = "Latitude: " + loc.getLatitude() + " Longitude:  " + loc.getLongitude();
////            Toast.makeText( getApplicationContext(),Text, Toast.LENGTH_SHORT).show();
//
//            coordinates.setText(Text);
//        }
//
//
//        @Override
//        public void onProviderDisabled(String provider){
//            Toast.makeText( getApplicationContext(),
//                    "Gps Disabled",
//
//                    Toast.LENGTH_SHORT ).show();
//
//        }
//
//
//        @Override
//        public void onProviderEnabled(String provider){
//            Toast.makeText( getApplicationContext(),"Gps Enabled",Toast.LENGTH_SHORT).show();
//        }
//
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras){
//        }
//
//    }/* End of Class MyLocationListener */
//




    public void setObject(View view){
        Intent intent = new Intent(this, SetObjectActivity.class);
        startActivity(intent);
    }
    public void setIP(View view){
        Intent intent = new Intent(this, SetIPActivity.class);
        startActivity(intent);
    }
    public void play(View view){
        Intent intent = new Intent(this, PlayActivity.class);

        startActivity(intent);
    }

    public void howToPlay(View view){
        Intent intent = new Intent(this, HowToPlayActivity.class);

        startActivity(intent);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }



}
