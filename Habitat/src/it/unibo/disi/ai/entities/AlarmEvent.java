package it.unibo.disi.ai.entities;

import it.unibo.disi.ai.app.aimodule.AIModule;

public class AlarmEvent {

	private String alarmId;
	private String patId;
	private String opId;
	private String locationURI;
	private String detail;
	private String answer;
	private AIModule aiModule;
	public long timestamp;	
	
	public AlarmEvent(String alarmId, String patId, String opId, String locationURI, String detail, String answer,
			AIModule aiModule) {
		this.alarmId = alarmId;
		this.patId = patId;
		this.opId = opId;
		this.locationURI = locationURI;
		this.detail = detail;
		this.answer = answer;
		this.aiModule = aiModule;
		this.timestamp = System.currentTimeMillis();
	}
	
	
	public String getAlarmId() {return alarmId;}
	public void setAlarmId(String alarmId) {this.alarmId = alarmId;}

	public String getPatId() {return patId;	}
	public void setPatId(String patId) {this.patId = patId;	}

	public String getOpId() {return opId;}
	public void setOpId(String opId) {this.opId = opId;}

	public String getLocationURI() {return locationURI;}
	public void setLocationURI(String locationURI) {this.locationURI = locationURI;}
	
	public String getDetail() {return detail;}
	public void setDetail(String detail) {this.detail = detail;}

	public String getAnswer() {return answer;}
	public void setAnswer(String answer) {this.answer = answer;	}

	public AIModule getAiModule() {return aiModule;	}
	public void setAiModule(AIModule aiModule) {this.aiModule = aiModule;}


//	public void setId(String i) { id = i; }
//	public void setLocationURI(String loc) { locationURI = loc; }
//	public void setX(String x1) { x = Integer.parseInt(x1); }
//	public void setY(String y1) { y = Integer.parseInt(y1); }
//	public void setAIModule(AIModule ai) { aiModule = ai; }
//	
//	public String getId() { return id; }
//	public String getLocationURI() { return locationURI; }
//	public Integer getX() { return x; }
//	public Integer getY() { return y; }
//	public AIModule getAIModule() { return aiModule; }
	
}
