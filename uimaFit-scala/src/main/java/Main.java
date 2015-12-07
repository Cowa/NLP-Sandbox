import uimaui.MauiModelResource;
import uimaui.MauiTopicExtractorAnnotator;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createDependencyAndBind;

public class Main {

  public static void main(String[] args) throws Exception {
    AnalysisEngineDescription aed1 = createEngineDescription(MauiTopicExtractorAnnotator.class);

    createDependencyAndBind(aed1, MauiModelResource.PARAM_MODEL(), MauiModelResource.class, MauiModelResource.PARAM_MODEL(),
      // Parameters
      MauiModelResource.PARAM_TRAIN_DIR(), "src/main/resources/data/term_assignment/train_fr",
      MauiModelResource.PARAM_VOCABULARY(), "src/main/resources/data/vocabularies/agrovoc_fr.rdf.gz",
      MauiModelResource.PARAM_FORMAT(), "skos",
      MauiModelResource.PARAM_LANGUAGE(), "fr",
      MauiModelResource.PARAM_ENCODING(), "UTF-8"
    );

    AnalysisEngine ae = createEngine(aed1,
      // Parameters
      MauiTopicExtractorAnnotator.PARAM_TEST_DIR(), "src/main/resources/data/term_assignment/test_fr",
      MauiTopicExtractorAnnotator.PARAM_FORMAT(), "skos",
      MauiTopicExtractorAnnotator.PARAM_LANGUAGE(), "fr",
      MauiTopicExtractorAnnotator.PARAM_NBOFTOPICS(), 10,
      MauiTopicExtractorAnnotator.PARAM_VOCABULARY(), "src/main/resources/data/vocabularies/agrovoc_fr.rdf.gz"
    );

    ae.process(ae.newJCas());
  }
}
