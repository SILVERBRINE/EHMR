package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Patient {
    public static ObservableList<String> patientList;
    public static final String PATIENTS_FILE_PATH = "data/patients.txt";

    public static void loadPatientList() {
        patientList = FXCollections.observableArrayList();

        try (BufferedReader reader = new BufferedReader(new FileReader(PATIENTS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] patientData = line.split(",");
                patientList.add(patientData[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void searchPatient(String searchTextField) {
        String searchTerm = searchTextField;

        if (!searchTerm.isEmpty()) {
            List<String> matchingPatients = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(PATIENTS_FILE_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] patientData = line.split(",");
                    String patientId = patientData[0].toLowerCase();
                    String patientName = patientData[1].toLowerCase();

                    if (patientId.contains(searchTerm) || patientName.contains(searchTerm)) {
                        matchingPatients.add(patientData[1]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                loadPatientList();
            }

            patientList.setAll(matchingPatients);
        } else {
            loadPatientList();
            doctorGUI.patientListView.setItems(patientList);
        }
    }

    public static void saveNewPatient(String name, String id, String nationalId, String age, String gender,
            String address,
            String contactNumber) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PATIENTS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] patientData = line.split(",");
                String patientName = patientData[1];
                if (patientName.equals(name)) {
                    // if there is same name;
                    // showAlert("Error");
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText("Duplicate Name Error");
                    alert.setContentText("Same name of patient is already exist");
                    alert.showAndWait();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATIENTS_FILE_PATH, true))) {
            writer.write(id + "," + name + "," + nationalId + "," + age + "," + gender + "," + address + ","
                    + contactNumber);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Refresh patient list
        loadPatientList();
        doctorGUI.patientListView.setItems(patientList);
    }

    public void patientUI(Stage primaryStage, String ID, String Name) {
        JFrame frame = new JFrame();
        ImageIcon icon = new ImageIcon("image/icon.png");
        JPanel icon1 = new JPanel() {
            public void paint(Graphics g) {
                g.drawImage(icon.getImage(), 0, 0, null);
                setOpaque(false);
                super.paint(g);
            }
        };

        icon1.setPreferredSize(new Dimension(1280, 70));
        icon1.setLayout(null);
        frame.add(icon1, BorderLayout.NORTH);

        JButton btnPrint, btnExit;

        frame.setSize(1280, 720);
        frame.setVisible(true);
        frame.setTitle("Patient Medical Record Access");
        frame.setResizable(false);
        // frame.getContentPane().setBackground(java.awt.Color.WHITE);

        Font font0 = new Font(Font.SERIF, Font.PLAIN, 34);
        Font font1 = new Font("Helvetica", Font.PLAIN, 20);
        Font font2 = new Font(Font.SANS_SERIF, Font.BOLD, 26);
        Font font3 = new Font(Font.MONOSPACED, Font.BOLD, 26);
        Font font4 = new Font(Font.SANS_SERIF, Font.ITALIC | Font.BOLD, 24);
        Font fontBtn = new Font("Helvetica", Font.PLAIN, 18);

        int y1 = 180;
        int x1 = 30;
        int x2 = 670;

        String Pname = Name.toUpperCase();
        JLabel welCome1 = new JLabel(Pname + " (" + ID + ") Welcome!");
        welCome1.setFont(font0);
        welCome1.setBounds(10, 85, 600, 30);
        frame.add(welCome1);
        JLabel welCome2 = new JLabel("This is your Medical Record:");
        welCome2.setForeground(java.awt.Color.DARK_GRAY);
        welCome2.setFont(font2);
        welCome2.setBounds(10, 140, 600, 30);
        frame.add(welCome2);
        // set UI and load data from pations.txt
        try (BufferedReader reader = new BufferedReader(new FileReader(Patient.PATIENTS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] patientData = line.split(",");
                if (patientData[1].toUpperCase().equals(Pname)) {
                    JLabel nID = new JLabel("National ID: " + patientData[2]);
                    JLabel Age = new JLabel("Age: " + patientData[3]);
                    JLabel pNum = new JLabel("Phone No. " + patientData[6]);
                    JLabel Gender = new JLabel("Gender: " + patientData[4]);
                    JLabel Address = new JLabel("Address: " + patientData[5]);

                    nID.setFont(font3);
                    Age.setFont(font3);
                    pNum.setFont(font3);
                    Gender.setFont(font3);
                    Address.setFont(font3);

                    nID.setBounds(x1, y1 + 10, 400, 30);
                    Age.setBounds(x1 + 400, y1 + 10, 180, 30);
                    pNum.setBounds(x1, y1 + 70, 400, 30);
                    Gender.setBounds(x1 + 350, y1 + 70, 200, 30);
                    Address.setBounds(x1, y1 + 130, 580, 30);

                    nID.setForeground(java.awt.Color.DARK_GRAY);
                    Age.setForeground(java.awt.Color.DARK_GRAY);
                    pNum.setForeground(java.awt.Color.DARK_GRAY);
                    Gender.setForeground(java.awt.Color.DARK_GRAY);
                    Address.setForeground(java.awt.Color.DARK_GRAY);

                    frame.add(nID);
                    frame.add(Age);
                    frame.add(pNum);
                    frame.add(Gender);
                    frame.add(Address);

                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // patient ui of Medicine History
        JLabel medi = new JLabel("Medicine History:");
        medi.setFont(font4);
        medi.setBounds(x1 - 10, y1 + 200, 580, 30);
        frame.add(medi);

        JTextArea p_Medi = new JTextArea();
        JScrollPane p_MediS = new JScrollPane(p_Medi);
        Medicine.loadMH4P(Pname, p_Medi);
        p_Medi.setEditable(false);
        // p_Medi.setBounds(x1 + 10, y1 + 240, 560, 240);
        p_Medi.setFont(font1);
        p_Medi.setBackground(null);
        // p_Medi.setBackground(java.awt.Color.WHITE);

        p_MediS.setBounds(x1, y1 + 240, 580, 240);
        frame.add(p_MediS);

        // patient ui of Treatment Course
        JLabel treatC = new JLabel("Treatment Course:");
        treatC.setFont(font4);
        treatC.setBounds(x2 - 10, y1 - 95, 580, 30);
        frame.add(treatC);

        JTextArea p_treatC = new JTextArea();
        JScrollPane p_treatCS = new JScrollPane(p_treatC);
        TreatmentCourse.loadTC4P(Pname, p_treatC);
        p_treatC.setEditable(false);
        p_treatC.setFont(font1);
        p_treatC.setBackground(null);

        p_treatCS.setBounds(x2, y1 - 55, 580, 120);
        frame.add(p_treatCS);

        // patient ui of Medical History
        JLabel mediHistory = new JLabel("Medical History:");
        mediHistory.setFont(font4);
        mediHistory.setBounds(x2 - 10, y1 + 80, 580, 30);
        frame.add(mediHistory);

        JTextArea p_mediHistory = new JTextArea();
        JScrollPane p_mediHistoryS = new JScrollPane(p_mediHistory);
        MedicalHistory.loadMH4P(Pname, p_mediHistory);
        p_mediHistory.setEditable(false);
        p_mediHistory.setFont(font1);
        p_mediHistory.setBackground(null);

        p_mediHistoryS.setBounds(x2, y1 + 120, 580, 360);
        frame.add(p_mediHistoryS);

        JLabel Junk = new JLabel(" ");
        frame.add(Junk);

        // button for exit
        btnExit = new JButton("LogOut");
        btnExit.setFont(fontBtn);
        btnExit.setBorder(new RBtn(15));
        btnExit.setBackground(new java.awt.Color(240, 255, 251));
        btnExit.setBounds(1150, 30, 100, 30);
        icon1.add(btnExit);
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                GUI.setVisible(true);
            }

        });

        // button for print
        btnPrint = new JButton("Print");
        btnPrint.setFont(fontBtn);
        btnPrint.setBorder(new RBtn(15));
        btnPrint.setBackground(new java.awt.Color(240, 255, 251));
        btnPrint.setBounds(1040, 30, 100, 30);
        icon1.add(btnPrint);
        btnPrint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Printer.printComponent(frame);
            }
        });
        frame.addWindowListener(new checkWindow());
    }
}
