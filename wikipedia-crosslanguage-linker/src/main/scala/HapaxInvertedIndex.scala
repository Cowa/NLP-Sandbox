import com.gilt.lucene._
import com.gilt.lucene.LuceneFieldHelpers._

import org.apache.lucene.document.Document

object HapaxInvertedIndex {
  def apply(documents: Vector[DocumentHapax]): HapaxInvertedIndex = {
    val index = new ReadableLuceneIndex
      with WritableLuceneIndex
      with LuceneStandardAnalyzer
      with DefaultFSLuceneDirectory

    documents.take(200).foreach { d =>
      println(s"Indexing ${d.document}...")

      val toIndex = new Document
      toIndex
        .addStoredOnlyField("id", d.document)
        .addIndexedStoredField("hapax", d.hapax.mkString(" "))

      index.addDocument(toIndex)
    }

    HapaxInvertedIndex(index)
    /*val doc = new Document
    doc.addIndexedStoredField("aField", "aValue")

    index.addDocument(doc)
    HapaxInvertedIndex(index)*/
  }
}

case class HapaxInvertedIndex(index: ReadableLuceneIndex) {
  def search(hapax: String): Iterable[Document] = {
    val queryParser = index.queryParserForDefaultField("id")
    val query = queryParser.parse("en-a.txt")
    val results = index.searchTopDocuments(query, 1)

    results

    /*val queryParser = index.queryParserForDefaultField("aField")
    val query = queryParser.parse("aValue")
    val results = index.searchTopDocuments(query, 100)
    results*/
  }
}
