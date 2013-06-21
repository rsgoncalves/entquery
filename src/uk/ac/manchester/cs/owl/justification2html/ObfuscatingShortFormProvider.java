package uk.ac.manchester.cs.owl.justification2html;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 29-Oct-2010
 */
public class ObfuscatingShortFormProvider implements ShortFormProvider {

    private RenamingVisitor visitor = new RenamingVisitor();

    public String getShortForm(OWLEntity owlEntity) {
        return owlEntity.accept(visitor);
    }

    public void dispose() {
    }

    private class RenamingVisitor implements OWLEntityVisitorEx<String> {

        private Map<OWLEntity, String> entity2ShortFormMap = new HashMap<OWLEntity, String>();

        private Set<OWLClass> clses = new HashSet<OWLClass>();

        private Set<OWLObjectProperty> objectProperties = new HashSet<OWLObjectProperty>();

        private Set<OWLDataProperty> dataProperties = new HashSet<OWLDataProperty>();

        private Set<OWLNamedIndividual> individuals = new HashSet<OWLNamedIndividual>();

        private Set<OWLDatatype> datatypes = new HashSet<OWLDatatype>();

        private String getShortForm(String prefix, int count, OWLEntity entity) {
            String shortForm = entity2ShortFormMap.get(entity);
            if(shortForm == null) {
                shortForm = prefix + count;
                entity2ShortFormMap.put(entity, shortForm);
            }
            return shortForm;
        }

        public String visit(OWLClass owlClass) {
            if(owlClass.isOWLThing()) {
                return "owl:Thing";
            }
            else if(owlClass.isOWLNothing()) {
                return "owl:Nothing";
            }
            else {
                clses.add(owlClass);
                return getShortForm("C", clses.size(), owlClass);
            }
        }

        public String visit(OWLObjectProperty owlObjectProperty) {
            objectProperties.add(owlObjectProperty);
            return getShortForm("prop", objectProperties.size(), owlObjectProperty);
        }

        public String visit(OWLDataProperty owlDataProperty) {
            dataProperties.add(owlDataProperty);
            return getShortForm("prop", dataProperties.size(), owlDataProperty);
        }

        public String visit(OWLNamedIndividual owlNamedIndividual) {
            individuals.add(owlNamedIndividual);
            return getShortForm("ind", individuals.size(), owlNamedIndividual);
        }

        public String visit(OWLDatatype datatype) {
            datatypes.add(datatype);
            return getShortForm("dt", datatypes.size(), datatype);
        }

        public String visit(OWLAnnotationProperty owlAnnotationProperty) {
            return null;
        }
    }
}
