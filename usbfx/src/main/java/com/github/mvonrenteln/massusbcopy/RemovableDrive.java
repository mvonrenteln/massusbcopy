package com.github.mvonrenteln.massusbcopy;

import java.io.File;

public class RemovableDrive {

	public enum Type {

		STICK("PenDrive-USB-3.0-Kingston-DT100-G3-16GB-1-icon.png"), PLAYER(
				"iPod-Shuffle-icon (3D Cartoon Vol. 2 Icons, cc-nc-nd).png"), UNDEFINED("");

		private String fileName;

		Type(String fileName) {
			this.fileName = fileName;
		}

		public String getFileName() {
			return fileName;
		}
		
		public static boolean isKnownType(RemovableDrive drive) {
			return getType(drive.getName()) != UNDEFINED;
		}
		
		public static RemovableDrive.Type getType(RemovableDrive drive) {
			return getType(drive.getName());
		}
		
		public static RemovableDrive.Type getType(String displayName) {
			 for (RemovableDrive.Type type : Type.values()) {
				 if (displayName.toUpperCase().contains(type.toString())) {
					 return type;
				 }
			 }
			 return UNDEFINED;
		}
	}

	private RemovableDrive.Type type;

	private File drivePath;

	private String name;
	
	private boolean inSync;

	public RemovableDrive(RemovableDrive.Type type, File drivePath, String name) {
		this.type = type;
		this.drivePath = drivePath;
		this.name = name;
	}

	public RemovableDrive.Type getType() {
		return type;
	}

	public File getDriveLetter() {
		return drivePath;
	}

	public String getName() {
		return name;
	}

	public boolean isInSync() {
		return inSync;
	}

	public void setInSync(boolean inSync) {
		this.inSync = inSync;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((drivePath == null) ? 0 : drivePath.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RemovableDrive other = (RemovableDrive) obj;
		if (drivePath == null) {
			if (other.drivePath != null)
				return false;
		} else if (!drivePath.equals(other.drivePath))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "RemovableDrive [name=" + name + "]";
	}

}