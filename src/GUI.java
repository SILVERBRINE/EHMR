package src;

import javafx.application.Platform;
import javax.swing.*;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.*;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;

public class GUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    static JFrame f;
    JButton btnLogin, btnExit;
    JTextField txtName, txtID;
    ImageIcon back;
    JPanel background;

    public GUI() {
        back = new ImageIcon("image/login_background.png");
        f = new JFrame();
        background = new JPanel() {
            public void paint(Graphics g) {
                g.drawImage(back.getImage(), 0, 0, null);
                setOpaque(false);
                super.paint(g);
            }
        };
    }

    public void start(Stage primaryStage) {
        f.addWindowListener(new checkWindow());
        primaryStage.setTitle("EHMR Login");
        f.setSize(800, 600);
        f.setTitle("EHMR Login");
        f.setResizable(false);
        background.setLayout(null);
        background.setBackground(new Color(232, 255, 249));

        int intX = 360;
        Font font1 = new Font(Font.SERIF, Font.BOLD, 34);
        Font font2 = new Font("Helvetica", Font.BOLD, 28);
        Font font3 = new Font(Font.MONOSPACED, Font.PLAIN, 30);
        Font fontBtn = new Font("Helvetica", Font.BOLD, 18);
        JLabel welcome1 = new JLabel("Enter ID/Name with");
        welcome1.setFont(font1);
        welcome1.setForeground(Color.DARK_GRAY);
        welcome1.setBounds(intX, 10, 500, 50);
        background.add(welcome1);

        JLabel welcome2 = new JLabel("Diagnosis from Hospital");
        welcome2.setFont(font1);
        welcome2.setForeground(Color.DARK_GRAY);
        welcome2.setBounds(intX, 60, 400, 50);
        background.add(welcome2);

        JLabel patientID = new JLabel("Enter Your ID");
        patientID.setForeground(Color.GRAY);
        patientID.setFont(font2);
        patientID.setBounds(intX, 160, 300, 50);
        background.add(patientID);
        txtID = new JTextField();
        txtID.setFont(font3);
        txtID.setBounds(intX, 210, 300, 50);
        background.add(txtID);

        JLabel patientName = new JLabel("Enter Your Name");
        patientName.setForeground(Color.GRAY);
        patientName.setBounds(intX, 300, 300, 50);
        patientName.setFont(font2);
        background.add(patientName);
        txtName = new JTextField();
        txtName.setFont(font3);
        txtName.setBounds(intX, 350, 300, 50);
        background.add(txtName);

        btnLogin = new JButton("Login");
        btnLogin.setFont(fontBtn);
        btnLogin.setBounds(550, 420, 150, 50);
        btnLogin.addActionListener(null);
        btnLogin.setBorder(new RBtn(30));
        btnLogin.setBackground(new Color(240, 255, 251));
        background.add(btnLogin);

        // damn exit button
        btnExit = new JButton("Exit");
        btnExit.setFont(fontBtn);
        btnExit.setBackground(Color.WHITE);
        btnExit.setBounds(690, 520, 80, 30);

        // LOGIN
        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tmpName = txtName.getText().toLowerCase();
                String tmpID = txtID.getText().toLowerCase();
                System.out.println(tmpName + ", " + tmpID);
                if (e.getSource() == btnLogin) {
                    if (tmpID.equals("doctor") && tmpName.equals("doctor")) {
                        Platform.runLater(() -> {
                            doctorGUI gui = new doctorGUI();
                            gui.doctorUI(primaryStage);
                            f.setVisible(false);
                            txtID.setText("");
                            txtName.setText("");
                        });
                    } else {
                        // find patient record for
                        try (BufferedReader reader = new BufferedReader(new FileReader(Patient.PATIENTS_FILE_PATH))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                String[] patientData = line.split(",");
                                String patientId = patientData[0].toLowerCase();
                                String patientName = patientData[1].toLowerCase();

                                if (patientName.equals(tmpName) && patientId.equals(tmpID)) {
                                    Patient patient = new Patient();
                                    patient.patientUI(primaryStage, txtID.getText(), txtName.getText());
                                    f.setVisible(false);
                                    txtID.setText("");
                                    txtName.setText("");
                                    break;
                                }
                            }
                        } catch (IOException i) {
                            i.printStackTrace();
                        }
                    }
                } else if (e.getSource() == btnExit)
                    System.exit(0);
            }
        };
        btnExit.addActionListener(action);
        btnLogin.addActionListener(action);

        background.add(btnExit);
        f.add(background);
        f.setVisible(true);
    }

    public static void setVisible(boolean visible) {
        f.setVisible(true);
    }

}
