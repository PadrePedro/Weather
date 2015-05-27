package com.pedroid.weather.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pedroid.weather.R;
import com.pedroid.weather.api.IConditionsRequest;
import com.pedroid.weather.api.RequestFactory;
import com.pedroid.weather.api.RequestProcessor;
import com.pedroid.weather.api.RequestProcessor.ThreadCount;
import com.pedroid.weather.model.Settings;
import com.pedroid.weather.widget.Switch;

/**
 * Created by pedro on 5/24/15.
 */
public class SettingsFragment extends Fragment {

    private Switch temperatureSwitch;
    private Switch velocitySwitch;
    private Switch threadsSwitch;
    private Switch apiFactorySwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_settings, container, false);
        temperatureSwitch = (Switch)layout.findViewById(R.id.temperatureSwitch);
        temperatureSwitch.selectValue(Settings.getInstance(getActivity()).getTempUnit().ordinal());
        temperatureSwitch.setSwitchListener(new Switch.SwitchListener() {
            @Override
            public void onValue1Selected() {
                Settings.getInstance(getActivity()).setTempUnit(IConditionsRequest.TempUnit.FAHRENHEIT);
            }

            @Override
            public void onValue2Selected() {
                Settings.getInstance(getActivity()).setTempUnit(IConditionsRequest.TempUnit.CELSIUS);
            }
        });
        velocitySwitch = (Switch)layout.findViewById(R.id.velocitySwitch);
        velocitySwitch.selectValue(Settings.getInstance(getActivity()).getVelocityUnit().ordinal());
        velocitySwitch.setSwitchListener(new Switch.SwitchListener() {
            @Override
            public void onValue1Selected() {
                Settings.getInstance(getActivity()).setVelocityUnit(IConditionsRequest.VelocityUnit.MPH);
            }

            @Override
            public void onValue2Selected() {
                Settings.getInstance(getActivity()).setVelocityUnit(IConditionsRequest.VelocityUnit.KPH);
            }
        });
        threadsSwitch = (Switch)layout.findViewById(R.id.threadSwitch);
        threadsSwitch.selectValue(Settings.getInstance(getActivity()).getVelocityUnit().ordinal());
        threadsSwitch.setSwitchListener(new Switch.SwitchListener() {
            @Override
            public void onValue1Selected() {
                Settings.getInstance(getActivity()).setThreadCount(RequestProcessor.ThreadCount.ONE);
                RequestProcessor.setThreads(RequestProcessor.THREADS_ONE);
            }

            @Override
            public void onValue2Selected() {
                Settings.getInstance(getActivity()).setThreadCount(ThreadCount.MULTI);
                RequestProcessor.setThreads(RequestProcessor.THREADS_MANY);
            }
        });
        apiFactorySwitch = (Switch)layout.findViewById(R.id.apiFactorySwitch);
        apiFactorySwitch.selectValue(Settings.getInstance(getActivity()).getRequestFactory().ordinal());
        apiFactorySwitch.setSwitchListener(new Switch.SwitchListener() {
            @Override
            public void onValue1Selected() {
                Settings.getInstance(getActivity()).setRequestFactory(RequestFactory.Factory.OPEN_WEATHER_MAP);
                RequestFactory.setFactory(RequestFactory.Factory.OPEN_WEATHER_MAP);
            }

            @Override
            public void onValue2Selected() {
                Settings.getInstance(getActivity()).setRequestFactory(RequestFactory.Factory.YAHOO);
                RequestFactory.setFactory(RequestFactory.Factory.YAHOO);
            }
        });
        return layout;
    }


}
