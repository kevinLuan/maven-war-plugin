package org.apache.maven.plugin.war.util;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.ext.maven.plugin.filter.Constants;
import com.ext.maven.plugin.filter.CopyFileAdapter;

public class JspMd5ProcessUtils {
  private static final String tags = "link|script|img";
  private static final String regxp = "(?i)<\\s*(" + tags + ")\\s+([^>]*)\\s*>";
  private static Pattern pattern = Pattern.compile(regxp);

  public static String process(String html, String baseURL, File source) {
    try {
      if (StringUtils.isNotEmpty(html)) {
        Matcher matcher = pattern.matcher(html);
        StringBuffer sb = new StringBuffer(html.length());
        boolean result = matcher.find();
        while (result) {
          String src = matcher.group(0);
          if (StringUtils.isNotEmpty(baseURL)) {
            String target = formatRegexChar$(appendBaseUrl(src, baseURL, source));
            if (target.equals(src) == false) {
              matcher.appendReplacement(sb, target);
            }
          }
          result = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
      }
    } catch (Exception e) {
      Logger.systemError("process(" + html + "\n\nbaseURL:" + baseURL + "\n\nsource:" + source.getAbsolutePath() + ")",
          e);
    }
    return html;
  }

  private static String formatRegexChar$(String str) {
    if (str.indexOf("$") != -1) {
      return str.replaceAll("\\$", "\\\\\\$");
    }
    return str;
  }

  protected static String versionControl(String str) {
    if (str.matches("(?i).*v=(\\d+).*")) {
      return str.replaceAll("v=(\\d+)", "v=" + System.currentTimeMillis());
    }
    return str;
  }

  private static final String REGEX_URL = "(?i)<\\s*.*?\\s+(src|href)=[\"'](.*?)[\"'].*?\\s*/?>";
  private static final Pattern pattern_replace = Pattern.compile(REGEX_URL);

  private static String appendBaseUrl(String htmlTag, String baseURL, File sourceFile) {
    try {
      if (StringUtils.isNotEmpty(htmlTag, baseURL)) {
        Matcher matcher = pattern_replace.matcher(htmlTag);
        StringBuffer sb = new StringBuffer(htmlTag.length() + baseURL.length());
        boolean result = matcher.find();
        while (result) {
          String matcherSrc = matcher.group(0);
          String url = matcher.group(2);
          if (url.startsWith("/")) {
            String source = processMd5(url, sourceFile);
            matcherSrc = matcherSrc.replace(url, baseURL + source);
            matcherSrc = formatRegexChar$(matcherSrc);
            matcher.appendReplacement(sb, matcherSrc);
          } else {// 没有进行MD5处理文件引用的需要生成版本号
            matcherSrc = versionControl(matcherSrc);
            matcherSrc = formatRegexChar$(matcherSrc);
            matcher.appendReplacement(sb, matcherSrc);
          }
          result = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
      }
    } catch (Exception e) {
      Logger.systemError(
          "appendBaseUrl(htmlTag:" + htmlTag + ",baseURL:" + baseURL + ",sourceFile:" + sourceFile.getAbsolutePath()
              + ")", e);
    }
    return htmlTag;
  }

  public static String processMd5(String source, File sourceFile) throws IOException {
    String file = parserSourceFile(source);
    if (isJS_OR_CSS(file)) {
      // 解析 JSP 引用的JS,CSS 处理MD5
      String query = parserQuery(source);
      File refFile = new File(buildSourcePath(file));
      String md5Val = GenerateShortKey.shortStr(FileUtils.readFileToString(refFile));
      Logger.debug(md5Val + "------>" + refFile.getAbsoluteFile());
      String tmpFile = buildMd5FileName(file, md5Val);
      CopyFileAdapter.copyMd5File(refFile, Constants.getProperty(Constants.OUTPUT_DIRECTORY) + file, md5Val);
      file = tmpFile;
      Logger.debug("---->>>>生成最后的链接" + file + query);
      return file + query;
    } else {
      return versionControl(source);
    }
  }

  public static String getMd5Value(String source, File sourceFile) throws IOException {
    String file = parserSourceFile(source);
    if (isJS_OR_CSS(file)) {
      File refFile = new File(buildSourcePath(file));
      String md5Val = GenerateShortKey.shortStr(FileUtils.readFileToString(refFile));
      Logger.debug(md5Val + "------>" + refFile.getAbsoluteFile());
      CopyFileAdapter.copyMd5File(refFile, Constants.getProperty(Constants.OUTPUT_DIRECTORY) + file, md5Val);
      return appendMd5Val(refFile.getName(), md5Val);
    }
    return null;
  }

  private static String appendMd5Val(String sourceName, String md5Val) {
    int endIndex = sourceName.lastIndexOf(".");
    String endWith = sourceName.substring(endIndex);
    return sourceName.substring(0, endIndex) + "_" + md5Val + endWith;
  }

  private static String buildSourcePath(String file) {
    int index = file.indexOf("/", 1);
    String endWith = file.substring(index);
    return Constants.getProperty(Constants.BASE_DIR) + "/src/main/webapp" + endWith;
  }

  private static String buildMd5FileName(String file, String md5Val) {
    int index = file.lastIndexOf(".");
    String suffix = file.substring(index);
    return file.substring(0, index) + "_" + md5Val + suffix;
  }

  private static String parserSourceFile(String url) {
    int index = url.indexOf("?");
    if (index > 0) {
      return url.substring(0, index);
    }
    return url;
  }

  public static String parserQuery(String url) {
    int index = url.indexOf("?");
    if (index > 0) {
      return url.substring(index);
    }
    return "";
  }

  public static boolean isJS_OR_CSS(String file) {
    file = file.toLowerCase();
    return file.endsWith(".js") || file.endsWith(".css");
  }

}
