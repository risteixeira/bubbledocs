package pt.ulisboa.tecnico.sdis.store.ws.handler;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
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

/**
 * This is the handler server class of the Relay example.
 *
 * #4 The server handler receives data from the client handler (via inbound SOAP
 * message header). #5 The server handler passes data to the server (via message
 * context).
 *
 * *** GO TO server class to see what happens next! ***
 *
 * #8 The server class receives data from the server (via message context). #9
 * The server handler passes data to the client handler (via outbound SOAP
 * message header).
 *
 * *** GO BACK TO client handler to see what happens next! ***
 */

public class IDServerHandler implements SOAPHandler<SOAPMessageContext> {

	public static final String REQUEST_PROPERTY = "my.request.property";
	public static final String RESPONSE_PROPERTY = "my.response.property";

	public static final String REQUEST_HEADER = "myRequestHeader";
	public static final String REQUEST_NS = "urn:example";

	public static final String RESPONSE_HEADER = "myResponseHeader";
	public static final String RESPONSE_NS = REQUEST_NS;

	public static final String CLASS_NAME = IDServerHandler.class
			.getSimpleName();
	public static final String TOKEN = "server-handler";

	private byte[] MAC;
//	private Key sharedKey = null;

	public boolean handleMessage(SOAPMessageContext smc) {
		Boolean outbound = (Boolean) smc
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outbound) {
			// outbound message

			// *** #8 ***
			// get token from response context
			String propertyValue = (String) smc.get(RESPONSE_PROPERTY);
			System.out.printf("%s received '%s'%n", CLASS_NAME, propertyValue);

			// put token in response SOAP header
			try {
				// get SOAP envelope
				SOAPMessage msg = smc.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();

				// add header
				SOAPHeader sh = se.getHeader();
				if (sh == null)
					sh = se.addHeader();

				// add header element (name, namespace prefix, namespace)
				Name name = se.createName(RESPONSE_HEADER, "e", RESPONSE_NS);
				SOAPHeaderElement element = sh.addHeaderElement(name);

				// *** #9 ***
				// add header element value
//				byte[] encriptedMAC = null;
//
//				try {
//					encriptedMAC = AuthenticationKeys.encrypt(sharedKey, MAC);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

				String newValue = printHexBinary(MAC);
				element.addTextNode(newValue);

				System.out.printf(
						"%s put token '%s' on response message header%n",
						CLASS_NAME, newValue);

			} catch (SOAPException e) {
				System.out.printf("Failed to add SOAP header because of %s%n",
						e);
			}

		} else {
			// inbound message

			// get token from request SOAP header
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
				Name name = se.createName(REQUEST_HEADER, "e", REQUEST_NS);
				Iterator it = sh.getChildElements(name);
				// check header element
				if (!it.hasNext()) {
					System.out.printf("Header element %s not found.%n",
							REQUEST_HEADER);
					return true;
				}
				SOAPElement element = (SOAPElement) it.next();

				// *** #4 ***
				// get header element value
				String headerValue = element.getValue();
				System.out.printf("%s got '%s'%n", CLASS_NAME, headerValue);

				byte[] splitter = new byte[] { 0x0f, 0x0e, 0x0d, 0x0c, 0x0b,
						0x0a, 0x09, 0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02,
						0x01, 0x00 };
				String[] messageParts = headerValue
						.split(printHexBinary(splitter));

				
				byte[] authcUser = DatatypeConverter
						.parseHexBinary(messageParts[0]);

				byte[] authcTime = DatatypeConverter
						.parseHexBinary(messageParts[1]);

				
				byte[] ticket = DatatypeConverter
						.parseHexBinary(messageParts[2]);

				byte[] receivedMAC = DatatypeConverter
						.parseHexBinary(messageParts[3]);

				
				ByteArrayOutputStream streamReceivedMessage = new ByteArrayOutputStream();

				try {
					streamReceivedMessage.write(authcUser);
					streamReceivedMessage.write(splitter);
					streamReceivedMessage.write(authcTime);
					streamReceivedMessage.write(splitter);
					streamReceivedMessage.write(ticket);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				boolean macVerification = false;

				try {
					macVerification = AuthenticationKeys.verifyMAC(receivedMAC,
							streamReceivedMessage.toByteArray(),
							AuthenticationKeys.makeKeySpec());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				
				if (!macVerification) {
					// TODO excepçao??
					return false;
				}

				MAC = receivedMAC;
				
				byte[] decriptedTicket = null;

				try {
					System.out.println(printHexBinary(AuthenticationKeys
							.makeKeySpec().getEncoded()));
					Key key = AuthenticationKeys.makeKeySpec();
					decriptedTicket = AuthenticationKeys.decrypt(key, ticket);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String[] ticketParts = printHexBinary(decriptedTicket).split(
						printHexBinary(splitter));
				
				ticketParts[4] = ticketParts[4].substring(0, 32);

//				sharedKey = new SecretKeySpec(
//						DatatypeConverter.parseHexBinary(ticketParts[4]), "AES");

				
				// byte[] decriptedAuthc = null;
				//
				// try {
				// decriptedAuthc = AuthenticationKeys.decrypt(sharedKey,
				// authc);
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				//
//				String[] authcParts = printHexBinary(authc).split(
//						printHexBinary(splitter));

//				long timeStart = Long.parseLong(ticketParts[2], 16);
//				long timeExpire = Long.parseLong(ticketParts[3], 16);
//				long timeAuth = Long.parseLong(printHexBinary(authcTime), 16);
//
//				if (timeAuth < timeStart || timeAuth > timeExpire) {
//					// TODO: Lançar um excepção?????
//					return false;
//				}
				

				String serverString = new String(
						DatatypeConverter.parseHexBinary(ticketParts[1]));

				if (!serverString.equals("SD-STORE")) {
					return false;
				}

				// *** #5 ***
				// put token in request context
				System.out.printf("%s put token '%s' on request context%n",
						CLASS_NAME, null);
				smc.put(REQUEST_PROPERTY, null);
				// set property scope to application so that server class can
				// access property
				smc.setScope(REQUEST_PROPERTY, Scope.APPLICATION);

			} catch (SOAPException e) {
				System.out.printf("Failed to get SOAP header because of %s%n",
						e);
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
