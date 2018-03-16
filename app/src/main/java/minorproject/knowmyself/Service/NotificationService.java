package minorproject.knowmyself.Service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import minorproject.knowmyself.Database.ServicesDBHelper;


public class NotificationService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        System.out.println("notification aa rhi h");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        ServicesDBHelper servicesDBHelper = new ServicesDBHelper(context);
        String uniqueID = intent.getStringExtra("ID");
        if(action.equals("YES_ACTION")) {
            //TODO  update in database
            servicesDBHelper.updateResponse("0",uniqueID);
            Log.v("shuffTest","Pressed YES");
            notificationManager.cancel(101);
        } else if("MAYBE_ACTION".equals(action)) {
            //TODO
            servicesDBHelper.updateResponse("1",uniqueID);
            Log.v("shuffTest","Pressed NO");
            notificationManager.cancel(101);
        } else if("NO_ACTION".equals(action)) {
            //TODO
            servicesDBHelper.updateResponse("2",uniqueID);
            Log.v("shuffTest","Pressed MAYBE");
            notificationManager.cancel(101);
        }
    }
}