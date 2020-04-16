package com.example.lab4.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.ObjectsCompat;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Student implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @NonNull
    @ColumnInfo(name = "first_name")
    public String firstName;
    @NonNull
    @ColumnInfo(name = "second_name")
    public String secondName;
    @NonNull
    @ColumnInfo(name = "last_name")
    public String lastName;
    @NonNull
    @ColumnInfo(name = "group_id")
    public int groupId;

    public Student(
            @NonNull String firstName,
            @NonNull String secondName,
            @NonNull String lastName,
            @NonNull int groupId
    ) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.secondName = secondName;
        this.groupId = groupId;
    }

    protected Student(Parcel in) {
        id = in.readInt();
        firstName = in.readString();
        secondName = in.readString();
        lastName = in.readString();
        groupId = in.readInt();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
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
        dest.writeInt(id);
        dest.writeString(firstName);
        dest.writeString(secondName);
        dest.writeString(lastName);
        dest.writeInt(groupId);
    }


    /////////////////////////////////////////////////////////////--------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;

        return lastName.equals(student.lastName) &&
                firstName.equals(student.firstName) &&
                secondName.equals(student.secondName);
    }

    @Override
    public int hashCode() {
        return ObjectsCompat.hash(lastName, firstName, secondName);
    }
}