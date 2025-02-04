package projet_scrapping;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.*;

public class CleaningData {



    public static void main(String[] args) {
        String sourceUrl = "jdbc:mysql://localhost:3306/testjava";
        String destinationUrl = "jdbc:mysql://localhost:3306/clean";
        String user = "root";
        String password = "";

        try {
            Connection sourceConnection = DriverManager.getConnection(sourceUrl, user, password);
            Connection destinationConnection = DriverManager.getConnection(destinationUrl, user, password);

            cleanAndCopyTable(sourceConnection, destinationConnection, "entreprise");

            sourceConnection.close();
            destinationConnection.close();

            System.out.println("Data cleaning completed successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void cleanAndCopyTable(Connection sourceConnection, Connection destinationConnection, String emploi) throws SQLException {
        Statement selectStatement = sourceConnection.createStatement();
        ResultSet resultSet = selectStatement.executeQuery("SELECT * FROM emploi");

        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();

        // Build the INSERT SQL statement for the destination database
        StringBuilder insertSql = new StringBuilder("INSERT INTO cleanemp VALUES (");
        for (int i = 0; i < columnCount; i++) {
            insertSql.append(i < columnCount - 1 ? "?," : "?)");
        }

        PreparedStatement insertStatement = destinationConnection.prepareStatement(insertSql.toString());

        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                Object value = resultSet.getObject(i);

                // Handle missing values by replacing them with NULL
                if (value == null) {
                    insertStatement.setNull(i, resultSetMetaData.getColumnType(i));
                } else {
                    insertStatement.setObject(i, value);
                }
            }
            insertStatement.executeUpdate();
        }

        resultSet.close();
        selectStatement.close();
        insertStatement.close();
    }
}
