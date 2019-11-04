/**
 * 
 */
package com.coap.server.client;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import com.coap.server.model.Device;
import com.google.gson.Gson;

/**
 * @author PedroAlmir
 * IP EC2: coap://52.67.118.29:5683/devices
 * Local: coap://localhost/devices
 */
public class ClientTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Device device1 = new Device("sgps01", "sensor", "location", MediaTypeRegistry.APPLICATION_JSON, getMyIP());
		Device device2 = new Device("adoor01", "actuator", "door", MediaTypeRegistry.APPLICATION_JSON, getMyIP());
		Device device3 = new Device("soprinter01", "smart object", "printer", MediaTypeRegistry.APPLICATION_JSON, getMyIP());
		Device device4 = new Device("stemp01", "sensor", "temperature", MediaTypeRegistry.APPLICATION_JSON, getMyIP());
		Device device5 = new Device("slight01", "sensor", "lightness", MediaTypeRegistry.APPLICATION_JSON, getMyIP());
		
		Gson gson = new Gson();
		
		CoapClient client = new CoapClient("coap://18.229.202.214:5683/devices");
        CoapResponse response = client.post(gson.toJson(device1), MediaTypeRegistry.APPLICATION_JSON);
        if(response != null){
        	System.out.println(response.getResponseText());
        }else{
        	System.out.println("Request failed");
        }
        
        response = client.post(gson.toJson(device2), MediaTypeRegistry.APPLICATION_JSON);
        if(response != null){
        	System.out.println(response.getResponseText());
        }else{
        	System.out.println("Request failed");
        }
        
        response = client.post(gson.toJson(device3), MediaTypeRegistry.APPLICATION_JSON);
        if(response != null){
        	System.out.println(response.getResponseText());
        }else{
        	System.out.println("Request failed");
        }
        
        response = client.post(gson.toJson(device4), MediaTypeRegistry.APPLICATION_JSON);
        if(response != null){
        	System.out.println(response.getResponseText());
        }else{
        	System.out.println("Request failed");
        }
        
        response = client.post(gson.toJson(device5), MediaTypeRegistry.APPLICATION_JSON);
        if(response != null){
        	System.out.println(response.getResponseText());
        }else{
        	System.out.println("Request failed");
        }
	}
	
	/**
	 * @return
	 */
	private static String getMyIP(){
		String ip = null;
	    try {
	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	        while (interfaces.hasMoreElements()) {
	            NetworkInterface iface = interfaces.nextElement();
	            // filters out 127.0.0.1 and inactive interfaces
	            if (iface.isLoopback() || !iface.isUp())
	                continue;

	            Enumeration<InetAddress> addresses = iface.getInetAddresses();
	            while(addresses.hasMoreElements()) {
	                InetAddress addr = addresses.nextElement();
	                ip = addr.getHostAddress();
	                System.out.println(iface.getDisplayName() + " " + ip);
	            }
	        }
	    } catch (SocketException e) {
	        throw new RuntimeException(e);
	    }
	    return ip;
	}
}
