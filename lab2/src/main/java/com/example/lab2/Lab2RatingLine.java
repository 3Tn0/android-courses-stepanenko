package com.example.lab2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

public class Lab2RatingLine implements Parcelable {
    private String name;
    private double rating;

    Lab2RatingLine(String name, double rating){
        this.name = name;
        this.rating = rating;
    }

    private Lab2RatingLine(Parcel in) {
        name = in.readString();
        rating = in.readDouble();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeDouble(rating);
    }

    public static final Parcelable.Creator<Lab2RatingLine> CREATOR = new Parcelable.Creator<Lab2RatingLine>() {
        public Lab2RatingLine createFromParcel(Parcel in) {
            return new Lab2RatingLine(in);
        }

        public Lab2RatingLine[] newArray(int size) {
            return new Lab2RatingLine[size];
        }
    };

    String getName() {
        return name;
    }

    Double getRating() {
        return rating;
    }

    static Comparator<Lab2RatingLine> rateComparator = new Comparator<Lab2RatingLine>() {
        @Override
        public int compare(Lab2RatingLine r1, Lab2RatingLine r2) {
            return (r2.getRating().compareTo(r1.getRating()));
        }
    };
}
