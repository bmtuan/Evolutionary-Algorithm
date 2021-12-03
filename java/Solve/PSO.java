/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Solve;

import DocMap.docmap;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.Vector;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static DocMap.docmap.R; //bk
import static DocMap.docmap.W;
import static DocMap.docmap.H;
import static DocMap.docmap.s; //so sensor
import java.io.File;
import java.io.FileWriter;

/**
 *
 * @author VietAnh
 */
public class PSO {
    public Random ran = new Random();
    public static int m = 25; // so park
    public static int n = 100; // so ca the trong 1 quan the
    public static double Vmax = 50.0; // thamso
    public static double Vmin = -50.0;
    public static double nn = 0.5;
    public static double aa = 0.4;
    public static double bb = 0.6;
    public static double Eelec = 50 * pow(10, -9);
    public static double Efs = 10 * pow(10, -12);
    public static double Emp = 0.0013 * pow(10, -12);
    public static double d0 = sqrt(Efs / Emp);
    public static double k = 10;
    public static double soLanLap = 500;
    public static ArrayList<Vector<Double>> listS = docmap.listS; // list sensor
    public static ArrayList<Vector<Double>> Vt; // list vận tốc
    public static ArrayList<Vector<Double>> quanThe; // quần thể
    public static ArrayList<Vector<Double>> Pbest; // list lưu Pbest
    public static Vector<Double> Gbest; // Gbest
    public static String ghi = "PSO: \n"; // Gbest

    public PSO() {
        quanThe = new ArrayList<>();
        Pbest = new ArrayList<>();
        Vt = new ArrayList<>();
        Gbest = new Vector<>();
        for (int i = 0; i < n; i++) {
            Vector<Double> tmp = new Vector<>();
            for (int j = 0; j < m; j++) {
                tmp.add(10.0);
                tmp.add(10.0);
            }
            Vt.add(tmp);
        }
    }

    public void khoitao() {
        for (int l = 0; l < n / 2; l++) { // tao n ca the va` add vao quanThe
            Vector<Double> caThe = new Vector<>();
            for (int i = 0; i < m; i++) {
                double x, y;
                x = ran.nextInt(W + 1);
                y = ran.nextInt(H + 1);
                Vector<Double> v = new Vector<>();
                v.add(x);
                v.add(y);
                caThe.add(x);
                caThe.add(y);
            }
            quanThe.add((Vector<Double>) caThe.clone());
            Pbest.add((Vector<Double>) caThe.clone());
        }
    }

    public void khoitaoLuoi() {
        double tmp = sqrt(m);
        int tm1 = (int) tmp;
        int tm2 = m - tm1 * tm1;
        double wkc = W / (tm1 + 1);

        double hkc = H / (tm1 + 1);
        for (int l = 0; l < 4 * n / 5; l++) { // tao n ca the va` add vao quanThe
            Vector<Double> caThe = new Vector<>();
            for (int i = 0; i < tm1; i++) {
                for (int j = 0; j < tm1; j++) {
                    double x1 = ran.nextInt((int) (wkc / 2)) - wkc / 4;
                    double y1 = ran.nextInt((int) (hkc) / 2) - hkc / 4;
                    double x, y;
                    x = wkc * (i + 1) + x1;
                    y = hkc * (j + 1) + y1;
                    Vector<Double> v = new Vector<>();
                    v.add(x);
                    v.add(y);
                    caThe.add(x);
                    caThe.add(y);
                }
            }
            for (int i = 0; i < tm2; i++) {
                double x, y;
                x = ran.nextInt(W + 1);
                y = ran.nextInt(H + 1);
                Vector<Double> v = new Vector<>();
                v.add(x);
                v.add(y);
                caThe.add(x);
                caThe.add(y);
            }
            quanThe.add((Vector<Double>) caThe.clone());
            Pbest.add((Vector<Double>) caThe.clone());

        }
        ghi += "Khoi tao: \n";
        for (int i = 0; i < quanThe.get(3).size(); i++) {
            // System.out.println(quanThe.get(3).get(i));
            ghi += quanThe.get(3).get(i) + " ";
        }
        Vector<Double> t2 = new Vector<>();
        t2 = getFit(quanThe.get(3));
        // System.out.println("Trung: "+ t2.get(0)+" Bao phu: "+ t2.get(1));
        ghi += "\nTrung: " + t2.get(0) + "  Bao phu: " + t2.get(1) + "  MaxSS: " + docmap.s + "\n";
        // System.out.println("Solve.PSO.khoitaoLuoi(): " +quanThe.get(3).size());
    }

    public void danhgia() { // đánh giá ban đầu
        double gmin = 999;
        int ind = -1;
        for (int u = 0; u < n; u++) {
            double tmp = fit(quanThe.get(u));
            if (tmp < gmin) {
                gmin = tmp;
                ind = u;
            }
            // System.out.println("Lan: "+u+ " Trung: "+ getFit(quanThe.get(u)).get(0)+" Bao
            // phu: "+ getFit(quanThe.get(u)).get(1)+" fit=
            // "+getFit(quanThe.get(u)).get(2));
        }
        Gbest = (Vector<Double>) quanThe.get(ind).clone();

    }

