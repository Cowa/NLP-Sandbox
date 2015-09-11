package com.github.cowa

import java.io.File

import com.github.cowa.models._
import com.github.cowa.domains._

object Main {
  def main(args: Array[String]) {
    val termsSource = new File("corpus/termer_source/corpus.lem")
    val termsTarget = new File("corpus/termer_target/corpus.lem")

    // And here goes pre-processing!
    // From unstructured to structured data
    val sources = RawTermerDataTransducer.rawTermerFileToHandyStruct(termsSource)
    val targets = RawTermerDataTransducer.rawTermerFileToHandyStruct(termsTarget)

    val listSourcesTerms = sources.flatMap(_.terms)
    val listTargetsTerms = targets.flatMap(_.terms)

    Alignment.findCognatesAndTransfugees(listSourcesTerms, listTargetsTerms)
  }
}
