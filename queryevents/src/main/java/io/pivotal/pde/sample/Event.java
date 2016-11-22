package io.pivotal.pde.sample;

public class Event {

	private String date;
	private String type;
	private String state;
	private int count;
	
	public Event() {
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "Event [date=" + date + ", type=" + type + ", state=" + state + ", count=" + count + "]";
	}

	
}
