package com.github.cowa

import java.io.File

import com.github.cowa.helpers._

object Main {
  def main(args: Array[String]) {
    val sourceTermsFile = new File("corpus/termer_source/corpus.lem")
    val targetTermsFile = new File("corpus/termer_target/corpus.lem")

    // From unstructured to structured data
    println("Structuring raw data...")

    val sources = TermerTransducer.rawTermerFileToHandyStruct(sourceTermsFile)
    val targets = TermerTransducer.rawTermerFileToHandyStruct(targetTermsFile)

    println("Done.\n")

    // And here goes pre-processing!
    println("Pre-processing...")

    val sourcesTerms = preprocessing(sources)
    val targetsTerms = preprocessing(targets)

    println(s"Source terms number: ${sourcesTerms.length}")
    println(s"Target terms number: ${targetsTerms.length}")

    println("Done.\n")

    println("Starting alignment...")

    // Starting alignment process...
    val aligned = Timer.executionTime { Alignment.findCognatesAndTransfugees(sourcesTerms, targetsTerms) }

    // Write results to CSV ;)
    AlignedWriter.writeToCsv(aligned("cognate").sortBy(_.w0), "result-cognate.csv")
    AlignedWriter.writeToCsv(aligned("transfugee").sortBy(_.w0), "result-transfugee.csv")
  }
}
