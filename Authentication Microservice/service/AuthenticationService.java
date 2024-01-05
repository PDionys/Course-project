package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;
import service.LogService;

public class AuthenticationService {
	private static final String relativePath = "Authentication Microservice/";
	private static boolean users;
	
	private static String name;
	private static String password;
	
	private final LogService log;
	
	public AuthenticationService(LogService log) throws IOException {
		this.log = log;
		File file = new File(relativePath + "Users.txt");
		if(!file.exists())
			file.createNewFile();
		file = new File(relativePath + "Passwords.txt");
		if(!file.exists())
			file.createNewFile();
	}
	
	public void handleLogin(BufferedWriter writer, BufferedReader reader) throws IOException {
		name = reader.readLine();
		password = reader.readLine();
		
		 if(CheckUsersName(name)){
           if(CheckUsersPassword(password, name)){
        	   log.RegisterTheAction(name, 1, 1);
        	   WriteServer(writer, '1');
           }else {
        	   log.RegisterTheAction(name, 1, 0);
        	   WriteServer(writer, '0');
           }
       }else {
    	   WriteServer(writer, '0');
       }
	}
	
	public void handleRegistration(BufferedWriter writer, BufferedReader reader) throws IOException {
		name = reader.readLine();
		password = reader.readLine();
		if(CheckUsersName(name)){
            WriteServer(writer, '1');
        }else {
        	RegisterNewUser(name,password);
            WriteServer(writer, '0');
        }
	}
	
	public void RegisterNewUser(String name, String password) throws IOException {

        File file1 = new File(Paths.get(relativePath+"Users.txt").toAbsolutePath().toString());
        File file2 = new File(Paths.get(relativePath+"Passwords.txt").toAbsolutePath().toString());

        FileWriter fileNameWriter = new FileWriter(file1,true);
        FileWriter filePasswordWriter = new FileWriter(file2,true);

        FileWrite(fileNameWriter, name);
        FileWrite(filePasswordWriter, password);
    }
	
	private static void FileWrite(FileWriter writer, String str) throws IOException {
		writer.write(str);
		writer.append('\n');
		writer.flush();
		writer.close();
	}
	
	private static void WriteServer(BufferedWriter writer, char op) throws IOException {
		writer.write(op);
        writer.newLine();
        writer.flush();
	}
	
	public static boolean CheckUsersName(String userName) throws FileNotFoundException {
        File file = new File(Paths.get(relativePath+"Users.txt").toAbsolutePath().toString());
        FileReader fileReader = new FileReader(file);
        Scanner scanner = new Scanner(fileReader);
        ArrayList<String> scanUsersNames = new ArrayList<>();

        users = false;

        if(scanner.hasNextLine()) {
            do {
                scanUsersNames.add(scanner.nextLine());
            } while (scanner.hasNextLine());
        }

        for(int i = 0; i < scanUsersNames.size(); i++)
            if(userName.equals(scanUsersNames.get(i))) {
                users = true;
                break;
            }

        return users;
	}
	
	private static boolean CheckUsersPassword(String userPass, String userName) throws FileNotFoundException {
		
        int iter = 0;

        File file1 = new File(Paths.get(relativePath+"Passwords.txt").toAbsolutePath().toString());
        File file2 = new File(Paths.get(relativePath+"Users.txt").toAbsolutePath().toString());
        FileReader fileReader1 = new FileReader(file1);
        FileReader fileReader2 = new FileReader(file2);
        Scanner scanner1 = new Scanner(fileReader1);
        Scanner scanner2 = new Scanner(fileReader2);
        ArrayList<String> scanUserPassword = new ArrayList<>();
        ArrayList<String> scanUserNames = new ArrayList<>();

        users = false;

        while (scanner2.hasNextLine()){
            scanUserPassword.add(scanner1.nextLine());
            scanUserNames.add(scanner2.nextLine());
        }

        for(int i = 0; i < scanUserNames.size(); i++)
            if(userName.equals(scanUserNames.get(i))) {
                iter = i;
                break;
            }

        if (scanUserPassword.get(iter).equals(userPass))
            users = true;


        return users;
    }
}
