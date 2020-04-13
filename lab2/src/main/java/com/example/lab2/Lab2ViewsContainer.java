package com.example.lab2;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Px;

import java.util.ArrayList;

public class Lab2ViewsContainer extends LinearLayout {
    double highestRating = 0;
    private ArrayList<Lab2RatingLine> ratingLines = new ArrayList<Lab2RatingLine>();

    public Lab2ViewsContainer(Context context) {
        this(context, null);
    }

    public Lab2ViewsContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Lab2ViewsContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Lab2ViewsContainer, defStyleAttr, 0);

        double defaultRating = a.getInt(R.styleable.Lab2ViewsContainer_lab2_defaultRating, 0);
        String defaultName = a.getString(R.styleable.Lab2ViewsContainer_lab2_defaultName);

        a.recycle();

        addRatingLine(defaultName, defaultRating);
        setRatingLines(ratingLines);
    }

    public void addRatingLine(String name, double rating){

        if (rating < 0 || rating > 10){
            Toast.makeText(this.getContext(), "Введите значение рейтинга от 0 до 10", Toast.LENGTH_LONG).show();
        }
        else{
            Lab2RatingLine lab2RatingLine = new Lab2RatingLine(name, rating);
            ratingLines.add(lab2RatingLine);
            setRatingLines(ratingLines);
        }
    }

    public void  showRatingLine(String name, double rating){
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));

        TextView textView = new TextView(getContext());
        textView.setText(name);
        textView.setLayoutParams(new LayoutParams(dpToPx(130), ViewGroup.LayoutParams.WRAP_CONTENT, 2));

        TextView textView1 = new TextView(getContext());
        textView1.setText(String.format("%.1f", rating));
        textView1.setLayoutParams(new LayoutParams(dpToPx(40), ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        relativeLayout.setLayoutParams(new LayoutParams(dpToPx(200), ViewGroup.LayoutParams.MATCH_PARENT, 4));
        relativeLayout.setPadding(dpToPx(6), dpToPx(6), dpToPx(6), dpToPx(6));

        View ratingLine = new View(getContext());
        ratingLine.setLayoutParams(new LayoutParams(dpToPx(200 * rating / 10), dpToPx(10)));
        ratingLine.setBackgroundColor(rating == highestRating ? 0xFFFF0000: 0xFF000000);;

        relativeLayout.addView(ratingLine);

        linearLayout.addView(textView);
        linearLayout.addView(relativeLayout);
        linearLayout.addView(textView1);

        addView(linearLayout);
    }

    public void setRatingLines(ArrayList<Lab2RatingLine> ratingLines){
        this.ratingLines = ratingLines;

        removeAllViews();

        for (Lab2RatingLine ratingLine : ratingLines) {
            if (ratingLine.getRating() > highestRating)
            {
                highestRating = ratingLine.getRating();
            }
        }

        for (Lab2RatingLine ratingLine : ratingLines) {
            showRatingLine(ratingLine.getName(),ratingLine.getRating());
        }
    }

    public ArrayList<Lab2RatingLine> getRatingLines() {return ratingLines;}

    /**
     * Метод трансформирует указанные dp в пиксели, используя density экрана.
     */
    @Px
    public int dpToPx(double dp) {
        if (dp == 0) {
            return 0;
        }
        double density = getResources().getDisplayMetrics().density;
        return (int) Math.ceil(density * dp);
    }
}
