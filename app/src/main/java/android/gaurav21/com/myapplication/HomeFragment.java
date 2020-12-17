package android.gaurav21.com.myapplication;

import android.app.LoaderManager;
import android.gaurav21.com.myapplication.UserData;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.gaurav21.com.myapplication.R;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment{

    private static final String TAG = "HomeFragment";
    private static final String REQUEST_URL =
            "https://api.openweathermap.org/data/2.5/weather?q="+UserData.getCity()+"&appid=48f65ee73db7efd301c4cf93c044c253";

    TextView tempTextView, humTextView, desTextView, presTextView, windTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        loadWeatherData(root);
        TextView nameTextView = root.findViewById(R.id.user_text_view);
        TextView locationTextView = root.findViewById(R.id.location_text_View);
        String name =" Welcome " + UserData.getName()+" ";
        nameTextView.setText(name);
        Log.d(TAG, "onCreateView: UserData.getLocation()"+UserData.getLocation());
        locationTextView.setText(UserData.getLocation());
        return root;
    }

    private void loadWeatherData(final View root){
        final List<String> weatherData = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, REQUEST_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, "onResponse: "+response);
                            JSONArray weather = response.getJSONArray("weather");
                            JSONObject main = response.getJSONObject("main");
                            JSONObject wind = response.getJSONObject("wind");
                            for(int i = 0; i < weather.length(); i++){
                                JSONObject object = weather.getJSONObject(i);
                                weatherData.add(object.getString("description"));
                            }
                            weatherData.add(Double.toString(main.getDouble("temp")));
                            weatherData.add(Double.toString(main.getDouble("pressure")));
                            weatherData.add(Double.toString(main.getDouble("humidity")));
                            weatherData.add(Double.toString(wind.getDouble("speed")));
                            setWeatherData(root, weatherData);
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
        queue.add(jsonObjectRequest);
    }

    private void setWeatherData(View root, List<String> weatherData) {
        Log.d(TAG, "setWeatherData: before" + weatherData.get(1));
        String temp = kelvinToCelsius(Double.parseDouble(weatherData.get(1)));
        Log.d(TAG, "setWeatherData: after"+ temp);
        desTextView = root.findViewById(R.id.description_text_view);
        desTextView.setText(" " + weatherData.get(0) + " ");
        tempTextView = root.findViewById(R.id.temperature_text_View);
        tempTextView.setText(" Temp " + temp + "C ");
        presTextView = root.findViewById(R.id.pressure_text_View);
        presTextView.setText(" Pressure " + weatherData.get(2) + " ");
        humTextView = root.findViewById(R.id.humidity_text_View);
        humTextView.setText(" Humidity " + weatherData.get(3) + " ");
        windTextView = root.findViewById(R.id.wind_text_View);
        windTextView.setText(" Wind Speed " + weatherData.get(4) + " ");
    }

    private String kelvinToCelsius(Double k){
        Log.d(TAG, "kelvinToCelsius: "+k);
        Double C = 289D - k;
        String res = new DecimalFormat("##.##").format(C);
        return res;
    }
}