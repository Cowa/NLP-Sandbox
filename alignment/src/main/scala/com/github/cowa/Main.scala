package com.github.cowa

import java.io.File

import com.github.cowa.models._
import com.github.cowa.domains._

object Main {
  def main(args: Array[String]) {
    val sourceTermsFile = new File("corpus/termer_source/corpus.lem")
    val targetTermsFile = new File("corpus/termer_target/corpus.lem")

    // And here goes pre-processing!
    // From unstructured to structured data
    val sources = TermerTransducer.rawTermerFileToHandyStruct(sourceTermsFile)
    val targets = TermerTransducer.rawTermerFileToHandyStruct(targetTermsFile)

    val sourcesTerms = sources.flatMap(_.terms)
    val targetsTerms = targets.flatMap(_.terms)

    Alignment.findCognatesAndTransfugees(sourcesTerms, targetsTerms)
  }
}
