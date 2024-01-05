package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import service.AuthenticationService;

public class PermisionService {
	private final AuthenticationService authService;
	private final LogService log;
	
	private static final String relativePath = "UserActions Microservice/Files";
	private static String name;
	private static String permission;
	private static String anotherUser;
	private static String anotherFile;
	private static String adminName;
	private static boolean users;
	
	public PermisionService(AuthenticationService authService, LogService log) {
		this.authService = authService;
		this.log = log;
	}
	
	public void handlePermision(BufferedWriter writer, BufferedReader reader) throws IOException {
      name = reader.readLine();
      if(authService.CheckUsersName(name)){
          permission = reader.readLine();
          if(permission.equals("Viewer") || permission.equals("Banned") || permission.equals("Moderator"))
              GetPermission(name,permission,reader.readLine());
      }
	}
	
	public void handleOddFile(BufferedWriter writer, BufferedReader reader) throws IOException {
      anotherUser = reader.readLine();
      if(authService.CheckUsersName(anotherUser)){
          WriteServer(writer, "1");

          anotherFile = reader.readLine();
          adminName = reader.readLine();
          permission = CheckPermission(adminName,anotherUser);

          WriteServer(writer, permission);

          if(permission.equals("Owner") || permission.equals("Moderator") || permission.equals("Viewer")){
              ArrayList<String> text = CRUDfileService.LoadFile(anotherFile,anotherUser);

              WriteServer(writer, Integer.toString(text.size()));
              for (int i = 0; i < text.size(); i++){
            	  WriteServer(writer, text.get(i));
              }
          }
      }else{
    	  WriteServer(writer, "0");
      }

      String state = reader.readLine();
      if (state.equals("Good"))
          log.RegisterTheAction(adminName,2,0);
      else if(state.equals("OK"))
          log.RegisterTheAction(adminName,2,1);
      else log.RegisterTheAction(adminName,2,2);
	}
	
	public String CheckPermission(String ownerName, String userName) throws FileNotFoundException {
        File fileUserName = new File(Paths.get(relativePath+"/"+userName).toAbsolutePath().toString(), "Users.txt");
        File filePermission = new File(Paths.get(relativePath+"/"+userName).toAbsolutePath().toString(), "Permission.txt");
        users = false;

        ArrayList<String> usersNames = new ArrayList<>();
        ArrayList<String> permissionArr = new ArrayList<>();

        FileReader fileReaderUser = new FileReader(fileUserName);
        Scanner scanUser = new Scanner(fileReaderUser);

        FileReader fileReaderPermission = new FileReader(filePermission);
        Scanner scanPermission = new Scanner(fileReaderPermission);

        do {
            usersNames.add(scanUser.nextLine());
            permissionArr.add(scanPermission.nextLine());
        }while (scanUser.hasNextLine());

        for(int i = 0; i < usersNames.size(); i++){
            if(ownerName.equals(usersNames.get(i))){
                return permissionArr.get(i);
            }
        }

        try {
            FileWriter fileWriterUser = new FileWriter(fileUserName,true);
            FileWriter fileWriterPermission = new FileWriter(filePermission,true);
            FileWrite(fileWriterUser, ownerName);
            FileWrite(fileWriterPermission, ownerName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Viewer";
    }
	
	private static void WriteServer(BufferedWriter writer, String op) throws IOException {
		writer.write(op);
        writer.newLine();
        writer.flush();
	}
	
	private static void GetPermission(String userName, String permission, String adminName) throws IOException {
        users = false;

        File fileUsers = new File(Paths.get(relativePath+"/"+adminName).toAbsolutePath().toString(),"Users.txt");
        File filePermission = new File(Paths.get(relativePath+"/"+adminName).toAbsolutePath().toString(),"Permission.txt");

        FileReader fileReaderUser = new FileReader(fileUsers);
        FileReader fileReaderPermission = new FileReader(filePermission);

        Scanner scanUser = new Scanner(fileReaderUser);
        Scanner scanPermission = new Scanner(fileReaderPermission);

        ArrayList<String> usersName = new ArrayList<>();
        ArrayList<String> permissionArr = new ArrayList<>();

        do {
            usersName.add(scanUser.nextLine());
            permissionArr.add(scanPermission.nextLine());
        }while (scanUser.hasNextLine());

        for (int i = 0; i < usersName.size(); i++){
            if (usersName.get(i).equals(userName)) {
                permissionArr.set(i, permission);
                users = true;
            }
        }
        if (!users){
            usersName.add(userName);
            permissionArr.add(permission);
        }

        FileWriter fileWriterUser = new FileWriter(fileUsers,false);
        FileWriter fileWriterPermission = new FileWriter(filePermission,false);

        for(int i = 0; i < usersName.size();i++){
            fileWriterUser.write(usersName.get(i));
            fileWriterUser.append('\n');

            fileWriterPermission.write(permissionArr.get(i));
            fileWriterPermission.append('\n');
        }
        fileWriterUser.flush();
        fileWriterUser.close();
        fileWriterPermission.flush();
        fileWriterPermission.close();
    }
	
	private static void FileWrite(FileWriter writer, String str) throws IOException {
		writer.write(str);
		writer.append("\n");
		writer.flush();
		writer.close();
	}
}
