package uk.ac.manchester.cs.owl.justification2html;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 29-Oct-2010
 */
public class RenderJustification {

    public static void main(String[] args) {
        try {
            FileDialog fileDialog = new FileDialog(new JFrame(), "Select justification");

           fileDialog.setVisible(true);
            //File dir = new File(args[0]);
            File dir = new File(fileDialog.getDirectory());

            int count = 1;
            for(File listedfile : dir.listFiles() ) {
                if(listedfile.getName().endsWith(".owl")) {
                    //String f = fileDialog.getFile();
                    String f = listedfile.getName();
                    String directory = listedfile.getParent();
                    //String directory = fileDialog.getDirectory();
                    File file = new File(directory, f);
                    System.out.println(file);
                    JustificationOntologyParser parser = new JustificationOntologyParser();
                    Explanation<OWLAxiom> expl = parser.parser(new FileInputStream(file));
                    System.out.println(expl);
                    System.out.println("------------------------------------------------------------------------");
                    ManchesterSyntaxConverter converter = new ManchesterSyntaxConverter();
                    String html = converter.convertToHTML(expl, new ObfuscatingShortFormProvider());
                    System.out.println(html);
                    System.out.println("------------------------------------------------------------------------");
                    DLSyntaxConverter dlSyntaxConverter = new DLSyntaxConverter();
                    String html2 = dlSyntaxConverter.convertToHTML(expl, new ObfuscatingShortFormProvider());
                    System.out.println(html2);

                    J2HTMLOutput.saveToHTML(html, html2, file, count);
                    count++;

                }
            }

            System.exit(0);
        }
        catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
