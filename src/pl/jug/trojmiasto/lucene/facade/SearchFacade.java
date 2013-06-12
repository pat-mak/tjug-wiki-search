package pl.jug.trojmiasto.lucene.facade;

import java.io.IOException;

import pl.jug.trojmiasto.lucene.search.SearchResult;
import pl.jug.trojmiasto.lucene.search.Searcher;

public class SearchFacade {

	private Searcher searcher;

	public SearchFacade() throws IOException {
		searcher = new Searcher();
	}

	public SearchResult suggestions(String query) throws IOException {
		return searcher.searchPrefix(query, 20);
	}

	public SearchResult search(String query) throws IOException {
		return searcher.search(query, 20);
	}
}
