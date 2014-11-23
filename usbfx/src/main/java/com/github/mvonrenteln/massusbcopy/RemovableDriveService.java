package com.github.mvonrenteln.massusbcopy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

import javax.swing.filechooser.FileSystemView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemovableDriveService extends ScheduledService<List<RemovableDrive>> {

	private static final Logger LOG = LoggerFactory.getLogger(RemovableDriveService.class);

	private static final String REMOVABLE_DISK = "Wechseldatenträger";

	private FileSystemView fsv = FileSystemView.getFileSystemView();
	

	@Override
	protected Task<List<RemovableDrive>> createTask() {
		return new Task<List<RemovableDrive>>() {
			@Override
			protected List<RemovableDrive> call() {
				List<RemovableDrive> drives = new ArrayList<RemovableDrive>();
				File[] f = File.listRoots();
				for (int i = 0; i < f.length; i++) {
					if (isRemovableDisk(f[i])) {
						String displayName = fsv.getSystemDisplayName(f[i]);
						RemovableDrive drive = new RemovableDrive(RemovableDrive.Type.getType(displayName), f[i],
								displayName);
						drives.add(drive);
					}
				}
				LOG.debug("Gefundene Wechsellaufwerke: {}", drives);
				return drives;
			}
		};
	}

	private boolean isRemovableDisk(File f) {
		String description = fsv.getSystemTypeDescription(f);
		LOG.debug(f + " = " + description);
		return description.equals(REMOVABLE_DISK);
	}

}
