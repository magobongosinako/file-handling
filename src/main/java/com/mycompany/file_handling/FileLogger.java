package com.mycompany.file_handling;

import java.text.SimpleDateFormat;
import java.io.*;
import java.util.*;
import javax.swing.JTextArea;

public class FileLogger implements AutoCloseable {


private String logFilePath;
private FileWriter writer;
private List<String> logHistory;
private JTextArea logArea; // For GUI logging

public FileLogger(String logFilePath){
    this.logFilePath = logFilePath;
    this.logHistory = new ArrayList<>();

    initializeLogger();
}
 private void initializeLogger(){
     try{

    // Creating the log file //
         File logFile = new File(logFilePath);
        File parentDir = logFile.getParentFile();
        if (parentDir != null && !parentDir.exists()){
          parentDir.mkdirs();
        }

       if(!logFile.exists()){

           // creating parent directories if needed//

           logFile.createNewFile();

       }
       this.writer = new FileWriter(logFile, true);
       logHistory.add("FileLogger initialized " + getCurrentTime());


     }catch(Exception e){
         System.out.println("Error initializing FileLogger : " + e.getMessage());



     }

 }
 public void log(String message){
     String timestamp = getCurrentTime();
     String logEntry = "[" + timestamp + "] " + message;
     // add to history//
     logHistory.add(logEntry);
     //write to the file//
     try{
       if (writer != null){
           writer.write(logEntry + "\n");
           writer.flush();//It ensures immediate writting//



       }

     }catch(IOException e){
     System.out.println("Error writting to log File" + e.getMessage());


 }
     // after print to console //
     System.out.println(logEntry);

     // Append to GUI log area if set
     if (logArea != null) {
         logArea.append(logEntry + "\n");
     }

 }
 public List<String> getLogHistory(){

     return new ArrayList<>(logHistory);//returning a copy to stop external modification//

 }
    @Override
    public void close(){
        try{
            if(writer != null){
                log("FileLogger is closing" + getCurrentTime());
                writer.close();
                writer = null;

            }
        }catch(Exception e){
            System.out.println("Error the FileLogger is closing" + e.getMessage());


        }
    }
    private String getCurrentTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(new Date ());

    }
    //method to get recent logs //

    public List<String> getRecentLogs (int count){
        if(count <= 0 || logHistory.isEmpty()){
            return new ArrayList<>();

        }

    if (count >= logHistory.size()){
       return new ArrayList<>(logHistory);

    }
     return new ArrayList<>(logHistory.subList(logHistory.size() - count, logHistory.size()));
    }

   // method to clear log history(clears memory but keeps files)
   public void clearHistory(){
       logHistory.clear();
       log("Log history is cleared" + getCurrentTime());


    }
   //checking if logger is active//
   public boolean isActive(){
       return writer != null;

   }

   // Set the JTextArea for GUI logging
   public void setLogArea(JTextArea logArea) {
       this.logArea = logArea;
   }

}
