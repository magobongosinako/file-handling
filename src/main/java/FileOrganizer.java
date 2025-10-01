/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author MAGOBONGO SINAKO
 */


import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

/**
 * Core logic:
 * - Scan files in the source directory (recursively).
 * - Classify each file using OrganizationRule.
 * - Move file to the target directory under the correct category.
 * - Log each operation using FileLogger.
 */
public class FileOrganizer {
    private final Path source;
    private final Path target;
    private final OrganizationRule rule;
    private final FileLogger logger;

    public FileOrganizer(Path source, Path target, OrganizationRule rule, FileLogger logger) {
        this.source = source;
        this.target = target;
        this.rule = rule;
        this.logger = logger;
    }

    public void organizeFiles() throws IOException {
        try (Stream<Path> paths = Files.walk(source)) {
            paths.filter(Files::isRegularFile).forEach(file -> {
                try {
                    FileMeta meta = new FileMeta(file, Files.size(file));
                    String category = rule.classify(meta);
                    meta.setCategory(category);

                    Path dest = target.resolve(category).resolve(file.getFileName());
                    Utils.safeMove(file, dest);

                    logger.log("MOVE", file.toString(), dest.toString());
                    System.out.println("Moved: " + meta);
                } catch (Exception e) {
                    System.err.println("Failed: " + file + " | " + e.getMessage());
                }
            });
        }
    }
}

