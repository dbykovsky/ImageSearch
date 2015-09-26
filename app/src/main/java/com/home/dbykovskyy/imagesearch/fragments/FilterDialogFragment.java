package com.home.dbykovskyy.imagesearch.fragments;


import android.content.DialogInterface;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.home.dbykovskyy.imagesearch.R;
import com.home.dbykovskyy.imagesearch.activities.SearchActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Created by dbykovskyy on 9/23/15.
 */
public class FilterDialogFragment extends DialogFragment {

    public DialogInterface.OnClickListener positiveClickListener;
    public DialogInterface.OnClickListener cancelClickListener;
    public DialogInterface.OnClickListener onChoiceClickListener;

    public FilterDialogFragment() {
        // Empty constructor required for DialogFragment
    }


    public void setPositiveListener(DialogInterface.OnClickListener positiveListener){
        this.positiveClickListener=positiveListener;
    }

    public void setCancelClickListener(DialogInterface.OnClickListener cancelListener){
        this.cancelClickListener = cancelListener;
    }

    public void setOnChoice1ClickListener(DialogInterface.OnClickListener onChoiceListener){
        this.onChoiceClickListener=onChoiceListener;
    }


    public static FilterDialogFragment newInstance(String title) {
        FilterDialogFragment filterFragment = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        filterFragment.setArguments(args);
        return filterFragment;
    }

/*    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);

        //alertDialogBuilder.setSingleChoiceItems(ItemPriority.priority, -1, onChoiceClickListener);
        alertDialogBuilder.setPositiveButton("SAVE", positiveClickListener);
        alertDialogBuilder.setNegativeButton("Cancel", cancelClickListener);
        return alertDialogBuilder.create();
    }*/

/*    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
    }*/

    @Override
    public void onResume() {
        super.onResume();
/*        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        getDialog().getWindow().setLayout(width, height);*/

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(STYLE_NO_TITLE, 0);

        //setStyle(STYLE_NO_INPUT, R.style.AppDialogTheme);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.filter_fragment, container, false);

         final Map<Integer, Integer> allSpinners =  new HashMap<>();
            allSpinners.put(R.id.sp_imageSize, R.array.size);
            allSpinners.put(R.id.sp_imageType, R.array.type);
            allSpinners.put(R.id.sp_colorFilter, R.array.color);

        //SET SPINNERS SPINNER
        for (Map.Entry<Integer, Integer> entry : allSpinners.entrySet()){
            Spinner spinner = (Spinner) view.findViewById(entry.getKey());
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), entry.getValue(), R.layout.custom_spinner_item); //android.R.layout.simple_spinner_item); // Create an ArrayAdapter using the string array and a default spinner layout
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
            spinner.setAdapter(adapter);
        }

        //SET TEXT

        final EditText et_site= (EditText)view.findViewById(R.id.et_site);
        et_site.setGravity(Gravity.LEFT);

        //SET BUTTON CLICK LISTENERS
        Button save = (Button)view.findViewById(R.id.bt_save);
        Button cancel = (Button)view.findViewById(R.id.bt_cancell);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> allSpinnerValues = new LinkedList<String>();

                for (Map.Entry<Integer, Integer> entry : allSpinners.entrySet()) {

                    Spinner mySpinner = (Spinner) view.findViewById(entry.getKey());
                    String text = mySpinner.getSelectedItem().toString();
                    allSpinnerValues.add(text);
                }
                //add website
                allSpinnerValues.add(et_site.getText().toString());

                SearchActivity callingActivity = (SearchActivity) getActivity();
                callingActivity.onUserSelectValue(allSpinnerValues);
                dismiss();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //SET TITLE;
        String title = getArguments().getString("title");
        setStyle(STYLE_NO_INPUT, R.style.AppDialogTheme);
        getDialog().setTitle(title);
        return view;

    }


}
