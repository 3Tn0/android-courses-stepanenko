package com.example.lab4.add;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.example.lab4.Const;
import com.example.lab4.Lab4Activity;
import com.example.lab4.R;
import com.example.lab4.db.Group;
import com.example.lab4.db.GroupDao;
import com.example.lab4.db.Lab4Database;
import com.example.lab4.db.Student;
import com.example.lab4.db.StudentDao;

/**
 * Аналогичный экран ввода информации о студенте, как и в lab3. Но теперь введенная информация
 * сохраняется в {@link android.content.SharedPreferences} (см {@link TempStudentPref}), что
 * позволяет восстановить введенную информацию после ухода и возвращения на экран. Также теперь
 * можно добавить фотографию через приложение камеры. Для работы с картинками см
 * {@link BitmapProcessor}.
 */
public class AddStudentActivity extends AppCompatActivity {

    private static final String EXTRA_STUDENT = "student";
    private static final String STUDENT_BEFORE = "studentBefore";
    private static final String STUDENT_INDEX = "index";
    private int studentIndex = -1;

    private Student studentBefore;

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, AddStudentActivity.class);
    }

    public static Student getResultStudent(@NonNull Intent intent) {
        return intent.getParcelableExtra(EXTRA_STUDENT);
    }

    public static Student getStudentBefore(@NonNull Intent intent) {
        return intent.getParcelableExtra(STUDENT_BEFORE);
    }

    public static int getStudentindex(@NonNull Intent intent) {
        return intent.getIntExtra(STUDENT_INDEX, -1);
    }

    private StudentDao studentDao;
    private GroupDao groupDao;

    private TempStudentPref studentPref;

    private EditText firstName;
    private EditText secondName;
    private EditText lastName;
    private Spinner groupsSpinner;

    private boolean skipSaveToPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab4_activity_add_student);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        studentPref = new TempStudentPref(this);

        studentDao = Lab4Database.getInstance(this).studentDao();
        groupDao = Lab4Database.getInstance(this).groupDao();

        firstName = findViewById(R.id.first_name);
        secondName = findViewById(R.id.second_name);
        lastName = findViewById(R.id.last_name);
        groupsSpinner = findViewById(R.id.spinner);
        ///****
        firstName.setText(studentPref.getFirstName());
        secondName.setText(studentPref.getSecondName());
        lastName.setText(studentPref.getLastName());
        ///****

        ArrayList<Group> arrayList = new ArrayList<>();

        for (Group group : groupDao.getAll()) {
            arrayList.add(group);
        }

        if (arrayList.size() < 1){
            Toast.makeText(this, R.string.lab4_error_no_groups, Toast.LENGTH_LONG).show();
            finish();
        }

        ArrayAdapter<Group> arrayAdapter = new ArrayAdapter<Group>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupsSpinner.setAdapter(arrayAdapter);

        Bundle b = getIntent().getExtras();
        if(b != null){
            firstName.setText(b.getString("firstName"));
            secondName.setText(b.getString("secondName"));
            lastName.setText(b.getString("lastName"));

            int groupId = b.getInt("groupId");
            int index = 0;
            for (Group group:arrayList) {
                if (group.id == groupId)
                    break;
                index++;
            }

            groupsSpinner.setSelection(index);
            studentIndex = b.getInt("studentIndex");

            studentBefore = b.getParcelable("studentToUpdate");
            //studentBefore = new Student(b.getString("firstName"),b.getString("secondName"),b.getString("lastName"), b.getInt("groupId"));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!skipSaveToPrefs) {
            studentPref.set(
                    firstName.getText().toString(),
                    secondName.getText().toString(),
                    lastName.getText().toString()
            );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lab4_add_student, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.action_save) {
            saveStudent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void saveStudent() {
        Group group = (Group) groupsSpinner.getSelectedItem();

        Student student = new Student(
                firstName.getText().toString(),
                secondName.getText().toString(),
                lastName.getText().toString(),
                group.id
        );

        Bundle b = getIntent().getExtras();
        if(b != null) {
            studentBefore.groupId = group.id;
            studentBefore.firstName = firstName.getText().toString();
            studentBefore.lastName = lastName.getText().toString();
            studentBefore.secondName = secondName.getText().toString();
        }

        // Проверяем, что все поля были указаны
        if (TextUtils.isEmpty(student.firstName) ||
                TextUtils.isEmpty(student.secondName) ||
                TextUtils.isEmpty(student.lastName)) {
            // Класс Toast позволяет показать системное уведомление поверх всего UI
            Toast.makeText(this, R.string.lab4_error_empty_fields, Toast.LENGTH_LONG).show();
            return;
        }

        if (studentDao.count(student.firstName, student.secondName, student.lastName) > 0 && studentIndex == -1) {
            Toast.makeText(
                    this,
                    R.string.lab4_error_already_exists,
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        skipSaveToPrefs = true;

        studentPref.clear();

        Intent data = new Intent();
        data.putExtra(EXTRA_STUDENT, student);
        data.putExtra(STUDENT_BEFORE, studentBefore);
        data.putExtra(STUDENT_INDEX, studentIndex);
        setResult(RESULT_OK, data);
        finish();
    }
}
