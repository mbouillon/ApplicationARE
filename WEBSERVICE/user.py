#!/usr/bin/env python
#-*- coding: utf-8 -*-
import sqlite3
import json
from flask import Flask, make_response, request
import hashlib
import smtplib

#Gestion des import pour python 3 et python 2
try:
    from email.MIMEMultipart import MIMEMultipart
    from email.MIMEText import MIMEText
except Exception as e:
    print("")
try:
    from email.mime.multipart import MIMEMultipart
    from email.mime.text import MIMEText
except Exception as e:
    print("")

#Methode qui teste si l'utilisateur existe et si le password est OK
def verifyPassword(mail, password):
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        try:
            cursor.execute(u"SELECT Password FROM Utilisateur WHERE Mail = '%s' "  %mail)
            if(cursor.fetchone()):
                cursor.execute(u"SELECT Password FROM Utilisateur WHERE Mail = '%s' "  %mail)
                res = cursor.fetchone()
                pw = res[0]
                #Hash du mot de passe passe en parametre pour la comparaison
                Pass = hashlib.md5(password).hexdigest()
                if(str(Pass) == str(pw)):
                    conn.close()
                    return True
                else:
                    conn.close()
                    print("Mot de passe incorrect")
                    return False
            else:
                conn.close()
                print("L'utilisateur n'existe pas")
                return False
        except sqlite3.Error as e:
            conn.close()
            print("Req echouée")
    except sqlite3.Error as e:
        print("Erreur lors de la connexion à la base de données")


#Methode qui retourne toutes les données de la table Utilisateur de la BDD au format JSON
def getUsers():
    REQUETE_GET_USERS = " SELECT * FROM Utilisateur "
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(REQUETE_GET_USERS)
        resultCmd = cursor.fetchall()
        Utilisateurs = []
        for Utilisateur in resultCmd:
            utilisateurDict = {
                'id': Utilisateur[0],
                'type': Utilisateur[1],
                'Nom' : Utilisateur[2],
                'Prenom' : Utilisateur[3],
                'Mail' : Utilisateur[4],
                'Telephone' : Utilisateur[5],
                'Formation' : Utilisateur[6],
                'cvID' : Utilisateur[7],
                'lmID' : Utilisateur[8],
                'Etiquette' : Utilisateur[9],
                'DashBoard' : Utilisateur[10],
                'Participations' : Utilisateur[11],
                'Password' : Utilisateur[12],
                'IsValid' : Utilisateur[13]}
            Utilisateurs.append(utilisateurDict)
    except sqlite3.Error as e:
        print(u"Erreur lors de la récupération des données de la base")
    try:
        return u'{"Utilisateur" : ' + json.dumps(Utilisateurs) + '}'
    except ValueError as e:
        print(u"Erreur lors de la serialisation des données en JSON")
    conn.close()

#Methode qui retourne les données d'un utilisateur grace à son id
def getUserById(idUser):
    REQUETE_GET_USER_BY_ID = " SELECT * FROM Utilisateur WHERE id = %d " % idUser
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(REQUETE_GET_USER_BY_ID)
        resultCmd = cursor.fetchall()
        Utilisateurs = []
        for Utilisateur in resultCmd:
            utilisateurDict = {
                'id': Utilisateur[0],
                'type': Utilisateur[1],
                'Nom' : Utilisateur[2],
                'Prenom' : Utilisateur[3],
                'Mail' : Utilisateur[4],
                'Telephone' : Utilisateur[5],
                'Formation' : Utilisateur[6],
                'cvID' : Utilisateur[7],
                'lmID' : Utilisateur[8],
                'Etiquette' : Utilisateur[9],
                'DashBoard' : Utilisateur[10],
                'Participations' : Utilisateur[11],
                'Password' : Utilisateur[12],
                'IsValid' : Utilisateur[13]}
            Utilisateurs.append(utilisateurDict)
    except sqlite3.Error as e:
        print(u"Erreur lors de la récupération des données de la base")
    try:
        return u'{"Utilisateur" : ' + json.dumps(Utilisateurs) + '}'
    except ValueError as e:
        print(u"Erreur lors de la serialisation des données en JSON")
    conn.close()

