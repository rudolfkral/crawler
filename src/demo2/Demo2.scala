package demo2

import pl.edu.mimuw.crawler.ak334680._
import java.io.File
/** Second Crawler demonstration program.
 *  
 *  Using first parameter as an offline host page and second parameter as the
 *  scanning depth, counts reachable local pages.
 */
object Demo2 extends App {
  if(args.length != 2) {
    println("You need to pass two parameters.")
  } else {
    val start = new File(args(0))
    var crawler = new Crawler(new BasicExceptionHandler)
    var proc = new LocalWebCrawler(args(1).toInt)
    crawler.addPageProcessor(proc)
    crawler.addPage(start.getCanonicalPath())
    crawler.process
    println(proc.toString)
  }
}