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
    result = []
    print(namedEntities)
    for named in namedEntities:
        result.append(computeCoreferences(named))

    #print(result)

##
# Compute coreferences of the given named entities dict
#
# @param namedEntities A dictionnary (key -> type, values -> array of named entities with index)
#                      Example: {'LOCATION': [(1, 'Hong-Kong'), (2, 'Mars'), (3, 'Nantes')]}
#
def computeCoreferences(wholeNamedEntities):
    coreferences = []

    for key in wholeNamedEntities.keys():
        entities = wholeNamedEntities[key]
        coreferences.append(actuallyComputeCoreferences(entities))

    return coreferences
##
# Actually compute coreferences
#
# @param namedEntities Array of values with index
#                      Example: [(1, 'Hong-Kong'), (2, 'Mars'), (3, 'Mars'), (4, 'Nantes')]
#
# @return The coreferences in a matrix
#         Example: [
#                    [ (2, 'Mars'), (3, 'Mars') ],
#                    [ (1, 'Hong-Kong') ],
#                    [ (4, 'Nantes') ]
#                  ]
#
def actuallyComputeCoreferences(namedEntities):
    return [[namedEntities[0], namedEntities[0]], [namedEntities[0], namedEntities[0]]]

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
