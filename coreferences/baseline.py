#!/usr/bin/env python
# coding: utf-8
# author: Brice Thomas

import sys, extract_named_entity as e
import make_cluster_coreferences as m

##
# Baseline filter
#
# @param x The current list element
# @param y The base element to compare with
#
# @return true if same value
#
def baseLineFilter(x, y):
    return x[1] == y[1]

##
# Main program
#
texts = e.extractTextFromFolder(sys.argv[1])
filesName = e.extractFileName(sys.argv[1])
namedEntitiesByFiles = []

# Put all named entities by files into an array
for text in texts:
    namedEntitiesByFiles.append(e.extractNamedEntity(text))

m.launchCoreferences(namedEntitiesByFiles, filesName, baseLineFilter)
