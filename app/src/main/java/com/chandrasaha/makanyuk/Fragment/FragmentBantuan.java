package com.chandrasaha.makanyuk.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandrasaha.makanyuk.R;

/**
 * Created by Chandra Saha on 2/25/2016.
 */
public class FragmentBantuan extends Fragment {
    private static final String KEY_TITLE = "Help";
    static Context ctx;
    public static FragmentBantuan newInstance(Context context, String title)
    {
        FragmentBantuan f = new FragmentBantuan();
        ctx = context;

        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        f.setArguments(args);
        return (f);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //ThemeManager.init(getActivity(), 1, 0, null);
        final View viewRoot = inflater.inflate(R.layout.fragment_bantuan, container, false);

        return viewRoot;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}
