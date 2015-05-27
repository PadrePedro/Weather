package com.pedroid.weather.ui;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.renderscript.Sampler;
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
import com.pedroid.weather.api.RequestFactory;
import com.pedroid.weather.api.IRequestListener;
import com.pedroid.weather.api.RequestProcessor;
import com.pedroid.weather.model.RequestCache;
import com.pedroid.weather.model.Settings;
import com.pedroid.weather.utils.BroadcastUtils;
import com.pedroid.weather.utils.LocationUtils;

/**
 * Created by pedro on 5/22/15.
 *
 * Main fragment for each location
 *
 */
public class ConditionsFragment extends Fragment implements IRequestListener, LocationListener {


    // views
    private View layout;
    private TextView temperatureTextView;
    private TextView temperatureUnitTextView;
    private TextView tempHiTextView;
    private TextView tempLoTextView;
    private TextView windTextView;
    private TextView humidityTextView;
    private TextView locationTextView;
    private TextView conditionTextView;
    private ImageView conditionsIconImageView;
    private TextView enableLocationTextView;

    private String location;
    private IConditionsRequest conditionsRequest;
    private BroadcastReceiver receiver;
    private LocationManager locationManager;

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
        layout = inflater.inflate(R.layout.fragment_conditions, container, false);
        enableLocationTextView = (TextView)layout.findViewById(R.id.enableLocationTextView);
        enableLocationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent( android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getActivity().startActivity(myIntent);
            }
        });

        temperatureTextView = (TextView)layout.findViewById(R.id.temperatureTextView);
        temperatureUnitTextView = (TextView)layout.findViewById(R.id.temperatureUnitTextView);
        locationTextView = (TextView)layout.findViewById(R.id.locationTextView);
        conditionTextView = (TextView)layout.findViewById(R.id.conditionTextView);
        conditionsIconImageView = (ImageView)layout.findViewById(R.id.conditionsIconImageView);
        tempHiTextView = (TextView)layout.findViewById(R.id.tempHiTextView);
        tempLoTextView = (TextView)layout.findViewById(R.id.tempLoTextView);
        windTextView = (TextView)layout.findViewById(R.id.windTextView);
        humidityTextView = (TextView)layout.findViewById(R.id.humidityTextView);
        refreshData();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastUtils.MSG_REFRESH);
        filter.addAction(BroadcastUtils.MSG_REDRAW);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(BroadcastUtils.MSG_REFRESH))
                    refreshData();
                else if (intent.getAction().equals(BroadcastUtils.MSG_REDRAW))
                    updateView();
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
        return layout;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    /**
     * Redraw the values from the model. Primarily used when units are changed.
     */
    private void refreshData() {
        conditionsRequest = RequestCache.getInstance().getConditions(location);
        if (conditionsRequest != null) {
            updateView();
        }
        else {
            if (IConditionsRequest.CURRENT_LOCATION.equals(location)) {
                if (LocationUtils.isLocationServicesEnabled(getActivity())) {
                    locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        conditionsRequest = RequestFactory.getInstance().getConditionsRequest(location.getLatitude(), location.getLongitude(), this);
                    } else {
                        Criteria criteria = new Criteria();
                        criteria.setAccuracy(Criteria.NO_REQUIREMENT);
                        locationManager.requestSingleUpdate(criteria, this, null);
                    }
                }
                else {
                    enableLocationTextView.setVisibility(View.VISIBLE);
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

    /**
     * Redraw the fragment
     */
    private void updateView() {
        Settings s = Settings.getInstance(getActivity());
        fadeIn();
        enableLocationTextView.setVisibility(View.INVISIBLE);
        temperatureTextView.setText(String.format("%d",
                (int) conditionsRequest.getTemperature(s.getTempUnit()),
                s.getTempUnitString()));
        temperatureUnitTextView.setText(s.getTempUnitString());
        locationTextView.setText(conditionsRequest.getLocation());
        conditionTextView.setText(conditionsRequest.getConditions());
        conditionsIconImageView.setImageResource(getConditionsIcon(conditionsRequest.getConditions()));
        tempHiTextView.setText(String.format("%d", (int) conditionsRequest.getHiTemperature(s.getTempUnit())));
        tempLoTextView.setText(String.format("%d", (int)conditionsRequest.getLoTemperature(s.getTempUnit())));
        windTextView.setText(String.format("%d %s", (int)conditionsRequest.getWindVelocity(s.getVelocityUnit()), s.getVelocityUnitString()));
        humidityTextView.setText(String.format("%d%%", (int) conditionsRequest.getHumidity()));
    }

    /**
     * Map conditions string to weather icon
     *
     * @param conditions
     * @return weather icon resource id
     */
    private int getConditionsIcon(String conditions) {
        String cond = conditions.toLowerCase();
        if (cond.contains("rain"))
            return R.drawable.cond_rain;
        if (cond.contains("clear") || cond.contains("sun"))
            return R.drawable.cond_sunny;
        if (cond.contains("cloud"))
            return R.drawable.cond_cloudy;
        if (cond.contains("wind"))
            return R.drawable.cond_windy;
        return R.drawable.cond_partly;
    }

    @Override
    public void onLocationChanged(Location location) {
        conditionsRequest = RequestFactory.getInstance().getConditionsRequest(location.getLatitude(), location.getLongitude(), this);
        RequestProcessor.execute(conditionsRequest);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * fade in entire view
     */
    private void fadeIn() {
        ValueAnimator animator = ValueAnimator.ofFloat(0,1);
        animator.setDuration(400);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float alpha = (Float)animation.getAnimatedValue();
                layout.setAlpha(alpha);
            }
        });
        animator.start();
    }
}
