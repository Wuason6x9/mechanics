package dev.wuason.mechanics.data.mysql;

import dev.wuason.mechanics.utils.AdventureUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.*;
import java.util.ArrayList;
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

    // Rest of your methods...

}
