/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.java.michal.jakobczyk.lab3.model;

import java.io.*;

/**
 * Validates user's parameters
 * @author Michał Jakóbczyk
 * @version 3.0
 */
public class MainMenuModel {
    
    /**
     * Variable that says if the user input is needed
     * in case program parameters are not provided
     */
    private boolean userInputAccess;
    
    /**
     * Default constructor. Doesnt allow user input
     * if it was not needed
     */
    public MainMenuModel() {
        this.userInputAccess = false;
    }
    
    /**
     * Allows user to pass the parameters to the application
     */
    public void setUserInputAccess() {
        this.userInputAccess = true;
    }
    
    /**
     * Getter of the user input flag
     * @return true if the user can pass parameters in the runtime
     */
    public boolean getUserInputAccess() {
        return this.userInputAccess;
    }
    
    /** 
     * Check if the file name given exists
     * @param inputFileName for the input file name
     * @return true if the input directory is correct, false otherwise
     */
    public boolean validateInputFileName(String inputFileName) {
        File file = new File(inputFileName);
        // Check if the file given by name exists
        return file.isFile();
    }
    
    /**
     * Check if the name given is the directory and if the
     * file in this directory exists
     * @param outputFileName for the output file name
     * @return true if the directory is correct, false otherwise
     */
    public boolean validateOutputFileName(String outputFileName) {
        File file = new File(outputFileName);
        // Check if the file given by name exists
        return file.isFile();
    }
    
    /**
     * Check if the conversion type is possible
     * @param conversionType either "l2m" or "m2l"
     * @return true if the conversion exists, otherwise for unknown tyoes
     */
    public boolean validateConversionType(String conversionType) {
        return ((conversionType.equals("m2l")) || (conversionType.equals("l2m")));
    }
    
}
