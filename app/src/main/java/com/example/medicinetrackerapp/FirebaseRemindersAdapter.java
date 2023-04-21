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

    /*
    FirebaseRecyclerAdapter constructor takes care of setting up a listener
    on the Firebase Realtime Database query and mapping the query results to
    instances of the ReminderModel class.
    */
    public FirebaseRemindersAdapter(@NonNull FirebaseRecyclerOptions<ReminderModel> options, Context context) {
        super(options);
        this.context = context;


    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_list_item, parent, false);
        return new ReminderViewHolder(itemView);
    }


    @Override
    protected void onBindViewHolder(@NonNull ReminderViewHolder holder, int position, @NonNull ReminderModel reminderModel) {
        String medId = reminderModel.getId();
        String medName = reminderModel.getMedName();
        ArrayList<String> medNotificationTimes = reminderModel.getMedNotificationTimes();
        String medType = reminderModel.getMedType();
        String medDosage = reminderModel.getMedDosage();


        holder.getMed_name_value_text_view().setText(medName);
        holder.getMed_type_value_text_view().setText(medType);

        String medNotificationTimesText = medNotificationTimes.toString();
        medNotificationTimesText = medNotificationTimesText.substring(1, medNotificationTimesText.length() - 1);
        holder.getMed_notification_time_value_text_view().setText(medNotificationTimesText);

        holder.getMed_dosage_value_text_view().setText(reminderModel.getMedDosage());

        // Click on med_info_button and send data to medInfoActivity
        holder.getMed_info_button().setOnClickListener(view -> {
            Intent intent = new Intent(context, MedicineInfoActivity.class);
            intent.putExtra(ReminderEditorActivity.MED_NAME_KEY, medName);
            context.startActivity(intent);
        });

        // Click on reminder to Update and Send data to ReminderEditorActivity for data to be shown in data fields in ReminderEditorActivity
        holder.getItemView().setOnClickListener(view -> {
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
            new AlertDialog.Builder(context)
                    .setTitle(R.string.are_you_sure)
                    .setMessage(R.string.ask_delete_reminder)
                    .setIcon(R.drawable.ic_app)
                    .setPositiveButton(R.string.yes, (dialogInterface, i) -> MainActivity.mbaseUserReminders.child(medId).removeValue()).setNegativeButton(R.string.no,null).show();
            return true;
        });

        // Set timed notifications
        try {
        setAlarm(medId,medName,medDosage,medNotificationTimes);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


    }



    static class ReminderViewHolder extends RecyclerView.ViewHolder {
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
    private void setAlarm(String medId,String medName, String medDosage, ArrayList<String> times) throws ParseException {
        int i = 0;
        Log.d(AlarmBroadcast.NOTI,"SETTING THE ALARM FOR NOTIFICATIONS!");
        for (String time : times) {
            i++;
            if (!isValidDate(time)) {
                continue;
            }
            //assigning alarm manager object to set alarm
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
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
            calendar.set(Calendar.SECOND, 0);

            long alarmTime = calendar.getTimeInMillis();

            Calendar nowCal = Calendar.getInstance();
            long nowTime = nowCal.getTimeInMillis();
            if (nowTime>alarmTime) {
                continue;
            }
            String digits = medId.replaceAll("\\D", ""); // remove non-numeric characters
            int number = Integer.parseInt(digits); // parse the resulting string as an integer
            int notiId = number + i;

            //sending data to alarm class to create channel and notification
            Intent intent = createAlarmIntent(notiId,medName,time, medDosage,context.getApplicationContext());


            // number + i => 2 timings will give 2 notifications of different medicine
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), notiId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


            // Set alarm for each time in the list
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
            Log.d(AlarmBroadcast.NOTI,"Alarm" +i+" is Set");

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

    private Intent createAlarmIntent(int medNotiId,String medName, String time, String medDosage, Context context) {
        Intent intent = new Intent(context.getApplicationContext(), AlarmBroadcast.class);
        intent.putExtra(AlarmBroadcast.Event_KEY, medName);
        intent.putExtra(AlarmBroadcast.TIME_KEY, time);
        intent.putExtra(AlarmBroadcast.DOSAGE_KEY, medDosage);
        intent.putExtra(AlarmBroadcast.NOTI_KEY,medNotiId);
        return intent;
    }




}
