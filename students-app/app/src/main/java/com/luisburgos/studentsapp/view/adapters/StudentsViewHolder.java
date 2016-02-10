package com.luisburgos.studentsapp.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luisburgos.studentsapp.R;

/**
 * Created by luisburgos on 2/02/16.
 */
public class StudentsViewHolder extends RecyclerView.ViewHolder {

    TextView textViewId;
    TextView textViewName;
    ImageView imageViewPhoto;

    public StudentsViewHolder(View view) {
        super(view);
        textViewId = (TextView ) view.findViewById(R.id.detailId);
        textViewName = (TextView) view.findViewById(R.id.detailName);
        imageViewPhoto = (ImageView) view.findViewById(R.id.detailPhoto);

    }
}
