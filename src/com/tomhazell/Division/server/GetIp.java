package com.tomhazell.Division.server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;

public class GetIp {
	
	String Ip = null;
	
	public GetIp(){
		try {
			Load();
		} catch (IOException e) {
		}
		if(Ip.equals("")){
			GetBestIp();
			save();
			
		}
		
	}
	
	private void GetBestIp(){
		// iterate over the network interfaces known to java
		Enumeration<NetworkInterface> interfaces = null;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e3) {
		}
		OUTER : for (NetworkInterface interface_ : Collections.list(interfaces)) {
		  // we shouldn't care about loopback addresses
		try {
			if (interface_.isLoopback())
			    continue;
		} catch (SocketException e2) {
			continue;
		}

		  // if you don't expect the interface to be up you can skip this
		  // though it would question the usability of the rest of the code
		  try {
			if (!interface_.isUp())
			    continue;
		} catch (SocketException e1) {
			continue;
		}

		  // iterate over the addresses associated with the interface
		  Enumeration<InetAddress> addresses = interface_.getInetAddresses();
		  for (InetAddress address : Collections.list(addresses)) {
		    // look only for ipv4 addresses
		    if (address instanceof Inet6Address)
		      continue;
		    
		    // java 7's try-with-resources statement, so that
		    // we close the socket immediately after use
		    try (SocketChannel socket = SocketChannel.open()) {
		      // again, use a big enough timeout
		      socket.socket().setSoTimeout(3000);

		      // bind the socket to your local interface
		      socket.bind(new InetSocketAddress(address, 8080));

		      // try to connect to *somewhere*
		      socket.connect(new InetSocketAddress("google.com", 80));
		    } catch (IOException ex) {
		      ex.printStackTrace();
		      continue;
		    }
		    Ip = address.getHostAddress();

		    // stops at the first *working* solution
		    break OUTER;
		  }
		}
		
	}
	
	private void Load() throws IOException{
		Properties properties = new Properties();
		try {
		  properties.load(new FileInputStream("BattleAssistant.prop"));
		} catch (IOException e) {
		  //
		}
		Ip = properties.getProperty("IP", "");
	}
	
	public void save(){
		Properties properties = new Properties();
		properties.setProperty("IP", Ip);
		try {
			properties.store(new FileOutputStream("BattleAssistant.prop"), "");
		} catch (IOException e) {
			//bad things
		}
	}
}
