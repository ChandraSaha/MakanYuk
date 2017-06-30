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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chandrasaha.makanyuk.R;

/**
 * Created by Chandra Saha on 2/15/2016.
 */
public class FragmentHome extends Fragment implements View.OnClickListener {
    private static final String KEY_TITLE = "Home";
    private static Context ctx;
    private CardView cardViewRestaurat;
    private CardView cardViewCafe;
    private CardView cardViewFastFood;
    private CardView cardViewCofeeShop;
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private ImageView iv4;
    public static FragmentHome newInstance(Context context, String title)
    {
        FragmentHome f = new FragmentHome();
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
        final View viewRoot = inflater.inflate(R.layout.fragment_home, container, false);
        cardViewRestaurat = (CardView) viewRoot.findViewById(R.id.card_view_restaurant);
        cardViewCafe = (CardView) viewRoot.findViewById(R.id.card_view_cafe);
        cardViewFastFood = (CardView) viewRoot.findViewById(R.id.card_view_fast_food);
        cardViewCofeeShop = (CardView) viewRoot.findViewById(R.id.card_view_cofee_shop);

        iv1 = (ImageView) viewRoot.findViewById(R.id.imgview1);
        iv2 = (ImageView) viewRoot.findViewById(R.id.imgview2);
        iv3 = (ImageView) viewRoot.findViewById(R.id.imgview3);
        iv4 = (ImageView) viewRoot.findViewById(R.id.imgview4);

        Glide.with(getActivity().getApplicationContext()).load(R.drawable.img1).into(iv1);
        Glide.with(getActivity().getApplicationContext()).load(R.drawable.img2).into(iv2);
        Glide.with(getActivity().getApplicationContext()).load(R.drawable.img3).into(iv3);
        Glide.with(getActivity().getApplicationContext()).load(R.drawable.img4).into(iv4);
        cardViewRestaurat.setOnClickListener(this);
        cardViewCafe.setOnClickListener(this);
        cardViewFastFood.setOnClickListener(this);
        cardViewCofeeShop.setOnClickListener(this);
        return viewRoot;
    }

    @Override
    public void onClick(View view) {
        Fragment f = null;
        switch (view.getId()){
            case R.id.card_view_restaurant:
                f = FragmentTerdekat.newInstance(getActivity().getApplicationContext(),"Restaurant");
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
                break;
            case R.id.card_view_cafe:
                f = FragmentTerdekat.newInstance(getActivity().getApplicationContext(),"Cafe");
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
                break;
            case R.id.card_view_fast_food:
                f = FragmentTerdekat.newInstance(getActivity().getApplicationContext(),"FastFood");
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
                break;
            case R.id.card_view_cofee_shop:
                f = FragmentTerdekat.newInstance(getActivity().getApplicationContext(),"CofeeShop");
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
                break;
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}
