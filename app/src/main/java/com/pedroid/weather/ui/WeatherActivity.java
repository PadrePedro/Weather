package com.pedroid.weather.ui;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.pedroid.weather.R;
import com.pedroid.weather.api.IConditionsRequest;
import com.pedroid.weather.api.IRequest;
import com.pedroid.weather.api.IRequestListener;
import com.pedroid.weather.api.RequestFactory;
import com.pedroid.weather.api.RequestProcessor;
import com.pedroid.weather.api.RequestProcessor.ThreadCount;
import com.pedroid.weather.model.RequestCache;
import com.pedroid.weather.model.Settings;
import com.pedroid.weather.utils.BroadcastUtils;


public class WeatherActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener, IRequestListener {

    private ViewPager pager;
    private ConditionsAdapter adapter;
    private BroadcastReceiver receiver;
    private DrawerLayout drawerLayout;
    private ImageView bgConditionsImageView;
    private ImageView deleteLocationImageView;

    private class WeatherActivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String location = intent.getStringExtra(AddLocationFragment.LOCATION);
            IConditionsRequest req = RequestFactory.getInstance().getConditionsRequest(location, WeatherActivity.this);
            RequestProcessor.execute(req);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide status bar
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        RequestProcessor.setThreads(Settings.getInstance(this).getThreadCount() == ThreadCount.ONE ? RequestProcessor.THREADS_ONE : RequestProcessor.THREADS_MANY);

        // setup layout
        setContentView(R.layout.activity_weather);
        bgConditionsImageView = (ImageView)findViewById(R.id.bgConditionImageView);
        bgConditionsImageView.setImageResource(getRandomBackground());
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        deleteLocationImageView = (ImageView)findViewById(R.id.deleteLocationImageView);
        deleteLocationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDeleteLocation();
            }
        });
        findViewById(R.id.overflowImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        findViewById(R.id.refreshImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestCache.getInstance().clear();
                LocalBroadcastManager.getInstance(WeatherActivity.this).sendBroadcast(new Intent(BroadcastUtils.MSG_REFRESH));
            }
        });
        findViewById(R.id.newLocationImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dlg = new AddLocationFragment();
                dlg.show(getFragmentManager(), "tag");
            }
        });
        pager = (ViewPager)findViewById(R.id.pager);
        adapter = new com.pedroid.weather.ui.ConditionsAdapter(getSupportFragmentManager());
        adapter.setLoctions(Settings.getInstance(this).getLocations());
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(this);

        // setup broadcast receiver
        receiver = new WeatherActivityReceiver();
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
        else if (id == R.id.action_refresh) {
            refreshData();
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshData() {
        Intent intent = new Intent(BroadcastUtils.MSG_REFRESH);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private int getRandomBackground() {
        int resources[] = { R.drawable.bg_cond_clear,
                            R.drawable.bg_cond_fair,
                            R.drawable.bg_cond_boats,
                            R.drawable.bg_cond_sunny,
                            R.drawable.bg_cond_plant,
                            R.drawable.bg_cond_city_sky,
                            R.drawable.bg_cond_bw,
                            R.drawable.bg_cond_rain};
        return resources[(int)(System.currentTimeMillis()/10000) % resources.length];
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        deleteLocationImageView.setVisibility(position>0 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



    private void handleDeleteLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_delete_location)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        adapter.deleteItem(pager.getCurrentItem());
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create().show();

    }

    @Override
    public void onFailure(final IRequest request, String reason) {
        if (!isFinishing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(WeatherActivity.this, R.string.location_not_found, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onSuccess(final IRequest request) {
        if (!isFinishing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    IConditionsRequest req = (IConditionsRequest)request;
                    adapter.add(req.getLocation());
                    adapter.notifyDataSetChanged();
                    pager.setCurrentItem(adapter.getCount() - 1);
                    Settings.getInstance(WeatherActivity.this).setLocations(adapter.getLocations());
                }
            });
        }
    }
}
