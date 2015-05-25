package com.pedroid.weather.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pedroid.weather.R;
import com.pedroid.weather.api.IConditionsRequest;
import com.pedroid.weather.model.Settings;
import com.pedroid.weather.widget.Switch;

/**
 * Created by pedro on 5/24/15.
 */
public class SettingsFragment extends Fragment {

    private Switch temperatureSwitch;
    private Switch velocitySwitch;
    private Switch threadsSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_settings, container, false);
        temperatureSwitch = (Switch)layout.findViewById(R.id.temperatureSwitch);
        temperatureSwitch.selectValue(Settings.getTempUnit().ordinal());
        temperatureSwitch.setSwitchListener(new Switch.SwitchListener() {
            @Override
            public void onValue1Selected() {
                Settings.setTempUnit(IConditionsRequest.TempUnit.FAHRENHEIT);
            }

            @Override
            public void onValue2Selected() {
                Settings.setTempUnit(IConditionsRequest.TempUnit.FAHRENHEIT);
            }
        });
        velocitySwitch = (Switch)layout.findViewById(R.id.velocitySwitch);
        velocitySwitch.selectValue(Settings.getVelocityUnit().ordinal());

        threadsSwitch = (Switch)layout.findViewById(R.id.threadSwitch);
        threadsSwitch.selectValue(Settings.getVelocityUnit().ordinal());

        return layout;
    }


}
