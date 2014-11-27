package uva.cs4720.ms3;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class MainFragment extends Fragment {
    private Button shareButton;
    private Context globalContext = null;
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
    private boolean pendingPublishReauthorization = false;
    private static final String TAG = MainFragment.class.getSimpleName();
    TextView coordinates;
    private UiLifecycleHelper uiHelper;

    private final List<String> permissions;

    public MainFragment() {
        permissions = Arrays.asList("user_status");
    }
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalContext = this.getActivity();
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        shareButton = (Button) view.findViewById(R.id.shareButton);
        coordinates = (TextView)view.findViewById(R.id.coordinates);
        LocationManager mlocManager = (LocationManager)globalContext.getSystemService(Context.LOCATION_SERVICE);
        LocationListener mlocListener = new MyLocationListener();
        mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishStory();
            }
        });
        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        authButton.setFragment(this);
        authButton.setReadPermissions(permissions);
        if (savedInstanceState != null) {
            pendingPublishReauthorization =
                    savedInstanceState.getBoolean(PENDING_PUBLISH_KEY, false);
        }
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
        uiHelper.onSaveInstanceState(outState);
    }
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {

            shareButton.setVisibility(View.VISIBLE);
            if (pendingPublishReauthorization &&
                    state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
                pendingPublishReauthorization = false;
                publishStory();
            }

        } else if (state.isClosed()) {
            shareButton.setVisibility(View.INVISIBLE);
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private void publishStory() {
        Session session = Session.getActiveSession();

        if (session != null){

            // Check for publish permissions
            List<String> permissions = session.getPermissions();
            if (!isSubsetOf(PERMISSIONS, permissions)) {
                pendingPublishReauthorization = true;
                Session.NewPermissionsRequest newPermissionsRequest = new Session
                        .NewPermissionsRequest(this, PERMISSIONS);
                session.requestNewPublishPermissions(newPermissionsRequest);
                return;
            }



            String text = coordinates.getText().toString();
            Bundle postParams = new Bundle();
            postParams.putString("name", "My Location!");
            postParams.putString("caption", "Thanks to Hot and Cold");
            postParams.putString("description", text);
            postParams.putString("link", null);
            postParams.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

            Request.Callback callback= new Request.Callback() {
                public void onCompleted(Response response) {
                    JSONObject graphResponse = response
                            .getGraphObject()
                            .getInnerJSONObject();
                    String postId = null;
                    try {
                        postId = graphResponse.getString("id");
                    } catch (JSONException e) {
                        Log.i(TAG,
                                "JSON error "+ e.getMessage());
                    }
                    FacebookRequestError error = response.getError();
                    if (error != null) {
                        Toast.makeText(getActivity()
                                        .getApplicationContext(),
                                error.getErrorMessage(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity()
                                        .getApplicationContext(),
                                postId,
                                Toast.LENGTH_LONG).show();
                    }
                }
            };

            Request request = new Request(session, "me/feed", postParams,
                    HttpMethod.POST, callback);

            RequestAsyncTask task = new RequestAsyncTask(request);
            task.execute();
        }

    }

    private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
        for (String string : subset) {
            if (!superset.contains(string)) {
                return false;
            }
        }
        return true;
    }

    /* Class My Location Listener */
    public class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc){
            loc.getLatitude();
            loc.getLongitude();
            String Text = "Latitude: " + loc.getLatitude() + "\nLongitude:  " + loc.getLongitude();
//            Toast.makeText( getApplicationContext(),Text, Toast.LENGTH_SHORT).show();


            coordinates.setText(Text);
        }


        @Override
        public void onProviderDisabled(String provider){
            Toast.makeText( globalContext.getApplicationContext(),
                    "Gps Disabled",

                    Toast.LENGTH_SHORT ).show();

        }


        @Override
        public void onProviderEnabled(String provider){
            Toast.makeText( globalContext.getApplicationContext(),"Gps Enabled",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){
        }

    }/* End of Class MyLocationListener */


}
