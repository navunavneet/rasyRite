package androidproject.com.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class RideDetailsActivity extends AppCompatActivity {
    ImageView imgMenuBack;
    TextView txtDestination, txtDistance, txtPrice;
    RadioGroup radioGroup;
    Button btnMap, btnConfirm;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private final static int PLACE_PICKER_REQUEST = 999;
    Bundle bundle;
    String lat, lng;
String totalPrice  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);
        initVariables();
        imgMenuBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bundle = getIntent().getExtras();
        if (bundle != null) {
            lat = bundle.getString("Latitude");
            lng = bundle.getString("Longitude");
            Log.e("Latitude" , lat);
            Log.e("Longitude" , lng);
        }
        txtDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the fields to specify which types of place data to
// return after the user has made a selection.
                openPlacePicker();
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioButton) {
                    Toast.makeText(getApplicationContext(), "choice: Sedan",
                            Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.radioButton2) {
                    Toast.makeText(getApplicationContext(), "choice: Luxury Sedan",
                            Toast.LENGTH_SHORT).show();
                }
                else if(checkedId == R.id.radioButton3){
                    Toast.makeText(getApplicationContext(), "choice: SUV",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openPlacePicker() {

      /*  try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }*/

        List<com.google.android.libraries.places.api.model.Place.Field> fields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.LAT_LNG);

// Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this);
        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
    }

    private void initVariables() {
        imgMenuBack = (ImageView) findViewById(R.id.imgMenuBack);
        txtDestination = (TextView) findViewById(R.id.txtDestination);
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        btnMap = (Button) findViewById(R.id.btnMap);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        if (!Places.isInitialized()) {
            Places.initialize(RideDetailsActivity.this, /*"AIzaSyAP11tokBgY4EMSLkTSQa9XYsh-hzoJ_Pk"*/"AIzaSyCMcMEBoL0nH5zIfFLrHcIg7vUYaqKP_34");
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (data != null) {
                com.google.android.libraries.places.api.model.Place place = Autocomplete.getPlaceFromIntent(data);
                LatLng mSelectedLatLng = place.getLatLng();
                String ADRS = getAddressFromLatLong(mSelectedLatLng);
                // Toast.makeText(getActivity(), mapPlace.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }

    private String getAddressFromLatLong(LatLng selectedLatLng) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(selectedLatLng.latitude, selectedLatLng.longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getAddressLine(0)).append("\n");
                result.append(address.getCountryName());
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }
}
