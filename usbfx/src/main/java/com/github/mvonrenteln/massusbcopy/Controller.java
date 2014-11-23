package com.github.mvonrenteln.massusbcopy;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import org.apache.commons.io.FileUtils;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialogs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Controller {

	private static final Logger LOG = LoggerFactory.getLogger(Controller.class);

	private Model model;

	private static Controller instance;

	@FXML
	Parent root;

	@FXML
	TextField sourceText;

	@FXML
	GridPane sourcePane;

	@FXML
	GridPane targetPane;

	@FXML
	public void initialize() {
		instance = this;
		model = new Model(this);
		// TODO später über CSS setzen
		sourcePane.setPadding(new Insets(10));
		targetPane.setPadding(new Insets(10));
		updateTargets();
	}

	public static Controller getInstance() {
		return instance;
	}

	/**
	 * Wählt einen neuen Source Ordner. Wird durch die View durch Klicken auf
	 * den Browse-Button angestoßen.
	 */
	@FXML
	protected void chooseSourcePath() {
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Wähle das Quellverzeichnis");
		dirChooser.setInitialDirectory(model.getSelectedSourceDir());
		File file = dirChooser.showDialog(getStage());
		if (file != null) {
			model.setSelectedSourceDir(file);
		}
	}

	/**
	 * Updated das Source Textfeld. Wird bei Modell-Änderungen aufgerufen.
	 */
	public void updateSelectedSourceDir(File sourceDir) {
		sourceText.setText(sourceDir.getAbsolutePath());
	}

	protected void updateTargets() {
		long now = System.currentTimeMillis();
		List<RemovableDrive> removableDrives = model.getRemovableDrives();
		LOG.debug("Zeit für holen der Laufwerke: {} ms.", (System.currentTimeMillis() - now));
		boolean changed = model.isRemovableDrivesChanged();
		LOG.debug("Laufwerke haben sich {} geändert.", changed ? "" : "nicht");
		if (changed) {
			targetPane.getChildren().clear();
			for (int i = 0; i < removableDrives.size(); i++) {
				RemovableDrive drive = removableDrives.get(i);
				addTargetToTargetPane(drive, i);
			}
		}
	}

	/**
	 * Fügt das übergebene Laufwerk der Target Pane der View hinzu. Hierzu
	 * gehört die Beschreibung, die erklärenden Bilder und Buttons, jeweils
	 * alles in einer Zeile
	 * 
	 */
	private void addTargetToTargetPane(RemovableDrive drive, int pos) {
		targetPane.add(getImage(drive), 0, pos);

		Label label = new Label(drive.getName());
		GridPane.setConstraints(label, 1, pos, 3, 1);
		targetPane.getChildren().add(label);

		Button copyButton = new Button("Kopieren");
		copyButton.setId("Copy" + pos);
		copyButton.setOnAction(this::copy);
		targetPane.add(copyButton, 4, pos);
		ToggleButton syncButton = new ToggleButton("Sync");
		syncButton.setId("Sync" + pos);
		syncButton.setOnAction(this::setSync);
		targetPane.add(syncButton, 5, pos);
		targetPane.add(getImage("check-icon (Web 2 Icons).png"), 6, pos);
	}

	private void copy(ActionEvent event) {
		// TODO Achtung: getRemovableDrives holt die Liste neu, es kann sich
		// also der Index ändern!

		RemovableDrive drive = model.getRemovableDrives().get(getIndexOfButton(event));
		File path = drive.getDriveLetter();
		// TODO Dialog auf Deutsch umstellen
		Action response = Dialogs.create().title("Sollen wirklich alle Daten überschrieben werden?")
				.message("Achtung! Es werden alle Daten auf dem Laufwerk \"" + drive.getName() + "\" überschrieben!")
				.showConfirm();
		System.out.println(response);
		if (response.toString().equals("YES")) {
			// TODO Rückmeldung des Fortschritts / Fertigstellung an die GUI
			Task<Void> copyTask = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					try {
						System.out.println("copying to " + path);
						if (RemovableDrive.Type.isKnownType(drive)) {
							FileUtils.cleanDirectory(path);
						}
						FileUtils.copyDirectoryToDirectory(model.getSelectedSourceDir(), path);
					} catch (IOException e) {
						LOG.warn("Fehler beim Schreiben auf " + drive.getName(), e);
					}
					return null;
				}
			};
			new Thread(copyTask).start();
		}
	}
	
	private void setSync(ActionEvent event) {
		ToggleButton syncButton = (ToggleButton)event.getSource();
		int id = getIndexOfButton(event);
		boolean sync = syncButton.isSelected();
		//TODO im Model setzen
		LOG.info("Sync für {} wurde {}aktiviert.", id, sync ? "" : "de");
		
	}

	private int getIndexOfButton(ActionEvent event) {
		String buttonId = ((Button) event.getSource()).getId();
		int endIndex = buttonId.length() - 1;
		// wenn das vorletzte Zeichen eine Zahl ist, ist dies ein zweistelliger
		// Index
		if (Character.isDigit(buttonId.charAt(endIndex - 1))) {
			endIndex--;
		}
		return Integer.parseInt(buttonId.substring(endIndex));
	}

	private ImageView getImage(RemovableDrive drive) {
		return getImage(drive.getType().getFileName());
	}

	private ImageView getImage(String fileName) {
		URL file = getClass().getResource(fileName);
		return new ImageView(file.toString());
	}

	private Stage getStage() {
		return (Stage) sourceText.getScene().getWindow();
	}

}
