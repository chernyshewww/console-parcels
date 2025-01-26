package com.hofftech.deliverysystem.util;

import org.springframework.stereotype.Service;

@Service
public class FormHelper {

    /**
     * Parses a form string into a 2D char array, replacing 'x' characters with the specified symbol.
     *
     * @param formString The string representing the form layout, where each line represents a row.
     *                   'x' characters in the form will be replaced by the specified symbol.
     * @param symbol The symbol to replace 'x' characters in the form.
     * @return A 2D char array representing the form with the specified symbol.
     */
    public char[][] parseForm(String formString, char symbol) {
        String[] lines = formString.split("\\\\n");

        int rows = lines.length;
        int cols = lines[0].length();

        char[][] form = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                form[i][j] = (lines[i].charAt(j) == 'x') ? symbol : lines[i].charAt(j);
            }
        }

        return form;
    }

    /**
     * Converts a 2D char array representing a form into a string representation.
     *
     * @param form A 2D char array representing the form layout.
     * @return A string representation of the form, with each row separated by a newline.
     */
    public String getFormAsString(char[][] form) {
        StringBuilder builder = new StringBuilder();
        for (char[] row : form) {
            builder.append(row).append("\n");
        }
        return builder.toString();
    }
}
