/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DocMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

/**
 *
 * @author VietAnh
 */

public class docmap { // docmap3

    public static int H;
    public static int W;
    public static int s; // so sensor
    public static double R; // bk sensor
    public static ArrayList<Vector<Double>> listS;

    public docmap(String c) throws FileNotFoundException {
        File file = new File(c);
        try (Scanner scan = new Scanner(file)) {
            H = scan.nextInt();
            W = scan.nextInt();
            s = scan.nextInt();
            R = scan.nextDouble();
            listS = new ArrayList<>();
            // for(int i=1;i<=6;i++) scan.nextLine();
            scan.nextLine();
            for (int i = 0; i < s; i++) {
                Vector<Double> tmp = new Vector<>();
                double v1 = scan.nextInt();
                double v2 = scan.nextInt();
                tmp.add(v1);
                tmp.add(v2);
                listS.add(tmp);
                scan.nextLine();
            }

            //
        }

    }
}
