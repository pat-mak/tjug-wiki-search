package pl.jug.trojmiasto.lucene.index;

import java.io.IOException;

import org.apache.lucene.index.IndexWriterConfig.OpenMode;

public class IndexerApp {

	public static void main(String[] args) throws IOException {
		Indexer indexer = new Indexer(WikiIndexConfig.INDEX_PATH, OpenMode.CREATE);
		indexer.indexAll(new WikiDataProvider("/tmp/wikipedia-15.indexData"));
	}

}
