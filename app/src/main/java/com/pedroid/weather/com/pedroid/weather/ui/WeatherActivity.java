package com.pedroid.weather.com.pedroid.weather.ui;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.pedroid.weather.R;
import com.pedroid.weather.api.IConditionsRequest;
import com.pedroid.weather.api.RequestFactory;
import com.pedroid.weather.api.RequestProcessor;
import com.pedroid.weather.api.Request;
import com.pedroid.weather.api.RequestListener;
import com.pedroid.weather.api.openweathermap.ConditionsRequest;


public class WeatherActivity extends ActionBarActivity implements RequestListener {

    private IConditionsRequest conditionsRequest;
    private ViewPager pager;
    private ConditionsAdapter adapter;
    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        pager = (ViewPager)findViewById(R.id.pager);
        adapter = new ConditionsAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(Request request) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Request request, String reason) {

    }
}
