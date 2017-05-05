#!/usr/bin/env python
#-*- coding: utf-8 -*-
import sqlite3
import json
from flask import Flask, make_response, request, flash, redirect, url_for
from flask import send_file
from werkzeug import secure_filename
import os


UPLOAD_FOLDER = '/CvUps/'
ALLOWED_EXTENSIONS = set(['pdf'])

#Filtre l'extension du fichier dans ce cas c'est .pdf
def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

#Methode qui retourne toutes les données de la table CV de la BDD
def getCVs():
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute("SELECT * FROM CV")
        resultCmd = cursor.fetchall()
        cvList = []
        for Cv in resultCmd:
            cvDict = {
                'ID': Cv[0],
                'UrlCv': Cv[1],
                'Etat' : Cv [2]}
            cvList.append(cvDict)
    except sqlite3.Error as e:
        print(u"Erreur lors de la récupération des données de la base")
    try:
        return u'{"Cv" : ' + json.dumps(cvList) + '}'
    except ValueError as e:
        print(u"Erreur lors de la serialisation des données en JSON")
    conn.close()

#Methode qui retourne un CV en fonction de son ID
def getCvById(idCv):
    REQUETE_GET_CV_BY_ID = "SELECT * FROM CV WHERE ID = %d" % idCv
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(REQUETE_GET_CV_BY_ID)
        resultCmd = cursor.fetchall()
        cvList = []
        for Cv in resultCmd:
            cvDict = {
                'ID': Cv[0],
                'UrlCv': Cv[1],
                'Etat' : Cv [2]}
            cvList.append(cvDict)
    except sqlite3.Error as e:
        print(u"Erreur lors de la récupération des données de la base")
    try:
        return u'{"Cv" : ' + json.dumps(cvList) + '}'
    except ValueError as e:
        print(u"Erreur lors de la serialisation des données en JSON")
    conn.close()

#Mehode pour ajouter un cv dans la base de données + sur le serveur
#TODO à tester
def addCv():
    file = request.files['uploaded_file']
    if file and allowed_file(file.filename):
        try:
            filename = secure_filename(file.filename)
            file.save(os.path.join('/CvUps/', filename))
            return redirect(url_for('uploaded_file', filename=filename))
            try:
                conn = sqite3.connect('AreAppSqlBase.db')
                cursor = conn.cursor()
                cursor.execute("INSERT INTO Cv (UrlCv) VALUES (?)", ("http://localhost:5000/CvUps/" + filename))
                conn.commit()
                conn.close()
            except Exception as e:
                return "Erreur lors de la communication SQL", 400
        except Error as e:
            return 'Erreur envoi de fichier', 400

def sendCv(filename):
    return send_from_directory('/CvUps/', filename)

#Methode DELETE D'un CV
def delCV(idCv):
    REQ_DELETE_CV = "DELETE FROM CV WHERE ID = %d " %idCv
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(REQ_DELETE_CV)
        conn.commit()
        conn.close()
    except Sqlite3.Error as e:
        print("Erreur lors de l'ajout des données dans la base")
    #Si le DELETE c'est bien passé
    resp = make_response("true", 204)
    resp.headers['Message'] = 'Supression OK'
    return resp