    public double fit(Vector<Double> a) // hàm tính fitness
    {
        Vector<Double> fitG = new Vector<>();
        double fitness;
        double d1 = 0.0; // so sensor trung
        double d2 = 0.0; // so sensor bao phu
        for (int i = 0; i < s; i++) {
            int dem = 0;
            for (int j = 0; j < m; j++) {
                // System.out.println("Solve.PSO.fit()"+ j);
                if (L(listS.get(i), Vec(a.get(2 * j), a.get((2 * j) + 1))) <= R) {
                    dem += 1;
                }
            }
            if (dem >= 1)
                d2 += 1;
            if (dem >= 2)
                d1 += 1;
        }
        if (d1 > 0 && d2 > 0)
            fitness = d1 / d2 * 100;
        else if (d1 == 0 && d2 > 0)
            fitness = 1 / d2 * 100;
        else
            fitness = 999 - d1 - d2;
        fitG.add(d1);
        fitG.add(d2);
        fitG.add(fitness);
        return fitness;
    }

    public void capNhatVt(int index) // cập nhật Vt của vị trí index
    {
        // Vt.get(u) = tinhV(Pbest.get(u), quanThe.get(u), Gbest);
        Vector<Double> tmp = new Vector<>();
        tmp = tinhV(Vt.get(index), Pbest.get(index), quanThe.get(index), Gbest);
        Vt.set(index, tmp);

    }

    public void capNhatP(int index) { // cập nhật vị trí P thứ index
        Vector<Double> tmp = new Vector<>();
        tmp = Cong(quanThe.get(index), Vt.get(index));
        quanThe.set(index, tmp);
    }

    public void capNhatBest(int b) { // cập nhật Pbest và Gbest
        // System.out.println(fit(quanThe.get(b)));
        // System.out.println(getFit(quanThe.get(b)).get(2));
        double fitt = fit(quanThe.get(b));
        if (fitt < fit(Pbest.get(b)))
            Pbest.set(b, (Vector<Double>) quanThe.get(b).clone());
        if (fitt < fit(Gbest)) {
            Gbest = (Vector<Double>) quanThe.get(b).clone();
            // System.out.println("trueeeee");
        }
        // System.out.println("Lan: "+b+ " Trung: "+ getFit(quanThe.get(b)).get(0)+" Bao
        // phu: "+ getFit(quanThe.get(b)).get(1)+" fit= "+fitt);
        // System.out.println("best: "+ " Trung: "+ getFit(Gbest).get(0)+" Bao phu: "+
        // getFit(Gbest).get(1)+" fit= "+fit(Gbest));
    }

    public void dotBien() { // dot bien
        int db = n / 5; // 10% dot bien ca the
        int mt = m / 2; // 50% gen
        for (int i = 0; i < db; i++) {
            int ra = ran.nextInt(n);
            for (int j = 0; j < mt; j++) {
                int ge = ran.nextInt(m);
                double w1 = ran.nextInt(W);
                double h1 = ran.nextInt(H);
                quanThe.get(ra).set(2 * ge, w1);
                quanThe.get(ra).set(2 * ge + 1, h1);
            }

        }
    }

    public void runPSO() {
        khoitao();
        khoitaoLuoi();
        danhgia();
        for (int i = 0; i < 500; i++) {
            int index = i % n;
            capNhatBest(index);
            capNhatVt(index);
            capNhatP(index);
            dotBien();

            // System.out.println("Lan: "+index+ " Trung: "+
            // getFit(quanThe.get(index)).get(0)+" Bao phu: "+
            // getFit(quanThe.get(index)).get(1)+" fit=
            // "+getFit(quanThe.get(index)).get(2));
            // System.out.println("best: "+ " Trung: "+ getFit(Gbest).get(0)+" Bao phu: "+
            // getFit(Gbest).get(1)+" fit= "+getFit(Gbest).get(2));
        }

    }

    public double L(Vector<Double> a, Vector<Double> b) { // tính khoảng cách vị trí a và b
        double c = sqrt(pow(a.get(0) - b.get(0), 2) + pow(a.get(1) - b.get(1), 2));
        return c;
    }

    public Vector<Double> Vec(Double a, Double b) { // tạo vecto từ a và b
        Vector<Double> c = new Vector<>();
        c.add(a);
        c.add(b);
        return c;
    }

    public Vector<Double> Cong(Vector<Double> a, Vector<Double> b) { // cộng 2 vecto
        Vector<Double> c = new Vector<>();
        for (int i = 0; i < a.size(); i++) {
            double tmp = a.get(i) + b.get(i);
            if (tmp > W)
                tmp = W;
            if (tmp < 0)
                tmp = 0;
            c.add(tmp);
        }
        return c;
    }

