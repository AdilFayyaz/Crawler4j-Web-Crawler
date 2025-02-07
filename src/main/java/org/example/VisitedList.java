package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class VisitedList {
    private static final List<Visit> visitedList = new ArrayList<>();

    public void addToVisitedList(Visit visit) {
        visitedList.add(visit);
    }
    public List<Visit> getVisitedList() {
        return visitedList;
    }

    public int getSize(){
        return visitedList.size();
    }

    public void writeToCSV(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // Write header
            writer.println("URL, Size(bytes), # of Outlinks, Content-Type");
            // Write data
            for (Visit visit : visitedList) {
                writer.printf("%s,%d, %d, %s%n",
                        escapeSpecialCharacters(visit.getUrl()),
                        visit.getSize(),
                        visit.getNoOfOutlinks(),
                        visit.getContentType());
            }
            System.out.println("CSV file '" + fileName + "' has been created successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
