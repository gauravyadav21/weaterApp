package android.gaurav21.com.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DateFragment extends Fragment {

    private static final String TAG = "DateFragment";

    TextView tempTextView, humTextView, desTextView, presTextView, windTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        final View root = inflater.inflate(R.layout.fragment_date, container, false);
        CalendarView calendarView = root.findViewById(R.id.calender_view);
        final Button selectCityButton = root.findViewById(R.id.select_city_button);
        final TextView dateTextView =root.findViewById(R.id.date_text_view);
        selectCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(getContext(), selectCityButton);
                menu.getMenuInflater().inflate(R.menu.city_select_menu, menu.getMenu());
                selectCityButton.setText("Select City");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(getContext(), "You have selected "+item.getTitle()+" date"+dateTextView.getText(), Toast.LENGTH_SHORT).show();
                        if(item.getTitle() != null && dateTextView.getText().toString().length() > 0){
                            //loadWeatherData(root);
                            selectCityButton.setText(item.getTitle());

                        }
                        return true;
                    }
                });
                menu.show();
            }
        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = (dayOfMonth + 1) + "/" + (month+1) + "/" + year;
                dateTextView.setText("Date Selected"+date);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);
        final Button selectCityButton = view.findViewById(R.id.select_city_button);
        final TextView dateTextView =view.findViewById(R.id.date_text_view);
        final CalendarView calendarView = view.findViewById(R.id.calender_view);
        final Button button = view.findViewById(R.id.fetch_button);
        button.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(dateTextView.getText().toString().length() > 0 && selectCityButton.getText().toString() != "Select City") {
                    loadWeatherData(view, selectCityButton.getText().toString());
                    selectCityButton.setVisibility(View.GONE);
                    dateTextView.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                    calendarView.setVisibility(View.GONE);
                }else{
                    Toast.makeText(getContext(), "Please select date and city", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void loadWeatherData(final View root, final String city){
        String REQUEST_URL =
                "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=48f65ee73db7efd301c4cf93c044c253";
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
                            setWeatherData(root, weatherData, city);
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

    private void setWeatherData(View root, List<String> weatherData, String city) {

        TextView location = root.findViewById(R.id.location_date_text_View);
        location.setText(city);
        desTextView = root.findViewById(R.id.description_date_text_view);
        desTextView.setText(" " + weatherData.get(0) + " ");
        tempTextView = root.findViewById(R.id.temperature_date_text_View);
        tempTextView.setText(" Temp " + weatherData.get(1) + " ");
        presTextView = root.findViewById(R.id.pressure_date_text_View);
        presTextView.setText(" Pressure " + weatherData.get(2) + " ");
        humTextView = root.findViewById(R.id.humidity_date_text_View);
        humTextView.setText(" Humidity " + weatherData.get(3) + " ");
        windTextView = root.findViewById(R.id.wind_date_text_View);
        windTextView.setText(" Wind Speed " + weatherData.get(4) + " ");
    }

}