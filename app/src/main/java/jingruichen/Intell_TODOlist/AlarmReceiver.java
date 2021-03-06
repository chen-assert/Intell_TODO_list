package jingruichen.Intell_TODOlist;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.content.Context.NOTIFICATION_SERVICE;

//ref:https://blog.csdn.net/fan7983377/article/details/51993793
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //when the system come to that set time,system would execute here
        String CHANNEL_ID = "my_channel";// The id of the channel.
        CharSequence name = "my_channel";// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        Notification.Builder mBuilder = new Notification.Builder(context, "my_channel");
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        mBuilder.setSmallIcon(R.drawable.ic_notifications);
        int id = intent.getIntExtra("id", 1);
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String before = intent.getStringExtra("before");
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(content);
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = id;
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.createNotificationChannel(mChannel);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    public AlarmReceiver() {

    }
}