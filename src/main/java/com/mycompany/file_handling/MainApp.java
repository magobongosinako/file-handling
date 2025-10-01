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
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Entry point.
 * Simple console-based app that asks for source and target folders.
 */
public class MainApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter source directory: ");
        Path source = Paths.get(scanner.nextLine());

        System.out.print("Enter target directory: ");
        Path target = Paths.get(scanner.nextLine());

        OrganizationRule rule = new OrganizationRule();
        FileLogger logger = new FileLogger();
        FileOrganizer organizer = new FileOrganizer(source, target, rule, logger);

        try {
            organizer.organizeFiles();
            System.out.println("Organization complete. Check 'organizer.log' for details.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

