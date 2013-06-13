package pl.jug.trojmiasto.lucene.index;

import org.apache.lucene.util.Version;

public class WikiIndexConfig {
	public static final String INDEX_PATH="/tmp/wiki_index";
	public static final String TAXO_INDEX_PATH_SUFFIX="/taxo";
	public static final String ROOT_CAT = "root";
	public static final String TITLE_FIELD_NAME = "title";
	public static final String CONTENT_FIELD_NAME = "content";
	public static final String CATEGORY_FIELD_NAME = "category";
	public static final String TIME_STRING_FIELD_NAME = "timeString";
	public static final String TITLE_NGRAM_FIELD_NAME = "titleNGram";
	public static final Version LUCENE_VERSION = Version.LUCENE_43;
}
