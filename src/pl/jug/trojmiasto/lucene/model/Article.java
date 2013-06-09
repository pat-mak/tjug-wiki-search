package pl.jug.trojmiasto.lucene.model;

public class Article {
	
	private String title;
	private String content;
	private String category;
	private String timeString;

	public Article(String title, String content, String category,
			String timeString) {
		super();
		this.title = title;
		this.content = content;
		this.category = category;
		this.timeString = timeString;
	}

	public String getTitle() {
		return title;
	}

	public String getCategory() {
		return category;
	}

	public String getContent() {
		return content;
	}

	public String getTimeString() {
		return timeString;
	}

	@Override
	public String toString() {
		return "Article [title=" + title + ", content=" + content
				+ ", category=" + category + ", timeString=" + timeString + "]";
	}

}
