package lab6.jakobczyk.michal.java.polsl.pl.morseapp.model;

import java.util.ArrayList;
import java.io.*;

import lab6.jakobczyk.michal.java.polsl.pl.morseapp.exception.IncorrectConversionTypeException;

/**
 * Contains converion algorithms
 *
 * @author Michał Jakóbczyk
 * @version 4.0
 */
public class ConverterModel {

    /**
     * Morse signs system
     */
    private final MorseSigns morseSigns;

    /**
     * Latin signs system
     */
    private final LatinSigns latinSigns;

    /**
     * Conversion type info
     * Either "l2m" or "m2l"
     */
    private String conversionType;

    /**
     * Text controller
     */
    private TextModel textModel;

    /**
     * Constant value of -1 to indicate that element does not exist
     */
    private final int elementNotFound = -1;

    /**
     * Default delimiter during conversion
     */
    private final String delimiter = " ";

    /**
     * Stores the history of the parameters passed to the application
     */
    private ArrayList<ArrayList<String>> parametersHistory;

    /**
     * Constructor of the converter model
     * @param fileInputName for reading data
     * @param fileOutputName for writing data
     * @param conversionType to choose between m2l and l2m
     */
    public ConverterModel(String fileInputName, String fileOutputName, String conversionType) {
        this.morseSigns = new MorseSigns();
        this.latinSigns = new LatinSigns();
        this.conversionType = conversionType;
        this.textModel = new TextModel(fileInputName, fileOutputName);
        this.parametersHistory = new ArrayList<>();
    }

    /**
     * Converts string into a list of characters using a specific delimiter
     * @param delimiter which is usually a space
     * @param content which is a string to explode
     * @param morseCharacters list of morse characters exploded from content by delimiter
     */
    private boolean explodeString(String delimiter, String content, ArrayList<String> morseCharacters) {
        ArrayList<Integer> matchesFound = new ArrayList<>();
        matchesFound.add(-1);
        int delimiterPosition = content.indexOf(delimiter);
        if (delimiterPosition == elementNotFound)
            return false;

        // Loop through the content as long as delimiters are found
        do {
            // Add every found delimiter position to the container
            matchesFound.add(delimiterPosition);

            // Calculate the next possible delimiter position
            delimiterPosition = content.indexOf(delimiter, delimiterPosition + delimiter.length());
        } while (delimiterPosition != elementNotFound);

        // Loop through delimiters positions as long as they exist
        for (int i = 0; i < matchesFound.size() - 1; ++i) {
            StringBuilder textTruncate = new StringBuilder();

            // Append to the string builder an adequate part of the passed string
            textTruncate.append(content.substring(matchesFound.get(i) + 1, matchesFound.get(i + 1)));

            // Convert string builder into a string
            String explodedContent = textTruncate.toString();

            // Add string to the container of morse characters
            morseCharacters.add(explodedContent);
        }

        return true;
    }

    /**
     * Converts morse code to latin
     */
    private void morseToLatin() {
        // Converted text variable
        ArrayList<String> converted = new ArrayList<>();

        // Loop through all the text lines
        for (int i = 0 ; i < this.textModel.getOriginalText().size(); ++i) {
            // Single line is being taken from the input container
            // String singleLine = this.textController.getTextModel().getOriginalText().get(i);

            ArrayList<String> morseStrings = new ArrayList<>();

            // Perform the explosion of the analyzed string
            if (this.explodeString(delimiter, this.textModel.getOriginalText().get(i), morseStrings) == false) {
                // If the explosion does not succeed simply pass the analyzed string to the container
                morseStrings.add(this.textModel.getOriginalText().get(i));
            }

            // Loop through each of the text line
            for (int j = 0; j < morseStrings.size(); ++j) {
                /*
                Single character from a single line is being converted from the character type
                o the string type and converted into a lower case because Morse Translator
                is not able to detect the size of the signs
                */

                int singleCharPosition = this.morseSigns.getIndexOfValue(morseStrings.get(j));
                if (singleCharPosition >= 0) {
                    converted.add(this.latinSigns.getValue(singleCharPosition));
                }
                else {
                    if (morseStrings.get(j).equals("")) {
                        converted.add(" ");
                    }
                    // If we came across any sign that is not compatible in conversion
                    // simply pass the original text to the output
                    else if (!morseStrings.get(j).equals(".") || !morseStrings.get(j).equals("-")) {
                        this.textModel.setConvertedText(this.textModel.getOriginalText());
                        return;
                    }
                    else {
                        converted.add(morseStrings.get(j));
                    }
                }
            }

            // If the current line is not the last a new line sign is being appended
            // Add a new line after converting a whole line (the 3rd attempt
            // because of morse tabbing system)
            if (i % 3 == 2 ) converted.add(System.lineSeparator());
        }

        // Update the converted text
        this.textModel.setConvertedText(converted);
    }

