package pl.jug.trojmiasto.lucene.index;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import pl.jug.trojmiasto.lucene.model.Article;

public class WikiDataProvider implements Iterable<Article>, Closeable {

	private BufferedReader br;

	public WikiDataProvider(String fileName) {
		try {
			br = new BufferedReader(new FileReader(
					fileName));
		} catch (IOException e) {
			e.printStackTrace();
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void close() throws IOException {
		br.close();
	}

	@Override
	public Iterator<Article> iterator() {
		return new Iterator<Article>() {

			private String title;
			private String content;
			private String date;
			private String category;

			@Override
			public void remove() {
				throw new UnsupportedOperationException("OR - czyta plik");
			}

			@Override
			public Article next() {
				return new Article(title, content, category, date);
			}

			@Override
			public boolean hasNext() {
				try {
					if (null == (title = br.readLine())) {
						return false;
					}
					if (null == (date = br.readLine())) {
						return false;
					}
					if (null == (content = br.readLine())) {
						return false;
					}
					return (category = br.readLine()) != null;
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		};
	}

}
