package com.example.lab4.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.example.lab4.add.AddStudentActivity;
import com.example.lab4.db.Group;
import com.example.lab4.db.Student;

public class StudentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_GROUP = 0;
    public static final int TYPE_STUDENT = 1;

    private List<Object> students = new ArrayList<>();

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
        Object object = students.get(position);

        switch (getItemViewType(position)) {
            case TYPE_GROUP:
                Group group = (Group) object;
                NumberHolder numberHolder = (NumberHolder) holder;
                numberHolder.bind(group.name);
                break;
            case TYPE_STUDENT:
                Student student = (Student) object;
                StudentHolder studentHolder = (StudentHolder) holder;
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = AddStudentActivity.newIntent(main);
                        Bundle b = new Bundle();
                        b.putString("firstName", student.firstName);
                        b.putString("lastName", student.lastName);
                        b.putString("secondName", student.secondName);
                        b.putInt("groupId", student.groupId);
                        b.putInt("studentIndex", position);
                        b.putParcelable("studentToUpdate", student);
                        //Toast.makeText(main, String.valueOf(position),Toast.LENGTH_SHORT);
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
        if(students.get(position).getClass().getSimpleName().charAt(0) == 'G')
            return TYPE_GROUP;

        return TYPE_STUDENT;
    }


    public void setStudents(List<Object> students) {
        this.students = students;
    }
}
