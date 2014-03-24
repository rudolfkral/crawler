/**
 *
 */
package pl.edu.mimuw.crawler.ak334680

/** The most simple exception handler in the history of Computer Science. */
class BasicExceptionHandler extends CrawlerExceptionHandler {
  /** Prints exception message.
   *  
   *  @param ex exception
   */
  def handle(ex: Exception) = println(ex.getMessage())
}