package com.pedroid.weather.ui;

import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.pedroid.weather.R;
import com.pedroid.weather.api.IConditionsRequest;
import com.pedroid.weather.api.Request;
import com.pedroid.weather.api.RequestListener;


public class WeatherActivity extends ActionBarActivity implements RequestListener {

    private IConditionsRequest conditionsRequest;
    private ViewPager pager;
    private com.pedroid.weather.ui.ConditionsAdapter adapter;
    private LocationManager locationManager;
    private BroadcastReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        pager = (ViewPager)findViewById(R.id.pager);
        adapter = new com.pedroid.weather.ui.ConditionsAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String location = intent.getStringExtra(AddLocationFragment.LOCATION);
                adapter.add(location);
                adapter.notifyDataSetChanged();
                pager.setCurrentItem(adapter.getCount()-1);
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(AddLocationFragment.LOCATION_ADDED));
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
            DialogFragment dlg = new AddLocationFragment();
            dlg.show(getFragmentManager(), "tag");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onSuccess(Request request) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Request request, String reason) {

    }
}
