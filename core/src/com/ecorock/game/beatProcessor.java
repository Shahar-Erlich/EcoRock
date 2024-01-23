package com.ecorock.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;

public class beatProcessor {
private String str;
private String[] general,temp;
private float[] secs,longs;
private int[]pos;
private FileHandle file;

public beatProcessor(FileHandle file){
    this.file =file;
}

public float[] getSeconds(){
    //song offset ~1.5 secs
    str = file.readString();
    general = str.split("\n");
    temp = new String[general.length];
    resetArr(temp);
    findLongs(general);
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
public float[] getLongs(){
    longs = findLongs(general);
    return longs;
}
public int[] getPos(){
    str = file.readString();
    general = str.split("\n");
    temp = new String[general.length];
    resetArr(temp);
    pos = new int[temp.length];
    for (int i = 0; i <temp.length ; i++) {
            temp[i]= String.valueOf(general[i].charAt(0));
    }
    for (int i = 0; i < temp.length; i++) {
        pos[i] = Integer.parseInt(temp[i]);
    }
    return pos;
}
    private float[] findLongs(String[] str)
    {
        float[] longTimes = new float[str.length];
        for (int i = 0; i < str.length; i++) {
            if(str[i].contains("-")){
                String temp ="";
                for (int j = 8; j < str[i].length(); j++) {
                    temp +=str[i].charAt(j);
                }
                longTimes[i] = Float.parseFloat(temp);
            }
        }


        return longTimes;
    }
private String[] resetArr(String[] str){
    for (int i = 0; i < str.length; i++) {
        str[i]="";
    }
return str;
}
}
