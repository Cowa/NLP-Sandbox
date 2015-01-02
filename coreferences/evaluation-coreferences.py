#!/usr/bin/env python
# -*- encoding: utf-8 -*-

import codecs, sys

##
# Creates a dictionary including the list of the coreference clusters, for a text
# used as a key.
#
# @param  lines A list of lines, each one representing a text and its coreference
#               clusters.
# @return A dictionary with the name of a text as a key and the list of its
#         keywords as the value.
def lines_2_dict(lines):
  dic = {}
  for line in lines:
    if not line == "":
      filename_and_clusters = line.split("\t")
      filename = filename_and_clusters[0].rsplit(".", 1)[0]
      clusters = filename_and_clusters[1].split(";")
      dic[filename] = clusters
  return dic

##
# Compares two sets of clusters.
#
# @param  ref_clusters The set of reference clusters.
# @param  res_clusters The set of extracted clusters.
# @return A tuple containing the number of assignated clusters, the number of
#         extracted clusters and the number of correctly assigned clusters.
def comparison(ref_clusters, res_clusters):
  nb_ref = len(ref_clusters)
  nb_res = len(res_clusters)
  nb_match = 0

  for i in range(nb_ref):
    if res_clusters.count(ref_clusters[i]) > 0:
      nb_match += 1

  return (nb_ref, nb_res, nb_match)

##
# Evaluates results according to references.
#
#@param   ref_file The file containing the reference results.
#@param   res_file The file containing the extraction results.
#@return  The evaluation measure (precision, recall, f1-measure).
def evaluation(ref_file, res_file):
  references_file = codecs.open(ref_file, "r", "utf-8")
  results_file = codecs.open(res_file, "r", "utf-8")
  references = references_file.read().split("\n")
  results = results_file.read().split("\n")
  references_file.close()
  results_file.close()
  ref_dict = lines_2_dict(references)
  res_dict = lines_2_dict(results)
  measures = {}
  average_precision = 0.0
  average_recall = 0.0
  average_f1_measure = 0.0

  # computes the measures
  for key in ref_dict.keys():
    if res_dict.has_key(key):
      nb_ref, nb_res, nb_match = comparison(ref_dict[key], res_dict[key])
      precision = float(nb_match) / float(nb_res)
      recall = float(nb_match) / float(nb_ref)
      if not (precision + recall) == 0:
        f1_measure = (2.0 * precision * recall) / (precision + recall)
      else:
        f1_measure = 0.0
      measures[key] = (precision, recall, f1_measure)
      average_precision += precision
      average_recall += recall
      average_f1_measure += f1_measure
  nb_values = len(measures)
  average_precision = (average_precision / nb_values) * 100
  average_recall = (average_recall / nb_values) * 100
  average_f1_measure = (average_f1_measure / nb_values) * 100

  return (average_precision, average_recall, average_f1_measure)

##
# Calls the evaluation function with the given reference and result files.
#
print evaluation(sys.argv[1], sys.argv[2])
