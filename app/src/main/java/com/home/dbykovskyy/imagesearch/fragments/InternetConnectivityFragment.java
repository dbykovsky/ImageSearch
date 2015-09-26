package com.home.dbykovskyy.imagesearch.fragments;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;


/**
 * Created by dbykovskyy on 9/26/15.
 */
public class InternetConnectivityFragment extends android.support.v4.app.DialogFragment{


    public InternetConnectivityFragment() {
        // Empty constructor required for DialogFragment
    }

    public static InternetConnectivityFragment newInstance(String title) {

        InternetConnectivityFragment frag = new InternetConnectivityFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);

        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Please check your connection and try again");

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return alertDialogBuilder.create();
    }


}
