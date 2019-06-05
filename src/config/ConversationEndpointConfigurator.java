package config;

import java.text.MessageFormat;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

import websocket.ConversationEndpoint;

public class ConversationEndpointConfigurator extends ServerEndpointConfig.Configurator {
	private HttpSession httpSession;

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        httpSession = (HttpSession) request.getHttpSession();
        super.modifyHandshake(sec, request, response);
    }

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        T endpoint = super.getEndpointInstance(endpointClass);

        if (endpoint instanceof ConversationEndpoint) {
            ((ConversationEndpoint) endpoint).setHttpSession(httpSession);     
        }
        else {
            throw new InstantiationException(
                    MessageFormat.format("Expected instanceof \"{0}\". Got instanceof \"{1}\".",
                    ConversationEndpoint.class, endpoint.getClass()));
        }
        return endpoint;
    }
}
