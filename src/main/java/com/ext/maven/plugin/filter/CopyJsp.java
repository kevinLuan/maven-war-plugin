package com.ext.maven.plugin.filter;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.combo.ComboExecute;
import org.apache.maven.plugin.war.util.InjectCodeUtils;
import org.apache.maven.plugin.war.util.JspMd5ProcessUtils;
import org.apache.maven.plugin.war.util.Logger;
import org.apache.maven.plugin.war.util.SeaJsTagProcessUtils;
import org.apache.maven.plugin.war.util.StringUtils;

import com.ext.maven.plugin.compress.HTMLCompress;
import com.ext.maven.plugin.parser.ScriptTagChunkCompress;
import com.ext.maven.plugin.parser.StyleTagChunkCompress;

public class CopyJsp extends CopyFileAdapter {
  private boolean versionCDN = false;
  private boolean cssCompress = false;
  private boolean jsCompress = false;
  private boolean htmlCompress = false;
  private boolean isCombo = false;
  private boolean isMd5File = false;
  private boolean isHeadInject = false;
  public static File srcFile = null;

  @Override
  public void copy(File source, File destination) throws IOException {
    if (Constants.isSkip(source)) {
      super.copy(source, destination);
      return;
    }
    if (!destination.getParentFile().exists()) {
      destination.getParentFile().mkdirs();
    }
    srcFile = source;
    versionCDN = Constants.isMatchesVersionCDN(source);
    cssCompress = Constants.isMatchesJsp_CssChunkCompress(source);
    jsCompress = Constants.isMatchesJsp_JsChunkCompress(source);
    htmlCompress = Constants.isMatchesHTMLCompress(source);
    isCombo = Constants.isMatchesCombo(source);
    isMd5File = Constants.isMatchesMD5FileName(source);
    isHeadInject = Constants.isMatchesHeadInjectCode(source);
    if (versionCDN || cssCompress || jsCompress || htmlCompress || isCombo || isMd5File || isHeadInject) {
      Logger.info("jsp:" + source.getAbsolutePath() + "|cssCompress:" + cssCompress + "|jsCompress:" + jsCompress
          + "|htmlCompress:" + htmlCompress + "|isCombo:" + isCombo + "|isMd5File:" + isMd5File + "|isHeadInject:"
          + isHeadInject);
      copyProcess(source, destination);
    } else {
      super.copy(source, destination);
    }
  }

  @Override
  public String process(String code, File source) throws IOException {
    String cdn = Constants.getCdnBaseURL();
    code = SeaJsTagProcessUtils.processSeaJsUrlMd5(code, cdn, source);
    code = compressCssChunk(code);
    code = compressJsChunk(code);
    code = compressHTML(code);
    code = combo(code, source);
    if (isMd5File) {
      code = JspMd5ProcessUtils.process(code, cdn, source);
    } else if (versionCDN) {
      code = StringUtils.filterHTML(code, cdn);
    }
    code = injectCode(code);
    return code;
  }

  private String injectCode(String code) {
    if (isHeadInject) {
      return InjectCodeUtils.injectCode(code);
    } else {
      return code;
    }
  }

  private String combo(String code, File source) throws IOException {
    if (isCombo) {
      code = ComboExecute.comboLink(code, source);
      code = ComboExecute.comboScript(code, source);
    }
    return code;
  }

  private String compressHTML(String code) {
    if (htmlCompress) {
      return HTMLCompress.compressCode(code);
    }
    return code;
  }

  private String compressCssChunk(String code) {
    if (cssCompress) {
      code = new StyleTagChunkCompress().parserHtmlAndCompress(code);
    }
    return code;
  }

  private String compressJsChunk(String code) {
    if (jsCompress) {
      return new ScriptTagChunkCompress().parserHtmlAndCompress(code);
    }
    return code;
  }

}
