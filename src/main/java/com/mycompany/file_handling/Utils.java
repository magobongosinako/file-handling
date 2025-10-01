/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author MAGOBONGO SINAKO
 */
package com.mycompany.file_handling;

import java.io.IOException;
import java.nio.file.*;

/**
 * Utility methods for file movement and conflict resolution.
 */

public class Utils {
    public static void safeMove(Path src, Path dest) throws IOException {
        if (Files.exists(dest)) {
            dest = resolveConflict(dest);
        }
        Files.createDirectories(dest.getParent()); // ensure category folder exists
        Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING);
    }

    private static Path resolveConflict(Path dest) {
        String name = dest.getFileName().toString();
        String base = name;
        String ext = "";
        int dot = name.lastIndexOf('.');
        if (dot != -1) {
            base = name.substring(0, dot);
            ext = name.substring(dot);
        }

        int counter = 1;
        Path parent = dest.getParent();
        Path newPath;
        do {
            newPath = parent.resolve(base + "(" + counter + ")" + ext);
            counter++;
        } while (Files.exists(newPath));
        return newPath;
    }
}

