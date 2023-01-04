package com.example.ecocount_public;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


public class SliderAdapter extends PagerAdapter {
    //Class is for the Tutorial Section of the App

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }

    public int[] slideImages = {R.drawable.earth, R.drawable.orange_car, R.drawable.yellow_satellite};

    public String[] slideHeadings = {"Why use this app?", "How to use the app", "How does it work?"};

    public String[] slideDescriptions = {"Carbon emissions from transportation accounts for more than 25% of the greenhouse gas. " +
            "Using this app will help you to keep track of your daily transportation emissions & reduce your carbon footprint! ",
            "Click on the select car drop down menu on the top of your screen, and choose the closest model of your car. " +
            "Every car has a different CO2 emission rate (CO2 released per kilometer)",
            "Using the Google Maps, the app tracks your speed using satellite Data. " +
            "Note that this app may not work indoors, since satellite data will be limited"};

    @Override
    public int getCount() {
        return slideHeadings.length; //total number of slides
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView)view.findViewById(R.id.slideImage);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription = (TextView)view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slideImages[position]); //Array
        slideHeading.setText(slideHeadings[position]); //Array
        slideDescription.setText(slideDescriptions[position]); //Array

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //Removes everything once we reach the end of the tutorial
        container.removeView((RelativeLayout)object);
    }
}
