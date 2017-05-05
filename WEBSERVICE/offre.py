#!/usr/bin/env python
#-*- coding: utf-8 -*-
import sqlite3
import json
from flask import Flask, make_response, request, redirect, url_for, flash
from datetime import date
import os

#Methode qui retourne toutes les données de la table Offre de la BDD au format JSON
def getOffers():
    REQUETE_GET_OFFERS = " SELECT * FROM Offre "
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(REQUETE_GET_OFFERS)
        resultCmd = cursor.fetchall()
        Offres = []
        for Offre in resultCmd:
            offreDict = {
                'OfferID': Offre[0],
                'Titre': Offre[1],
                'Duree' : Offre[2],
                'Contrat' : Offre[3],
                'FormationAssociee' : Offre[4],
                'Etat' : Offre[5],
                'Details' : Offre[6],
                'DetailsResponsable' : Offre[7],
                'DatePublication' : Offre[8],
                'ContactRef' : Offre[9]}
            Offres.append(offreDict)
    except sqlite3.Error as e:
        print(u"Erreur lors de la récuperation de données dans la base")
    try:
        return u'{"Offre" : ' + json.dumps(Offres) + '}'
    except ValueError as e:
        print("Erreur lors de la serialisation en JSON")
    conn.close()

#Methode qui retourne une offre en fonction de son ID au format JSON
def getOfferById(idOffer):
    REQUETE_GET_OFFER_BY_ID = "SELECT * FROM Offre WHERE OfferID = %d" % idOffer
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(REQUETE_GET_OFFER_BY_ID)
        resultCmd = cursor.fetchall()
        Offres = []
        for Offre in resultCmd:
            offreDict = {
                'OfferID': Offre[0],
                'Titre': Offre[1],
                'Duree' : Offre[2],
                'Contrat' : Offre[3],
                'FormationAssociee' : Offre[4],
                'Etat' : Offre[5],
                'Details' : Offre[6],
                'DetailsResponsable' : Offre[7],
                'DatePublication' : Offre[8],
                'ContactRef' : Offre[9]}
            Offres.append(offreDict)
    except sqlite3.Error as e:
        print(u"Erreur lors de la récuperation de données dans la base")
    try:
        return u'{"Offre" : ' + json.dumps(Offres) + '}'
    except ValueError as e:
        print("Erreur lors de la serialisation en JSON")
    conn.close()

#Methode qui retourne toutes les données de la table Offre avec les détails du contact associé de la BDD au format JSON
def getOffersLinkedWithContact():
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(""" SELECT * FROM Offre INNER JOIN Contact ON ContactRef = ContactID """)
        resultCmd = cursor.fetchall()
        OffresLinkedWithContact = []
        for OffreContact in resultCmd:
            offreContactDict = {
                'OfferID': OffreContact[0],
                'Titre': OffreContact[1],
                'Duree' : OffreContact[2],
                'Contrat' : OffreContact[3],
                'FormationAssociee' : OffreContact[4],
                'Etat' : OffreContact[5],
                'Details' : OffreContact[6],
                'DetailsResponsable' : OffreContact[7],
                'DatePublication' : OffreContact[8],
                'ContactRef' : OffreContact[9],
                'ContactID' : OffreContact[10],
                'NomContact' : OffreContact[11],
                'NomEntreprise' : OffreContact[12],
                'Lieu' : OffreContact[13],
                'ContactMail' : OffreContact[14],
                'ContactPhone' : OffreContact[15]}
            OffresLinkedWithContact.append(offreContactDict)
    except sqlite3.Error as e:
        print(u"Erreur lors de la récuperation de données dans la base")
    try:
        return u'{"Offre" : ' + json.dumps(OffresLinkedWithContact) + '}'
    except ValueError as e:
        print("Erreur lors de la serialisation en JSON")
    conn.close()

#Methode qui retourne une Offre avec les détails du contact associé de la BDD au format JSON
def getOfferLinkedWithContactById(idOffer):
    REQUETE_GET_OFFER_LINKED_WITH_CONTACT_BY_ID = " SELECT * FROM Offre INNER JOIN Contact ON ContactRef = ContactID WHERE OfferID  = %d" % idOffer
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(REQUETE_GET_OFFER_LINKED_WITH_CONTACT_BY_ID)
        resultCmd = cursor.fetchall()
        OffreLinkedWithContact = []
        for OffreContact in resultCmd:
            offreContactDict = {
                'OfferID': OffreContact[0],
                'Titre': OffreContact[1],
                'Duree' : OffreContact[2],
                'Contrat' : OffreContact[3],
                'FormationAssociee' : OffreContact[4],
                'Etat' : OffreContact[5],
                'Details' : OffreContact[6],
                'DetailsResponsable' : OffreContact[7],
                'DatePublication' : OffreContact[8],
                'ContactRef' : OffreContact[9],
                'ContactID' : OffreContact[10],
                'NomContact' : OffreContact[11],
                'NomEntreprise' : OffreContact[12],
                'Lieu' : OffreContact[13],
                'ContactMail' : OffreContact[14],
                'ContactPhone' : OffreContact[15]}
            OffreLinkedWithContact.append(offreContactDict)
    except sqlite3.Error as e:
        print(u"Erreur lors de la récuperation de données dans la base")
    try:
        return u'{"Offre" : ' + json.dumps(Offres) + '}'
    except ValueError as e:
        print("Erreur lors de la serialisation en JSON")
    conn.close()


