package com.example.lab3.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab3.AddStudentActivity;
import com.example.lab3.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_GROUP = 0;
    public static final int TYPE_STUDENT = 1;

    private List<Student> students = new ArrayList<>();

    private Context main;

    public StudentsAdapter(Context main){
        this.main = main;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_GROUP:
                return new NumberHolder(parent);
            case TYPE_STUDENT:
                return new StudentHolder(parent);
        }
        throw new IllegalArgumentException("unknown viewType = " + viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Student student = students.get(position);

        switch (getItemViewType(position)) {
            case TYPE_GROUP:
                NumberHolder numberHolder = (NumberHolder) holder;

                numberHolder.bind(student.groupName);
                break;
            case TYPE_STUDENT:
                StudentHolder studentHolder = (StudentHolder) holder;
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = AddStudentActivity.newIntent(main);
                        Bundle b = new Bundle();
                        b.putString("firstName", student.firstName);
                        b.putString("lastName", student.lastName);
                        b.putString("secondName", student.secondName);
                        b.putString("groupName", student.groupName);
                        b.putInt("studentIndex", position);
                        Toast.makeText(main, String.valueOf(position),Toast.LENGTH_SHORT);
                        intent.putExtras(b);

                        ((Activity) main).startActivityForResult(intent,1);
                    }
                });
                studentHolder.student.setText(
                        student.lastName + " " + student.firstName + " " + student.secondName
                );
                break;
        }
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    @Override
    public int getItemViewType(int position) {
        Student student = students.get(position);
        if(student.isStudent == "0")
            return TYPE_GROUP;

        return TYPE_STUDENT;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
