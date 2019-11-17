package com.coap.server.client;

import org.eclipse.californium.core.CoapClient;

abstract class AFClientRequest {
	
	/*
	 * returns a coap client searching for all actuators
	 * */
	public static CoapClient actuatorsRequest() {
		return new CoapClient("coap://localhost/devices?type=actuator");
	}
	
	/*
	 * returns a coap client searching for all sensors
	 * */
	public static CoapClient sensorsRequest() {
		return new CoapClient("coap://localhost/devices?type=sensor");
	}
	
	/*
	 * String parameters comes right after ? in URI 
	 * 
	*/
	public static CoapClient customRequest(String parameters) {
		return new CoapClient("coap://18.229.202.214:5683/.well-known/core?"+parameters);
	}
	
	/**/
	public static CoapClient customCloudRequest(String parameters) {
		return new CoapClient("coap://18.229.202.214:5683/devices?"+parameters);
	}
	
	/**/
	public static CoapClient sensorCloudRequest() {
		return new CoapClient("coap://18.229.202.214:5683/devices?type=sensor");
	}
	
	/**/
	public static CoapClient actuatorCloudRequest() {
		return new CoapClient("coap://18.229.202.214:5683/devices?type=actuator");
	}
	

}