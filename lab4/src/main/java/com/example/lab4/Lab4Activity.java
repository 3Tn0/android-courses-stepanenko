package com.example.lab4;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4.db.Group;
import com.example.lab4.db.GroupDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.lab4.adapter.StudentsAdapter;
import com.example.lab4.add.AddStudentActivity;
import com.example.lab4.db.Lab4Database;
import com.example.lab4.db.Student;
import com.example.lab4.db.StudentDao;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Взаимодействие с файловой системой, SQLite</b>
 * <p>
 * В лабораторной работе вместо сохранения студентов в оперативную память будем сохранять их в
 * базу данных SQLite, которая интегрирована в ОС Android. Для более удобного взаимодействия с
 * ней будем использовать ORM библиотеку Room (подключение см. в build.gradle).
 * </p>
 * <p>
 * В {@link AddStudentActivity} введенные поля теперь сохраняются в
 * {@link android.content.SharedPreferences} - удобный способ для хранения небольших данных в
 * файловой системе, а также напрямую поработаем с {@link java.io.File} для работы с фото,
 * полученного с камеры.
 * </p>
 */
public class Lab4Activity extends AppCompatActivity {

    private static final int REQUEST_STUDENT_ADD = 1;

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab4Activity.class);
    }

    private StudentDao studentDao;
    private GroupDao groupDao;

    private  TempScrollPref scrollPref;

    private ArrayList<Object> recordsList = new ArrayList<>();

    private RecyclerView list;
    LinearLayoutManager layoutManager;
    private FloatingActionButton fab;

    private StudentsAdapter studentsAdapter;

    public void showAddGroupAlert() {
        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.lab4_alert_title);
        alert.setMessage(R.string.lab4_alert_message);

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();

                if (!(value.length() > 0))
                {
                    Toast.makeText(alert.getContext(), R.string.lab4_error_empty_group_fields, Toast.LENGTH_LONG).show();
                }
                else{
                    Group group = new Group(value);
                    if (groupDao.count(group.name) > 0) {
                        Toast.makeText(alert.getContext(), R.string.lab4_error_group_already_exists, Toast.LENGTH_LONG).show();
                    }
                    else {
                        groupDao.insert(group);
                        recordsList.add(group);
                        studentsAdapter.setStudents(recordsList);
                        //studentsAdapter.notifyItemInserted(recordsList.size());
                        //studentsAdapter.notifyDataSetChanged();
                        list.scrollToPosition(studentsAdapter.getItemCount() - 1);
                    }
                }
            }
        });

        alert.setNegativeButton(R.string.lab4_alert_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    protected void cashesToList(List<Group> gc, List<Student> cc){
        for (Group group:gc) {
            recordsList.add(group);

            for (Student student: cc){
                if (student.groupId == group.id)
                    recordsList.add(student);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.lab4_title, getClass().getSimpleName()));

        scrollPref = new TempScrollPref(this);


        /*
        Получаем объект для выполнения запросов к БД. См. Lab4Database.
         */
        studentDao = Lab4Database.getInstance(this).studentDao();
        groupDao = Lab4Database.getInstance(this).groupDao();

        setContentView(R.layout.lab4_activity);
        list = findViewById(android.R.id.list);
        fab = findViewById(R.id.fab);

        layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);

        // Точно такой же список, как и в lab3, но с добавленным выводом фото
        list.setAdapter(studentsAdapter = new StudentsAdapter(this));
        cashesToList(groupDao.getAll(), studentDao.getAll());
        studentsAdapter.setStudents(recordsList);

        layoutManager.scrollToPosition(scrollPref.getPos());

//        list.getLayoutManager().scrollToPosition(9);
//        list.getLayoutManager().getPosition();
//        layoutManager.scrollToPosition(9);
//        layoutManager.findFirstCompletelyVisibleItemPosition();

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

    public int findIndex(List<Object> students, Student student){
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
                studentDao.update(studentBefore);
                //studentDao.insert(student);
                //studentsCache.editStudent(studentBefore, student);
            }
            else{
                studentDao.insert(student);
            }

            recordsList.clear();
            cashesToList(groupDao.getAll(), studentDao.getAll());
            studentsAdapter.setStudents(recordsList);
            studentsAdapter.notifyDataSetChanged();
            list.scrollToPosition(studentsAdapter.getItemCount() - 1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scrollPref.set(layoutManager.findFirstCompletelyVisibleItemPosition());
    }
}
