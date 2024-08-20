package com.example.demomacros;

import java.sql.Date;
import java.time.LocalDate;

public class ProductInfo {

    private int id;
    private String title;
    private float calories, carbs, proteins, fats;
    private LocalDate date;

    public ProductInfo() {
        id = 0;
        title = "";
        calories = 0.0f;
        carbs = 0.0f;
        proteins = 0.0f;
        fats = 0.0f;
        date = null;
    }

    public ProductInfo (int id, String title, float calories, float carbs, float proteins, float fats, LocalDate date) {
        this.id = id;
        this.title = title;
        this.calories = calories;
        this.carbs = carbs;
        this.proteins = proteins;
        this.fats = fats;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public float getCalories() { return calories; }
    public void setCalories(float calories) { this.calories = calories; }

    public float getCarbs() { return carbs; }
    public void setCarbs(float carbs) { this.carbs = carbs; }

    public float getProteins() { return proteins; }
    public void setProteins(float proteins) { this.proteins = proteins; }

    public float getFats() { return fats; }
    public void setFats(float fats) { this.fats = fats; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
