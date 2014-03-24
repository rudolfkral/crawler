/**
 *
 */
package pl.edu.mimuw.crawler.ak334680

/** Handles exceptions thrown during page processing. */
trait CrawlerExceptionHandler {
  /** Handles an exception.
   *  
   *  @param ex exception
   */
  def handle(ex: Exception): Unit
}