package studio.home.howwasyourday;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static studio.home.howwasyourday.R.drawable.bad;
import static studio.home.howwasyourday.R.drawable.great;
import static studio.home.howwasyourday.R.drawable.normal;


public class MainActivity extends Activity {

    CalendarView calendar;
    int mId;
    public int state;
    public int alertHour;
    public int alertMinute;

    public final int GREAT  =  1;
    public final int NORMAL =  0;
    public final int BAD    = -1;

    public final String PREFS_FILE_NAME         = "HWYDPreferencesFile";

    public final String PREFS_ALERT_HOUR        = "HWYD.alert.hour";
    public final String PREFS_ALERT_MINUTE      = "HWYD.alert.minute";

    public final int PREFS_ALERT_HOUR_DEFAULT   = 14;
    public final int PREFS_ALERT_MINUTE_DEFAULT = 00;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // read default datetime
        getPreferences();

        //sets the main layout of the activity
        setContentView(R.layout.activity_main);

        //initializes the calendarview
        initializeCalendar();

        // set the datetime for the alert!
        initializeAlert();
    }

    private void getPreferences(){
        preferences = getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);

        alertHour    = preferences.getInt(PREFS_ALERT_HOUR, PREFS_ALERT_HOUR_DEFAULT);
        alertMinute  = preferences.getInt(PREFS_ALERT_MINUTE, PREFS_ALERT_MINUTE_DEFAULT);

        Toast.makeText(getApplicationContext(), "hora = " + alertHour + ":" + alertMinute, Toast.LENGTH_LONG).show();

    }

    private void initializeAlert() {
        Timer timer = new Timer();
        TimerTask tt = new TimerTask(){
            public void run(){
                Calendar cal = Calendar.getInstance(); //this is the method you should use, not the Date(), because it is desperated.

                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                if(hour == alertHour && minute == alertMinute){
                    notifica();
                }
            }
        };
        timer.schedule(tt, 0, 1000*5);
    }

    public void initializeCalendar() {
        calendar = (CalendarView) findViewById(R.id.calendar);

        // sets whether to show the week number.
        calendar.setShowWeekNumber(false);

        // sets the first day of week according to Calendar.
        // here we set Monday as the first day of the Calendar
        calendar.setFirstDayOfWeek(2);

        //The background color for the selected week.
        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));

        //sets the color for the dates of an unfocused month.
        calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));

        //sets the color for the separator line between weeks.
        calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));

        //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
        calendar.setSelectedDateVerticalBar(R.color.darkgreen);

        //sets the listener to be notified upon selected date change.
        MyDateChangeListener myDateChangeListener = new MyDateChangeListener(this);
        calendar.setOnDateChangeListener(myDateChangeListener);
    }

    public void notifica() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(great);
        mBuilder.setContentTitle("How was your day?");
        mBuilder.setContentText("Did you think about? DO IT! Time to rate!");

        Intent resultIntent = new Intent(this, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());
    }

    public synchronized void alerta(final CalendarView view, final int year, final int month, final int day) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("How was day: " + day + "/" + month + "/" + year + "?");
//        alert.setMessage("Message");

        builder.setPositiveButton(great, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(getApplicationContext(), "poner GREAT en " + day + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();
                CalendarView cal = (CalendarView) view;
                view.setSelectedWeekBackgroundColor(Color.GREEN);
            }
        }).setNeutralButton(normal, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(getApplicationContext(), "poner NORMAL en " + day + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();
                view.setSelectedWeekBackgroundColor(Color.GRAY);
            }
        }).setNegativeButton(bad, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(getApplicationContext(), "poner BAD en " + day + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();
                view.setSelectedWeekBackgroundColor(Color.RED);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

        Button button0 = alert.getButton(AlertDialog.BUTTON_POSITIVE);
        button0.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.great), null, null, null);
        button0.setText("");
        button0.setPadding(40,20,0,20);

        Button button1 = alert.getButton(AlertDialog.BUTTON_NEUTRAL);
        button1.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.normal), null, null, null);
        button1.setText("");
        button1.setPadding(40, 20, 0, 20);

        Button button2 = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
        button2.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.bad), null, null, null);
        button2.setText("");
        button2.setPadding(40, 20, 0, 20);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
