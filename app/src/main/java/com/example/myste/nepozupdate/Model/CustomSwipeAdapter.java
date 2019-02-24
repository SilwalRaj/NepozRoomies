package com.example.myste.nepozupdate.Model;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.myste.nepozupdate.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomSwipeAdapter extends PagerAdapter {

    private ArrayList<byte[]> images;
    private Context ctxt;
    private LayoutInflater layoutInflater;
    private List<String> imageUrls;


    public CustomSwipeAdapter(Context context, List<String> imageUrls){
        this.ctxt =context;
        this.imageUrls =imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view ==(LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int postion){

        layoutInflater = (LayoutInflater)ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.simple_layout,container,false);
        ImageView imageView = (ImageView)item_view.findViewById(R.id.image_view);
        Picasso.get().load(imageUrls.get(postion)).into(imageView);

        container.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((LinearLayout) object);
    }
}

