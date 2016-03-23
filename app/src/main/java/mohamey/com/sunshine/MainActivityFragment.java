package mohamey.com.sunshine;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ArrayList<String> weather = new ArrayList<String>();
        weather.add("Today - Sunny - 88/63");
        weather.add("Tomorrow - Foggy - 66/69");
        weather.add("Weds - Cloudy - 72/63");
        weather.add("Thurs - Rainy - 64/51");
        weather.add("Fri - Foggy - 70/46");
        weather.add("Sat - Sunny - 76/68");

        ArrayAdapter<String> weatherAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, weather);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(weatherAdapter);
        return rootView;
    }
}
