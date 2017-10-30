package com.example.shaw.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class StaticReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");

        if(intent.getAction().equals("STATICACTION"))
        {
            Bundle bundle = intent.getExtras();
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), bundle.getInt("icon"));
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentTitle("新商品热卖")
                    .setContentText(bundle.getString("item")+"仅售"+bundle.getString("price"))
                    .setTicker("您有一条新消息")
                    .setLargeIcon(bm)
                    .setSmallIcon(bundle.getInt("icon"))//很重要，不知道为什么之前没有这一句会闪退
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true);

            Intent intent1 = new Intent(context, ItemDetail.class);
            intent1.putExtra("itemName", bundle.getString("item"));
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(pendingIntent);
            Notification notify = builder.build();
            manager.notify(0, notify);
        }

    }
}
