package com.cinema.cinema.booking.factory;

import com.cinema.cinema.booking.dao.*;

public class DAOFactory {
    
    public static UserDAO getUserDAO() {
        return new UserDAOImpl();
    }
}