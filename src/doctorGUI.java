package src;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.ListChangeListener;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class doctorGUI {
    public static ListView<String> patientListView;
    private TextField searchTextField;
    private TextArea patientInfoTextArea;
    private TextArea medicalHistoryTextArea;
    private TextArea treatmentCourseTextArea;
    private TextArea MedicineTextArea;

    public void doctorUI(Stage primaryStage) {
        javafx.scene.image.Image backgroundImage = new javafx.scene.image.Image("image/doc_background.png");
        BackgroundImage backgroundImg = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImg);

        Stage doctorGUI = new Stage();
        primaryStage.setTitle("Electronic Medical Record");

        // Leftside layout
        VBox leftVBox = new VBox();
        leftVBox.setPadding(new Insets(10));
        leftVBox.setSpacing(10);
        leftVBox.setBackground(background);

        Label searchLabel = new Label("Search(Type Name or ID):");
        searchTextField = new TextField();

        Button searchButton = new Button("Search");
        searchButton.getStyleClass().add("search-button");
        searchButton.setOnAction(e -> Patient.searchPatient(searchTextField.getText().toLowerCase()));

        // remove button
        Button removeButton = new Button("Remove");
        removeButton.getStyleClass().add("remove-button");
        removeButton.setOnAction(e -> removePatient());

        patientListView = new ListView<>();
        patientListView.setPrefWidth(200);
        patientListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPatientInfo(newValue));

        leftVBox.getChildren().addAll(searchLabel, searchTextField, searchButton, patientListView, removeButton);

        // layout for right
        BorderPane rightBorderPane = new BorderPane();

        patientInfoTextArea = new TextArea();
        patientInfoTextArea.setEditable(false);
        patientInfoTextArea.getStyleClass().add("textArea");

        medicalHistoryTextArea = new TextArea();
        medicalHistoryTextArea.setEditable(false);
        medicalHistoryTextArea.getStyleClass().add("textArea");

        treatmentCourseTextArea = new TextArea();
        treatmentCourseTextArea.setEditable(false);
        treatmentCourseTextArea.getStyleClass().add("textArea");

        MedicineTextArea = new TextArea();
        MedicineTextArea.setEditable(false);
        MedicineTextArea.getStyleClass().add("textArea");

        TabPane tabPane = new TabPane();
        Tab patientInfoTab = new Tab("Patient Info", patientInfoTextArea);
        Tab medicalHistoryTab = new Tab("Medical History", medicalHistoryTextArea);
        Tab treatmentCourseTab = new Tab("Treatment Course", treatmentCourseTextArea);
        Tab MedicineTab = new Tab("Medicine", MedicineTextArea);
        tabPane.getTabs().addAll(patientInfoTab, medicalHistoryTab, treatmentCourseTab, MedicineTab);

        rightBorderPane.setCenter(tabPane);

        // patient button
        Button newPatientButton = new Button("New Patient");
        newPatientButton.setOnAction(e -> openNewPatientScreen(doctorGUI));

        // remember closed taps
        List<Tab> closedTabs = new ArrayList<>();

        // restorreButton
        Button restoreButton = new Button("Restore Tab");
        restoreButton.setOnAction(e -> {
            for (Tab tab : closedTabs) {
                if (!tabPane.getTabs().contains(tab)) {
                    tabPane.getTabs().add(tab);
                }
            }
            closedTabs.clear();
        });

        tabPane.getTabs().addListener((ListChangeListener.Change<? extends Tab> change) -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    List<? extends Tab> removedTabs = change.getRemoved();
                    closedTabs.addAll(removedTabs);
                }
            }
        });

        Region gapRegion = new Region();
        HBox.setHgrow(gapRegion, Priority.ALWAYS);

        // diagnosis button
        Button diagnosisButton = new Button("Diagnosis");
        diagnosisButton.setOnAction(e -> {
            if (patientListView.getSelectionModel().getSelectedItem() == null) {
                // No patient is selected, return or show an error message
                System.out.println("No patient selected.");
            } else {
                openNewDiagnosisScreen();
            }
        });

        // buttonBox on top
        HBox buttonBox = new HBox(restoreButton, gapRegion, diagnosisButton, newPatientButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        rightBorderPane.setTop(buttonBox);

        // Main layout
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(leftVBox);
        borderPane.setCenter(rightBorderPane);

        Scene scene = new Scene(borderPane, 800, 600);
        scene.getStylesheets().add("src/styles.css");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Load patient list
        Patient.loadPatientList();
        patientListView.setItems(Patient.patientList);

        primaryStage.setOnCloseRequest(event -> {
            // Run the checkWindow class or perform any desired actions
            checkWindow checkWindowInstance = new checkWindow();
            checkWindowInstance.windowClosing(null);
        });
    }

    private void showPatientInfo(String patientName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(Patient.PATIENTS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] patientData = line.split(",");
                if (patientData[1].equals(patientName)) {
                    patientInfoTextArea.setText(
                            "Patient ID: " + patientData[0] +
                                    "\nName: " + patientData[1] +
                                    "\nNational ID: " + patientData[2] +
                                    "\nAge: " + patientData[3] +
                                    "\nGender: " + patientData[4] +
                                    "\nAddress: " + patientData[5] +
                                    "\nContact Number: " + patientData[6]);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load medical history
        MedicalHistory.loadMedicalHistory(patientName, medicalHistoryTextArea);

        // Load treatment course
        TreatmentCourse.loadTreatmentCourse(patientName, treatmentCourseTextArea);

        Medicine.loadMedicineHistory(patientName, MedicineTextArea);
    }

    private void openNewPatientScreen(Stage doctorGUI) {
        Stage newPatientStage = new Stage();
        newPatientStage.setTitle("New Patient");

        // Form layout
        GridPane formGridPane = new GridPane();
        formGridPane.setAlignment(Pos.CENTER);
        formGridPane.setHgap(10);
        formGridPane.setVgap(10);
        formGridPane.setPadding(new Insets(10));

        Label nameLabel = new Label("Name:");
        TextField nameTextField = new TextField();

        Label idLabel = new Label("ID:");
        TextField idTextField = new TextField();

        Label nationalIdLabel = new Label("National ID:");
        TextField nationalIdTextField = new TextField();

        Label ageLabel = new Label("Age:");
        TextField ageTextField = new TextField();

        Label genderLabel = new Label("Gender:");
        ComboBox<String> genderComboBox = new ComboBox<>();
        genderComboBox.getItems().addAll("Male", "Female", "Other");

        Label addressLabel = new Label("Address:");
        TextField addressTextField = new TextField();

        Label contactNumberLabel = new Label("Contact Number:");
        TextField contactNumberTextField = new TextField();

        Button saveButton = new Button("Add New Patients");
        saveButton.setOnAction(e -> {
            Patient.saveNewPatient(
                    nameTextField.getText(),
                    idTextField.getText(),
                    nationalIdTextField.getText(),
                    ageTextField.getText(),
                    genderComboBox.getValue(),
                    addressTextField.getText(),
                    contactNumberTextField.getText());
            newPatientStage.close();
        });

        formGridPane.addColumn(0, nameLabel, idLabel, nationalIdLabel, ageLabel, genderLabel, addressLabel,
                contactNumberLabel);
        formGridPane.addColumn(1, nameTextField, idTextField, nationalIdTextField, ageTextField, genderComboBox,
                addressTextField, contactNumberTextField);
        formGridPane.add(saveButton, 1, 7);

        Scene newPatientScene = new Scene(formGridPane, 400, 400);
        newPatientStage.setScene(newPatientScene);
        newPatientStage.initOwner(doctorGUI);
        newPatientStage.showAndWait();
    }

    private void openNewDiagnosisScreen() {
        String selectedPatient = patientListView.getSelectionModel().getSelectedItem();
        System.out.println(selectedPatient);
        Stage DiagnosisStage = new Stage();
        DiagnosisStage.setTitle("New Diagnosis of: " + selectedPatient);

        GridPane formGridPane2 = new GridPane();
        formGridPane2.setAlignment(Pos.CENTER_LEFT);
        formGridPane2.setHgap(10);
        formGridPane2.setVgap(10);
        formGridPane2.setPadding(new Insets(10));

        // Set background image
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image("image/diagnosis.png", 1280, 720, false, true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        formGridPane2.setBackground(new Background(backgroundImage));
        formGridPane2.setVisible(true);

        // Date field
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select date");
        formGridPane2.add(new Label("Date:"), 0, 0);
        formGridPane2.add(datePicker, 1, 0);

        // Time field
        TextField timeField = new TextField();
        timeField.setPromptText("Enter time");
        formGridPane2.add(new Label("Time:"), 0, 1);
        formGridPane2.add(timeField, 1, 1);

        // Ward field
        TextField wardField = new TextField();
        wardField.setPromptText("Enter ward");
        formGridPane2.add(new Label("Ward:"), 0, 2);
        formGridPane2.add(wardField, 1, 2);

        // Treatment results field
        TextArea treatmentResultsArea = new TextArea();
        treatmentResultsArea.setPromptText("Enter treatment results");
        treatmentResultsArea.setPrefRowCount(5);
        formGridPane2.add(new Label("Treatment Results:"), 0, 3);
        formGridPane2.add(treatmentResultsArea, 1, 3);

        // Observation field
        TextArea observationArea = new TextArea();
        observationArea.setPromptText("Enter observation");
        observationArea.setPrefRowCount(5);
        formGridPane2.add(new Label("Observation:"), 0, 4);
        formGridPane2.add(observationArea, 1, 4);

        // Major complications field
        TextField complicationsField = new TextField();
        complicationsField.setPromptText("Enter major complications");
        formGridPane2.add(new Label("Major Complications:"), 0, 5);
        formGridPane2.add(complicationsField, 1, 5);

        // Attending doctor/nurse field
        TextField attendingField = new TextField();
        attendingField.setPromptText("Enter attending doctor/nurse");
        formGridPane2.add(new Label("Attending Doctor/Nurse:"), 0, 6);
        formGridPane2.add(attendingField, 1, 6);

        // Medicine field
        TextField medicineField = new TextField();
        medicineField.setPromptText("Enter medicine");
        formGridPane2.add(new Label("Medicine:"), 0, 7);
        formGridPane2.add(medicineField, 1, 7);

        // Period field
        TextField periodField = new TextField();
        periodField.setPromptText("Enter period");
        formGridPane2.add(new Label("Period:"), 0, 8);
        formGridPane2.add(periodField, 1, 8);

        // Patient's medicine allergy field
        TextField takingdateField = new TextField();
        takingdateField.setPromptText("Enter patient's Medicine Taking Dates");
        formGridPane2.add(new Label("Patient's Medicine Medicine Taking Dates:"), 0, 9);
        formGridPane2.add(takingdateField, 1, 9);

        // Treatment course start field
        DatePicker startPicker = new DatePicker();
        startPicker.setPromptText("Select start date");
        formGridPane2.add(new Label("Treatment Course Start:"), 0, 10);
        formGridPane2.add(startPicker, 1, 10);

        // Treatment course end (expected) field
        DatePicker endPicker = new DatePicker();
        endPicker.setPromptText("Select end date");
        formGridPane2.add(new Label("Treatment Course End (Expected):"), 0, 11);
        formGridPane2.add(endPicker, 1, 11);

        Button diagnoseButton = new Button("Diagnose");
        diagnoseButton.setOnAction(e -> {
            // Retrieve the entered values
            String date = datePicker.getValue().toString();
            String time = timeField.getText();
            String ward = wardField.getText();
            String treatmentResults = treatmentResultsArea.getText();
            String observation = observationArea.getText();
            String complications = complicationsField.getText();
            String attending = attendingField.getText();
            String medicine = medicineField.getText();
            String period = periodField.getText();
            String takingdate = takingdateField.getText();
            String start = startPicker.getValue().toString();
            String end = endPicker.getValue().toString();

            // Write to medical_history.txt
            writeToFile(
                    selectedPatient + "," +
                            date + "," +
                            time + "," +
                            ward + "," +
                            treatmentResults + "," +
                            observation + "," +
                            complications + "," +
                            attending,
                    MedicalHistory.MEDICAL_HISTORY_FILE_PATH);

            // Write to medicine.txt
            writeToFile(selectedPatient + "," + date + "," + time + "," + medicine + "," + period + "," + takingdate
                    + "," + attending, Medicine.MEDICINE_FILE_PATH);

            // Write to treatment_course.txt
            writeToFile(selectedPatient + "," + start + "," + end, TreatmentCourse.TREATMENT_COURSE_FILE_PATH);

            // Display a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Diagnosis Result");
            alert.setHeaderText(null);
            String result = "Name: " + selectedPatient +
                    "\nDate: " + date +
                    "\nTime: " + time +
                    "\nWard: " + ward +
                    "\nTreatment Results: " + treatmentResults +
                    "\nObservation: " + observation +
                    "\nMajor Complications: " + complications +
                    "\nAttending Doctor/Nurse: " + attending +
                    "\nMedicine: " + medicine +
                    "\nPeriod: " + period +
                    "\nPatient's Medicine Taking Dates: " + takingdate +
                    "\nTreatment Course Start: " + start +
                    "\nTreatment Course End (Expected): " + end;
            alert.setContentText(result);
            alert.showAndWait();
            formGridPane2.setVisible(false);
            DiagnosisStage.close();
        });
        formGridPane2.add(diagnoseButton, 1, 12);

        Scene scene = new Scene(formGridPane2, 1280, 720);
        DiagnosisStage.setScene(scene);
        DiagnosisStage.show();
    }

    private void writeToFile(String data, String fileName) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(data + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removePatient() {
        String selectedPatient = patientListView.getSelectionModel().getSelectedItem();

        if (selectedPatient != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Remove Patient");
            confirmationAlert.setHeaderText("Removing Patient Confirmation");
            confirmationAlert.setContentText("Are you sure you want to remove this patient?");

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try (BufferedReader reader = new BufferedReader(new FileReader(Patient.PATIENTS_FILE_PATH));
                        BufferedWriter writer = new BufferedWriter(new FileWriter("temp.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] patientData = line.split(",");
                        String patientName = patientData[1];

                        if (!patientName.equals(selectedPatient)) {
                            writer.write(line);
                            writer.newLine();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                File patientsFile = new File(Patient.PATIENTS_FILE_PATH);
                File tempFile = new File("temp.txt");

                if (patientsFile.delete()) {
                    if (!tempFile.renameTo(patientsFile)) {
                        System.err.println("Failed to rename the temporary file.");
                    }
                } else {
                    System.err.println("Failed to delete the original file.");
                }

                // Clear the UI
                patientListView.getSelectionModel().clearSelection();
                patientInfoTextArea.clear();
                medicalHistoryTextArea.clear();
                treatmentCourseTextArea.clear();

                // Refresh patient list
                Patient.loadPatientList();
                patientListView.setItems(Patient.patientList);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Patient Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a patient to remove.");
            alert.showAndWait();
        }
    }

}