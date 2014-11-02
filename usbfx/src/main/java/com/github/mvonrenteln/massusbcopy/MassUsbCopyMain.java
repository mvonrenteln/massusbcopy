package com.github.mvonrenteln.massusbcopy;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;


public class MassUsbCopyMain extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("usbdesign.fxml"));

		Scene scene = new Scene(root, 1024, 600);

		stage.setTitle("FXML Welcome");
		stage.setScene(scene);
		stage.show();
		
		heartbeatSticks();
//		heartbeatFilesystem();
	}

	private void heartbeatSticks() {
		Timeline targetUpdater = new Timeline(new KeyFrame(Duration.seconds(1),	new EventHandler<ActionEvent>() {

		    @Override
		    public void handle(ActionEvent event) {
		    	Controller.getInstance().updateTargets();
		    }
		}));
		targetUpdater.setCycleCount(Timeline.INDEFINITE);
		targetUpdater.play();
	}
	
//	private void heartbeatFilesystem() {
//		FileAlterationObserver observer = new FileAlterationObserver(new File("C:\\Users\\VONRENTELN\\Desktop\\Quelle"));
//		observer.addListener(new FileAlterationListener() {
//			
//			@Override
//			public void onStop(FileAlterationObserver arg0) {
//				// TODO Auto-generated method stub
//			}
//			
//			@Override
//			public void onStart(FileAlterationObserver arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onFileDelete(File arg0) {
//				// TODO Auto-generated method stub
//				System.out.println("delete  "+arg0);
//				
//			}
//			
//			@Override
//			public void onFileCreate(File arg0) {
//				System.out.println("create "+arg0);
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onFileChange(File arg0) {
//				// TODO Auto-generated method stub
//				System.out.println("change "+arg0);
//				// IOutils.copy(stream, stream)
//				
//			}
//			
//			@Override
//			public void onDirectoryDelete(File arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onDirectoryCreate(File arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onDirectoryChange(File arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//		scheduler.scheduleAtFixedRate(() -> observer.checkAndNotify(), 5, 1, TimeUnit.SECONDS);
//		
//	}
}
