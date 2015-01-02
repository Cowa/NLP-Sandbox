#!/usr/bin/env python
# coding: utf-8
# author: Brice Thomas

import sys, extract_named_entity as e

##
# Launch coreferences process
#
# @param namedEntities Array of named entities dict by files
# @param filesName     Array with files name (in same order than namedEntities)
#
# @return Create a new file .ref
#
def launchCoreferences(namedEntities, filesName):
    cursor = 0
    for named in namedEntities:
        computeCoreferences(named)

def computeCoreferences(namedEntities):
    uniqid = 1
    coreferences = []

    for key in namedEntities.keys():
        entities = namedEntities[key]
        toSet = list(set(entities))
        print(len(toSet))
        print(len(entities))
        print()

##
# Main program
#

texts = e.extractTextFromFolder(sys.argv[1])
filesName = e.extractFileName(sys.argv[1])
namedEntitiesByFiles = []

# Put all named entities by files into an array
for text in texts:
    namedEntitiesByFiles.append(e.extractNamedEntity(text))

launchCoreferences(namedEntitiesByFiles, filesName)
