package usbfx;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsbFxModel {

	private static final String REMOVABLE_DISK = "Wechseldatenträger";

	private static final Logger LOG = LoggerFactory.getLogger(UsbFxModel.class);

	private UsbFxController controller;

	FileSystemView fsv = FileSystemView.getFileSystemView();

	private File defaultSourceDir = new File(System.getProperty("user.home"));

	private File selectedSourceDir;

	List<RemovableDrive> removableDrives = Collections.emptyList();

	boolean isRemovableDrivesChanged = true;

	public UsbFxModel(UsbFxController controller) {
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
		isRemovableDrivesChanged = false;
		List<RemovableDrive> drives = new ArrayList<RemovableDrive>();
		File[] f = File.listRoots();
		for (int i = 0; i < f.length; i++) {
			if (isRemovableDisk(f[i])) {
				String displayName = fsv.getSystemDisplayName(f[i]);
				RemovableDrive drive = new RemovableDrive(RemovableDrive.Type.getType(displayName), f[i], displayName);
				drives.add(drive);
			}
		}
		if (!CollectionUtils.isEqualCollection(drives, removableDrives)) {
			isRemovableDrivesChanged = true;
		}
		// TODO sync-status beibehalten
		this.removableDrives = drives;
		LOG.debug("Gefundene Wechsellaufwerke: {}", drives);
		return drives;
	}

	private boolean isRemovableDisk(File f) {
		String description = fsv.getSystemTypeDescription(f);
		LOG.debug(f + " = " + description);
		return description.equals(REMOVABLE_DISK);
	}

	public boolean isRemovableDrivesChanged() {
		return isRemovableDrivesChanged;
	}

	public void setRemovableDrivesChanged(boolean isRemovableDrivesChanged) {
		this.isRemovableDrivesChanged = isRemovableDrivesChanged;
	}
}
