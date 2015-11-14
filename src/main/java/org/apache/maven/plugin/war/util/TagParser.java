package org.apache.maven.plugin.war.util;

import java.util.HashMap;
import java.util.Map;

public class TagParser {
  private Integer index = 0;
  private char[] tagChar;
  private int position = 0;
  /* 解析符号 */
  private char symbol = '"';
  public static final String START_SCRIPT = "<";
  public static final String END_SCRIPT = ">";
  public static final String END_SCRIPT_1 = "/>";
  public static final String EQ = "=";
  public static final char SPACE = ' ';
  public static final String MUST_SPACE = " ";
  public static final String TAB = "	";
  public static final String SYMBOL = "'";
  private DFAStatus status;
  private Map<Integer, Entity> map = new HashMap<Integer, Entity>();
  private Map<String, String> result = new HashMap<String, String>();
  private String tagName;
  public Map<String, String>getProperty(){
    return result;
  }
  public String getTagName() {
    return this.tagName;
  }

  private boolean startsWith(String str) {
    char[] chat = str.toCharArray();
    if (position + chat.length > tagChar.length) {
      return false;
    }
    for (int i = 0; i < chat.length; i++) {
      if (tagChar[position + i] != chat[i]) {
        if (is_az(chat[i])) {
          if (tagChar[position + i] == chat[i] - 32) {
            continue;
          }
        }
        return false;
      }
    }
    return true;
  }

  public void parser() throws SymbolError {
    if (status == null) {
      status = DFAStatus.UNSTART;
      skipSpace();
    }
    if (status == DFAStatus.UNSTART) {
      if (startsWith(START_SCRIPT)) {
        position += START_SCRIPT.length();
        status = DFAStatus.TAG_NAME;
        parser();
      } else {
        throw new SymbolError("语法错误:" + tagChar[position]);
      }
    } else if (status == DFAStatus.TAG_NAME) {
      parserTagName();
      status = DFAStatus.START;
      parser();
    } else if (status == DFAStatus.START) {
      nextSpace();
      parser();
    } else if (status == DFAStatus.NULL) {
      skipSpace();
      if (startsWith(END_SCRIPT_1)) {
        status = DFAStatus.DONE;
        done();
        return;
      } else if (startsWith(END_SCRIPT)) {
        status = DFAStatus.DONE;
        done();
        return;
      }
      parserName();
      parser();
    } else if (status == DFAStatus.EQ) {
      parserVal();
      parser();
    } else if (status == DFAStatus.SYMBOL_END) {
      status = DFAStatus.NULL;
      parser();
    }
  }

  private void parserTagName() throws SymbolError {
    StringBuilder builder = new StringBuilder(20);
    for (int i = position; i < tagChar.length; i++) {
      if (isValidName(tagChar[i])) {
        builder.append(tagChar[i]);
        position++;
      } else if (builder.length() > 0 && tagChar[i] == ' ') {
        this.tagName = builder.toString();
        return;
      } else {
        break;
      }
    }
    throw new SymbolError("语法错误:" + tagChar[position]);
  }

  private void done() {
    for (Entity entity : map.values()) {
      result.put(entity.name, entity.value);
    }
  }

  private void parserVal() throws SymbolError {
    skipSpace();
    StringBuilder builder = new StringBuilder();
    int startIndex = position;
    for (int i = position; i < tagChar.length; i++) {
      if (i == startIndex) {
        if (tagChar[i] == '\'' || tagChar[i] == '"') {
          symbol = tagChar[i];
          status = DFAStatus.SYMBOL_START;
          position++;
        } else {
          throw new SymbolError("语法错误:" + tagChar[position]);
        }
      } else {
        if (tagChar[i] == symbol) {
          status = DFAStatus.SYMBOL_END;
          position++;
          break;
        } else {
          builder.append(tagChar[i]);
          position++;
        }
      }
    }
    map.get(index).value = builder.toString();
    index++;
  }

  private boolean is_AZ(char chat) {
    return chat >= 65 && chat <= 90;
  }

  private boolean is_az(char chat) {
    return chat >= 97 && chat <= 122;
  }

  private void parserName() throws SymbolError {
    StringBuilder builder = new StringBuilder();
    for (int i = position; i < tagChar.length; i++) {
      if (isValidName(tagChar[i])) {
        builder.append(tagChar[i]);
        position++;
      } else {
        if (builder.length() > 0) {
          skipSpace();
          nextEQ();
          break;
        }
      }
    }
    if (builder.toString().length() == 0) {
      throw new SymbolError("语法错误:" + tagChar[position]);
    }
    map.put(index, new Entity(builder.toString()));
  }

  private void nextEQ() throws SymbolError {
    if (startsWith(EQ)) {
      position++;
      status = DFAStatus.EQ;
    } else {
      throw new SymbolError("语法错误:" + tagChar[position]);
    }
  }

  private void skipSpace() {
    for (int i = position; i < tagChar.length; i++) {
      if (tagChar[i] == SPACE || tagChar[i] == '\t') {
        position++;
      } else {
        return;
      }
    }
  }

  private void nextSpace() throws SymbolError {
    if (startsWith(MUST_SPACE)) {
      position += MUST_SPACE.length();
      status = DFAStatus.NULL;
    } else if (startsWith(TAB)) {
      position += TAB.length();
      status = DFAStatus.NULL;
    } else {
      throw new SymbolError("语法错误:" + tagChar[position]);
    }
  }

  public String getAttr(String name) {
    return result.get(name);
  }

  public TagParser(String str) {
    this.tagChar = str.toCharArray();
  }

  @Override
  public String toString() {
    return result.toString();
  }

  public static class SymbolError extends Exception {
    private static final long serialVersionUID = 2441411373778495898L;

    public SymbolError(String msg) {
      super(msg);
    }
  }

  public static class Entity {
    public Entity(String name) {
      this.name = name;
    }

    public String name;
    public String value;

    @Override
    public String toString() {
      return "[" + name + ":" + value + "]";
    }
  }

  public enum DFAStatus {
    UNSTART, START, TAG_NAME, SYMBOL_START, SYMBOL_END, DONE, NULL, EQ
  }

  public static TagParser parser(String htmlTag) throws SymbolError {
    TagParser tag = new TagParser(htmlTag);
    tag.parser();
    return tag;
  }

  public boolean isValidName(char chat) {
    return is_az(chat) || is_AZ(chat);
  }
}
