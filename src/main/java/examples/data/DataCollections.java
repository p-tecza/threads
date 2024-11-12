/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.data;

import java.util.*;
import java.io.*;
import java.awt.geom.*;

/**
 *
 * @author Dominik Olszewski
 */
public class DataCollections {

    private static Set<Integer> set;
    private static List<Integer> list;
    private static Map<String, Integer> map;
    private static Map<Point2D, Double> paraboloid;

    public static void generateSet() {
        set = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            set.add((int) (Math.random() * 200 - 100));
        }

        System.out.println(set);

        set = new LinkedHashSet<>(set);

        System.out.println(set);

        set = new TreeSet<>(set);

        System.out.println(set);
    }

    public static void generateList() {
        list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add((int) (Math.random() * 200 - 100));
        }

        System.out.println(list);

        Collections.sort(list);

        System.out.println(list);

        Collections.shuffle(list);

        System.out.println(list);
    }

    public static void generateMap() {
        map = new LinkedHashMap<>();

        String[] strings = {"Alabama",
                            "South Carolina",
                            "Mississippi",
                            "Georgia",
                            "Texas",
                            "Louisiana",
                            "Florida"};

        for (String s : strings) {
            map.put(s, s.length());
        }

        System.out.println(map);

        map = new HashMap<>(map);

        System.out.println(map);

        map = new TreeMap<>(map);

        System.out.println(map);
    }

    public static Map<String, Integer> computeWordsFrequency(String fileNamePath) {
        Map<String, Integer> wordsFreq = new TreeMap<>();
        try (Scanner in = new Scanner(new File(fileNamePath))) {
            while (in.hasNext()) {
                String word = in.next();
                wordsFreq.put(word, wordsFreq.containsKey(word) ? wordsFreq.get(word) + 1 : 1);
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe.getMessage());
        }
        return wordsFreq;
    }

    public static void optimizeParaboloid(double lowerBound, double upperBound, double step) {
        paraboloid = new LinkedHashMap<>();

        for (double x = lowerBound; x <= upperBound; x += step) {
            for (double y = lowerBound; y <= upperBound; y += step) {
                paraboloid.put(new Point2D.Double(x, y), x * x + y * y);
            }
        }

        Double minValue = Collections.min(paraboloid.values());
        Map<Point2D, Double> minMap = new LinkedHashMap<>(paraboloid);
        minMap.values().retainAll(Collections.singleton(minValue));
        System.out.println(minMap);

        System.out.println("====================");

        Double maxValue = Collections.max(paraboloid.values());
        Map<Point2D, Double> maxMap = new LinkedHashMap<>(paraboloid);
        maxMap.values().retainAll(Collections.singleton(maxValue));
        System.out.println(maxMap);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //generateSet();
        //generateList();
        //generateMap();
        optimizeParaboloid(-10., 10., 1.);
    }
}