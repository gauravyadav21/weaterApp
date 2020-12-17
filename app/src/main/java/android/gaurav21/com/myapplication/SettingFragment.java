package android.gaurav21.com.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.gaurav21.com.myapplication.R;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SettingFragment extends Fragment {

    private static final String TAG = "SettingFragment";

    private static final String REQUEST_URL =
            "https://api.openweathermap.org/data/2.5/weather?q="+UserData.getCity()+"&appid=48f65ee73db7efd301c4cf93c044c253";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        loadWeatherData(root);
        final TextView textView = root.findViewById(R.id.text_setting);
        textView.setText(UserData.getName());
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
                            JSONObject main = response.getJSONObject("main");
                            weatherData.add(Double.toString(main.getDouble("temp")));
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
        String temp = kelvinToCelsius(Double.parseDouble(weatherData.get(0))) + "C";
        Log.d(TAG, "setWeatherData: temp"+temp);
        TextView tempTextView = root.findViewById(R.id.temperature_setting);
        tempTextView.setText(temp);
        Log.d(TAG, "setWeatherData: after temp"+ temp);

    }

    private String kelvinToCelsius(Double k){
        Log.d(TAG, "kelvinToCelsius: "+k);
        Double C = 289D - k;
        String res = new DecimalFormat("##.##").format(C);
        return res;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button selectCelsius = view.findViewById(R.id.celsius);
        Button selectFahrenheit = view.findViewById(R.id.fahrenheit);
        final TextView tempTextView = view.findViewById(R.id.temperature_setting);

        selectCelsius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = tempTextView.getText().toString();
                if(str.charAt(str.length()-1) != 'C'){
                    str = str.substring(0,str.length()-1);
                    Double F = Double.parseDouble(str);
                    Double C = (F - 32) * 0.55D;
                    str = new DecimalFormat("##.##").format(C);
                    tempTextView.setText(str + "C");
                }
            }
        });
        selectFahrenheit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = tempTextView.getText().toString();
                if(str.charAt(str.length()-1) != 'F') {
                    str = str.substring(0, str.length() - 1);
                    Double C = Double.parseDouble(str);
                    Double F = (C * 1.8D) + 32;
                    str = new DecimalFormat("##.##").format(F);
                    tempTextView.setText(str + "F");
                }
                Log.d(TAG, "onClick:selectFahrenheit "+tempTextView.getText());
            }
        });
    }
}