#!/usr/bin/env python
# coding: utf-8
# author: Brice Thomas

import string, re
import sys, extract_named_entity as e
import make_cluster_coreferences as m

def removeLower(text):
    return re.sub('[a-z.-]', '', text)

##
# Baseline + contained filter
#
# @param x The current list element
# @param y The base element to compare with
#
# @return true if same value or if contained in base element or if uppercase letter are the same
#
def baselineContainedUppercasedFilter(x, y):
    xc = x[1]
    yc = y[1]

    xs = removeLower(xc)
    ys = removeLower(yc)

    return (x[1] == y[1]) or (xc in yc) or (xs == ys)

##
# Main program
#
texts = e.extractTextFromFolder(sys.argv[1])
filesName = e.extractFileName(sys.argv[1])
namedEntitiesByFiles = []

# Put all named entities by files into an array
for text in texts:
    namedEntitiesByFiles.append(e.extractNamedEntity(text))

result = m.launchCoreferences(namedEntitiesByFiles, filesName, baselineContainedUppercasedFilter)
m.exportToRefFile(result)

print("\nExport done, see result.ref")
