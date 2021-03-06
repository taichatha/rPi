package uva.cs4720.ms3;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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


//Referenced 'http://hmkcode.com/android-send-json-data-to-server/' for this milestone

/*
William Andrade - wla3ww
Taimoor Chatha - tuc4uw
 */
public class MainActivity extends Activity implements OnClickListener {


    Button sendIPbutton; //Button for sending IP Address
    EditText mEdit; //Get info from what user enters in form
    //TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*get reference to views*/
        sendIPbutton = (Button) findViewById(R.id.sendIP);
        mEdit = (EditText) findViewById(R.id.enterIP);
        sendIPbutton.setOnClickListener(this);
        /*add click listener to Button "sendIPbutton"*/
//        sendIPbutton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                //Add JSON Logic here
//
                String ip = "http://";
                ip = ip + ((mEdit).getText().toString()) + "/rpi/";
                System.out.println(ip);
                //String json = "{\"lights\": [{\"lightId\": 1, \"red\":242,\"green\":116,\"blue\":12, \"intensity\": 0.5}],\"propagate\": true}";

                InputStream inputStream = null;
                String result = "";
                try {
                    //1. CREATE HTTPCLIENT
                    HttpClient httpClient = new DefaultHttpClient();
                    System.out.println("no error");
                    //2. MAKE POST REQUEST TO GIVEN ipAddress
                    HttpPost httpPost = new HttpPost(ip);
                    System.out.println("no error2");

                    //String json = "";
                    //3. BUILD JSON OBJECT
                    //JSONObject jsonObject = new JSONObject();
                    //jsonObject.accumulate("")
                    String json = "";//"{\"lights\": [{\"lightId\": 1, \"red\":242,\"green\":116,\"blue\":12, \"intensity\": 0.5}],\"propagate\": true}";
                    JSONObject jsonObj = new JSONObject("{\"phonetype\":\"N95\",\"cat\":\"WP\"}");
                    json = jsonObj.toString();
                    httpPost.setEntity(new StringEntity(json));

                    //5. SET JSON to STRING ENTITY
//                    StringEntity se = new StringEntity(json);
//                    System.out.println("no error3");
//
//                    //6. SET HTTP POST ENTITY
//                    httpPost.setEntity(se);
//                    System.out.println("no error4");

                    //7. SET SOME HEADERS TO INFORM SERVER ABOUT THE TYPE OF CONTENT
//                    httpPost.setHeader("Accept", "application/json");
//                    System.out.println("no error5");
//                    httpPost.setHeader("Content-type", "application/json");
//                    System.out.println("no error6");
                    //HttpPost httpPost = new HttpPost(url);
                    //httpPost.setEntity(new StringEntity(json));
                    //CloseableHttpResponse response2 = httpclient.execute(httpPost);
                    //ERROR HERE
                    //8. EXECUTE POST REQUEST TO THE GIVEN IP ADDRESS
                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    System.out.println("no error7");
//                    httpPost.completed();
//                    response2.close();

                    //9. RECEIVE RESPONSE AS inputStream
                    inputStream = httpResponse.getEntity().getContent();
                    System.out.println("no error8");
                    //10. CONVERT inputStream to string
                    if(inputStream != null) {
                        result = convertInputStreamToString(inputStream);
                        System.out.println("no error9");
                    }
                    else
                        result = "Did not work!";

                } catch (Exception e) {

                    Log.d("InputStream", e.getLocalizedMessage());
                }
//
//
//                mEdit = (EditText)findViewById(R.id.enterIP); //get text from form?
//            }
//        });
      //  addListenerOnButton(); //Added for button to Send IP Address
    }


    public static String POST(String ip) {
        //Add JSON Logic here
        InputStream inputStream = null;

        //((mEdit).getText().toString()) put on the line below if doesn't work
        ip = "http://" + ip + "/rpi/";
        System.out.println(ip);
        //String json = "{\"lights\": [{\"lightId\": 1, \"red\":242,\"green\":116,\"blue\":12, \"intensity\": 0.5}],\"propagate\": true}";


        String result = "";
        try {
            //1. CREATE HTTPCLIENT
            HttpClient httpclient = new DefaultHttpClient();
            System.out.println("no error");
            //2. MAKE POST REQUEST TO GIVEN ipAddress
            HttpPost httpPost = new HttpPost(ip);
            System.out.println("no error2");

            //String json = "";
            //3. BUILD JSON OBJECT
            //JSONObject jsonObject = new JSONObject();
            //jsonObject.accumulate("")
            String json = "{\"lights\": [{\"lightId\": 1, \"red\":242,\"green\":116,\"blue\":12, \"intensity\": 0.5}],\"propagate\": true}";


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

            Log.d("InputStream", e.getLocalizedMessage());
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
    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.sendIP:
                if(!validate())
                    Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
                // call AsynTask to perform network operation on separate thread
                new HttpAsyncTask().execute("http://hmkcode.appspot.com/jsonservlet");
                break;
        }

    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {


            return POST(((mEdit).getText().toString()));
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validate(){
        if(mEdit.getText().toString().trim().equals(""))
            return false;
        else
            return true;
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

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
