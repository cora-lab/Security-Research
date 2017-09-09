package spider;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

import classes.LinkQueue;

public class MyCrawler				//广度优先爬虫主程序
{
	/**
	 * 使用种子初始化URL队列
	 */
	private void initCrawlerWithSeeds(String[] seeds)
	{
		for(int i=0;i<seeds.length;i++)
		{
			LinkQueue.addUnvisitedUrl(seeds[i]);
		}
	}
	/**
	 * 抓取过程
	 */
	public void crawling(String[]seeds)
	{
		//定义过滤器，提取以http://www.baidu.com 开头的链接
		LinkFilter filter = new LinkFilter()
		{
			public boolean accept(String url)
			{
				if(url.startsWith("http://blog.sina.com.cn"))
					return true;
				else 
					return false;
			}
		};
		//初始化URL队列
		initCrawlerWithSeeds(seeds);
		
		//循环条件：待抓取的链接不空且抓取的网页不多于100
		while(!LinkQueue.unVisitedUrlsEmpty()&&LinkQueue.getVisitedUrlNum()<=1000)//爬虫结束条件
		{
			//队头URL出队列
			String visitUrl = (String)LinkQueue.unVisitedUrlDeQueue();
			if(visitUrl==null)
				continue;
			
			DownloadFile downLoader = new DownloadFile();
			//下载网页
			downLoader.downloadFile(visitUrl);
			
			//该URL放入已访问的URL中
			LinkQueue.addVisitedUrl(visitUrl);
			
			//提取出下载网页中的URL
			Set<String>links = HtmlParserTool.extracLinks(visitUrl, filter);
			
			//新的未访问的URL入队
			for(String link:links)
			{
				LinkQueue.addUnvisitedUrl(link);
			}
		}
		
	}
	
	public static void main(String [] args) throws IOException
	{
		MyCrawler crawler = new MyCrawler();
		String url="http://www.baidu.com";
		String url1="http://blog.sina.com.cn";
		crawler.crawling( new String[] {url1} );
		System.out.println("main方法已完成");
	}
}