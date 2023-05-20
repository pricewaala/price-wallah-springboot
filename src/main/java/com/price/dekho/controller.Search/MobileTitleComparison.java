package com.price.dekho.controller.Search;

import java.util.HashMap;
import java.util.Map;

public class MobileTitleComparison {
    public static void main(String[] args) {
        // Simulated API data
        Map<String, String> apiData = new HashMap<>();
        apiData.put("Amazon", "Apple iPhone 13 128GB -  RED");
        apiData.put("Flipkart", "128GB Apple iPhone 13  - (Product) RED");
        apiData.put("Reliance", "iPhone Apple  13 (128GB) -  RED");

        // Clean the titles by removing special characters and converting to lowercase
        Map<String, String> cleanTitles = cleanTitles(apiData);

        // Compare the titles
        boolean isSimilar = compareTitles(cleanTitles);

        // Output the result
        if (isSimilar) {
            System.out.println("The titles are similar.");
        } else {
            System.out.println("The titles are not similar.");
        }
    }

    private static Map<String, String> cleanTitles(Map<String, String> titles) {
        Map<String, String> cleanTitles = new HashMap<>();
        for (Map.Entry<String, String> entry : titles.entrySet()) {
            String key = entry.getKey();
            String title = entry.getValue();
            // Remove special characters and convert to lowercase
            String cleanTitle = title.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase();
            cleanTitles.put(key, cleanTitle);
        }
        return cleanTitles;
    }

    private static boolean compareTitles(Map<String, String> titles) {
        int totalTitles = titles.size();
        int matchingPairs = 0;

        // Convert titles map to array for easier indexing
        String[] titleArray = new String[totalTitles];
        titles.values().toArray(titleArray);

        for (int i = 0; i < totalTitles - 1; i++) {
            String title1 = titleArray[i];

            for (int j = i + 1; j < totalTitles; j++) {
                String title2 = titleArray[j];

                if (hasSimilarWords(title1, title2)) {
                    matchingPairs++;
                }
            }
        }

        // Check if the number of matching pairs exceeds the threshold
        int uniqueTitleCombinations = (totalTitles * (totalTitles - 1)) / 2;
        double similarityThreshold = 0.5; // Adjust the similarity threshold as needed
        double matchingPercentage = (double) matchingPairs / uniqueTitleCombinations;
        return matchingPercentage >= similarityThreshold;
    }

    private static boolean hasSimilarWords(String title1, String title2) {
        String[] words1 = title1.split(" ");
        String[] words2 = title2.split(" ");

        int matchingWords = 0;

        // Check if any word from title1 is present in title2
        for (String word : words1) {
            if (containsIgnoreCase(words2, word)) {
                matchingWords++;
            }
        }

        // Check if more than 50% of the words from title1 are present in title2
        double wordPercentage = (double) matchingWords / words1.length;
        return wordPercentage > 0.5;
    }

    private static boolean containsIgnoreCase(String[] array, String word) {
        for (String str : array) {
            if (str.equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }
}
