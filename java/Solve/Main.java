/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Solve;

import DocMap.docmap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Vector;

/**
 *
 * @author VietAnh
 */
public class Main {
        public static void main(String[] args) throws IOException {
                String input = "java/Data/dt.txt";
                new docmap(input);
                String ghi = "";
                ghi += "W: " + docmap.W + ", H: " + docmap.H + ", soSS: " + docmap.s + ", bk: " + docmap.R + "\n";
                ghi += "soPP: " + PSO.m + ", soMSink: " + GA.k + "\n";
                // System.out.println(docmap.listS.size());
                // System.out.println(docmap.s);
                PSO x1 = new PSO();
                x1.runPSO();
                System.out.println("Solve.Main.main()");
                ghi += Solve.PSO.ghi;
                ghi += "Gbest: \n";
                for (int i = 0; i < x1.getGbest().size(); i++) {
                        // System.out.println(x1.getGbest().get(i));
                        ghi += x1.getGbest().get(i) + " ";
                }
                // x1.khoitao();
                // x1.danhgia();
                // ArrayList<Vector<Double>> tm = new ArrayList<>();
                // tm = x1.getHTmin();
                // System.out.println(tm.get(0).size());
                // for(int i=0;i<x1.getGbest().size();i++){
                // System.out.println(x1.getGbest().get(i));
                // }
                Vector<Integer> fit = new Vector<>();
                fit = x1.getFitG();
                System.out.println("Trung: " + fit.get(0) + "  Bao phu: " + fit.get(1));
                ghi += "\nTrung: " + fit.get(0) + "  Bao phu: " + fit.get(1) + "  MaxSS: " + docmap.s + "\n";
                ghi += "Tong NL: " + x1.getLife(x1.getGbest()).get(0) + "\n";
                ghi += "Life round: " + x1.getLife(x1.getGbest()).get(1) + "\n\n";
                GA x2 = new GA();
                // x2.runGA(x1.getGbest());
                x2.khoitaoKC(x1.getGbest());
                x2.khoitao();
                for (int i = 0; i < 400; i++) {
                        x2.fitness();
                        x2.capNhatBest();
                        x2.chonloc();
                        x2.laighep();
                        x2.dotbien();
                }
                System.out.println(x2.getTre());
                ghi += GA.ghi;

                ghi += "Best: \nSequences: ";
                for (int j = 0; j < x2.getBestSQ().length; j++)
                        ghi += x2.getBestSQ()[j] + ",";
                ghi += " Break points: ";
                for (int j = 1; j < x2.getBestBP().length - 1; j++) {
                        ghi += x2.getBestBP()[j] + ",";
                }
                ghi += "Best: " + x2.getBest();
                ghi += "\nTre: " + x2.getTre();
                ghi += "\n\n===============================================================================================";
                for (int e = 0; e < 5; e++)
                        ghi += "====================================";
                ghi += "===============================================================================================\n\n";
                // System.out.println("nl= "+x1.getLife(x1.getGbest()));
                // for(int i=0;i<x1.getGbest().size();i++){
                // System.out.println(x1.getGbest().get(i));
                // }
                // PSO x3 = new PSO();
                // x3.khoitaoLuoi();

                try {
                        File f2 = new File("java/Data/output.text");
                        FileWriter fw = new FileWriter(f2, true);
                        // Bước 2: Ghi dữ liệu
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write(ghi);
                        bw.close();
                        fw.close();
                } catch (IOException ex) {
                        ex.printStackTrace();
                }
                System.out.println("Solve.Main.main()");
                System.out.println(ghi);

        }
}