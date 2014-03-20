
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.io.EncodingPrintWriter;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.objectbank.DelimitRegExIterator;
import edu.stanford.nlp.objectbank.IteratorFromReaderFactory;
import edu.stanford.nlp.sequences.ColumnDocumentReaderAndWriter;
import edu.stanford.nlp.sequences.DocumentReaderAndWriter;
import edu.stanford.nlp.sequences.SeqClassifierFlags;
import edu.stanford.nlp.util.StringUtils;

import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;


/**
 * This is a very simple demo of calling the Chinese Word Segmenter
 * programmatically.  It assumes an input file in UTF8.
 * <p/>
 * <code>
 * Usage: java -mx1g -cp seg.jar SegDemo fileName
 * </code>
 * This will run correctly in the distribution home directory.  To
 * run in general, the properties for where to find dictionaries or
 * normalizations have to be set.
 *
 * @author Christopher Manning
 */

public class SegDemo {

    public static void main(String[] args) throws Exception {
//    if (args.length != 1) {
//      System.err.println("usage: java -mx1g SegDemo filename");
//      return;
//    }

        String file = "src/test/resources/test.simp.utf8";
        String model = "data/ctb.gz";

        Properties props = new Properties();
        props.setProperty("sighanCorporaDict", "data");
        // props.setProperty("NormalizationTable", "data/norm.simp.utf8");
        // props.setProperty("normTableEncoding", "UTF-8");
        // below is needed because CTBSegDocumentIteratorFactory accesses it
        props.setProperty("serDictionary", "data/dict-chris6.ser.gz");
        props.setProperty("testFile", file);
        props.setProperty("inputEncoding", "UTF-8");
        props.setProperty("sighanPostProcessing", "true");

        URL url = SegDemo.class.getClassLoader().getResource(model);
        if (url == null)
            throw new IllegalArgumentException("Please download ctb.gz and put it in src/main/java");

        CRFClassifier<CoreLabel> segmenter = new CRFClassifier<CoreLabel>(props);
        segmenter.loadClassifierNoExceptions(model, props);
        segmenter.classifyAndWriteAnswers(file);

    }

}
