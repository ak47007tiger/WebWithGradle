package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

/**
 * Servlet implementation class EchoServlet
 */
public class EchoServlet extends WebSocketServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see WebSocketServlet#WebSocketServlet()
     */
    public EchoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    private final AtomicInteger connectionIds = new AtomicInteger(0);  
    @Override  
    protected StreamInbound createWebSocketInbound(String arg0,  
            HttpServletRequest request) {  
    	System.out.println("some connect...");
        return new HelloMessageInbound(connectionIds.getAndIncrement(), request  
                .getSession().getId());  
    }
    
    class HelloMessageInbound extends StreamInbound{
    	private String WS_NAME;  
        private final String FORMAT = "%s : %s";  
        private final String PREFIX = "ws_";  
        private String sessionId = "";  
      
        public HelloMessageInbound(int id, String _sessionId) {  
            this.WS_NAME = PREFIX + id;  
            this.sessionId = _sessionId;  
        }  
      
        @Override  
        protected void onTextData(Reader reader) throws IOException {  
            char[] chArr = new char[1024];  
            int len = reader.read(chArr);  
            send(String.copyValueOf(chArr, 0, len));  
        }  
      
        @Override  
        protected void onClose(int status) {  
            System.out.println(String.format(FORMAT, WS_NAME, "closing ......"));  
            super.onClose(status);  
        }  
      
        @Override  
        protected void onOpen(WsOutbound outbound) {  
            super.onOpen(outbound);  
            try {
				getWsOutbound().writeTextMessage(CharBuffer.wrap("you connect server"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }  
      
        private void send(String message) throws IOException {  
            message = String.format(FORMAT, "echo", message);  
            System.out.println(message);  
            getWsOutbound().writeTextMessage(CharBuffer.wrap(message));  
        }  
      
        @Override  
        protected void onBinaryData(InputStream arg0) throws IOException {  
        	System.out.println("onBinaryData");
        }  
    }

}
