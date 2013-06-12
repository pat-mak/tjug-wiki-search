package pl.jug.trojmiasto.lucene.search;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.analyzing.AnalyzingQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import pl.jug.trojmiasto.lucene.index.WikiIndexConfig;
import pl.jug.trojmiasto.lucene.model.Article;

public class Searcher {

	private IndexSearcher searcher;

	public Searcher() throws IOException {
		searcher = new IndexSearcher(DirectoryReader.open(FSDirectory
				.open(new File(WikiIndexConfig.INDEX_PATH))));
	}

	public SearchResult searchPrefix(String query, int i) throws IOException {
		Query prefixQuery = new PrefixQuery(new Term(
				WikiIndexConfig.TITLE_FIELD_NAME, query));
		TopDocs topDocs = searcher.search(prefixQuery, i);
		List<Article> articles = extractArticlesFromTopDocs(topDocs);
		SearchResult searchResult = new SearchResult();
		searchResult.setArticles(articles);
		return searchResult;
	}

	private List<Article> extractArticlesFromTopDocs(TopDocs topDocs)
			throws IOException {
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		List<Article> articles = new LinkedList<Article>();
		for (ScoreDoc scoreDoc : scoreDocs) {
			Document document = searcher.doc(scoreDoc.doc);
			Article article = new Article(
					document.get(WikiIndexConfig.TITLE_FIELD_NAME),
					document.get(WikiIndexConfig.CONTENT_FIELD_NAME),
					document.get(WikiIndexConfig.CATEGORY_FIELD_NAME),
					document.get(WikiIndexConfig.TIME_STRING_FIELD_NAME));
			articles.add(article);
		}
		return articles;
	}

	public SearchResult search(String query, int count) throws IOException {
		SearchResult searchResult = new SearchResult();
		AnalyzingQueryParser queryParser = new AnalyzingQueryParser(WikiIndexConfig.LUCENE_VERSION,
				WikiIndexConfig.TITLE_FIELD_NAME, new StandardAnalyzer(
						WikiIndexConfig.LUCENE_VERSION));
		Query titleQuery = null;
		try {
			titleQuery = queryParser.parse(query);
		} catch (ParseException e) {
			searchResult.markFailed("Zapytanie było złe! i tory też...były złe, bo "+e.getMessage());
			e.printStackTrace();
			return searchResult;
		}
		
		TopDocs topDocs = searcher.search(titleQuery, count);
		searchResult.setArticles(extractArticlesFromTopDocs(topDocs));
		return searchResult;
	}

}
