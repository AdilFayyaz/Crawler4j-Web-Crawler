package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class UrlsList {
    private static final List<Urls> urlsList = new ArrayList<>();

    public void addToUrls(Urls url) {
        urlsList.add(url);
    }
    public List<Urls> getUrls() {
        return urlsList;
    }

    public int getSize(){
        return urlsList.size();
    }

    public void writeToCSV(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // Write header
            writer.println("URL, Is Ok");
            // Write data
            for (Urls url : urlsList) {
                writer.printf("%s,%s%n",
                        escapeSpecialCharacters(url.getUrl()),
                        url.getIsOk());
            }
            System.out.println("CSV file '" + fileName + "' has been created successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    private String escapeSpecialCharacters(String data) {
        if (data == null) {
            return "";
        }
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }


}
