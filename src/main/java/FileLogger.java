/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author MAGOBONGO SINAKO
 */



import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Logs operations performed by the system.
 * Appends to "organizer.log".
 */
package com.mycompany.file_handling;

public class FileLogger {
    private final String logFile = "organizer.log";

    public void log(String action, String src, String dest) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            String line = LocalDateTime.now() + " | " + action + " | " + src + " -> " + dest;
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to write log: " + e.getMessage());
        }
    }
}

