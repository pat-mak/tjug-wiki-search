package pl.jug.trojmiasto.lucene.index;

import java.io.Reader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter.Side;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.util.Version;

public class EdgeNGramAnalyzer extends StopwordAnalyzerBase {

	private static final int MAX_NGRAM_LENGTH=50;
	
	protected EdgeNGramAnalyzer(Version version) {
		super(version);
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName,
			Reader reader) {
		StandardTokenizer tokenizer = new StandardTokenizer(WikiIndexConfig.LUCENE_VERSION, reader);
		TokenStream tokenStream = new StandardFilter(WikiIndexConfig.LUCENE_VERSION, tokenizer);
		tokenStream = new LowerCaseFilter(WikiIndexConfig.LUCENE_VERSION, tokenStream);
		tokenStream = new EdgeNGramTokenFilter(tokenStream,Side.FRONT, 1, MAX_NGRAM_LENGTH);
		return new TokenStreamComponents(tokenizer, tokenStream);
	}

}
