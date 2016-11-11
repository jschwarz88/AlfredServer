package org.jschwarz.alfredServer.entities.devices;

import java.io.IOException;

import org.jschwarz.alfredServer.Logger;
import org.jschwarz.alfredServer.entities.Device;

public class TV extends Device {

	public TV(int deviceID) {
		super(deviceID);
	}

	@Override
	public boolean handleCommand(int cmd) throws IOException,
			InterruptedException {
		boolean result = false;
		if (cmd == Device.COMMAND_OFF || cmd == Device.COMMAND_ON) {
			Process p = new ProcessBuilder("sudo", "irsend", "SEND_ONCE", "TV",
					"KEY_POWER").start();
			result = p.waitFor() == 0 ? true : false;
			logSuccess("sudo irsend SEND_ONCE TV KEY_POWER");
		} else {
			Logger.log("FEHLER. Unknown command " + cmd + " for DeviceID "
					+ deviceID + "(TV).");
			result = false;
		}

		return result;
	}
}