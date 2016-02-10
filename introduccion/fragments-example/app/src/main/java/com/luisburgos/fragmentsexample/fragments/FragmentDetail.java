package com.luisburgos.fragmentsexample.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.luisburgos.fragmentsexample.R;
import com.luisburgos.fragmentsexample.data.Injection;
import com.luisburgos.fragmentsexample.data.Student;
import com.luisburgos.fragmentsexample.presenters.StudentDetailPresenter;
import com.luisburgos.fragmentsexample.presenters.StudentDetailContract;
import com.luisburgos.fragmentsexample.presenters.StudentListContract;

public class FragmentDetail extends Fragment implements StudentDetailContract.View {

    private StudentDetailContract.UserActionsListener mActionsListener;
    private String studentEnrollmentID;

    private EditText enrollmentID;
    private EditText name;
    private EditText lastName;
    private EditText secondLastName;
    private EditText birthDate;
    private EditText bachelorsDegree;

    private Button btnEdit;
    private Button btnSaveEdition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionsListener = new StudentDetailPresenter(
                this,
                Injection.provideStudentsDataSource(getActivity().getApplicationContext())
        );
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        enrollmentID = (EditText) getView().findViewById(R.id.enrollmentID);
        name = (EditText) getView().findViewById(R.id.name);
        lastName = (EditText) getView().findViewById(R.id.lastName);
        secondLastName = (EditText) getView().findViewById(R.id.secondLastName);
        birthDate = (EditText) getView().findViewById(R.id.birthDate);
        bachelorsDegree = (EditText) getView().findViewById(R.id.bachelorsDegree);


        btnEdit = (Button) getView().findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.editStudent();
            }
        });

        btnSaveEdition = (Button) getView().findViewById(R.id.btn_save_edition);
        btnSaveEdition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.saveStudentChanges(
                        enrollmentID.getText().toString().trim(),
                        name.getText().toString().trim(),
                        lastName.getText().toString().trim(),
                        secondLastName.getText().toString().trim(),
                        birthDate.getText().toString().trim(),
                        bachelorsDegree.getText().toString().trim()
                );
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        return view;
    }

    @Override
    public void enableInformationEdition(boolean editionEnable) {
        enrollmentID.setEnabled(false);
        name.setEnabled(editionEnable);
        lastName.setEnabled(editionEnable);
        secondLastName.setEnabled(editionEnable);
        birthDate.setEnabled(editionEnable);
        bachelorsDegree.setEnabled(editionEnable);
        btnSaveEdition.setEnabled(editionEnable);
    }

    @Override
    public void showEnrollmentID(String enrollmentID) {
        this.enrollmentID.setVisibility(View.VISIBLE);
        this.enrollmentID.setText(enrollmentID);
    }

    @Override
    public void showName(String name) {
        this.name.setVisibility(View.VISIBLE);
        this.name.setText(name);
    }

    @Override
    public void hideName() {
        name.setVisibility(View.GONE);
    }


    @Override
    public void showLastName(String lastName) {
        this.lastName.setVisibility(View.VISIBLE);
        this.lastName.setText(lastName);
    }

    @Override
    public void hideEnrollmentID() {
        enrollmentID.setVisibility(View.GONE);
    }

    @Override
    public void hideLastName() {
        lastName.setVisibility(View.GONE);
    }

    @Override
    public void showSecondLastName(String secondLastName) {
        this.secondLastName.setVisibility(View.VISIBLE);
        this.secondLastName.setText(secondLastName);
    }

    @Override
    public void hideSecondLastName() {
        secondLastName.setVisibility(View.GONE);
    }

    @Override
    public void showBirthDate(String birthDate) {
        this.birthDate.setVisibility(View.VISIBLE);
        this.birthDate.setText(birthDate);
    }

    @Override
    public void hideBirthDate() {
        birthDate.setVisibility(View.GONE);
    }

    @Override
    public void showBachelorsDegree(String bachelorsDegree) {
        this.bachelorsDegree.setVisibility(View.VISIBLE);
        this.bachelorsDegree.setText(bachelorsDegree);
    }

    @Override
    public void hideBachelorsDegree() {
        bachelorsDegree.setVisibility(View.GONE);
    }

    @Override
    public void showStudentsList() {
        StudentListContract.View fragmentList = (FragmentsList) getFragmentManager().findFragmentById(R.id.listFragment);
        fragmentList.getActionsListener().loadStudents();
    }

    @Override
    public void showMissingStudent() {
        String noData = getString(R.string.no_data);
        enrollmentID.setText(noData);
        enrollmentID.setEnabled(false);
        name.setText(noData);
        name.setEnabled(false);
        lastName.setText(noData);
        lastName.setEnabled(false);
    }

    @Override
    public void showEmptyStudentError() {
        Snackbar.make(name, getString(R.string.empty_student_message), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void loseFocus() {
        //InputMethodManager imm = (InputMethodManager) getView().getContext()
          //          .getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        this.getView().clearFocus();
    }

    @Override
    public StudentDetailContract.UserActionsListener getActionsListener() {
        return mActionsListener;
    }

}
