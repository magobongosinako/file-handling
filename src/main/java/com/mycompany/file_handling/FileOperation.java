/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author MAGOBONGO SINAKO
 */
package com.mycompany.file_handling;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FileOperation {
    
    public enum ActionType {
        MOVE, DELETE, CREATE_FOLDER, RENAME
        
    }
    private File file;
    private String oldPath;
    private String newPath;
    private ActionType actionType;
    private boolean executed;
    private String timestamp;
    
    
    public FileOperation(File file, String oldPath, String newPath, ActionType actionType){
        this.file = file;
        this.oldPath = oldPath;
        this.actionType = actionType;
        this.newPath = newPath;
        this.executed = false;
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd  HH :mm : ss").format(new Date());
        
        // execute the operation//
          
        
    }
    public ActionType getActionType(){
        return actionType;
    }
    
    public boolean execute(){
        
      try{
        
          switch (actionType){
              case MOVE:
                  Files.move(Path.of(oldPath), Path.of(newPath), StandardCopyOption.REPLACE_EXISTING); break;
              case CREATE_FOLDER: new File(newPath).mkdirs();break;
              case DELETE: Files.deleteIfExists(Path.of(oldPath));break;
              case RENAME: Files.move(Path.of(oldPath), Path.of(newPath));break;
              
          }
          this.executed = true;
          return true;
          
    
      } catch(Exception e){
         System.out.println("Execution Error:" + e.getMessage());
         return false;
         
      } 
        
    }
    public boolean undo(){
        
        try{
          switch (actionType){
              case MOVE: Files.move(Path.of(newPath), Path.of(oldPath), StandardCopyOption.REPLACE_EXISTING);break;
              case CREATE_FOLDER: deleteFolder(new File(newPath)); break;
              case DELETE: new File(oldPath).createNewFile();break;
              case RENAME: Files.move(Path.of(newPath), Path.of(oldPath));break;
              
          } 
            this.executed = false;
            return true;
            
        }catch(Exception e){
            System.out.println("Error undoing the operation:" + e.getMessage());
            
            return false;
            
        }
    }
    //Method t delete folders recursively//
    private void deleteFolder(File folder){
        if (folder.isDirectory()){
            File[] files = folder.listFiles();
            if (files != null){
                for(File fle : files){
                    deleteFolder(fle);
                    
                }
            }
        }
        folder.delete();
    }
    public File getFile(){return file; }
    public String getOldPath(){ return oldPath; }
    public String getNewPath(){ return newPath;}
    public boolean isExecuted(){ return executed; }
    public String getTimestamp(){ return timestamp; }
    
}