    /**
     * Converts latin code to morse
     */
    private void latinToMorse() {
        // Converted text variable
        ArrayList<String> converted = new ArrayList<>();

        // Loop through all the text lines
        for (int i = 0 ; i < this.textModel.getOriginalText().size(); ++i) {
            // Single line is being taken from the input container
            String singleLine = this.textModel.getOriginalText().get(i);

            // Loop through each of the text line
            for (int j = 0; j < singleLine.length(); ++j) {
                /*
                Single character from a single line is being converted from the character type
                o the string type and converted into a lower case because Morse Translator
                s not able to detect the size of the signs
                */
                String singleChar = (Character.toString(singleLine.charAt(j))).toLowerCase();
                int singleCharPosition = this.latinSigns.getIndexOfValue(singleChar);
                if (singleCharPosition >= 0) {
                    converted.add(this.morseSigns.getValue(singleCharPosition));
                    converted.add(" ");
                }
                else {
                    converted.add(singleChar);
                }
            }

            // If the current line is not the last a new line sign is being appended
            // Add a new line after converting a whole line (the 2nd attempt
            // because of morse tabbing system)
            if (i % 2 == 1)
                converted.add(System.lineSeparator());

        }

        // Update the converted text
        this.textModel.setConvertedText(converted);
    }

    /**
     * Runs the conversion of given type
     * @throws IOException if there was a problem with a stream
     * @throws IncorrectConversionTypeException if conversion type is invalid
     */
    public void convert() throws IOException, IncorrectConversionTypeException {

        // Adjust conversion type
        if (this.conversionType.equals("m2l")) {
            this.morseToLatin();
        }
        else if (this.conversionType.equals("l2m")) {
            this.latinToMorse();
        }
        else {
            throw new IncorrectConversionTypeException();
        }
    }

    /**
     * Getter for the TextModel
     * @return TextModel instance
     */
    public TextModel getTextModel() {
        return this.textModel;
    }

    /**
     * Set model parameters
     * @param inputFile for read
     * @param outputFile for write
     * @param conversionType for conversion style
     */
    public void setParameters(String inputFile, String outputFile, String conversionType) {
        this.textModel = new TextModel(inputFile, outputFile);
        this.conversionType = conversionType;
    }

    /**
     * Getter for parameters history.
     * @return the original parameters history
     */
    public ArrayList<ArrayList<String>> getParametersHistory() {
        return this.parametersHistory;
    }

    /**
     * Adds new arraylist of parameters to the history
     * @param newParameters passed to the arraylist
     */
    public void addParametersToHistory(ArrayList<String> newParameters) {
        if (newParameters != null) {
            parametersHistory.add(newParameters);
        }
    }

    /**
     * Sets the original text of the text model object
     * @param newOriginalText to set
     */
    public void setOriginalText(ArrayList<String> newOriginalText) {
        this.textModel.setOriginalText(newOriginalText);
    }
}
