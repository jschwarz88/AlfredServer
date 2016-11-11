package org.jschwarz.alfredServer.entities;

import java.io.IOException;

import org.jschwarz.alfredServer.Logger;

public abstract class Device {

	public static final int COMMAND_OFF = 0;
	public static final int COMMAND_ON = 1;
	protected int deviceID;

	public Device(int deviceID) {
		this.deviceID = deviceID;
	}

	public abstract boolean handleCommand(int cmd) throws IOException,
			InterruptedException;

	public int getDeviceID() {
		return deviceID;
	}

	protected void logSuccess(String cmdString) {
		Logger.log("Command succesfully executed: " + cmdString);
	}

	public void setDeviceID(int deviceID) {
		this.deviceID = deviceID;
	}
}