package org.jschwarz.alfredServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.jschwarz.alfredServer.devices.Lamp;
import org.jschwarz.alfredServer.devices.Projector;
import org.jschwarz.alfredServer.devices.Receiver;
import org.jschwarz.alfredServer.devices.TV;

public class AlfredServer {

	private final static String PASSWORD = "5Jd0ksd43mp";
	private final static String ALEXAID = "alexacmd";
	private final static int SERVERPORT = 6969;

	private static int deviceID = 99;
	private static int commandID = 99;

	public static int COMMAND_OFF = 0;
	public static int COMMAND_ON = 1;
	public static int COMMAND_MUTE = 2;
	public static int COMMAND_UNMUTE = 3;
	public static int COMMAND_INPUT_BT = 4;
	public static int COMMAND_INPUT_TV = 5;
	public static int COMMAND_INPUT_NB = 6;
	public static int COMMAND_INPUT_FIRE = 7;

	private static List<Device> devices = new LinkedList<Device>();

	private static void handleConnection(Socket client) throws IOException {
		Scanner in = new Scanner(client.getInputStream());
		String receivedString = "";

		while (in.hasNext()) {
			receivedString = in.nextLine().trim();

			Logger.log("Received String: " + receivedString);
			if (receivedString.contains(ALEXAID)
					&& receivedString.contains(PASSWORD)) {
				deviceID = Integer.parseInt(receivedString.split(";")[2]);
				commandID = Integer.parseInt(receivedString.split(";")[3]);
				for (Device device : devices) {
					if (device.getDeviceID() == deviceID) {
						try {
							device.handleCommand(commandID);
						} catch (InterruptedException e) {
							Logger.log(e.getLocalizedMessage());
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		Logger.log("AlfredServer started");
		ServerSocket server = new ServerSocket(SERVERPORT);
		createDeviceMapping();

		while (true) {
			Socket client = null;

			try {
				Logger.log("Waiting for new connections");
				client = server.accept();
				Logger.log("Incomming connection");
				handleConnection(client);
			} catch (IOException e) {
				Logger.log(e.getLocalizedMessage());
			} finally {
				if (client != null)
					try {
						client.close();
					} catch (IOException e) {
						Logger.log(e.getLocalizedMessage());
					}
			}
		}
	}

	private static void createDeviceMapping() {
		Device lampCouch = new Lamp(1);
		Device lampDimmer = new Lamp(2);
		Device lampDoor = new Lamp(3);
		Device tv = new TV(4);
		Device projector = new Projector(5);
		Device receiver = new Receiver(6);

		devices.add(lampCouch);
		devices.add(lampDimmer);
		devices.add(lampDoor);
		devices.add(tv);
		devices.add(projector);
		devices.add(receiver);
	}
}