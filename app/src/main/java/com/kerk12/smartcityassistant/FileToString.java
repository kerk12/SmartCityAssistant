package com.kerk12.smartcityassistant;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Class used for parsing the contents of an ASCII file to a string. The file is taken from the Raw folder.
 */
public class FileToString {
    private int fileName;
    private boolean configured = false;

    public FileToString(int FileName){
        fileName = FileName;
        configured = true;
        //TODO check if file exists
    }

    /**
     *
     * @param context The context of the app
     * @return A string with the contents of the file.
     * @throws Resources.NotFoundException When the specified Raw file doesn't exist.
     */
    public String convert(Context context) throws Resources.NotFoundException{
        StringBuilder sb = new StringBuilder();

        InputStream is = context.getResources().openRawResource(fileName);
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
