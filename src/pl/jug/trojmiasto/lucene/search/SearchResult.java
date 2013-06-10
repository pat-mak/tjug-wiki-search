package pl.jug.trojmiasto.lucene.search;

import java.util.LinkedList;
import java.util.List;

import pl.jug.trojmiasto.lucene.model.Article;
import pl.jug.trojmiasto.lucene.model.Category;

public class SearchResult {
	private static final int MICRO_TO_MILIS = 1000000;
	private List<Article> articles = new LinkedList<Article>();
	private List<Category> categories = new LinkedList<Category>();
	private int count;
	private long searchTime;
	private boolean searchSucceded = true;
	private String failMessage = "";
	
	public long getSearchTime() {
		return (int) searchTime / MICRO_TO_MILIS;
	}

	public void setSearchTime(long searchTime) {
		this.searchTime = searchTime;
	}


	public SearchResult markFailed(String message) {
		searchSucceded = false;
		failMessage = message;
		return this;
	}

	public boolean isSearchSucceded() {
		return searchSucceded;
	}

	public String getFailMessage() {
		return failMessage;
	}

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
