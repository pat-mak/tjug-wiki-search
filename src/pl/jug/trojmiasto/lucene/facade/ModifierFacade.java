package pl.jug.trojmiasto.lucene.facade;

import java.io.IOException;
import java.util.Date;

import pl.jug.trojmiasto.lucene.modify.Modifier;

public class ModifierFacade {

	private Modifier modifier;

	public ModifierFacade() throws IOException {
		modifier = new Modifier();

	}

	public boolean add(String title, String category, String content) {
		String date = new Date().toString();
		return modifier.add(title, category, content, date);
	}
}
