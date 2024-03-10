package com.ecorock.game.MainPage;

import android.graphics.drawable.Drawable;

import com.ecorock.game.R;

import java.util.LinkedList;
import java.util.List;

public class GuitarTemplate {
    private int resource;
    private String name;
    private String filePath;

    public GuitarTemplate(int resource, String name,String filePath) {
        this.resource = resource;
        this.name = name;
        this.filePath = filePath;
    }
    public GuitarTemplate(){
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public String getName() {
        return name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setName(String name) {
        this.name = name;
    }
    public static List<GuitarTemplate> getList(){
        List<GuitarTemplate> gtl = new LinkedList<>();
        GuitarTemplate defaultGuitar = new GuitarTemplate(R.drawable.guitarneckdefault,"Default Skin","GuitarNeckTest.png");
        GuitarTemplate defaultGuitar2 = new GuitarTemplate(R.drawable.guitarneckdefault2,"Default Skin 2","GuitarNeckTest2.png");
        gtl.add(defaultGuitar);
        gtl.add(defaultGuitar2);
        return gtl;
    }
}
