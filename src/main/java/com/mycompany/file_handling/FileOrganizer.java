/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author MAGOBONGO SINAKO
 */
package com.mycompany.file_handling;
import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
//import java.util.List;//
    
public class FileOrganizer implements AutoCloseable {
    
    
    @Override
    public void close(){
        if (logger != null ){
            logger.log("FileOrganizer is shutting down");
            logger.close();
        }
    }

private String baseDirectory;
    private FileLogger logger;
    private List<OrganizationRule> organizationRules;
    private Stack<FileOperation> operationHistory;
    
    
    public String getBaseDirectory(){
        return baseDirectory;
    }
    
    
    //intutive constructor//
    
    public FileOrganizer(String baseDirectory,String logFile){
        this.baseDirectory = baseDirectory;
        this.logger = new FileLogger(logFile);
        this.organizationRules = new ArrayList<>();
        this.operationHistory = new Stack<>();
        
        setupDefaultRules();
        setupLogger();
        
        
        
    }
    public boolean undoLastOperation(){
        if (operationHistory.isEmpty()){
            logger.log("No operations to undo");
            return false;
              
        }
        FileOperation lastOp = operationHistory.pop();
        logger.log("Undoing the operation:" + lastOp);
        
        if (lastOp.undo()){
          logger.log("undone Successfully: " + lastOp.getActionType());
          return true;
}else{
            logger.log("Failing to undo:" + lastOp.getActionType());
            operationHistory.push(lastOp);
            return false;
            
            
        }
    }
    
    private void setupLogger() {
        
        
        try {
            
            
            logger.log("File organizer Log has Started");
            logger.log("FileOrganizer  is initialized for" + baseDirectory);
           
            
        }catch(Exception e) {
            System.out.println("Error setting up the Logger:" + e.getMessage());
            
            
            
        }
    }
    
    
    
 
     private void setupDefaultRules(){
         
       // Rules using OrganizationRule objects//
       
         organizationRules.add(new OrganizationRule(".jpg", "images"));
         organizationRules.add(new OrganizationRule(".jpeg", "images"));
         organizationRules.add(new OrganizationRule(".png", "images"));
         organizationRules.add(new OrganizationRule(".gif", "images"));
         organizationRules.add(new OrganizationRule(".bmp", "images"));
      
         organizationRules.add(new OrganizationRule(".pdf", "documents"));
         organizationRules.add(new OrganizationRule(".rtf", "documents"));
         organizationRules.add(new OrganizationRule(".doc", "documents"));
         organizationRules.add(new OrganizationRule(".docx", "documents"));
         organizationRules.add(new OrganizationRule(".txt", "documents"));
       
         organizationRules.add(new OrganizationRule(".mp4", "vedios"));
         organizationRules.add(new OrganizationRule(".avi", "vedios"));
         organizationRules.add(new OrganizationRule(".mov", "vedios"));
         organizationRules.add(new OrganizationRule(".wmv", "vedios"));
       
         organizationRules.add(new OrganizationRule(".mp3", "audio"));
         organizationRules.add(new OrganizationRule(".wav", "audio"));
         organizationRules.add(new OrganizationRule(".aac", "audio"));
         organizationRules.add(new OrganizationRule(".m4a",  "audio"));
       
         organizationRules.add(new OrganizationRule(".zip", "archives"));
         organizationRules.add(new OrganizationRule(".rar", "archives"));
         organizationRules.add(new OrganizationRule(".7z","archives"));
         
         organizationRules.add(new OrganizationRule(".java", "code"));
         organizationRules.add(new OrganizationRule(".py", "code"));
         organizationRules.add(new OrganizationRule(".js", "code"));
         organizationRules.add(new OrganizationRule(".html", "code"));
         organizationRules.add(new OrganizationRule(".css", "code"));
         
    
      logger.log("FileOrganizer initialized for: "+ baseDirectory);
      
      
}

public void organizeFiles(boolean dryRun){

File directory = new File(baseDirectory);
if (!directory.exists() || !directory.isDirectory()){
logger.log("Error : Directory is not found " + baseDirectory);
return;

}
 logger.log("File organization is starting:" + (dryRun ? "(Dry Run )": ""));


List<String> movedFiles = new ArrayList<>();
List<String> skippedFiles = new ArrayList<>();
List<String> errorFiles = new ArrayList <>();

try{

File[] files = directory.listFiles();
if (files != null){
for (File file : files){
if ( file.isFile()){
String result = processFile(file, dryRun);

switch (result){

case "moved" : movedFiles.add(file.getName()); break;
case "skipped" : skippedFiles.add(file.getName()); break;
case "error" : errorFiles.add(file.getName()); break;

             }
         }
     }
 }

showSummary(movedFiles,skippedFiles, errorFiles, dryRun);


}catch (Exception e){

logger.log ("Error during the organization: " + e.getMessage());

     }
}

