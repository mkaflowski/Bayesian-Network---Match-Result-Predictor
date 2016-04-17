package pl.kaflowski.psi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Node {

	private String name;
	private String[] options_names; // options names
	private int options_number; // number of options
	private Node[] link;
	private int links_number;
	private float[] prob_table;
	private int prob_number; // number of probsibilities
	private int[][] link_id; // prob_index; link state -
								// [prob_number][links_number]
	private int state; // -1 - unset

	// names separated with comma
	public Node(String name2, String options_names2) {
		name = name2;
		options_names = options_names2.split(",");
		options_number = options_names.length;
		link = null;
		links_number = 0;
		prob_table = null;
		prob_number = options_number;
		prob_table = new float[prob_number];
		state = -1;
	}

	public void addLink(Node node) {
		links_number++;
		Node[] tmp = new Node[links_number];
		for (int i = 0; i < links_number - 1; i++)
			tmp[i] = link[i];
		tmp[links_number - 1] = node;
		link = tmp;

		prob_number = link[0].options_number;
		for (int i = 1; i < tmp.length; i++) {
			prob_number *= link[i].options_number;
		}
		prob_number *= options_number;
		prob_table = new float[prob_number];

		link_id = new int[prob_number][links_number];
		int iloraz = options_number;

		for (int i = links_number - 1; i >= 0; i--) {
			int tmp_o = 0;
			for (int j = 0; j < prob_number; j++) {
				if (j % iloraz == 0 && j != 0) {
					tmp_o = tmp_o + 1;
					if (tmp_o >= link[i].options_number)
						tmp_o = 0;
				}
				link_id[j][i] = tmp_o;
			}
			iloraz *= link[i].options_number;
		}
	}

	public String getName() {
		return name;
	}
	
	public Node getLink(int i){
		return link[i];
	}
	
	public int getLinkNumber(){
		return links_number;
	}

	public String[] getOptionsNames() {
		String names[] = new String[options_number];
		for (int i = 0; i < options_names.length; i++) {
			names[i] = options_names[i];
		}
		return names;
	}

	public String getOptionName(int i) {
		return options_names[i];
	}

	public int getOptionNumber() {
		return options_number;
	}

	public float calc(int state1) {
		if (state != -1)
			if (state1 == state)
				return 1f;
			else
				return 0f;

		float prob = 0;
		for (int i = state1; i < prob_number; i += options_number) {
			float tmp = prob_table[i];
			for (int j = 0; j < links_number; j++) {
				tmp *= link[j].calc(link_id[i][j]);
			}
			prob += tmp;
		}
		return prob;
	}

	public void setState(int x) {
		state = x;
	}
	
	public int getState() {
		return state;
	}

	public void delState() {
		state = -1;
	}

	public void printOptions() {
		System.out.print("\n\nNumber of options: " + options_number);
		for (int i = 0; i < options_names.length; i++) {
			System.out.print("\n");
			System.out.print(options_names[i]);
		}
	}

	public void printProbabilities() {
		System.out.print("\n\nNumber of probsibilities: " + prob_number + "\n");

		for (int j = 0; j < links_number; j++)
			System.out.print(link[j].name + "\t");

		System.out.print("\n");

		for (int i = 0; i < links_number; i++) {
			System.out.print("\t");
		}

		for (int i = 0; i < options_number; i++) {
			System.out.print(options_names[i]);
			System.out.print("\t");
		}

		for (int i = 0; i < prob_number; i++) {
			if (i % options_number == 0) {
				System.out.print("\n");
				for (int j = 0; j < links_number; j++) {
					System.out.print(link[j].options_names[link_id[i][j]]
							+ "\t");
				}
			}

			for (int j = 0; j < links_number; j++) {
			}

			System.out.print(prob_table[i]);
			System.out.print("\t");
		}
	}

	public void setPTable(float[] p, boolean bprint) {
		for (int i = 0; i < prob_number; i++) {
			if (i >= p.length)
				break;
			if (bprint)
				System.out.print("probsibility of - "
						+ options_names[i % options_number] + ": " + p[i]);
			prob_table[i] = p[i];
			if (bprint)
				System.out.print("\n");
		}
	}

	int index;

	public void setPTable(boolean toFile) throws IOException {
		index = 0;
		if (new File(name + ".bpt").exists()) {
			System.out.print("File " + name + ".bpt" + " .Overwrite?\nY/N\n");
			Scanner scanner = new Scanner(System.in);
			if (!scanner.next().equals("Y")) {
				setPTable();
				return;
			}
		}
		FileWriter fstream = new FileWriter(name + ".bpt");
		BufferedWriter out = new BufferedWriter(fstream);
		System.out.print(name.toUpperCase());
		setTable(0, toFile, out);
		out.close();
	}

	private void setTable(int i, boolean toFile, BufferedWriter out)
			throws IOException {
		Scanner scanner = new Scanner(System.in);
		if (i == links_number) {
			System.out.print("\n");
			for (int j = 0; j < options_number; j++) {
				System.out
						.print("probsibility of - " + options_names[j] + ": ");
				if (scanner.hasNextFloat())
					prob_table[index] = scanner.nextFloat();
				else
					prob_table[index] = 0;
				out.write(Float.toString(prob_table[index]) + " ");
				index++;
			}
		} else
			for (int j = 0; j < link[i].options_number; j++) {
				System.out.print("\t" + link[i].name + ": "
						+ link[i].options_names[j] + "  ");
				setTable(i + 1, toFile, out);
			}
	}

	public void setPTable() throws IOException {
		// TODO Auto-generated method stub
		File file = new File(name + ".bpt");
		Scanner scanner = null;

		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.print("FILE " + file.getPath() + " NOT FOUD!");
			setPTable(true);
			return;
		}
		int i = 0;
		while (scanner.hasNext() && i < prob_number) {
			prob_table[i] = Float.parseFloat(scanner.next());
			i++;
		}
	}
}
