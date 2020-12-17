package android.gaurav21.com.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private static final int LOCATION_REQUEST_CODE = 1001;

    private static final String REQUEST_URL =
            "https://api.openweathermap.org/data/2.5/weather?q="+UserData.getCity()+"&appid=48f65ee73db7efd301c4cf93c044c253";

    private static final String TAG = "LoginActivity";
    Button button;
    EditText editText;
    EditText locationEditText;
    private String city = null;
    private String address = null;
    private Double latitude;
    private Double longitude;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        button = findViewById(R.id.submitButton);
        editText = findViewById(R.id.nameEditView);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().length() > 0) {
                    if ((city != null || locationEditText.getText().toString().length() > 0)) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        String name = editText.getText().toString();
                        setUpUserData(name, address, longitude, latitude, city);
                        startActivity(intent);
                    } else {
                        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                LOCATION_REQUEST_CODE);
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUpUserData(String name, String location, Double log, Double lat, String city){
        UserData.setName(name);
        UserData.setLocation(location);
        UserData.setLatitude(lat);
        UserData.setLongitude(log);
        UserData.setCity(city);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            getPermission();
        } else {
            getLastLocation();
        }
    }

    private void getPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle("Required Location Permission")
                    .setMessage("You have to give this permission to access the feature")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    LOCATION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission granted
                getLastLocation();
            } else {
                //Permission not granted
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            }
        }

    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getPermission();
            return;
        }
        fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(Task<Location> task) {

                Location location = task.getResult();
                    if(location != null){
                        Geocoder geocoder = new Geocoder(LoginActivity.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                            Log.d(TAG, "onComplete: "+addresses.get(0).getLatitude() + addresses.get(0).getLongitude());
                            address = addresses.get(0).getLocality() + " ";
                            city = addresses.get(0).getLocality() + " ";
                            Log.d(TAG, "onComplete: city"+ city);
                            address += addresses.get(0).getCountryName() + " ";
                            Log.d(TAG, "onComplete: city"+ city);
                            address += addresses.get(0).getPostalCode() + " ";
                            Log.d(TAG, "onComplete: city"+ city);
                            longitude = addresses.get(0).getLongitude();
                            latitude = addresses.get(0).getLatitude();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(LoginActivity.this, "Your current location is: " + city, Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.d(TAG, "onSuccess: Location is null...");
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+ e.getLocalizedMessage());
            }
        });
    }
}
