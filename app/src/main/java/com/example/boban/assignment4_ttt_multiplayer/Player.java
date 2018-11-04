package com.example.boban.assignment4_ttt_multiplayer;

public class Player {


    String symbol;
	String name;
	DataCell[] dataCell = new DataCell[9];
	
	public Player(String s, String s1) {
		symbol = s;
		name = s1;
		for(int i = 0; i < dataCell.length; i++) {
			dataCell[i] = new DataCell();
		}
	}
	
	public void MarkCell(int x) {
		dataCell[x].setSymbol(symbol);
	}
	
	public void register(Observer obs, int y) {
		for(int i = 0; i < dataCell.length; i++) {
			dataCell[i].registerObserver(obs);
		}
	}
	
	public String getSymbol() {
		return symbol;
	}

    public String getName() {
        return name;
    }

	public void unRegister() {
		for (int i = 0; i < dataCell.length; i++) {
			dataCell[i].clear();
		}
	}

}