    private String processFile(File file, boolean dryRun){
        
        try{
           
            String filename = file.getName();
            String extension = getFileExtension(filename);
            
            if (extension.isEmpty()){
                logger.log("skip : there is no found extension for" + filename);
                return "skipped";
                
            }
            String category = getFileCategory(extension);
            
            if (category.equals("unknown")){
                logger.log("Skip : Uknown type" + filename);
                return "skipped";
                
            }
            //target folder//
            File targetFolder = new File(baseDirectory + File.separator + category);
            // creating folder operation//
            if (!targetFolder.exists() && !dryRun){
                FileOperation folderOp = new FileOperation(targetFolder,null,targetFolder.getAbsolutePath(),FileOperation.ActionType.CREATE_FOLDER);
                if (folderOp.execute()){
                    operationHistory.push(folderOp);
                    logger.log("Created folder:" + category);
                    
                }   
            }
            
            File targetPath = new File(targetFolder, filename);
            File finalTargetPath = handleDuplicates(targetPath);
            
            
            
            if(!dryRun){
                //Creating and tracking file operation//
                FileOperation moveOp = new FileOperation(file,file.getAbsolutePath(),finalTargetPath.getAbsolutePath(),FileOperation.ActionType.MOVE);
                if (moveOp.execute()){
                    operationHistory.push(moveOp);
                    logger.log("Moved :"+ filename + "to" + category);
                    return "moved";
}else{
                    return "error";
                    
                }
}else{
                logger.log("Dry Run: Would move" + filename + " to " + category);
                return "moved";
            }
            // Moving the file//
}catch(Exception e){
    
    logger.log("Error processing " + file.getName() + ": " + e.getMessage());
    return "error";
    
}
    } 


    
    
    private String getFileCategory(String extension){
        
    for (OrganizationRule rule : organizationRules){
        if(rule.matches(extension)){
            return rule.getCategory();
            
        }
}
        return "unknown";
         }
        private File handleDuplicates(File filePath) throws IOException {
            if (!filePath.exists()){
                return filePath;
                
            }
            String baseName = filePath.getName();
            int lastDotIndex = baseName.lastIndexOf('.');
            String nameWithoutExt = baseName.substring(0, lastDotIndex);
            String extension = baseName.substring(lastDotIndex);
            
            File parentDir = filePath.getParentFile();
            
            int counter = 1;
            while (true){
                String newName = nameWithoutExt + "_" + counter + extension;
                File newFile = new File(parentDir, newName);
                if (!newFile.exists()){
                    return newFile;
                    
                    
                }
                counter++;
                
            }
}
        public void addCustomRule(String fileType, String category){
            OrganizationRule newRule = new OrganizationRule(fileType, category);
            
            organizationRules.add(newRule);
            logger.log("add custom rule : " + newRule);
            
           
         
    }
public void showRules() {


        logger.log("Current Organization Rules");
        for (OrganizationRule rule : organizationRules) {
        
        logger.log(rule.toString());
}
}
public OrganizationRule findMatchingRule (File file) {
 for (OrganizationRule rule : organizationRules) {
 if( rule.matches(file)){
     return rule;
}
}
return null;
}

   public List<OrganizationRule> getRulesCategory(String category) {
    
   List<OrganizationRule> categoryRules = new ArrayList<>();
   for (OrganizationRule rule : organizationRules) {
       
        if ( rule.getCategory().equalsIgnoreCase(category)){
            categoryRules.add(rule);
            
        }
        
        }
   return categoryRules;
   
    }
   public boolean removeRule(String fileType, String category){
OrganizationRule ruleToRemove = new OrganizationRule(fileType,category);
boolean removed = organizationRules.remove(ruleToRemove);
if (removed){
    logger.log("Removed the rule :" + ruleToRemove);
}
return removed;
}


    private String getFileExtension(String filename){
        
       int lastDotIndex = filename.lastIndexOf('.');
        if(lastDotIndex > 0){
            return filename.substring(lastDotIndex).toLowerCase();
            
            
            
        }
        return "";
        
    }


 
 
 private void showSummary(List<String> moved,List<String> skipped,List<String> errors,boolean isdryRun){
 
     String summary = "Organization Summary" + (isdryRun? "(Dry Run)" : "");
     summary += "Files Moved: " + moved.size();
     summary += "Files skipped: " + skipped.size();
     summary += "Errors: " + errors.size();
     
     if (!moved.isEmpty()){
         summary += "Moved files " + String.join(", ",moved );
         
     }
     logger.log(summary);
 }
 public Map<String, Object> getFilesInfo(String filename){
     Map<String, Object> info = new HashMap<>();
     File file = new File(baseDirectory + File.separator + filename);
     
     if(!file.exists()){
         info.put("error", "file does not exist");
         return info;
         
     }
     try {
         
       info.put("name", filename);
       info.put("size_bytes", file.length());
       info.put("size_readable", formatSize(file.length()));
       info.put("created",new Date (file.lastModified()));
       
       // using OrganizationRule to get file type info//
       
       OrganizationRule matchingRule = findMatchingRule(file);
       if(matchingRule != null){
           info.put("type", matchingRule.getCategory());
           info.put("file_type", matchingRule.getFileType());
} else {
           info.put("type", "uknown");
           info.put("file_type", getFileExtension(filename));
           
       }
         
         
     }catch (Exception e){
         
         info.put("error", e.getMessage());
         
     }
     return info;
     
 }
 private String formatSize(long sizeBytes){
     if(sizeBytes < 1024){
         return sizeBytes + " B";
     }else if (sizeBytes < 1024 * 1024){
         return String.format("%.2f KB",sizeBytes / 1024.0);
         
     } else if (sizeBytes < 1024 * 1024 * 1024){
         return String.format("%.2f MB",sizeBytes / (1024.0 * 1024.0));
         
     } else {
         return String.format("%.2f GB",sizeBytes / (1024.0 * 1024.0 * 1024.0));
         
     }
 }

 private String getCurrentTime(){
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy - MM - dd");
     return formatter.format(new Date());
}   
 }




      
 



  
 


