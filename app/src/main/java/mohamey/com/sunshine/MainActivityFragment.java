package mohamey.com.sunshine;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
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
        ArrayList<String> weather = new ArrayList<String>(Arrays.asList(weatherArray));
        //Create an adapter to display weather items. Layout defines the layout of all child elements using id structure
        //List Item forecast is the parent in this scenario - the layout. It's the listview contained within fragment_main
        //List item forecast textview is the child of list item forecast, list item forecast is literally a list of these text views
        ArrayAdapter<String> weatherAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, weather);
        //Create a new listView defined by the id Listview_forecast found in fragment_main
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        //Bind the weatherAdapter to the listview. This means the listView loads appropriate list items using this adapter
        listView.setAdapter(weatherAdapter);
        return rootView;
    }
}
