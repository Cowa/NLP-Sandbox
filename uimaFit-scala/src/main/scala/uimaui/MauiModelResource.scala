package uimaui

import com.entopix.maui.util.DataLoader
import com.entopix.maui.filters.MauiFilter
import com.entopix.maui.main.MauiModelBuilder
import com.entopix.maui.stemmers.FrenchStemmer
import com.entopix.maui.stopwords.StopwordsFrench

import org.apache.uima.fit.descriptor.ConfigurationParameter
import org.apache.uima.resource.{DataResource, SharedResourceObject}
import org.apache.uima.fit.component.initialize.ConfigurationParameterInitializer

class MauiModelResource extends SharedResourceObject {
  var model: MauiFilter = null

  @ConfigurationParameter(name=MauiModelResource.PARAM_TRAIN_DIR, mandatory = true)
  var trainDir = ""

  @ConfigurationParameter(name=MauiModelResource.PARAM_VOCABULARY, mandatory = true)
  var vocabulary = ""

  @ConfigurationParameter(name=MauiModelResource.PARAM_FORMAT, mandatory = true)
  var format = ""

  @ConfigurationParameter(name=MauiModelResource.PARAM_ENCODING, mandatory = true)
  var encoding = ""

  @ConfigurationParameter(name=MauiModelResource.PARAM_LANGUAGE, mandatory = true)
  var language = ""

  def load(aData: DataResource): Unit = {
    ConfigurationParameterInitializer.initialize(this, aData)

    val modelBuilder = new MauiModelBuilder()

    // @todo nice to have: make stemmer & stopword as parameters
    val stemmer = new FrenchStemmer
    val stopwords = new StopwordsFrench

    modelBuilder.inputDirectoryName = trainDir
    modelBuilder.vocabularyFormat = format
    modelBuilder.vocabularyName = vocabulary
    modelBuilder.stemmer = stemmer
    modelBuilder.stopwords = stopwords
    modelBuilder.documentLanguage = language
    modelBuilder.documentEncoding = encoding
    modelBuilder.serialize = true

    modelBuilder.setBasicFeatures(true)
    modelBuilder.setKeyphrasenessFeature(true)
    modelBuilder.setFrequencyFeatures(false)
    modelBuilder.setPositionsFeatures(true)
    modelBuilder.setLengthFeature(true)
    modelBuilder.setThesaurusFeatures(true)

    model = modelBuilder.buildModel(DataLoader.loadTestDocuments(modelBuilder.inputDirectoryName))
  }
}

object MauiModelResource {
  final val PARAM_TRAIN_DIR = "mauiTrainDir"
  final val PARAM_VOCABULARY = "mauiVocabulary"
  final val PARAM_FORMAT = "mauiFormat"
  final val PARAM_LANGUAGE = "mauiLang"
  final val PARAM_ENCODING = "mauiEncoding"
  final val PARAM_MODEL = "mauiModel"
}
