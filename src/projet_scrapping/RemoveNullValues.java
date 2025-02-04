package projet_scrapping;

import java.sql.*;

public class RemoveNullValues {

    public static void main(String[] args) {
        String sourceUrl = "jdbc:mysql://localhost:3306/testjava";
        String destinationUrl = "jdbc:mysql://localhost:3306/java2";
        String user = "root";
        String password = "";

        try {
            Connection sourceConnection = DriverManager.getConnection(sourceUrl, user, password);
            Connection destinationConnection = DriverManager.getConnection(destinationUrl, user, password);

            removeNullValues(sourceConnection, destinationConnection, "emploi", "cleanemploi");

            sourceConnection.close();
            destinationConnection.close();

            System.out.println("Null values removed and data transferred successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void removeNullValues(Connection sourceConnection, Connection destinationConnection, String sourceTable, String destinationTable) throws SQLException {
        Statement selectStatement = sourceConnection.createStatement();
        ResultSet resultSet = selectStatement.executeQuery("SELECT * FROM " + sourceTable);

        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();

        // Build the INSERT SQL statement for the destination database
        StringBuilder insertSql = new StringBuilder("INSERT INTO " + destinationTable + " VALUES (");
        for (int i = 0; i < columnCount; i++) {
            insertSql.append(i < columnCount - 1 ? "?," : "?)");
        }

        PreparedStatement insertStatement = destinationConnection.prepareStatement(insertSql.toString());

        while (resultSet.next()) {
            boolean hasNullValues = false;

            for (int i = 1; i <= columnCount; i++) {
                Object value = resultSet.getObject(i);

                // Check for null values
                if (value == null) {
                    hasNullValues = true;
                    break;
                }

                // Set non-null values to the insert statement
                insertStatement.setObject(i, value);
            }

            // Only insert the row if it doesn't contain null values
            if (!hasNullValues) {
                insertStatement.executeUpdate();
            }
        }

        resultSet.close();
        selectStatement.close();
        insertStatement.close();
    }
}
