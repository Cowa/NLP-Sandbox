package com.github.cowa.nlp

import com.github.cowa.models.Aligned
import com.github.cowa.helpers.FileWriter

object AlignedWriter {

  def toCSVFormat(alignedData: List[Aligned]): String = {
    alignedData.map(x => s"${x.w0};${x.w1}").mkString("\n")
  }

  def writeToCsv(alignedData: List[Aligned], fileName: String = "result.csv") = {
    FileWriter.write(fileName, toCSVFormat(alignedData))
  }
}