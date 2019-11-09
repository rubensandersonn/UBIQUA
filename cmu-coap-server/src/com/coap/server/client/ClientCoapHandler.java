package com.coap.server.client;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;

public class ClientCoapHandler implements CoapHandler {
	
	private String uid;
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public ClientCoapHandler(String uid) {
		this.uid = uid;
	}

	/**
	 * Invoked when a CoAP response or notification has arrived.
	 *
	 * @param response the response
	 */
	public void onLoad(CoapResponse response) {
		if(response != null){
        	//System.out.println("- RESPONSE: "+ new Gson().fromJson(response.getResponseText(), Collection.class) );
			System.out.println("* Response: " + response.getResponseText());
        }else{
        	System.out.println("Request failed");
        }
	}
	
	/**
	 * Invoked when a request timeouts or has been rejected by the server.
	 */
	public void onError() {
		System.out.println("!!! Error in client handler !!!");
	}

}