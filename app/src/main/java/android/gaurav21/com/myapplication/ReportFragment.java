package android.gaurav21.com.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.gaurav21.com.myapplication.R;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportFragment extends Fragment {

    private static final String TAG = "ReportFragment";

    private static final String REQUEST_URL =
            "https://api.openweathermap.org/data/2.5/onecall?lat=28&lon=77&appid=48f65ee73db7efd301c4cf93c044c253";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_report, container, false);

        loadWeatherData(root);

        TextView nameTextView = root.findViewById(R.id.user_text_view);
        TextView locationTextView = root.findViewById(R.id.location_text_View);
        String name = UserData.getName();
        nameTextView.setText(name);
        locationTextView.setText(UserData.getLocation());

        return root;
    }
    private void loadWeatherData(final View root){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        Log.d(TAG, "loadWeatherData: "+getContext());

// Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, REQUEST_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, "onResponse: "+response);
                            JSONArray daily = response.getJSONArray("daily");
                            Log.d(TAG, "onResponse:daily "+daily);
//                            Weather weather1;
                            String date="";
//                            Double pressure=null;
//                            Double temperature=null;
//                            Double humidity=null;
//                            Double windSpeed=null;
                            for(int i = 0; i < daily.length(); i++){
                                JSONObject object = daily.getJSONObject(i);
                                date = object.getString("dt");
                                break;
                            }
                            Log.d(TAG, "onResponse: date"+date);
                            try {
                                dateConversion(date);
                            } catch (ParseException e) {
                                Log.d(TAG, "onResponse: "+e.getLocalizedMessage());
                            }
//                            temperature = main.getDouble("temp");
//                            pressure = main.getDouble("pressure");
//                            humidity = main.getDouble("humidity");

//                            Log.d(TAG, "onResponse: temp pre, hum"+temperature+" "+pressure+" "+humidity);
                            // JSONObject object = wind.getJSONObject(i);
//                            windSpeed = wind.getDouble("speed");
                            //}
//                            Log.d(TAG, "onResponse: windspeed"+windSpeed);
//                            Weather weather2 = new Weather(temperature, description, pressure, humidity, windSpeed);
//                            weathersData = new Weather(weather2);

//                            if(weathersData != null){
//                                TextView weatherDataTextView =root.findViewById(R.id.weather_data_text_view);
//                                weatherDataTextView.setText("Temperature: "+ weathersData.getTemperature()+"\n"+
//                                        "Humidity: "+ weathersData.getHumidity() + "\n"+
//                                        "Pressure: "+ weathersData.getPressure()+"\n"+
//                                        "Wind Speed: " +weathersData.getWind()+"\n"+
//                                        "Description: " +weathersData.getDescription());
//                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error.getLocalizedMessage());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }

    private void dateConversion(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Log.d(TAG, "dateConversion: "+sdf.parse(date));
    }
}