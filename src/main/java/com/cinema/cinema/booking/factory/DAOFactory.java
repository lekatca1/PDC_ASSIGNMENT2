/**
 *
 * @author xps1597
 */
package com.cinema.cinema.booking.factory;

import com.cinema.cinema.booking.dao.UserDAO;
import com.cinema.cinema.booking.dao.UserDAOImpl;
import com.cinema.cinema.booking.dao.BookingDAO;
import com.cinema.cinema.booking.dao.BookingDAOImpl;

public class DAOFactory {
    private static UserDAO userDAO;
    private static BookingDAO bookingDAO;
    
    public static UserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = new UserDAOImpl();
        }
        return userDAO;
    }
    
    public static BookingDAO getBookingDAO() {
        if (bookingDAO == null) {
            bookingDAO = new BookingDAOImpl();
        }
        return bookingDAO;
    }
}