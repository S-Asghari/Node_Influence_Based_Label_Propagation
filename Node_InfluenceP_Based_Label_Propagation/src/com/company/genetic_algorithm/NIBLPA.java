package com.company.genetic_algorithm;

import java.util.*;

public class NIBLPA {
    private double alpha;
    private int[] label;
    private Vector<Vector<Integer>> graph;
    private double[] NI;
    private int[] KS;
    private Vector<Integer> X;
    private Vector<Vector<Integer>> result;

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public Vector<Vector<Integer>> getGraph() {
        return graph;
    }

    public void setGraph(Vector<Vector<Integer>> graph) {
        this.graph = graph;
    }

    public int[] getLabel() {
        return label;
    }

    public void setLabel(int[] label) {
        this.label = label;
    }

    public double[] getNI() {
        return NI;
    }

    public void setNI(double[] NI) {
        this.NI = NI;
    }

    public int[] getKS() {
        return KS;
    }

    public void setKS(int[] KS) {
        this.KS = KS;
    }

    public Vector<Integer> getX() {
        return X;
    }

    public void setX(Vector<Integer> x) {
        X = x;
    }

    public Vector<Vector<Integer>> getResult() {
        return result;
    }

    public void setResult(Vector<Vector<Integer>> result) {
        this.result = result;
    }

    public NIBLPA(Vector<Vector<Integer>> graph, double alpha) {
        this.graph = graph;
        NI = new double[graph.size()];
        KS = new int[graph.size()];
        this.alpha = alpha;
        label = new int[graph.size()];
        X = new Vector<>();
        result = new Vector<>();
    }


    private void begin() {               //Initialization : Assigns a unique label to each node in the network
        for (int i = 1; i < label.length; i++) {
            label[i] = i;
        }
    }

    private boolean isEmpty(Vector<Vector<Integer>> x) {
        for (int i = 1; i < x.size(); i++) {
            if (x.get(i).size() > 0)
                return false;
        }
        return true;
    }

    public void findKS()                //Finds the K-shell value of all nodes
    {

        int k = 1;
        Vector<Vector<Integer>> x = new Vector<>();
        for (int i = 0; i < graph.size(); i++) {            //Copies "graph" to "x", a vector of vector
            x.add(new Vector<>());                          //which we want to find the K_shell value of all its nodes
            for (int j = 0; j < graph.get(i).size(); j++) {
                x.get(i).add(graph.get(i).get(j));
            }
        }

        while (!isEmpty(x)) {
            boolean end = false;
            while (!end) {
                end = true;
                for (int i = 1; i < x.size(); i++) {
                    if (KS[i] == 0 && x.get(i).size() <= k) {
                        KS[i] = k;
                        for (int j = 0; j < x.get(i).size(); j++) {
                            x.get(x.get(i).get(j)).remove(new Integer(i));
                        }
                        x.get(i).removeAllElements();

                        end = false;
                    }
                }
            }
            k++;
        }

    }


    public void findNI() {                  //Finds the node influence value of all nodes
        for (int i = 1; i < graph.size(); i++) {
            double sum = 0;
            for (int j = 0; j < graph.get(i).size(); j++) {     //See the formula of calculating NI in the article
                sum += ((double) KS[graph.get(i).get(j)]) / graph.get(graph.get(i).get(j)).size();
            }
            NI[i] = (double) KS[i] + getAlpha() * sum;
        }
    }

