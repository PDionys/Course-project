import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class LoginScreen implements ActionListener {
    JFrame frame;
    JButton LoginButton, RegistrationButton;
    JTextField UserNameTextField;
    JPasswordField PasswordField;

    public void CreateLoginScreen(){
        frame = new JFrame();
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);

        //---------------------------------------------------------------- Panel 1
        JPanel UserNamePanel = new JPanel();
        UserNamePanel.setBounds(0,0,500,166);
        UserNamePanel.setBackground(Color.GRAY);
        UserNamePanel.setLayout(null);
        //--------------------------------------------------------------- Text Field
        UserNameTextField = new JTextField();
        UserNameTextField.setBounds(135,110,250,50);
        UserNameTextField.setFont(new Font(null,Font.PLAIN,24));
        //--------------------------------------------------------------- Label
        JLabel UserNameLabel = new JLabel();
        UserNameLabel.setText("User Name: ");
        UserNameLabel.setBounds(5,110,200,50);
        UserNameLabel.setFont(new Font(null,Font.PLAIN,24));
        //--------------------------------------------------------------- Panel.add
        UserNamePanel.add(UserNameTextField);
        UserNamePanel.add(UserNameLabel);
        //---------------------------------------------------------------

        //--------------------------------------------------------------- Panel 2
        JPanel PasswordPanel = new JPanel();
        PasswordPanel.setBounds(0,166,500,100);
        PasswordPanel.setBackground(Color.GRAY);
        PasswordPanel.setLayout(null);
        //---------------------------------------------------------------- Password Field
        PasswordField = new JPasswordField();
        PasswordField.setBounds(135,2,250,50);
        PasswordField.setEchoChar('*');
        PasswordField.setFont(new Font(null,Font.PLAIN,24));
        //---------------------------------------------------------------- Label
        JLabel PasswordLabel = new JLabel();
        PasswordLabel.setText("Password: ");
        PasswordLabel.setFont(new Font(null,Font.PLAIN,24));
        PasswordLabel.setBounds(5,2,200,50);
        //---------------------------------------------------------------- Panel.add
        PasswordPanel.add(PasswordField);
        PasswordPanel.add(PasswordLabel);
        //----------------------------------------------------------------

        //--------------------------------------------------------------- Panel 3
        JPanel ButtonsPanel = new JPanel();
        ButtonsPanel.setBounds(0,265,500,220);
        ButtonsPanel.setBackground(Color.GRAY);
        ButtonsPanel.setLayout(null);
        //--------------------------------------------------------------- Login Button
        LoginButton = new JButton();
        LoginButton.setText("Login");
        LoginButton.setFocusable(false);
        LoginButton.setBounds(50,5,150,35);
        LoginButton.setFont(new Font(null,Font.PLAIN,24));
        LoginButton.addActionListener(this);
        //--------------------------------------------------------------- Registration Button
        RegistrationButton = new JButton();
        RegistrationButton.setText("Registration");
        RegistrationButton.setFocusable(false);
        RegistrationButton.setBounds(205,5,200,35);
        RegistrationButton.setFont(new Font(null,Font.PLAIN,24));
        RegistrationButton.addActionListener(this);
        //--------------------------------------------------------------- Panel.add
        ButtonsPanel.add(LoginButton);
        ButtonsPanel.add(RegistrationButton);
        //---------------------------------------------------------------

        frame.add(UserNamePanel);
        frame.add(PasswordPanel);
        frame.add(ButtonsPanel);
        frame.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == LoginButton) {
            String userName = UserNameTextField.getText();
            String userPass = String.valueOf(PasswordField.getPassword());

            try {
                Socket socket = new Socket("127.0.0.1", 8000);
                BufferedWriter writer =
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        socket.getOutputStream()));
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()));

                writer.write("1");
                writer.newLine();
                writer.flush();

                writer.write(userName);
                writer.newLine();
                writer.flush();

                writer.write(userPass);
                writer.newLine();
                writer.flush();

                if (Integer.valueOf(reader.readLine()) == 1) {
                    /*JOptionPane.showMessageDialog(null,
                            "You are successfully login!",
                            "title",
                            JOptionPane.PLAIN_MESSAGE);*/
                    MainScreen mainScreen = new MainScreen();
                    mainScreen.CreateMainFrame(userName);
                    UserNameTextField.setText("");
                    PasswordField.setText("");
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Your username or password is incorrect! Try again.",
                            "title",
                            JOptionPane.WARNING_MESSAGE);
                }

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        if (e.getSource() == RegistrationButton){
            new RegistrationWindow();
        }
    }
}

