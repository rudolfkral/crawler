/**
 *
 */
package pl.edu.mimuw.crawler.ak334680

import org.jsoup._
import org.jsoup.nodes._
import scala.collection.mutable.Queue
import scala.collection.mutable.Set
import java.net.URI

/** Contains algorithm for page processing.
 *
 * Crawler uses [[pl.edu.mimuw.crawler.ak334680.CrawlerPageProcessor.process]]
 * to handle each enqueued page. There are also several protected methods which
 * execute most common tasks.
 */
trait CrawlerPageProcessor {
  /** Retrieves host from given [[org.jsoup.nodes.Document]]
   *  
   *  @param page a [[org.jsoup.nodes.Document]] instance
   */
  protected def getHost(page: Document) = new URI(page.baseUri()).getHost()
  /** Retrieves host from given page address
   *  
   *  @param address page address
   */
  protected def getHost(address: String) = new URI(address).getHost()
  /** Returns true if page address is absolute.
   *  
   *  @param address page address
   */
  protected def isAbsolute(address: String) = new URI(address).isAbsolute()
  /** Returns a sequence of anchors from document's DOM tree.
   *  
   *  @param page a [[org.jsoup.nodes.Document]] instance
   *  ''It does '' '''NOT''' '' absolutize paths. If looking for so, use
   *  [[pl.mimuw.edu.crawler.ak334680.CrawlerPageProcessor.getAbsAnchors]]''
   */
  protected def getAnchors(page: Document): IndexedSeq[String] = {
    val anchors = page.select("a")
    for(i <- 0 until anchors.size()) yield anchors.get(i).attr("href")
  }
  /** Returns a sequence of absolutized anchors from document's DOM tree.
   *  
   *  @param page a [[org.jsoup.nodes.Document]] instance
   *  ''If you want addresses that are not absolutized, you are looking for
   *  [[pl.mimuw.edu.crawler.ak334680.CrawlerPageProcessor.getAnchors]]''
   */
  protected def getAbsAnchors(page: Document): IndexedSeq[String] = {
    val anchors = page.select("a")
    for(i <- 0 until anchors.size()) yield anchors.get(i).attr("abs:href")
  }
  /** Processes a page.
   *  
   *  @param page a [[org.jsoup.nodes.Document]] instance
   *  @param pageAdder procedure that allows you to enqueue a new page
   *  Crawler executes this method, passing each processed page and
   *  [[pl.mimuw.edu.crawler.ak334680.Crawler.addPage]] as arguments.
   */
  def process(page: Document, pageAdder: String => Unit): Unit
  // pageAdder allows enqueueing new pages during process
}