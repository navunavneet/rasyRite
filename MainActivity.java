package androidproject.com.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import androidproject.com.myapplication.utilities.GpsLocationTracker;

public class MainActivity extends AppCompatActivity implements com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    EditText edtPostalCode;
    String strPostalCode;
    Button btnSave;
    ImageView imgHamBurger;
    final static int REQUEST_LOCATION = 199;
    private GoogleApiClient googleApiClient;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    Location mCurrentLocation;
    String myLat = "", myLng = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtPostalCode = (EditText) findViewById(R.id.edtPostalCode);
        btnSave = (Button) findViewById(R.id.btnSave);
        imgHamBurger = (ImageView) findViewById(R.id.imgHamBurger);
        createLocationRequest();
        initVariables();
        imgHamBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strPostalCode = edtPostalCode.getText().toString().trim();
                if (strPostalCode.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please add postal code", Toast.LENGTH_SHORT).show();
                } else if (strPostalCode.length() != 6) {
                    Toast.makeText(MainActivity.this, "Postal code must be of 6 digits", Toast.LENGTH_SHORT).show();
                } else if (!strPostalCode.matches("^([a-zA-Z][0-9])*$")) {
                    Toast.makeText(MainActivity.this, "Please add correct postal code", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "postal code saved", Toast.LENGTH_SHORT).show();

                    /**
                     * Set GPS Location fetched address
                     */
                    Bundle bundle = new Bundle();
                    bundle.putString("Latitude", myLat);
                    bundle.putString("Longitude", myLng);
                    Intent intent = new Intent(MainActivity.this, RideDetailsActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            }
        });
    }

    private void initVariables() {
        if (!isGooglePlayServicesAvailable()) {

        }
        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("onConnected", "onConnected");
        //  Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("onConnectionSuspended", "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("onConnectionFailed", "onConnectionFailed");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("onLocationChanged", "onLocationChanged");
        //    Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        //  Toast.makeText(getActivity(), mCurrentLocation.getLatitude() + "", Toast.LENGTH_SHORT).show();
        myLat = String.valueOf(mCurrentLocation.getLatitude());
        myLng = String.valueOf(mCurrentLocation.getLongitude());
        Log.e("LAT", mCurrentLocation.getLatitude() + "ds");
        Log.e("LONG", mCurrentLocation.getLongitude() + "dsds");
        //   mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("OnSTART", "Onstart");

        try {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }

        } catch (NullPointerException e) {
            Log.e("On start", "Null");
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        Log.e("onStop  ", "ONSTOp");
        mGoogleApiClient.disconnect();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("PAUSE", "Pause");
        try {
            stopLocationUpdates();
        } catch (IllegalStateException e) {
        }

    }


    protected void startLocationUpdates() {
        Log.e("startLocationUpdates", "startLocationUpdates");
        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("permissionnnn", "permission");
            // TODO: Consider calling
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.e("prermission 1", "granted");
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new android.app.AlertDialog.Builder(MainActivity.this)
                        .setTitle("title_location_permission")
                        .setMessage("location_permission")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                Log.e("prermission 2", "granted");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            //return false;
        }
        Log.e("permissionnnn outttt ", "permission outttt");
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Log.e("Location update started", "Location update started ..............: ");
     /*   if (ActivityCompat.checkSelfPermission(HomeScreen.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeScreen.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           Log.e("permissionnnn", "permission");

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/


        //Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        //onLocationChanged(location);

    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        Log.e("HOME", "Location update stopped .......................");
    }


    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();


                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.e("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                // Log.e("try","try");
                                status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);


                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                                //  Log.e("catch","e");
                            }
                            break;
                    }
                }
            });
        }


    }


    private boolean isGooglePlayServicesAvailable() {
        Log.e("isGooglePlayServices", "isGooglePlayServicesAvailable");
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainActivity.this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, MainActivity.this, 0).show();
            return false;
        }
    }
}
