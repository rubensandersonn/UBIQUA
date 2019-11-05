package com.coap.server.client;

import org.eclipse.californium.core.CoapClient;

import com.coap.server.model.Client;

public class ClientGetTest {
	public static void main(String[] args) {
		Client client1 = new Client("client01", "administrator", "room1", Utils.getMyIP());
		Client client2 = new Client("client02", "researcher", "room2", Utils.getMyIP());
		Client client3 = new Client("client03", "researcher", "room2", Utils.getMyIP());
		Client client4 = new Client("client04", "professor", "room3", Utils.getMyIP());
		Client client5 = new Client("client05", "administrator", "room4", Utils.getMyIP());
		
		// testing for actuators
		CoapClient actuatorsFilterClient = AFClientRequest.actuatorsRequest();
		ClientCoapHandler clientHandler = new ClientCoapHandler(client1.getUid());
		actuatorsFilterClient.get(clientHandler);
        
        //clientHandler = new ClientCoapHandler(client2.getUid());
        //client.get(clientHandler);
        
        //clientHandler = new ClientCoapHandler(client3.getUid());
        //client.get(clientHandler);
        
        //clientHandler = new ClientCoapHandler(client4.getUid());
        //client.get(clientHandler);
        
        //clientHandler = new ClientCoapHandler(client5.getUid());
        System.out.println("Discovered: "+actuatorsFilterClient.discover().toString());
        
	}
}