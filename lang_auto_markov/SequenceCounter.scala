package me.verticale

case class SequenceCounter() {

  /** Count the occurrences of sub-sequences
    *
    * @param toFind elements to find
    * @param inside elements to search from
    * @return Number of occurrences of sub-sequences
    */
  def count(toFind: Array[Element], inside: Array[Element]): Int = {
    val matrix = Array.ofDim[Int](toFind.length, inside.length)

    for (i <- 0 until toFind.length) {
      for (j <- 0 until inside.length) (toFind(i), inside(j)) match {
        case (x, y) if (x != y) => {
          if (j != 0)
            matrix(i)(j) = matrix(i)(j - 1)
        }
        case (x, y) => {
          if (j != 0 && i != 0)
            matrix(i)(j) = matrix(i - 1)(j - 1) + matrix(i)(j - 1)
          else if (i == 0 && j != 0)
            matrix(i)(j) = matrix(i)(j - 1) + 1
          else if (i == 0 && j == 0)
            matrix(i)(j) = 1
        }
      }
    }
    matrix(toFind.length - 1)(inside.length - 1)
  }
}
