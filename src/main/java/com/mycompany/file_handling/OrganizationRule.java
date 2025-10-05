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
import java.util.*;

public class OrganizationRule {
    
    private String fileType;
    private String category;
    
    // constructor//
    
    public OrganizationRule(String fileType, String category){
        
        setFileType(fileType);
        setCategory(category);
        
        
    }
    
    public String getFileType(){
        return fileType;
        
    }
    public void setFileType(String fileType){
        if (fileType != null && !fileType.trim().isEmpty()){
            String processedType = fileType.trim().toLowerCase();
            if (!processedType.startsWith(".")){
                processedType = "." + processedType;
                
                
            }
            this.fileType = processedType;
        }else {
            this.fileType = "";
            
        }
    }
    public String getCategory(){
        return category;
        
    }
    public void setCategory(String category){
        if (category != null && !category.trim().isEmpty()){
            this.category = category.trim();
        } else {
            this.category = "unknown";
            
        }
    }
    //checking if the rule matches the given file//
    public boolean matches (File file){
        if (file == null || !file.exists() || file.isDirectory()){
            return false;
            
        }
        String filename = file.getName().toLowerCase();
        return filename.endsWith(fileType);
        
    }
    //checking if the rule matches file extension//
    
    public boolean matches (String fileExtension){
        if (fileExtension == null || fileExtension.isEmpty()){
            return false;
            
        }
        //Ensuring the extension has a dot for comparing//
        
        String extensionTocheck = fileExtension.toLowerCase();
        String ruleTypeTocheck = this.fileType.toLowerCase();
        
        if (!extensionTocheck.startsWith(".")){
            extensionTocheck = "." + extensionTocheck;
            
        }
        return extensionTocheck.equals(ruleTypeTocheck);
           
    }
    //getting the target folder for the file based on the rule//
    public String getTargetFolder(){
        return category;
        
    }
    // Use toString method for displaying//
    
    public String toString(){
        return "OrganizationRule{fileType='"   + fileType + "', category='" + category + "'}";
        
    }
    
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
        return false;
        OrganizationRule that = (OrganizationRule) obj;
        return Objects.equals(fileType, that.fileType) && Objects.equals(category, that.category);
        
        
    } 
    public int hashCode(){
        return Objects.hash(fileType,category);
        
    }
}
