/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author MAGOBONGO SINAKO

*/
package com.mycompany.file_handling;
import java.nio.file.Path;

/**
 * Represents a file's metadata in the system.
 * Stores path, size, and assigned category.
 */
public class FileMeta {
    private final Path path;
    private final long size;
    private String category;

    public FileMeta(Path path, long size) {
        this.path = path;
        this.size = size;
    }

    public Path getPath() { return path; }
    public long getSize() { return size; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public String toString() {
        return path.getFileName() + " (" + size + " bytes) -> " + category;
    }
}

