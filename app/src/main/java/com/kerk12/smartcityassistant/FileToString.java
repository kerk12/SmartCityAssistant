package com.kerk12.smartcityassistant;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by kgiannakis on 13/2/2017.
 */

public class FileToString {
    private int fileName;
    private boolean configured = false;

    public FileToString(int FileName){
        fileName = FileName;
        configured = true;
        //TODO check if file exists
    }

    public String convert(Context c){
        StringBuilder sb = new StringBuilder();

        InputStream is = c.getResources().openRawResource(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;
        try {
            while ((line = br.readLine()) != null){
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
