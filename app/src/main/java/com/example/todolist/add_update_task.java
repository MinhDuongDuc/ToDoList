package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class add_update_task extends AppCompatActivity {
    TextView date,time,name,detail,currentTime;
    ImageButton date_pick,time_pick;
    String date1,time1,error = "";
    Database database = new Database(this,"TODOLIST",null,1);
    boolean isEdit = false;
    int id = 0,requestCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_task);

        name = (TextView) findViewById(R.id.txtName);
        detail = (TextView) findViewById(R.id.txtDetail);
        currentTime = (TextView) findViewById(R.id.txtCurrent_time);

        date = (TextView) findViewById(R.id.txtDate);
        date_pick = (ImageButton) findViewById(R.id.date_pick);
        time = (TextView) findViewById(R.id.txtTime);
        time_pick = (ImageButton) findViewById(R.id.time_pick);

        String actionbarTitle = null;
        ToDoList task = (ToDoList) getIntent().getSerializableExtra("task");

        if (task != null){
            isEdit = true;
        }
        if(isEdit){
            id = task.getId();
            currentTime.setText("Chỉnh sửa lần cuối vào: "+task.getCurrentDate_time());
            name.setText(task.getName());
            detail.setText(task.getDetail());
            date.setText(task.getDate());
            time.setText(task.getTime());
        }else {
            Toast.makeText(getApplicationContext(),"day la activity add",Toast.LENGTH_SHORT).show();
        }

        date_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chonNgay();
            }
        });
        time_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chonGio();
            }
        });
    }

    private void chonNgay(){
        final Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i,i1,i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                date1 = simpleDateFormat.format(calendar.getTime());
                date.setText(date1);
            }
        },nam,thang,ngay);
        datePickerDialog.show();
    }
    private void chonGio(){
        final Calendar calendar = Calendar.getInstance();
        int gio = calendar.get(Calendar.HOUR);
        int phut = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                calendar.set(0,0,0,i,i1);
                time1 = simpleDateFormat.format(calendar.getTime());
                time.setText(time1);
            }
        },gio,phut,false);
        timePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.saveItem:
                if(isEdit){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String current_date = simpleDateFormat.format(System.currentTimeMillis());
                    validate();
                    if(error.equals("nullError")){
                        Toast.makeText(getApplicationContext(),"Vui lòng nhập đầy đủ thông tin",Toast.LENGTH_LONG).show();
                    }else if (error.equals("timeError")){
                        Toast.makeText(getApplicationContext(),"Vui lòng chọn mốc thời gian trong tương lai",Toast.LENGTH_LONG).show();
                    }else {
                        database.update(id,name.getText().toString(),detail.getText().toString(),time.getText().toString(),date.getText().toString(),current_date);
                        setAlarm(id);
                        startActivity(new Intent(add_update_task.this,MainActivity.class));
                    }

                }else {
                    requestCode = (int) System.currentTimeMillis();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String current_date = simpleDateFormat.format(System.currentTimeMillis());
                    validate();
                    if(error.equals("nullError")){
                        Toast.makeText(getApplicationContext(),"Vui lòng nhập đầy đủ thông tin",Toast.LENGTH_LONG).show();
                    }else if (error.equals("timeError")){
                        Toast.makeText(getApplicationContext(),"Vui lòng chọn mốc thời gian trong tương lai",Toast.LENGTH_LONG).show();
                    }else {
                        database.insert(requestCode,name.getText().toString(),detail.getText().toString(),time.getText().toString(),date.getText().toString(),current_date);
                        setAlarm(requestCode);
                        startActivity(new Intent(add_update_task.this,MainActivity.class));
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void setAlarm(int requestCode){

        AlarmManager alarmManager;
        PendingIntent pendingIntent;
        DateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date alarm_date = null;
        try {
            alarm_date = simpleDateFormat.parse(date.getText().toString()+" "+time.getText().toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(alarm_date);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(add_update_task.this,AlarmReceiver.class);
        intent.putExtra("id",requestCode);
        pendingIntent = PendingIntent.getBroadcast(add_update_task.this,requestCode,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pendingIntent);
        //sendBroadcast(intent);
    }
    public boolean compareTime(){
        Calendar cal1 = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String date_current = simpleDateFormat.format(cal1.getTime());
        String date_set = date.getText().toString()+" "+time.getText().toString();


        if(date_set.compareTo(date_current) < 0){
            return false;
        }else if(date_set.equals(date_current)){
            return false;
        }else return true;
    }
    public void validate(){
        if(name.getText().toString().equals("") || detail.getText().toString().equals("") || time.getText().toString().equals("")|| date.getText().toString().equals("")){
            error = "nullError";
        }else if(!compareTime()){
            error = "timeError";
        }else error = "";

    }
}