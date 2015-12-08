package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import world.DungeonMap;

public class DatabaseHandler
{
	Connection con;
	DungeonMap dm;
	Statement stmt;

	public DatabaseHandler(DungeonMap dm)
	{
		this.dm=dm;

		try
		{
			Class.forName("org.sqlite.JDBC");
			con=DriverManager.getConnection("jdbc:sqlite:game.db");
			dm.log("Succesfully established database connection");
			stmt=con.createStatement();
			stmt.execute("CREATE TABLE IF NOT EXISTS Highscore (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(30), score INTEGER)");
			stmt.close();
		} 
		catch (ClassNotFoundException e)
		{
			dm.logError(e.getMessage());
		}
		catch 
		(SQLException e)
		{
			dm.logError(e.getMessage());
		}

	}
	
	public void addHighscore(String name, int highscore)
	{
		try
		{
			String sql="INSERT INTO Highscore (name, score) VALUES (?, ?)";
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setInt(2, highscore);
			pstmt.executeUpdate();
			pstmt.close();
		} 
		catch(SQLException e)
		{
			dm.logError(e.getMessage());
		}
	}
	
	public ArrayList<HighscoreEntry> getTopEntries(int number)
	{
		ArrayList<HighscoreEntry> entries=new ArrayList<HighscoreEntry>();
		try
		{
			stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("SELECT * FROM Highscore ORDER BY score DESC LIMIT "+number);
			while(rs.next())
			{
				entries.add(new HighscoreEntry(rs.getString(2), rs.getInt(3)));
			}
		} catch (SQLException e)
		{
			dm.logError(e.getMessage());
		}
		finally
		{
			try
			{
				stmt.close();
			} catch (SQLException e)
			{
				dm.logError(e.getMessage());
			}
		}
		
		return entries;
	}
}
