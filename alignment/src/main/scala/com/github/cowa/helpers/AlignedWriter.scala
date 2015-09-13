package com.github.cowa.helpers

object AlignedWriter {
  import com.github.cowa.models.Aligned

  def toCSVFormat(alignedData: List[Aligned]): String = {
    alignedData.map(x => s"${x.word0};${x.word1}").mkString("\n")
  }
  
  def writeToCsv(alignedData: List[Aligned], fileName: String = "result.csv") = {
    FileWriter.write(fileName, toCSVFormat(alignedData))
  }
}

object FileWriter {
  import java.io.PrintWriter

  /**
   * Write to disk
   *
   * @param fileName File's name
   * @param data Data to put in the file
   */
  def write(fileName: String, data: String) = {
    new PrintWriter(fileName) { write(data); close() }
  }
}
