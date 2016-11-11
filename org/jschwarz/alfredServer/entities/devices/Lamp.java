package org.jschwarz.alfredServer.entities.devices;

import java.io.IOException;

import org.jschwarz.alfredServer.Logger;
import org.jschwarz.alfredServer.entities.Device;

public class Lamp extends Device {

	public Lamp(int deviceID) {
		super(deviceID);
	}

	@Override
	public boolean handleCommand(int cmd) throws IOException,
			InterruptedException {
		boolean result = false;
		if (cmd == Device.COMMAND_ON || cmd == Device.COMMAND_OFF) {
			Process p = new ProcessBuilder("sudo",
					"/home/pi/wiringPi/raspberry-remote/send", "01111",
					deviceID + "", cmd + "").start();
			result = p.waitFor() == 0 ? true : false;
			logSuccess("sudo /home/pi/wiringPi/raspberry-remote/send 01111 "
					+ deviceID + " " + cmd);
		} else {
			Logger.log("FEHLER. Unknown command " + cmd + " for DeviceID "
					+ deviceID + "(Lamp).");
			result = false;
		}
		return result;
	}
}