package pl.jug.trojmiasto.lucene.modify;

import java.io.IOException;

import org.apache.lucene.index.IndexWriterConfig.OpenMode;

import pl.jug.trojmiasto.lucene.index.Indexer;
import pl.jug.trojmiasto.lucene.index.WikiIndexConfig;
import pl.jug.trojmiasto.lucene.model.Article;

public class Modifier {

	private Indexer indexer;

	public Modifier() throws IOException {
		indexer = new Indexer(WikiIndexConfig.INDEX_PATH, OpenMode.APPEND);
	}

	public boolean add(String title, String category, String content,
			String date) {
		try {
			indexer.addSingleArticle(new Article(title, content, category, date));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
