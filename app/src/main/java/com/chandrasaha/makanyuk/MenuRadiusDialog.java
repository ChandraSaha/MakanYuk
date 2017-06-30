package com.chandrasaha.makanyuk;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chandrasaha.makanyuk.Fragment.FragmentTerdekat;

/**
 * Created by Chandra Saha on 11/30/2015.
 */
public class MenuRadiusDialog extends DialogFragment {
    private TextView tv;
    private int radius;
    public MenuRadiusDialog(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_radius, container);
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.your_dialog_seekbar);

        tv = (TextView) view.findViewById(R.id.tv_menu_radius);
        tv.setText(FragmentTerdekat.radius*100+"m");
        final Button button = (Button) view.findViewById(R.id.your_dialog_button);
        button.setText("OK");
        radius =2;
        seekBar.setProgress(FragmentTerdekat.radius);
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                radius = i;
                tv.setText(radius*100+" m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        getDialog().setTitle("Atur jarak dari lokasi");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.Fragment prev = getFragmentManager().findFragmentByTag("a");
                if (prev != null) {
                    DialogFragment df = (DialogFragment) prev;
                    FragmentTerdekat.radiusChange = true;
                    FragmentTerdekat.radius = radius;
                    df.dismiss();
                }
            }
        });

        return view;
    }

}
