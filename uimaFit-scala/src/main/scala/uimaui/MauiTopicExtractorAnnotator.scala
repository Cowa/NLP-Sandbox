package uimaui

import com.entopix.maui.util.DataLoader
import com.entopix.maui.stemmers.FrenchStemmer
import com.entopix.maui.stopwords.StopwordsFrench
import com.entopix.maui.main.MauiTopicExtractorEnhanced

import org.apache.uima.jcas.JCas
import org.apache.uima.fit.component.JCasAnnotator_ImplBase
import org.apache.uima.fit.descriptor.{ConfigurationParameter, ExternalResource}

import scala.collection.JavaConverters._

class MauiTopicExtractorAnnotator extends JCasAnnotator_ImplBase {
  @ExternalResource(key = MauiModelResource.PARAM_MODEL)
  var mauiModel: MauiModelResource = null

  @ConfigurationParameter(name=MauiTopicExtractorAnnotator.PARAM_TEST_DIR, mandatory = true)
  var testDir = ""

  @ConfigurationParameter(name=MauiTopicExtractorAnnotator.PARAM_VOCABULARY, mandatory = true)
  var vocabulary = ""

  @ConfigurationParameter(name=MauiTopicExtractorAnnotator.PARAM_FORMAT, mandatory = true)
  var format = ""

  @ConfigurationParameter(name=MauiTopicExtractorAnnotator.PARAM_LANGUAGE, mandatory = true)
  var language = ""

  @ConfigurationParameter(name=MauiTopicExtractorAnnotator.PARAM_NBOFTOPICS, mandatory = true)
  var numTopicsToExtract = 0

  def process(jcas: JCas): Unit = {
    val topicExtractor = new MauiTopicExtractorEnhanced

    // @todo nice to have: make stemmer & stopword as parameters
    val stemmer = new FrenchStemmer
    val stopwords = new StopwordsFrench

    topicExtractor.inputDirectoryName = testDir
    topicExtractor.vocabularyName = vocabulary
    topicExtractor.vocabularyFormat = format
    topicExtractor.stemmer = stemmer
    topicExtractor.stopwords = stopwords
    topicExtractor.documentLanguage = language
    topicExtractor.setTopicsPerDocument(numTopicsToExtract)
    topicExtractor.cutOffTopicProbability = 0.0
    topicExtractor.serialize = true
    topicExtractor.setModel(mauiModel.model)

    val topics = topicExtractor.extractTopics(DataLoader.loadTestDocuments(testDir)).asScala.toList

    val extractedTopics = topics.flatMap(d => d.getTopics.asScala.toList.map { t =>
      val topic = new Topic(jcas, 0, 0)
      topic.setTitle(t.getTitle)
      topic.setCorrect(t.isCorrect)
      topic.setId(t.getId)
      topic.setProbability(t.getProbability)

      topic
    })

    extractedTopics.foreach(_.addToIndexes())

    // Display topics title
    extractedTopics.foreach(t => println(t.getTitle))
  }
}

object MauiTopicExtractorAnnotator {
  final val PARAM_TEST_DIR = "mauiTestDir"
  final val PARAM_VOCABULARY = "mauiVocabulary"
  final val PARAM_FORMAT = "mauiFormat"
  final val PARAM_LANGUAGE = "mauiLang"
  final val PARAM_NBOFTOPICS = "mauiNbOfTopics"
}
