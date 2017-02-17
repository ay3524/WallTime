package ay3524.com.wallpapertime.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ay3524.com.wallpapertime.R;
import ay3524.com.wallpapertime.data.LogDbContract;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ashish on 15-02-2017.
 */

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public LogAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public LogAdapter.LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.single_log_item, parent, false);

        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LogAdapter.LogViewHolder holder, int position) {

        int taskIndex = mCursor.getColumnIndex(LogDbContract.LogDbContractEntry.COLUMN_NAME_TASK);
        int timeIndex = mCursor.getColumnIndex(LogDbContract.LogDbContractEntry.COLUMN_NAME_TIME);

        mCursor.moveToPosition(position);

        String task = mCursor.getString(taskIndex);
        String time = mCursor.getString(timeIndex);

        if (task.equals(mContext.getString(R.string.yes_wall_del))) {
            holder.task.setText(task);
        } else {
            holder.task.setText(mContext.getString(R.string.dwnld).concat(task));
        }

        holder.time.setText(time);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    class LogViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.task)
        TextView task;
        @BindView(R.id.time)
        TextView time;

        LogViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
