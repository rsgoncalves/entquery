entquery
========

#### a simple Web-based query interface using extended entailment regimes ####

A demo instance is available [here](http://owl.cs.manchester.ac.uk/entquery).


basics
--------------------

The query engine takes an ontology and either a set of queries formulated in **Manchester syntax**, or a query-ontology file (where variables are encoded as entities under the namespace **http://semanticweb.org/variable**).

Examples of queries formulated directly in the application: 

* ?x SubClassOf A
* B SubClassOf p some ?y

The application has the option to provide only inferred answers, as well as using an advanced entailment grammar, where variables are replaced with:

* C and D
* C or D
* r some C
* r only C
* not C


dependencies
--------------------
Currently the query engine uses the reasoners: FaCT++ v1.5.3, Pellet v2.2.2, HermiT v1.3.6 and JFact v0.9, and is based on the OWL API v3.3.