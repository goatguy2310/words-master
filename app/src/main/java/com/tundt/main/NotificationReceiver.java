package com.tundt.main;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.tundt.model.Note;

import java.util.ArrayList;
import java.util.Random;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT > 26) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("default",
                    "Channel name",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel description");
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManager not = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent repeatingIntent = new Intent(context, NoteActivity.class);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pid = PendingIntent.getActivity(context, 100, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        ArrayList<Note> noteArr = new ArrayList<>();

        Cursor c = Const.noteDatabase.query("notes", null, null, null, null, null, null);
        noteArr.clear();
        while (c.moveToNext()) {
            int id = c.getInt(0);
            String word = c.getString(1);
            String meaning = c.getString(2);
            noteArr.add(new Note(word, meaning, id));
        }
        c.close();

        Note n = noteArr.get(new Random().nextInt(noteArr.size()));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                .setContentIntent(pid)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(n.word)
                .setContentText(n.meaning);
        not.notify(100, builder.build());
    }
}
