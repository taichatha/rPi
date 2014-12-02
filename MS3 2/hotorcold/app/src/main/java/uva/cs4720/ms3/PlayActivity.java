package uva.cs4720.ms3;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;

import com.google.android.gms.maps.model.LatLng;


public class PlayActivity extends Activity {
    TextView distanceView;
    TextView RedNum;
    TextView GreenNum;
    TextView BlueNum;
    TextView hotorcold;

    public static LatLng updatedLocation;
    public static double ratio;
    public static double newDistance;
    public static double prevDistance;
    private Timer timer;
    public static int red;
    public static int green;
    public static int blue;
    public static double newRed;
    public static double newBlue;

    public static double currLocLong;
    public static double currLocLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        red = 0;
        green = 255;
        blue = 0;
        //Green is neutral

        distanceView = (TextView) findViewById(R.id.distanceView);
        RedNum = (TextView) findViewById(R.id.RedNum);
        GreenNum = (TextView) findViewById(R.id.GreenNum);
        BlueNum = (TextView) findViewById(R.id.BlueNum);
        hotorcold = (TextView) findViewById(R.id.hotorcold);

        BlueNum.setText(""+blue);
        RedNum.setText(""+red);
        GreenNum.setText(""+green);

        LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener mlocListener = new MyLocationListener();
        mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
//        Handler mHandler = new Handler(){
//
//            public void handleMessage(Message msg){
//                String txt = (String)msg.obj;
//                distanceView.setText(txt);
//
//            }
//
//        };


        timer = new Timer();


        timer.schedule(new timerTask(), 0, 5000);







    }


    private class timerTask extends TimerTask{
        @Override
        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(updatedLocation!=null){
//                        prevDistance = newDistance;
                        newDistance = SetObjectActivity.CalculationByDistance(updatedLocation, SetObjectActivity.destination);
                        ratio = newDistance/SetObjectActivity.distance;

                    }

//                    if(newDistance <= 0.003048 ) {//10 ft{
//                        hotorcold.setText("HERE!");
//                        red = 255;
//                        green = 255;
//                        blue = 255;
//                        timer.cancel();
//                        timer.purge();
//                    }



                    if (ratio != 0) {
                        distanceView.setText("Ratio:" + ratio);

                        if (ratio <= 1.1 && ratio >= .9) {
                            hotorcold.setText("At the Start");
                            red = 0;
                            blue = 0;
                            green = 255;
                        }
                        else if (ratio < .9) {

                            red = 255;

                            if (ratio < .9 && ratio >= .6) {
                                hotorcold.setText("WARMER");
                                green = 255;
                                blue = 0;


                            } else if (ratio < .6 && ratio >= .3) {
                                hotorcold.setText("MUCH WARMER");
                                green = 153;
                                blue = 0;


                            } else if (ratio < .3) {
                                hotorcold.setText("HOT!");
                                green = 0;
                                blue = 0;
                            }


                            BlueNum.setText("" + blue);
                            RedNum.setText("" + red);
                            GreenNum.setText("" + green);


                        }
                        else if (ratio > 1.1) {


                            blue = 255;


                            if (ratio > 1.1 && ratio <= 1.4) {
                                hotorcold.setText("COLDER");
                                red = 0;
                                green = 179;
                            } else if (ratio >= 1.4 && ratio <= 1.7) {
                                hotorcold.setText("MUCH COLDER!!");
                                red = 0;
                                green = 196;

                            } else if (ratio > 1.7) {
                                hotorcold.setText("ICE COLD");
                                red = 0;
                                green = 0;
                            }


                            BlueNum.setText("" + blue);
                            RedNum.setText("" + red);
                            GreenNum.setText("" + green);


                        }

                        //                        Message ratioMsg = new Message();
                        //                        ratioMsg.obj = ""+ratio;
                        //                        mHandler.handleMessage(ratioMsg);
                    }








                }
            });

        }
    }






    public void checkWin(double distance){
        if(distance < 0.0003048){
//            Congrats();
            Intent intent = new Intent(this, CongratsActivity.class);
            startActivity(intent);

            //Reset shit to 0
            //Kill Current Activity
            //Open new activity

        }

    }
    public void Congrats(View view){
        Intent intent = new Intent(this, CongratsActivity.class);
        startActivity(intent);

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
