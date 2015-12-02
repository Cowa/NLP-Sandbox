package maui

import types.Topic

import com.entopix.maui.util.DataLoader
import com.entopix.maui.stemmers.FrenchStemmer
import com.entopix.maui.stopwords.StopwordsFrench
import com.entopix.maui.main.MauiTopicExtractorEnhanced

import org.apache.uima.jcas.JCas
import org.apache.uima.fit.descriptor.ExternalResource
import org.apache.uima.fit.component.JCasAnnotator_ImplBase

import scala.collection.JavaConverters._

class TopicExtractorAE extends JCasAnnotator_ImplBase {
  @ExternalResource(key = "maui-model")
  var mauiModel: MauiModelResource = null

  def process(jcas: JCas): Unit = {
    val topicExtractor = new MauiTopicExtractorEnhanced

    ///
    /// PARAMETERS @todo Make it configurable
    ///
    val testDir = "src/main/resources/data/term_assignment/test_fr"
    val vocabulary = "src/main/resources/data/vocabularies/agrovoc_fr.rdf.gz"
    val format = "skos"
    val language = "fr"
    val stemmer = new FrenchStemmer
    val stopwords = new StopwordsFrench
    val numTopicsToExtract = 10

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
    topics.foreach(x => x.getTopics.asScala.foreach(l => println(l.getTitle)))

    val extractedTopics = topics.flatMap(d => d.getTopics.asScala.toList.map { t =>
      val topic = new Topic(jcas, 1, 1)
      topic.setTitle(t.getTitle)
      topic.setCorrect(t.isCorrect)
      topic.setId(t.getId)
      topic.setProbability(t.getProbability)

      topic
    })

    extractedTopics.foreach(_.addToIndexes())
  }
}
