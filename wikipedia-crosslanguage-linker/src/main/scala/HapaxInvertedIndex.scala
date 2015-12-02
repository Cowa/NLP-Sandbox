import scala.language._
import com.gilt.lucene._
import com.gilt.lucene.LuceneText._
import com.gilt.lucene.LuceneFieldHelpers._
import org.apache.lucene.document.Document

object HapaxInvertedIndex {
  def apply(documents: Vector[DocumentHapax]): HapaxInvertedIndex = {
    val index = new ReadableLuceneIndex
      with WritableLuceneIndex
      with LuceneStandardAnalyzer
      with DefaultFSLuceneDirectory

    documents.take(100).foreach { d =>
      println(s"Indexing ${d.document}...")

      val toIndex = new Document
      toIndex
        .addIndexedStoredField("id", d.document)
        .addIndexedStoredField("hapax", "incident".toLuceneText)

      index.addDocument(toIndex)
    }

    HapaxInvertedIndex(index)
  }
}

case class HapaxInvertedIndex(index: ReadableLuceneIndex) {
  def search(hapax: String): Iterable[Document] = {
    val queryParser = index.queryParserForDefaultField("hapax")
    val query = queryParser.parse(hapax)
    val results = index.searchTopDocuments(query, 1000)

    results
  }
}
