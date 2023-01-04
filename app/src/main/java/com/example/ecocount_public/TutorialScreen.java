package com.example.ecocount_public;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/* If Virtual Device isn't running
 *Tools -> AVD manager -> right-click on the emulator you are using -> Stop */

public class
TutorialScreen extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotsLayout;
    private TextView[] mDots;

    private Button mNextButton;
    private Button mPrevButton;
    private Button mFinishButton;

    private int mCurrentSlide;

    private String[] carArray;
    private Boolean previousSelected=false;

    ArrayList<String> numberList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Main Method of the App
        super.onCreate(savedInstanceState);

        //Checks to see if the app has been run before, if so show the Help Screen
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true); //check if we started this app before CHANGE WHEN REAL
        if (firstStart) {
            Log.i("First time running app ", "Success");
            //Sets the Content View to the corresponding XML File
            setContentView(R.layout.activity_tutorial);
            showHelp();
        }else{
            //Navigates to the MainActivity class
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);
        }
        parseCSVFile();
    }

    private void parseCSVFile(){
        //Reads the CSV file stored in the res/raw folder
        InputStream is = this.getResources().openRawResource(R.raw.first2020);
        String line;
        String csvSplitBy = ",";

        try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            while ((line = br.readLine()) != null) {

                //splits each line into separate string according to the commas,
                //placing each component into their respective carArray slot
                //carArray temporarily holds all of the car information in a SINGLE LINE
                //ends with  2020 Volvo XC90 T6 AWD (Last car in the CSV File)
                carArray = line.split(csvSplitBy);

                SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.WalkthroughDemo", Context.MODE_PRIVATE);
                String storedCar = sharedPreferences.getString("Car Selected", "Please Select a Car");

                //Checks to see if we have selected a car before

                if(storedCar.equals("Please Select a Car")){
                    Log.i("Nothing was selected", "OKAY GOOD");
                }else{
                    if(!previousSelected) {
                        //Adds the previously selected car in savedPreferences to the number list
                        numberList.add(storedCar);
                        previousSelected = true;
                    }
                }
                /* What each index is referring to :
                 * index 0: YEAR
                 * index 1: MAKE
                 * index 2: MODEL
                 * index 3: VEHICLE CLASS
                 * index 4: ENGINE SIZE
                 * index 5: CO2 EMISSIONS (g/km) */
                numberList.add(carArray[0] + " " + carArray[1] + " " + carArray[2] + " " + carArray[3] + ", Engine Size: " + carArray[4] + ", Emissions per km: " + carArray[12]);
            }
            setFinalCarList(numberList);
        } catch (IOException e) {
            Log.i("exception", e.toString());
        }
    }

    public void setFinalCarList(ArrayList<String> numList){
        numberList=numList;
        MainActivity.setNumberList(numberList); //Sends the complete ArrayList of Car details to MainActivity class
    }

    private void showHelp(){
        //Shows the Tutorial for using the app, showHelp(), addDotsIndicator(), etc.

        mSlideViewPager = findViewById(R.id.slideViewPager); //(ViewPager)
        mDotsLayout = findViewById(R.id.dotsLayout); //(LinearLayout)

        mNextButton = findViewById(R.id.nextButton); //(Button)
        mPrevButton = findViewById(R.id.prevButton); //(Button)
        mFinishButton = findViewById(R.id.finishButton); //(Button)

        SliderAdapter sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrentSlide+1); //Go forward one page
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrentSlide-1); //Go back one page
            }
        });

        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Clicking the "Finish" Button brings the user to the Core Part of the app
                Intent sideActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(sideActivity);
            }
        });

        //Updates the SharedPreferences so that the Tutorial Screen doesn't pop up ever again
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    public void addDotsIndicator(int position){
        //Dots on the middle bottom of the Tutorial Screen

        mDots = new TextView[3];
        mDotsLayout.removeAllViews(); //clear the dots

        for(int i=0; i<mDots.length; i++){ //the # of dots is equal to the number of slides
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite, getResources().newTheme()));
            mDotsLayout.addView(mDots[i]);

        }

        if(mDots.length>0){ //If there's actually a slide there
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite, getResources().newTheme()));  //Set the page dot to white :D
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            mCurrentSlide = position;

            if(position==0){ //First Page
                mNextButton.setEnabled(true);
                mPrevButton.setEnabled(false);
                mFinishButton.setEnabled(false);

                mNextButton.setVisibility(View.VISIBLE);
                mPrevButton.setVisibility(View.INVISIBLE);
                mFinishButton.setVisibility(View.INVISIBLE);

                mNextButton.setText("Next");
                mPrevButton.setText("");


            }else if(position==mDots.length-1){ //Last page
                mNextButton.setEnabled(false);
                mPrevButton.setEnabled(true);
                mFinishButton.setEnabled(true);

                mNextButton.setVisibility(View.INVISIBLE);
                mPrevButton.setVisibility(View.VISIBLE);
                mFinishButton.setVisibility(View.VISIBLE);

                mNextButton.setText("");
                mPrevButton.setText("Back");
                mFinishButton.setText("Finish");


            }else{
                mNextButton.setEnabled(true);
                mPrevButton.setEnabled(true);
                mFinishButton.setEnabled(false);

                mNextButton.setVisibility(View.VISIBLE);
                mPrevButton.setVisibility(View.VISIBLE);
                mFinishButton.setVisibility(View.INVISIBLE);

                mNextButton.setText("Next");
                mPrevButton.setText("Back");

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onResume(){
        super.onResume();

    }

}