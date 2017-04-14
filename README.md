# ApplicationARE

## Synopsis

Le but de cette application est de proposer un outil mobile qui sera utile aux Étudiants ainsi qu’aux responsables ARE pour appuyer le cours d’Aide à la recherche d’Entreprise.
Cette application sera disponible seulement pour les imeriens et IMERIR et pourra être utilisée partout, le but est de pouvoir rapidement vérifier les offres disponibles, peu importe l’endroit où se situe l’étudiant, les responsables ARE eux pourront déposer des offres que les entreprises leurs envoient et aussi avoir un suivi des étudiants. <br>
Nous réalisons cette application dans le cadre d’un projet pour nos études afin d’obtenir de l’expérience dans le domaine du développement des applications mobiles et à terme de proposer un outil pratique et utilisé par les étudiants et l’école. <br>Le résultat attendu sera de fournir une application native iOS et Android correspondant au cahier des charges défini.

L’objectif principal sera de proposer un accès à toutes ces fonctions qu’importe l’endroit où se trouve un utilisateur tant qu’il dispose d’un appareil compatible et d’une connexion internet.

## Deux accès différents

L'étudiant :
Les étudiants pourront consulter les offres lier à leur formation (CDPIR, CDSM ou UPVD), les derniers messages des responsables, les documents (CV, Lettre de motivation ...) et leurs espaces profil ou ils pourront ajouter leurs cv et lettre de motivation (Dashboard et Etiquette).<br>
Pendent qu'il consulte une offre ils auront la possibilité de téléphone directement le recruteur ou l'envoyer un email. <br>
Pendent qu’il consulté un document ils auront la possibilité de télécharger le PDF qu’ils sont en train de lire. <br>
Dans profil ils auront accès à leurs donné personnel (Nom, Prénom, email et téléphone)
Ainsi que la possibilité d’envoyer leurs CV, Lettre de motivation, Dashboard et Etiquette que pourra consulter un Responsable ARE.

Responsable ARE : 
Le responsable aura accès à toutes les fonctionnalités d’un étudiant. <br>
Sur la page offre en plus il pourra ajouter un message d’accueil ou crée une nouvelle offre.
Dans une offre il aura la possibilité de supprimer l’offre qu’il consulte. <br>
Sur la page document en plus il pourra ajouter un document. <br>
Dans le document il pourra supprimer le document qu’il consulte. <br>
La partie profil du responsable ARE aura une liste de tous les étudiants enregistrés sur l’application avec la possibilité de trier avec les formations (CDPIR, CDSM et UPVD) ainsi qu’une barre recherchée d’étudiant. <br>
Il pourra alors consulter un profil étudiant et aura accès a c’est information personnelle (Nom, Prénom, email et téléphone) il pourra contacter directement l’étudiant par mail ou téléphone et consulter les documents qu’il a envoyés.

## Installation

Projet sous Android Studio<br>
Version du SDK 25<br>
SDK minium 19

## Attention
LE WEB SERVICE PERMET POUR L’INSTANT QUE LA LECTURE (get) DONC PAS D’AJOUT (utilisateur, document, message, offre…) POSSIBLE DANS LA BDD(post) POUR LE MOMENT.

## Avancement actuel version Android 
Page de connexion ARE – IMERIR :

<img src="https://cloud.githubusercontent.com/assets/24228893/25006489/5d1ecd5a-205d-11e7-8754-4017f0a8e62e.png" alt="" width="200" height="350"><img src="https://cloud.githubusercontent.com/assets/24228893/25006492/5d2a4c98-205d-11e7-9a8a-6d6ea32d3a2b.png" alt="" width="200" height="350">

Page d’inscription Etudiant - Page d’inscription Responsable ARE

<img src="https://cloud.githubusercontent.com/assets/24228893/25006491/5d261ad8-205d-11e7-959e-debacb5de5ea.png" alt="" width="200" height="350"><img src="https://cloud.githubusercontent.com/assets/24228893/25006490/5d24e564-205d-11e7-82ed-0e8301aa56da.png" alt="" width="200" height="350">

Page d’offre – Page d’offre avec bouton ajout Message et Offre

