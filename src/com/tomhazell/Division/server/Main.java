package com.tomhazell.Division.server;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Main {

	public static String IP;
	public static String AppName = "Battle Assistant For The Division";
	public static boolean Going = true;;

	public static void main(String[] args) throws Exception {

		SystemTray tray = SystemTray.getSystemTray();

		PopupMenu menu = new PopupMenu();

		// Add title item
		MenuItem NameItem = new MenuItem(AppName);
		// NameItem.setEnabled(false);
		menu.add(NameItem);
		menu.addSeparator();

		// add option that will create a popup to show IP of the user
		MenuItem IPItem = new MenuItem("Show IP");
		IPItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, IP, "Your IP is:", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		menu.add(IPItem);

		// add close action
		MenuItem closeItem = new MenuItem("Close");
		closeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Going = false;
			}
		});
		menu.add(closeItem);

		Image image = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/images/icon2.png"));
		TrayIcon icon = new TrayIcon(image, AppName, menu);
		icon.setImageAutoSize(true);

		tray.add(icon);

		GetIp ip = new GetIp();
		IP = ip.Ip;
		System.out.println("Using: " + IP);
		
		
		// initiate actual server
		DatagramSocket serverSocket = null;
		try {
			serverSocket = new DatagramSocket(54545, InetAddress.getByName(IP));
			//set timeout so that in loop below it wont hold the loop up
			serverSocket.setSoTimeout(500);
		} catch (java.net.BindException e) {
			JOptionPane.showMessageDialog(null,
					"Unable to bind socket 54545 (has The division battle assisteant been Ran twice?, check system tray)",
					"ERROR: The division battle assisteant", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		}

		
		while (Going) {
			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				serverSocket.receive(receivePacket);

				String command = new String(receivePacket.getData()).trim();
				System.out.println("FROM CLIENT:" + command);

				MakeAction action = new MakeAction();
				String responce = action.doAction(command);

				InetAddress IPAddress = receivePacket.getAddress();
				int port = receivePacket.getPort();

				sendData = responce.getBytes();

				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				serverSocket.send(sendPacket);
			} catch (SocketTimeoutException e) {
				//so that it will exit when Going = false even if not reciving data
			}

		}
		serverSocket.close();
		System.exit(0);
	}
}