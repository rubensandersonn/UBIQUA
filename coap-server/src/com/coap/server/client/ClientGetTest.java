package com.coap.server.client;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.server.resources.Resource;

import com.coap.server.main.ServerResponse;
import com.coap.server.model.Client;
import com.coap.server.model.Device;
import com.google.gson.Gson;

public class ClientGetTest {
	public static void main(String[] args) {
		
		// testing for actuators
		CoapClient actuatorsFilterClient = AFClientRequest.actuatorsRequest();
		
		
		System.out.println(actuatorsFilterClient.get().getResponseText());
        
        
        //System.out.println("Discovered: "+actuatorsFilterClient.discover().toString());
        
	}
}