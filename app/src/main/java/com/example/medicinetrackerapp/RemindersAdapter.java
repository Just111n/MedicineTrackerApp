package com.example.medicinetrackerapp;

import static androidx.core.content.ContextCompat.startActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.RemindersHolder> {

    LayoutInflater mInflater;
    Context context;
    RemindersDataSource data;

    RemindersAdapter(Context context, RemindersDataSource remindersDataSource) {
        this.context = context;
        this.data = remindersDataSource;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RemindersAdapter.RemindersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.reminder_list_item, parent, false);
        return new RemindersHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RemindersAdapter.RemindersHolder holder, int position) {
        Reminder reminder = data.getReminder(position);
        holder.getMed_name_value_text_view().setText(reminder.getMedName());
        holder.getMed_type_value_text_view().setText(reminder.getMedType());
        // TODO Notification Time
        holder.getMed_dosage_value_text_view().setText(reminder.getMedDosage());


    }

    @Override
    public int getItemCount() {
        return data.getSize();
    }

    class RemindersHolder extends RecyclerView.ViewHolder {

        private View itemView;

        private TextView med_name_value_text_view;
        private TextView med_type_value_text_view;
        private TextView med_notification_time_value_text_view;
        private TextView med_dosage_value_text_view;






        public RemindersHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            med_name_value_text_view = itemView.findViewById(R.id.med_name_value_text_view);
            med_type_value_text_view = itemView.findViewById(R.id.med_type_value_text_view);
            med_notification_time_value_text_view = itemView.findViewById(R.id.med_notification_time_value_text_view);
            med_dosage_value_text_view = itemView.findViewById(R.id.med_dosage_value_text_view);
           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   int position = getAdapterPosition();
                   String medName = med_name_value_text_view.getText().toString();
                   Intent intent = new Intent(context,ReminderEditorActivity.class);
                   intent.putExtra(ReminderEditorActivity.ACTION_KEY,ReminderEditorActivity.UPDATE);
                   intent.putExtra(ReminderEditorActivity.POSITION_KEY,position);
                   intent.putExtra(ReminderEditorActivity.MED_NAME_KEY,medName);
                   context.startActivity(intent);


               }
           });
           itemView.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View view) {
                   int position = getAdapterPosition();
                   Reminder reminder = data.getReminder(position);

                   new AlertDialog.Builder(context)
                           .setTitle("Are you sure?")
                           .setMessage("Do you want to delete this reminder?")
                           .setIcon(R.drawable.ic_app)
                           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialogInterface, int i) {
                                   data.removeReminder(position);
                                   notifyDataSetChanged();
                               }
                           }).setNegativeButton("No",null).show();



                   return true;
               }
           });

        }


        public View getItemView() {
            return itemView;
        }

        public TextView getMed_name_value_text_view() {
            return med_name_value_text_view;
        }

        public TextView getMed_type_value_text_view() {
            return med_type_value_text_view;
        }

        public TextView getMed_notification_time_value_text_view() {
            return med_notification_time_value_text_view;
        }

        public TextView getMed_dosage_value_text_view() {
            return med_dosage_value_text_view;
        }

     
    }
}
