package pt.ulisboa.tecnico.sdis.store.ws.handler;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import pt.ulisboa.tecnico.sdis.store.ws.AuthenticationKeys;
import pt.ulisboa.tecnico.sdis.store.ws.Session;


/**
 *  This is the handler client class of the Relay example.
 *
 *  #2 The client handler receives data from the client (via message context).
 *  #3 The client handler passes data to the server handler (via outbound SOAP message header).
 *
 *  *** GO TO server handler to see what happens next! ***
 *
 *  #10 The client handler receives data from the server handler (via inbound SOAP message header).
 *  #11 The client handler passes data to the client (via message context).
 *
 *  *** GO BACK TO client to see what happens next! ***
 */

public class IDClientHandler implements SOAPHandler<SOAPMessageContext> {

    public static final String REQUEST_PROPERTY = "my.request.property";
    public static final String RESPONSE_PROPERTY = "my.response.property";

    public static final String REQUEST_HEADER = "myRequestHeader";
    public static final String REQUEST_NS = "urn:example";

    public static final String RESPONSE_HEADER = "myResponseHeader";
    public static final String RESPONSE_NS = REQUEST_NS;

    public static final String CLASS_NAME = IDClientHandler.class.getSimpleName();
    public static final String TOKEN = "client-handler";
    
    private byte[] MAC; 
//    private Key sharedKey;

    public boolean handleMessage(SOAPMessageContext smc) {
        Boolean outbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outbound) {
            // outbound message

            // *** #2 ***
            // get token from request context
            String propertyValue = (String) smc.get(REQUEST_PROPERTY);
            System.out.printf("%s received '%s'%n", CLASS_NAME, propertyValue);

            // put token in request SOAP header
            try {
                // get SOAP envelope
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();
//                SOAPBody bod = se.getBody();
                
//                System.out.println("USER do SOAP");
//                String userId = msg.getSOAPBody().getLastChild().getLastChild().getLastChild().getTextContent();
                
                byte[] ticket;
				try {
					ticket = Session.getInstance().getTicket(propertyValue);
				} catch (NullPointerException e3) {
					System.err.println("User has no ticket");
					return false;
				}
//                sharedKey = Session.getInstance().getKey(propertyValue);
                byte[] splitter = new byte[] {0x0f, 0x0e, 0x0d, 0x0c, 0x0b, 0x0a, 0x09, 0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00};
                
                ByteBuffer bufferTime = ByteBuffer.allocate(Long.SIZE);
        	    bufferTime.putLong(System.currentTimeMillis());
        	    byte[] time = bufferTime.array();
                
                ByteArrayOutputStream authcStream = new ByteArrayOutputStream();
                
                try {
					authcStream.write(propertyValue.getBytes());
					authcStream.write(splitter);
					authcStream.write(time);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
                
                byte[] authc = authcStream.toByteArray();
                
//                try {
//					authc = AuthenticationKeys.encrypt(sharedKey, authcStream.toByteArray());
//				} catch (Exception e2) {
//					// TODO Auto-generated catch block
//					e2.printStackTrace();
//				}
                
                ByteArrayOutputStream streamB4MAC = new ByteArrayOutputStream();
                
                try {
					streamB4MAC.write(authc);
					streamB4MAC.write(splitter);
					streamB4MAC.write(ticket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                byte[] messageB4MAC = streamB4MAC.toByteArray();
                
                try {
					MAC = AuthenticationKeys.createMAC(messageB4MAC, AuthenticationKeys.makeKeySpec());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                
                ByteArrayOutputStream streamFinal = new ByteArrayOutputStream();
                
                try {
					streamFinal.write(messageB4MAC);
					streamFinal.write(splitter);
					streamFinal.write(MAC);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                byte[] messageFinal = streamFinal.toByteArray();
                
                String newValue = printHexBinary(messageFinal);
                
                // add header
                SOAPHeader sh = se.getHeader();
                if (sh == null)
                    sh = se.addHeader();

                // add header element (name, namespace prefix, namespace)
                Name name = se.createName(REQUEST_HEADER, "e", REQUEST_NS);
                SOAPHeaderElement element = sh.addHeaderElement(name);

                // *** #3 ***
                // add header element value
//                String newValue = propertyValue;
                element.addTextNode(newValue);

                System.out.printf("%s put token '%s' on request message header%n", CLASS_NAME, newValue);

            } catch (SOAPException e) {
                System.out.printf("Failed to add SOAP header because of %s%n", e);
            }

        } else {
            // inbound message

            // get token from response SOAP header
            try {
                // get SOAP envelope header
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();
                SOAPHeader sh = se.getHeader();

                // check header
                if (sh == null) {
                    System.out.println("Header not found.");
                    return true;
                }

                // get first header element
                Name name = se.createName(RESPONSE_HEADER, "e", RESPONSE_NS);
                Iterator it = sh.getChildElements(name);
                // check header element
                if (!it.hasNext()) {
                    System.out.printf("Header element %s not found.%n", RESPONSE_HEADER);
                    return true;
                }
                SOAPElement element = (SOAPElement) it.next();

                // *** #10 ***
                // get header element value
                String headerValue = element.getValue();
                System.out.printf("%s got '%s'%n", CLASS_NAME, headerValue);
                
                byte[] encriptedReceivedMAC = DatatypeConverter.parseHexBinary(headerValue);
//                byte[] receivedMAC = null;
//                try {
//					receivedMAC = AuthenticationKeys.decrypt(sharedKey, encriptedReceivedMAC);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
                
                if(!encriptedReceivedMAC.equals(MAC)){
                	return false;
                }

                // *** #11 ***
                // put token in response context
                String newValue = headerValue;
                System.out.printf("%s put token '%s' on response context%n", CLASS_NAME, null);
                System.out.println(msg.getSOAPBody().getAttribute("userId"));
                smc.put(RESPONSE_PROPERTY, null);
                // set property scope to application so that client class can access property
                smc.setScope(RESPONSE_PROPERTY, Scope.APPLICATION);

            } catch (SOAPException e) {
                System.out.printf("Failed to get SOAP header because of %s%n", e);
            }

        }

        return true;
    }

    public boolean handleFault(SOAPMessageContext smc) {
        return true;
    }

    public Set<QName> getHeaders() {
        return null;
    }

    public void close(MessageContext messageContext) {
    }

}
