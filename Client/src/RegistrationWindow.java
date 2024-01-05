import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class RegistrationWindow extends JFrame implements ActionListener {
    JButton SigninButton;
    JTextField UserNameField;
    JPasswordField PasswordField1,PasswordField2;

    RegistrationWindow(){
        this.setSize(500,500);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);

        //--------------------------------------------------
        JPanel UserNamePanel = new JPanel();
        UserNamePanel.setLayout(null);
        UserNamePanel.setBackground(Color.GRAY);
        UserNamePanel.setBounds(0,0,500,200);
        //--------------------------------------------------
        UserNameField = new JTextField();
        UserNameField.setBounds(135,145,250,50);
        UserNameField.setFont(new Font(null,Font.PLAIN,24));
        //--------------------------------------------------
        JLabel UserNameLabel = new JLabel();
        UserNameLabel.setText("User Name: ");
        UserNameLabel.setBounds(5,145,200,50);
        UserNameLabel.setFont(new Font(null,Font.PLAIN,24));
        JLabel label = new JLabel();
        label.setText("Registration");
        label.setFont(new Font(null,Font.PLAIN,34));
        label.setBounds(160,50,200,50);
        //--------------------------------------------------
        UserNamePanel.add(UserNameField);
        UserNamePanel.add(UserNameLabel);
        UserNamePanel.add(label);
        //--------------------------------------------------

        //--------------------------------------------------
        JPanel PasswordPanel = new JPanel();
        PasswordPanel.setLayout(null);
        PasswordPanel.setBackground(Color.GRAY);
        PasswordPanel.setBounds(0,200,500,150);
        //--------------------------------------------------
        PasswordField1 = new JPasswordField();
        PasswordField1.setBounds(160,2,250,50);
        PasswordField1.setEchoChar('*');
        PasswordField1.setFont(new Font(null,Font.PLAIN,24));
        PasswordField2 = new JPasswordField();
        PasswordField2.setBounds(160,55,250,50);
        PasswordField2.setEchoChar('*');
        PasswordField2.setFont(new Font(null,Font.PLAIN,24));
        //--------------------------------------------------
        JLabel PasswordLabel1 = new JLabel();
        PasswordLabel1.setText("Set Password: ");
        PasswordLabel1.setFont(new Font(null,Font.PLAIN,24));
        PasswordLabel1.setBounds(5,2,200,50);
        JLabel PasswordLabel2_1 = new JLabel();
        JLabel PasswordLabel2_2 = new JLabel();
        PasswordLabel2_1.setText("Confirm");
        PasswordLabel2_2.setText("Password: ");
        PasswordLabel2_1.setFont(new Font(null,Font.PLAIN,24));
        PasswordLabel2_1.setBounds(5,35,200,50);
        PasswordLabel2_2.setFont(new Font(null,Font.PLAIN,24));
        PasswordLabel2_2.setBounds(5,60,200,50);
        //--------------------------------------------------
        PasswordPanel.add(PasswordField1);
        PasswordPanel.add(PasswordField2);
        PasswordPanel.add(PasswordLabel1);
        PasswordPanel.add(PasswordLabel2_1);
        PasswordPanel.add(PasswordLabel2_2);
        //--------------------------------------------------

        //--------------------------------------------------
        JPanel ButtonPanel = new JPanel();
        ButtonPanel.setLayout(null);
        ButtonPanel.setBackground(Color.GRAY);
        ButtonPanel.setBounds(0,300,500,200);
        //--------------------------------------------------
        SigninButton = new JButton();
        SigninButton.setText("Sign In");
        SigninButton.setFocusable(false);
        SigninButton.setBounds(150,60,200,35);
        SigninButton.setFont(new Font(null,Font.PLAIN,24));
        SigninButton.addActionListener(this);
        //--------------------------------------------------
        ButtonPanel.add(SigninButton);
        //--------------------------------------------------


        this.add(PasswordPanel);
        this.add(UserNamePanel);
        this.add(ButtonPanel);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == SigninButton){
            if(UserNameField.getText().equals("")){
                JOptionPane.showMessageDialog(null,
                        "Feel User Name Field!",
                        "title",
                        JOptionPane.WARNING_MESSAGE);
            }else if(String.valueOf(PasswordField1.getPassword()).equals("")){
                JOptionPane.showMessageDialog(null,
                        "Feel Password Field!",
                        "title",
                        JOptionPane.WARNING_MESSAGE);
            }else if(String.valueOf(PasswordField2.getPassword()).equals("")){
                JOptionPane.showMessageDialog(null,
                        "Confirm your password!",
                        "title",
                        JOptionPane.WARNING_MESSAGE);
            }else if(!(String.valueOf(PasswordField1.getPassword()).equals(String.valueOf(PasswordField2.getPassword())))){
                JOptionPane.showMessageDialog(null,
                        "Your passwords are not match!",
                        "title",
                        JOptionPane.WARNING_MESSAGE);
            }else {
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
                    
                    //System.out.println("Error of stack here?");

                    writer.write("2");
                    writer.newLine();
                    writer.flush();

                    writer.write(UserNameField.getText());
                    writer.newLine();
                    writer.flush();
                    
                    writer.write(String.valueOf(PasswordField1.getPassword()));
                    writer.newLine();
                    writer.flush();

                    if(Integer.valueOf(reader.readLine()) == 1)
                        JOptionPane.showMessageDialog(null,
                                "User with this name is already exist!",
                                "title",
                                JOptionPane.WARNING_MESSAGE);
                    else {                      
                        JOptionPane.showMessageDialog(null,
                                "You are successfully sign in!",
                                "title",
                                JOptionPane.PLAIN_MESSAGE);
                        this.dispose();
                    }

                }catch (IOException ioException){
                    ioException.printStackTrace();
                }
            }
        }
    }
}
