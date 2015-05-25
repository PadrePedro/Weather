package com.pedroid.weather.ui;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pedroid.weather.R;
import com.pedroid.weather.api.IConditionsRequest;
import com.pedroid.weather.api.Request;
import com.pedroid.weather.api.RequestFactory;
import com.pedroid.weather.api.RequestListener;
import com.pedroid.weather.api.RequestProcessor;
import com.pedroid.weather.model.Settings;

/**
 * Created by pedro on 5/22/15.
 */
public class ConditionsFragment extends Fragment implements RequestListener {

    private String location;
    private TextView temperatureTextView;
    private TextView locationTextView;
    private TextView conditionTextView;
    private IConditionsRequest conditionsRequest;

    private static final String LOCATION = "location";

    public static ConditionsFragment newInstance(String location) {
        ConditionsFragment fragment = new ConditionsFragment();
        Bundle args = new Bundle();
        args.putString(LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location = getArguments().getString(LOCATION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conditions, container, false);
        temperatureTextView = (TextView)view.findViewById(R.id.temperatureTextView);
        locationTextView = (TextView)view.findViewById(R.id.locationTextView);
        conditionTextView = (TextView)view.findViewById(R.id.conditionTextView);
        refreshData();
        return view;
    }

    private void refreshData() {
        if (IConditionsRequest.CURRENT_LOCATION.equals(location)) {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                conditionsRequest = RequestFactory.getInstance().getConditionsRequest(location.getLatitude(), location.getLongitude(), this);
            }
        }
        else {
            conditionsRequest = RequestFactory.getInstance().getConditionsRequest(location, this);
        }
        if (conditionsRequest != null)
            RequestProcessor.execute(conditionsRequest);
    }

    @Override
    public void onFailure(Request request, String reason) {

    }

    @Override
    public void onSuccess(Request request) {
        getView().post(new Runnable() {
            @Override
            public void run() {
                temperatureTextView.setText(Double.toString(conditionsRequest.getTemperature(Settings.getTempUnit())));
                locationTextView.setText(conditionsRequest.getLocation());
                conditionTextView.setText(conditionsRequest.getConditions());
            }
        });
    }

}
