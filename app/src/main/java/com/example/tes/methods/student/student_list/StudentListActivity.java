package com.example.tes.methods.student.student_list;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tes.R;
import com.example.tes.database.QueryContract;
import com.example.tes.database.QueryResponse;
import com.example.tes.database.StudentQueryImplementation;
import com.example.tes.database.TableRowCountQueryImplementation;
import com.example.tes.methods.student.create_student.StudentCreate;
import com.example.tes.methods.student.StudentCrudListener;
import com.example.tes.methods.subject.subject_list.SubjectListActivity;
import com.example.tes.model.Student;
import com.example.tes.model.TableRowCount;
import com.example.tes.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity implements StudentCrudListener {

    private RecyclerView recyclerView;
    private TextView noDataFoundTextView;
    private FloatingActionButton fab;
    private TextView studentCountTextView;
    private TextView subjectCountTextView;
    private TextView takenSubjectCountTextView;

    private List<Student> studentList = new ArrayList<>();
    private StudentListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialization();

        adapter = new StudentListAdapter(this, studentList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        showStudentList();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudentCreate studentCreate = StudentCreate.newInstance("Create Student", StudentListActivity.this);
                studentCreate.show(getSupportFragmentManager(), Constants.CREATE_STUDENT);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showTableRowCount();
    }

    @Override
    public void onStudentListUpdate(boolean isUpdated) {
        if (isUpdated) {
            showStudentList();
            showTableRowCount();
        }
    }

    private void showStudentList() {
        QueryContract.StudentQuery query = new StudentQueryImplementation();
        query.readAllStudent(new QueryResponse<List<Student>>() {
            @Override
            public void onSuccess(List<Student> data) {
                recyclerView.setVisibility(View.VISIBLE);
                noDataFoundTextView.setVisibility(View.GONE);

                studentList.clear();
                studentList.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String message) {
                recyclerView.setVisibility(View.GONE);
                noDataFoundTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showTableRowCount() {
        QueryContract.TableRowCountQuery query = new TableRowCountQueryImplementation();
        query.getTableRowCount(new QueryResponse<TableRowCount>() {
            @Override
            public void onSuccess(TableRowCount data) {
                studentCountTextView.setText(getString(R.string.student_footer, data.getStudentRow()));
                subjectCountTextView.setText(getString(R.string.subject_footer, data.getSubjectRow()));
                takenSubjectCountTextView.setText(getString(R.string.taken_subject_footer, data.getTakenSubjectRow()));
            }

            @Override
            public void onFailure(String message) {
                studentCountTextView.setText(getString(R.string.table_row_count_failed));
                subjectCountTextView.setText(message);
                takenSubjectCountTextView.setText("");
            }
        });
    }

    private void initialization() {
        recyclerView = findViewById(R.id.recyclerView);
        noDataFoundTextView = findViewById(R.id.noDataFoundTextView);
        fab = findViewById(R.id.fab);

        studentCountTextView = findViewById(R.id.studentCount);
        subjectCountTextView = findViewById(R.id.subjectCount);
        takenSubjectCountTextView = findViewById(R.id.takenSubjectCount);


        ImageButton toolbarButton = findViewById(R.id.toolbar_button);
        toolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentListActivity.this, SubjectListActivity.class);
                startActivity(intent);
            }
        });
    }
}
