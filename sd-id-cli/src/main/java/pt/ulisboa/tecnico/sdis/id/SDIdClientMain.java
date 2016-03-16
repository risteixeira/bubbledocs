package pt.ulisboa.tecnico.sdis.id;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.registry.JAXRException;

import pt.ulisboa.tecnico.sdis.id.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

public class SDIdClientMain {

	public static void main(String[] args) {

			try {
				if (args.length < 2) {
					System.err.println("Argument(s) missing!");
					System.err.printf("Usage: java %s uddiURL name%n", SDIdClient.class.getName());
					return;
				}

				String uddiURL = args[0];
				String name = args[1];

				System.out.printf("Contacting UDDI at %s%n", uddiURL);
				UDDINaming uddiNaming = new UDDINaming(uddiURL);

				System.out.printf("Looking for '%s'%n", name);
				String endpointAddress = uddiNaming.lookup(name);
				URL url = new URL(endpointAddress);
				
				SDIdClient client = new SDIdClient(url);

				if (endpointAddress == null) {
					System.out.println("Not found!");
					return;
				} else {
					System.out.printf("Found %s%n", endpointAddress);
				}

				System.out.println("CreateUser");
				client.createUser("pedro", "pedro@fialho");

				System.out.println("renewPassword");
				client.renewPassword("alice");

				System.out.println("removeUser");
				client.removeUser("duarte");

				System.out.println("requestAuthentication");
				byte[] authentication = client.requestAuthentication("bruno", "Bbb2".getBytes());
				String expected_auth = new String(authentication, "UTF-8");
				System.out.println(printHexBinary(authentication));
				
			} catch (SDIdClientException e) {
				e.printStackTrace();
			} catch (JAXRException e) {
				e.printStackTrace();
			} catch (EmailAlreadyExists_Exception e) {
				e.printStackTrace();
			} catch (InvalidEmail_Exception e) {
				e.printStackTrace();
			} catch (InvalidUser_Exception e) {
				e.printStackTrace();
			} catch (UserAlreadyExists_Exception e) {
				e.printStackTrace();
			} catch (UserDoesNotExist_Exception e) {
				e.printStackTrace();
			} catch (AuthReqFailed_Exception e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
