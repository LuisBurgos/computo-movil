package com.luisburgos.studentsapp.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.luisburgos.studentsapp.R;
import com.luisburgos.studentsapp.domain.Student;

import java.util.List;
import com.luisburgos.studentsapp.utils.ScreenUtils;
import com.luisburgos.studentsapp.view.listeners.StudentItemListener;

/**
 * Created by luisburgos on 2/02/16.
 */
public class StudentsAdapter extends RecyclerView.Adapter<StudentsViewHolder> {

    private Context mContext;
    private int lastAnimatedPosition = -1;
    private List<Student> mStudents;
    private StudentItemListener mItemListener;

    public StudentsAdapter(Context context, List<Student> students, StudentItemListener itemListener) {
        setList(students);
        mContext = context;
        mItemListener = itemListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public StudentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_student, parent, false);
        StudentsViewHolder vh = new StudentsViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(StudentsViewHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);

        final Student student = mStudents.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.onStudentClick(student);
            }
        });

        holder.textViewId.setText(student.getId());
        holder.textViewName.setText(student.getName());
    }

    public void replaceData(List<Student> students) {
        setList(students);
        notifyDataSetChanged();
    }

    private void setList(List<Student> students) {
        mStudents = students;
    }

    @Override
    public int getItemCount() {
        return mStudents.size();
    }

    public Student getItem(int position) {
        return mStudents.get(position);
    }

    private void runEnterAnimation(View view, int position) {
        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(ScreenUtils.getScreenHeight(mContext));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }
}
