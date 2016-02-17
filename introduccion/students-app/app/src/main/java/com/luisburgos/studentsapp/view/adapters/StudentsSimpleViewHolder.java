package com.luisburgos.studentsapp.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.luisburgos.studentsapp.R;

/**
 * Created by luisburgos on 16/02/16.
 */
public class StudentsSimpleViewHolder extends RecyclerView.ViewHolder {

    TextView detailEnrollmentID;

    public StudentsSimpleViewHolder(View view) {
        super(view);
        detailEnrollmentID = (TextView ) view.findViewById(R.id.detailEnrollmentID);

    }
}
