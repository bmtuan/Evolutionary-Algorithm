/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Solve;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author VietAnh
 */
public class GA {
	public int max = 10000;
	public static int soPark = PSO.m;
	public static Double khoangcach[][] = new Double[soPark][soPark];
	public static Random ran = new Random();
	public static int arr_dotbien[] = { 0, 5, 10, 15, 20, 25, 30, 40, 50, 60, 70, 80 };
	public static int m = khoangcach[0].length;
	public static int n = 128;
	public static int k = 3;
	public static int Vm = 5;
	public static int Tm = 10;
	public static int[][] nghiem = new int[n][];
	public static int[][] break_points = new int[n][];
	Double[] fitness = new Double[n];
	int[] person = new int[m];
	public static Double[] sum_distance = new Double[m];
	public static Double global_best = 1000000.0;
	public static int best_seq[] = new int[m];
	public static int best_bp[] = new int[k - 1];
	public static String ghi = "GA: \n";

	public void khoitaoKC(Vector<Double> gb) {
		for (int i = 0; i < soPark; i++) {
			for (int j = i + 1; j < soPark; j++) {
				double tm1 = gb.get(2 * i) - gb.get(2 * j);
				double tm2 = gb.get(2 * i + 1) - gb.get(2 * j + 1);
				double p = Math.sqrt(Math.pow(tm1, 2) + Math.pow(tm2, 2));
				khoangcach[i][j] = p;
				khoangcach[j][i] = p;
			}
			khoangcach[i][i] = 0.0;
		}
	}

	public static int ngauNhienTrongKhoang(int min, int max) {
		return (int) Math.floor(Math.random() * (max - min + 1) + min);
	}

