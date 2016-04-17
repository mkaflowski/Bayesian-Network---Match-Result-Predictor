package pl.kaflowski.psi;

import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

public class MyWindow extends JFrame implements ActionListener {

	JButton button;

	JButton bclear, bclearall;
	JComboBox nodesList;
	JComboBox cbleague, cbteam1, cbteam2;
	JRadioButton[] rboptions;
	ButtonGroup bgroup;
	JLabel result;
	JTextArea linfo;
	Network net;
	String[] league_list = { "Serie A", "Premier League" };

	public MyWindow(Network net) {
		super("Football Typer");
		setSize(420, 300);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setIconImage(Toolkit.getDefaultToolkit()
				  .getImage("icon.png"));

		this.net = net;

		// inicjalizacja: =============================================
		button = new JButton("Licz prawdopodobieñstwo");
		button.addActionListener(this);

		cbleague = new JComboBox(league_list);
		cbteam1 = new JComboBox();
		cbteam2 = new JComboBox();

		nodesList = new JComboBox(net.getNodesNames());
		bclear = new JButton("Czyœæ");
		bclearall = new JButton("Czyœæ wszystko");

		bgroup = new ButtonGroup();
		rboptions = new JRadioButton[3];
		for (int i = 0; i < rboptions.length; i++) {
			rboptions[i] = new JRadioButton();
			bgroup.add(rboptions[i]);
		}
		result = new JLabel("Wynik");
		linfo = new JTextArea();

		// listenery: =================================================
		nodesList.addActionListener(this);
		cbleague.addActionListener(this);
		cbteam2.addActionListener(this);
		cbteam1.addActionListener(this);

		for (int i = 0; i < rboptions.length; i++) {
			rboptions[i] = new JRadioButton();
			bgroup.add(rboptions[i]);
			rboptions[i].addActionListener(this);
		}

		bclear.addActionListener(this);
		bclearall.addActionListener(this);
		result = new JLabel("Wynik");

		// dodawanie na ekran: =========================================
		add(button);

		add(cbleague);
		add(cbteam1);
		add(cbteam2);

		add(nodesList);
		add(bclear);
		add(bclearall);
		for (int i = 0; i < rboptions.length; i++) {
			add(rboptions[i]);
		}
		add(result);
		add(linfo);

		getTeams();
		setRadioButtons();
		setInfo();

		setLayout(new FlowLayout());
		setVisible(true);
	}

	private void setInfo() {
		int index = nodesList.getSelectedIndex();
		String info = net.nodes[index].getName() + " zale¿y od:\n";
		if (net.nodes[index].getLinkNumber() != 0) {
			for (int i = 0; i < net.nodes[index].getLinkNumber(); i++) {
				info +="- " + net.nodes[index].getLink(i).getName().toLowerCase();
				if (i != net.nodes[index].getLinkNumber() - 1)
					info += ",\n";
			}
		} else
			info = "      UZUPE£NIJ DANE      ";
		if (bgroup.getSelection() != null)
			info = "";
		linfo.setText(info);
	}

	// wczytywanie dru¿yn do comboboxów
	private void getTeams() {
		cbteam1.removeAllItems();
		cbteam2.removeAllItems();
		File file = new File("teams/"
				+ cbleague.getSelectedItem().toString().toLowerCase()
				+ "/@liga.txt");
		Scanner scanner;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.print("File: " + file.getPath() + " not found!");
			return;
		}
		while (scanner.hasNextLine()) {
			String tmp = scanner.nextLine();
			cbteam1.addItem(tmp);
			cbteam2.addItem(tmp);
		}
		cbteam2.setSelectedIndex(1);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == nodesList) {
			setRadioButtons();
			setInfo();
			return;
		}

		if (source == button) {
			String string = "";
			string += cbteam1.getSelectedItem().toString()
					+ ": "
					+ Float.toString(Math
							.round(net.getLastNode().calc(0) * 10000f) / 100f)
					+ "%  ";
			string += "remis"
					+ ": "
					+ Float.toString(Math
							.round(net.getLastNode().calc(1) * 10000f) / 100f)
					+ "%  ";
			string += cbteam2.getSelectedItem().toString()
					+ ": "
					+ Float.toString(Math
							.round(net.getLastNode().calc(2) * 10000f) / 100f)
					+ "%";

			result.setText(string);
		}

		if (source == cbleague) {
			cbteam1.removeActionListener(this);
			cbteam2.removeActionListener(this);
			getTeams();
			cbteam2.addActionListener(this);
			cbteam1.addActionListener(this);
			clearAll();
			return;
		}

		if (source == cbteam1) {
			net.setStatesFromFileHome(cbteam1.getSelectedItem().toString()
					.toLowerCase(), cbleague.getSelectedItem().toString()
					.toLowerCase());
			setRadioButtons();
			return;
		}
		if (source == cbteam2) {
			net.setStatesFromFileAway(cbteam2.getSelectedItem().toString()
					.toLowerCase(), cbleague.getSelectedItem().toString()
					.toLowerCase());
			setRadioButtons();
			return;
		}

		for (int i = 0; i < rboptions.length; i++) {
			if (source == rboptions[i]) {
				net.nodes[nodesList.getSelectedIndex()].setState(i);
			}
			setInfo();
		}

		if (source == bclear) {
			bgroup.clearSelection();
			net.nodes[nodesList.getSelectedIndex()].delState();
			setInfo();
			return;
		}

		if (source == bclearall) {
			clearAll();
			setInfo();
		}
	}

	private void clearAll() {
		for (int i = 0; i < net.nodes.length - 1; i++) {
			net.nodes[i].delState();
		}
		bgroup.clearSelection();
		net.setStatesFromFileHome(cbteam1.getSelectedItem().toString()
				.toLowerCase(), cbleague.getSelectedItem().toString()
				.toLowerCase());
		net.setStatesFromFileAway(cbteam2.getSelectedItem().toString()
				.toLowerCase(), cbleague.getSelectedItem().toString()
				.toLowerCase());
		setRadioButtons();
	}

	private void setRadioButtons() {
		bgroup.clearSelection();
		for (int i = 0; i < rboptions.length; i++) {
			rboptions[i].setText(net.getNodeOptionsNames(nodesList
					.getSelectedIndex())[i]);
			if (net.nodes[nodesList.getSelectedIndex()].getState() == i)
				rboptions[i].setSelected(true);
			else
				rboptions[i].setSelected(false);
		}
	}
}


