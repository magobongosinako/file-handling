/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author MAGOBONGO SINAKO
 */
package com.mycompany.file_handling;


import java.util.Scanner;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

public class MainApp {

  private FileOrganizer organizer;
  private Scanner scanner;
  
  public MainApp(String baseDirectory, String logFile){
      this.organizer = new FileOrganizer(baseDirectory, logFile);
      this.scanner = new Scanner(System.in);
      
      
  }
    public static void main(String[] args) {
      String baseDirectory = "./files";
      String logFile = "file_organizer.log";
      
      
      // create files directory Automatically//
      File filesDir = new File("./files");
      if(!filesDir.exists()){
          filesDir.mkdirs();
          System.out.println("Created directory :" + filesDir.getAbsolutePath());
      }
      //Aceept command line//
      if (args.length >= 1){
          baseDirectory = args[0];
          new File(baseDirectory).mkdirs();
      }
      if(args.length >= 2){
          logFile = args[1];
      }
      MainApp app = new MainApp(baseDirectory,logFile);
      app.run();
      
      
     
      
    }
    public void run(){
        
        System.out.println("=== FILE ORGANIZER APPLICATION ===");
        System.out.println("Organizing files in : " + organizer.getBaseDirectory());
         
        boolean running = true;
        while (running){
            displayMenu();
            int choice = getMenuChoice();
            running = handleMenuChoice(choice);
            
        }
        scanner.close();
        System.out.println("Thanks for using File Organizer");
        
    }
    private void displayMenu(){
        
        
        System.out.println("\n === MAIN MENU ===");
        System.out.println("1. Organize Files (Dry Run)");
        System.out.println("2. Organize Files (Actual Execution)");
        System.out.println("3. Undo Last Operation");
        System.out.println("4  Show Organization Rules");
        System.out.println("5. Add Custom Rule");
        System.out.println("6. Remove Rule ");
        System.out.println("7. Show Operation history");
        System.out.println("8. Get File Information");
        System.out.println("9. Clear Operation History");
        System.out.println("10. Exit");
        System.out.println("Enter your choice (1-10): ");
        
    }
    private void organizeFiles(boolean dryRun){
        System.out.println(dryRun ? "\n ---DRY RUN MODE ---" : "\n --- ORGANIZING FILES ---");
        organizer.organizeFiles(dryRun);// calls organizer's method//
         
        if (dryRun){
            System.out.println("Dry run completed.check the log for Details");
}else{
            System.out.println("File organization Completed");
            
        }
        waitForEnter();
        
    }
    private void undoLastOperation(){
        System.out.println("\n --- UNDO LAST OPERATION ---");
        boolean success = organizer.undoLastOperation();
        if (success){
            System.out.println("Operation undone successfully!");
}else{
            System.out.println("undo failed");
            
        }
        waitForEnter();
        
    }
    private void showRules(){
        System.out.println("\n --- CURRENT ORGANIZATION RULES---");
        organizer.showRules();
        waitForEnter();
    }
    
    private void addCustomRule(){
        System.out.println("\n --- ADD CUSTOM RULE ---");
        System.out.print("Enter File extension (e.g., .json, .xml):");
        String fileType = scanner.nextLine().trim();
        System.out.print("Enter category name:");
        String category = scanner.nextLine().trim();
        
        organizer.addCustomRule(fileType, category);
        System.out.println("Custom rule added:" + fileType + "" +category);
        waitForEnter();
        
    }
    private void RemoveRule(){
        System.out.println("\n ---REMOVE RULE ---");
        System.out.println("Enter the file extension to remove");
        String fileType = scanner.nextLine().trim();
        System.out.print("Enter category");
        String category = scanner.nextLine().trim();
        
        boolean removed = organizer.removeRule(fileType, category);
        if (removed){
            System.out.println("Rule removed successfully");
}else{
            System.out.println("Removal failed.");
            
            
        }
        waitForEnter();
        
    }
    private void showOperationHistory(){
        System.out.println("\n --- OPERATION HISTORY ---");
        System.out.println("Operation history feature coming soon!");
        waitForEnter();
        
    }
    private void getFileInfo(){
        System.out.println("\n --- FILE INFORMATION ---");
        System.out.println("Enter filename:");
        String filename = scanner.nextLine().trim();
        
        Map<String, Object> info = organizer.getFilesInfo(filename);
        System.out.println("File Information: ");
        for (Map.Entry<String, Object> entry : info.entrySet()){
            System.out.println("" + entry.getKey() + ": " + entry.getValue());
            
        }
        waitForEnter();
    }
    private void ClearOperationHistory(){
        System.out.println("\n ---CLEAR OPERATION");
        System.out.println("Operation history cleared!");
        waitForEnter();
        
    }
    private void waitForEnter(){
        System.out.print("\n Press Enter to continue ");
        scanner.nextLine();
    }
    
    
    
    
    private int getMenuChoice(){
        
        try{
         return Integer.parseInt(scanner.nextLine().trim());
         
        }catch(NumberFormatException e){
            return -1; //invalid choice//
            
        }
    }
    
    private boolean handleMenuChoice(int choice){
        switch (choice){
            case 1:
            organizeFiles(true);//Dry Run//
            break;
            
            case 2:
                organizeFiles(false);//Actual Execution//
                break;
                
            case 3:
                undoLastOperation();
                break;
                
                case 4:
                   showRules();
                break;
                
                case 5:
                    addCustomRule();
                    break;
                    
                case 6:
                    
                    RemoveRule();
                    break;
                    
                case 7:
                    
                showOperationHistory();
                break;
                
                case 8:
                    getFileInfo();
                    break;
                    
                    
                case 9:
                    
                    ClearOperationHistory();
                    break;
                    
                case 10:
                    return false; //Exit the Application//
                    
                default:
                    System.out.println("Invalid choice! Please enter a number that is between 1-10");
                    
                
            }
           return true;
    }

}
    