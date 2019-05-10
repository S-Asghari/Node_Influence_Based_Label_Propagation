package com.company.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class Reader {
    private String fileAddress;
    private Vector<Vector<Integer>> graph;
    private int nodes;

    public Reader(String fileAddress, int nodes) {
        graph = new Vector<>();
        this.fileAddress = fileAddress;
        this.nodes = nodes;
        buildGraph();
    }

    private void validate() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter File Path : ");
        fileAddress = scanner.next();
        System.out.println("Enter number of nodes : ");
        nodes = scanner.nextInt();
        buildGraph();
    }


    private void buildGraph() {
        while (graph.size() < nodes + 1)
            graph.add(new Vector<>());
        try {
            FileReader reader = new FileReader(fileAddress);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] temp = line.split(" ");
                int source = Integer.parseInt(temp[0]);
                int destination = Integer.parseInt(temp[1]);

                graph.get(source).add(destination);
                graph.get(destination).add(source);
            }
            bufferedReader.close();
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Wrong input File!");
            System.out.println("Please Enter File Path again :");
            validate();
        } catch (IOException e) {
            System.out.println("IO Exception Error reading File accured.");
            validate();
        }

    }

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }

    public Vector<Vector<Integer>> getGraph() {
        return graph;
    }

    public void setGraph(Vector<Vector<Integer>> graph) {
        this.graph = graph;
    }

    public int getNodes() {
        return nodes;
    }

    public void setNodes(int nodes) {
        this.nodes = nodes;
    }
}
