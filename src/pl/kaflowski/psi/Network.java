package pl.kaflowski.psi;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Network {

	Node[] nodes;
	private int nodes_number;

	public Network() {
		nodes_number = 0;
	}

	public void addNode(String name, String options_names) {
		nodes_number++;
		Node[] tmp = new Node[nodes_number];
		for (int i = 0; i < nodes_number - 1; i++) {
			tmp[i] = nodes[i];
		}
		tmp[nodes_number - 1] = new Node(name, options_names);
		nodes = tmp;
	}

	//ustawia stany z pliku dla wêz³ów gospodarza
	public void setStatesFromFileHome(String filename, String folder){
		File file = new File("teams/"+folder+"/"+filename + ".txt");
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.print("FILE " + file.getPath() + " NOT FOUD!");
			return;
		}
		nodes[0].setState(scanner.nextInt());
		nodes[6].setState(scanner.nextInt());
		//nodes[9].setState(scanner.nextInt());
	}
	
	//ustawia stany z pliku dla wêz³ów rywala
	public void setStatesFromFileAway(String filename, String folder){
		File file = new File("teams/"+folder+"/"+filename + ".txt");
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.print("FILE " + file.getPath() + " NOT FOUD!\n");
			return;
		}

		nodes[10].setState(scanner.nextInt());
		nodes[16].setState(scanner.nextInt());
	}
	
	public int getNodesNumber() {
		return nodes_number;
	}
	
	// nazwy bez ostatniego wêz³a
	public String[] getNodesNames(){
		String[] names = new String[nodes_number-1];
		for (int i = 0; i < names.length; i++) {
			names[i] = nodes[i].getName();
		}
		return names;
	}

	public String[] getNodeOptionsNames(int i) {
		return nodes[i].getOptionsNames();
	}
	
	public Node getLastNode(){
		return nodes[nodes_number-1];
	}
}
