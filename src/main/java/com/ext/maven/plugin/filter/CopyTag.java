package com.ext.maven.plugin.filter;

import java.io.File;
import java.io.IOException;


public class CopyTag extends CopyJsp {
  @Override
  public void copy(File source, File destination) throws IOException {
    super.copy(source, destination);
  }

  @Override
  public String process(String code, File source) throws IOException {
    return super.process(code, source);
  }

}
