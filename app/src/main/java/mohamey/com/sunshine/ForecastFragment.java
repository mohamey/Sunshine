package mohamey.com.sunshine;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {
    final String TAG = ForecastFragment.class.getSimpleName();

    public ForecastFragment() {
    }

    //This is the first method that is called when creating the fragment
    //By overriding, we define our own start up procedure
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        //Show that we have option menu to display
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get the View for the fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //Populate an array list for dummy data
        String[] weatherArray = {
                "Today - Sunny - 88/63",
                "Tomorrow - Foggy - 66/69",
                "Weds - Cloudy - 72/63",
                "Thurs - Rainy - 64/51",
                "Fri - Foggy - 70/46",
                "Sat - Sunny - 76/68",
                "Sun - Sunny - 88/63",
                "Mon - Foggy - 66/69",
                "Tues - Cloudy - 72/63",
                "Weds - Rainy - 64/51",
                "Thurs - Foggy - 70/46",
                "Fri - Sunny - 76/68"
        };
        ArrayList<String> weather = new ArrayList<>(Arrays.asList(weatherArray));
        //Create an adapter to display weather items. Layout defines the layout of all child elements using id structure
        //List Item forecast is the parent in this scenario - the layout. It's the listview contained within fragment_main
        //List item forecast textview is the child of list item forecast, list item forecast is literally a list of these text views
        ArrayAdapter<String> weatherAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, weather);
        //Create a new listView defined by the id Listview_forecast found in fragment_main
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        //Bind the weatherAdapter to the listview. This means the listView loads appropriate list items using this adapter
        listView.setAdapter(weatherAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Context context = adapterView.getContext();
                String text = adapterView.getItemAtPosition(position).toString();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
        return rootView;
    }

    //Options menu generator for the fragment
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.forecast_fragment, menu);
    }

    //Handler for when menu items are selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(id){
            case R.id.action_refresh: {
                new FetchWeatherTask().execute("7778677");
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //Resource Requests cannot take place on the main thread, they must be made in an Async task
    private class FetchWeatherTask extends AsyncTask<String, Void, Void>{
        protected Void doInBackground(String...params){
            //If no parameters are passed in, just exit the task
            if(params.length == 0){
                return null;
            }
            //Initialise all the variables that will be used
            String city = params[0], jsonResponse=null, result;
            HttpURLConnection con = null;
            BufferedReader reader = null;
            Uri.Builder builder = new Uri.Builder();
            URL url;

            //Build the request. Useful for when the link isn't always going to be the same
            builder.path("http://api.openweathermap.org/data/2.5/forecast");
            builder.appendQueryParameter("id", city);
            builder.appendQueryParameter("mode", "json");
            builder.appendQueryParameter("units", "metric");
            builder.appendQueryParameter("cnt", "7");
            builder.appendQueryParameter("APPID", "0382df99b110a240f2942a92a24e8f0c");
            result = builder.build().toString();
            try{
                //Must call decode to parse all '%' encoded chars
                url = new URL(Uri.decode(result));
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();

                //Read input stream
                InputStream in = con.getInputStream();
                if(in == null){
                    return null;
                }
                StringBuilder buffer = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(in));

                //Read in all the response until null
                String line;
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                    buffer.append("\n");
                }
                if(buffer.length() == 0){
                    //Buffer was empty, no point parsing
                    return null;
                }else {
                    //Code to return max temp on second day
                    jsonResponse = buffer.toString();
                    JSONObject jobj = new JSONObject(jsonResponse);
                    System.out.println(jobj);
                    JSONArray jarray = jobj.getJSONArray("list");
                    JSONObject requiredDay = jarray.getJSONObject(2);
                    jobj = requiredDay.getJSONObject("main");
                    System.out.println(jobj);
                    String res = jobj.getString("temp_max");
                    Log.i(TAG, res);
                }
            }catch(Exception except){
                Log.e(TAG, "There was an error accessing openweather APIs", except);
            }finally {
                if(con != null){
                    con.disconnect();
                }
                if(reader != null){
                    try{
                        reader.close();
                    }catch(final IOException e){
                        Log.e(TAG, "There was an error closing the stream", e);
                    }
                }
            }
            Log.v(TAG, "Json Response: "+jsonResponse);
            return null;
        }
    }
}
