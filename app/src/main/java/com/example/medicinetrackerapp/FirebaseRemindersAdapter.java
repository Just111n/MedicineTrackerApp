package com.example.medicinetrackerapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FirebaseRemindersAdapter extends FirebaseRecyclerAdapter<ReminderModel, FirebaseRemindersAdapter.ReminderViewHolder> {


     Context context;


    public FirebaseRemindersAdapter(@NonNull FirebaseRecyclerOptions options, Context context) {
        super(options);
        this.context = context;


    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_list_item, parent, false);
        return new FirebaseRemindersAdapter.ReminderViewHolder(itemView);
    }


    @Override
    protected void onBindViewHolder(@NonNull ReminderViewHolder holder, int position, @NonNull ReminderModel reminderModel) {
        String medId = reminderModel.getId();

        holder.getItemView().setOnClickListener(view -> {
            String medName = reminderModel.getMedName();
            ArrayList<String> medNotificationTimes = reminderModel.getMedNotificationTimes();
            String medType = reminderModel.getMedType();
            String medDosage = reminderModel.getMedDosage();


            // Click on reminder to Update and Send data to ReminderEditorActivity for data to be shown in data fields in ReminderEditorActivity
            Intent intent = new Intent(context,ReminderEditorActivity.class);
            intent.putExtra(ReminderEditorActivity.ACTION_KEY,ReminderEditorActivity.UPDATE);
            intent.putExtra(ReminderEditorActivity.MED_ID_KEY,medId);
            intent.putExtra(ReminderEditorActivity.MED_NAME_KEY,medName);
            intent.putExtra(ReminderEditorActivity.MED_NOTIFICATION_TIMES_KEY,medNotificationTimes);
            intent.putExtra(ReminderEditorActivity.MED_TYPE_KEY, medType);
            intent.putExtra(ReminderEditorActivity.MED_DOSAGE_KEY, medDosage);
            context.startActivity(intent);


        });
        holder.getItemView().setOnLongClickListener(view -> {

//                    Reminder reminder = data.getReminder(position);
            new AlertDialog.Builder(context)
                    .setTitle(R.string.are_you_sure)
                    .setMessage(R.string.ask_delete_reminder)
                    .setIcon(R.drawable.ic_app)
                    .setPositiveButton(R.string.yes, (dialogInterface, i) -> MainActivity.mbase.child(medId).removeValue()).setNegativeButton(R.string.no,null).show();



            return true;
        });

        holder.getMed_name_value_text_view().setText(reminderModel.getMedName());
        holder.getMed_type_value_text_view().setText(reminderModel.getMedType());

        String medNotificationTimesText = reminderModel.getMedNotificationTimes().toString();
        medNotificationTimesText = medNotificationTimesText.substring(1, medNotificationTimesText.length() - 1);

        holder.getMed_notification_time_value_text_view().setText(medNotificationTimesText);
        holder.getMed_dosage_value_text_view().setText(reminderModel.getMedDosage());

        // Click on med_info_button and send data to medInfoActivity
        holder.getMed_info_button().setOnClickListener(view -> {
            Intent intent = new Intent(context, MedicineInfoActivity.class);
            intent.putExtra(ReminderEditorActivity.MED_NAME_KEY, reminderModel.getMedName());
            context.startActivity(intent);
        });
        if (reminderModel.getMedNotificationTimes()!=null) {
            try {
            setAlarm(reminderModel.getMedName(),reminderModel.getMedDosage(),reminderModel.getMedNotificationTimes());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }



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


    private void setAlarm(String medName, String medDosage, ArrayList<String> times) throws ParseException {
        for (String time : times) {
            if (!isValidDate(time)) {
                Log.e("testing", "Invalid time string: " + time);
                continue;
            }
            //assigning alarm manager object to set alarm
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            //sending data to alarm class to create channel and notification
            Intent intent = new Intent(context.getApplicationContext(), AlarmBroadcast.class);
            intent.putExtra(AlarmBroadcast.Event_KEY, medName);
            intent.putExtra(AlarmBroadcast.TIME_KEY, time);
            intent.putExtra(AlarmBroadcast.DOSAGE_KEY, medDosage);
            long uniqueId = System.currentTimeMillis();

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), (int) uniqueId, intent, PendingIntent.FLAG_ONE_SHOT);

            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
            Date date = sdf.parse(time);
            Calendar calendar1 = Calendar.getInstance();
            assert date != null;
            calendar1.setTime(date);
            int hour = calendar1.get(Calendar.HOUR_OF_DAY);
            int minute = calendar1.get(Calendar.MINUTE);

            // get current date and time
            Calendar calendar = Calendar.getInstance();

            // set time
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);

            long alarmTime = calendar.getTimeInMillis();

            // Set a repeating alarm for each time in the list
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        }
    }





    private boolean isValidDate(String dateStr) {
        DateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        formatter.setLenient(false);
        try {
            formatter.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }



}
