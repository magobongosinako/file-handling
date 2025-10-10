package com.mycompany.file_handling;

import java.text.SimpleDateFormat;
import java.io.*;
import java.util.*;
import javax.swing.JTextArea;

public class FileLogger implements AutoCloseable {
 private String logFilePath;
    private FileWriter writer;
    private List<String> logHistory;

    public FileLogger(String logFilePath) {
        this.logFilePath = logFilePath;
        this.logHistory = new ArrayList<>();
        initializeLogger();
    }

    private void initializeLogger() {
        try {
            File logFile = new File(logFilePath);
            File parentDir = logFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            this.writer = new FileWriter(logFile, true);
            logHistory.add("FileLogger initialized " + getCurrentTime());
        } catch (Exception e) {
            System.out.println("Error initializing FileLogger: " + e.getMessage());
        }
    }
    
    public void log(String message) {
        String timestamp = getCurrentTime();
        String logEntry = "[" + timestamp + "] " + message;
        logHistory.add(logEntry);
        
        try {
            if (writer != null) {
                writer.write(logEntry + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            System.out.println("Error writing to log file: " + e.getMessage());
        }
        System.out.println(logEntry);
    }
    
    public List<String> getLogHistory() {
        return new ArrayList<>(logHistory);
    }
    
    public List<String> readLogsFromFile() {
        List<String> logs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logs.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading log file: " + e.getMessage());
        }
        return logs;
    }
    
    public void clearLogs() {
        logHistory.clear();
        try {
            new FileWriter(logFilePath, false).close(); // Clears the file
            log("Log file cleared");
        } catch (IOException e) {
            System.out.println("Error clearing log file: " + e.getMessage());
        }
    }
    
    @Override
    public void close() {
        try {
            if (writer != null) {
                log("FileLogger is closing " + getCurrentTime());
                writer.close();
                writer = null;
            }
        } catch (Exception e) {
            System.out.println("Error closing FileLogger: " + e.getMessage());
        }
    }
    
    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(new Date());
    }
    
    public List<String> getRecentLogs(int count) {
        if (count <= 0 || logHistory.isEmpty()) {
            return new ArrayList<>();
        }
        
        if (count >= logHistory.size()) {
            return new ArrayList<>(logHistory);
        }
        return new ArrayList<>(logHistory.subList(logHistory.size() - count, logHistory.size()));
    }
    
    public void clearHistory() {
        logHistory.clear();
        log("Log history cleared " + getCurrentTime());
    }
    
    public boolean isActive() {
        return writer != null;
    

}
