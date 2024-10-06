package v2;

import java.text.*;
import java.util.*;

public class Log {

	private String message;
	private String timestamp;
	private LogType type;
	private Manager manager;

	public enum LogType {
		INFO, ERROR, WARNING, OPEN, EXIT, SUCCESS, SQLEXCEPTION,
	}

	public enum Manager {
		USER_MANAGER, PROJECT_MANAGER, TASK_MANAGER, WORKTIME_MANAGER, LOG_MANAGER, DB_CONNECT, GUI,
	}

	// Konstruktor keine leeren Logs möglich
	public Log(String message, LogType type, Manager manager) {
		setMessage(message);
		setType(type);
		setManager(manager);
		this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

	}

	// Methode, um das Log als String darzustellen (mit Zeitstempel, von welcher Klasse und Typ)
	@Override
	public String toString() {
		return "[" + timestamp + "] - " + message + " - [" + type + "] - [" + manager + "]";
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

	public LogType getType() {
		return type;
	}

	public void setType(LogType type) {
		this.type = type;
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}
}