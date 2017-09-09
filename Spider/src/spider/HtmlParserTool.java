package spider;

import java.util.*;     //作用是：解析html网页，从网页中提取URL

import org.htmlparser.Node;//树形节点操作
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
public class HtmlParserTool 
{
	/**
	 * 获取一个网站上的链接 ，filter用来过滤链接获取子链接，
	 * url为网页url，filter是链接过滤器，返回该页面子链接的HashSet
	 * @return
	 */
	public static Set<String>extracLinks(String url , LinkFilter filter)
	{
		Set<String>links = new HashSet<String>();
		try
		{
			Parser parser = new Parser(url);
			parser.setEncoding("gb2312");
			//过滤<frame>标签的filter，用来提取frame标签里的src属性
			NodeFilter frameFilter = new NodeFilter()
			{
				public boolean accept(Node node)
				{
					if(node.getText().startsWith("frame src="))
						return true;
					else
						return false;
				}
			};
			//OrFilter来设置过滤<a>标签和<frame>标签
			OrFilter linkFilter = new OrFilter(new NodeClassFilter(LinkTag.class), frameFilter);
			
			//得到所有经过过滤的标签
			NodeList list = parser.extractAllNodesThatMatch(linkFilter);
			for(int i=0;i<list.size();i++)
			{
				Node tag=list.elementAt(i);
				if(tag instanceof LinkTag)     //<a>标签
				{
					LinkTag link = (LinkTag) tag;
					String linkUrl = link.getLink();//获得链接
					if(filter.accept(linkUrl))
						links.add(linkUrl);   //加入HashSet
				}
				else//<frame>标签
				{
					//提取frame里src属性的链接 ， 如<framesrc="test.html"/ >
					String frame = tag.getText();
					int start = frame.indexOf("src=");
					/**
					 * URL	规定要在框架中显示的文档的地址。
					 * 可能的值：
					 *		绝对 URL - 指向其他站点（比如 src="www.example.com/index.html"）
					 *		相对 URL - 指向站点内的文件（比如 src="index.html"）
					 */
					
					frame = frame.substring(start);
					int end = frame.indexOf(" ");
					if(end==-1)
						end=frame.indexOf(">");
					String frameUrl = frame.substring( 5 , end-1 );
					if(filter.accept(frameUrl))
						links.add(frameUrl);
				}//if
			}//for
		}//try
		catch(ParserException e)
		{
			e.printStackTrace();
		}
		return links;
	}
}