	public void doiCho(int[] arr, int i, int j) {
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

	public void latNguoc(int[] arr, int start, int end) {
		for (int i = start; i < (start + end) / 2; i++) {
			int tmp = arr[i];
			arr[i] = arr[end + start - i];
			arr[end + start - i] = tmp;
		}
	}

	public void dichDoan(int[] arr, int start, int end, int jump) {
		int[] tmp = arr.clone();
		for (int i = start; i < start + jump; i++) {
			arr[i] = tmp[end - start + i + 1];
		}
		for (int i = start + jump; i <= end + jump; i++) {
			arr[i] = tmp[i - jump];
		}
	}

	public void khoitao() {
		for (int i = 0; i < m; i++) {
			person[i] = i;
		}
		for (int i = 0; i < n; i++) {
			int[] clone_person = person.clone();
			nghiem[i] = new int[m];
			for (int j = 0; j < m; j++) {
				int index = ran.nextInt(m);
				doiCho(clone_person, 0, index);
			}
			nghiem[i] = clone_person;
		}
		for (int i = 0; i < n; i++) {
			break_points[i] = new int[k + 1];
			break_points[i][0] = 0;
			for (int j = 1; j < k; j++) {
				break_points[i][j] = ngauNhienTrongKhoang(break_points[i][j - 1] + 1, m - 2);
			}
			break_points[i][k] = m - 1;
		}
		// System.out.print("Best: " + global_best);
		// ghi += "Best: " + global_best + ", Sequences: ";
		ghi += "Khoitao: \nSequences: ";
		for (int j = 0; j < best_seq.length; j++)
			ghi += nghiem[3][j] + ",";
		ghi += " Break points: ";
		for (int j = 1; j < k; j++) {
			ghi += break_points[3][j] + ",";
		}
		ghi += "\n";
	}

	public void fitness() {
		for (int i = 0; i < n; i++) {
			Double max_path = -1.0;
			sum_distance[0] = 0.0;
			for (int j = 1; j < m; j++) {
				sum_distance[j] = sum_distance[j - 1] + khoangcach[nghiem[i][j - 1]][nghiem[i][j]];
			}
			for (int l = 1; l <= k; l++) {
				// System.out.println(break_points[i][l]);
				max_path = Math.max(max_path, sum_distance[break_points[i][l]] - sum_distance[break_points[i][l - 1]]
						+ khoangcach[nghiem[i][break_points[i][l]]][nghiem[i][break_points[i][l - 1]]]);
			}
			fitness[i] = max_path;
			// System.out.println(max_path);
		}
	}

	public double getFit(int[] sq, int[] bp) {
		double fit;
		Double max_path = -1.0;
		sum_distance[0] = 0.0;
		for (int j = 1; j < m; j++) {
			sum_distance[j] = sum_distance[j - 1] + khoangcach[sq[j - 1]][sq[j]];
		}
		for (int l = 1; l <= k; l++) {
			// System.out.println(break_points[i][l]);
			max_path = Math.max(max_path, sum_distance[bp[l]] - sum_distance[bp[l - 1]]
					+ khoangcach[sq[bp[l]]][sq[bp[l - 1]]]);
		}
		;
		if (max_path == -1.0) {
			System.out.println("code ngu vl");
		}
		return max_path;
		// System.out.println(max_path);

	}

	public void chonloc() {
		Double[] temp = fitness.clone();
		Arrays.sort(temp);
		Double nguong = temp[n * 70 / 100];
		for (int i = 0; i < n; i++) {
			if (fitness[i] > nguong) {
				nghiem[i] = nghiem[ran.nextInt(n)].clone();
				break_points[i] = break_points[ran.nextInt(n)].clone();
			}
		}
	}

	public void laighep() {
		for (int i = 0; i < n / 3; i++) {
			int cha = ran.nextInt(n);
			int me = ran.nextInt(n);
			int[] tmp = new int[nghiem[cha].length];
			ArrayList<Integer> tmp1 = new ArrayList<>();
			for (int j = 0; j < nghiem[cha].length; j++) {
				if (ran.nextInt(2) == 1) {
					for (int k = 0; k < nghiem[cha].length; k++) {
						if (!tmp1.contains(nghiem[cha][k])) {
							tmp1.add(nghiem[cha][k]);
							break;
						}
					}
				} else {
					for (int k = 0; k < nghiem[cha].length; k++) {
						if (!tmp1.contains(nghiem[me][k])) {
							tmp1.add(nghiem[me][k]);
							break;
						}
					}
				}
			}
			for (int k = 0; k < nghiem[cha].length; k++)
				tmp[k] = tmp1.get(k);
			if (getFit(tmp, break_points[cha]) < getFit(nghiem[cha], break_points[cha]))
				nghiem[cha] = tmp.clone();
			else if (getFit(tmp, break_points[me]) < getFit(nghiem[me], break_points[me]))
				nghiem[me] = tmp.clone();
		}
	}

	// Randomly flip the fragments in the sequence of the optimal population.
	public void dotbien_1(int[] arr) {
		int start = ran.nextInt(m);
		int end = ngauNhienTrongKhoang(start, m - 1);
		latNguoc(arr, start, end);
	}

	// Randomly swap the different nodes in the sequence of the optimal population.
	public void dotbien_2(int[] arr) {
		int i = ran.nextInt(m);
		int j = ran.nextInt(m);
		doiCho(arr, i, j);
	}

	// Randomly translate a fragment in the sequence of the optimal population.
	public void dotbien_3(int[] arr) {
		int start = ran.nextInt(m - 2);
		int end = ngauNhienTrongKhoang(start + 1, m - 2);
		int jump = ngauNhienTrongKhoang(1, m - end - 1);
		dichDoan(arr, start, end, jump);
	}

	// Randomly modify the break points of the optimal population.
	public void dotbien_4(int[] arr) {
		if (k != 1) {
			int index = ngauNhienTrongKhoang(1, k - 1);
			arr[index] = ran.nextInt(m);
		}
		Arrays.sort(arr);
	}

	// Randomly flip the fragments in the sequence and modify the break points of
	// the optimal population.
	public void dotbien_5(int[] sequence, int[] break_point) {
		dotbien_1(sequence);
		dotbien_4(break_point);
	}

	// Randomly swap the different nodes in the sequence and modify the break points
	// of the optimal population
	public void dotbien_6(int[] sequence, int[] break_point) {
		dotbien_2(sequence);
		dotbien_4(break_point);
	}

	// Randomly translate a fragment in the sequence and modify the break points of
	// the optimal population.
	public void dotbien_7(int[] sequence, int[] break_point) {
		dotbien_3(sequence);
		dotbien_4(break_point);
	}

	public void dotbien() {
		Double[] temp = fitness.clone();
		Arrays.sort(temp);
		int i_dotbien = 0;
		Double nguong = 0.0;
		for (int j = 0; j < arr_dotbien.length; j++) {
			nguong = temp[arr_dotbien[j]];
			for (int i = 0; i < n; i++) {
				if (fitness[i] == nguong)
					i_dotbien = i;
			}
			int rd_dotbien = ngauNhienTrongKhoang(1, 7);
			int[] tmp_nghiem = nghiem[i_dotbien].clone();
			int[] tmp_breakp = break_points[i_dotbien].clone();
			switch (rd_dotbien) {
				case 1:
					dotbien_1(tmp_nghiem);
					break;
				case 2:
					dotbien_2(tmp_nghiem);
					break;
				case 3:
					dotbien_3(tmp_nghiem);
					break;
				case 4:
					dotbien_4(tmp_breakp);
					break;
				case 5:
					dotbien_5(tmp_nghiem, tmp_breakp);
					break;
				case 6:
					dotbien_6(tmp_nghiem, tmp_breakp);
					break;
				case 7:
					dotbien_7(tmp_nghiem, tmp_breakp);
					break;
				default:
					break;
			}
			Double fit = getFit(tmp_nghiem, tmp_breakp);

			if (fit < getFit(nghiem[i_dotbien], break_points[i_dotbien])) {
				nghiem[i_dotbien] = tmp_nghiem.clone();
				break_points[i_dotbien] = tmp_breakp.clone();
			}
		}
	}

	public void runGA(Vector<Double> Gbest) {
		khoitaoKC(Gbest);
		khoitao();
		fitness();
		for (int i = 0; i < 300; i++) {
			fitness();
			Print();
			chonloc();
			dotbien();

		}

	}

	public void Print() {
		Double[] temp = fitness.clone();
		Arrays.sort(temp);
		Double best = -1.0;
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] != 0) {
				best = temp[i];
				break;
			}
		}
		if (global_best > best) {
			global_best = best;
			for (int i = 0; i < n; i++) {
				if (fitness[i] == best) {
					best_seq = nghiem[i].clone();
					best_bp = break_points[i].clone();
					break;
				}
			}
		}
		System.out.print("Best: " + global_best);
		System.out.print(" Sequences: ");
		for (int j = 0; j < best_seq.length; j++)
			System.out.print(best_seq[j] + ",");
		System.out.print("Break points: ");
		for (int j = 1; j < k; j++) {
			System.out.print(best_bp[j] + ",");
		}
		System.out.println();
	}

	public void capNhatBest() {
		Double[] temp = fitness.clone();
		Arrays.sort(temp);
		Double best = -1.0;
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] != 0) {
				best = temp[i];
				break;
			}
		}
		if (global_best > best) {
			global_best = best;
			for (int i = 0; i < n; i++) {
				if (fitness[i] == best) {
					best_seq = nghiem[i].clone();
					best_bp = break_points[i].clone();
					break;
				}
			}
		}

	}

	public void print_populations() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				System.out.print(nghiem[i][j] + " ");
			}
			System.out.print(" Break_points: ");
			for (int j = 0; j <= k; j++) {
				System.out.print(break_points[i][j] + " ");
			}
			System.out.println("Max path: " + fitness[i]);
			for (int j = 0; j < m; j++) {
				System.out.print(sum_distance[j] + " ");
			}
			System.out.println();
			for (int j = 0; j < m - 1; j++) {
				System.out.print(khoangcach[nghiem[i][j]][nghiem[i][j + 1]] + " ");
			}
			System.out.println();
			System.out.println();
		}
	}

	public void generate_graph() {
		int[][] a = new int[20][20];
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				if (i == j) {
					a[i][j] = 0;
				} else
					a[i][j] = -1;
			}
		}

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				int x = ran.nextInt(20);
				if (a[i][j] == -1) {
					a[i][j] = x;
					a[j][i] = x;
				}
			}
		}
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				System.out.print(a[i][j] + ",");
			}
			System.out.println();
		}
	}

	public double getTre() {
		Double max_path = -1.0;
		sum_distance[0] = 0.0;
		for (int j = 1; j < m; j++) {
			sum_distance[j] = sum_distance[j - 1] + khoangcach[best_seq[j - 1]][best_seq[j]];
		}
		for (int l = 1; l <= k; l++) {
			int Nm = 0;
			if (l == k)
				Nm = m - best_bp[l - 1];
			else if (l == 1)
				Nm = best_bp[0];
			else
				Nm = best_bp[l - 1] - best_bp[l - 2];
			double lm = sum_distance[best_bp[l]] - sum_distance[best_bp[l - 1]]
					+ khoangcach[best_seq[best_bp[l]]][best_seq[best_bp[l - 1]]];
			double tre = lm / Vm + Tm * Nm;

			max_path = Math.max(max_path, tre);
		}
		return max_path;

	}

	public Double getBest() {
		return global_best;
	}

	public int[] getBestSQ() {
		return best_seq;
	}

	public int[] getBestBP() {
		return best_bp;
	}

}