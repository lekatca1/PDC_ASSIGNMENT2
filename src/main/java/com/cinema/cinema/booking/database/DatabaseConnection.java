package com.cinema.cinema.booking.database;

import java.sql.*;
import java.io.File;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private static final String DB_NAME = "CinemaDB";
    private static final String DB_URL = "jdbc:derby:" + DB_NAME + ";create=true";
    
    private DatabaseConnection() {
        try {
            // Check if database is locked and clean up if needed
            cleanupLockFiles();
            
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("✓ Database connection established successfully");
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Derby JDBC driver not found");
            throw new RuntimeException("Database driver error", e);
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            
            // If database is locked, try to recover
            if (e.getMessage().contains("Another instance") || 
                e.getMessage().contains("Failed to start database")) {
                System.err.println("Database appears to be locked. Attempting recovery...");
                cleanupLockFiles();
                try {
                    connection = DriverManager.getConnection(DB_URL);
                    System.out.println("✓ Database connection established after recovery");
                    initializeDatabase();
                } catch (SQLException e2) {
                    System.err.println("❌ Recovery failed: " + e2.getMessage());
                    throw new RuntimeException("Database connection error", e2);
                }
            } else {
                throw new RuntimeException("Database connection error", e);
            }
        }
    }
    
    private void cleanupLockFiles() {
        try {
            File dbDir = new File(DB_NAME);
            if (dbDir.exists() && dbDir.isDirectory()) {
                File lockFile = new File(dbDir, "db.lck");
                File logFile = new File(dbDir, "dbex.lck");
                
                if (lockFile.exists()) {
                    boolean deleted = lockFile.delete();
                    if (deleted) {
                        System.out.println("✓ Removed stale lock file");
                    }
                }
                
                if (logFile.exists()) {
                    boolean deleted = logFile.delete();
                    if (deleted) {
                        System.out.println("✓ Removed stale log lock file");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not clean up lock files: " + e.getMessage());
        }
    }
    
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get database connection", e);
        }
        return connection;
    }
    
    private void initializeDatabase() {
        try {
            Statement stmt = connection.createStatement();
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet rs = meta.getTables(null, null, "USERS", null);
            
            if (rs.next()) {
                System.out.println("✓ Database schema already exists");
                return;
            }
            
            System.out.println("Creating database schema...");
            
            // Create USERS table
            stmt.execute(
                "CREATE TABLE USERS (" +
                "user_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY, " +
                "username VARCHAR(50) NOT NULL UNIQUE, " +
                "password VARCHAR(255) NOT NULL, " +
                "email VARCHAR(100) NOT NULL, " +
                "first_name VARCHAR(50) NOT NULL, " +
                "last_name VARCHAR(50) NOT NULL, " +
                "user_type VARCHAR(20) NOT NULL, " +
                "phone VARCHAR(20), " +
                "role VARCHAR(50), " +
                "department VARCHAR(50), " +
                "created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );
            
            // Create MOVIES table
            stmt.execute(
                "CREATE TABLE MOVIES (" +
                "movie_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY, " +
                "title VARCHAR(200) NOT NULL, " +
                "description VARCHAR(1000), " +
                "genre VARCHAR(50), " +
                "duration_minutes INTEGER NOT NULL, " +
                "rating VARCHAR(10), " +
                "release_date DATE, " +
                "director VARCHAR(100), " +
                "movie_cast VARCHAR(500), " +
                "poster_url VARCHAR(255), " +
                "is_active BOOLEAN DEFAULT TRUE" +
                ")"
            );
            
            // Create SCREENS table - FIXED: renamed 'rows' and 'columns' to avoid reserved keywords
            stmt.execute(
                "CREATE TABLE SCREENS (" +
                "screen_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY, " +
                "screen_number INTEGER NOT NULL UNIQUE, " +
                "screen_name VARCHAR(100) NOT NULL, " +
                "total_seats INTEGER NOT NULL, " +
                "num_rows INTEGER NOT NULL, " +
                "num_columns INTEGER NOT NULL, " +
                "is_active BOOLEAN DEFAULT TRUE" +
                ")"
            );
            
            // Create SEATS table
            stmt.execute(
                "CREATE TABLE SEATS (" +
                "seat_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY, " +
                "screen_id INTEGER NOT NULL, " +
                "row_number VARCHAR(5) NOT NULL, " +
                "seat_number INTEGER NOT NULL, " +
                "seat_type VARCHAR(20) DEFAULT 'STANDARD', " +
                "is_available BOOLEAN DEFAULT TRUE, " +
                "FOREIGN KEY (screen_id) REFERENCES SCREENS(screen_id), " +
                "UNIQUE (screen_id, row_number, seat_number)" +
                ")"
            );
            
            // Create SHOWTIMES table
            stmt.execute(
                "CREATE TABLE SHOWTIMES (" +
                "showtime_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY, " +
                "movie_id INTEGER NOT NULL, " +
                "screen_id INTEGER NOT NULL, " +
                "show_date DATE NOT NULL, " +
                "show_time TIME NOT NULL, " +
                "price DECIMAL(10,2) NOT NULL, " +
                "available_seats INTEGER NOT NULL, " +
                "is_active BOOLEAN DEFAULT TRUE, " +
                "FOREIGN KEY (movie_id) REFERENCES MOVIES(movie_id), " +
                "FOREIGN KEY (screen_id) REFERENCES SCREENS(screen_id)" +
                ")"
            );
            
            // Create BOOKINGS table
            stmt.execute(
                "CREATE TABLE BOOKINGS (" +
                "booking_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY, " +
                "customer_id INTEGER NOT NULL, " +
                "showtime_id INTEGER NOT NULL, " +
                "total_price DECIMAL(10,2) NOT NULL, " +
                "booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "status VARCHAR(20) DEFAULT 'PENDING', " +
                "payment_method VARCHAR(50), " +
                "transaction_id VARCHAR(100), " +
                "FOREIGN KEY (customer_id) REFERENCES USERS(user_id), " +
                "FOREIGN KEY (showtime_id) REFERENCES SHOWTIMES(showtime_id)" +
                ")"
            );
            
            // Create BOOKING_SEATS table
            stmt.execute(
                "CREATE TABLE BOOKING_SEATS (" +
                "booking_seat_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY, " +
                "booking_id INTEGER NOT NULL, " +
                "seat_id INTEGER NOT NULL, " +
                "price DECIMAL(10,2) NOT NULL, " +
                "FOREIGN KEY (booking_id) REFERENCES BOOKINGS(booking_id), " +
                "FOREIGN KEY (seat_id) REFERENCES SEATS(seat_id)" +
                ")"
            );
            
            stmt.close();
            System.out.println("✓ Database schema created successfully");
            insertDefaultData();
            
        } catch (SQLException e) {
            System.err.println("❌ Failed to initialize database: " + e.getMessage());
            throw new RuntimeException("Database initialization error", e);
        }
    }
    
    private void insertDefaultData() {
        try {
            Statement stmt = connection.createStatement();
            
            // Insert default admin
            stmt.execute(
                "INSERT INTO USERS (username, password, email, first_name, last_name, user_type, role, department) " +
                "VALUES ('admin', 'admin123', 'admin@cinema.com', 'System', 'Administrator', 'ADMIN', 'SUPER_ADMIN', 'Management')"
            );
            
            // Insert default customer
            stmt.execute(
                "INSERT INTO USERS (username, password, email, first_name, last_name, user_type, phone) " +
                "VALUES ('rob', 'rob', 'rob@cinema.com', 'Rob', 'Customer', 'CUSTOMER', '1234567890')"
            );
            
            // Insert sample movies
            stmt.execute(
                "INSERT INTO MOVIES (title, description, genre, duration_minutes, rating, release_date, director, movie_cast) " +
                "VALUES ('The Bad Guys 2', 'The reformed Bad Guys are trying to do good.', " +
                "'Animation', 105, 'PG', '2025-08-01', 'Pierre Perifel', 'Sam Rockwell, Marc Maron')"
            );
            
            stmt.execute(
                "INSERT INTO MOVIES (title, description, genre, duration_minutes, rating, release_date, director, movie_cast) " +
                "VALUES ('Fantastic Four', 'Marvel superhero team faces a cosmic threat.', " +
                "'Action', 135, 'PG-13', '2025-07-25', 'Matt Shakman', 'Pedro Pascal, Vanessa Kirby')"
            );
            
            stmt.execute(
                "INSERT INTO MOVIES (title, description, genre, duration_minutes, rating, release_date, director, movie_cast) " +
                "VALUES ('Superman', 'The Man of Steel protects Earth.', " +
                "'Action', 140, 'PG-13', '2025-07-11', 'James Gunn', 'David Corenswet, Rachel Brosnahan')"
            );
            
            // Insert screens - FIXED: using num_rows and num_columns
            stmt.execute("INSERT INTO SCREENS (screen_number, screen_name, total_seats, num_rows, num_columns) VALUES (1, 'Screen 1', 50, 5, 10)");
            stmt.execute("INSERT INTO SCREENS (screen_number, screen_name, total_seats, num_rows, num_columns) VALUES (2, 'Screen 2', 60, 6, 10)");
            
            // Insert seats for Screen 1
            String[] rows = {"A", "B", "C", "D", "E"};
            for (String row : rows) {
                for (int seat = 1; seat <= 10; seat++) {
                    String seatType = (seat >= 5 && seat <= 6) ? "VIP" : "STANDARD";
                    stmt.execute(
                        "INSERT INTO SEATS (screen_id, row_number, seat_number, seat_type) " +
                        "VALUES (1, '" + row + "', " + seat + ", '" + seatType + "')"
                    );
                }
            }
            
            // Insert sample showtimes
            stmt.execute(
                "INSERT INTO SHOWTIMES (movie_id, screen_id, show_date, show_time, price, available_seats) " +
                "VALUES (1, 1, '2025-10-25', '14:00:00', 12.50, 50)"
            );
            
            stmt.execute(
                "INSERT INTO SHOWTIMES (movie_id, screen_id, show_date, show_time, price, available_seats) " +
                "VALUES (1, 1, '2025-10-25', '17:30:00', 15.00, 50)"
            );
            
            stmt.execute(
                "INSERT INTO SHOWTIMES (movie_id, screen_id, show_date, show_time, price, available_seats) " +
                "VALUES (2, 1, '2025-10-26', '19:00:00', 15.00, 50)"
            );
            
            stmt.close();
            System.out.println("✓ Default data inserted successfully");
            
        } catch (SQLException e) {
            System.err.println("❌ Failed to insert default data: " + e.getMessage());
        }
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error closing database connection: " + e.getMessage());
        }
    }
    
    public static void shutdown() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
            if (e.getSQLState().equals("XJ015")) {
                System.out.println("✓ Database shut down successfully");
            }
        }
    }
}