#Methode qui recupere un post d'une Offre avec un contact au format JSON et qui le poste dans la BDD
def addOfferLinkedWithContact():
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        #Creation du dictionnaire prenant les valeurs du JSON reçu
        data = request.get_json()
        #Recuperation des variables dans le dictionnaire
        NomContact = str(data.get("nomContact"))
        NomEntreprise = str(data.get("nomEntreprise"))
        Lieu = str(data.get("lieuEntreprise"))
        ContactMail = str(data.get("mailContact"))
        ContactPhone = str(data.get("telephoneContact"))
        Titre = str(data.get("titre"))
        Duree = str(data.get("dureeContrat"))
        Contrat = int(data.get("typeContrat"))
        FormationAssociee = int(data.get("formationAssociee"))
        Etat = int(data.get("etat"))
        Details = str(data.get("details"))
        DetailsResponsables = str(data.get("detailsResponsables"))
        #On recupere la date
        Date = date.today()
        Annee = Date.year
        Mois = Date.month
        Jour = Date.day
        DatePublication = "%d/%d/%d" % (Annee, Mois, Jour)
        #Recuperation du Contact Lié à l'offre
        cursor.execute(""" SELECT MAX(ContactID) FROM CONTACT """)
        resultCmd = cursor.fetchone()
        ContactRef = int(resultCmd[0])
        ContactRef = ContactRef + 1
        #Execution de la requete on poste dans les tables Contact et Offre le contenu du POST
        cursor.execute(""" INSERT INTO Contact (NomContact, NomEntreprise, Lieu, ContactMail, ContactPhone) VALUES (?,?,?,?,?) """, (NomContact, NomEntreprise, Lieu, ContactMail, ContactPhone))
        cursor.execute(""" INSERT INTO Offre (Titre, Duree, Contrat, FormationAssociee, Etat, Details, DetailsResponsables, DatePublication, ContactRef) VALUES (?,?,?,?,?,?,?,?,?)""", (Titre, Duree, Contrat, FormationAssociee, Etat, Details, DetailsResponsables, DatePublication, ContactRef))
        conn.commit()
    except sqlite3.Error as e:
        print(u"Erreur lors du post des données dans la base")
        #Reponse si le post c'est bien passé
        resp = make_response("true", 201)
        resp.headers['Content-Type'] = 'application/json'
        conn.close()
        return resp

#Methode DELETE D'une Offre
def delOffre(idOffre):
    REQ_DELETE_OFFER = "DELETE FROM Offre WHERE OfferID = %d " %idOffre
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(REQ_DELETE_OFFER)
        conn.commit()
        conn.close()
    except sqlite3.Error as e:
        print(u"Erreur lors de la supression de la donnée")
    #Si le DELETE c'est bien passé
    resp = make_response("true", 204)
    resp.headers['Message'] = 'Supression OK'
    return resp

#Methode qui recupere un PUT d'une Offre avec un contact au format JSON et qui le Modifie dans la BDD
def updateOfferLinkedWithContact(idOffre):
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        #Creation du dictionnaire prenant les valeurs du JSON reçu
        data = request.get_json()
        #Recuperation des variables dans le dictionnaire
        NomContact = str(data.get("NomContact"))
        NomEntreprise = str(data.get("NomEntreprise"))
        Lieu = str(data.get("Lieu"))
        ContactMail = str(data.get("ContactMail"))
        ContactPhone = str(data.get("ContactPhone"))
        Titre = str(data.get("Titre"))
        Duree = str(data.get("Duree"))
        Contrat = int(data.get("Contrat"))
        FormationAssociee = int(data.get("FormationAssociee"))
        Etat = int(data.get("Etat"))
        Details = str(data.get("Details"))
        DetailsResponsables = str(data.get("DetailsResponsables"))
        #Recuperation du Contact Lié à l'offre
        cursor.execute("SELECT ContactRef FROM Offre WHERE OfferID = %d " % idOffre)
        result = cursor.fetchone()
        ContactRef = int(result[0])
        #Execution de la requete on poste dans les tables Contact et Offre le contenu du POST
        cursor.execute(" UPDATE Contact SET NomContact = '%s', NomEntreprise = '%s', Lieu = '%s', ContactMail = '%s', ContactPhone = '%s' WHERE ContactID = %d " % (NomContact, NomEntreprise, Lieu, ContactMail, ContactPhone, ContactRef))
        cursor.execute(" UPDATE Offre SET Titre = '%s', Duree = '%s', Contrat = %d, FormationAssociee = %d, Etat = %d, Details = '%s', DetailsResponsables = '%s' WHERE OfferID = %d" % (Titre, Duree, Contrat, FormationAssociee, Etat, Details, DetailsResponsables,idOffre))
        conn.commit()
    except sqlite3.Error as e:
        print(u"Erreur lors de la modification des données")
    #Reponse si le post c'est bien passé
    resp = make_response("true", 201)
    resp.headers['Content-Type'] = 'application/json'
    conn.close()
    return resp
