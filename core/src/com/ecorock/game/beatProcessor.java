package com.ecorock.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class beatProcessor {
private String str;
private String[] general,temp;
private float[] secs;
private FileHandle file;
public float[] getSeconds(){
    file = Gdx.files.internal("SongBeats/HisTheme.txt");
    str = file.readString();
    general = str.split("\n");
    temp = new String[general.length];
    resetArr(temp);
    secs = new float[temp.length];
    for (int i = 0; i <temp.length ; i++) {
        for (int j = 2; j < 7; j++) {
            temp[i]+= String.valueOf(general[i].charAt(j));
        }
    }
    for (int i = 0; i < temp.length; i++) {
        secs[i] = Float.parseFloat(temp[i]);
    }
    return secs;
}

private String[] resetArr(String[] str){
    for (int i = 0; i < str.length; i++) {
        str[i]="";
    }
return str;
}
}
