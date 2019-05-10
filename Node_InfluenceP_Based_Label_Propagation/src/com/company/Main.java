package com.company;

import com.company.Test.Test;
import com.company.genetic_algorithm.LPA;
import com.company.genetic_algorithm.NIBLPA;
import com.company.reader.Reader;

import java.util.Scanner;
import java.util.Vector;


public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String string = sc.next();
        String net = string +"\\network.txt";
        String community = string + "\\community.txt";
        System.out.println(string);
        System.out.println(net);
        System.out.println(community);
        Test test = new Test();
        int num = sc.nextInt();
        long s1,s2;
        while (!string.isEmpty()) {
            Reader reader = new Reader(net, num);

            Vector<Vector<Integer>> x = reader.getGraph();

            double sum1 = 0;
            double sum2 = 0;

            s1 = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {
                LPA lpa = new LPA(x);
                lpa.detectCommunity();
                int[] labels = lpa.getLabel();
                Vector<Integer> v = new Vector<>();
                for (int j = 0; j < labels.length; j++) {
                    v.add(labels[j]);
                }
                try {
                    sum2 += test.NMI(v, community);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            s2 = System.currentTimeMillis();

            System.out.println("Time LPA : " + (s2 - s1));
            System.out.println("Avg of LPA : " + (sum2 / 100));


            s1 = System.currentTimeMillis();
            double a = 0.0;
            for (int i = 0; i < 100; i++) {
                NIBLPA niblpa = new NIBLPA(x, a);
                niblpa.detectCommunity();
                int[] labels = niblpa.getLabel();
                Vector<Integer> v = new Vector<>();
                for (int j = 0; j < labels.length; j++) {
                    v.add(labels[j]);
                }
                try {
                    sum1 += test.NMI(v, community);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                a += 0.01;
            }
            s2 = System.currentTimeMillis();

            System.out.println("Time NIBLPA : " + (s2 - s1));
            System.out.println("Avg of NIBLPA : " + (sum1 / 100));




            string = sc.next();
            net = string + "\\network.txt";
            community = string + "\\community.txt";
            num = sc.nextInt();
        }

        }

    }

