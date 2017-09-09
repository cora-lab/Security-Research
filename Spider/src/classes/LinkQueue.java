package classes;

import java.util.HashSet;
import java.util.Set;
import classes.Queue;

public class LinkQueue           // 记录已经被访问的URL的
{
	/**选择HashSet的原因
	 * 结构中保存的URL不能重复。
	 * 能够快速地查找 (实际系统中URL的数目非常多，因此要考虑查找性能) 。
	 */
	
	//已访问的url集合
	private static Set visitedUrl = new HashSet();
	
	//待访问的url集合
	private static Queue unVisitedUrl = new Queue();
	
	//获得未访问的URL
	public static Queue getUnVisitedUrl()
	{
		return unVisitedUrl;
	}
	// 添加到访问过的URL队列中
	public static void addVisitedUrl(String url)
	{
		visitedUrl.add(url);
	}
	// 移除访问过的URL
	public static void removeVisitedUrl(String url)
	{
		visitedUrl.remove(url);
	}
	// 未访问的URL出队列
	public static Object unVisitedUrlDeQueue()
	{
		return unVisitedUrl.deQueue();
	}
	//保证每个URL只被访问一次
	public static void addUnvisitedUrl(String url)
	{
		if(url!=null&&!url.trim().equals("")&&!visitedUrl.contains(url)&&!unVisitedUrl.contians(url))
			unVisitedUrl.enQueue(url);
	}
	// 获得已经访问的URL数目
	public static int getVisitedUrlNum()
	{
		return visitedUrl.size();
	}
	// 判断未访问的URL队列中是否为空
	public static boolean unVisitedUrlsEmpty()
	{
		return unVisitedUrl.empty();
	}
}
