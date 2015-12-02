import maui.MauiModelResource;
import maui.MauiTopicExtractorAnnotator;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createDependencyAndBind;

public class Main {

  public static void main(String[] args) throws Exception {
    AnalysisEngineDescription aed1 = createEngineDescription(MauiTopicExtractorAnnotator.class);

    // Bind external resource to the aggregate
    createDependencyAndBind(aed1, "maui-model", MauiModelResource.class, "maui-model");

    // Check the external resource was injected
    AnalysisEngine ae = createEngine(aed1);
    ae.process(ae.newJCas());
  }
}
