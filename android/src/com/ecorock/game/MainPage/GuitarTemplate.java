package com.ecorock.game.MainPage;

import android.graphics.drawable.Drawable;

import com.ecorock.game.R;

import java.util.LinkedList;
import java.util.List;

public class GuitarTemplate {
    private int resource;
    private String name;

    public GuitarTemplate(int resource, String name) {
        this.resource = resource;
        this.name = name;
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

    public void setName(String name) {
        this.name = name;
    }
    public static List<GuitarTemplate> getList(){
        List<GuitarTemplate> gtl = new LinkedList<>();
        GuitarTemplate defaultGuitar = new GuitarTemplate(R.drawable.guitarneckdefault,"Default Skin");
        gtl.add(defaultGuitar);
        return gtl;
    }
}
