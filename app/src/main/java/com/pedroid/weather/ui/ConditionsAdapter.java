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

    private List<String> locations = new ArrayList<>();
    private int offset = 0;

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

    public void deleteItem(int position) {
        offset += locations.size();
        locations.remove(position);
    }

    @Override
    public int getItemPosition(Object object){
        return FragmentPagerAdapter.POSITION_NONE;
    }

    @Override
    public long getItemId(int position) {
        return position + offset;
    }
}
