package org.myservice.data;

public class SmsData {

	String sender;
	String content;
	String timeStamp;
	
	public SmsData(String sender, String content, String timeStamp) {
		this.sender = sender;
		this.content = content;
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString() {
		return sender + "|" + content + "|" + timeStamp + "\n";
	}
	
	
}
