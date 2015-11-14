package com.ext.maven.plugin.filter;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.war.util.FileProcess;
import org.apache.maven.plugin.war.util.Logger;

import com.ext.maven.plugin.compress.AbstractCompress;

public class CopyJS extends CopyFileAdapter {
	@Override
	public void copy(File source, File destination) throws IOException {
		if (Constants.isSkip(source)) {
			super.copy(source, destination);
			return;
		}
		try {
			if (Constants.isMatchesJsFileCompress(source)) {
				if (!destination.getParentFile().exists()) {
					destination.getParentFile().mkdirs();
				}
				try {
					AbstractCompress.getJavaScriptCompressInstance().compress(source.getAbsolutePath(),
							destination.getAbsolutePath());
					Logger.info("[OK]\tcompress.js:" + source.getAbsolutePath() + "|native2ascii:"
							+ Constants.isSupportNative2ascii(source));
					if (Constants.isSupportNative2ascii(source)) {
						FileProcess.chineseTransformToUncode(destination);
					}
					return;
				} catch (Exception ex) {
					Logger.error("[ERROR]\tcompress.js:" + source.getAbsolutePath(), ex);
				}
			}
			super.copy(source, destination);
		} finally {
			if (productMap.containsKey(source)) {
				copyMd5File(source, destination,productMapMd5.get(source));
				productMap.remove(source);
			}
		}
	}

}
