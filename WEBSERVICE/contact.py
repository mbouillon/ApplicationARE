#!/usr/bin/env python
#-*- coding: utf-8 -*-
import sqlite3
from flask import Flask, make_response, request
import json

#Methode qui retourne toutes les données de la table Contact de la BDD
def getContacts():
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(""" SELECT * FROM Contact """)
        resultCmd = cursor.fetchall()
        Contacts = []
        for Contact in resultCmd:
            contactDict = {
                'ContactID': Contact[0],
                'NomContact': Contact[1],
                'NomEntreprise' : Contact[2],
                'Lieu' : Contact[3],
                'ContactMail' : Contact[4],
                'ContactPhone' : Contact[5]}
            Contacts.append(contactDict)
    except sqlite3.Error as e:
        print("Erreur lors de la récuperation de la donnée dans la base")
    try:
        return u'{"Contact" : ' + json.dumps(Contacts) + '}'
    except ValueError as e:
        print("Erreur lors de la serialisation de la donnée en JSON")
    conn.close()

#Methode qui retourne un Contact en fonction de son ID
def getContactById(idContact):
    REQUETE_GET_CONTACT_BY_ID = "SELECT * FROM Contact WHERE ContactID = %d" % idContact
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(REQUETE_GET_CONTACT_BY_ID)
        resultCmd = cursor.fetchall()
        Contacts = []
        for Contact in resultCmd:
            contactDict = {
                'ContactID': Contact[0],
                'NomContact': Contact[1],
                'NomEntreprise' : Contact[2],
                'Lieu' : Contact[3],
                'ContactMail' : Contact[4],
                'ContactPhone' : Contact[5]}
            Contacts.append(contactDict)
    except sqlite3.Error as e:
        print("Erreur lors de la récuperation de la donnée dans la base")
    try:
        u'{"Contact" : ' + json.dumps(Contacts) + '}'
    except ValueError as e:
        print("Erreur lors de la serialisation de la donnée en JSON")
    conn.close()

#La méthode qui ajoute un contact se fait dans la methode addOffer car elles sont liées.
