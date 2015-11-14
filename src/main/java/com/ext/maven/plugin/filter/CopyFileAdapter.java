package com.ext.maven.plugin.filter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.maven.plugin.war.util.GenerateShortKey;
import org.apache.maven.plugin.war.util.Logger;
import org.codehaus.plexus.util.FileUtils;

public class CopyFileAdapter {
  public void copy(File source, File destination) throws IOException {
    FileUtils.copyFile(source.getCanonicalFile(), destination);
    destination.setLastModified(source.lastModified());
  }

  private static CopyCSS copyCSS = new CopyCSS();
  private static CopyJS copyJS = new CopyJS();
  private static CopyJsp copyJsp = new CopyJsp();
  private static CopyTag copyTag = new CopyTag();

  public static void exec(File srcFile, File outFile) throws IOException {
    String name = srcFile.getName();
    if (name.endsWith(".css")) {
      copyCSS.copy(srcFile, outFile);
    } else if (name.endsWith(".js")) {
      copyJS.copy(srcFile, outFile);
    } else if (name.endsWith(".jsp")) {
      copyJsp.copy(srcFile, outFile);
    } else if (name.endsWith(".tag")) {
      copyTag.copy(srcFile, outFile);
    } else {
      FileUtils.copyFile(srcFile.getCanonicalFile(), outFile);
      outFile.setLastModified(srcFile.lastModified());
    }
  }

  protected void copyProcess(File srcFile, File outFile) throws IOException {
    FileInputStream fis = null;
    DataInputStream dis = null;
    FileOutputStream fos = null;
    DataOutputStream out = null;
    if (!outFile.getParentFile().exists()) {
      outFile.getParentFile().mkdirs();
    }
    try {
      fis = new FileInputStream(srcFile);
      dis = new DataInputStream(fis);
      fos = new FileOutputStream(outFile);
      out = new DataOutputStream(fos);
      byte[] b = new byte[(int) srcFile.length()];
      dis.read(b, 0, b.length);
      b = process(new String(b, "UTF-8"), srcFile).getBytes("UTF-8");
      out.write(b, 0, b.length);
    } catch (FileNotFoundException ex) {
      throw ex;
    } finally {
      if (out != null) {
        out.flush();
        out.close();
      }
      out = null;
      if (fos != null) {
        fos.close();
      }
      fos = null;
      if (dis != null) {
        dis.close();
      }
      dis = null;
      if (fis != null) {
        fis.close();
      }
      fos = null;
    }
    outFile.setLastModified(srcFile.lastModified());
  }

  public String process(String code, File file) throws IOException {
    // TODO 由子类各自实现
    return code;
  }

  /**
   * 拷贝MD5文件
   * 
   * @param file 原始文件
   * @param destSourcePath 目标源文件
   * @throws IOException
   */
  public static void copyMd5File(File file, String destSourcePath, String md5File) throws IOException {
    File destFile = new File(destSourcePath);
    copyMd5File(file, destFile, md5File);
  }

  public static void copyMd5File(File file, File destFile, String md5File) throws IOException {
    Logger.debug("copyMd5File(" + file.getAbsolutePath() + "," + destFile.getAbsolutePath() + "," + md5File + ")");
    if (destFile.exists()) {
      if (!cacheSet.contains(file)) {
        File distMd5File = new File(getDestFile(file, destFile, md5File));
        org.apache.commons.io.FileUtils.copyFile(destFile, distMd5File);
        cacheSet.add(file);
      }
    } else {
      productMap.put(file, destFile);
      productMapMd5.put(file, md5File);
    }
  }

  protected static Map<File, File> productMap = new HashMap<File, File>();
  private static Set<File> cacheSet = new HashSet<File>();
  protected static Map<File, String> productMapMd5 = new HashMap<File, String>();

  public static String getDestFile(File file, File destFile, String md5Name) throws IOException {
    String code = org.apache.commons.io.FileUtils.readFileToString(file);
    String shortName = GenerateShortKey.shortStr(code);
    if (md5Name != null && !md5Name.equals(shortName)) {
      Logger.info("getDestFile(" + file.getAbsolutePath() + "," + destFile.getAbsolutePath() + "," + md5Name + ")");
      Logger.systemError(new IllegalArgumentException("MD5验证失败啦..." + shortName + ":" + md5Name));
    }
    String name = destFile.getName();
    int index = name.lastIndexOf(".");
    name = name.substring(0, index) + "_" + shortName + name.substring(index);
    return destFile.getParent() + "/" + name;
  }
}
