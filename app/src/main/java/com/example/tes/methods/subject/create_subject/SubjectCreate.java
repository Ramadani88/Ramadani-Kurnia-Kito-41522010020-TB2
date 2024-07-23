package com.example.tes.methods.subject.create_subject;

import android.app.Dialog;
import androidx.fragment.app.DialogFragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tes.R;
import com.example.tes.database.*;
import com.example.tes.methods.subject.SubjectCrudListener;
import com.example.tes.model.Subject;
import com.example.tes.utils.Constants;

public class SubjectCreate extends DialogFragment {

    private EditText subjectNameEditText;
    private EditText subjectCodeEditText;
    private EditText subjectCreditEditText;
    private Button createButton;
    private Button cancelButton;

    private static SubjectCrudListener subjectCrudListener;

    public SubjectCreate() {
    }

    public static SubjectCreate newInstance(String title, SubjectCrudListener listener){
        subjectCrudListener = listener;
        SubjectCreate subjectCreate = new SubjectCreate();
        Bundle args = new Bundle();
        args.putString("title", title);
        subjectCreate.setArguments(args);

        subjectCreate.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return subjectCreate;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subject_create_dialog, container, false);

        subjectNameEditText = view.findViewById(R.id.subjectName);
        subjectCodeEditText = view.findViewById(R.id.subjectCode);
        subjectCreditEditText = view.findViewById(R.id.subjectCreditEditText);
        createButton = view.findViewById(R.id.createButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        String title = getArguments().getString(Constants.TITLE);
        getDialog().setTitle(title);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subjectName = subjectNameEditText.getText().toString();
                int subjectCode = Integer.parseInt(subjectCodeEditText.getText().toString());
                double subjectCredit = Double.parseDouble(subjectCreditEditText.getText().toString());

                final Subject subject = new Subject(-1, subjectName, subjectCode, subjectCredit);

                QueryContract.SubjectQuery query = new SubjectQueryImplementation();
                query.createSubject(subject, new QueryResponse<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        getDialog().dismiss();
                        subjectCrudListener.onSubjectListUpdate(true);
                        Toast.makeText(getContext(), "Subject created successfully", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(String message) {
                        subjectCrudListener.onSubjectListUpdate(false);
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //noinspection ConstantConditions
            dialog.getWindow().setLayout(width, height);
        }
    }
}
