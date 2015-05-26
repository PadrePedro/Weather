package com.pedroid.weather.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.pedroid.weather.R;
import com.pedroid.weather.api.IRequest;
import com.pedroid.weather.api.IRequestListener;

/**
 * Created by pedro on 5/23/15.
 */
public class AddLocationFragment extends DialogFragment implements IRequestListener {

    interface AddLocationListener {
        void locationAdded(String location);
    }
    public static String LOCATION_ADDED = "location_added";
    public static String LOCATION = "location";

    private EditText locationEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FrameLayout frameView = new FrameLayout(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.fragment_add_location, frameView);
        locationEditText = (EditText)dialoglayout.findViewById(R.id.locationEditText);
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.add_location)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = new Intent(LOCATION_ADDED);
                                intent.putExtra(LOCATION, locationEditText.getText().toString());
                                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                            }
                        }
                )
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
//                                ((FragmentAlertDialog)getActivity()).doNegativeClick();
                            }
                        }
                )
                .setView(frameView)
                .create();
    }

    @Override
    public void onSuccess(IRequest request) {

    }

    @Override
    public void onFailure(IRequest request, String reason) {

    }
}
