package com.example.lab3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab3.adapter.StudentsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

public class Lab3Activity extends AppCompatActivity {

    private static final int REQUEST_STUDENT_ADD = 1;

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab3Activity.class);
    }

    private final StudentsCache studentsCache = StudentsCache.getInstance();
    private final GroupsCache groupsCache = GroupsCache.getInstance();

    private RecyclerView list;
    private FloatingActionButton fab;

    private StudentsAdapter studentsAdapter;

    public void showAddGroupAlert() {
        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.lab3_alert_title);
        alert.setMessage(R.string.lab3_alert_message);

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();

                if (!(value.length() > 0))
                {
                    Toast.makeText(alert.getContext(), R.string.lab3_error_empty_group_fields, Toast.LENGTH_LONG).show();
                }
                else{
                    Group group = new Group(value);
                    if (groupsCache.contains(group)) {
                        Toast.makeText(alert.getContext(), R.string.lab3_error_group_already_exists, Toast.LENGTH_LONG).show();
                    }
                    else {
                        groupsCache.addGroup(group);

                        Student student = new Student(group.name, group.name, group.name, group.name, "0");
                        studentsCache.addStudent(student);

                        List<Student> sortedStudents = studentsCache.getStudents();
                        Collections.sort(sortedStudents);
                        int index = findIndex(sortedStudents, student);

                        studentsAdapter.setStudents(sortedStudents);
                        studentsAdapter.notifyItemRangeInserted(index, 1);
                        list.scrollToPosition(studentsAdapter.getItemCount() - 1);

                    }
                }
            }
        });

        alert.setNegativeButton(R.string.lab3_alert_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.lab3_title, getClass().getSimpleName()));

        setContentView(R.layout.lab3_activity);
        list = findViewById(android.R.id.list);
        fab = findViewById(R.id.fab);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);

        list.setAdapter(studentsAdapter = new StudentsAdapter(this));
        List<Student> sortedStudents = studentsCache.getStudents();
        Collections.sort(sortedStudents);
        studentsAdapter.setStudents(sortedStudents);

        fab.setOnClickListener(
                v -> startActivityForResult(
                        AddStudentActivity.newIntent(this),
                        REQUEST_STUDENT_ADD
                )
        );

        FloatingActionButton fas = findViewById(R.id.floatingActionButton);

        fas.setOnClickListener(
                v -> showAddGroupAlert()
        );

    }

    public int findIndex(List<Student> students, Student student){
        int index;

        for (index = 0; index < students.size(); index++){
            if(students.get(index) == student)
                break;
        }

        Log.d("myTag", String.valueOf(index));
        return index;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_STUDENT_ADD && resultCode == RESULT_OK) {

            Student student = AddStudentActivity.getResultStudent(data);
            Student studentBefore = AddStudentActivity.getStudentBefore(data);
            int studentIndex = AddStudentActivity.getStudentindex(data);

            if (studentIndex != -1){
                studentsCache.editStudent(studentBefore, student);
                List<Student> sortedStudents = studentsCache.getStudents();
                Collections.sort(sortedStudents);
                int index = findIndex(sortedStudents, student);
                studentsAdapter.setStudents(sortedStudents);
                studentsAdapter.notifyDataSetChanged();
            }
            else{
                studentsCache.addStudent(student);
                List<Student> sortedStudents = studentsCache.getStudents();
                Collections.sort(sortedStudents);
                int index = findIndex(sortedStudents, student);
                studentsAdapter.setStudents(sortedStudents);
                studentsAdapter.notifyItemRangeInserted(index, 1);

            }
            list.scrollToPosition(studentsAdapter.getItemCount() - 1);
        }
    }
}
