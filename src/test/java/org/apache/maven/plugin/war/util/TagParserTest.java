package org.apache.maven.plugin.war.util;

import org.apache.maven.plugin.war.util.TagParser.SymbolError;
import org.junit.Assert;
import org.junit.Test;

public class TagParserTest {
  @Test
  public void testHtmlTag() throws SymbolError {
    String tag =
        "<HELLO  filter=\"a\'\" type=\"text/javascript\" id='node' src=\"http://www.test.com/abc.js\" async=\"true\" />";
    TagParser token = new TagParser(tag);
    token.parser();
    Assert.assertEquals("HELLO", token.getTagName());
    Assert.assertEquals("http://www.test.com/abc.js", token.getAttr("src"));
    Assert.assertEquals(null, token.getAttr("test"));
  }

  @Test
  public void testJspTag() throws SymbolError {
    String tag =
        "<tags:sea_js module='slots' module_alias=\"slots.js\" base_url=\"/activity/resources/slots/js/${requestScope['sea.env']}/\"></tags:sea_js>";
    tag="<tags:sea_js module=\"slots\" module_alias=\"slots_a6d69b7d.js\" isMd5Process=\"true\" base_url=\"/activity/resources/slots/js/dist/\"></tags:sea_js>";
    JspTagParser token = new JspTagParser(tag);
    token.parser();
    Assert.assertEquals("tags:sea_js", token.getTagName());
    Assert.assertEquals("slots", token.getAttr("module"));
    Assert.assertEquals(null, token.getAttr("test"));
    token.getProperty().put("isMd5Process", "true");
    System.out.println(token.formatTag(true));
  }

}
