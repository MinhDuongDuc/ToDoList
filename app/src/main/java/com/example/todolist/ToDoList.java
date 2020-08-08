package com.example.todolist;

import java.io.Serializable;

public class ToDoList implements Serializable {
    private int id;
    private String name;
    private String detail;
    private String date;
    private String time;
    private String currentDate_time;

    public ToDoList(int id, String name, String detail,  String time,String date, String currentDate_time) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.time = time;
        this.date = date;
        this.currentDate_time = currentDate_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCurrentDate_time() {
        return currentDate_time;
    }

    public void setCurrentDate_time(String currentDate_time) {
        this.currentDate_time = currentDate_time;
    }
}
