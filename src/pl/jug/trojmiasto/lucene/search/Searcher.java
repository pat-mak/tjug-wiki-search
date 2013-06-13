package pl.jug.trojmiasto.lucene.search;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.facet.params.FacetSearchParams;
import org.apache.lucene.facet.search.CountFacetRequest;
import org.apache.lucene.facet.search.FacetRequest;
import org.apache.lucene.facet.search.FacetResult;
import org.apache.lucene.facet.search.FacetResultNode;
import org.apache.lucene.facet.search.FacetsAccumulator;
import org.apache.lucene.facet.search.FacetsCollector;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.analyzing.AnalyzingQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiCollector;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.FSDirectory;

import pl.jug.trojmiasto.lucene.index.WikiIndexConfig;
import pl.jug.trojmiasto.lucene.model.Article;
import pl.jug.trojmiasto.lucene.model.Category;

public class Searcher {

	private static final String HL_SEPARATOR = "<span class=\"separator\">&nbsp;(...)</span>";
	private IndexSearcher searcher;
	private TaxonomyReader taxonomyReader;

	public Searcher() throws IOException {
		searcher = new IndexSearcher(DirectoryReader.open(FSDirectory
				.open(new File(WikiIndexConfig.INDEX_PATH))));
		taxonomyReader = new DirectoryTaxonomyReader(FSDirectory.open(new File(
				WikiIndexConfig.INDEX_PATH
						+ WikiIndexConfig.TAXO_INDEX_PATH_SUFFIX)));
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
		BooleanQuery booleanQuery;
		try {
			booleanQuery = prepareBooleanQuery(query);
		} catch (ParseException e) {
			searchResult
					.markFailed("Zapytanie było złe! i tory też...były złe, bo "
							+ e.getMessage());
			e.printStackTrace();
			return searchResult;
		}

		FacetsCollector categoryCollector = prepateCategoryCollector();
		Sort sort = new Sort(new SortField(
				WikiIndexConfig.TIME_STRING_FIELD_NAME, Type.STRING, true));
		TopFieldCollector topFieldCollector = TopFieldCollector.create(sort,
				count, true, false, false, false);
		searcher.search(booleanQuery,
				MultiCollector.wrap(topFieldCollector, categoryCollector));

		Highlighter highlighter = new Highlighter(new SimpleHTMLFormatter(),
				new QueryScorer(booleanQuery));

		TopDocs topDocs = topFieldCollector.topDocs();
		try {
			searchResult.setArticles(extractArticlesFromTopDocs(topDocs,
					highlighter));
			searchResult
					.setCategories(extractCaegoriesFromTopDocs(categoryCollector));
		} catch (InvalidTokenOffsetsException e) {
			searchResult
					.markFailed("Podświetlanie było złe! i tory też...były złe, bo "
							+ e.getMessage());
			e.printStackTrace();
			return searchResult;
		}
		searchResult.setCount(topDocs.totalHits);
		return searchResult;
	}

	private BooleanQuery prepareBooleanQuery(String userQuery)
			throws ParseException {
		QueryParser titleQueryParser = new AnalyzingQueryParser(
				WikiIndexConfig.LUCENE_VERSION,
				WikiIndexConfig.TITLE_FIELD_NAME, new StandardAnalyzer(
						WikiIndexConfig.LUCENE_VERSION));
		QueryParser contentQueryParser = new AnalyzingQueryParser(
				WikiIndexConfig.LUCENE_VERSION,
				WikiIndexConfig.CONTENT_FIELD_NAME, new StandardAnalyzer(
						WikiIndexConfig.LUCENE_VERSION));

		Query titleQuery = null;
		Query contentQuery = null;
		titleQuery = titleQueryParser.parse(userQuery);
		contentQuery = contentQueryParser.parse(userQuery);
		contentQuery.setBoost(20.0f);
		BooleanQuery booleanQuery = new BooleanQuery();
		booleanQuery.add(titleQuery, Occur.SHOULD);
		booleanQuery.add(contentQuery, Occur.SHOULD);
		return booleanQuery;
	}

	private List<Article> extractArticlesFromTopDocs(TopDocs topDocs,
			Highlighter highlighter) throws IOException,
			InvalidTokenOffsetsException {
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		List<Article> articles = new LinkedList<Article>();
		for (ScoreDoc scoreDoc : scoreDocs) {
			Document document = searcher.doc(scoreDoc.doc);

			String content = document.get(WikiIndexConfig.CONTENT_FIELD_NAME);
			TokenStream tokenStream = TokenSources.getAnyTokenStream(searcher
					.getIndexReader(), scoreDoc.doc,
					WikiIndexConfig.CONTENT_FIELD_NAME, new StandardAnalyzer(
							WikiIndexConfig.LUCENE_VERSION));

			Article article = new Article(
					document.get(WikiIndexConfig.TITLE_FIELD_NAME),
					highlighter.getBestFragments(tokenStream, content, 3,
							HL_SEPARATOR),
					document.get(WikiIndexConfig.CATEGORY_FIELD_NAME),
					document.get(WikiIndexConfig.TIME_STRING_FIELD_NAME));
			articles.add(article);
		}
		return articles;
	}

	private FacetsCollector prepateCategoryCollector() {
		FacetRequest facetRequest = new CountFacetRequest(new CategoryPath(
				WikiIndexConfig.ROOT_CAT), 20);
		facetRequest.setDepth(2);
		FacetsCollector collector = FacetsCollector
				.create(new FacetsAccumulator(new FacetSearchParams(
						facetRequest), searcher.getIndexReader(),
						taxonomyReader));
		return collector;
	}

	private List<Category> extractCaegoriesFromTopDocs(
			FacetsCollector categoryCollector) throws IOException {
		List<FacetResult> facetResults = categoryCollector.getFacetResults();
		List<Category> categories = new LinkedList<Category>();
		for (FacetResult facetResult : facetResults) {
			List<FacetResultNode> subResults = facetResult.getFacetResultNode().subResults;
			for (FacetResultNode facetResultNode : subResults) {
				categories.add(new Category(facetResultNode.label.toString(),
						(int) facetResultNode.value));
			}
		}
		return categories;
	}

}
