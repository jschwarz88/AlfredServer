package org.jschwarz.alfredServer.entities;

import java.io.IOException;
import java.util.List;

public abstract class Scene {

	protected int id;
	protected List<Device> devices;

	public Scene(int id, List<Device> devices) {
		this.id = id;
		this.devices = devices;
	}

	public abstract void enableScene() throws IOException, InterruptedException;
}