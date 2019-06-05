package websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import config.ChatEndpointConfigurator;
import config.MessageDecoder;
import config.MessageEncoder;
import dao.MessageDao;
import dao.UserDao;
import model.Message;
import model.User;

@ServerEndpoint(value = "/chatroom/{username}", decoders = MessageDecoder.class,
encoders = MessageEncoder.class, configurator = ChatEndpointConfigurator.class)
public class ChatEndpoint {
    private Session session;
    private static final Set<ChatEndpoint> chatEndpoints = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> loggedUsers = new HashMap<>();
    private UserDao userDao = new UserDao();
    private MessageDao messageDao = new MessageDao();
     
    private HttpSession httpSession;

    public void setHttpSession(HttpSession httpSession) {
        if (this.httpSession != null) {
            throw new IllegalStateException("HttpSession has already been set!");
        }
        this.httpSession = httpSession;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
        this.session = session;
        Set<User> allActiveUsers = userDao.getAllActiveUsers();
        Set<String> allActiveUsersnames = new HashSet<>();
        List<Message> allMessages = messageDao.getAllChatroomMesages();
        allActiveUsersnames  = allActiveUsers.stream().map(User::getUsername).collect(Collectors.toSet());
        chatEndpoints.add(this);
        String oldUsername = loggedUsers.get(httpSession.getId());
        Message message = new Message();
        message.setActiveUsernames(allActiveUsersnames);
        
      if(oldUsername == null || !oldUsername.equals(username)) {
    	  loggedUsers.put(httpSession.getId(), username);
    	  message.setFrom(username);
    	  message.setContent("Zalogował się!");
    	  messageDao.insertMessage(message);
    	  List<Message> allMessagesWithNew = messageDao.getAllChatroomMesages();
    	  message.setAllMessages(allMessagesWithNew);
    	  sendToChatRoom(message);
        }
      else {
    	  Message messaged = new Message();
          messaged.setAllMessages(allMessages);
          messaged.setActiveUsernames(allActiveUsersnames);
          sendToChatRoom(messaged);
      }
        
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException {
    	Set<User> allActiveUsers = userDao.getAllActiveUsers();
    	Set<String> allActiveUsernames = new HashSet<>();
    	allActiveUsernames = allActiveUsers.stream().map(User::getUsername).collect(Collectors.toSet());
    	message.setFrom(loggedUsers.get(httpSession.getId()));
       	message.setActiveUsernames(allActiveUsernames);
       	messageDao.insertMessage(message);
       	List<Message> allMessages = messageDao.getAllChatroomMesages();
       	message.setAllMessages(allMessages);
      
       	sendToChatRoom(message);
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
    	Set<User> allActiveUsers = userDao.getAllActiveUsers();
    	Set<String> allActiveUsersnames = new HashSet<>();
    	allActiveUsersnames = allActiveUsers.stream().map(User::getUsername).collect(Collectors.toSet());
    	chatEndpoints.remove(this);
        Message message = new Message();
        message.setActiveUsernames(allActiveUsersnames);
        sendToChatRoom(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
    	
    }

    private static void sendToChatRoom(Message message) throws IOException, EncodeException {
       chatEndpoints.forEach(endpoint -> {
           synchronized (endpoint) {
               try {
                   endpoint.session.getBasicRemote()
                       .sendObject(message);
               } catch (IOException | EncodeException e) {
                   e.printStackTrace();
               }
           }
       });
    }
}