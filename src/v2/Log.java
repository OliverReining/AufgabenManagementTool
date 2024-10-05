package v2;

import java.text.*;
import java.util.*;

public class Log {
	
	private String message;
	private String timestamp;
	private LogType type; 
	
	public enum LogType{
		INFO,
		ERROR,
		WARNING, 
		OPEN,
		EXIT,
		LOAD,
		SUCCESS
	}

    // Konstruktor
    public Log(String message, LogType type) {
        setMessage(message);
        setType(type);
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        
    }

    public void setMessage(String message) {
		this.message = message;
	}

	// Getter für die Nachricht
    public String getMessage() {
        return message;
    }

    // Getter für den Zeitstempel
    public String getTimestamp() {
        return timestamp;
    }

    // Methode, um das Log als String darzustellen (mit Zeitstempel und Typ)
    @Override
    public String toString() {
        return "[" + type + "] - [" + timestamp + "] --" + message;
    }

	public LogType getType() {
		return type;
	}

	public void setType(LogType type) {
		this.type = type;
	}
}