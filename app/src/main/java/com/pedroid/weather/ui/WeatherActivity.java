package com.pedroid.weather.ui;

import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.location.LocationManager;
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

import com.pedroid.weather.R;
import com.pedroid.weather.api.IConditionsRequest;
import com.pedroid.weather.api.RequestProcessor;
import com.pedroid.weather.api.RequestProcessor.ThreadCount;
import com.pedroid.weather.model.RequestCache;
import com.pedroid.weather.model.Settings;
import com.pedroid.weather.utils.BroadcastUtils;
import com.pedroid.weather.widget.CrossFadeImageView;


public class WeatherActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener {

    private IConditionsRequest conditionsRequest;
    private ViewPager pager;
    private com.pedroid.weather.ui.ConditionsAdapter adapter;
    private LocationManager locationManager;
    private BroadcastReceiver receiver;
    private DrawerLayout drawerLayout;
    private ImageView bgConditionsImageView;
    private Bitmap bm1;
    private Bitmap bm2;


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
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        bgConditionsImageView = (ImageView)findViewById(R.id.bgConditionImageView);
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
        findViewById(R.id.newImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dlg = new AddLocationFragment();
                dlg.show(getFragmentManager(), "tag");
            }
        });
        pager = (ViewPager)findViewById(R.id.pager);
        adapter = new com.pedroid.weather.ui.ConditionsAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(this);

        // setup broadcast receiver
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

        bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_cond_cloudy);
        bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_cond_sunny);

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
//        Bitmap bm = BitmapFactory.decodeResource(getResources(),
//                position % 2 == 0 ? R.drawable.bg_cond_any : R.drawable.bg_cond_cloudy);
//        bgConditionsImageView.setImage(bm);
        if (position % 2 == 0) {
//            bgConditionsImageView.setImageBitmap(bm1);
        }
        else {
//            bgConditionsImageView.setImageBitmap(bm2);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
