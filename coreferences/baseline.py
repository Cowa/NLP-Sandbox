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
# @return Corefences clusters by files, an awesome dict structured this way :
#         {
#           fileName: [ [cluster1], [cluster2], [cluster3] ]
#         }
#
def launchCoreferences(namedEntities, filesName):
    result = dict()
    cursor = 0

    for named in namedEntities:
        result[filesName[cursor]] = computeCoreferences(named)
        cursor = cursor + 1

    print(result)

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
        coreferences.extend(actuallyComputeCoreferences(entities))

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
    cluster = []

    for entity in namedEntities:
        result = betterFilter(simpleFilter, namedEntities, entity)

        if result not in cluster:
            cluster.append(betterFilter(simpleFilter, namedEntities, entity))

    return cluster

##
# Baseline filter
#
# @param x The current list element
# @param y The base element to compare with
#
# @return true if same value
#
def simpleFilter(x, y):
    return x[1] == y[1]

##
# A better (for me) list filter
#
def betterFilter(callback, list, item):
    l = []

    for element in list:
       if callback(element, item): l.append(element)

    return l

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
