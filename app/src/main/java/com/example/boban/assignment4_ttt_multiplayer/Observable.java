package com.example.boban.assignment4_ttt_multiplayer;

public interface Observable {
	void notifyListeners();
	void registerObserver(Observer obs);
}
