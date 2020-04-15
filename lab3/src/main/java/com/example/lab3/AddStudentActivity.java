package com.example.lab3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

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

    private final StudentsCache studentsCache = StudentsCache.getInstance();
    private final GroupsCache groupsCache = GroupsCache.getInstance();

    private EditText firstName;
    private EditText secondName;
    private EditText lastName;
    private Spinner groupsSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab3_activity_add_student);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firstName = findViewById(R.id.first_name);
        secondName = findViewById(R.id.second_name);
        lastName = findViewById(R.id.last_name);
        groupsSpinner = findViewById(R.id.spinner);

        ArrayList<String> arrayList = new ArrayList<>();

        for (Group group : groupsCache.getGroups()) {
            arrayList.add(group.name);
        }

        if (arrayList.size() < 1){
            Toast.makeText(this, R.string.lab3_error_no_groups, Toast.LENGTH_LONG).show();
            finish();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupsSpinner.setAdapter(arrayAdapter);


        Bundle b = getIntent().getExtras();
        if(b != null){
            firstName.setText(b.getString("firstName"));
            secondName.setText(b.getString("secondName"));
            lastName.setText(b.getString("lastName"));
            groupsSpinner.setSelection(arrayList.indexOf(b.getString("groupName")));
            studentIndex = b.getInt("studentIndex");

            studentBefore = new Student(b.getString("firstName"),b.getString("secondName"),b.getString("lastName"),b.getString("groupName"),"1");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lab3_add_student, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.action_save) {
            Student student = new Student(
                    firstName.getText().toString(),
                    secondName.getText().toString(),
                    lastName.getText().toString(),
                    groupsSpinner.getSelectedItem().toString(),
                    "1"
            );

            if (TextUtils.isEmpty(student.firstName) ||
                    TextUtils.isEmpty(student.secondName) ||
                    TextUtils.isEmpty(student.lastName)) {
                Toast.makeText(this, R.string.lab3_error_empty_fields, Toast.LENGTH_LONG).show();
                return true;
            }

            if (studentsCache.contains(student) && studentIndex == -1) {
                Toast.makeText(this, R.string.lab3_error_already_exists, Toast.LENGTH_LONG).show();
                return true;
            }

            Intent data = new Intent();
            data.putExtra(EXTRA_STUDENT, student);
            data.putExtra(STUDENT_BEFORE, studentBefore);
            data.putExtra(STUDENT_INDEX, studentIndex);
            setResult(RESULT_OK, data);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
