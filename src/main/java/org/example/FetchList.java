package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;



public class FetchList {
    private static final List<Fetch> fetchList = new ArrayList<>();

    public void addToFetchList(Fetch fetch) {
        fetchList.add(fetch);
    }
    public List<Fetch> getFetchList() {
        return fetchList;
    }

    public int getSize(){
        return fetchList.size();
    }

    public void writeToCSV(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // Write header
            writer.println("URL,Status");
            // Write data
            for (Fetch fetch : fetchList) {
                writer.printf("%s,%d%n",
                        escapeSpecialCharacters(fetch.getUrl()),
                        fetch.getHttpCode());
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
