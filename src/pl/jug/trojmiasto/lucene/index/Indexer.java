package pl.jug.trojmiasto.lucene.index;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.facet.index.FacetFields;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.facet.taxonomy.TaxonomyWriter;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.FSDirectory;

import pl.jug.trojmiasto.lucene.model.Article;

public class Indexer {
	private IndexWriter indexWriter;
	private TaxonomyWriter taxonomyWriter;
	private FacetFields facetFields;

	public Indexer(String indexPath, OpenMode openMode) throws IOException {
		openIndex(indexPath, openMode);
	}

	private void openIndex(String indexPath, OpenMode openMode)
			throws IOException {
		IndexWriterConfig conf = new IndexWriterConfig(
				WikiIndexConfig.LUCENE_VERSION, new StandardAnalyzer(
						WikiIndexConfig.LUCENE_VERSION));
		conf.setOpenMode(openMode);
		indexWriter = new IndexWriter(FSDirectory.open(new File(indexPath)),
				conf);

		taxonomyWriter = new DirectoryTaxonomyWriter(FSDirectory.open(new File(
				indexPath + WikiIndexConfig.TAXO_INDEX_PATH_SUFFIX)), openMode);
		facetFields = new FacetFields(taxonomyWriter);
	}

	public void indexAll(WikiDataProvider wikiDataProvider) throws IOException {
		for (Article article : wikiDataProvider) {
			addArticle(article);
		}
		wikiDataProvider.close();
		closeIndex();
	}

	private void addArticle(Article article) throws IOException {
		Document document = articleToDocument(article);

		List<CategoryPath> categoryPaths = new LinkedList<CategoryPath>();
		categoryPaths.add(new CategoryPath(WikiIndexConfig.ROOT_CAT
				+ article.getCategory(), '/'));
		facetFields.addFields(document, categoryPaths);

		indexWriter.addDocument(document);

	}

	private Document articleToDocument(Article article) {
		Document document = new Document();
		FieldType textField = new FieldType();
		textField.setStored(true);
		textField.setIndexed(true);
		textField.setTokenized(true);
		Field title = new Field(WikiIndexConfig.TITLE_FIELD_NAME,
				article.getTitle(), textField);
		Field content = new Field(WikiIndexConfig.CONTENT_FIELD_NAME,
				article.getContent(), textField);
		Field date = new Field(WikiIndexConfig.TIME_STRING_FIELD_NAME,
				article.getTimeString(), textField);
		Field category = new Field(WikiIndexConfig.CATEGORY_FIELD_NAME,
				article.getCategory(), textField);
		document.add(title);
		document.add(content);
		document.add(date);
		document.add(category);
		return document;
	}

	private void closeIndex() throws IOException {
		indexWriter.close();
		taxonomyWriter.close();
	}
}
