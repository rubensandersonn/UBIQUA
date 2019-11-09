/**
 * 
 */
package com.coap.server.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author PedroAlmir
 *
 */
public class BroadcastTest {
	
	public final static int UDP_OUT_PORT = 4445;
	public final static int UDP_IN_PORT = 54809;
	
	public static void broadcast(String broadcastMessage, InetAddress address) throws IOException {
		DatagramSocket socket = null;
		
		socket = new DatagramSocket(UDP_IN_PORT);
		socket.setBroadcast(true);

		byte[] buffer = broadcastMessage.getBytes();

		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, UDP_OUT_PORT);
		socket.send(packet);
		socket.close();
	}
}
