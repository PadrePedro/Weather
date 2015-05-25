package com.pedroid.weather.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pedroid.weather.R;
import com.pedroid.weather.api.IConditionsRequest;
import com.pedroid.weather.api.IRequest;
import com.pedroid.weather.api.Request;
import com.pedroid.weather.api.RequestFactory;
import com.pedroid.weather.api.RequestListener;
import com.pedroid.weather.api.RequestProcessor;
import com.pedroid.weather.model.RequestCache;
import com.pedroid.weather.model.Settings;
import com.pedroid.weather.utils.BroadcastUtils;

/**
 * Created by pedro on 5/22/15.
 */
public class ConditionsFragment extends Fragment implements RequestListener {

    private ImageView backgroundImageView;
    private String location;
    private TextView temperatureTextView;
    private TextView temperatureUnitTextView;
    private TextView locationTextView;
    private TextView conditionTextView;
    private IConditionsRequest conditionsRequest;
    private BroadcastReceiver receiver;

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
        backgroundImageView = (ImageView)view.findViewById(R.id.backgroundImageView);
        temperatureTextView = (TextView)view.findViewById(R.id.temperatureTextView);
        temperatureUnitTextView = (TextView)view.findViewById(R.id.temperatureUnitTextView);
        locationTextView = (TextView)view.findViewById(R.id.locationTextView);
        conditionTextView = (TextView)view.findViewById(R.id.conditionTextView);
        refreshData();

        IntentFilter filter = new IntentFilter(BroadcastUtils.MSG_REFRESH);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshData();
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    private void refreshData() {
        conditionsRequest = RequestCache.getInstance().getConditions(location);
        if (conditionsRequest != null) {
            updateView();
        }
        else {
            if (IConditionsRequest.CURRENT_LOCATION.equals(location)) {
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    conditionsRequest = RequestFactory.getInstance().getConditionsRequest(location.getLatitude(), location.getLongitude(), this);
                }
            } else {
                conditionsRequest = RequestFactory.getInstance().getConditionsRequest(location, this);
            }
            if (conditionsRequest != null)
                RequestProcessor.execute(conditionsRequest);
        }
    }

    @Override
    public void onFailure(IRequest request, String reason) {

    }

    @Override
    public void onSuccess(IRequest request) {
        RequestCache.getInstance().putConditions(location, (IConditionsRequest)request);
        if (getView() != null) {
            getView().post(new Runnable() {
                @Override
                public void run() {
                    updateView();
                }
            });
        }
    }

    private void updateView() {
        temperatureTextView.setText(String.format("%d",
                (int) conditionsRequest.getTemperature(Settings.getTempUnit()),
                Settings.getTempUnitString()));
        temperatureUnitTextView.setText(Settings.getTempUnitString());
        locationTextView.setText(conditionsRequest.getLocation());
        conditionTextView.setText(conditionsRequest.getConditions());
    }

    private int getBackgroundResource(String conditions) {
        String cond = conditions.toLowerCase();
        if (cond.contains("rain"))
            return R.drawable.cond_rain;
        if (cond.contains("clear"))
            return R.drawable.cond_clear;
        if (cond.contains("cloud"))
            return R.drawable.cond_cloudy;
        if (cond.contains("sunny"))
            return R.drawable.cond_sunny;
        return R.drawable.cond_any;
    }

}
