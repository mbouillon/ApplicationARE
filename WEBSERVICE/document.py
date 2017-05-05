#!/usr/bin/env python
#-*- coding: utf-8 -*-
import sqlite3
import json
from flask import Flask, make_response, request, flash, redirect, url_for
from datetime import date
from flask import send_file
from werkzeug import secure_filename
import os

ALLOWED_EXTENSIONS = set(['pdf'])
#Filtre l'extension du fichier dans ce cas c'est .pdf
def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

#Mehode pour ajouter un document dans la base de données + sur le serveur
#TODO à tester
def addDocument():
    file = request.files['uploaded_file']
    if file and allowed_file(file.filename):
        try:
            filename = secure_filename(file.filename)
            file.save(os.path.join('/DocUps/', filename))
            return redirect(url_for('uploaded_file', filename=filename))
            try:
                conn = sqite3.connect('AreAppSqlBase.db')
                cursor = conn.cursor()
                cursor.execute("INSERT INTO Document (Document) VALUES (?)", ("http://localhost:5000/DocUps/" + filename))
                conn.commit()
                conn.close()
            except Exception as e:
                return "Erreur lors de la communication SQL", 400
        except Error as e:
            return 'Erreur envoi de fichier', 400

def sendDoc(filename):
    return send_from_directory('/DocUps/', filename)

#Methode qui retourne toutes les données de la table Documents de la BDD
def getDocuments():
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(""" SELECT * FROM Document """)
        resultCmd = cursor.fetchall()
        documentList = []
        for Document in resultCmd:
            documentDict = {
                'DocID': Document[0],
                'PublisherId': Document[1],
                'PublishDate' : Document[2],
                'Document' : Document[3]}
            documentList.append(documentDict)
    except sqlite3.Error as e:
        print("Erreur lors de la récuperation des données dans la base")
    try:
        return u'{"Document" : ' + json.dumps(documentList) + '}'
    except ValueError as e:
        print("Erreur lors de la serialisation de la donnée en JSON")
    conn.close()

#Methode qui retourne un CV en fonction de son ID
def getDocumentById(idDocument):
    REQUETE_GET_DOCUMENT_BY_ID = "SELECT * FROM Document WHERE DocID = %d" % idDocument
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(REQUETE_GET_DOCUMENT_BY_ID)
        resultCmd = cursor.fetchall()
        documentList = []
        for Document in resultCmd:
            documentDict = {
                'DocID': Document[0],
                'PublisherId': Document[1],
                'PublishDate' : Document[2],
                'Document' : Document[3]}
            documentList.append(documentDict)
    except sqlite3.Error as e:
        print("Erreur lors de la récuperation des données dans la base")
    try:
        return u'{"Document" : ' + json.dumps(documentList) + '}'
    except ValueError as e:
        print("Erreur lors de la serialisation de la donnée en JSON")
    conn.close()

#Methode DELETE d'un document
def delDoc(idDoc):
    REQ_DELETE_DOC = "DELETE FROM Document WHERE DocID = %d " %idDoc
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(REQ_DELETE_DOC)
        conn.commit()
        conn.close()
    except sqlite3.Error as e:
        print("Erreur lors de la supression de la donnée dans la base")
    #Si le DELETE c'est bien passé
    resp = make_response("true", 204)
    resp.headers['Message'] = 'Supression OK'
    return resp
