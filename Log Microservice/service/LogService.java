package service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogService {
	private static final String relativePath = "Log Microservice/Log";
	
	public LogService() {
		File dir = new File(relativePath);
		if(!dir.exists()) {
			dir.mkdirs();
		}
	}
	
	public void RegisterTheAction(String userName, int actionCod, int logCod) throws IOException {
        String fileName = userName + ".txt";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        File file = new File(relativePath,fileName);
        FileWriter fileWriter = new FileWriter(file,true);

        switch (actionCod){
            case 1:
                if(logCod == 1) {
                    String fullAction = dtf.format(now) + " " + userName + " successfully login";
                    FileWrite(fileWriter, fullAction);
                }else if(logCod == 0){
                    String fullAction = dtf.format(now) + " " + userName + " try to login";
                    FileWrite(fileWriter, fullAction);
                }
                break;
            case 2:
                if (logCod == 0){
                    String fullAction = dtf.format(now) + " " + userName + " successfully get access to file as Moderator";
                    FileWrite(fileWriter, fullAction);
                }else if(logCod == 1){
                    String fullAction = dtf.format(now) + " " + userName + " successfully get access to file as Viewer";
                    FileWrite(fileWriter, fullAction);
                }else {
                    String fullAction = dtf.format(now) + " " + userName + " try to get access to file as Banned";
                    FileWrite(fileWriter, fullAction);
                }
        }
    }
	
	private static void FileWrite(FileWriter writer, String str) throws IOException {
		writer.write(str);
		writer.append("\n");
		writer.flush();
		writer.close();
	}
}
