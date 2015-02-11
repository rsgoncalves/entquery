entquery
========

#### a simple Web-based query interface using extended entailment regimes ####

A demo instance is available [here](http://entquery-rsgtools.rhcloud.com/).


basics
--------------------

The query engine takes an ontology and either a set of queries formulated in **Manchester syntax**, or a query-ontology file (where variables are encoded as entities under the namespace **http://semanticweb.org/variable**).

Examples of queries formulated directly in the application: 

* ?x SubClassOf A
* B SubClassOf p some ?y

By default, the application seeks variable replacements in the form of (complex) concepts explicit in the given ontology. Optionally, the tool can use an advanced entailment grammar where variables are replaced with:

* C and D
* C or D
* r some C
* r only C
* not C

Where C, D are (complex) concepts in the ontology, and r is an atomic role.


dependencies
--------------------
Currently the query engine uses the reasoners: FaCT++ v1.5.3, Pellet v2.2.2, HermiT v1.3.6 and JFact v0.9, and is based on the OWL API v3.3.

Note that, in order to use this in your system, you will need the appropriate FaCT++ library.

[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/rsgoncalves/entquery/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

