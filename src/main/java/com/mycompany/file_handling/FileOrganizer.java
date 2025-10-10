package com.mycompany.file_handling;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class FileOrganizer implements AutoCloseable {

    private String baseDirectory;
    private FileLogger logger;
    private List<OrganizationRule> organizationRules;
    private Stack<FileOperation> operationHistory;
    private List<String> operationLog;
    private int filesProcessed;
    private int filesOrganized;
    
    public FileOrganizer(String baseDirectory, String logFile) {
        this.baseDirectory = baseDirectory;
        this.logger = new FileLogger(logFile);
        this.organizationRules = new ArrayList<>();
        this.operationHistory = new Stack<>();
        this.operationLog = new ArrayList<>();
        this.filesProcessed = 0;
        this.filesOrganized = 0;
        
        setupDefaultRules();
        setupLogger();
    }
    
    public String getSourceDirectory() {
        return baseDirectory;
    }
    
    public void setSourceDirectory(String directory) {
        this.baseDirectory = directory;
        logger.log("Source directory changed to: " + directory);
    }
    
    public int getFilesProcessed() {
        return filesProcessed;
    }
    
    public int getFilesOrganized() {
        return filesOrganized;
    }
    
    @Override
    public void close() {
        if (logger != null) {
            logger.log("FileOrganizer is shutting down");
            logger.close();
        }
    }
    
    public String getBaseDirectory() {
        return baseDirectory;
    }
    
    public boolean undoLastOperation() {
        if (operationHistory.isEmpty()) {
            logger.log("No operations to undo");
            logOperation("No operations: Failed - No operations to undo");
            return false;
        }
        FileOperation lastOp = operationHistory.pop();
        logger.log("Undoing the operation: " + lastOp);
        
        if (lastOp.undo()) {
            logger.log("Undone Successfully: " + lastOp.getActionType());
            logOperation("Undo operation: Success - " + lastOp.getActionType());
            return true;
        } else {
            logger.log("Failed to undo: " + lastOp.getActionType());
            operationHistory.push(lastOp);
            logOperation("Undo operation: Failed - " + lastOp.getActionType());
            return false;
        }
    }
    
    public boolean undoAllOperations(){
        if(operationHistory.isEmpty()){
            logger.log("No operations to undo");
            logOperation("Bulk undo: No operations to undo");
            return false;
        }
        System.out.println("\n --- BULK UNDO ---");
        System.out.println("Undoing " + operationHistory.size() + " operations");
        
        
        boolean allSuccessful = true;
        int successCount = 0;
        int failCount = 0;
        
        // Undo all operations in reverse(Last in - first out)
        while(!operationHistory.isEmpty()){
            FileOperation operation = operationHistory.pop();
            logger.log("Undoing " + operation);
            
            if (operation.undo()){
                successCount++;
                logger.log("Undone sucessfully:" + operation.getActionType());
}else{
                failCount++;
                failCount++;
                logger.log("Failed to undo:" + operation.getActionType());
                allSuccessful = false;
            }
        }
        String result = String.format("Bulk undo completed: %s sucessful,%d failed",successCount, failCount);
        System.out.println(result);
        logOperation(result);
        
        return allSuccessful;
    }
    
    private void setupLogger() {
        try {
            logger.log("File organizer Log has Started");
            logger.log("FileOrganizer is initialized for " + baseDirectory);
        } catch (Exception e) {
            System.out.println("Error setting up the Logger: " + e.getMessage());
        }
    }
    
    private void setupDefaultRules() {
        organizationRules.add(OrganizationRule.byExtension(".jpg", "images"));
        organizationRules.add(OrganizationRule.byExtension(".jpeg", "images"));
        organizationRules.add(OrganizationRule.byExtension(".png", "images"));
        organizationRules.add(OrganizationRule.byExtension(".gif", "images"));
        organizationRules.add(OrganizationRule.byExtension(".bmp", "images"));
        
        organizationRules.add(OrganizationRule.byExtension(".pdf", "documents"));
        organizationRules.add(OrganizationRule.byExtension(".rtf", "documents"));
        organizationRules.add(OrganizationRule.byExtension(".doc", "documents"));
        organizationRules.add(OrganizationRule.byExtension(".docx", "documents"));
        organizationRules.add(OrganizationRule.byExtension(".txt", "documents"));
        
        organizationRules.add(OrganizationRule.byExtension(".mp4", "videos"));
        organizationRules.add(OrganizationRule.byExtension(".avi", "videos"));
        organizationRules.add(OrganizationRule.byExtension(".mov", "videos"));
        organizationRules.add(OrganizationRule.byExtension(".wmv", "videos"));
        
        organizationRules.add(OrganizationRule.byExtension(".mp3", "audio"));
        organizationRules.add(OrganizationRule.byExtension(".wav", "audio"));
        organizationRules.add(OrganizationRule.byExtension(".aac", "audio"));
        organizationRules.add(OrganizationRule.byExtension(".m4a", "audio"));
        
        organizationRules.add(OrganizationRule.byExtension(".zip", "archives"));
        organizationRules.add(OrganizationRule.byExtension(".rar", "archives"));
        organizationRules.add(OrganizationRule.byExtension(".7z", "archives"));
        
        organizationRules.add(OrganizationRule.byExtension(".java", "code"));
        organizationRules.add(OrganizationRule.byExtension(".py", "code"));
        organizationRules.add(OrganizationRule.byExtension(".js", "code"));
        organizationRules.add(OrganizationRule.byExtension(".html", "code"));
        organizationRules.add(OrganizationRule.byExtension(".css", "code"));
        
        logger.log("FileOrganizer initialized for: " + baseDirectory);
    }
    
    public void addRule(OrganizationRule rule) {
        organizationRules.add(rule);
        logOperation("Added rule: " + rule.getRuleName());
        logger.log("Added rule: " + rule);
    }
    
    public void organizeFiles() {
        organizeFiles(false);
    }
    
    public void organizeFiles(boolean dryRun) {
        filesProcessed = 0;
        filesOrganized = 0;
        
        File directory = new File(baseDirectory);
        if (!directory.exists() || !directory.isDirectory()) {
            logger.log("Error: Directory not found " + baseDirectory);
            logOperation("File organization FAILED: Directory not found - " + baseDirectory);
            return;
        }
        
        logOperation("File organization: " + (dryRun ? "Dry Run" : "Actual Execution"));
        logger.log("File organization is starting: " + (dryRun ? "(Dry Run)" : ""));
        
        List<String> movedFiles = new ArrayList<>();
        List<String> skippedFiles = new ArrayList<>();
        List<String> errorFiles = new ArrayList<>();
        
        try {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        filesProcessed++;
                        String result = processFile(file, dryRun);
                        switch (result) {
                            case "moved" -> {
                                movedFiles.add(file.getName());
                                filesOrganized++;
                            }
                            case "skipped" -> skippedFiles.add(file.getName());
                            case "error" -> errorFiles.add(file.getName());
                        }
                    }
                }
            }
            showSummary(movedFiles, skippedFiles, errorFiles, dryRun);
        } catch (Exception e) {
            logger.log("Error during the organization: " + e.getMessage());
        }
    }
    
    private String processFile(File file, boolean dryRun) {
        try {
            String filename = file.getName();
            String extension = getFileExtension(filename);
            
            if (extension.isEmpty()) {
                logger.log("Skip: No extension found for " + filename);
                return "skipped";
            }
            
            String category = getFileCategory(file);
            
            if (category.equals("unknown")) {
                logger.log("Skip: Unknown type " + filename);
                return "skipped";
            }
            
            File targetFolder = new File(baseDirectory + File.separator + category);
            if (!targetFolder.exists() && !dryRun) {
                FileOperation folderOp = new FileOperation(targetFolder, null, targetFolder.getAbsolutePath(), FileOperation.ActionType.CREATE_FOLDER);
                if (folderOp.execute()) {
                    operationHistory.push(folderOp);
                    logger.log("Created folder: " + category);
                }
            }
            
            File targetPath = new File(targetFolder, filename);
            File finalTargetPath = handleDuplicates(targetPath);
            
            if (!dryRun) {
                FileOperation moveOp = new FileOperation(file, file.getAbsolutePath(), finalTargetPath.getAbsolutePath(), FileOperation.ActionType.MOVE);
                if (moveOp.execute()) {
                    operationHistory.push(moveOp);
                    logger.log("Moved: " + filename + " to " + category);
                    return "moved";
                } else {
                    return "error";
                }
            } else {
                logger.log("Dry Run: Would move " + filename + " to " + category);
                return "moved";
            }
        } catch (IOException e) {
            logger.log("Error processing " + file.getName() + ": " + e.getMessage());
            return "error";
        }
    }
    
    private String getFileCategory(File file) {
        for (OrganizationRule rule : organizationRules) {
            if (rule.matches(file)) {
                return rule.getCategory();
            }
        }
        return "unknown";
    }
    
    private File handleDuplicates(File filePath) throws IOException {
        if (!filePath.exists()) {
            return filePath;
        }
        String baseName = filePath.getName();
        int lastDotIndex = baseName.lastIndexOf('.');
        String nameWithoutExt = baseName.substring(0, lastDotIndex);
        String extension = baseName.substring(lastDotIndex);
        
        File parentDir = filePath.getParentFile();
        
        int counter = 1;
        while (true) {
            String newName = nameWithoutExt + "_" + counter + extension;
            File newFile = new File(parentDir, newName);
            if (!newFile.exists()) {
                return newFile;
            }
            counter++;
        }
    }
    
    public void addCustomRule(String fileType, String category) {
        OrganizationRule newRule = OrganizationRule.byExtension(fileType, category);
        addRule(newRule);
    }
    
    public void showRules() {
        logger.log("Current Organization Rules");
        for (OrganizationRule rule : organizationRules) {
            logger.log(rule.toString());
        }
    }
    
    public OrganizationRule findMatchingRule(File file) {
        for (OrganizationRule rule : organizationRules) {
            if (rule.matches(file)) {
                return rule;
            }
        }
        return null;
    }
    
    public List<OrganizationRule> getRulesCategory(String category) {
        List<OrganizationRule> categoryRules = new ArrayList<>();
        for (OrganizationRule rule : organizationRules) {
            if (rule.getCategory().equalsIgnoreCase(category)) {
                categoryRules.add(rule);
            }
        }
        return categoryRules;
    }
    
    public boolean removeRule(String fileType, String category) {
        OrganizationRule ruleToRemove = OrganizationRule.byExtension(fileType, category);
        boolean removed = organizationRules.remove(ruleToRemove);
        if (removed) {
            logger.log("Removed the rule: " + ruleToRemove);
            logOperation("Removed rule: " + fileType + " " + category);
        }
        return removed;
    }
    
    public boolean removeRule(OrganizationRule rule) {
        boolean removed = organizationRules.remove(rule);
        if (removed) {
            logger.log("Removed the rule: " + rule);
            logOperation("Removed rule: " + rule.getRuleName());
        }
        return removed;
    }
    
    private void logOperation(String action) {
        if (operationLog != null) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String logEntry = "[" + timestamp + "] " + action;
            operationLog.add(0, logEntry);
            
            if (operationLog.size() > 100) {
                operationLog = new ArrayList<>(operationLog.subList(0, 100));
            }
        }
    }
    
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return filename.substring(lastDotIndex).toLowerCase();
        }
        return "";
    }
    
    private void showSummary(List<String> moved, List<String> skipped, List<String> errors, boolean isDryRun) {
        String summary = "Organization Summary" + (isDryRun ? " (Dry Run)" : "");
        summary += "\nFiles Moved: " + moved.size();
        summary += "\nFiles Skipped: " + skipped.size();
        summary += "\nErrors: " + errors.size();
        
        if (!moved.isEmpty()) {
            summary += "\nMoved files: " + String.join(", ", moved);
        }
        logger.log(summary);
    }
    
  
    
    private String formatSize(long sizeBytes) {
        if (sizeBytes < 1024) {
            return sizeBytes + " B";
        } else if (sizeBytes < 1024 * 1024) {
            return String.format("%.2f KB", sizeBytes / 1024.0);
        } else if (sizeBytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", sizeBytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", sizeBytes / (1024.0 * 1024.0 * 1024.0));
        }
    }
    
    
    public void debugDirectoryContents() {
        File directory = new File(baseDirectory);
        System.out.println("\n=== DEBUG CONTENTS ===");
        System.out.println("Base directory: " + baseDirectory);
        System.out.println("Directory exists: " + directory.exists());
        System.out.println("Is directory: " + directory.isDirectory());
        
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                System.out.println("Folders found: " + files.length);
                for (File file : files) {
                    System.out.println("- " + file.getName() + " (File: " + file.isFile() + ")");
                }
            } else {
                System.out.println("Unable to read directory!");
            }
        }
    }
    
    public void debugAllFilesRecursively() {
        System.out.println("\n=== ALL FILES IN ALL FOLDERS ===");
        System.out.println("Base directory: " + baseDirectory);
        int fileCount = listAllFilesRecursively(new File(baseDirectory), "");
        System.out.println("Total files found: " + fileCount);
    }
    
    public List<String> getOperationHistory() {
        return new ArrayList<>(operationLog);
    }
    
    private int listAllFilesRecursively(File directory, String path) {
        int count = 0;
        if (!directory.exists() || !directory.isDirectory())
            return 0;
        
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println("- " + path + file.getName());
                    count++;
                } else if (file.isDirectory()) {
                    count += listAllFilesRecursively(file, path + file.getName() + "/");
                }
            }
        }
        return count;
    }
    
    public void clearOperationHistory() {
        if (operationLog != null) {
            operationLog.clear();
            logger.log("Operation history cleared");
            logOperation("Operation history cleared");
        }
        if (operationHistory != null) {
            operationHistory.clear();
            logger.log("Undo history cleared");
        }
        System.out.println("All operation history has been cleared successfully.");
    }
    
    public void showOperationHistory() {
        System.out.println("\n--- OPERATION HISTORY ---");
        if (operationLog == null) {
            System.out.println("Operation Log is not initialized.");
            return;
        }
        
        if (operationLog.isEmpty()) {
            System.out.println("No operations recorded yet");
            System.out.println("Use other features to see operations here");
        } else {
            System.out.println("Total operations: " + operationLog.size());
            for (String operation : operationLog) {
                System.out.println(operation);
            }
        }
    }
 }
