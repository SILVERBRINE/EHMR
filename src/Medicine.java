package src;

import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JTextArea;

public class Medicine {
    static final String MEDICINE_FILE_PATH = "data/medicine.txt";

    public static void loadMedicineHistory(String patientName, TextArea medicineTextArea) {
        medicineTextArea.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(MEDICINE_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] mediData = line.split(",");
                if (mediData[0].equals(patientName)) {
                    medicineTextArea.appendText(
                            "Date: " + mediData[1] +
                                    "\nTime: " + mediData[2] +
                                    "\nDrug Prescription: " + mediData[3] +
                                    "\nDose: " + mediData[4] +
                                    "\nTaking Date: " + mediData[5] +
                                    "\nPharmacist: " + mediData[6] +
                                    "\n\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadMH4P(String patientName, JTextArea textArea) {
        try (BufferedReader reader = new BufferedReader(new FileReader(MEDICINE_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] mediData2 = line.split(",");
                if (mediData2[0].toUpperCase().equals(patientName)) {
                    textArea.append("Date: " + mediData2[1] +
                            "\nTime: " + mediData2[2] +
                            "\nDrug Prescription: " + mediData2[3] +
                            "\nDose: " + mediData2[4] +
                            "\nTaking Date: " + mediData2[5] +
                            "\nPharmacist: " + mediData2[6] +
                            "\n\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
