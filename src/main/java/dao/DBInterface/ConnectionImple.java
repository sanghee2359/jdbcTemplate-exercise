package dao.DBInterface;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionImple {
    public Connection makeConnection() throws ClassNotFoundException, SQLException;
}
