package demo1

import pl.edu.mimuw.crawler.ak334680._
import java.net.URI
/** First Crawler demonstration program.
 *  
 *  Using first parameter as a host page, returns the list of reachable
 *  external domains with their occurence frequency.
 */
object Demo1 extends App {
  if(args.length != 1) {
    println("You need to pass one parameter.")
  } else {
    val host = (new URI(args(0))).getHost()
    var crawler = new Crawler(new BasicExceptionHandler)
    var proc = new ExternalDomainCrawler(host)
    crawler.addPageProcessor(proc)
    crawler.addPage(args(0))
    crawler.process
    println(proc.toString)
  }
}