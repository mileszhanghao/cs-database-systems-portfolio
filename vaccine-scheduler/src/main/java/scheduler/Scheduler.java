package scheduler;

import scheduler.db.ConnectionManager;
import scheduler.model.Caregiver;
import scheduler.model.Patient;
import scheduler.model.Vaccine;
import scheduler.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class Scheduler {

    // objects to keep track of the currently logged-in user
    // Note: it is always true that at most one of currentCaregiver and currentPatient is not null
    //       since only one user can be logged-in at a time
    private static Caregiver currentCaregiver = null;
    private static Patient currentPatient = null;

    public static void main(String[] args) {
        // printing greetings text
        System.out.println();
        System.out.println("Welcome to the COVID-19 Vaccine Reservation Scheduling Application!");
        System.out.println("*** Please enter one of the following commands ***");
        System.out.println("> create_patient <username> <password>");
        System.out.println("> create_caregiver <username> <password>");
        System.out.println("> login_patient <username> <password>");
        System.out.println("> login_caregiver <username> <password>");
        System.out.println("> search_caregiver_schedule <date>");
        System.out.println("> reserve <date> <vaccine>");
        System.out.println("> upload_availability <date>");
        System.out.println("> cancel <appointment_id>");
        System.out.println("> add_doses <vaccine> <number>");
        System.out.println("> show_appointments");
        System.out.println("> logout");
        System.out.println("> quit");
        System.out.println();

        // read input from user
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("> ");
            String response = "";
            try {
                response = r.readLine();
            } catch (IOException e) {
                System.out.println("Please try again!");
            }
            // split the user input by spaces
            String[] tokens = response.split(" ");
            // check if input exists
            if (tokens.length == 0) {
                System.out.println("Please try again!");
                continue;
            }
            // determine which operation to perform
            String operation = tokens[0];
            if (operation.equals("create_patient")) {
                createPatient(tokens);
            } else if (operation.equals("create_caregiver")) {
                createCaregiver(tokens);
            } else if (operation.equals("login_patient")) {
                loginPatient(tokens);
            } else if (operation.equals("login_caregiver")) {
                loginCaregiver(tokens);
            } else if (operation.equals("search_caregiver_schedule")) {
                searchCaregiverSchedule(tokens);
            } else if (operation.equals("reserve")) {
                reserve(tokens);
            } else if (operation.equals("upload_availability")) {
                uploadAvailability(tokens);
            } else if (operation.equals("cancel")) {
                cancel(tokens);
            } else if (operation.equals("add_doses")) {
                addDoses(tokens);
            } else if (operation.equals("show_appointments")) {
                showAppointments(tokens);
            } else if (operation.equals("logout")) {
                logout(tokens);
            } else if (operation.equals("quit")) {
                System.out.println("Bye!");
                return;
            } else if (operation.equals("cancel")) {
                cancel(tokens);
            } else {
                System.out.println("Invalid operation name!");
            }
        }
    }

    private static void createPatient(String[] tokens) {
        if (tokens.length != 3) {
            System.out.println("Create patient failed");
            return;
        }
        String username = tokens[1];
        String password = tokens[2];

        // Validate password strength
        if (!isValidPassword(password)) {
            System.out.println("Password does not meet the criteria. Please try again.");
            return;
        }

        if (usernameExistsPatient(username)) {
            System.out.println("Username taken, try again");
            return;
        }

        byte[] salt = Util.generateSalt();
        byte[] hash = Util.generateHash(password, salt);

        try {
            Patient patient = new Patient.PatientBuilder(username, salt, hash).build();
            patient.saveToDB();
            System.out.println("Created user " + username);
        } catch (SQLException e) {
            System.out.println("Create patient failed");
            e.printStackTrace();
        }
    }


    private static void createCaregiver(String[] tokens) {
        if (tokens.length != 3) {
            System.out.println("Failed to create user.");
            return;
        }
        String username = tokens[1];
        String password = tokens[2];

        // Validate password strength
        if (!isValidPassword(password)) {
            System.out.println("Password does not meet the criteria. Please try again.");
            return;
        }

        if (usernameExistsCaregiver(username)) {
            System.out.println("Username taken, try again!");
            return;
        }

        byte[] salt = Util.generateSalt();
        byte[] hash = Util.generateHash(password, salt);

        try {
            Caregiver caregiver = new Caregiver.CaregiverBuilder(username, salt, hash).build();
            caregiver.saveToDB();
            System.out.println("Created user " + username);
        } catch (SQLException e) {
            System.out.println("Failed to create user.");
            e.printStackTrace();
        }
    }


    private static boolean usernameExistsCaregiver(String username) {
        ConnectionManager cm = new ConnectionManager();
        Connection con = cm.createConnection();

        String selectUsername = "SELECT * FROM Caregivers WHERE Username = ?";
        try {
            PreparedStatement statement = con.prepareStatement(selectUsername);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            // returns false if the cursor is not before the first record or if there are no rows in the ResultSet.
            return resultSet.isBeforeFirst();
        } catch (SQLException e) {
            System.out.println("Error occurred when checking username");
            e.printStackTrace();
        } finally {
            cm.closeConnection();
        }
        return true;
    }

    private static void loginPatient(String[] tokens) {
        if (currentCaregiver != null || currentPatient != null) {
            System.out.println("User already logged in, try again");
            return;
        }
        if (tokens.length != 3) {
            System.out.println("Login patient failed");
            return;
        }
        String username = tokens[1];
        String password = tokens[2];

        try {
            Patient patient = new Patient.PatientGetter(username, password).get();
            if (patient == null) {
                System.out.println("Login patient failed");
            } else {
                System.out.println("Logged in as " + username);
                currentPatient = patient;
            }
        } catch (SQLException e) {
            System.out.println("Login patient failed");
            e.printStackTrace();
        }
    }

    private static void loginCaregiver(String[] tokens) {
        // login_caregiver <username> <password>
        // check 1: if someone's already logged-in, they need to log out first
        if (currentCaregiver != null || currentPatient != null) {
            System.out.println("User already logged in.");
            return;
        }
        // check 2: the length for tokens need to be exactly 3 to include all information (with the operation name)
        if (tokens.length != 3) {
            System.out.println("Login failed.");
            return;
        }
        String username = tokens[1];
        String password = tokens[2];

        Caregiver caregiver = null;
        try {
            caregiver = new Caregiver.CaregiverGetter(username, password).get();
        } catch (SQLException e) {
            System.out.println("Login failed.");
            e.printStackTrace();
        }
        // check if the login was successful
        if (caregiver == null) {
            System.out.println("Login failed.");
        } else {
            System.out.println("Logged in as: " + username);
            currentCaregiver = caregiver;
        }
    }

    private static void searchCaregiverSchedule(String[] tokens) {
        if (currentCaregiver == null && currentPatient == null) {
            System.out.println("Please login first");
            return;
        }

        if (tokens.length != 2) {
            System.out.println("Please try again");
            return;
        }

        String date = tokens[1];
        ConnectionManager cm = new ConnectionManager();
        Connection con = cm.createConnection();

        try {
            // Fetch available caregivers for the date
            String getCaregiversQuery = "SELECT Username FROM Availabilities WHERE AvailableDate = ? ORDER BY Username";
            PreparedStatement caregiverStatement = con.prepareStatement(getCaregiversQuery);
            caregiverStatement.setDate(1, Date.valueOf(date));
            ResultSet caregivers = caregiverStatement.executeQuery();

            // Output the usernames of available caregivers
            while (caregivers.next()) {
                System.out.println(caregivers.getString("Username"));
            }

            // Fetch available vaccine doses
            String getVaccinesQuery = "SELECT Name, Doses FROM Vaccines WHERE Doses > 0";
            PreparedStatement vaccineStatement = con.prepareStatement(getVaccinesQuery);
            ResultSet vaccines = vaccineStatement.executeQuery();

            // Output the vaccines and doses
            while (vaccines.next()) {
                System.out.println(vaccines.getString("Name") + " " + vaccines.getInt("Doses"));
            }
        } catch (SQLException e) {
            System.out.println("Please try again");
        } finally {
            cm.closeConnection();
        }
    }



    private static void reserve(String[] tokens) {
        if (currentPatient == null) {
            System.out.println("Please login as a patient");
            return;
        }

        if (tokens.length != 3) {
            System.out.println("Please try again");
            return;
        }

        String date = tokens[1];
        String vaccine = tokens[2];
        ConnectionManager cm = new ConnectionManager();
        Connection con = cm.createConnection();

        try {
            // Check for available caregiver
            String getCaregiverQuery = "SELECT Username FROM Availabilities WHERE AvailableDate = ? ORDER BY Username";
            PreparedStatement caregiverStatement = con.prepareStatement(getCaregiverQuery);
            caregiverStatement.setDate(1, Date.valueOf(date));
            ResultSet caregiverResult = caregiverStatement.executeQuery();

            if (!caregiverResult.next()) {
                System.out.println("No caregiver is available");
                return;
            }
            String caregiverUsername = caregiverResult.getString("Username");

            // Check for vaccine doses availability
            String getVaccineQuery = "SELECT Doses FROM Vaccines WHERE Name = ?";
            PreparedStatement vaccineStatement = con.prepareStatement(getVaccineQuery);
            vaccineStatement.setString(1, vaccine);
            ResultSet vaccineResult = vaccineStatement.executeQuery();

            if (!vaccineResult.next() || vaccineResult.getInt("Doses") <= 0) {
                System.out.println("Not enough available doses");
                return;
            }

            // Reduce vaccine doses
            int remainingDoses = vaccineResult.getInt("Doses") - 1;
            String updateVaccineQuery = "UPDATE Vaccines SET Doses = ? WHERE Name = ?";
            PreparedStatement updateVaccineStatement = con.prepareStatement(updateVaccineQuery);
            updateVaccineStatement.setInt(1, remainingDoses);
            updateVaccineStatement.setString(2, vaccine);
            updateVaccineStatement.executeUpdate();

            // Insert reservation
            String insertReservationQuery = "INSERT INTO Reservations (PatientUsername, CaregiverUsername, VaccineName, ReservationDate) VALUES (?, ?, ?, ?)";
            PreparedStatement reservationStatement = con.prepareStatement(insertReservationQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            reservationStatement.setString(1, currentPatient.getUsername());
            reservationStatement.setString(2, caregiverUsername);
            reservationStatement.setString(3, vaccine);
            reservationStatement.setDate(4, Date.valueOf(date));
            reservationStatement.executeUpdate();

            ResultSet generatedKeys = reservationStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int reservationID = generatedKeys.getInt(1);
                System.out.println("Appointment ID " + reservationID + ", Caregiver username " + caregiverUsername);
            }

            // Remove caregiver availability for that date
            String removeAvailabilityQuery = "DELETE FROM Availabilities WHERE Username = ? AND AvailableDate = ?";
            PreparedStatement removeAvailabilityStatement = con.prepareStatement(removeAvailabilityQuery);
            removeAvailabilityStatement.setString(1, caregiverUsername);
            removeAvailabilityStatement.setDate(2, Date.valueOf(date));
            removeAvailabilityStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Please try again");
        } finally {
            cm.closeConnection();
        }
    }



    private static void uploadAvailability(String[] tokens) {
        // upload_availability <date>
        // check 1: check if the current logged-in user is a caregiver
        if (currentCaregiver == null) {
            System.out.println("Please login as a caregiver first!");
            return;
        }
        // check 2: the length for tokens need to be exactly 2 to include all information (with the operation name)
        if (tokens.length != 2) {
            System.out.println("Please try again!");
            return;
        }
        String date = tokens[1];
        try {
            Date d = Date.valueOf(date);
            currentCaregiver.uploadAvailability(d);
            System.out.println("Availability uploaded!");
        } catch (IllegalArgumentException e) {
            System.out.println("Please enter a valid date!");
        } catch (SQLException e) {
            System.out.println("Error occurred when uploading availability");
            e.printStackTrace();
        }
    }

    private static void cancel(String[] tokens) {
        if (tokens.length != 2) {
            System.out.println("Please provide the appointment ID to cancel.");
            return;
        }

        if (currentCaregiver == null && currentPatient == null) {
            System.out.println("Please login first");
            return;
        }

        int appointmentId;
        try {
            appointmentId = Integer.parseInt(tokens[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid appointment ID format");
            return;
        }

        ConnectionManager cm = new ConnectionManager();
        Connection con = cm.createConnection();

        try {
            // Check if the appointment exists and belongs to the logged-in user
            String checkAppointmentQuery = "SELECT ReservationID, PatientUsername, CaregiverUsername FROM Reservations WHERE ReservationID = ?";
            PreparedStatement checkStatement = con.prepareStatement(checkAppointmentQuery);
            checkStatement.setInt(1, appointmentId);
            ResultSet resultSet = checkStatement.executeQuery();

            if (!resultSet.next()) {
                System.out.println("Invalid appointment ID.");
                return;
            }

            String patientUsername = resultSet.getString("PatientUsername");
            String caregiverUsername = resultSet.getString("CaregiverUsername");

            // Check if the logged-in user has the right to cancel the appointment
            if ((currentPatient != null && !currentPatient.getUsername().equals(patientUsername)) ||
                    (currentCaregiver != null && !currentCaregiver.getUsername().equals(caregiverUsername))) {
                System.out.println("You do not have permission to cancel this appointment.");
                return;
            }

            // Delete the reservation from the Reservations table
            String deleteReservationQuery = "DELETE FROM Reservations WHERE ReservationID = ?";
            PreparedStatement deleteStatement = con.prepareStatement(deleteReservationQuery);
            deleteStatement.setInt(1, appointmentId);
            int rowsAffected = deleteStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Appointment canceled successfully.");
            } else {
                System.out.println("Failed to cancel appointment.");
            }

        } catch (SQLException e) {
            System.out.println("Error occurred while canceling appointment.");
            e.printStackTrace();
        } finally {
            cm.closeConnection();
        }
    }


    private static void addDoses(String[] tokens) {
        // add_doses <vaccine> <number>
        // check 1: check if the current logged-in user is a caregiver
        if (currentCaregiver == null) {
            System.out.println("Please login as a caregiver first!");
            return;
        }
        // check 2: the length for tokens need to be exactly 3 to include all information (with the operation name)
        if (tokens.length != 3) {
            System.out.println("Please try again!");
            return;
        }
        String vaccineName = tokens[1];
        int doses = Integer.parseInt(tokens[2]);
        Vaccine vaccine = null;
        try {
            vaccine = new Vaccine.VaccineGetter(vaccineName).get();
        } catch (SQLException e) {
            System.out.println("Error occurred when adding doses");
            e.printStackTrace();
        }
        // check 3: if getter returns null, it means that we need to create the vaccine and insert it into the Vaccines
        //          table
        if (vaccine == null) {
            try {
                vaccine = new Vaccine.VaccineBuilder(vaccineName, doses).build();
                vaccine.saveToDB();
            } catch (SQLException e) {
                System.out.println("Error occurred when adding doses");
                e.printStackTrace();
            }
        } else {
            // if the vaccine is not null, meaning that the vaccine already exists in our table
            try {
                vaccine.increaseAvailableDoses(doses);
            } catch (SQLException e) {
                System.out.println("Error occurred when adding doses");
                e.printStackTrace();
            }
        }
        System.out.println("Doses updated!");
    }

    private static void showAppointments(String[] tokens) {
        if (currentCaregiver == null && currentPatient == null) {
            System.out.println("Please login first");
            return;
        }

        ConnectionManager cm = new ConnectionManager();
        Connection con = cm.createConnection();

        try {
            String getAppointmentsQuery;
            if (currentPatient != null) {
                getAppointmentsQuery = "SELECT ReservationID, VaccineName, ReservationDate, CaregiverUsername FROM Reservations WHERE PatientUsername = ? ORDER BY ReservationID";
                PreparedStatement statement = con.prepareStatement(getAppointmentsQuery);
                statement.setString(1, currentPatient.getUsername());
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    System.out.println(resultSet.getInt("ReservationID") + " " +
                            resultSet.getString("VaccineName") + " " +
                            resultSet.getDate("ReservationDate") + " " +
                            resultSet.getString("CaregiverUsername"));
                }
            } else {
                getAppointmentsQuery = "SELECT ReservationID, VaccineName, ReservationDate, PatientUsername FROM Reservations WHERE CaregiverUsername = ? ORDER BY ReservationID";
                PreparedStatement statement = con.prepareStatement(getAppointmentsQuery);
                statement.setString(1, currentCaregiver.getUsername());
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    System.out.println(resultSet.getInt("ReservationID") + " " +
                            resultSet.getString("VaccineName") + " " +
                            resultSet.getDate("ReservationDate") + " " +
                            resultSet.getString("PatientUsername"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Please try again");
        } finally {
            cm.closeConnection();
        }
    }



    private static void logout(String[] tokens) {
        if (currentCaregiver == null && currentPatient == null) {
            System.out.println("Please login first");
            return;
        }

        currentCaregiver = null;
        currentPatient = null;
        System.out.println("Successfully logged out");
    }


    private static boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#?])[A-Za-z\\d!@#?]{8,}$";
        return password.matches(passwordPattern);
    }


}
