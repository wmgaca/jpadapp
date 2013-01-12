package lib;

import lib.types.PADState;
import lib.types.PADValue;
import lib.net.packages.PADPackage;

import java.sql.*;
import java.util.ArrayList;

import static lib.utils.Logging.log;

public class DataHandler {

    protected Connection connection;

    public DataHandler(String connectionString, String user, String password) throws SQLException {
        try {
            connection = DriverManager.getConnection(connectionString, user, password);
        } catch (SQLException error) {
            log("Cannot connect.");
            error.printStackTrace();
            throw error;
        }
    }

    public ArrayList<PADState> fetchAll(int resultSetId) {
        ArrayList<PADState> states = new ArrayList<PADState>();

        if (resultSetId == -1) {
            resultSetId = 2;
        }

        try {
            ResultSet results = connection.createStatement().executeQuery("SELECT p, a, d, cp, ca, cd, timestamp FROM pad_value WHERE result_set_id = " + resultSetId);

            while (results.next()) {
                states.add(new PADState(new PADValue(results.getFloat(1), results.getFloat(4), results.getLong(7)),
                                        new PADValue(results.getFloat(2), results.getFloat(5), results.getLong(7)),
                                        new PADValue(results.getFloat(3), results.getFloat(6), results.getLong(7)),
                                        results.getLong(7)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return states;
    }

    public boolean insertPadValue(PADPackage data, int resultSetId) {
        try {
            PreparedStatement stmt;
            stmt = connection.prepareStatement("INSERT INTO pad_value(p, a, d, cp, ca, cd, timestamp, result_set_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setFloat(1, data.getState().getP().getValue());
            stmt.setFloat(2, data.getState().getA().getValue());
            stmt.setFloat(3, data.getState().getD().getValue());
            stmt.setFloat(4, data.getState().getP().getCertainty());
            stmt.setFloat(5, data.getState().getA().getCertainty());
            stmt.setFloat(6, data.getState().getD().getCertainty());
            stmt.setLong(7, data.getState().getTimestamp());
            stmt.setInt(8, resultSetId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log("Cannot insert PadValue record");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private int getLastInsertedId() {
        ResultSet results = null;

        try {
            results = connection.createStatement().executeQuery("SELECT last_insert_id()");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (results != null) {
            try {
                if (results.next()) {
                    return results.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1;
    }

    public int createSession(int experimentId, int methodId) {
        try {
            PreparedStatement stmt;
            stmt = connection.prepareStatement("INSERT INTO experiment_session(method_id, experiment_id) VALUES(?, ?)");
            stmt.setInt(1, methodId);
            stmt.setInt(2, experimentId);
            stmt.executeUpdate();
            return getLastInsertedId();
        }  catch (SQLException e) {
            log("Cannot insert PadValue record");
            e.printStackTrace();
        }

        return -1;
    }

    public int createResultSet(int sessionId) {
        try {
            PreparedStatement stmt;
            stmt = connection.prepareStatement("INSERT INTO result_set(session_id) VALUES(?)");
            stmt.setInt(1, sessionId);
            stmt.executeUpdate();
            return getLastInsertedId();
        }  catch (SQLException e) {
            log("Cannot insert ResultSet record");
            e.printStackTrace();
        }

        return -1;
    }
}