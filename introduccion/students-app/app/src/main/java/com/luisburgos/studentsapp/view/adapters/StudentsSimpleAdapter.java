package com.luisburgos.studentsapp.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.luisburgos.studentsapp.R;
import com.luisburgos.studentsapp.domain.Student;
import com.luisburgos.studentsapp.utils.ScreenUtils;
import com.luisburgos.studentsapp.view.listeners.StudentItemListener;

import java.util.List;

/**
 * Created by luisburgos on 16/02/16.
 */
public class StudentsSimpleAdapter extends RecyclerView.Adapter<StudentsSimpleViewHolder> {

    private Context mContext;
    private int lastAnimatedPosition = -1;
    private List<Student> mStudents;
    private StudentItemListener mItemListener;

    public StudentsSimpleAdapter(Context context, List<Student> students, StudentItemListener itemListener) {
        setList(students);
        mContext = context;
        mItemListener = itemListener;
    }

    @Override
    public StudentsSimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_simple_student, parent, false);
        StudentsSimpleViewHolder vh = new StudentsSimpleViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(StudentsSimpleViewHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        final Student student = mStudents.get(position);
        holder.detailEnrollmentID.setText(student.getEnrollmentID());
        holder.detailEnrollmentID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.onStudentClick(student);
            }
        });
    }

    public void replaceData(List<Student> students) {
        setList(students);
        notifyDataSetChanged();
    }

    public void addItem(Student student) {
        mStudents.add(student);
        notifyItemInserted(mStudents.size());
    }

    public void removeItem(int position) {
        mStudents.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mStudents.size());
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