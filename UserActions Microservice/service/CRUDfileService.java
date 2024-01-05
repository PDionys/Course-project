package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class CRUDfileService {
	private static final String relativePath = "UserActions Microservice/Files";
	
	private static String name;
	private static String filename;
	
	public CRUDfileService() {
		File dir = new File(relativePath);
		if(!dir.exists()) {
			dir.mkdirs();
		}
	}
	
	public void handleCreate(BufferedWriter writer, BufferedReader reader) throws IOException {
		name = reader.readLine();
		filename = reader.readLine();
		CreateFile(name,filename);
	}
	
	public void handleRead(BufferedWriter writer, BufferedReader reader) throws IOException {
		filename = reader.readLine();
		name = reader.readLine();
		ArrayList<String> FullText = LoadFile(filename,name);
		
		WriteServer(writer, Integer.toString(FullText.size()));
		for (int i = 0; i < FullText.size(); i++){
			WriteServer(writer, FullText.get(i));
		}
	}
	
	public void handleUpdate(BufferedWriter writer, BufferedReader reader) throws IOException {
		name = reader.readLine();
	    filename = reader.readLine();
	    String fullText = "";
	    Integer lenght = Integer.valueOf(reader.readLine());
	    ArrayList<String> arrayList = new ArrayList<>();
	    for (int i = 0; i < lenght; i++)
	        arrayList.add(reader.readLine());
	    for (int i = 0; i < arrayList.size();i++)
	        fullText = fullText + arrayList.get(i) + "\n";
	
	    SaveFile(name,filename,fullText);
	}
	
	private static void SaveFile(String username, String filename, String fullText) throws IOException {
        FileWriter fileWriter = new FileWriter(Paths.get(relativePath+"/"+username+"/"+filename).toAbsolutePath().toString(),false);
        FileWrite(fileWriter, fullText);
    }
	
	private static void WriteServer(BufferedWriter writer, String op) throws IOException {
		writer.write(op);
        writer.newLine();
        writer.flush();
	}
	
	public static ArrayList<String> LoadFile(String filename, String username) throws FileNotFoundException {

        FileReader fileReader = new FileReader(Paths.get(relativePath+"/"+username+"/"+filename).toAbsolutePath().toString());
        Scanner scanner = new Scanner(fileReader);
        ArrayList<String> text = new ArrayList<>();

        if(scanner.hasNextLine()) {
            do {
                text.add(scanner.nextLine());
            } while (scanner.hasNextLine());
        }

        return text;
    }
	
	private static void CreateFile(String username, String filename) throws IOException {
        new File(Paths.get(relativePath+"/"+username).toAbsolutePath().toString()).mkdir();

        File fileText = new File(Paths.get(relativePath+"/"+username).toAbsolutePath().toString(),filename);
        File fileUsers = new File(Paths.get(relativePath+"/"+username).toAbsolutePath().toString(),"Users.txt");
        File filePermission = new File(Paths.get(relativePath+"/"+username).toAbsolutePath().toString(),"Permission.txt");

        fileText.createNewFile();
        if(fileUsers.createNewFile() && filePermission.createNewFile()){
            FileWriter fileWriter = new FileWriter(fileUsers,true);
            FileWrite(fileWriter, username);

            fileWriter = new FileWriter(filePermission,true);
            FileWrite(fileWriter, "Owner");
        }
    }
	
	private static void FileWrite(FileWriter writer, String str) throws IOException {
		writer.write(str);
		writer.append("\n");
		writer.flush();
		writer.close();
	}
}
