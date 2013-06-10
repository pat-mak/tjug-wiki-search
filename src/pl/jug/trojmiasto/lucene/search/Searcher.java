package pl.jug.trojmiasto.lucene.search;

import java.util.LinkedList;
import java.util.List;

import pl.jug.trojmiasto.lucene.model.Article;
import pl.jug.trojmiasto.lucene.model.Category;

public class Searcher {

	public SearchResult searchPrefix(String query, int i) {
		// TODO to są przykładowe dane
		return fakeResults();
	}

	public SearchResult search(String query) {
		// TODO to są przykładowe dane
		List<Category> categories = new LinkedList<Category>();
		categories.add(new Category("root/Java", 1));
		SearchResult searchResult = fakeResults();
		searchResult.setCategories(categories);
		return searchResult;
	}

	private SearchResult fakeResults() {
		List<Article> articles = new LinkedList<Article>();
		articles.add(new Article("JUG", "Przykładowy artykuł.", "Java",
				"2013-03-12T13:38:36Z"));
		SearchResult searchResult = new SearchResult();
		searchResult.setArticles(articles);
		searchResult.setCount(1);
		searchResult.setSearchTime(3000000);
		return searchResult;
	}
}
