package pl.jug.trojmiasto.lucene.model;

import pl.jug.trojmiasto.lucene.index.WikiIndexConfig;

public class Category {

	private String name;
	private int count;

	public Category(String name, int count) {
		this.name = name;
		this.count = count;
	}
	
	public String getName() {
		return name;
	}

	public String getPrintableName() {
		return (null != name) ? name.substring(WikiIndexConfig.ROOT_CAT
				.length() + 1) : null;
	}

	public int getCount() {
		return count;
	}

}
