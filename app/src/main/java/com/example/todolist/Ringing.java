package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Ringing extends AppCompatActivity {
    TextView textView;
    Button btn;
    Database database;
    int id = 0;
    Ringtone ringtone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringing);

        final Intent intent = getIntent();
        id = intent.getIntExtra("id",3);

        database = new Database(this,"TODOLIST",null,1);
        Cursor cursor = database.getTask(id);
        cursor.moveToFirst();

        textView =(TextView) findViewById(R.id.txtName);
        textView.setText(cursor.getString(1));
        //Toast.makeText(getApplicationContext(),String.valueOf(cursor.getInt(0)),Toast.LENGTH_LONG).show();
        ringtone = RingtoneManager.getRingtone(this, Settings.System.DEFAULT_RINGTONE_URI);
        ringtone.play();

        btn = (Button) findViewById(R.id.btnStop);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentt = new Intent(Ringing.this,AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(Ringing.this,id,intentt,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
                ringtone.stop();
                finish();
            }
        });
    }
}