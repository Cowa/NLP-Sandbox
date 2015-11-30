package index

import scala.math._

object InvertedIndexer {
  type Index = Map[String, Entry]
  type TermFrequency = Map[String, Map[String, Double]]
  type DocumentFrequency = Map[String, Int]
  type InverseDocumentFrequency = Map[String, Double]
  type TermFrequencyInverseDocumentFrequency = Map[String, Map[String, Double]]

  def apply(documents: Vector[Document]): Unit = {
    val tf = termFrequency(documents)
    val df = documentsFrequency(documents)
    val idf = inverseDocumentFrequency(df, documents.size)

    val tfidf = termFrequencyInverseDocumentFrequency(tf, idf)
  }

  def termFrequency(documents: Vector[Document]): TermFrequency = {
    def step(document: Document) = {
      document.terms.groupBy(identity).mapValues(_.size.toDouble / document.terms.size)
    }
    documents.map(d => (d.id, step(d))).toMap
  }

  def documentsFrequency(documents: Vector[Document]): DocumentFrequency = {
    documents.flatMap(_.terms.distinct).groupBy(identity).mapValues(_.size)
  }

  def inverseDocumentFrequency(df: DocumentFrequency, nbDocuments: Int): InverseDocumentFrequency = {
    df.mapValues(freq => log10(nbDocuments.toDouble / freq))
  }

  def termFrequencyInverseDocumentFrequency(tf: TermFrequency, idf: InverseDocumentFrequency): TermFrequencyInverseDocumentFrequency = {
    idf.keys.map(term => (term, tf.keys.map(document => (document, tf(term)(document) * idf(term))).toMap)).toMap
  }
}

case class Entry(term: String, score: Double)

case class Document(id: String, terms: Vector[String])
