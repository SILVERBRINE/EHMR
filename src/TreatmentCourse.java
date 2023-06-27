package src;

import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JTextArea;

public class TreatmentCourse {
    static final String TREATMENT_COURSE_FILE_PATH = "data/treatment_course.txt";

    public static void loadTreatmentCourse(String patientName, TextArea treatmentCourseTextArea) {
        treatmentCourseTextArea.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(TREATMENT_COURSE_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] courseData = line.split(",");
                if (courseData[0].equals(patientName)) {
                    treatmentCourseTextArea.appendText(
                            "Start Date: " + courseData[1] +
                                    "\nEnd Date: " + courseData[2] +
                                    "\n\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadTC4P(String patientName, JTextArea textArea) {
        try (BufferedReader reader = new BufferedReader(new FileReader(TREATMENT_COURSE_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] courseData = line.split(",");
                if (courseData[0].toUpperCase().equals(patientName)) {
                    textArea.append(
                            "Start Date: " + courseData[1] +
                                    "\nEnd Date: " + courseData[2] +
                                    "\n\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
