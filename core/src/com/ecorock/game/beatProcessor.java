package com.ecorock.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import java.io.File;

public class beatProcessor {
private String str;
private String[] general,temp;
private float[] secs;
private Array<Float> longs;
private int[]pos;
private FileHandle file;

public beatProcessor(FileHandle file){
    this.file =file;
}

public float[] getSeconds(){
    //song offset ~1.5 secs
    Divide();
    secs = new float[temp.length];
    int k;
    for (int i = 0; i <temp.length ; i++) {
        k =findStart(i);
        for (int j = 2; j < k; j++) {
            temp[i]+= String.valueOf(general[i].charAt(j));
        }
    }
    for (int i = 0; i < temp.length; i++) {
        secs[i] = Float.parseFloat(temp[i]);
    }
    return secs;
}
public Array<Float> getLongs(){
    longs = findLongs(general);
    return longs;
}
public int[] getPos(){
    pos = new int[temp.length];
    for (int i = 0; i <temp.length ; i++) {
            temp[i]= String.valueOf(general[i].charAt(0));
    }
    for (int i = 0; i < temp.length; i++) {
        pos[i] = Integer.parseInt(temp[i]);
    }
    return pos;
}
    private Array<Float> findLongs(String[] str)
    {
        Divide();
        Array<Float> longTimes = new Array<Float>();
        int k;
        for (int i = 0; i < str.length; i++) {
            k = findStart(i)+2;
            if(str[i].contains("-")){
                String temp2 ="";
                for (int j = k; j < str[i].length(); j++) {
                    temp2 +=str[i].charAt(j);
                }
                longTimes.add(Float.parseFloat(temp2));
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
    private void Divide(){
        str = file.readString();
        general = str.split("\n");
        temp = new String[general.length];
        resetArr(temp);
    }
    private int findStart(int ind){
        int i=0;
        int j =2;
        while(!(general[ind].charAt(j)=='-')) {
            i = j;
            j++;
        }
        return i;
    }
}
