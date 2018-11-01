package com.example.boban.assignment4_ttt_multiplayer;

import java.util.ArrayList;

public class DataCell implements Observable{
	private String symbol;
	ArrayList<Observer> observers= new ArrayList<Observer>();
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
		notifyListeners();
	}

	@Override
	public void notifyListeners() {
		for(int i = 0; i < observers.size(); i++) {
			observers.get(i).update(this.symbol);
		}
	}

	@Override
	public void registerObserver(Observer obs) {
		observers.add(obs);
	}

	public void clear() {
		for (int i = 0; i < observers.size(); i++) {
			observers.get(i).update("");
		}
		observers.clear();
	}

}
