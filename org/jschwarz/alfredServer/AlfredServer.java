package org.jschwarz.alfredServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.jschwarz.alfredServer.entities.Device;
import org.jschwarz.alfredServer.entities.Scene;
import org.jschwarz.alfredServer.entities.devices.Lamp;
import org.jschwarz.alfredServer.entities.devices.Projector;
import org.jschwarz.alfredServer.entities.devices.Receiver;
import org.jschwarz.alfredServer.entities.devices.TV;
import org.jschwarz.alfredServer.entities.scenes.Chilling;
import org.jschwarz.alfredServer.entities.scenes.WatchMovie;

public class AlfredServer {

	private final static String PASSWORD = "5Jd0ksd43mp";
	private final static String ALEXAID = "alexacmd";
	private final static int SERVERPORT = 6969;

	private static int deviceID = 99;
	private static int commandID = 99;

	public static final int ID_DEVICE_MOOD = 0;
	public static final int ID_DEVICE_COUCH = 1;
	public static final int ID_DEVICE_DIMMER = 2;
	public static final int ID_DEVICE_DOOR = 3;
	public static final int ID_DEVICE_TV = 4;
	public static final int ID_DEVICE_PROJECTOR = 5;
	public static final int ID_DEVICE_RECEIVER = 6;
	public static final int ID_DEVICE_SCENE = 11;

	private static List<Device> devices = new LinkedList<Device>();
	private static List<Scene> scenes = new LinkedList<Scene>();

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

				if (deviceID == AlfredServer.ID_DEVICE_SCENE) {
					handleSceneRequest(commandID);
					return;
				}

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

	private static void handleSceneRequest(int sceneID) {
		try {
			scenes.get(sceneID).enableScene();
		} catch (Exception e) {
			Logger.log(e.getMessage());
		}
	}

	public static void main(String[] args) throws IOException {
		Logger.log("AlfredServer started");
		ServerSocket server = new ServerSocket(SERVERPORT);
		createDeviceMapping();
		createSceneMapping();

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
		Device lampMood = new Lamp(ID_DEVICE_MOOD);
		Device lampCouch = new Lamp(ID_DEVICE_COUCH);
		Device lampDimmer = new Lamp(ID_DEVICE_DIMMER);
		Device lampDoor = new Lamp(ID_DEVICE_DOOR);
		Device tv = new TV(ID_DEVICE_TV);
		Device projector = new Projector(ID_DEVICE_PROJECTOR);
		Device receiver = new Receiver(ID_DEVICE_RECEIVER);

		devices.add(lampCouch);
		devices.add(lampDimmer);
		devices.add(lampDoor);
		devices.add(tv);
		devices.add(projector);
		devices.add(receiver);
		devices.add(lampMood);
	}

	private static void createSceneMapping() {
		Scene sceneChilling = new Chilling(1, devices);
		Scene sceneWatchMovie = new WatchMovie(2, devices);

		scenes.add(sceneChilling);
		scenes.add(sceneWatchMovie);
	}
}