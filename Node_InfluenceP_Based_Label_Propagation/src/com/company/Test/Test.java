package com.company.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.Vector;

public class Test {


    public float NMI(Vector<Integer> Prediction, String TrueCommunityPathTXT) throws Exception {
        Vector<Integer> TrueLabel = new Vector<Integer>();
        int countGuess = 0, countGold = 0;
        float NMI = 0, up = 0, down = 0;
        int n = 0;
        TrueLabel.add(0);
        BufferedReader br = new BufferedReader(new FileReader(TrueCommunityPathTXT));
        String line = br.readLine();
        while (line != null) {
            String[] parts = line.split(" ");
            TrueLabel.add(Integer.parseInt(parts[1]));
            n++;
            line = br.readLine();
        }
        br.close();
        if (n != Prediction.size() - 1)
            return -1;
        Hashtable<Integer, Integer> temp = new Hashtable<Integer, Integer>();
        int k = 1;
        for (int i = 1; i <= n; i++) {
            if (temp.containsKey((Integer) Prediction.get(i)))
                Prediction.set(i, temp.get(Prediction.get(i)));
            else {
                temp.put(Prediction.get(i), k);
                Prediction.set(i, temp.get(Prediction.get(i)));
                k++;
            }
        }
        for (int i = 1; i <= n; i++) {
            if (Prediction.get(i) > countGuess)
                countGuess = Prediction.get(i);
            if (TrueLabel.get(i) > countGold)
                countGold = TrueLabel.get(i);
        }
        float NRow[] = new float[countGold];
        float NCol[] = new float[countGuess];
        float matrix[][] = new float[countGold][countGuess];
        for (int i = 0; i < countGold; i++)
            matrix[i] = new float[countGuess];
        for (int i = 1; i <= n; i++) {
            matrix[TrueLabel.get(i) - 1][Prediction.get(i) - 1]++;
            NRow[TrueLabel.get(i) - 1]++;
            NCol[Prediction.get(i) - 1]++;
        }
        for (int i = 0; i < countGold; i++)
            if (NRow[i] != 0)
                down += NRow[i] * (Math.log(NRow[i] / (float) (n)) / Math.log(2));
        for (int i = 0; i < countGuess; i++)
            if (NCol[i] != 0)
                down += NCol[i] * (Math.log(NCol[i] / (float) (n)) / Math.log(2));
        for (int i = 0; i < countGold; i++)
            for (int j = 0; j < countGuess; j++)
                if (matrix[i][j] != 0)
                    up += matrix[i][j] * (Math.log((matrix[i][j] * (float) (n) / ((NRow[i] * NCol[j])))) / Math.log(2));
        up *= (float) -2;
        NMI = up / down;
        return NMI;
    }

}


