package com.github.cowa

import java.io.File
import com.github.cowa.nlp._
import com.github.cowa.helpers._

object Main {
  lazy val simpleDictionary = SimpleDictionary.get("src/main/resources/dictionary-fr-en.csv")
  lazy val cognateDictionary = CognateDictionary.get("src/main/resources/cognates.csv")
  lazy val specializedDictionary = SpecializedDictionary.get("src/main/resources/ts.xml")

  lazy val sourceTermsFile = new File("corpus/termer_source/corpus.lem")
  lazy val targetTermsFile = new File("corpus/termer_target/corpus.lem")

  def main(args: Array[String]): Unit = {
    contextVectorTranslator()
  }

  // Translate with context vector
  def contextVectorTranslator() {
    println("Loading and structuring...")
    val (sources, targets) = Timer.executionTime {
      (TermsExtractor.rawTermerFileToHandyStruct(sourceTermsFile), TermsExtractor.rawTermerFileToHandyStruct(targetTermsFile))
    }

    println("\nPre-processing structured data...")
    val (srcTrms, trgTrms) = Timer.executionTime {
      (sources.map(Preprocessing(_)), targets.map(Preprocessing(_)))
    }

    // Build simple context vector
    println("\nCreating context vectors...")
    val (srcContextVector, trgContextVector) = Timer.executionTime {
      (srcTrms.map(x => ContextVector.build(x.map(_.lemme), 7)), trgTrms.map(x => ContextVector.build(x.map(_.lemme), 7)))
    }

    // Add frequencies to context vectors
    println("\nAdding frequencies...")
    val (flatSrcContextVector, flatTrgContextVector) = Timer.executionTime {
      (ContextVector.addFrequencies(srcContextVector.flatten), ContextVector.addFrequencies(trgContextVector.flatten))
    }

    println("\nTurning to Map...")
    val (mapSrcContextVector, mapTrgContextVector) = Timer.executionTime {
      (
        ContextVector.toMap(flatSrcContextVector).filterKeys(specializedDictionary.contains)
          // a word must appear more than twice
          .mapValues(_.filter(_._2 > 2))
        ,
        ContextVector.toMap(flatTrgContextVector).mapValues(_.filter(_._2 > 2))//.filterKeys(specializedDictionary.contains)
      )
    }

    println("\nTranslating context vectors...")
    val translatedContextVector = Timer.executionTime {
      specializedDictionary.map { case (word, _) =>
        (word, mapSrcContextVector.getOrElse(word, List()).map { case (x, i) =>
          (x, (simpleDictionary.getOrElse(x, List()) ++ cognateDictionary.getOrElse(x, List())).groupBy(identity).mapValues(_.size))
        })
      }
    }

    /*
    val mammoCandidates = translatedContextVector("mammographie").map { case (k, contextTranslated) =>
      contextTranslated.flatMap { case (wordTranslated, freq) =>
        mapTrgContextVector.getOrElse(wordTranslated, List())
      }
    }.filterNot(_.isEmpty).distinct

    //println(mammoCandidates.mkString("\n\n"))

    val mammoSimilarities = mammoCandidates.combinations(2).map(x => (x.head, CosineSimilarity(x.head, x.last))).toList
    println(mammoSimilarities.maxBy(_._2))
    */

    println("\nSearching candidates vectors...")
    val candidates = Timer.executionTime {
      translatedContextVector.map { case (word, v) =>
        (word, v.map { case (_, contextTranslated) =>
          contextTranslated.flatMap { case (wordTranslated, freq) =>
            mapTrgContextVector.getOrElse(wordTranslated, List())
          }
        }.filterNot(_.isEmpty).distinct)
      }
    }

    println("\nComputing cosine similarities...")
    val similarities = Timer.executionTime(
      candidates.map { case (k, v) => v.combinations(2).map(x => (k, x.head, CosineSimilarity(x.head, x.last))).toList }.filterNot(_.isEmpty)
    )

    val chosenCandidates = similarities.map { x =>
      val max = x.maxBy(_._3)
      (max._1, max._2)
    }.toMap

    //println(chosenCandidates.mkString("\n"))

    val containsRightTranslation = chosenCandidates.map { case (k, v) =>
      specializedDictionary.getOrElse(k, List()).intersect(v.toList.sortWith(_._2 > _._2).map(_._1)).nonEmpty
    }

    val potentialAccuracy = containsRightTranslation.count(identity).toDouble / containsRightTranslation.size

    println(s"\nPotential max accuracy: ${potentialAccuracy * 100}%")

    // TOP 10
    val top10 = chosenCandidates.map { case (k, v) =>
      specializedDictionary.getOrElse(k, List()).intersect(v.toList.sortWith(_._2 > _._2).map(_._1).take(10)).nonEmpty
    }

    val top10Accuracy = top10.count(identity).toDouble / containsRightTranslation.size
    println(s"\nTop 10 accuracy: ${top10Accuracy * 100}%")

    // TOP 20
    val top20 = chosenCandidates.map { case (k, v) =>
      specializedDictionary.getOrElse(k, List()).intersect(v.toList.sortWith(_._2 > _._2).map(_._1).take(20)).nonEmpty
    }

    val top20Accuracy = top20.count(identity).toDouble / containsRightTranslation.size
    println(s"\nTop 20 accuracy: ${top20Accuracy * 100}%")

    // TOP 30
    val top30 = chosenCandidates.map { case (k, v) =>
      specializedDictionary.getOrElse(k, List()).intersect(v.toList.sortWith(_._2 > _._2).map(_._1).take(30)).nonEmpty
    }

    val top30Accuracy = top30.count(identity).toDouble / containsRightTranslation.size
    println(s"\nTop 30 accuracy: ${top30Accuracy * 100}%")

    // TOP 100
    val top100 = chosenCandidates.map { case (k, v) =>
      specializedDictionary.getOrElse(k, List()).intersect(v.toList.sortWith(_._2 > _._2).map(_._1).take(100)).nonEmpty
    }

    val top100Accuracy = top100.count(identity).toDouble / containsRightTranslation.size
    println(s"\nTop 100 accuracy: ${top100Accuracy * 100}%")
  }

  // Generate cognates
  def cognates() {
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

    println("Starting cognates alignment...")

    // Starting alignment process...
    val aligned = Timer.executionTime {
      AlignmentCognate(sourcesTerms, targetsTerms)
    }.filter(x => simpleDictionary.contains(x.w0))

    println(s"Cognates number: ${aligned.length}")

    // Write results to CSV ;)
    AlignedWriter.writeToCsv(aligned.sortBy(_.w0), "result-cognate.csv")
  }
}
