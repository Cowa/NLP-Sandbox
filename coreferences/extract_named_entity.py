#!/usr/bin/env python
# coding: utf-8
# author: Brice Thomas

import os, re, sys, glob, codecs

##
# Extract text content from the given folder
#
# @param folder Path to the folder containing .txt files (ex: "ne-src/")
#
# @return An array containing text of each files
#
def extractTextFromFolder(folder):
    files = glob.glob(folder + "*.txt")
    filesContent = []

    # Get all text files' content
    for file in files:
        fh = codecs.open(file, "r", "utf-8")
        filesContent.append(fh.read())
        fh.close()

    return filesContent

##
# Extract annotated named entity from the given text
#
# @param text A string containing the text
#
# @return A dictionnary (key -> type, values -> array of named entities with index)
#         Example: {'LOCATION': [(1, 'Hong-Kong'), (2, 'Mars'), (3, 'Nantes')]}
#
def extractNamedEntity(text):
    index = 1
    namedEntities = dict()
    matched = re.findall(r"<(.*?)>(.*?)</(.*?)>", text)

    for match in matched:
        if match[0] in namedEntities:
            namedEntities[match[0]].append((index, match[1]))
        else:
            namedEntities[match[0]] = [(index, match[1])]
        index = index + 1

    return namedEntities

##
# Extract file's name from the given folder
#
# @param folder Path to the folder
#
# @return An array containing the name of each files
#
def extractFileName(folder):
    return [os.path.basename(x) for x in glob.glob(folder + "*.txt")]
