package uva.cs4720.ms3;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;
///////////////////////////////
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;





public class MainActivity extends Activity {


    Button sendIPbutton; //Button for sending IP Address
    EditText mEdit; //Get info from what user enters in form
    TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButton(); //Added for button to Send IP Address

    }

    // Added for button to send IP Address
    public void addListenerOnButton(){
        sendIPbutton = (Button) findViewById(R.id.sendIP);

        sendIPbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri. parse("http://www.google.com"));
//                startActivity(browserIntent);
                mEdit = (EditText)findViewById(R.id.enterIP);

                //NEED TO ADD JSON LOGIC HERE
            }
        });
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
}
