package com.datalex.tdp.analyzer.text;

import java.util.regex.Matcher;

/**
 * Created by Dzmitry_Charnavoki on 5/6/2017.
 */
public class RemoveInvisibleContent extends ATextChange {
  private static final String TO_REPLACE = "<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>";

  public RemoveInvisibleContent() {
    super("(^.*?<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>)");
  }

  @Override
  protected String change(Matcher matcher) {
    return TO_REPLACE;
  }
}