#Methode qui retourne les données d'un utilisateur grace à son e-mail
def getUserByMail(mailUser):
    REQUETE_GET_USER_BY_MAIL = " SELECT * FROM Utilisateur WHERE Mail = %s " % mailUser
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(REQUETE_GET_USER_BY_MAIL)
        resultCmd = cursor.fetchall()
        Utilisateurs = []
        for Utilisateur in resultCmd:
            utilisateurDict = {
                'id': Utilisateur[0],
                'type': Utilisateur[1],
                'Nom' : Utilisateur[2],
                'Prenom' : Utilisateur[3],
                'Mail' : Utilisateur[4],
                'Telephone' : Utilisateur[5],
                'Formation' : Utilisateur[6],
                'cvID' : Utilisateur[7],
                'lmID' : Utilisateur[8],
                'Etiquette' : Utilisateur[9],
                'DashBoard' : Utilisateur[10],
                'Participations' : Utilisateur[11],
                'Password' : Utilisateur[12],
                'IsValid' : Utilisateur[13]}
            Utilisateurs.append(utilisateurDict)
    except sqlite3.Error as e:
        print(u"Erreur lors de la récupération des données de la base")
    try:
        return u'{"Utilisateur" : ' + json.dumps(Utilisateurs) + '}'
    except ValueError as e:
        print(u"Erreur lors de la serialisation des données en JSON")
    conn.close()

#Methode qui retourne les données d'un utilisateur grace à son type (responsable ou etudiant)
def getUserByType(typeUser):
    REQUETE_GET_USER_BY_TYPE = " SELECT * FROM Utilisateur WHERE Type = %d " % typeUser
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(REQUETE_GET_USER_BY_TYPE)
        resultCmd = cursor.fetchall()
        Utilisateurs = []
        for Utilisateur in resultCmd:
            utilisateurDict = {
                'id': Utilisateur[0],
                'type': Utilisateur[1],
                'Nom' : Utilisateur[2],
                'Prenom' : Utilisateur[3],
                'Mail' : Utilisateur[4],
                'Telephone' : Utilisateur[5],
                'Formation' : Utilisateur[6],
                'cvID' : Utilisateur[7],
                'lmID' : Utilisateur[8],
                'Etiquette' : Utilisateur[9],
                'DashBoard' : Utilisateur[10],
                'Participations' : Utilisateur[11],
                'Password' : Utilisateur[12],
                'IsValid' : Utilisateur[13]}
            Utilisateurs.append(utilisateurDict)
    except sqlite3.Error as e:
        print(u"Erreur lors de la récupération des données de la base")
    try:
        return u'{"Utilisateur" : ' + json.dumps(Utilisateurs) + '}'
    except ValueError as e:
        print(u"Erreur lors de la serialisation des données en JSON")
    conn.close()

#Methode qui recupere un POST User au format JSON et qui le poste dans la BDD
def addUser():
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        content = request.json
        print("hello")
        print(content)
        print("world")
        #Creation du dictionnaire prenant les valeurs du JSON reçu
        data = request.get_json()
        #Recuperation des variables dans le dictionnaire
        Type = bool(data.get("type"))
        Nom = str(data.get("nom"))
        Prenom = str(data.get("prenom"))
        Mail = str(data.get("mail"))
        Telephone = str(data.get("telephone"))
        Formation = int(data.get("formation"))
        Password = str(data.get("password"))
    except ValueError as e:
        print(u"Erreur lors de la recuperation du JSON")
    #Sécurisation du mot de passe hashage en md5
    try:
        Pass = hashlib.md5(Password).hexdigest()
    except ValueError as e:
        print(u"Erreur lors du hashage du MDP en MD5")
    #Execution de la requete
    if extensionMailOk(Mail):
        #Teste si un compte créé existe déjà
        if checkIfExists(Mail, cursor) == False:
            try:
                cursor.execute(" INSERT INTO Utilisateur (Type, Nom, Prenom, Mail, Telephone, Formation, Password, IsValid) VALUES (?,?,?,?,?,?,?,0) ", (Type, Nom, Prenom, Mail, Telephone, Formation, Pass))
                conn.commit()
            except sqlite3.Error as e:
                print(u"Erreur lors de l'insertion des données dans la base")
            #Mail de validation
            sendCheckMail(Mail, cursor)
            #Reponse si le post c'est bien passé
            resp = make_response("true", 201)
            resp.headers['Content-Type'] = 'application/json'
            conn.close()
            return resp
        else:
            resp = make_response("false", 403)
            resp.headers['Message'] = "Erreur : Un compte existe déjà avec cette adresse mail"
            conn.close()
            return resp
    else:
        #Si l'email ne porte pas l'extention imerir.com
        resp = make_response("false", 403)
        resp.headers['Message'] = "Erreur : Adresse mail doit être imerir.com"
        conn.close()
        return resp

