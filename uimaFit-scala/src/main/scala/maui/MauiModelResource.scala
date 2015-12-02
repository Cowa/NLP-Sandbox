package maui

import com.entopix.maui.util.DataLoader
import com.entopix.maui.filters.MauiFilter
import com.entopix.maui.main.MauiModelBuilder
import com.entopix.maui.stemmers.FrenchStemmer
import com.entopix.maui.stopwords.StopwordsFrench

import org.apache.uima.resource.{DataResource, SharedResourceObject}

class MauiModelResource extends SharedResourceObject {
  var model: MauiFilter = null

  def load(aData: DataResource): Unit = {
    val modelBuilder = new MauiModelBuilder()

    ///
    /// PARAMETERS @todo Make it configurable
    ///
    val trainDir = "src/main/resources/data/term_assignment/train_fr"
    val vocabulary = "src/main/resources/data/vocabularies/agrovoc_fr.rdf.gz"

    val format = "skos"
    val language = "fr"
    val encoding = "UTF-8"
    val stemmer = new FrenchStemmer
    val stopwords = new StopwordsFrench

    ///
    /// SETTINGS
    ///
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

    ///
    /// BUILD
    ///
    model = modelBuilder.buildModel(DataLoader.loadTestDocuments(modelBuilder.inputDirectoryName))
  }
}