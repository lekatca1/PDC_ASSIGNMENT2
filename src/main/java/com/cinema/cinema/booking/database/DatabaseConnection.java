package com.cinema.cinema.booking.database;

import java.sql.*;
import java.io.File;

/**
 * DatabaseConnection - Singleton class for managing database connection
 * Handles Derby embedded database initialization and connection management
 * @author xps1597
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private static final String DB_NAME = "CinemaDB";
    private static final String DB_URL = "jdbc:derby:" + DB_NAME + ";create=true";
    
    /**
     * Private constructor - Singleton pattern
     * Initializes database connection and creates schema if needed
     */
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
    
    /**
     * Clean up stale lock files from previous database sessions
     * Helps recover from improper shutdowns
     */
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
    
    /**
     * Get singleton instance of DatabaseConnection
     * @return DatabaseConnection instance
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Get active database connection
     * Reopens connection if it was closed
     * @return Active Connection object
     */
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
    
    /**
     * Initialize database schema
     * Creates all tables if they don't exist
     * Only runs once when database is first created
     */
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
            System.out.println("✓ USERS table created");
            
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
            System.out.println("✓ MOVIES table created");
            
            // Create SCREENS table
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
            System.out.println("✓ SCREENS table created");
            
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
            System.out.println("✓ SEATS table created");
            
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
            System.out.println("✓ SHOWTIMES table created");
            
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
            System.out.println("✓ BOOKINGS table created");
            
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
            System.out.println("✓ BOOKING_SEATS table created");
            
            stmt.close();
            System.out.println("✓ Database schema created successfully");
            insertDefaultData();
            
        } catch (SQLException e) {
            System.err.println("❌ Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Database initialization error", e);
        }
    }
    
    /**
     * Insert default data into database
     * Creates default users, movies, screens, seats, and showtimes
     * CRITICAL: Creates 7 showtimes to match GUI expectations (showtime IDs 1-7)
     */
    private void insertDefaultData() {
        try {
            Statement stmt = connection.createStatement();
            
            System.out.println("Inserting default data...");
            
            // Insert default admin (user_id will be 1)
            stmt.execute(
                "INSERT INTO USERS (username, password, email, first_name, last_name, user_type, role, department) " +
                "VALUES ('admin', 'admin123', 'admin@cinema.com', 'System', 'Administrator', 'ADMIN', 'SUPER_ADMIN', 'Management')"
            );
            System.out.println("✓ Default admin created");
            
            // Insert default customer (user_id will be 2)
            stmt.execute(
                "INSERT INTO USERS (username, password, email, first_name, last_name, user_type, phone) " +
                "VALUES ('rob', 'rob', 'rob@cinema.com', 'Rob', 'Customer', 'CUSTOMER', '1234567890')"
            );
            System.out.println("✓ Default customer 'rob' created");
            
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
            System.out.println("✓ Sample movies created");
            
            // Insert screens
            stmt.execute("INSERT INTO SCREENS (screen_number, screen_name, total_seats, num_rows, num_columns) VALUES (1, 'Screen 1', 50, 5, 10)");
            stmt.execute("INSERT INTO SCREENS (screen_number, screen_name, total_seats, num_rows, num_columns) VALUES (2, 'Screen 2', 60, 6, 10)");
            System.out.println("✓ Screens created");
            
// Insert seats for Screen 1 - EXPANDED to match GUI
// GUI shows: A(12), B(14), C(16), D(16), E(18), F(18), G(18), H(18), J(20), K(20)
        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H", "J", "K"};
        int[] seatsPerRow = {12, 14, 16, 16, 18, 18, 18, 18, 20, 20};

         for (int rowIndex = 0; rowIndex < rows.length; rowIndex++) {
          String row = rows[rowIndex];
    int numSeats = seatsPerRow[rowIndex];
    
    for (int seat = 1; seat <= numSeats; seat++) {
        // VIP seats are positions 5-6 in each row
        String seatType = (seat >= 5 && seat <= 6) ? "VIP" : "STANDARD";
        stmt.execute(
            "INSERT INTO SEATS (screen_id, row_number, seat_number, seat_type) " +
            "VALUES (1, '" + row + "', " + seat + ", '" + seatType + "')"
        );
    }
}
System.out.println("✓ Seats created for Screen 1 (170 total seats)");
            
            // CRITICAL: Insert 7 showtimes to match GUI expectations (showtime IDs 1-7)
            // The GUI uses hardcoded showtime IDs from 1-7
            System.out.println("Creating 7 showtimes for GUI compatibility...");
            
            // Showtime 1: 10:00 AM - Morning show
            stmt.execute(
                "INSERT INTO SHOWTIMES (movie_id, screen_id, show_date, show_time, price, available_seats) " +
                "VALUES (1, 1, '2025-10-29', '10:00:00', 25.00, 50)"
            );
            
            // Showtime 2: 12:30 PM - Lunch time
            stmt.execute(
                "INSERT INTO SHOWTIMES (movie_id, screen_id, show_date, show_time, price, available_seats) " +
                "VALUES (1, 1, '2025-10-29', '12:30:00', 28.00, 50)"
            );
            
            // Showtime 3: 3:00 PM - Afternoon matinee
            stmt.execute(
                "INSERT INTO SHOWTIMES (movie_id, screen_id, show_date, show_time, price, available_seats) " +
                "VALUES (1, 1, '2025-10-29', '15:00:00', 19.50, 50)"
            );
            
            // Showtime 4: 5:30 PM - Early evening
            stmt.execute(
                "INSERT INTO SHOWTIMES (movie_id, screen_id, show_date, show_time, price, available_seats) " +
                "VALUES (1, 1, '2025-10-29', '17:30:00', 22.00, 50)"
            );
            
            // Showtime 5: 8:00 PM - Prime time
            stmt.execute(
                "INSERT INTO SHOWTIMES (movie_id, screen_id, show_date, show_time, price, available_seats) " +
                "VALUES (1, 1, '2025-10-29', '20:00:00', 25.00, 50)"
            );
            
            // Showtime 6: 10:30 PM - Late night
            stmt.execute(
                "INSERT INTO SHOWTIMES (movie_id, screen_id, show_date, show_time, price, available_seats) " +
                "VALUES (1, 1, '2025-10-29', '22:30:00', 19.50, 50)"
            );
            
            // Showtime 7: 11:59 PM - Midnight show
            stmt.execute(
                "INSERT INTO SHOWTIMES (movie_id, screen_id, show_date, show_time, price, available_seats) " +
                "VALUES (1, 1, '2025-10-29', '23:59:00', 16.50, 50)"
            );
            
            System.out.println("✓ All 7 showtimes created (IDs 1-7)");
            
            stmt.close();
            System.out.println("✓ Default data inserted successfully");
            
        } catch (SQLException e) {
            System.err.println("❌ Failed to insert default data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Close database connection
     * Should be called when application shuts down
     */
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
    
    /**
     * Shutdown Derby database
     * Should be called when application exits
     */
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