#!/usr/bin/env python
# coding: utf-8
# author: Brice Thomas

import sys, extract_named_entity as e

##
# Launch coreferences process
#
# @param namedEntities Array of named entities dict by files
# @param filesName     Array with files name (in same order than namedEntities)
# @param filterWay     Filter function (the way how coreferences will be possible)
#
# @return Corefences clusters by files, an awesome dict structured this way :
#         {
#           fileName: [ [cluster1], [cluster2], [cluster3] ]
#         }
#
def launchCoreferences(namedEntities, filesName, filterWay):
    result = dict()
    cursor = 0

    for named in namedEntities:
        result[filesName[cursor]] = computeCoreferences(named, filterWay)
        cursor = cursor + 1

    print(result)

##
# Compute coreferences of the given named entities dict
#
# @param namedEntities A dictionnary (key -> type, values -> array of named entities with index)
#                      Example: {'LOCATION': [(1, 'Hong-Kong'), (2, 'Mars'), (3, 'Nantes')]}
# @param filterWay     Filter function (the way how coreferences will be possible)
#
def computeCoreferences(wholeNamedEntities, filterWay):
    coreferences = []

    for key in wholeNamedEntities.keys():
        entities = wholeNamedEntities[key]
        coreferences.extend(actuallyComputeCoreferences(entities, filterWay))

    return coreferences

##
# Actually compute coreferences
#
# @param namedEntities Array of values with index
#                      Example: [(1, 'Hong-Kong'), (2, 'Mars'), (3, 'Mars'), (4, 'Nantes')]
# @param filterWay     Filter function (the way how coreferences will be possible)
#
# @return The coreferences in a matrix
#         Example: [
#                    [ (2, 'Mars'), (3, 'Mars') ],
#                    [ (1, 'Hong-Kong') ],
#                    [ (4, 'Nantes') ]
#                  ]
#
def actuallyComputeCoreferences(namedEntities, filterWay):
    cluster = []

    for entity in namedEntities:
        result = betterFilter(filterWay, namedEntities, entity)

        if result not in cluster:
            cluster.append(result)

    return cluster

##
# A better (for me) list filter
#
# @param callback Callback function with arity 2
# @param list     List to filter
# @param item     Base element for comparison
#
# @return The filtered list
#
def betterFilter(callback, list, item):
    l = []

    for element in list:
       if callback(element, item): l.append(element)

    return l