<img src="https://cloud.githubusercontent.com/assets/24228893/25007521/1a4a987a-2061-11e7-964a-b5a9a86e79de.png" alt="" width="200" height="350"><img src="https://cloud.githubusercontent.com/assets/24228893/25007524/1a7132f0-2061-11e7-86cb-9304a320ab9b.png" alt="" width="200" height="350">


Page message d’accueil

<img src="https://cloud.githubusercontent.com/assets/24228893/25007526/1a7cd25e-2061-11e7-92a6-1b566b6556cf.png" alt="" width="200" height="350">


Page détail offre –avec bouton appel et email

<img src="https://cloud.githubusercontent.com/assets/24228893/25007527/1a989002-2061-11e7-8d49-ee60a9f3b5d5.png" alt="" width="200" height="350"><img src="https://cloud.githubusercontent.com/assets/24228893/25007525/1a76a4c4-2061-11e7-88f7-39ca89c6843c.png" alt="" width="200" height="350">


Page document avec bouton ajout document

<img src="https://cloud.githubusercontent.com/assets/24228893/25007523/1a6d35f6-2061-11e7-9693-d10a001e3f8a.png" alt="" width="200" height="350">


Page détail document

<img src="https://cloud.githubusercontent.com/assets/24228893/25007522/1a66a3d0-2061-11e7-8566-893b0b9c40df.png" alt="" width="200" height="350">

## Tests à réaliser durent le déroulement de l’application.
-	Dans inscription étudiant et responsable ARE que les champs sois remplit ou pas et qu’il retourne les bons messages d’erreur. <br>
-	Vérifier la présence de l’icône de chargement quand une connexion au serveur et faites. Ou quand aucune connexion est établie.


## En cours
-	Web Service corriger l’envoi des documents <br>
-	Création de l’écran ajout d’une offre <br>
-	Optimisation générale de l’application <br>
-	Optimisation du Web Service <br>


## Dernière problématique rencontrée

-	Trouvé une solution pour gères les deux comptes Etudiant et Responsable ARE <br>

À la connexion si c’est un étudiant basculé sur ce qu’il doit avoir accès. <br>
À la connexion si c’est un responsable ARE basculé sur ce qu’il doit avoir accès. <br>

Masquer le bouton ajouter un Message et ajouter une offre si le compte connecté est un compte étudiant. <br>
Masquer le champ détails visibles par les responsables si compte utilisateur connecté est étudiant. <br>
Masquer le bouton ajouter document si Utilisateur connecté et un compte étudiant. <br>
Afficher la page Profil Etudiant si c’est un étudiant qui c’est connecter. <br>
Afficher la page Profil Responsable ARE si c’est un responsable ARE. <br>


## A faire

-	Affichage des offres en fonction de la formation de l’utilisateur connecté. <br>
    -	Si responsable afficher toutes les offres. <br>
    -	Si étudiant CDPIR afficher que les offre CDPIR. <br>
    - Si étudiant CDSM afficher que les offre CDSM. <br>
    - Si Etudiant UPVD afficher que les offre UPVD. <br>
-	Création de la page profil étudiante. <br>
-	Création de la page Profil Responsable avec suivie des étudiants. <br>
-	Gérer les sharedPreferences pour enregistre le nom, prénom et le type de formation de l’étudiant connecter pour les réutiliser dans profil étudiant par exemple. <br>
-	Intégrer nativement l’envoi de mail via smtp sans utiliser d’application tierce pour offre détail. <br>
-	Quand un nouvel utilisateur est créé faire un retour à la page de connexion et s’il se connecte f directement après, afficher un cas d’erreur qu’il n’a pas validée sont adresse mail. <br>
 [Quand le POSTUser est effectué sur registerActivity faire un retour à la LoginActivity (Attention bien gérer la destruction de l'activité au changement) + Ajout d'un message à l'accueil]
<img src="https://cloud.githubusercontent.com/assets/24228893/25009451/007f7afe-2067-11e7-8b44-8641a33fa5d4.png" alt="" width="350" height="350">


## Avancement actuel version iOS
Entraînement et exercice lier au développent iOS qui amèneront a la création du projet.
## Tests à réaliser durent le déroulement de l’application.
## En cours
## A faire

## Contributeurs

IMERIR - Par Bouillon Maxime et Sire Remy


<img src="https://cloud.githubusercontent.com/assets/24228893/25009503/26831db4-2067-11e7-8d28-91a929b16b74.jpg" alt="" width="200" height="200" >


