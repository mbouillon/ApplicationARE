#!/usr/bin/env python
#-*- coding: utf-8 -*-
from flask import Flask, make_response, request, render_template
from contact import *
from offre import *
from user import *
from document import *
from cv import *
from lm import *
from messageAccueil import *
from flask_httpauth import HTTPBasicAuth
import sqlite3


#VERSION A PUBLIER SUR SERVEUR NE PERMET L'ACCES AUX DONNES QUE SI ON POSSEDE UN COMPTE
app = Flask(__name__)
#Permet l'acces seulement au membres de l'application
auth = HTTPBasicAuth()

@auth.verify_password
def verify_pw(mail, password):
    return verifyPassword(mail, password)

#Route pour l'acces à la documentation de l'api
@app.route("/doc/")
def Documentation():
    return render_template('docApi.html')


################################################
#                                              #
#                      CV                      #
#                                              #
################################################
#Les Get CV
@app.route("/mobile/cvList/")
#@auth.login_required
def CvList():
    return getCVs()
@app.route("/mobile/cv/<int:cv_id>")
#@auth.login_required
def CvById(cv_id):
    return getCvById(cv_id)
#Post de CV
@app.route("/mobile/cv/", methods=["POST"])
#@auth.login_required
def newCv():
    return addCv()
#Lien vers le cv
@app.route('/CvUps/<filename>')
#@auth.login_required
def uploaded_CV(filename):
    return sendCv(filename)
#Delete de CV
@app.route("/mobile/cv/<int:cv_id>", methods=["DELETE"])
#@auth.login_required
def deleteCv(cv_id):
    return delCV(cv_id)
#TODO
# #Modifier un CV
# @app.route("/mobile/cv/<int:cv_id>", methods=["PUT"])
# def modifyCv(cv_id):
#     return updateCv(cv_id)

################################################
#                                              #
#                      LM                      #
#                                              #
################################################
#Les Get de LM
@app.route("/mobile/lmList/")
#@auth.login_required
def LmList():
    return getLMs()
@app.route("/mobile/lm/<int:lm_id>")
#@auth.login_required
def LmById(lm_id):
    return getLmById(lm_id)
#Post de LM
@app.route("/mobile/lm/", methods=["POST"])
def newLm():
    return addLm()
#Lien vers le cv
@app.route('/LmUps/<filename>')
#@auth.login_required
def uploaded_LM(filename):
    return sendCv(filename)
#Delete de LM
@app.route("/mobile/lm/<int:lm_id>", methods=["DELETE"])
#@auth.login_required
def deleteLm(lm_id):
    return delLM(lm_id)
#TODO
# #Put de LM Afin de modifier un LM
# @app.route("/mobile/lm/<int:lm_id>", methods=["PUT"])
# def modifyLm(lm_id):
#     return updateLm(lm_id)

################################################
#                                              #
#                    CONTACT                   #
#                                              #
################################################
#Les Get de Contact
@app.route("/mobile/ContactList/")
#@auth.login_required
def ContactList():
    return getContacts()
@app.route("/mobile/Contact/<int:contact_id>")
#@auth.login_required
def ContactById(contact_id):
    return getContactById(contact_id)
#Post de contact se fait dans offre
#Pas besoin de delete un contact

################################################
#                                              #
#                    OFFRES                    #
#                                              #
################################################
#Les get des Offres
@app.route("/mobile/OffersList/")
#@auth.login_required
def OffersList():
    return getOffers()
@app.route("/mobile/Offre/<int:offer_id>")
#@auth.login_required
def OfferById(offer_id):
    return getOfferById(offer_id)
@app.route("/mobile/OffersListLinkedWithContact/")
##@auth.login_required
def OffersListLinkedWithContact():
    return getOffersLinkedWithContact()
@app.route("/mobile/OfferLinkedWithContact/<int:offer_id>")
#@auth.login_required
def OfferLinkedWithContactById(offer_id):
    return getOfferLinkedWithContactById(offer_id)
#Post d'offre + Contact
@app.route("/mobile/OfferLinkedWithContact/", methods=["POST"])
#@auth.login_required
def newOffer():
    return addOfferLinkedWithContact()
#Delete d'une offre
@app.route("/mobile/Offre/<int:offer_id>", methods=["DELETE"])
#@auth.login_required
def deleteOffre(offer_id):
    return delOffre(offer_id)
#Put d'une offre afin de modifier une offre
@app.route("/mobile/Offre/<int:offer_id>", methods=["PUT"])
#@auth.login_required
def modifyOffre(offer_id):
    return updateOfferLinkedWithContact(offer_id)

################################################
#                                              #
#                    USERS                     #
#                                              #
################################################
#Les get de Utilisateurs
@app.route("/mobile/UsersList/")
def UsersList():
    return getUsers()
@app.route("/mobile/UserById/<int:user_id>")
#@auth.login_required
def UserById(user_id):
    return getUserById(user_id)
@app.route("/mobile/UserByMail/<string:user_mail>")
#@auth.login_required
def UserByMail(user_mail):
    return getUserByMail(user_mail)
@app.route("/mobile/UserByType/<int:user_type>")
#@auth.login_required
def UserByType(user_type):
    return getUserByType(user_type)
#Post de User
@app.route("/mobile/User/", methods=["POST"])
def newUser():
    return addUser()
#Validation d'adresse Mail quand un get est effectué à cette adresse
@app.route("/mobile/check/<int:user_id>")
#@auth.login_required
def CheckMail(user_id):
    return UpdateIsValid(user_id)
#Delete d'un user
@app.route("/mobile/UserById/<int:user_id>", methods=["DELETE"])
#@auth.login_required
def deleteUser(user_id):
    return delUser(user_id)

################################################
#                                              #
#                  DOCUMENTS                   #
#                                              #
################################################
#Get de document
@app.route("/mobile/DocumentList/")
#@auth.login_required
def DocList():
    return getDocuments()
@app.route("/mobile/Document/<int:doc_id>")
#@auth.login_required
def DocumentById(doc_id):
    return getDocumentById(doc_id)
#TODO
# #Post de Document
@app.route("/mobile/Document/", methods=["POST"])
#@auth.login_required
def newDoc():
    return addDocument()
#Delete d'un document
@app.route("/mobile/Document/<int:doc_id>", methods=["DELETE"])
#@auth.login_required
def deleteDoc(doc_id):
    return delDoc(doc_id)
#Lien de qui retourne un document
@app.route('/DocUps/<filename>')
#@auth.login_required
def uploaded_Doc(filename):
    return sendDoc(filename)


################################################
#                                              #
#               Message Accueil                #
#                                              #
################################################
#Get de msg
@app.route("/mobile/MessagesList/")
#@auth.login_required
def MessageList():
    return getMessages()
@app.route("/mobile/Message/<int:msg_id>")
#@auth.login_required
def MessageById(msg_id):
    return getMessageById(msg_id)
@app.route("/mobile/LastMessage/")
#@auth.login_required
def LastMessage():
    return getLastMessage()
#Post de Msg
@app.route("/mobile/Message/", methods=["POST"])
#@auth.login_required
def newMsg():
    return addMessage()
#Delete de msg
@app.route("/mobile/Message/<int:msg_id>", methods=["DELETE"])
#@auth.login_required
def deleteMsg(msg_id):
    return delMsg(msg_id)
#Put de msg afin de modifier le Msg
@app.route("/mobile/Message/<int:msg_id>", methods=["PUT"])
#@auth.login_required
def modifyMsg(msg_id):
    return updateMsg(msg_id)


if __name__ == "__main__":
    app.run()
