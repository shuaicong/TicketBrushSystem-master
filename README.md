# TicketBrushSystem

## 系统介绍
爬取免费`ip`代理

## 使用技术
使用HttpClient + JSoup + 多线程，JSoup用于解析页面，多线程技术用于分离任务，使得分工更加明确。使用到了生产者消费者模式，该模式直接使用BlockingQueue来实现。


　几点说明：

　　1.系统使用的代理IP站点URL为http://www.kuaidaili.com/，www.xicidaili.com。

　　2.提取IP信息为提取单条IP信息，并判断历史IP表是否已经存在，若存在，表示之前已经加入过此IP信息，则直接丢弃，反之，则加入队列并加入历史IP表。

　　3.此任务会定期开启，如一个小时爬取一次代理IP。
  
  
  　其中，bean包的IpInfo封装了爬取的IP信息。

　　其中，entrance包的Vote为系统的入口。

　　其中，thread包的IPCollectTask为爬取代理IP任务