    public Vector<Double> tinhV(Vector<Double> Vtt, Vector<Double> Pbb, Vector<Double> Ptt, Vector<Double> Gbb) { // tính
                                                                                                                  // Vt
        Vector<Double> d = new Vector<>();
        for (int i = 0; i < Pbb.size(); i++) {
            double r1 = ran.nextDouble();
            double r2 = ran.nextDouble();
            double tmp = nn * Vtt.get(i) + aa * r1 * (Pbb.get(i) - Ptt.get(i)) + bb * r2 * (Gbb.get(i) - Ptt.get(i));
            if (tmp > Vmax)
                tmp = Vmax;
            if (tmp < Vmin)
                tmp = Vmin;
            d.add(tmp);
        }
        // for(int i=0;i<d.size();i++) System.out.println("Solve.PSO.tinhV(): "+
        // d.get(i));
        return d;
    }

    public ArrayList<Vector<Double>> getHTmin() {
        return (ArrayList<Vector<Double>>) quanThe.clone();
    }

    public Vector<Double> getGbest() {
        return (Vector<Double>) Gbest.clone();
    }

    public Vector<Integer> getFitG() {
        double fitness;
        Vector<Integer> fitG = new Vector<>();
        int d1 = 0; // so sensor trung
        int d2 = 0; // so sensor bao phu
        for (int i = 0; i < s; i++) {
            int dem = 0;
            for (int j = 0; j < m; j++) {
                // System.out.println("Solve.PSO.fit()"+ j);
                if (L(listS.get(i), Vec(Gbest.get(2 * j), Gbest.get((2 * j) + 1))) <= R) {
                    dem += 1;
                }
            }
            if (dem >= 1)
                d2 += 1;
            if (dem >= 2)
                d1 += 1;
        }
        if (d1 > 0 && d2 > 0)
            fitness = d1 / d2 * 100;
        else if (d1 == 0 && d2 > 0)
            fitness = 1 / d2 * 10000;
        else
            fitness = 999 - d1 - d2;
        fitG.add(d1);
        fitG.add(d2);

        return fitG;
    }

    public Vector<Double> getFit(Vector<Double> a) // hàm tính fitness
    {
        Vector<Double> fitG = new Vector<>();
        double fitness;
        double d1 = 0.0; // so sensor trung
        double d2 = 0.0; // so sensor bao phu
        for (int i = 0; i < s; i++) {
            int dem = 0;
            for (int j = 0; j < m; j++) {
                // System.out.println("Solve.PSO.fit()"+ j);
                if (L(listS.get(i), Vec(a.get(2 * j), a.get((2 * j) + 1))) <= R) {
                    dem += 1;
                }
            }
            if (dem >= 1)
                d2 += 1;
            if (dem >= 2)
                d1 += 1;
        }
        if (d1 > 0 && d2 > 0)
            fitness = d1 / d2 * 100;
        else if (d1 == 0 && d2 > 0)
            fitness = 1 / d2 * 100;
        else
            fitness = 999 - d1 - d2;
        fitG.add(d1);
        fitG.add(d2);
        fitG.add(fitness);
        return fitG;
    }

    public Vector<Double> getLife(Vector<Double> a) {
        Double nl[] = new Double[s]; // NL cua sensor
        Double kc[] = new Double[s]; // KC cua sensor toi sink
        Vector<Double> life = new Vector<>();
        double fitness;
        double d1 = 0.0; // so sensor trung
        double d2 = 0.0; // so sensor bao phu
        for (int i = 0; i < s; i++) { // tim KC
            double lmin = 99999;
            for (int j = 0; j < m; j++) {
                // System.out.println("Solve.PSO.fit()"+ j);
                if (L(listS.get(i), Vec(a.get(2 * j), a.get((2 * j) + 1))) <= lmin) {
                    lmin = L(listS.get(i), Vec(a.get(2 * j), a.get((2 * j) + 1)));
                }
            }
            kc[i] = lmin;

        }

        for (int i = 0; i < s; i++) { // tinh NL tieu thu
            if (kc[i] <= d0) {
                double tmp = (Eelec + Efs * pow(kc[i], 2)) * k;
                nl[i] = tmp;
            } else {
                double tmp2 = (Eelec + Emp * pow(kc[i], 4)) * k;
                nl[i] = tmp2;
            }

        }

        int round[] = new int[s];
        for (int i = 0; i < s; i++) {
            double tmp = 0.0036 / nl[i];
            int tmp2 = (int) tmp;
            round[i] = tmp2;

        }
        Arrays.sort(round);
        double nlll = 0;
        for (int i = 0; i < s; i++) {
            //
            nlll += nl[i];
        }
        // for(int i=0;i<s;i++){
        ////
        // System.out.println(nl[i]*100000000);
        // }
        String as = "";
        for (int i = 0; i < s; i++) {
            //
            as += String.valueOf(i) + " " + String.valueOf(round[i]) + "\n";
        }
        try {
            File f2 = new File("java/Data/output_1.text");
            FileWriter fw = new FileWriter(f2);
            // Bước 2: Ghi dữ liệu
            fw.write(as);
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        life.add(nlll);
        life.add((double) round[0]);

        return life;
    }

}