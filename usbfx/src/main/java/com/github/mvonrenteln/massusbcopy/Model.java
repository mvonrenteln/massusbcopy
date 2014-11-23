package com.github.mvonrenteln.massusbcopy;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Model {

	private static final Logger LOG = LoggerFactory.getLogger(Model.class);

	private Controller controller;

	private File defaultSourceDir = new File(System.getProperty("user.home"));

	private File selectedSourceDir;

	List<RemovableDrive> removableDrives = Collections.emptyList();

	boolean isRemovableDrivesChanged = true;

	public Model(Controller controller) {
		this.controller = controller;
		setSelectedSourceDir(defaultSourceDir);
	}

	public File getDefaultSourceDir() {
		return defaultSourceDir;
	}

	public void setDefaultSourceDir(File defaultSourceDir) {
		this.defaultSourceDir = defaultSourceDir;
	}

	public File getSelectedSourceDir() {
		return selectedSourceDir;
	}

	public void setSelectedSourceDir(File selectedSourceDir) {
		this.selectedSourceDir = selectedSourceDir;
		controller.updateSelectedSourceDir(selectedSourceDir);
	}

	public List<RemovableDrive> getRemovableDrives() {
		return removableDrives;
	}
	
	public void setRemovableDrives(List<RemovableDrive> drives) {
		isRemovableDrivesChanged = false;

		if (!CollectionUtils.isEqualCollection(drives, removableDrives)) {
			// TODO controller aufrufen
			isRemovableDrivesChanged = true;
		}
		// TODO sync-status beibehalten
		this.removableDrives = drives;
	}



	public boolean isRemovableDrivesChanged() {
		return isRemovableDrivesChanged;
	}

	public void setRemovableDrivesChanged(boolean isRemovableDrivesChanged) {
		this.isRemovableDrivesChanged = isRemovableDrivesChanged;
	}
}
