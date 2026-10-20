# TP2 REST : Service web « Calculatrice » avec JAX-RS et Tomcat

Matière : Architecture MicroServices, MPSRCC1
Auteure : Nour Chtourou

## Lancer le projet
- Prérequis : JDK 17+, Tomcat 10.1.61, Maven (wrapper inclus)
- Construire : `.\mvnw.cmd clean package` (produit `target/RestWebApp.war`)
- Déployer dans Tomcat (IntelliJ, contexte `/RestWebApp`)
- URL de base : `http://localhost:8080/RestWebApp/api`
- Les requêtes de test sont dans `calcul.http`

## Partie A : Questions de conception
1. **GET et non POST** : un calcul direct ne crée ni ne modifie rien sur le serveur, il demande seulement un résultat. GET est le verbe prévu pour la lecture.
2. **Partie après le `?`** : la chaîne de requête (query string), qui contient les paramètres de requête (`a=2&b=3`).
3. **`/operations` et `/operations/3`** : `/operations` désigne la collection de toutes les opérations, `/operations/3` une seule ressource, l'opération d'identifiant 3.
4. **Opération inexistante** : le serveur renvoie le statut 404 (Not Found).

## Partie C : Somme

Requête : `GET /RestWebApp/api/calcul/somme?a=2&b=3`

| Outil | Statut | Corps | Capture |
|---|---|---|---|
| Navigateur | 200 | 5 | captures/somme-navigateur.png |
| IntelliJ (`calcul.http`) | 200 | 5 | captures/somme-intellij.png |
| Postman | 200 OK | 5 | captures/somme-postman.png |
| curl | 200 | 5 | captures/somme-curl.png |

**Question C.1** (POST au lieu de GET) :
- Statut obtenu : _à compléter après le test Postman_
- Explication : la méthode `somme` est annotée `@GET` uniquement, donc JAX-RS refuse les autres verbes sur cette URI.

## Partie D
_à compléter_

## Partie E
_à compléter_

## Partie F : tests (requête, statut, corps)
_à compléter_

## Questions de synthèse
_à compléter_