package org.jschwarz.alfredServer.entities.devices;

import java.io.IOException;

import org.jschwarz.alfredServer.Logger;
import org.jschwarz.alfredServer.entities.Device;

public class Projector extends Device {

	public Projector(int deviceID) {
		super(deviceID);
	}

	@Override
	public boolean handleCommand(int cmd) throws IOException,
			InterruptedException {
		boolean result = false;
		if (cmd == Device.COMMAND_ON) {
			Process p = new ProcessBuilder("sudo", "irsend", "SEND_ONCE",
					"BEAMER", "KEY_POWER").start();
			result = p.waitFor() == 0 ? true : false;
			logSuccess("sudo irsend SEND_ONCE BEAMER KEY_POWER");
		} else if (cmd == Device.COMMAND_OFF) {
			Process p = new ProcessBuilder("sudo", "irsend", "SEND_ONCE",
					"BEAMER", "KEY_STOP").start();
			Thread.sleep(2500);
			p = new ProcessBuilder("sudo", "irsend", "SEND_ONCE", "BEAMER",
					"KEY_STOP").start();
			result = p.waitFor() == 0 ? true : false;
			logSuccess("sudo irsend SEND_ONCE BEAMER KEY_STOP #(two times)");
		} else {
			Logger.log("FEHLER. Unknown command " + cmd + " for DeviceID "
					+ deviceID + "(Projector).");
			result = false;
		}
		return result;
	}
}