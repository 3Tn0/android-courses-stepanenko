package com.example.lab3;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.core.util.ObjectsCompat;

public class Student implements Parcelable, Comparable<Student> {

    @NonNull
    public String firstName;
    @NonNull
    public String secondName;
    @NonNull
    public String lastName;
    public String groupName;
    public String isStudent;

    public Student(@NonNull String firstName, @NonNull String secondName, @NonNull String lastName, String groupName, String isStudent) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.secondName = secondName;
        this.groupName = groupName;
        this.isStudent = isStudent;
    }

    protected Student(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        secondName = in.readString();
        groupName = in.readString();
        isStudent = in.readString();
    }

    public static final Parcelable.Creator<Student> CREATOR = new Parcelable.Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(secondName);
        dest.writeString(groupName);
        dest.writeString(isStudent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;

        return lastName.equals(student.lastName) &&
                firstName.equals(student.firstName) &&
                secondName.equals(student.secondName)&&
                isStudent.equals(student.isStudent);
    }

    public int compareTo(Student s) {
        return groupName.compareTo(s.groupName);
    }

    @Override
    public int hashCode() {
        if(lastName==firstName&&lastName==secondName&&lastName==groupName)
            return ObjectsCompat.hash(lastName, firstName, secondName, groupName);
        return ObjectsCompat.hash(lastName, firstName, secondName);
    }
}
