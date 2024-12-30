package me.psikuvit.betterQuests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL {

    private final BQPlugin plugin;
    private Connection connection;

    public MySQL(BQPlugin plugin) {
        this.plugin = plugin;
        connect();
        createLevelsTable();
    }

    public void createLevelsTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS player_levels ("
                + "player_uuid VARCHAR(36) PRIMARY KEY, "
                + "level INT DEFAULT 1, "
                + "total_exp BIGINT DEFAULT 0, "
                + "current_exp BIGINT DEFAULT 0 "
                + ")";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            plugin.getLogger().info("MySQL driver loaded");
            String URL = "jdbc:mysql://" + plugin.getConfigUtils().getHost() + ":" + plugin.getConfigUtils().getPort() + "/" + plugin.getConfigUtils().getDatabase();
            connection = DriverManager.getConnection(URL, plugin.getConfigUtils().getUsername(), plugin.getConfigUtils().getPassword());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPlayerLevel(String playerUUID) {
        String query = "SELECT level FROM player_levels WHERE player_uuid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, playerUUID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return resultSet.getInt("level");

            } catch (SQLException e) {
                e.printStackTrace();
            }

        return -1; // Return -1 if the player is not found or an error occurs
    }
}
