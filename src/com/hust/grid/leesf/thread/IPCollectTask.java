package com.hust.grid.leesf.thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hust.grid.leesf.bean.IpInfo;

public class IPCollectTask extends TimerTask {
	private BlockingQueue<IpInfo> ipInfoQueue; // 连接生产者与消费者的阻塞队列
	private List<IpInfo> historyIpLists; // 记录已经获取的ip信息

	public IPCollectTask(BlockingQueue<IpInfo> ipInfoQueue) {
		this.ipInfoQueue = ipInfoQueue;
		this.historyIpLists = new ArrayList<IpInfo>();
	}

	/**
	 * 获取www.xicidaili.com的ip地址信息
	 */
	public void getXiCiDaiLiIpLists() {
		String url = "http://www.xicidaili.com/nn/";
		String host = "www.xicidaili.com";
		Document doc = getDocumentByUrl(url, host);
		// 解析页面的ip信息
		parseXiCiDaiLiIpLists(doc);
	}
	
	/**
	 * 解析页面的ip信息 
	 * @param doc
	 */
	public void parseXiCiDaiLiIpLists(Document doc) {
		Elements eleLists = doc.getElementsByTag("tbody");
		Element tbody = eleLists.get(0); // 获取tbody
		Elements trLists = tbody.children();
		Element ele = null;
		for (int i = 0; i < trLists.size(); i++) {
			if ((i % 22 == 0) || (i % 22 == 1)) { // 去掉不符合条件的项
				continue;
			}
			ele = trLists.get(i);
			Elements childrenList = ele.children();
			String ipAddress = childrenList.get(1).text();
			int port = Integer.parseInt(childrenList.get(2).text());
			String location = childrenList.get(3).text();
			String anonymousType = childrenList.get(4).text();
			String type = childrenList.get(5).text();
			//String responseSpeed = childrenList.get(6).select("[title]").text();
			String confirmTime = childrenList.get(9).text();
			IpInfo ipInfo = new IpInfo(ipAddress, port, location,
					anonymousType, type, confirmTime, "0");	
			System.out.println("获取到可用代理IP\t" + ipAddress + "\t" + port);
			if (checkProxy(ipAddress, port)) {
                System.out.println("XiCi获取到可用代理IP\t" + ipAddress + "\t" + port);
                putIpInfo(ipInfo);
            }else {
            	System.out.println("XiCi的代理IP\t" + ipAddress + "\t" + port+ "------不可用");
            }
			
		}
	}
	
