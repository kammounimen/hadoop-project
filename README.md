# 🐘 Hadoop Project — Analyse de données de ventes

> Ensemble de projets d'analyse de données avec **Apache Hadoop** : chiffre d'affaires, ventes par pays et par mois, produits les plus vendus, meilleurs clients.

![Hadoop](https://img.shields.io/badge/Apache%20Hadoop-HDFS%20%2B%20MapReduce-66CCFF?logo=apachehadoop&logoColor=black)
![Big Data](https://img.shields.io/badge/Domaine-Big%20Data-orange)

---

## 📌 Table des matières

1. [Présentation](#présentation)
2. [Analyses réalisées](#analyses-réalisées)
3. [Structure du dépôt](#structure-du-dépôt)
4. [Architecture et flux de traitement](#architecture-et-flux-de-traitement)
5. [Jeu de données](#jeu-de-données)
6. [Prérequis](#prérequis)
7. [Installation de l'environnement Hadoop](#installation-de-lenvironnement-hadoop)
8. [Exécution d'un job](#exécution-dun-job)
9. [Résultats](#résultats)
10. [Bonnes pratiques Git pour ce dépôt](#bonnes-pratiques-git-pour-ce-dépôt)
11. [Dépannage](#dépannage)
12. [Auteur](#auteur)

---

## Présentation

Ce dépôt regroupe plusieurs **traitements Big Data** exécutés sur un cluster Hadoop (HDFS pour le stockage distribué, MapReduce pour le calcul). Chaque dossier correspond à une analyse indépendante réalisée sur un jeu de données de ventes.

> ✏️ **À compléter :** précisez le contexte (cours, projet de fin de module, entreprise fictive…) et la source du jeu de données.

---

## Analyses réalisées

| Dossier | Analyse | Question métier |
|---|---|---|
| `hadoop_total_revenue` | Chiffre d'affaires total | Combien l'entreprise a-t-elle généré au total ? |
| `hadoop_sales_by_country` | Ventes par pays | Quels marchés rapportent le plus ? |
| `hadoop_sales_by_month` | Ventes par mois | Quelle est l'évolution / la saisonnalité des ventes ? |
| `hadoop_most_sold` | Produits les plus vendus | Quels produits se vendent le plus ? |
| `hadoop_products_by_country` | Produits par pays | Quels produits dominent dans chaque pays ? |
| `hadoop_top_clients` | Meilleurs clients | Qui sont les clients les plus contributeurs ? |
| `hadoop_project` | Projet principal | ✏️ à préciser (projet de base, version consolidée…) |

---

## Structure du dépôt

```
hadoop-project/
├── hadoop_project/                  # Projet principal
├── hadoop_total_revenue/            # Job : chiffre d'affaires total
├── hadoop_sales_by_country/         # Job : ventes par pays
├── hadoop_sales_by_month/           # Job : ventes par mois
├── hadoop_most_sold/                # Job : produits les plus vendus
├── hadoop_products_by_country/      # Job : produits par pays
├── hadoop_top_clients/              # Job : meilleurs clients
└── hadoop-data/
    └── hdfs/namenode/current/       # Données internes du NameNode HDFS
```

> ⚠️ Le dossier `hadoop-data/` contient des **fichiers internes de HDFS** (métadonnées du NameNode). Il ne devrait pas être versionné : voir [Bonnes pratiques Git](#bonnes-pratiques-git-pour-ce-dépôt).

> ✏️ Détaillez ici le contenu type d'un dossier (ex. `Mapper`, `Reducer`, `Driver`, script d'exécution, données d'entrée).

---

## Architecture et flux de traitement

Chaque analyse suit le modèle **MapReduce** :

```
 Données de ventes (CSV)
          │
          ▼
   ┌──────────────┐
   │     HDFS     │   stockage distribué des fichiers d'entrée
   └──────┬───────┘
          │
          ▼
   ┌──────────────┐
   │    MAP       │   lit chaque ligne, émet des paires (clé, valeur)
   │              │   ex. (pays, montant) ; (mois, montant) ; (client, montant)
   └──────┬───────┘
          │  shuffle & sort (regroupement par clé)
          ▼
   ┌──────────────┐
   │   REDUCE     │   agrège les valeurs par clé (somme, comptage, max…)
   └──────┬───────┘
          │
          ▼
   Résultats écrits dans HDFS (part-r-00000)
```

| Analyse | Clé émise par le Mapper | Agrégation du Reducer |
|---|---|---|
| Chiffre d'affaires total | clé constante | somme des montants |
| Ventes par pays | pays | somme des montants |
| Ventes par mois | mois | somme des montants |
| Produits les plus vendus | produit | somme des quantités (puis tri) |
| Produits par pays | pays + produit | somme des quantités |
| Meilleurs clients | client | somme des montants (puis tri) |

> ✏️ Ce tableau décrit la logique **attendue** de chaque analyse. Vérifiez-le par rapport à votre implémentation réelle.

---

## Jeu de données

> ✏️ **À compléter :**
> - Nom et source du dataset (ex. Kaggle, fourni par l'enseignant…)
> - Taille et nombre de lignes
> - Format (CSV, séparateur, en-tête)

Exemple de description des colonnes :

| Colonne | Description |
|---|---|
| `InvoiceNo` | Numéro de facture |
| `StockCode` / `Description` | Code et nom du produit |
| `Quantity` | Quantité vendue |
| `InvoiceDate` | Date de la vente |
| `UnitPrice` | Prix unitaire |
| `CustomerID` | Identifiant du client |
| `Country` | Pays |

> ✏️ Ces colonnes sont un **exemple** typique d'un jeu de données de ventes ; remplacez-les par celles de votre fichier.

---

## Prérequis

- **Java** 8 ou 11 (selon votre version de Hadoop)
- **Apache Hadoop** 3.x
- Environnement d'exécution (au choix) :
  - installation locale / pseudo-distribuée,
  - ou **Docker** (cluster Hadoop conteneurisé)
- ✏️ Maven / Python, selon le langage utilisé pour vos jobs

---

## Installation de l'environnement Hadoop

```bash
# 1. Cloner le dépôt
git clone https://github.com/kammounimen/hadoop-project.git
cd hadoop-project

# 2. Démarrer HDFS et YARN (installation locale)
start-dfs.sh
start-yarn.sh

# 3. Vérifier que les services tournent
jps
# Attendu : NameNode, DataNode, ResourceManager, NodeManager
```

Interfaces web par défaut (Hadoop 3.x) :

| Service | URL |
|---|---|
| NameNode (HDFS) | http://localhost:9870 |
| ResourceManager (YARN) | http://localhost:8088 |

> ✏️ Si vous utilisez Docker, remplacez cette section par votre `docker-compose` ou la commande de lancement du cluster.

---

## Exécution d'un job

Exemple générique, à adapter au dossier concerné :

```bash
# 1. Créer le répertoire d'entrée dans HDFS et y charger les données
hdfs dfs -mkdir -p /input
hdfs dfs -put data/sales.csv /input/

# 2. Lancer le job (exemple avec un JAR Java)
hadoop jar hadoop_total_revenue/target/total-revenue.jar \
    /input/sales.csv /output/total_revenue

# 3. Consulter le résultat
hdfs dfs -cat /output/total_revenue/part-r-00000

# 4. Récupérer le résultat en local
hdfs dfs -get /output/total_revenue ./results/
```

> ⚠️ Le répertoire de sortie ne doit **pas** exister avant l'exécution. Pour relancer un job :
> ```bash
> hdfs dfs -rm -r /output/total_revenue
> ```

> ✏️ Si vos jobs sont écrits en **Python (Hadoop Streaming)**, utilisez plutôt :
> ```bash
> hadoop jar $HADOOP_HOME/share/hadoop/tools/lib/hadoop-streaming-*.jar \
>     -input /input/sales.csv -output /output/total_revenue \
>     -mapper mapper.py -reducer reducer.py \
>     -file mapper.py -file reducer.py
> ```

---

## Résultats

> ✏️ **À compléter :** ajoutez un extrait ou une capture des résultats obtenus pour chaque analyse.

Exemple de présentation :

```
# Ventes par pays (extrait)
United Kingdom    8 187 806.36
Netherlands         284 661.54
EIRE                263 276.82
```

*(valeurs d'illustration — remplacez-les par vos vrais résultats)*

---

## Bonnes pratiques Git pour ce dépôt

Le dossier `hadoop-data/hdfs/namenode/current` est visible dans le dépôt : ce sont des **fichiers générés par HDFS** (images du système de fichiers, journaux), propres à votre machine. Ils alourdissent le dépôt et peuvent poser des problèmes lors d'un clone sur une autre installation.

Ajoutez un `.gitignore` à la racine :

```gitignore
# Données internes de Hadoop
hadoop-data/

# Résultats et fichiers générés
results/
*.crc
*.class
target/

# Environnements
.env
__pycache__/
```

Puis retirez le dossier du suivi Git :

```bash
git rm -r --cached hadoop-data
git commit -m "Retire les données internes HDFS du dépôt"
```

---

## Dépannage

| Problème | Piste de solution |
|---|---|
| `Output directory already exists` | `hdfs dfs -rm -r /output/<nom>` avant de relancer |
| `Connection refused` sur HDFS | Vérifier que `start-dfs.sh` a été lancé et que `jps` affiche NameNode/DataNode |
| NameNode en *safe mode* | `hdfs dfsadmin -safemode leave` |
| `JAVA_HOME is not set` | Définir `JAVA_HOME` dans `hadoop-env.sh` |
| Job bloqué en `ACCEPTED` | Vérifier que YARN (ResourceManager/NodeManager) tourne et dispose de mémoire |
| `ClassNotFoundException` | Vérifier la classe principale (`Driver`) et le contenu du JAR |

---

## Auteur

**kammounimen** — [github.com/kammounimen](https://github.com/kammounimen)
