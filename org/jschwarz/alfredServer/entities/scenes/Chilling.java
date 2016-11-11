package org.jschwarz.alfredServer.entities.scenes;

import java.io.IOException;
import java.util.List;

import org.jschwarz.alfredServer.AlfredServer;
import org.jschwarz.alfredServer.entities.Device;
import org.jschwarz.alfredServer.entities.Scene;
import org.jschwarz.alfredServer.entities.devices.Receiver;

public class Chilling extends Scene {

	public Chilling(int id, List<Device> devices) {
		super(id, devices);
	}

	@Override
	public void enableScene() throws IOException, InterruptedException {
		int[] turnOnDevices = { 0, 1, 2, 6 };
		int[] turnOffDevices = { 3 };

		// Turn on devices
		for (int i : turnOnDevices) {
			devices.get(i).handleCommand(Device.COMMAND_ON);
		}

		// Turn off devices
		for (int i : turnOffDevices) {
			devices.get(i).handleCommand(Device.COMMAND_OFF);
		}

		// Other Actions:
		// Receiver Input: Notebook
		devices.get(AlfredServer.ID_DEVICE_RECEIVER).handleCommand(
				Receiver.COMMAND_INPUT_BT);
	}
}