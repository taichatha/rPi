package uva.cs4720.ms3;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;


public class PlayActivity extends Activity {
    TextView distanceView;
    TextView RedNum;
    TextView GreenNum;
    TextView BlueNum;
    TextView hotorcold;
    boolean gameEnd;

    public static LatLng updatedLocation;
    public static double ratio;
    public static double newDistance;
    public static double prevDistance;
    public static Timer timer;
    public static int red;
    public static int green;
    public static int blue;
    public static double newRed;
    public static double newBlue;
    public static String ip;
    public static double currLocLong;
    public static double currLocLat;
    public static Button quitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        gameEnd = false;
        ip = SetIPActivity.set_ip;
//        ip = "http://"+SetIPActivity.set_ip+"/rpi/";
//        System.out.println(ip);
        System.out.println(SetObjectActivity.distance);
        red = 0;
        green = 255;
        blue = 0;
        new HttpAsyncTask().execute("http://hmkcode.appspot.com/jsonservlet");

        //Green is neutral
        quitButton = (Button) findViewById(R.id.quitButton);
        distanceView = (TextView) findViewById(R.id.distanceView);
        distanceView.setText("Ratio:");
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


        timer.schedule(new timerTask(), 0, 7000);

        System.out.println("It's over!");





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
                        System.out.println(ratio);
                        checkWin(newDistance);

                    }



//                    if(newDistance <= 0.003048 ) {//10 ft{
//                        hotorcold.setText("HERE!");
//                        red = 255;
//                        green = 255;
//                        blue = 255;
//                        timer.cancel();
//                        timer.purge();
//                    }

                    //Should add capability that if prevdistance
                    //is closer than current distance, then colder
                    //perhaps for each situation(.9-.6, .6-.3, etc)
                    //the best thing would be to do would make it
                    //so that lights let you know if you are getting closer.
                    //Hmm actually im not sure.
                    if (ratio != 0) {
                        distanceView.setText("Ratio:" + ratio);

                        if (ratio <= 1.1 && ratio >= .9) {
                            hotorcold.setText("NEUTRAL");
                            red = 0;
                            blue = 0;
                            green = 255;
                            new HttpAsyncTask().execute("http://hmkcode.appspot.com/jsonservlet");
//                            POST(red, green, blue);
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
                            new HttpAsyncTask().execute("http://hmkcode.appspot.com/jsonservlet");

//                            POST(red, green, blue);


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
                            new HttpAsyncTask().execute("http://hmkcode.appspot.com/jsonservlet");

//                            POST(red, green, blue);


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
        if(distance < .003048){//10 ft
//            Congrats();
            timer.cancel();
            timer.purge();
            SetObjectActivity.theMarker = null;
            MainFragment.playButton.setEnabled(false);
            Intent intent = new Intent(this, CongratsActivity.class);
            finish();
            startActivity(intent);

            //Reset shit to 0
            //Kill Current Activity
            //Open new activity

        }

    }
//    public void Congrats(View view){
//        Intent intent = new Intent(this, CongratsActivity.class);
//        startActivity(intent);
//
//    }

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

    public static String POST() {
        //Add JSON Logic here
        InputStream inputStream = null;
        ip = SetIPActivity.set_ip;
        ip = "http://"+ip+"/rpi/";
        //((mEdit).getText().toString()) put on the line below if doesn't work
        System.out.println(ip);
        //String json = "{\"lights\": [{\"lightId\": 1, \"red\":242,\"green\":116,\"blue\":12, \"intensity\": 0.5}],\"propagate\": true}";


        String result = "";
        try {
            //1. CREATE HTTPCLIENT
            HttpClient httpclient = new DefaultHttpClient();
//            System.out.println("no error");
            //2. MAKE POST REQUEST TO GIVEN ipAddress
            HttpPost httpPost = new HttpPost(ip);
//            System.out.println("no error2");

            //String json = "";
            //3. BUILD JSON OBJECT
            //JSONObject jsonObject = new JSONObject();
            //jsonObject.accumulate("")
            String json = "{\"lights\": [{\"lightId\": 1, \"red\":"+ red+",\"green\":"+green+",\"blue\":"+blue+", \"intensity\": 0.5}],\"propagate\": true}";
//            String json = "{\"lights\": [{\"lightId\": 1, \"red\":242,\"green\":116,\"blue\":12, \"intensity\": 0.5}],\"propagate\": true}";

            //5. SET JSON to STRING ENTITY
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            //httpPost.setHeader("Accept", "application/json");
//                    httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            System.out.println("no error8");
            //10. CONVERT inputStream to string
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
                System.out.println("no error9");
            } else
                result = "Did not work!";

        } catch (Exception e) {

            Log.d("InputStream:", e.getLocalizedMessage());
        }

        return result;
    }
//
//
//                mEdit = (EditText)findViewById(R.id.enterIP); //get text from form?
//            }
//        });

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
//    @Override
//    public void onClick(View view) {
//
//        switch(view.getId()){
//            case R.id.sendIP:
//                if(!validate())
//                    Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
//                // call AsynTask to perform network operation on separate thread
//                new HttpAsyncTask().execute("http://hmkcode.appspot.com/jsonservlet");
//                break;
//        }
//
//    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {


            return POST();
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            System.out.println("Data Sent!");
//            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validate(){
        if(ip.trim().equals(""))
            return false;
        else
            return true;
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }



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


    public void quitGame(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to quit the game?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        timer.cancel();
                        timer.purge();
                        SetObjectActivity.theMarker = null;
                        MainFragment.playButton.setEnabled(false);



                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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
