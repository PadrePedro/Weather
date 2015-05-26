package com.pedroid.weather.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pedroid.weather.api.IConditionsRequest;
import com.pedroid.weather.utils.JsonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedro on 5/22/15.
 */
public class ConditionsAdapter extends FragmentPagerAdapter {

    private static class LocationList extends ArrayList<String> implements Serializable {
    }

    private List<String> locations = new ArrayList<>();

    public ConditionsAdapter(FragmentManager fm) {
        super(fm);
        locations.add(IConditionsRequest.CURRENT_LOCATION);
        locations.add("Hong Kong");
        locations.add("London");
    }

    public void add(String location) {
        locations.add(location);
    }

    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public Fragment getItem(int position) {
        return ConditionsFragment.newInstance(locations.get(position));
    }

    public List getLocations() {
        return locations;
    }

    public void setLoctions(List locations) {
        this.locations = locations;
    }
}
