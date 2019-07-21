package main;

import processing.core.PGraphics;

public interface MarkerHint {
	final static float TITLE_SIZE = 16;
	final static float TEXT_SIZE = 12;
	final static float PADDING_X = 20;
	final static float PADDING_Y = 10;
	
	public default void showHint(PGraphics pg, float x, float y) {
		pg.pushStyle();
		
		float textLineSpacing = (pg.textAscent() + pg.textDescent()) / 2;
		float textSize = getHintTextLinesCount() * (TEXT_SIZE + textLineSpacing);
		
		String title = getHintTitle();
		String text = getHintText();
		
		float textWidth = pg.textWidth(title) > pg.textWidth(text)
				? pg.textWidth(title) : pg.textWidth(text);
		
		pg.textSize(TITLE_SIZE);
		float titleLineSpacing = (pg.textAscent() + pg.textDescent()) / 2;
		float titleSize = getHintTitleLinesCount() * (TITLE_SIZE + titleLineSpacing);
		
		pg.strokeWeight = 1;
		pg.rect(x, y, textWidth + PADDING_X, titleSize + textSize + PADDING_Y);
		
		pg.textAlign(PGraphics.LEFT, PGraphics.TOP);
		pg.fill(0, 0, 0);

		pg.text(title, x + PADDING_X / 2, y + PADDING_Y);
		
		pg.textSize(TEXT_SIZE);
		pg.text(text, x + PADDING_X / 2, y + PADDING_Y + titleSize);
		
		pg.popStyle();
	}
	
	abstract public String getHintTitle();
	abstract public String getHintText();
	abstract public int getHintTitleLinesCount();
	abstract public int getHintTextLinesCount();
}
