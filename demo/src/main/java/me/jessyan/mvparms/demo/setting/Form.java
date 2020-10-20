package me.jessyan.mvparms.demo.setting;

import android.graphics.Color;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import io.realm.RealmList;
import io.realm.RealmObject;


public class Form  {

    private int yearsOfExp;
    private int favoriteColor;
    private boolean isAdequate;
    private Set<String> technologies;

    public Form() {
        technologies = new HashSet<>();
        favoriteColor = Color.CYAN;
        yearsOfExp = 5;
    }

    public int getYearsOfExperience() {
        return yearsOfExp;
    }

    public void setYearsOfExp(int yearsOfExp) {
        this.yearsOfExp = yearsOfExp;
    }

    public int getFavoriteColor() {
        return favoriteColor;
    }

    public void setFavoriteColor(int favoriteColor) {
        this.favoriteColor = favoriteColor;
    }

    public boolean isAdequate() {
        return isAdequate;
    }

    public void setIsAdequate(boolean isAdequate) {
        this.isAdequate = isAdequate;
    }

    public Set<String> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(Set<String> technologies) {
        this.technologies = technologies;
    }

    @Override
    public String toString() {
        return "Form{" +
                "yearsOfExp=" + yearsOfExp +
                ", favoriteColor=" + favoriteColor +
                ", isAdequate=" + isAdequate +
                ", technologies=" + technologies +
                '}';
    }
}
