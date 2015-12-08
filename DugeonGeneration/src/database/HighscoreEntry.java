package database;

public class HighscoreEntry
{
	final String name;
	final int score;
	
	public HighscoreEntry(String name, int score)
	{
		this.name=name;
		this.score=score;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getScore()
	{
		return score;
	}
}
