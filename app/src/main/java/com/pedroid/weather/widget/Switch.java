package com.pedroid.weather.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pedroid.weather.R;

import org.w3c.dom.Text;

/**
 * Created by pedro on 5/24/15.
 */
public class Switch extends LinearLayout {

    public interface SwitchListener {
        void onValue1Selected();
        void onValue2Selected();
    }

    private SwitchListener listener;
    private TextView labelTextView;
    private TextView value1TextView;
    private TextView value2TextView;
    private boolean value1Selected;

    public Switch(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.widget_switch, this, true);
        labelTextView = (TextView)layout.findViewById(R.id.labelTextView);
        value1TextView = (TextView)layout.findViewById(R.id.value1TextView);
        value1TextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onValue1Selected();
                }
                value1Selected = true;
                refresh();
            }
        });
        value2TextView = (TextView)layout.findViewById(R.id.value2TextView);
        value2TextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onValue2Selected();
                }
                value1Selected = false;
                refresh();
            }
        });

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.Switch, 0, 0);
        String label = a.getString(R.styleable.Switch_label);
        String value1 = a.getString(R.styleable.Switch_value1Text);
        String value2 = a.getString(R.styleable.Switch_value2Text);
        if (label != null) {
            labelTextView.setText(label);
        }
        if (value1 != null) {
            value1TextView.setText(value1);
        }
        if (value2 != null) {
            value2TextView.setText(value2);
        }
        a.recycle();

        refresh();
    }

    public void setSwitchListener(SwitchListener listener) {
        this.listener = listener;
    }

    public void setValue1(int resourceId) {
        value1TextView.setText(resourceId);
    }

    public void setValue2(int resourceId) {
        value2TextView.setText(resourceId);
    }

    public void selectValue1() {
        value1Selected = true;
        refresh();
    }

    public void selectValue2() {
        value1Selected = false;
        refresh();
    }

    public void selectValue(int value) {
        if (value == 0) {
            selectValue1();
        }
        else {
            selectValue2();
        }
    }

    private void refresh() {
        value1TextView.setBackgroundResource(value1Selected ? R.color.switch_selected_background : R.color.switch_deselected_background);
        value2TextView.setBackgroundResource(value1Selected ? R.color.switch_deselected_background : R.color.switch_selected_background);
    }
}
