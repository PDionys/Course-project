import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class MainScreen implements ActionListener{
    JFrame MainFrame;
    JButton CreateFile,LoadFile,SaveFile,GivePermission,OpenAnother;
    JTextArea LoadedFileTextArea;
    JTextField FileNameTextField,OwnerTextField,PermissionTextField;
    String adminName;
    boolean EditorFile;

    public void CreateMainFrame(String userName){
        MainFrame = new JFrame();
        MainFrame.setSize(720,720);
        MainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        MainFrame.setResizable(false);
        MainFrame.setLayout(null);
        adminName = userName;

        JPanel panel1 = new JPanel();
        panel1.setBackground(Color.GRAY);
        panel1.setBounds(0,0,210,720);
        panel1.setLayout(null);
        //----------------------------------------------------------
        CreateFile = new JButton();
        CreateFile.setText("Create File");
        CreateFile.setFocusable(false);
        CreateFile.setFont(new Font(null,Font.PLAIN,24));
        CreateFile.setBounds(5,5,200,50);
        CreateFile.addActionListener(this);
        //----------------------------------------------------------
        LoadFile = new JButton();
        LoadFile.setText("Load File");
        LoadFile.setFocusable(false);
        LoadFile.setFont(new Font(null,Font.PLAIN,24));
        LoadFile.setBounds(5,60,200,50);
        LoadFile.addActionListener(this);
        //---------------------------------------------------------
        SaveFile = new JButton();
        SaveFile.setText("Save File");
        SaveFile.setFocusable(false);
        SaveFile.setFont(new Font(null,Font.PLAIN,24));
        SaveFile.setBounds(5,115,200,50);
        SaveFile.addActionListener(this);
        //---------------------------------------------------------
        GivePermission = new JButton();
        GivePermission.setText("Give Permission");
        GivePermission.setFocusable(false);
        GivePermission.setFont(new Font(null,Font.PLAIN,20));
        GivePermission.setBounds(5,170,200,50);
        GivePermission.addActionListener(this);
        //---------------------------------------------------------
        OpenAnother = new JButton();
        OpenAnother.setText("Open Another's File");
        OpenAnother.setFocusable(false);
        OpenAnother.setFont(new Font(null,Font.PLAIN,18));
        OpenAnother.setBounds(5,225,200,50);
        OpenAnother.addActionListener(this);
        //---------------------------------------------------------
        panel1.add(CreateFile);
        panel1.add(LoadFile);
        panel1.add(SaveFile);
        panel1.add(GivePermission);
        panel1.add(OpenAnother);

        //---------------------------------------------------------
        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.GRAY);
        panel2.setBounds(210,0,510,100);
        panel2.setLayout(null);
        //---------------------------------------------------------
        JLabel LabelFileName = new JLabel();
        LabelFileName.setText("File Name: ");
        LabelFileName.setFont(new Font(null,Font.PLAIN,18));
        LabelFileName.setBounds(5,5,200,20);
        //---------------------------------------------------------
        JLabel LabelOwnerName = new JLabel();
        LabelOwnerName.setText("Owner: ");
        LabelOwnerName.setFont(new Font(null,Font.PLAIN,18));
        LabelOwnerName.setBounds(5,40,100,20);
        //---------------------------------------------------------
        JLabel LabelPermission = new JLabel();
        LabelPermission.setText("Permission: ");
        LabelPermission.setFont(new Font(null,Font.PLAIN,18));
        LabelPermission.setBounds(5,75,100,20);
        //---------------------------------------------------------
        FileNameTextField = new JTextField();
        FileNameTextField.setEditable(false);
        FileNameTextField.setBounds(100,5,200,20);
        FileNameTextField.setFont(new Font(null,Font.PLAIN,18));
        //---------------------------------------------------------
        OwnerTextField = new JTextField();
        OwnerTextField.setEditable(false);
        OwnerTextField.setBounds(70,40,200,20);
        OwnerTextField.setFont(new Font(null,Font.PLAIN,18));
        //---------------------------------------------------------
        PermissionTextField = new JTextField();
        PermissionTextField.setEditable(false);
        PermissionTextField.setFont(new Font(null,Font.PLAIN,18));
        PermissionTextField.setBounds(100,75,100,20);
        //---------------------------------------------------------
        panel2.add(LabelFileName);
        panel2.add(FileNameTextField);
        panel2.add(LabelOwnerName);
        panel2.add(OwnerTextField);
        panel2.add(LabelPermission);
        panel2.add(PermissionTextField);

        //---------------------------------------------------------
        JPanel panel3 = new JPanel();
        panel3.setBackground(Color.GRAY);
        panel3.setBounds(210,100,510,620);
        panel3.setLayout(null);
        //---------------------------------------------------------
        LoadedFileTextArea = new JTextArea();
        LoadedFileTextArea.setBounds(5,5,485,570);
        LoadedFileTextArea.setFont(new Font(null,Font.PLAIN,24));
        //---------------------------------------------------------
        panel3.add(LoadedFileTextArea);

        MainFrame.add(panel1);
        MainFrame.add(panel2);
        MainFrame.add(panel3);
        MainFrame.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == CreateFile){
            String fileName = JOptionPane.showInputDialog("File Name: ");

            try {
                Socket socket = new Socket("127.0.0.1",8000);
                BufferedWriter writer =
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        socket.getOutputStream()));
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()));

                writer.write("3");
                writer.newLine();
                writer.flush();

                writer.write(adminName);
                writer.newLine();
                writer.flush();

                writer.write(fileName);
                writer.newLine();
                writer.flush();
            }catch (IOException ioException){
                ioException.printStackTrace();
            }
        }else if(e.getSource() == LoadFile){
            EditorFile = true;
            String fileName = JOptionPane.showInputDialog("File Name: ");

            try {
                Socket socket = new Socket("127.0.0.1",8000);
                BufferedWriter writer =
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        socket.getOutputStream()));
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()));

                writer.write("4");
                writer.newLine();
                writer.flush();

                writer.write(fileName);
                writer.newLine();
                writer.flush();

                writer.write(adminName);
                writer.newLine();
                writer.flush();

                Integer length = Integer.valueOf(reader.readLine());
                ArrayList<String> arrayList = new ArrayList<>();
                for (int i = 0; i < length; i++)
                    arrayList.add(reader.readLine());
                String str = "";
                for (int i = 0; i < arrayList.size();i++)
                    str = str + arrayList.get(i) + "\n";

                LoadedFileTextArea.setText(str);
                FileNameTextField.setText(fileName);
                OwnerTextField.setText(adminName);
                PermissionTextField.setText("Owner");
            }catch (IOException ioException){
                ioException.printStackTrace();
            }
        }else if(e.getSource() == SaveFile && EditorFile){
            String fullText = LoadedFileTextArea.getText();

            try {
                Socket socket = new Socket("127.0.0.1",8000);
                BufferedWriter writer =
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        socket.getOutputStream()));
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()));

                writer.write("5");
                writer.newLine();
                writer.flush();

                writer.write(OwnerTextField.getText());
                writer.newLine();
                writer.flush();

                writer.write(FileNameTextField.getText());
                writer.newLine();
                writer.flush();

                File file = new File("temp");
                FileWriter fileWriter = new FileWriter(file,false);
                fileWriter.write(fullText);
                fileWriter.flush();
                fileWriter.close();

                FileReader fileReader = new FileReader(file);
                Scanner scanner = new Scanner(fileReader);
                ArrayList<String> arrayList = new ArrayList<>();

                while (scanner.hasNextLine())
                    arrayList.add(scanner.nextLine());

                writer.write(Integer.toString(arrayList.size()));
                writer.newLine();
                writer.flush();
                for (int i = 0; i < arrayList.size();i++){
                    writer.write(arrayList.get(i));
                    writer.newLine();
                    writer.flush();
                }

                JOptionPane.showMessageDialog(null,
                        "File successfully saved!",
                        "title",
                        JOptionPane.PLAIN_MESSAGE);
            }catch (IOException ioException){
                ioException.printStackTrace();
            }
        }else if (e.getSource() == GivePermission){
            String userName = JOptionPane.showInputDialog("User Name: ");

            if(!userName.equals(""))
                try {
                    Socket socket = new Socket("127.0.0.1",8000);
                    BufferedWriter writer =
                            new BufferedWriter(
                                    new OutputStreamWriter(
                                            socket.getOutputStream()));
                    BufferedReader reader =
                            new BufferedReader(
                                    new InputStreamReader(
                                            socket.getInputStream()));

                    writer.write("6");
                    writer.newLine();
                    writer.flush();

                    writer.write(userName);
                    writer.newLine();
                    writer.flush();


                    String userPermission = JOptionPane.showInputDialog("Permission: ");
                    writer.write(userPermission);
                    writer.newLine();
                    writer.flush();

                    writer.write(adminName);
                    writer.newLine();
                    writer.flush();
                }catch (IOException ioException){
                    ioException.printStackTrace();
                }
        }else if(e.getSource() == OpenAnother){
            String anotherUser = JOptionPane.showInputDialog("User Name: ");

            if(!anotherUser.equals(""))
                try {
                    Socket socket = new Socket("127.0.0.1",8000);
                    BufferedWriter writer =
                            new BufferedWriter(
                                    new OutputStreamWriter(
                                            socket.getOutputStream()));
                    BufferedReader reader =
                            new BufferedReader(
                                    new InputStreamReader(
                                            socket.getInputStream()));

                    writer.write("7");
                    writer.newLine();
                    writer.flush();

                    writer.write(anotherUser);
                    writer.newLine();
                    writer.flush();

                    if (Integer.valueOf(reader.readLine()) == 1){
                        String anotherFileName = JOptionPane.showInputDialog("File Name: ");
                        writer.write(anotherFileName);
                        writer.newLine();
                        writer.flush();

                        writer.write(adminName);
                        writer.newLine();
                        writer.flush();

                        String permission = reader.readLine();
                        if (permission.equals("Owner") || permission.equals("Moderator")){
                            EditorFile = true;

                            Integer length = Integer.valueOf(reader.readLine());
                            ArrayList<String> arrayList = new ArrayList<>();
                            for (int i = 0; i < length; i++)
                                arrayList.add(reader.readLine());
                            String str = "";
                            for (int i = 0; i < arrayList.size();i++)
                                str = str + arrayList.get(i) + "\n";

                            LoadedFileTextArea.setText(str);
                            FileNameTextField.setText(anotherFileName);
                            OwnerTextField.setText(anotherUser);
                            PermissionTextField.setText(permission);

                            writer.write("Good");
                            writer.newLine();
                            writer.flush();
                        }else if (permission.equals("Viewer")){
                            EditorFile = false;

                            Integer length = Integer.valueOf(reader.readLine());
                            ArrayList<String> arrayList = new ArrayList<>();
                            for (int i = 0; i < length; i++)
                                arrayList.add(reader.readLine());
                            String str = "";
                            for (int i = 0; i < arrayList.size();i++)
                                str = str + arrayList.get(i) + "\n";

                            LoadedFileTextArea.setText(str);
                            FileNameTextField.setText(anotherFileName);
                            OwnerTextField.setText(anotherUser);
                            PermissionTextField.setText(permission);

                            writer.write("OK");
                            writer.newLine();
                            writer.flush();
                        }else if (permission.equals("Banned")) {
                            JOptionPane.showMessageDialog(null,
                                    "Your are Banned!",
                                    "BAN",
                                    JOptionPane.WARNING_MESSAGE);

                            writer.write("Bad");
                            writer.newLine();
                            writer.flush();
                        }
                    }else{
                        JOptionPane.showMessageDialog(null,
                                "User " + anotherUser + " are not exist!!",
                                "title",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }catch (IOException ioException){
                    ioException.printStackTrace();
                }
        }
    }
}
