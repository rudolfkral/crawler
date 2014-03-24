/**
 *
 */
package pl.edu.mimuw.crawler

/** Provides a simple page crawling mechanism.
 * 
 * ==Overview==
 * The main class to use is [[pl.edu.mimuw.crawler.ak334680.Crawler]]. It has 
 * to be provided with an exception handling class, extending [[pl.edu.mimuw.crawler.ak334680.CrawlerExceptionHandler]].
 * You can use the built-in exception handler, [[pl.edu.mimuw.crawler.ak334680.BasicExceptionHandler]]:
 * {{{
 * scala > val crawler = new Crawler(new BasicExceptionHandler)
 * }}}
 * 
 * However, the most important thing is that Crawler processes pages using
 * subclasses of [[pl.edu.mimuw.crawler.ak334680.CrawlerPageProcessor]].
 * They contain information about what to do with a single page.
 */
package object ak334680 {

}