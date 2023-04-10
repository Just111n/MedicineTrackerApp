package com.example.medicinetrackerapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// TODO DELETE LATER
public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.ReminderViewHolder> {

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
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.reminder_list_item, parent, false);
        return new ReminderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {


            // TODO 1.3 Read data in MainActivity
            Reminder reminder = data.getReminder(position);

            holder.getMed_name_value_text_view().setText(reminder.getMedName());
            holder.getMed_type_value_text_view().setText(reminder.getMedType());

            String medNotificationTimesText = reminder.getMedNotificationTimes().toString();
            medNotificationTimesText = medNotificationTimesText.substring(1, medNotificationTimesText.length() - 1);

            holder.getMed_notification_time_value_text_view().setText(medNotificationTimesText);
            holder.getMed_dosage_value_text_view().setText(reminder.getMedDosage());

            holder.getMed_info_button().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DrugViewActivity.class);
                    intent.putExtra(ReminderEditorActivity.MED_NAME_KEY, reminder.getMedName());
                    intent.putExtra(ReminderEditorActivity.MED_ID_KEY, reminder.getId());
                    context.startActivity(intent);
                }
            }


        );



    }

    @Override
    public int getItemCount() {
        return data.getSize();
    }

    class ReminderViewHolder extends RecyclerView.ViewHolder {

        private View itemView;

        private TextView med_name_value_text_view, med_type_value_text_view, med_notification_time_value_text_view, med_dosage_value_text_view;

        private Button med_info_button;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            med_name_value_text_view = itemView.findViewById(R.id.med_name_value_text_view);
            med_type_value_text_view = itemView.findViewById(R.id.med_type_value_text_view);
            med_notification_time_value_text_view = itemView.findViewById(R.id.med_notification_time_value_text_view);
            med_dosage_value_text_view = itemView.findViewById(R.id.med_dosage_value_text_view);
            med_info_button = itemView.findViewById(R.id.med_info_button);


            // TODO onClick reminder
           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   int position = getAdapterPosition();
                   String medName = med_name_value_text_view.getText().toString();

                   String medNotificationTimesText = med_notification_time_value_text_view.getText().toString();

                   String[] medNotificationTimesArray = medNotificationTimesText.split(",");
                   ArrayList<String> medNotificationTimes = new ArrayList<>();
                   for (String time : medNotificationTimesArray) {
                       medNotificationTimes.add(time.trim());  // trim and add to list
                   }
                   String medType = med_type_value_text_view.getText().toString();
                   String medDosage = med_dosage_value_text_view.getText().toString();




                   // TODO 1.4 Send data to ReminderEditorActivity
                   Intent intent = new Intent(context,ReminderEditorActivity.class);
                   intent.putExtra(ReminderEditorActivity.ACTION_KEY,ReminderEditorActivity.UPDATE);
                   intent.putExtra(ReminderEditorActivity.POSITION_KEY,position);
                   intent.putExtra(ReminderEditorActivity.MED_NAME_KEY,medName);
                   intent.putExtra(ReminderEditorActivity.MED_NOTIFICATION_TIMES_KEY,medNotificationTimes);
                   intent.putExtra(ReminderEditorActivity.MED_TYPE_KEY, medType);
                   intent.putExtra(ReminderEditorActivity.MED_DOSAGE_KEY, medDosage);
                   context.startActivity(intent);


               }
           });
           // TODO onLongClick Reminder
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
                                   notifyItemRemoved(position);
                               }
                           }).setNegativeButton("No",null).show();



                   return true;
               }
           });

        }

        /* View Getters Section */
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

        public Button getMed_info_button() {return med_info_button;}


        /* End View Getters Section */



     
    }
}
