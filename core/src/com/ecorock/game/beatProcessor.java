package com.ecorock.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import java.io.File;

public class beatProcessor { // Class to process beatmap files
private String str;
private String[] general,temp;
private float[] secs;
private Array<Float> longs;
private int[]pos;
private FileHandle file;

public beatProcessor(FileHandle file){
    this.file =file;
} // Constructor to initialize the file



// Method to extract beat timings in seconds from the beatmap file
public float[] getSeconds(){
    // Read the file and divide it into lines
    Divide();
    // Initialize arrays
    secs = new float[temp.length];
    int k;
    // Process each line to extract beat timings
    for (int i = 0; i <temp.length ; i++) {
        k =findStart(i);
        // Concatenate characters until '-' is found to get the beat timing
        for (int j = 2; j < k; j++) {
            temp[i]+= String.valueOf(general[i].charAt(j));
        }
    }
    // Convert extracted timings to float array
    for (int i = 0; i < temp.length; i++) {
        secs[i] = Float.parseFloat(temp[i]);
    }
    return secs;
}
    // Method to extract long note timings from the beatmap file
public Array<Float> getLongs(){
    // Find long note timings from the beatmap
    longs = findLongs(general);
    return longs;
}

    // Method to extract positions from the beatmap file
public int[] getPos(){
    // Initialize array
    pos = new int[temp.length];
    // Extract positions from each line
    for (int i = 0; i <temp.length ; i++) {
            temp[i]= String.valueOf(general[i].charAt(0));
    }
    // Convert extracted positions to integer array
    for (int i = 0; i < temp.length; i++) {
        pos[i] = Integer.parseInt(temp[i]);
    }
    return pos;
}
    // Method to find long note timings in the beatmap file
    private Array<Float> findLongs(String[] str)
    {
        // Read the file and divide it into lines
        Divide();
        // Initialize array to store long note timings
        Array<Float> longTimes = new Array<Float>();
        int k;
        // Iterate through each line to find long note timings
        for (int i = 0; i < str.length; i++) {
            k = findStart(i)+2;
            // If the line contains a long note, extract the timing
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
    // Method to reset an array
private String[] resetArr(String[] str){
    for (int i = 0; i < str.length; i++) {
        str[i]="";
    }
return str;
}

    // Method to divide the beatmap file into lines
    private void Divide(){
        // Read the file and split it into lines
        str = file.readString();
        general = str.split("\n");
        temp = new String[general.length];
        // Reset temporary array
        resetArr(temp);
    }
    // Method to find the start of a beat in a line
    private int findStart(int ind){
        int i=0;
        int j =2;
        // Iterate through characters until '-' is found
        while(!(general[ind].charAt(j)=='-')) {
            i = j;
            j++;
        }
        return i;
    }
}
