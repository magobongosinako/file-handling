/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author MAGOBONGO SINAKO
 */
package org.smartfile;

import java.nio.file.Path;

/**
 * Represents an operation performed on a file (move, rename, etc.).
 * Useful for tracking actions and enabling undo functionality later.
 */
public class FileOperation {
    private final Path oldPath;
    private final Path newPath;
    private final String actionType;

    public FileOperation(Path oldPath, Path newPath, String actionType) {
        this.oldPath = oldPath;
        this.newPath = newPath;
        this.actionType = actionType;
    }

    public Path getOldPath() { return oldPath; }
    public Path getNewPath() { return newPath; }
    public String getActionType() { return actionType; }

    @Override
    public String toString() {
        return actionType + ": " + oldPath + " -> " + newPath;
    }
}

