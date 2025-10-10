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
import java.util.*;

public class OrganizationRule {
    
    public enum RuleType {
        EXTENSION, NAME_CONTAINS, SIZE
    }
    
    private String fileType; // For EXTENSION
    private String keyword;  // For NAME_CONTAINS
    private long minSize;    // For SIZE
    private long maxSize;    // For SIZE
    private String category;
    private RuleType ruleType;
    
    // Constructor for EXTENSION rule
    public OrganizationRule(String fileType, String category) {
        this.ruleType = RuleType.EXTENSION;
        setFileType(fileType);
        setCategory(category);
    }
    
    // Factory methods for different rule types
    public static OrganizationRule byExtension(String fileType, String category) {
        return new OrganizationRule(fileType, category);
    }
    
    public static OrganizationRule byNameContains(String keyword, String category) {
        OrganizationRule rule = new OrganizationRule("", category);
        rule.ruleType = RuleType.NAME_CONTAINS;
        rule.keyword = keyword != null ? keyword.trim().toLowerCase() : "";
        return rule;
    }
    
    public static OrganizationRule bySize(long minSize, long maxSize, String category) {
        OrganizationRule rule = new OrganizationRule("", category);
        rule.ruleType = RuleType.SIZE;
        rule.minSize = minSize;
        rule.maxSize = maxSize;
        return rule;
    }
    
    public String getFileType() {
        return fileType;
    }
    
    public void setFileType(String fileType) {
        if (fileType != null && !fileType.trim().isEmpty()) {
            String processedType = fileType.trim().toLowerCase();
            if (!processedType.startsWith(".")) {
                processedType = "." + processedType;
            }
            this.fileType = processedType;
        } else {
            this.fileType = "";
        }
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        if (category != null && !category.trim().isEmpty()) {
            this.category = category.trim();
        } else {
            this.category = "unknown";
        }
    }
    
    public String getRuleName() {
        switch (ruleType) {
            case EXTENSION:
                return "Extension: " + fileType + " -> " + category;
            case NAME_CONTAINS:
                return "Name Contains: " + keyword + " -> " + category;
            case SIZE:
                return "Size: " + minSize + "-" + maxSize + " bytes -> " + category;
            default:
                return "Unknown Rule";
        }
    }
    
    public boolean matches(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return false;
        }
        
        switch (ruleType) {
            case EXTENSION:
                String filename = file.getName().toLowerCase();
                return filename.endsWith(fileType);
            case NAME_CONTAINS:
                return file.getName().toLowerCase().contains(keyword);
            case SIZE:
                long size = file.length();
                return size >= minSize && size <= maxSize;
            default:
                return false;
        }
    }
    
    public boolean matches(String fileExtension) {
        if (ruleType != RuleType.EXTENSION || fileExtension == null || fileExtension.isEmpty()) {
            return false;
        }
        
        String extensionToCheck = fileExtension.toLowerCase();
        String ruleTypeToCheck = this.fileType.toLowerCase();
        
        if (!extensionToCheck.startsWith(".")) {
            extensionToCheck = "." + extensionToCheck;
        }
        return extensionToCheck.equals(ruleTypeToCheck);
    }
    
    public String getTargetFolder() {
        return category;
    }
    
    @Override
    public String toString() {
        return getRuleName();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        OrganizationRule that = (OrganizationRule) obj;
        return ruleType == that.ruleType &&
               Objects.equals(fileType, that.fileType) &&
               Objects.equals(keyword, that.keyword) &&
               minSize == that.minSize &&
               maxSize == that.maxSize &&
               Objects.equals(category, that.category);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(ruleType, fileType, keyword, minSize, maxSize, category);
    }
}
