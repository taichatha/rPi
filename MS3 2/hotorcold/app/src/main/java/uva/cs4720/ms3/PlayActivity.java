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
    private Handler mHandler;

    public static double currLocLong;
    public static double currLocLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        
        mHandler = new Handler(){

            public void handleMessage(Message msg){
                String txt = (String)msg.obj;
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
                    Message ratioMsg = new Message();
                    ratioMsg.obj = ""+ratio;
                    mHandler.handleMessage(ratioMsg);
                }
            }
        };
        timer.schedule(timerTask, 0, 5000);







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
