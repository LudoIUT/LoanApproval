RABAT Cédric
RABAT Ludovic

Répartition des tâches :

1) Service AccManager : Cédric Rabat
http://1-dot-robust-resource-130621.appspot.com/accManager/account

/getAccount : liste tous les comptes
/add : ajoute un compte
/delete/{lastName} : supprime un compte
/updateRisk/{lastName}/{risk} : modifie un risque
/updateAccount/{lastName}/{account} : modifie le montant d'un compte bancaire

2) Service AppManager : Ludovic Rabat
http://1-dot-robust-resource-130621.appspot.com/appManager/approval

/getApprovals : liste tous les approvals
/add : ajoute un approval
/delete/{lastName} : supprime un approval

Service CheckAccount : Cédric Rabat
http://1-dot-robust-resource-130621.appspot.com/checkAccount/check/

/getRisk/{lastName} : retourne le risque d'une personne

Service LoanApproval : Ludovic Rabat
http://safe-scrubland-69417.herokuapp.com/askCredit

/askCredit : demande un crédit en passant le nom de la personne et le montant

PS : nous avons choisi de faire transiter toutes nos données au format JSON.

Architecture globale du service composite :

	- Le service AccManager a été déployé sur GAE. 
	On retrouve le code de ce service sous le package « fr.accmanager ».
	Ce service permet d’ajouter, supprimer un compte bancaire. 
	IL permet également de récupérer la liste des comptes, de modifier le montant du compte et de modifier le risque du compte. 
	Les entités sont stockées dans la partie Entités du Datastore sur Google Cloud Platform. 
	Le genre de l’entité pour ce service est nommé « bankAccount ». 
	
	- Le service AppManager a été déployé sur GAE. 
	On retrouve le code de ce service sous le package « fr.appmanager ».
	Ce service permet d’ajouter, supprimer un approval.
	IL permet également de récupérer la liste des approvals.
	Les entités sont stockées dans la partie Entités du Datastore sur Google Cloud Platform.
	Le genre de l’entité pour ce service est nommé « Approval ».
	
	- Le service Check_Account a été déployé sur GAE.
	On retrouve le code de ce service sous le package « fr.ckeckaccount ».
	Ce service permet de récupérer le risque suivant un compte bancaire. 

	- Le service composite a été déployé sur Heroku.

	- Le projet contient un autre package nommé « fr.exception ».
	Il comprend les classes exceptions que nous avons créé pour le projet : 

		- AlreadyExistsException : lorsqu’un élément avec le même nom est déjà présent en base de données.
		- ResourceNotFoundException : lorsque l’élément n’existe pas en base de données.
		- InvalidParameterException : lorsqu'un paramètre est invalide

Nous avons configuré un client simple HTML (Client/index.html) permettant d'appeler les actions principales. Vous pouvez le lancer simplement en local.
Nous vous avons préconfiguré des données afin d'effectuer tous les cas de test du LoanApproval :

Name : Martin - Risk : high - Response : accepted
Name : David - Risk : low - Response : accepted
Name : Rabat - Risk : low - Response : refused
Name : Toto - Risk : high - Response : refused

Merci et bonne correction.
