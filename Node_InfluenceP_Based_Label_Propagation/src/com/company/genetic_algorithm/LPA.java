package com.company.genetic_algorithm;


import java.util.HashSet;
import java.util.Random;
import java.util.Vector;

public class LPA {
    private Vector<Vector<Integer>> graph;
    private Vector<Integer> mark;
    private Vector<Vector<Integer>> result;         //The result of community detection
    private int[] label;

    public int[] getLabel() {
        return label;
    }

    public void setLabel(int[] label) {
        this.label = label;
    }

    public Vector<Integer> getMark() {
        return mark;
    }

    public void setMark(Vector<Integer> mark) {
        this.mark = mark;
    }

    public Vector<Vector<Integer>> getResult() {
        return result;
    }

    public void setResult(Vector<Vector<Integer>> result) {
        this.result = result;
    }

    public Vector<Vector<Integer>> getGraph() {
        return graph;
    }

    public void setGraph(Vector<Vector<Integer>> graph) {
        this.graph = graph;
    }

    public LPA(Vector<Vector<Integer>> graph) {
        this.graph = graph;
        mark = new Vector<>();
        label = new int[graph.size()];
    }

    private void begin() {              //Initialization : Assigns a unique label to each node in the network
        for (int i = 1; i < label.length; i++) {
            label[i] = i;
            mark.add(i);
        }
        arrange();
    }

    private void arrange()                  //Adds all the nodes of the network to the vector "mark",
    {
        Random random = new Random();
        for (int i = 0; i < mark.size(); i++) {
            int s1 = Math.abs(random.nextInt() % mark.size());
            int s2 = Math.abs(random.nextInt() % mark.size());
            int mark1 = mark.get(s1);
            mark.set(s1, mark.get(s2));
            mark.set(s2, mark1);
        }

    }

    private boolean updateLabels()                //For each node "index" in the vector "mark",
    {                                             //finds the label that the maximum number of its neighbors has
        boolean end = true;
        Random random = new Random();
        for (int cnt = 0; cnt < mark.size(); cnt++) {
            int index = mark.get(cnt);
            int max = 0;
            int[] count = new int[graph.size()];
            Vector<Integer> maxLabels = new Vector<>();

            for (int i = 0; i < graph.get(index).size(); i++) {
                count[label[graph.get(index).get(i)]]++;
                if (count[label[graph.get(index).get(i)]] > max) {
                    max = count[label[graph.get(index).get(i)]];
                    maxLabels.removeAllElements();
                    maxLabels.add(label[graph.get(index).get(i)]);
                } else if (count[label[graph.get(index).get(i)]] == max) {
                    maxLabels.add(label[graph.get(index).get(i)]);
                }
            }


            boolean change = true;              //Checks out if "index" label needs to be changed
            for (int i = 0; i < maxLabels.size(); i++) {
                if (maxLabels.get(i) == label[index])
                    change = false;
            }
            if (change) {
                end = false;

                //If multiple labels have the maximum number, randomly select one of them to assign to the node.
                label[index] = maxLabels.get(Math.abs(random.nextInt() % maxLabels.size()));
            }
        }

        return end;
    }


    private void divideCommunity() {                //Assigns the nodes with the same label to a community
        HashSet<Integer> community = new HashSet<>();
        for (int i = 1; i < label.length; i++) {
            community.add(label[i]);
        }

        Vector<Integer> x = new Vector<>();
        x.addAll(community);

        result = new Vector<>();

        for (int i = 0; i < community.size(); i++)
            result.add(new Vector<>());

        for (int i = 1; i < graph.size(); i++) {
            for (int j = 0; j < x.size(); j++) {
                if (label[i] == x.get(j))
                    result.get(j).add(i);
            }
        }


    }

    public void detectCommunity() {
        begin();
        boolean end = updateLabels();
        while (!end) {                 //Stops the algorithm, If the label of every node does not change anymore
            arrange();
            end = updateLabels();
        }
        divideCommunity();
    }


}