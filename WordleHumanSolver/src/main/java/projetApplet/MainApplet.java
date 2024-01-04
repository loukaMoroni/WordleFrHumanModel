package projetApplet;

import java.applet.Applet;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JApplet;

import projet.Francais;
import projet.Main;
import projet.WordInfo;
import projet.IHM.IhmWordle;
import projet.saveData.DixData;

public class MainApplet extends Applet{
	private static final long serialVersionUID = 1L;
	
	private static MainApplet main;
	public static MainApplet getMain() { return main; }
	private IhmWordle ihm;
	public IhmWordle getIHM() { return ihm; }
	private Thread gameTask;
	public Thread getGameTask() { return gameTask; }
	public boolean mode = true;//mode true = humain / false = modele
	public static final List<String> chosenWords = new ArrayList<>(List.of(new String[]{"VENEZ","CARNE","RAMPE","LEGOS","TASSE","FIXER","NAZES","TIRES","SALTO","ELEVE"}));
	
    public static void main(String[] args) {
    	MainApplet ma = new MainApplet();
    	ma.setVisible(true);
    }
    public void init() {
		main = this;
		loadWords();
		gameTask = new Thread() {
			@Override
			public void run() {
				ihm.clear();
				ihm.log("human start game");
				new Francais().play10(chosenWords);
			}
		};
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ihm = new IhmWordle();
					add(ihm.getContentPane());
					ihm.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }
    public static final List<WordInfo> wordsInfo = new ArrayList<>();

    public static void loadWords() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(MainApplet.class.getResourceAsStream("/motsFR.csv")));
		    String line = br.readLine();
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(",");
		        wordsInfo.add(new WordInfo(values[0],values[1],Float.parseFloat(values[2]),Integer.parseInt(values[3]),Integer.parseInt(values[4])));
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<WordInfo> getWordsInfo(){
			return wordsInfo;
	}

	public Thread resetGameTask() {
//		if(ihm.data!=null)ihm.data.save(ihm.tabGues);
		Thread temp = new Thread() {
			@Override
			public void run() {
				ihm.datas = new DixData();
				ihm.clear();
				ihm.log("game start");
				new Francais().play10(MainApplet.chosenWords);
			}
		};
		return temp;
	}
}
