package com.mycompany.file_handling;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author MAGOBONGO SINAKO
 */
import java.io.File;

public class MainApp {

    public MainApp(String baseDirectory, String logFile) {
        new FileOrganizer(baseDirectory, logFile);
    }
    
    public static void main(String[] args) {
        String baseDirectory = "./files";
        String logFile = "file_organizer.log";
        
        // Create files directory automatically
        File filesDir = new File("./files");
        if (!filesDir.exists()) {
            filesDir.mkdirs();
            System.out.println("Created directory: " + filesDir.getAbsolutePath());
        }
        
        // Accept command line arguments
        if (args.length >= 1) {
            baseDirectory = args[0];
            new File(baseDirectory).mkdirs();
        }
        if (args.length >= 2) {
            logFile = args[1];
        }
        
        FileOrganizer organizer = new FileOrganizer(baseDirectory, logFile);
        FileLogger logger = new FileLogger(logFile);
        SwingFileOrganizerApp gui = new SwingFileOrganizerApp(organizer, logger);
        gui.display();
    }
  
}
    
