package dev.wuason.mechanics.data.mysql;

import dev.wuason.mechanics.data.Data;
import dev.wuason.mechanics.utils.AdventureUtils;
import dev.wuason.mechanics.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SqlManager {

    private Connection connection;
    private static ArrayList<SqlManager> dataManagers = new ArrayList<>();

    private Plugin plugin;
    private String host;
    private int port;
    private String database;
    private String user;
    private String password;
    private String driver;
    final private String DATA_NAME_COLUMN = "data";
    final private String DATA_ID_NAME_COLUMN = "data_id";

    public SqlManager(Plugin plugin, String host, int port, String database, String user, String password, String driver) {
        this.plugin = plugin;
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
        this.driver = driver;
        connectToMySQL();
        dataManagers.add(this);
        if (!isDatabaseConnected()) {
            handleDisconnection();
        }
    }

    private void connectToMySQL() {
        String url = "jdbc:" + driver + "://" + host + ":" + port + "/" + database + "?createDatabaseIfNotExist=true";

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            AdventureUtils.sendMessagePluginConsole(plugin, "<red> Cannot establish a connection to the database. Plugin is being disabled.");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    public boolean isDatabaseConnected() {
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void reconnectToDatabase() {
        try {
            if (connection.isClosed()) {
                connectToMySQL();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AdventureUtils.sendMessagePluginConsole(plugin, "<red> Cannot reconnect to the database. Plugin is being disabled.");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    public void handleDisconnection() {
        new Thread(() -> {
            while (!isDatabaseConnected()) {
                AdventureUtils.sendMessagePluginConsole(plugin, "<red> Attempting to reconnect to the database...");
                reconnectToDatabase();
                try {
                    Thread.sleep(10000); // Retry every 10 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void createCustomTable(String tableName, List<Column> columns) {
        StringBuilder createTableSQL = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            createTableSQL.append(column.getName()).append(" ").append(column.getType());
            if (i < columns.size() - 1) {
                createTableSQL.append(", ");
            }
        }
        createTableSQL.append(");");

        Connection connection = null;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertData(String tableName, String columnName, String value) {
        String insertSQL = "INSERT INTO " + tableName + " (" + columnName + ") VALUES (?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, value);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateData(String tableName, String columnName, String value, String conditionColumn, String conditionValue) {
        String updateSQL = "UPDATE " + tableName + " SET " + columnName + " = ? WHERE " + conditionColumn + " = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, value);
            pstmt.setString(2, conditionValue);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteData(String tableName, String conditionColumn, String value) {
        String deleteSQL = "DELETE FROM " + tableName + " WHERE " + conditionColumn + " = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setString(1, value);
            pstmt.executeUpdate();
        } catch (SQLException e) {
        }
    }

    public String getData(String tableName, String columnName, String conditionColumn, String conditionValue) {
        String selectSQL = "SELECT " + columnName + " FROM " + tableName + " WHERE " + conditionColumn + " = ?";
        String result = null;

        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setString(1, conditionValue);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString(columnName);
            }
        } catch (SQLException e) {
        }

        return result;
    }

    public List<String> getAllData(String tableName, String columnName) {
        String selectSQL = "SELECT " + columnName + " FROM " + tableName;
        List<String> results = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(selectSQL);
            while (rs.next()) {
                results.add(rs.getString(columnName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    public List<String> searchData(String tableName, String columnName, List<Condition> conditions) {
        StringBuilder selectSQL = new StringBuilder("SELECT " + columnName + " FROM " + tableName + " WHERE ");
        for (int i = 0; i < conditions.size(); i++) {
            Condition condition = conditions.get(i);
            selectSQL.append(condition.getColumn()).append(" ").append(condition.getOperator()).append(" ?");
            if (i < conditions.size() - 1) {
                selectSQL.append(" AND ");
            }
        }
        List<String> results = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL.toString())) {
            for (int i = 0; i < conditions.size(); i++) {
                pstmt.setString(i + 1, conditions.get(i).getValue());
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                results.add(rs.getString(columnName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    public void dropTable(String tableName) {
        try (Connection connection = getConnection()) {
            // Preparamos la sentencia SQL
            String sql = "DROP TABLE IF EXISTS " + tableName;

            // Ejecutamos la sentencia SQL
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertDataMultiColumn(String tableName, List<Column> columns, List<String> values) {
        if(columns.size() != values.size()) {
            // Handle error: the number of columns and values should be the same
            return;
        }

        StringBuilder insertSQL = new StringBuilder("INSERT INTO " + tableName + " (");

        for(int i = 0; i < columns.size(); i++) {
            insertSQL.append(columns.get(i).getName());
            if(i < columns.size() - 1) {
                insertSQL.append(", ");
            } else {
                insertSQL.append(") VALUES (");
            }
        }

        for(int i = 0; i < values.size(); i++) {
            insertSQL.append("?");
            if(i < values.size() - 1) {
                insertSQL.append(", ");
            } else {
                insertSQL.append(")");
            }
        }

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL.toString())) {
            for(int i = 0; i < values.size(); i++) {
                pstmt.setString(i + 1, values.get(i));
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDataMultiCondition(String tableName, List<Column> columns, List<String> newValues, List<Condition> conditions) {
        if(columns.size() != newValues.size()) {
            // Handle error: the number of columns and new values should be the same
            return;
        }

        StringBuilder updateSQL = new StringBuilder("UPDATE " + tableName + " SET ");

        for(int i = 0; i < columns.size(); i++) {
            updateSQL.append(columns.get(i).getName()).append(" = ?");
            if(i < columns.size() - 1) {
                updateSQL.append(", ");
            }
        }

        updateSQL.append(" WHERE ");

        for(int i = 0; i < conditions.size(); i++) {
            updateSQL.append(conditions.get(i).getColumn()).append(" ").append(conditions.get(i).getOperator()).append(" ?");
            if(i < conditions.size() - 1) {
                updateSQL.append(" AND ");
            }
        }

        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL.toString())) {
            int index = 1;
            for(String value : newValues) {
                pstmt.setString(index++, value);
            }
            for(Condition condition : conditions) {
                pstmt.setString(index++, condition.getValue());
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDataMultiCondition(String tableName, List<Condition> conditions) {
        StringBuilder deleteSQL = new StringBuilder("DELETE FROM " + tableName + " WHERE ");

        for(int i = 0; i < conditions.size(); i++) {
            deleteSQL.append(conditions.get(i).getColumn()).append(" ").append(conditions.get(i).getOperator()).append(" ?");
            if(i < conditions.size() - 1) {
                deleteSQL.append(" AND ");
            }
        }

        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL.toString())) {
            for(int i = 0; i < conditions.size(); i++) {
                pstmt.setString(i + 1, conditions.get(i).getValue());
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean dataExists(String tableName, List<Condition> conditions) {
        StringBuilder selectSQL = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");

        for (int i = 0; i < conditions.size(); i++) {
            selectSQL.append(conditions.get(i).getColumn()).append(" ").append(conditions.get(i).getOperator()).append(" ?");
            if (i < conditions.size() - 1) {
                selectSQL.append(" AND ");
            }
        }

        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL.toString())) {
            for (int i = 0; i < conditions.size(); i++) {
                pstmt.setString(i + 1, conditions.get(i).getValue());
            }
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Si hay al menos una fila, el dato existe
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }



    public static ArrayList<SqlManager> getDataManagers() {
        return dataManagers;
    }

    public static void setDataManagers(ArrayList<SqlManager> dataManagers) {
        SqlManager.dataManagers = dataManagers;
    }

    public Connection getConnection() {
        return connection;
    }

    public static ArrayList<SqlManager> getSqlManagers(){
        return dataManagers;
    }

    public Data getData(String dataType, String dataID){
        try {
            return (Data) Utils.deserializeObjectBukkit(getData(dataType,DATA_NAME_COLUMN,DATA_ID_NAME_COLUMN,dataID));
        } catch (IOException | ClassNotFoundException e) {
        }
        return null;
    }
    public String getDataStr(String dataType, String dataID){
        return getData(dataType,DATA_NAME_COLUMN,DATA_ID_NAME_COLUMN,dataID);
    }

    public boolean existData(String dataType, String dataID){
        return dataExists(dataType, Collections.singletonList(new Condition(DATA_ID_NAME_COLUMN,"=",dataID)));
    }
    public void removeDataStr(String dataType, String dataID){
        deleteData(dataType,DATA_ID_NAME_COLUMN,dataID);
    }
    public void removeData(Data data){
        deleteData(data.getDataType(),DATA_ID_NAME_COLUMN,data.getId());
    }
    public void saveDataStr(String dataType, String dataID, String data){
        if(existData(dataType,dataID)){
            updateData(dataType,DATA_NAME_COLUMN,data,DATA_ID_NAME_COLUMN,dataID);
            return;
        }
        List<Column> columns = new ArrayList<>();
        columns.add(new Column(DATA_ID_NAME_COLUMN,"VARCHAR(255)"));
        columns.add(new Column(DATA_NAME_COLUMN,"TEXT"));
        List<String> values = new ArrayList<>();
        values.add(dataID);
        values.add(data);
        insertDataMultiColumn(dataType,columns,values);
    }
    public void saveData(Data data){
        String dataStr = null;
        try {
            dataStr = Utils.serializeObjectBukkit(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        saveDataStr(data.getDataType(),data.getId(),dataStr);
    }

}