#Methode appellée à la création d'un user qui envoie un mail de validation pour valider le compte
def sendCheckMail(UserMail, cursor):
    #Récuperation de l'id lié au mail pour creer le lien de validation
    try:
        REQ_SELECT_ID = "SELECT id FROM Utilisateur WHERE Mail = '%s' " % UserMail
        cursor.execute(REQ_SELECT_ID)
        resultCmd = cursor.fetchone()
        IdUser = int(resultCmd[0])
    except sqlite3.Error as e:
        print(u"Erreur lors de la recuperation des données ")
    #Création du contenu du mail
    try:
        fromaddr = "devareapp@gmail.com"
        toaddr = UserMail
        msg = MIMEMultipart()
        msg['From'] = "devareapp@gmail.com"
        msg['To'] = UserMail
        msg['Subject'] = "Confirmation d'inscription Application ARE IMERIR"
        Link = "http://127.0.0.1:5000/mobile/check/%d" % IdUser
        body = "Bonjour, et merci pour votre inscription à l'application d'Aide à la Recherche d'Entreprise de IMERIR. \nVeuillez cliquer sur le lien qui suit afin de vérifier l'adresse email.\n\nLien : %s " % Link
        msg.attach(MIMEText(body, 'plain'))
        #Connexion au serveur smtp gmail
        server = smtplib.SMTP('smtp.gmail.com', 587)
        server.starttls()
        server.login(fromaddr, "areapplication")
        text = msg.as_string()
        #Envoi du mail et deconnexion du serveur
        server.sendmail(fromaddr, toaddr, text)
        server.quit()
    except ValueError as e:
        print(u"Erreur lors de l'envoi du mail")

#Methode PUT qui modifie la valeur de IsValid de la table user quand le lien de validation est sollicité
def UpdateIsValid(user_id):
    REQ_UPDATE_ISVALID = "UPDATE Utilisateur SET IsValid = 1 WHERE ID = %d" % user_id
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(REQ_UPDATE_ISVALID)
        conn.commit()
    except sqlite3.Error as e:
        print(u"Erreur lors de l'update")
    #Reponse si l'up c'est bien passé
    resp = make_response("true", 201)
    resp.headers['Message'] = 'Adresse email validée'
    resp = u"<h1> Votre compte à été validé </h1>"
    conn.close()
    return resp

#Methode de test pour savoir si une chaine comporte l'extension imerir.com
def extensionMailOk(Mail):
    return '@' in Mail and Mail.rsplit('@',1)[1] in ('imerir.com')

#Mehode de test pour savoir si un compte existe déjà avec l'adresse mail entrée
def checkIfExists(Mail, cursor):
    REQ_SELECT_MAIL = "SELECT Mail FROM Utilisateur WHERE Mail = '%s' " % Mail
    try:
        cursor.execute(REQ_SELECT_MAIL)
    except sqlite3.Error as e:
        print(u"Erreur lors de la recupération des données dans la base")
    if(cursor.fetchone()):
        return True
    else:
        return False

#Methode DELETE D'un utilisateur
def delUser(idUser):
    REQ_DELETE_USER = "DELETE FROM Utilisateur WHERE id = %d " %idUser
    try:
        conn = sqlite3.connect('AreAppSqlBase.db')
        cursor = conn.cursor()
        cursor.execute(REQ_DELETE_USER)
        conn.commit()
        conn.close()
    except sqlite3.Error as e:
        print (u"Erreur lors de la mise à jour de la base")
    #Si le DELETE c'est bien passé
    resp = make_response("true", 204)
    resp.headers['Message'] = 'Supression OK'
    return resp
