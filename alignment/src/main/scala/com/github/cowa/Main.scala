package com.github.cowa

import java.io.File

import com.github.cowa.helpers._
import com.github.cowa.nlp._

object Main {
  def main(args: Array[String]) {
    val sourceTermsFile = new File("corpus/termer_source/corpus.lem")
    val targetTermsFile = new File("corpus/termer_target/corpus.lem")

    // From unstructured to structured data
    println("Structuring raw data...")

    val sources = TermsExtractor.rawTermerFileToHandyStruct(sourceTermsFile)
    val targets = TermsExtractor.rawTermerFileToHandyStruct(targetTermsFile)

    println("Done.\n")

    // And here goes pre-processing!
    println("Pre-processing...")

    val sourcesTerms = Preprocessing(sources)
    val targetsTerms = Preprocessing(targets)

    println(s"Source terms number: ${sourcesTerms.length}")
    println(s"Target terms number: ${targetsTerms.length}")

    println("Done.\n")

    println("Starting alignment...")

    // Starting alignment process...
    val aligned = Timer.executionTime { Alignment.findCognates(sourcesTerms, targetsTerms) }

    println(s"Cognates number: ${aligned.length}")

    // Write results to CSV ;)
    AlignedWriter.writeToCsv(aligned.sortBy(_.w0), "result-cognate.csv")
  }
}
