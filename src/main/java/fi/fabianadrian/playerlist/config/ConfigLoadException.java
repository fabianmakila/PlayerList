package fi.fabianadrian.playerlist.config;

public class ConfigLoadException extends RuntimeException {
	public ConfigLoadException() {

	}

	public ConfigLoadException(String message) {
		super(message);
	}
}
