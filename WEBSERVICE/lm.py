#!/usr/bin/env python
#-*- coding: utf-8 -*-
import sqlite3
import json
from flask import Flask, make_response, request, flash, redirect, url_for
from flask import send_file
from werkzeug import secure_filename
import os

UPLOAD_FOLDER = '/LmUps/'
ALLOWED_EXTENSIONS = set(['pdf'])


#Filtre l'extension du fichier dans ce cas c'est .pdf
def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

#Methode qui retourne toutes les données de la table LM de la BDD
def getLMs():
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(""" SELECT * FROM LM """)
        resultCmd = cursor.fetchall()
        lmList = []
        for Lm in resultCmd:
            lmDict = {
                'ID': Lm[0],
                'UrlLM': Lm[1],
                'Etat' : Lm [2]}
            lmList.append(lmDict)
    except sqlite3.Error as e:
        print(u"Erreur lors de la récuperation des données dans la base")
    try:
        return u'{"Lm" : ' + json.dumps(lmDict) + '}'
    except ValueError as e:
        print(u"Erreur lors de la serialisation de la donnée en JSON")
    conn.close()

#Methode qui retourne une LM en fonction de son ID
def getLmById(idLm):
    REQUETE_GET_LM_BY_ID = "SELECT * FROM LM WHERE ID = %d" % idLm
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(REQUETE_GET_LM_BY_ID)
        resultCmd = cursor.fetchall()
        lmList = []
        for Lm in resultCmd:
            lmDict = {
                'ID': Lm[0],
                'UrlCv': Lm[1],
                'Etat' : Lm[2]}
            lmList.append(lmDict)
    except sqlite3.Error as e:
        print(u"Erreur lors de la récuperation des données dans la base")
    try:
        return u'{"Lm" : ' + json.dumps(lmDict) + '}'
    except ValueError as e:
        print(u"Erreur lors de la serialisation de la donnée en JSON")
    conn.close()

#Mehode pour ajouter un document dans la base de données + sur le serveur
#TODO à tester
def addLm():
    file = request.files['uploaded_file']
    if file and allowed_file(file.filename):
        try:
            filename = secure_filename(file.filename)
            file.save(os.path.join(UPLOAD_FOLDER, filename))
            return redirect(url_for('uploaded_file', filename=filename))
            try:
                conn = sqite3.connect('AreAppSqlBase.db')
                cursor = conn.cursor()
                cursor.execute("INSERT INTO LM (UrlLM) VALUES (?)", ("http://localhost:5000/LmUps/" + filename))
                conn.commit()
                conn.close()
            except Exception as e:
                return "Erreur lors de la communication SQL", 400
        except Error as e:
            return 'Erreur envoi de fichier', 400

def sendLm(filename):
    return send_from_directory(UPLOAD_FOLDER, filename)

#Methode DELETE D'une LM
def delLM(idLm):
    REQ_DELETE_LM = "DELETE FROM LM WHERE ID = %d " %idLm
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(REQ_DELETE_LM)
        conn.commit()
        conn.close()
    except sqlite3.Error as e:
        print("Erreur lors de la supression de la donnée dans la base")
    #Si le DELETE c'est bien passé
    resp = make_response("true", 204)
    resp.headers['Message'] = 'Supression OK'
    return resp

#TODO modifyLm
