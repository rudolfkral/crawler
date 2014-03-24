/**
 *
 */
package demo2

import pl.edu.mimuw.crawler.ak334680.CrawlerPageProcessor
import org.jsoup.nodes.Document
import scala.collection.mutable.Map
import java.io.File

/** LocalWebCrawler counts all pages that are reachable in n steps from pages
 * which are present in Crawler's queue.
 * 
 * @param n the maximum length of the path from start page to each other.
 * 
 */
class LocalWebCrawler(n: Integer) extends CrawlerPageProcessor {
  private var pageLevel: Map[String, Integer] = Map.empty
  private var pages: Integer = 0
  /** Processes a single page.
   *  
   *  @param page a [[org.jsuop.nodes.Document]] instance
   *  @param pageAdder a method allowing enqueueing new pages
   *  If page is reachable in n steps from start page, adds its "local"
   *  anchors. If not, do nothing.
   */
  def process(page: Document, pageAdder: String => Unit): Unit = {
    if(pageLevel.contains(page.baseUri()) && pageLevel(page.baseUri()) > n)
      return
    pages += 1
    val links = getAnchors(page).filter(p => !isAbsolute(p))
    for(link <- links) {
      val newPath = new File(page.baseUri()).getParent()
      val newLink = new File(newPath, link).getCanonicalPath()
      if(!pageLevel.contains(newLink)) {
        pageLevel.put(newLink, if(pageLevel.contains(page.baseUri()))
          (pageLevel(page.baseUri()) + 1) else 1)
        pageAdder(newLink)
      }
    }
    pageLevel -= page.baseUri()
  }
  override def toString = pages.toString
}