package com.luisburgos.studentsapp.view.listeners;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.luisburgos.studentsapp.R;

/**
 * Created by luisburgos on 18/02/16.
 */
public class StudentItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {

    private Paint paint = new Paint();
    private Activity mActivity;
    private SwipeReactor mListener;

    public StudentItemTouchHelperCallback(Activity activity) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mActivity = activity;
        mListener = (SwipeReactor) activity;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT){
            mListener.onLeftSwiped(position);
        } else {
            mListener.onRightSwiped(position);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        Bitmap icon;
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

            View itemView = viewHolder.itemView;
            float height = (float) itemView.getBottom() - (float) itemView.getTop();
            float width = height / 3;

            if(dX > 0){
                paint.setColor(mActivity.getResources().getColor(R.color.colorEditItem));
                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                c.drawRect(background, paint);
                icon = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.ic_edit_white);
                RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                c.drawBitmap(icon,null,icon_dest, paint);
            } else {
                paint.setColor(mActivity.getResources().getColor(R.color.colorDeleteItem));
                RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                c.drawRect(background, paint);
                icon = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.ic_delete);
                RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                c.drawBitmap(icon,null,icon_dest, paint);
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }


    public interface SwipeReactor {
        void onLeftSwiped(int position);
        void onRightSwiped(int position);
    }
}
