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
    Timer.executionTime {
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

      val availableEnglishTranslations = simpleDictionary.values.toList.flatten.groupBy(identity)

      println("\nTurning to Map and normalize...")
      val (mapSrcContextVector, mapTrgContextVector) = Timer.executionTime {
        (
          ContextVector.normalize(
            ContextVector.toMap(flatSrcContextVector)
              .filterKeys(k => specializedDictionary.contains(k) && simpleDictionary.contains(k))
              .mapValues(_.filter(_._2 > 1))
          ),
          ContextVector.normalize(
            ContextVector.toMap(flatTrgContextVector)
              .filterKeys(availableEnglishTranslations.contains)
              .mapValues(_.filter(_._2 > 1))
          )
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

      val realTranslatedContextVector = Timer.executionTime {
        specializedDictionary.map { case (word, _) =>
          (word, mapSrcContextVector.getOrElse(word, List()).map { case (x, i) =>
            ((simpleDictionary.getOrElse(x, List()) ++ cognateDictionary.getOrElse(x, List())).headOption.getOrElse("NOPE"), i)
          }.toMap)
        }
      }

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
        candidates.map { case (k, v) => v.map(x => (k, x, CosineSimilarity(x, realTranslatedContextVector(k)))) }.filterNot(_.isEmpty)
      )

      val chosenCandidates = similarities.map { x =>
        val max = x.maxBy(_._3)
        (max._1, max._2)
      }.toMap

      val containsRightTranslation = chosenCandidates.map { case (k, v) =>
        specializedDictionary.getOrElse(k, List()).intersect(v.toList.sortWith(_._2 > _._2).map(_._1)).nonEmpty
      }

      val totalToFind = containsRightTranslation.size
      val potentialAccuracy = containsRightTranslation.count(identity).toDouble / totalToFind

      println(s"\nTop INFINITE accuracy: ${potentialAccuracy * 100}%\n")

      // Top 10 to 500
      for (a <- 10 to 100 by 10) {
        top(a, chosenCandidates, totalToFind)
      }
    }
    /*val yolo = ContextVector.normalize(ContextVector.toMap(flatSrcContextVector)
      .filterKeys(specializedDictionary.contains)
      .mapValues(_.filter(_._2 > 2)))
    println(yolo("sein").sortWith(_._2 > _._2))*/
  }

  // Print top accuracy
  def top(n: Int, candidates: Map[String, Map[String, Double]], totalToFind: Int): Unit = {
    val t = candidates.map { case (k, v) =>
      specializedDictionary.getOrElse(k, List()).intersect(v.toList.sortWith(_._2 > _._2).map(_._1).take(n)).nonEmpty
    }

    println(s"Top $n accuracy: ${(t.count(identity).toDouble / totalToFind) * 100}%")
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
