package pl.jug.trojmiasto.lucene.facade;

import java.io.IOException;

import pl.jug.trojmiasto.lucene.search.SearchResult;
import pl.jug.trojmiasto.lucene.search.Searcher;

public class SearchFacade {

	private static Searcher searcher;

	public SearchFacade() throws IOException {
		searcher = new Searcher();
	}
	
	public SearchResult suggestions(String query) {
		return searcher.searchPrefix(query, 20);
	}

	public SearchResult searchWithCategories(String query){
		return searcher.searchWithCategoriesAndHighlight(100, 10, query);
	}
}
