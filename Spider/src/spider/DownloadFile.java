package spider;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
public class DownloadFile        //网页下载并处理
<getResponseBodyAsStream>
{
	/**
	 * 根据URL和网页类型生成需要保存的网页的文件名，去除URL中的非文件名字符
	 */
	public String getFileNameByUrl(String url, String contentType)
	{
		//移除http
		url = url.substring(7);
		//text/html类型
		if(contentType.indexOf("html")!=-1)
		{
			url = url.replace("[\\?/:*|<>\"]", "_")+".html";
			return url;
		}
		//application/pdf类型
		else 
		{
			return url.replaceAll("[\\?/:*<>\"]", "_")+"."+contentType.substring(contentType.lastIndexOf("/")+1);
		}
	}
	/**
	 * 保存网页字节数组到本地文件，filePath为要保存的文件的相对地址
	 */
	
	private  String get_right_filename(String filename)
	{
		String res="";
		for(char c:filename.toCharArray())
			if(c!='/'&&c!='\\'&&c!=':'&&c!='*'&&c!='?'&&c!='"'&&c!='>'&&c!='<'&&c!='|') res+=c;
		return res;
	}
	private void saveToLocal(byte[] data,String filePath)
	{
		System.out.println("文件原路径为"+filePath);
		
		String path0=filePath.substring(0,filePath.lastIndexOf("/")+1);
		String path1=filePath.substring(filePath.lastIndexOf("/")+1,filePath.length());
		path1=get_right_filename(path1);
		
		filePath=path0+path1;
		System.out.println("文件处理后路径为"+filePath);
		File f = new File(path0);
        // 创建文件夹
        if (!f.exists()) {
            f.mkdirs();
        }
        
        
		try
		{
			DataOutputStream out;
			out = new DataOutputStream(new FileOutputStream(new File(filePath)));
			for(int i=0;i< data.length;i++)
				out.write(data[i]);
			out.flush();
			out.close();
		}
		catch (IOException e) 
		{
			
			System.out.println("保存文件出错");
			e.printStackTrace();
		}
	}
	//下载URL指向的网页
	public String downloadFile(String url)
	{
		String filePath = null;
		
		//1.生成HttpClient对象并设置参数
		HttpClient httpClient = new HttpClient();
		
		//设置HTTP连接超时5s
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		
		//2.生成GetMethod对象并设置参数
		GetMethod getMethod = new GetMethod(url);
		
		//设置get请求超时5s
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		
		//设置请求重试处理
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		//3.执行HTTP GET请求
		try 
		{
			int statusCode;
			statusCode = httpClient.executeMethod(getMethod);
			//判断状态码
			if(statusCode != HttpStatus.SC_OK);
			{
				System.err.println("Method failed: "+getMethod.getStatusLine());
				filePath = null;
			}
			//4.处理HTTP响应内容
			byte[] responseBody = getMethod.getResponseBody();//读取为字节数组
			
			//System.out.println(responseBody.length);
			//根据网页url生成保存时的文件名
			filePath = "temp/"+getFileNameByUrl(url,getMethod.getResponseHeader("Content-Type").getValue());
			
			//filePath="E:\\"+url.substring(7, url.length())+".html";
			
			saveToLocal(responseBody,filePath);
			
			
		}
		catch(HttpException e)
		{
			//发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("请检查网址是否正确!");
			e.printStackTrace();
		}
		catch(IOException e)
		{
			//发生网络异常
			e.printStackTrace();
		}
		finally
		{
			//释放连接
			getMethod.releaseConnection();
		}
		return filePath;		
	}
}