    private void merge(Vector<Integer> nodes, double[] U, int l, int m, int r) {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;


        /* Create temp arrays */
        double L[] = new double[n1];
        int L1[] = new int[n1];
        double R[] = new double[n2];
        int R1[] = new int[n2];

        /*Copy data to temp arrays*/
        for (int i = 0; i < n1; ++i) {
            L[i] = U[l + i];
            L1[i] = nodes.get(l + i);
        }
        for (int j = 0; j < n2; ++j) {
            R[j] = U[m + 1 + j];
            R1[j] = nodes.get(m + 1 + j);
        }

        /* Merge the temp arrays */

        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarry array
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i] >= R[j]) {
                U[k] = L[i];
                nodes.set(k, L1[i]);
                i++;
            } else {
                U[k] = R[j];
                nodes.set(k, R1[j]);
                j++;
            }
            k++;
        }

        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            U[k] = L[i];
            nodes.set(k, L1[i]);
            i++;
            k++;
        }



        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            U[k] = R[j];
            nodes.set(k, R1[j]);
            j++;
            k++;
        }
    }

    // Main function that sorts arr[l..r] using
    // merge()
    public void sort(Vector<Integer> nodes, double U[], int l, int r) {
        if (l < r) {
            // Find the middle point
            int m = (l + r) / 2;

            // Sort first and second halves
            sort(nodes, U, l, m);
            sort(nodes, U, m + 1, r);

            // Merge the sorted halves
            merge(nodes, U, l, m, r);
        }
    }

    private void sortX(double[] U)              //Arranges nodes in descending order
    {                                           //of NI and storing the results in the vector 𝑋
        for (int i = 0; i < U.length; i++) {
            X.add(i);
        }
        sort(X, U, 1, U.length - 1);
    }

    private int calculateMaxLI(int index, Vector<Integer> maxLabels) {
        Vector<Double> res = new Vector<>();
        Vector<Integer> connected = graph.get(index);       //All the neighbors of node "index"
        HashSet<Integer> maxL = new HashSet<>();
        for (int i = 0; i < maxLabels.size(); i++) {
            maxL.add(maxLabels.get(i));
            res.add(0.0);
        }
        for (int i = 0; i < connected.size(); i++) {        //See the formula of calculating LI in the article
            int connectedIndex = connected.get(i);
            if (maxL.contains(label[connectedIndex])) {
                int labelIndex = maxLabels.indexOf(label[connectedIndex]);
                res.set(labelIndex, res.get(labelIndex) + (NI[connectedIndex] / graph.get(connectedIndex).size()));
            }
        }
        double max = -1.0;
        for (int i = 0; i < res.size(); i++) {
            if (res.get(i) > max)
                max = res.get(i);
        }
        Vector<Integer> ans = new Vector<>();
        for (int i = 0; i < res.size(); i++) {
            if (res.get(i) - max > 1e-5)
                ans.add(maxLabels.get(i));
        }
        //If multiple labels have the same LI, randomly select one of them to assign to the node.
        Random rand = new Random();
        return maxLabels.get(Math.abs(rand.nextInt() % maxLabels.size()));
    }


    private boolean updateLabels()                //For each node "index" in the vector "mark",
    {                                             //finds the label that the maximum number of its neighbors has
        boolean end = true;
        Random random = new Random();
        for (int cnt = 0; cnt < X.size(); cnt++) {
            int index = X.get(cnt);
            int max = 0;
            int[] count = new int[graph.size()];
            Vector<Integer> maxLabels = new Vector<>();

            for (int i = 0; i < graph.get(index).size(); i++) {
                count[label[graph.get(index).get(i)]]++;
                if (count[label[graph.get(index).get(i)]] > max) {
                    max = count[label[graph.get(index).get(i)]];
                    maxLabels.removeAllElements();
                    maxLabels.add(label[graph.get(index).get(i)]);
                }
                else if(count[label[graph.get(index).get(i)]] == max){
                    maxLabels.add(label[graph.get(index).get(i)]);
                }
            }


            boolean change = true;              //Checks out if "index" label needs to be changed
            for (int i = 0; i < maxLabels.size(); i++) {
                if (maxLabels.get(i) == label[index])
                    change = false;
            }
            if (change) {
                if (maxLabels.size() > 1)
                    label[index] = calculateMaxLI(index, maxLabels);
                else
                    //If multiple labels have the maximum number, select the one with the maximum LI
                    label[index] = maxLabels.get(0);
                end = false;
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

        for (int i = 0; i < x.size(); i++)
            result.add(new Vector<>());

        for (int i = 1; i < graph.size(); i++) {
            for (int j = 0; j < x.size(); j++) {
                if (label[i] == x.get(j))
                    result.get(j).add(i);
            }
        }
//        int sum = 0;
//        for (int i = 0; i < graph.size(); i++) {
//            sum += graph.get(i).size();
//        }
//        sum /= 1000;
//        System.out.println("sum = " + sum);
//        for (int i = 1; i < result.size(); i++) {
//            System.out.print("Labal " + x.get(i) + ": ");
//            for (int j = 0; j < result.get(i).size(); j++) {
//                System.out.print(result.get(i).get(j) + " ");
//            }
//            System.out.println();
//            System.out.println(result.get(i).size());
//        }
    }


    public void detectCommunity() {
        begin();
        findKS();
        findNI();
        double[] U = new double[NI.length];
        for (int i = 0; i < NI.length; i++) {
            U[i] = NI[i];
        }
        sortX(U);
        X.remove(0);
        boolean end = updateLabels();
        while (!end) {              //Stops the algorithm, If the label of every node does not change anymore
            end = updateLabels();
        }
        divideCommunity();
    }

}