/**
 *
 */
package demo1

import scala.collection.mutable.Queue
import scala.collection.mutable.Map
import pl.edu.mimuw.crawler.ak334680.CrawlerPageProcessor
import org.jsoup.nodes.Document
import java.net.URI
import scala.collection.mutable.TreeSet

/** Crawler counting the number of links to external domains from a host page.
 *  
 *  @param host the only domain which doesn't count as an external domain
 * Counts the number of links leading to external domains. Results are ordered
 * by number of links, and, if it isn't decisive, by domain names.
 */
class ExternalDomainCrawler(val host: String) extends CrawlerPageProcessor {
  private class ResultOrdering extends Ordering[(String, Int)] {
    def compare(a: (String, Int), b: (String, Int)) = {
      if(a._2 == b._2)
        a._1 compare b._1
      else
        -(a._2 compare b._2)
    }
  }
  private var reachableDomains: Map[String, Int] = Map.empty
  /** Processes a single page.
   *  
   *  @param page a [[org.jsoup.node.Document]] instance
   *  @param pageAdder method allowing enqueueing new pages
   *  If page has external domain, increase occurency counter.
   *  If not, enqueue all anchors from the document.
   */
  def process(page: Document, pageAdder: String => Unit): Unit = {
    if(getHost(page) == host) {
      val anchors = getAbsAnchors(page)
      for(link <- anchors) {
        val tmphost = getHost(link)
        if(tmphost == host)
          pageAdder(link)
        else
          reachableDomains.put(tmphost,
              reachableDomains.getOrElse(tmphost, 0) + 1)
      }
    } else {
      reachableDomains.put(getHost(page),
          reachableDomains.getOrElse(getHost(page), 0) + 1)
    }
  }
  override def toString = {
    var result = ""
    for(k <- (new TreeSet()(new ResultOrdering) ++ reachableDomains.toSet))
      if(k._1 != null) result += (k._1 + " " + k._2.toString + "\n")
    result
  }
}