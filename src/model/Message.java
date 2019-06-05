package model;

import java.util.List;
import java.util.Set;

public class Message {
	private String from;
    private String to;
    private String content;
    private Set<String> activeUsernames;
    private List<Message> allMessages;
    private String sentAt;

    @Override
    public String toString() {
        return super.toString();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public Set<String> getActiveUsernames(){
    	return activeUsernames;
    }
    
    public void setActiveUsernames(Set<String> activeUsernames) {
        this.activeUsernames = activeUsernames;
    }

	public List<Message> getAllMessages() {
		return allMessages;
	}

	public void setAllMessages(List<Message> allMessages) {
		this.allMessages = allMessages;
	}

	public String getSentAt() {
		return sentAt;
	}

	public void setSentAt(String sentAt) {
		this.sentAt = sentAt;
	}
}
