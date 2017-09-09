package spider;

public interface LinkFilter //内部类的一个接口
{
		public boolean accept(String url);
}
/**
*	LinkFilter类其实是个接口，实现为一个内部类。自己定义一下这个接口，代码如下：
	public interface LinkFilter 
	{
		public boolean accept(String url);
	}
*/