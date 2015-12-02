import scala.io.Source

object InvertedIndex {

  /**
   * Build an inverted index from a file with this format:
   *   doc0 w0 w1 ...
   *   doc1 w2 w4 ...
   * Each elements of a line is separated by whitespaces
   *
   * @param path Path to the file containing data to invert
   *
   * @return An inverted index
   *   w0 -> doc0 doc2 ...
   *   w1 -> doc0 doc5 ...
   */
  def apply(path: String): Map[String, Vector[String]] = {
    val file = Source.fromFile(path)
    val lines = file.getLines().toList
    file.close()

    lines.map(_.split(" "))
      .flatMap(x => x.drop(1).map(y => (y, x(0))))
      .groupBy(_._1)
      .map(p => (p._1, p._2.map(_._2).toVector))
  }
}
