package com.example.taskmanagerapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagerapp.R;
import com.example.taskmanagerapp.data.local.TaskEntry;

import java.util.ArrayList;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.EntryHolder> {

    private List<TaskEntry> entryList = new ArrayList<>();
    private TapListener tapListener;
    private HoldListener holdListener;

    public interface TapListener {
        void onEntryTapped(TaskEntry entry);
    }

    public interface HoldListener {
        void onEntryHeld(TaskEntry entry);
    }

    public void refreshList(List<TaskEntry> updatedList) {
        this.entryList = updatedList;
        notifyDataSetChanged();
    }

    public void setTapListener(TapListener listener) {
        this.tapListener = listener;
    }

    public void setHoldListener(HoldListener listener) {
        this.holdListener = listener;
    }

    @NonNull
    @Override
    public EntryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_card_item, parent, false);
        return new EntryHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryHolder holder, int position) {
        TaskEntry current = entryList.get(position);
        holder.tvTaskTitle.setText(current.getTaskTitle());
        holder.tvTaskBody.setText(current.getTaskBody());
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    class EntryHolder extends RecyclerView.ViewHolder {
        private final TextView tvTaskTitle;
        private final TextView tvTaskBody;

        public EntryHolder(@NonNull View row) {
            super(row);

            tvTaskTitle = row.findViewById(R.id.tvTaskTitle);
            tvTaskBody  = row.findViewById(R.id.tvTaskBody);

            row.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (tapListener != null && pos != RecyclerView.NO_POSITION) {
                    tapListener.onEntryTapped(entryList.get(pos));
                }
            });

            row.setOnLongClickListener(v -> {
                int pos = getAdapterPosition();
                if (holdListener != null && pos != RecyclerView.NO_POSITION) {
                    holdListener.onEntryHeld(entryList.get(pos));
                    return true;
                }
                return false;
            });
        }
    }
}