package com.luisburgos.fragmentsexample.fragments;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.luisburgos.fragmentsexample.R;
import com.luisburgos.fragmentsexample.data.Injection;
import com.luisburgos.fragmentsexample.data.Student;
import com.luisburgos.fragmentsexample.presenters.StudentDetailContract;
import com.luisburgos.fragmentsexample.presenters.StudentListContract;
import com.luisburgos.fragmentsexample.presenters.StudentListPresenter;

import java.util.List;

public class FragmentsList extends ListFragment implements StudentListContract.View {

    private StudentListContract.UserActionsListener mActionsListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActionsListener = new StudentListPresenter(
                this,
                Injection.provideStudentsDataSource(getActivity().getApplicationContext())
        );
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Student student = (Student) getListAdapter().getItem(position);
        mActionsListener.openStudentDetails(student);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActionsListener.loadStudents();
    }

    @Override
    public void showStudents(List<Student> students) {
        ArrayAdapter<Student> adapter = new ArrayAdapter<Student>(
                getActivity(), android.R.layout.simple_list_item_1, students
        );
        setListAdapter(adapter);
    }

    @Override
    public void showStudentDetailUi(Student student) {
        StudentDetailContract.View fragmentDetail = (FragmentDetail) getFragmentManager().findFragmentById(R.id.detailFragment);
        if(fragmentDetail != null && ((FragmentDetail) fragmentDetail).isInLayout()){
            fragmentDetail.getActionsListener().openStudent(student.getId());
        }else{
            Intent intent = new Intent(getActivity().getApplicationContext(), FragmentDetail.class);
            intent.putExtra("id", student.getId());
            intent.putExtra("name", student.getName());
            intent.putExtra("lastName", student.getLastName());
            intent.putExtra("secondLastName", student.getSecondLastName());
            intent.putExtra("birthDate", student.getBirthDate());
            intent.putExtra("degree", student.getBachelorsDegree());
            startActivity(intent);
        }
    }

    @Override
    public StudentListContract.UserActionsListener getActionsListener() {
        return mActionsListener;
    }
}