	/**
	 * 将ip信息放入队列和历史记录中
	 * @param ipInfo
	 */
	private void putIpInfo(IpInfo ipInfo) {
		if (!historyIpLists.contains(ipInfo)) { // 若历史记录中不包含ip信息，则加入队列中
			// 加入到阻塞队列中，用作生产者
			try {
				ipInfoQueue.put(ipInfo);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 加入历史记录中
			historyIpLists.add(ipInfo);
		}
	}
	
	/**
	 * 根据网页Document解析出ip地址信息
	 * @param doc
	 */
	private void parseKuaiDaiLiIpLists(Document doc) {
		Elements eleLists = doc.getElementsByTag("tbody");
		Element tbody = eleLists.get(0); // 获取tbody
		Elements trLists = tbody.children(); // 获取十条ip记录
		for (Element tr : trLists) { // 遍历tr
			Elements tdElements = tr.children(); // tr中的td包含了具体的信息
			String ipAddress = tdElements.get(0).text();
			int port = Integer.parseInt(tdElements.get(1).text());
			String anonymousType = tdElements.get(2).text();
			String type = tdElements.get(3).text();
			String location = tdElements.get(4).text();
			String responseSpeed = tdElements.get(5).text();
			String confirmTime = tdElements.get(6).text();
			
			IpInfo ipInfo = new IpInfo(ipAddress, port, location,
					anonymousType, type, confirmTime, responseSpeed);
			
			if (checkProxy(ipAddress, port)) {
                System.out.println("KuaiDaiLi获取到可用代理IP\t" + ipAddress + "\t" + port);
                putIpInfo(ipInfo);
            }else {
            	System.out.println("KuaiDaiLi的代理IP\t" + ipAddress + "\t" + port+ "------不可用");
            }
				
		}
	}
	
	private static boolean checkProxy(String ip, Integer port) {
        try {
            //http://1212.ip138.com/ic.asp 可以换成任何比较快的网页
        	//Jsoup.connect("http://1212.ip138.com/ic.asp").
        	//Jsoup.connect("http://json.cn/").
        	Jsoup.connect("http://www.cnblogs.com/zhangfei/p/?page=3") 
            .timeout(3*1000)  
            .proxy(ip, port)  
            .get();  
        	System.out.println(ip+":"+port); 
            return true;
        } catch (Exception e) {
            return false;
        }
    }
	
	/**
	 * 根据提供的url和host来获取页面信息
	 * @param url
	 * @param host
	 * @return
	 */
	private Document getDocumentByUrl(String url, String host) {
		Document doc = null;
		try {
//			doc = Jsoup
//					.connect(url)
//					.header("User-Agent",
//							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
//					.header("Host", host).timeout(5000).get();
			
			 doc = Jsoup.connect(url)
                     .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                     .header("Accept-Encoding", "gzip, deflate, sdch")
                     .header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
                     .header("Cache-Control", "max-age=0")
                     .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                     .header("Cookie", "Hm_lvt_7ed65b1cc4b810e9fd37959c9bb51b31=1462812244; _gat=1; _ga=GA1.2.1061361785.1462812244")
                     .header("Host", "www.kuaidaili.com")
                     .header("Referer", "http://www.kuaidaili.com/free/outha/")
                     .timeout(30 * 1000)
                     .get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return doc;
	} 
	
	/**
	 * 获取http://www.kuaidaili.com/free/的ip
	 */
//	private void getKuaiDaiLiFreeIpLists() {
//		// 第一次访问，需解析总共多少页
//		String baseUrl = "http://www.kuaidaili.com/free/inha/";
//		String host = "www.kuaidaili.com";
//		Document doc = getDocumentByUrl(baseUrl, host);
//		// 解析ip信息
//		parseKuaiDaiLiIpLists(doc);
//		Element listNav = doc.getElementById("listnav");
//		// 获取listnav下的li列表
//		Elements liLists = listNav.children().get(0).children();
//		// 获取含有多少页的子元素
//		Element pageNumberEle = liLists.get(liLists.size() - 2);
//		// 解析有多少页
//		int pageNumber = Integer.parseInt(pageNumberEle.text());
//		// 拼接成其他页的访问地址
//		for (int index = 1; index <= pageNumber; index++) {			
//			baseUrl = baseUrl + index;
//			doc = getDocumentByUrl(baseUrl, host);
//			parseKuaiDaiLiIpLists(doc);
//			// 休眠一秒
//			fallSleep(1);
//		}
//	}
	
	/**
	 * 获取www.kuaidaili.com/proxylist/的ip
	 */
	private void getKuaiDaiLiIpLists() {
		int start = 1;
		//String baseUrl = "http://www.kuaidaili.com/proxylist/";
		String baseUrl = "https://www.kuaidaili.com/free/inha/";
		String host = "www.kuaidaili.com";
		while (start <= 20) { // 爬取10页
			String url = baseUrl + start + "/";
			Document doc = getDocumentByUrl(url, host);
			// 解析ip信息
			parseKuaiDaiLiIpLists(doc);
			start++;
			// 休眠一秒
			fallSleep(1);
		}		
	}
	
	/**
	 * 进行休眠
	 */
	private void fallSleep(long seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		//getKuaiDaiLiFreeIpLists();
		System.out.println("IPCollect task is running");
	//	getKuaiDaiLiIpLists();
		getXiCiDaiLiIpLists();
	}
	
	public BlockingQueue<IpInfo> getIpInfoQueue() {
		return ipInfoQueue;
	}

	public static void main(String[] args) throws IOException {
		BlockingQueue<IpInfo> queue = new LinkedBlockingQueue<IpInfo>();
		IPCollectTask task = new IPCollectTask(queue);
		Thread thread = new Thread(task);
		thread.start();
//		try {
//			Thread.sleep(30 * 1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
//		System.out.println("queue size is " + queue.size());
//		try {
//			while (!queue.isEmpty()) {
//				System.out.println(queue.take());
//			}	
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		System.out.println("historyList size is " + task.historyIpLists.size());
		
	}
}
