package com.stuckinadrawer;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class FileReader {

    private File terminalsFile;
    private File nonTerminalsFile;

    public FileReader() {

        File dir = new File(".");
        try {
            terminalsFile = new File(dir.getCanonicalPath() + File.separator + "terminals.txt");
            nonTerminalsFile = new File(dir.getCanonicalPath() + File.separator + "nonTerminals.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getNonTerminals(){
        return loadFile(nonTerminalsFile);
    }

    public ArrayList<String> getTerminals(){
        return loadFile(terminalsFile);
    }

    private ArrayList<String> loadFile(File file){
        ArrayList<String> strings = new ArrayList<String>();

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
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
        Collections.sort(strings);

        return strings;
    }

    public static void main(String[] arg) throws IOException {
        FileReader fr = new FileReader();
        for(String s : fr.getNonTerminals()){
            System.out.println(s);
        }
        System.out.println("---");
        for(String s : fr.getTerminals()){
            System.out.println(s);
        }

    }

}
