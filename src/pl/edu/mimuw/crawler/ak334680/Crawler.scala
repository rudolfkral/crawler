/**
 *
 */
package pl.edu.mimuw.crawler.ak334680

import org.jsoup._
import scala.collection.mutable.Queue
import scala.collection.mutable.Set
import java.io.File
import java.net.URI
import scala.annotation.tailrec

/** Uses one or more CrawlerPageProcessors to crawl through the
 * pages listed in queues.
 * 
 * @constructor create a new Crawler
 * @param exceptionHandler class which handles exceptions thrown during process
 * 
 */
class Crawler(exceptionHandler: CrawlerExceptionHandler) {
  private var pagesToProcess: Queue[String] = Queue.empty
  private var pagesProcessed: Set[String] = Set.empty
  private var pageProcessors: Queue[CrawlerPageProcessor] = Queue.empty
  private def isLocal(path: String): Boolean = 
    new URI(path).getHost() == null
  private def absolutePath(path: String): String = {
    val result = if(isLocal(path)) {
      if(!new File(path).exists() || new File(path).isDirectory())
        new File(path, "index.html").getCanonicalPath()
      else
        new File(path).getCanonicalPath()
    } else {
      path
    }
    val hashPos = result.indexOf("#")
    if(hashPos == -1) result else result.dropRight(result.length() - hashPos)
  }
  /** Adds page, if not enqueued.
   *  
   *  @param address page address
   */
  def addPage(address: String) = {
    val path = absolutePath(address)
    if(!(pagesToProcess.contains(path) || pagesProcessed.contains(path)))
        pagesToProcess += path
  }
  /** Removes page from queue.
   *  
   *  @param address page address
   */
  def removePage(address: String) = {
    val path = absolutePath(address)
    pagesToProcess.dequeueAll(p => p == path)
  }
  /** Ensures that page won't be visited.
   *  
   *  @param address page address
   */
  def avoidPage(address: String) = {
    val path = absolutePath(address)
    if(!pagesProcessed.contains(path))
      pagesProcessed += path
    removePage(address)
  }
  /** Adds a page processor.
   *  
   *  @param proc page processor
   */
  def addPageProcessor(proc: CrawlerPageProcessor) = pageProcessors.enqueue(proc)
  /** Removes a page processor.
   *  
   *  @param proc page processor.
   */
  def removePageProcessor(proc: CrawlerPageProcessor): Unit =
    pageProcessors.dequeueFirst(p => p == proc)
  /** Clears Crawler's memory.
   *  
   *  Deletes all pages from the queue, and also all information about which
   *  pages were processed in the past.
   */
  def clear = {
    pagesToProcess.clear
    pagesProcessed.clear
  }
  private def processOne(address: String): Unit = {
    if(pagesProcessed.contains(address)) return
    try {
      val page = if(isLocal(address))
        Jsoup.parse(new File(address), "UTF-8") else
        Jsoup.connect(address).get()
      pageProcessors.foreach((a: CrawlerPageProcessor) =>
        a.process(page, addPage))
    } catch {
      case ex: Exception => exceptionHandler.handle(ex)
    }
    pagesProcessed += address
  }
  /** Processes a single page.
   *  
   *  @param address page address
   *  Processes the page, invocating [[pl.mimuw.edu.crawler.ak334680.CrawlerPageProcessor.process]]
   *  of each page processor.
   */
  def processPage(address: String) = processOne(absolutePath(address))
  /** Processes all enqueued pages.
   *  
   *  See also: [[pl.mimuw.edu.crawler.ak334680.Crawler.processAll]]
   */
  def process = processAll
  /** Processes all enqueued pages.
   *  
   *  See also: [[pl.mimuw.edu.crawler.ak334680.Crawler.process]]
   */
  def processAll = {
    @tailrec
    def processRec: Unit =
      if(pagesToProcess.isEmpty) return else {
        processOne(pagesToProcess.dequeue)
        processRec
      }
    processRec
  }
  
}