package com.cognixia.jumplus.dao;

public class Movie {
	
	// private variables for movie
	private int id;
	
	private String title;
	
	private String genre;
	
	private int lengthMin;
	
	private double avgRating;

	public Movie(int id, String title, String genre, int lengthMin, double avgRating) {
		super();
		this.id = id;
		this.title = title;
		this.genre = genre;
		this.lengthMin = lengthMin;
		this.avgRating = avgRating;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getLengthMin() {
		return lengthMin;
	}

	public void setLengthMin(int lengthMin) {
		this.lengthMin = lengthMin;
	}

	public double getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(double avgRating) {
		this.avgRating = avgRating;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Movie [id=" + id + ", title=" + title + ", genre=" + genre + ", lengthMin=" + lengthMin
				+ ", avgRating=" + avgRating + "]";
	}
	
	

}
