package com.sc2guide.sc2_guides_android.view.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.sc2guide.sc2_guides_android.MainActivity;
import com.sc2guide.sc2_guides_android.R;

public class ErrorFragment extends DialogFragment {

    public static ErrorFragment newInstance(String title, String message) {
        ErrorFragment frag = new ErrorFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");

        return new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setTitle(title)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((MainActivity)getActivity()).doPositiveClick();
                            }
                        }
                )
                .create();
    }
}