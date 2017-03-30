#!/usr/bin/env python
#-*- coding: utf-8 -*-
import sqlite3
import json
from flask import Flask, make_response, request

#Methode qui retourne toutes les données de la table MessageAccueil de la BDD
def getMessages():
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(""" SELECT * FROM MessageAccueil """)
        resultCmd = cursor.fetchall()
        msgList = []
        for msg in resultCmd:
            msgDict = {
                'MessageID': msg[0],
                'PublisherId': msg[1],
                'Message' : msg [2]}
            msgList.append(msgDict)
    except sqlite3.Error as e:
        print(u"Erreur lors de la récuperation des données")
    try:
        return u'{"Message" : ' + json.dumps(msgList) + '}'
    except ValueError as e:
        print(u"Erreur lors de la serialisation en JSON")
    conn.close()

#Methode qui retourne toutes les données d'un MessageAccueil de la BDD
def getMessageById(idMsg):
    REQ_MESSAGE_BY_ID = "SELECT * FROM MessageAccueil WHERE MessageID = %d " % idMsg
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(REQ_MESSAGE_BY_ID)
        resultCmd = cursor.fetchall()
        msgList = []
        for msg in resultCmd:
            msgDict = {
                'MessageID': msg[0],
                'PublisherId': msg[1],
                'Message' : msg [2]}
            msgList.append(msgDict)
    except sqlite3.Error as e:
        print(u"Erreur lors de la récuperation des données")
    try:
        return u'{"Message" : ' + json.dumps(msgList) + '}'
    except ValueError as e:
        print(u"Erreur lors de la serialisation en JSON")
    conn.close()

#Methode qui retourne toutes les données d'un MessageAccueil de la BDD
def getLastMessage():
    REQ_LAST_MESSAGE = "SELECT * FROM MessageAccueil WHERE MessageId = (SELECT MAX(MessageId) FROM MessageAccueil)"
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(REQ_LAST_MESSAGE)
        resultCmd = cursor.fetchall()
        msgList = []
        for msg in resultCmd:
            msgDict = {
                'MessageID': msg[0],
                'PublisherId': msg[1],
                'Message' : msg [2]}
            msgList.append(msgDict)
    except sqlite3.Error as e:
        print(u"Erreur lors de la récuperation des données")
    try:
        return u'{"Message" : ' + json.dumps(msgList) + '}'
    except ValueError as e:
        print(u"Erreur lors de la serialisation en JSON")
    conn.close()

#Methode qui recupere un message au format JSON et qui le poste dans la BDD
def addMessage():
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        #Creation du dictionnaire prenant les valeurs du JSON reçu
        data = request.get_json()
        #Recuperation des variables dans le dictionnaire
        PublisherId = int(data.get("PublisherId"))
        Message = str(data.get("Message"))
        #Execution de la requete
        cursor.execute(""" INSERT INTO MessageAccueil (PublisherId, Message) VALUES (?,?) """, (PublisherId, Message))
        conn.commit()
    except sqlite3.Error as e:
        print(u"Erreur lors de l'ajout dans la base")
    #Reponse si le post c'est bien passé
    resp = make_response("true", 201)
    resp.headers['Content-Type'] = 'application/json'
    conn.close()
    return resp

#Methode DELETE D'un msg
def delMsg(idMessage):
    REQ_DELETE_MSG = "DELETE FROM MessageAccueil WHERE MessageID = %d " %idMessage
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(REQ_DELETE_MSG)
        conn.commit()
        conn.close()
    except sqlite3.Error as e:
        print(u"Erreur lors de la supression de la donnée")
    #Si le DELETE c'est bien passé
    resp = make_response("true", 204)
    resp.headers['Message'] = 'Supression OK'
    return resp

#Methode Updtade qui modifie le message d'accueil
def updateMsg(idMessage):
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        #Creation du dictionnaire prenant les valeurs du JSON reçu
        data = request.get_json()
        #Recuperation des variables dans le dictionnaire
        Message = str(data.get("Message"))
        #Execution de la requete
        cursor.execute(" UPDATE MessageAccueil SET Message='%s' WHERE MessageID=%d " % (Message,idMessage))
        conn.commit()
    except:
        print("Problème lors de la mise à jour des données")
    #Reponse si le Put c'est bien passé
    resp = make_response("true", 204)
    resp.headers['Content-Type'] = 'application/json'
    conn.close()
    return resp
