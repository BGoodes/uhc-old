package fr.aiidor.uhc.enums;

public enum Permission {
	
	ALL, NONE, CONFIG, LOG, ALERT, CHAT;
	
	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
