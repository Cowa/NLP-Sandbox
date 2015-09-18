import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

import de.tudarmstadt.ukp.dkpro.core.berkeleyparser.BerkeleyParser;
import de.tudarmstadt.ukp.dkpro.core.io.conll.Conll2006Writer;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.languagetool.LanguageToolLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.maltparser.MaltParser;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpSegmenter;
import de.tudarmstadt.ukp.dkpro.core.snowball.SnowballStemmer;
import de.tudarmstadt.ukp.dkpro.core.testing.dumper.CasDumpWriter;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.resource.metadata.TypeSystemDescription;

import java.io.FileOutputStream;

public class Pipeline {

  public static void main(String[] args) throws Exception {
    runPipeline(
      createReaderDescription(TextReader.class, TextReader.PARAM_SOURCE_LOCATION, "document.txt", TextReader.PARAM_LANGUAGE, "en"),
      createEngineDescription(BerkeleyParser.class),
      createEngineDescription(SnowballStemmer.class),
      createEngineDescription(OpenNlpSegmenter.class),
      createEngineDescription(OpenNlpPosTagger.class),
      createEngineDescription(LanguageToolLemmatizer.class),
      createEngineDescription(MaltParser.class),
      createEngineDescription(CasDumpWriter.class, CasDumpWriter.PARAM_TARGET_LOCATION, "./document.dump"),
      createEngineDescription(Conll2006Writer.class, Conll2006Writer.PARAM_TARGET_LOCATION, ".")
    );

    TypeSystemDescription tsd = TypeSystemDescriptionFactory.createTypeSystemDescription();
    FileOutputStream os = new FileOutputStream("./GeneratedTypes.xml");
    tsd.toXML(os);
    os.close();
  }
}
