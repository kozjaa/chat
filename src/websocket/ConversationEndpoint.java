package websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpSession;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import config.ConversationEndpointConfigurator;
import config.MessageDecoder;
import config.MessageEncoder;
import dao.MessageDao;
import model.Message;

@ServerEndpoint(value = "/conversation/{receiver}", decoders = MessageDecoder.class, encoders = MessageEncoder.class
				, configurator = ConversationEndpointConfigurator.class)
public class ConversationEndpoint {
	private Session session;
	private String sender;
	private String receiver;
    private static final Set<ConversationEndpoint> chatEndpoints = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> loggedUsers = new HashMap<>();
    private MessageDao messageDao = new MessageDao();
    
    private HttpSession httpSession;

    public void setHttpSession(HttpSession httpSession) {
        if (this.httpSession != null) {
            throw new IllegalStateException("HttpSession has already been set!");
        }
        this.httpSession = httpSession;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("receiver") String receiver) throws IOException, EncodeException {
    	this.sender = (String) httpSession.getAttribute("name");
    	this.receiver = receiver;
        this.session = session;
        chatEndpoints.add(this);
        loggedUsers.put(session.getId(), sender);
        Message message = new Message();
        message.setFrom(sender);
        message.setTo(receiver);
        List<Message> allConversationMessages = messageDao.getAllConverstationMessages(sender, receiver);
        message.setAllMessages(allConversationMessages);
        broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException {
        message.setFrom(loggedUsers.get(session.getId()));
        message.setTo(receiver);
        messageDao.insertMessage(message);
        List<Message> allConversationMessages = messageDao.getAllConverstationMessages(sender, receiver);
        message.setAllMessages(allConversationMessages);
        broadcast(message);
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        chatEndpoints.remove(this);        
    }

    @OnError
    public void onError(Session session, Throwable throwable) {

    }

    private void broadcast(Message message) throws IOException, EncodeException {
        chatEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                	if((endpoint.sender.equals(receiver) && endpoint.receiver.equals(sender)) || (endpoint.sender.equals(sender) && endpoint.receiver.equals(receiver))) {
                    endpoint.session.getBasicRemote()
                        .sendObject(message);
                	}
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    	
    }

}
