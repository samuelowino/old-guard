package org.oldguard.usecases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseAccessExample {

    //<> 'name' ... sql injection attack
    public User getUser(String name) throws SQLException  {
        String query = "SELECT * FROM user where name = " + name + ";";

        Connection connection = null; // mysql db connection

        PreparedStatement preparedStatement = connection.
                prepareStatement("SELECT * FROM user WHERE name = ?");
        preparedStatement.setString(0, name);

        return null;
    }

    class User {
        private String name;
    }
}
