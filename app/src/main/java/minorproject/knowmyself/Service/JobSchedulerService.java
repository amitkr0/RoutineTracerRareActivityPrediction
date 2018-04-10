package minorproject.knowmyself.Service;
/*


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import minorproject.knowmyself.Service.SensorService;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService  {

//    MainActivity act = new MainActivity();//                          ToDo
    private Handler mJobHandler = new Handler( new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(), "JobService task running", Toast.LENGTH_SHORT).show();
            startService();
            return true;
        }
    });

    public void startService() {
        AlarmManager scheduler = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), SensorService.class );
        PendingIntent scheduledIntent = PendingIntent.getService(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        scheduler.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 15*60*1000, scheduledIntent);
//        startService(new Intent(getBaseContext(), SensorService.class));
    }

    @Override
    public boolean onStartJob(JobParameters params ) {
        mJobHandler.sendMessage( Message.obtain( mJobHandler, 1, params ) );
        return true;
    }

    @Override
    public boolean onStopJob( JobParameters params ) {
        mJobHandler.removeMessages( 1 );
        return false;
    }

}*/
