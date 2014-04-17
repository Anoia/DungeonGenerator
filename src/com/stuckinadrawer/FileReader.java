package com.stuckinadrawer;

import java.io.*;
import java.util.ArrayList;

public class FileReader {

    private File dataFile;

    public FileReader() {

        File dir = new File(".");
        try {
            dataFile = new File(dir.getCanonicalPath() + File.separator + "data.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getVertexNames(){
        ArrayList<String> strings = new ArrayList<String>();

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(dataFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        String line = null;
        try {
            while((line = br.readLine()) != null){
            strings.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strings;
    }

    public static void main(String[] arg) throws IOException {
        FileReader fr = new FileReader();
        for(String s : fr.getVertexNames()){
            System.out.println(s);
        }

    }

}
