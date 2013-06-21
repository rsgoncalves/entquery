package uk.ac.manchester.cs.owl.justification2html;

import java.io.*;

/**
 * Created by
 * User: Samantha Bail
 * Date: 15/04/2011
 * Time: 18:30
 * The University of Manchester
 */


public class J2HTMLOutput {

    public static void saveToHTML(String html, String html2, File f, int count) {
        try {
            File outputfile = new File (f.getAbsolutePath() + ".html");
            System.out.println(outputfile.getAbsolutePath());
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(outputfile), "UTF8");
            out.write("<html><head>");
            out.write("<LINK REL=StyleSheet HREF='style_screen.css' TYPE='text/css' MEDIA='screen'>");
            out.write("<LINK REL=StyleSheet HREF='style_print.css' TYPE='text/css' MEDIA='print'>");
            out.write("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/> ");

            out.write("</head><body><div class='page'>");
            out.write("<span class ='jcount'>J "+ count +"</span>");

            out.write("<div class='just'>");
            out.write("<div class='entailment'><span class='ent_heading'>Entailment:</span>");
            out.write("C1 <span class='axkw'>SubClassOf</span> C2</div>");
            out.write(html);
            out.write("</div>");

            out.write("<div class='just'>");
            out.write("<div class='entailment'><span class='ent_heading'>Entailment:</span>");
            out.write("C1 <span>&#8849;</span> C2</div>");

            out.write(html2);
            out.write("</div>");
            out.write("<span class ='filepath'>"+ f.getName()+"</span>");



            out.write("</div></body></html>");
            out.close();



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
