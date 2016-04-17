package pl.kaflowski.psi;

import java.awt.EventQueue;
import java.io.IOException;

public class BayesianNetwork {
	
	static Network net;

	public static void main(String[] args) throws IOException {

		net = new Network();
		
		net.addNode("Liczba dni od ostatniego meczu", "0-3,4-5,6-8"); //0
		net.addNode("Przygotowanie przedsezonowe", "dobre,œrednie,z³e"); //1
		net.addNode("Wysi³ek w ostatnich spotkaniach", "du¿y,œredni,ma³y"); //2
		net.addNode("Przygotowanie fizyczne", "dobre,œrednie,z³e"); //3 
		net.nodes[3].addLink(net.nodes[0]);
		net.nodes[3].addLink(net.nodes[1]);
		net.nodes[3].addLink(net.nodes[2]);
		
		net.addNode("Morale", "dobre,œrednie,z³e"); //4 
		
		net.addNode("Forma w ostatnim meczu", "dobra,œrednia,z³a"); //5
		net.addNode("Zwyciêstwa w ostatnich meczach", "wiêkszoœæ,po³owa,ma³o"); //6
		net.addNode("Forma", "dobra,œrednia,z³a"); //7   !!!
		net.nodes[4].addLink(net.nodes[5]);
		net.nodes[7].addLink(net.nodes[5]);
		net.nodes[7].addLink(net.nodes[6]);
		net.nodes[7].addLink(net.nodes[3]);
		net.nodes[7].addLink(net.nodes[4]);
		
		
		net.addNode("Zwyciêstwa w ostatnich meczach z rywalem", "wiêkszoœæ,po³owa,ma³o"); //8   !!!
		
		net.addNode("Lokalizacja spotkania", "dom,wyjazd"); //9   !!!
		
		net.addNode("Liczba dni od ostatniego meczu rywala", "0-3,4-5,6-8"); //10
		net.addNode("Przygotowanie przedsezonowe rywala", "dobre,œrednie,z³e"); //11
		net.addNode("Wysi³ek w ostatnich spotkaniach rywala", "du¿y,œredni,ma³y"); //12
		net.addNode("Przygotowanie fizyczne rywala", "dobre,œrednie,z³e"); //13
		net.nodes[13].addLink(net.nodes[10]);
		net.nodes[13].addLink(net.nodes[11]);
		net.nodes[13].addLink(net.nodes[12]);
		
		net.addNode("Morale rywala", "dobre,œrednie,z³e"); //14 
		
		net.addNode("Forma w ostatnim meczu rywala", "dobra,œrednia,z³a"); //15
		net.addNode("Zwyciêstwa w ostatnich meczach rywala", "wiêkszoœæ,po³owa,ma³o"); //16
		net.addNode("Forma rywala", "dobra,œrednia,z³a"); //17   !!!
		net.nodes[14].addLink(net.nodes[15]);
		net.nodes[17].addLink(net.nodes[15]);
		net.nodes[17].addLink(net.nodes[16]);
		net.nodes[17].addLink(net.nodes[13]);
		net.nodes[17].addLink(net.nodes[14]);
		
		net.addNode("Zmiany taktyczne", "du¿e,œrednie,brak"); //18   !!!
		net.addNode("Zmiany taktyczne u rywala", "du¿e,œrednie,brak"); //19   !!!
		
		
		net.addNode("Rezultat", "zwyciêstwo,remis,pora¿ka"); //20 REZULTAT
		
		net.nodes[20].addLink(net.nodes[7]);
		net.nodes[20].addLink(net.nodes[8]);
		//net.nodes[20].addLink(net.nodes[9]);
		net.nodes[20].addLink(net.nodes[17]);
		net.nodes[20].addLink(net.nodes[18]);
		net.nodes[20].addLink(net.nodes[19]);
		
		for (int i = 0; i < net.nodes.length; i++) {
			//net.nodes[i].setPTable(true);
			net.nodes[i].setPTable();
		}
		
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				new MyWindow(net);
			}
		});
	}
}
