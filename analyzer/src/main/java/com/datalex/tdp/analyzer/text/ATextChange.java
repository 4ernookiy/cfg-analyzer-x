package com.datalex.tdp.analyzer.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dzmitry_Charnavoki on 5/6/2017.
 */
public abstract class ATextChange {
  protected final String regexp;

  public ATextChange(String regexp) {
    super();
    this.regexp = regexp;
  }

  public String replace(String line) {
    StringBuffer sb = new StringBuffer();
    Matcher matcher = Pattern.compile(regexp).matcher(line);
    while (matcher.find()) {
      String replace = change(matcher);
      matcher.appendReplacement(sb, replace);
    }
    matcher.appendTail(sb);
    return sb.toString();
  }

  protected abstract String change(Matcher matcher);
}
