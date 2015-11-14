package org.apache.maven.plugin.war.util;

import java.util.Map;


public class JspTagParser extends TagParser {

  public JspTagParser(String tag) {
    super(tag);
  }

  public String formatTag(boolean isEnd) {
    StringBuilder builder = new StringBuilder(200);
    builder.append("<" + getTagName());
    for (Map.Entry<String, String> entry : getProperty().entrySet()) {
      builder.append(" " + entry.getKey()).append("=").append("\"" + entry.getValue() + "\"");
    }
    if(isEnd){
      builder.append("/>");
    }else{
      builder.append(">");
    }
    return builder.toString();
  }

  public boolean isValidName(char chat) {
    return super.isValidName(chat) || chat == ':' || chat == '_' || (chat>='0' && chat<='9');
  }

}
