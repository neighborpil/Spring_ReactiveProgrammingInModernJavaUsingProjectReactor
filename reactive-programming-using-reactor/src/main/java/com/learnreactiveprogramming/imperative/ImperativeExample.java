package com.learnreactiveprogramming.imperative;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ImperativeExample {

    public static void main(String[] args) {
        List<String> namesList = List.of("alex", "ben", "chloe", "adam");

        var newNamesList = namesGreaterThanSize(namesList, 3);
        System.out.println("newNamesList: " +  newNamesList);

    }

    private static List<String> namesGreaterThanSize(List<String> namesList, int size) {
        var newNamesList = new ArrayList<String>();

        for (String name : namesList) {
            if (name.length() > size && !newNamesList.contains(name)) {
                newNamesList.add(name.toUpperCase(Locale.ROOT));
            }
        }

        return newNamesList;
    }
}
