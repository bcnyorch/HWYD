package studio.home.howwasyourday;

import android.widget.CalendarView;

/**
 * Created by yorch on 03/06/15.
 */
public class MyDateChangeListener implements CalendarView.OnDateChangeListener {

    MainActivity activity;

    MyDateChangeListener(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
        activity.alerta(view, year, month, dayOfMonth);
    }
}
