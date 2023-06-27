package src;

import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JTextArea;

public class MedicalHistory {
    static final String MEDICAL_HISTORY_FILE_PATH = "data/medical_history.txt";

    public static void loadMedicalHistory(String patientName, TextArea medicalHistoryTextArea) {
        medicalHistoryTextArea.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(MEDICAL_HISTORY_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] historyData = line.split(",");
                if (historyData[0].equals(patientName)) {
                    medicalHistoryTextArea.appendText(
                            "Date: " + historyData[1] +
                                    "\nTime: " + historyData[2] +
                                    "\nWard: " + historyData[3] +
                                    "\nTreatment Results: " + historyData[4] +
                                    "\nObservation: " + historyData[5] +
                                    "\nMajor Complications: " + historyData[6] +
                                    "\nAttending Doctor/Nurse: " + historyData[7] +
                                    "\n\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadMH4P(String patientName, JTextArea textArea) {
        try (BufferedReader reader = new BufferedReader(new FileReader(MEDICAL_HISTORY_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] historyData = line.split(",");
                if (historyData[0].toUpperCase().equals(patientName)) {
                    textArea.append(
                            "Date: " + historyData[1] +
                                    "\nTime: " + historyData[2] +
                                    "\nWard: " + historyData[3] +
                                    "\nTreatment Results: " + historyData[4] +
                                    "\nObservation: " + historyData[5] +
                                    "\nMajor Complications: " + historyData[6] +
                                    "\nAttending Doctor/Nurse: " + historyData[7] +
                                    "\n\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
