/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author MAGOBONGO SINAKO
 */


public class OrganizationRule {
    public String classify(FileMeta meta) {
        String name = meta.getPath().getFileName().toString().toLowerCase();

        if (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg")) {
            return "Images";
        } else if (name.endsWith(".mp4") || name.endsWith(".mkv") || name.endsWith(".avi")) {
            return "Videos";
        } else if (name.endsWith(".pdf") || name.endsWith(".docx") || name.endsWith(".txt")) {
            return "Documents";
        }
        return "Others";
    }
}

