package edu.stanford.nlp.international.arabic.process;

import java.util.ArrayList;
import java.util.Collection;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.sequences.Clique;
import edu.stanford.nlp.sequences.FeatureFactory;
import edu.stanford.nlp.sequences.SeqClassifierFlags;
import edu.stanford.nlp.util.Characters;
import edu.stanford.nlp.util.Generics;
import edu.stanford.nlp.util.PaddedList;

/**
 * Feature factory for the IOB clitic segmentation model described by
 * Green and DeNero (2012).
 * 
 * @author Spence Green
 *
 * @param <IN>
 */
public class ArabicSegmenterFeatureFactory<IN extends CoreLabel> extends FeatureFactory<IN> {
  
  private static final long serialVersionUID = -4560226365250020067L;
  
  public void init(SeqClassifierFlags flags) {
    super.init(flags);
  }

  /**
   * Extracts all the features from the input data at a certain index.
   *
   * @param cInfo The complete data set as a List of WordInfo
   * @param loc  The index at which to extract features.
   */
  public Collection<String> getCliqueFeatures(PaddedList<IN> cInfo, int loc, Clique clique) {
    Collection<String> features = Generics.newHashSet();

    if (clique == cliqueC) {
      addAllInterningAndSuffixing(features, featuresC(cInfo, loc), "C");
    } else if (clique == cliqueCpC) {
      addAllInterningAndSuffixing(features, featuresCpC(cInfo, loc), "CpC");
    } else if (clique == cliqueCp2C) {
      addAllInterningAndSuffixing(features, featuresCp2C(cInfo, loc), "Cp2C");
    } else if (clique == cliqueCp3C) {
      addAllInterningAndSuffixing(features, featuresCp3C(cInfo, loc), "Cp3C");
    }

    return features;
  }

  protected Collection<String> featuresC(PaddedList<? extends CoreLabel> cInfo, int loc) {
    Collection<String> features = new ArrayList<String>();
    CoreLabel c = cInfo.get(loc);
    CoreLabel n = cInfo.get(loc + 1);
    CoreLabel n2 = cInfo.get(loc + 2);
    CoreLabel p = cInfo.get(loc - 1);
    CoreLabel p2 = cInfo.get(loc - 2);

    String charc = c.get(CoreAnnotations.CharAnnotation.class);
    String charn = n.get(CoreAnnotations.CharAnnotation.class);
    String charn2 = n2.get(CoreAnnotations.CharAnnotation.class);
    String charp = p.get(CoreAnnotations.CharAnnotation.class);
    String charp2 = p2.get(CoreAnnotations.CharAnnotation.class);

    // Default feature set...a 5 character window
    // plus a few other language-independent features
    features.add(charc +"-c");
    features.add(charn + "-n1");
    features.add(charn2 + "-n2" );
    features.add(charp + "-p");
    features.add(charp2 + "-p2");
    
    // Length feature 
    if (charc.length() > 1) {
      features.add("length");
    }
    
    // Character-level class features
    boolean seenPunc = false;
    boolean seenDigit = false;
    for (int i = 0; i < charc.length(); ++i) {
      char charcC = charc.charAt(i);
      if ( ! seenPunc && Characters.isPunctuation(charcC)) {
        seenPunc = true;
        features.add("haspunc");        
      }
      if ( ! seenDigit && Character.isDigit(charcC)) {
        seenDigit = true;
        features.add("hasdigit");        
      }
      String cuBlock = Characters.unicodeBlockStringOf(charcC);
      features.add(cuBlock + "-uBlock");
      String cuType = String.valueOf(Character.getType(charcC));
      features.add(cuType + "-uType");
    }

    // Indicator transition feature
    features.add("cliqueC");

    return features;
  }

  private Collection<String> featuresCpC(PaddedList<IN> cInfo, int loc) {
    Collection<String> features = new ArrayList<String>();
    CoreLabel c = cInfo.get(loc);
    CoreLabel p = cInfo.get(loc - 1);

    String charc = c.get(CoreAnnotations.CharAnnotation.class);
    String charp = p.get(CoreAnnotations.CharAnnotation.class);
    
    features.add(charc + charp + "-cngram");
    
    // Indicator transition feature
    features.add("cliqueCpC");
    
    return features;
  }

  private Collection<String> featuresCp2C(PaddedList<IN> cInfo, int loc) {
    Collection<String> features = new ArrayList<String>();
    CoreLabel c = cInfo.get(loc);
    CoreLabel p = cInfo.get(loc - 1);
    CoreLabel p2 = cInfo.get(loc - 2);

    String charc = c.get(CoreAnnotations.CharAnnotation.class);
    String charp = p.get(CoreAnnotations.CharAnnotation.class);
    String charp2 = p2.get(CoreAnnotations.CharAnnotation.class);

    features.add(charc + charp + charp2 + "-cngram");

    // Indicator transition feature
    features.add("cliqueCp2C");
    
    return features;
  }

  private Collection<String> featuresCp3C(PaddedList<IN> cInfo, int loc) {
    Collection<String> features = new ArrayList<String>();
    CoreLabel c = cInfo.get(loc);
    CoreLabel p = cInfo.get(loc - 1);
    CoreLabel p2 = cInfo.get(loc - 2);
    CoreLabel p3 = cInfo.get(loc - 3);

    String charc = c.get(CoreAnnotations.CharAnnotation.class);
    String charp = p.get(CoreAnnotations.CharAnnotation.class);
    String charp2 = p2.get(CoreAnnotations.CharAnnotation.class);
    String charp3 = p3.get(CoreAnnotations.CharAnnotation.class);
    
    features.add(charc + charp + charp2 + charp3 + "-cngram");
    
    // Indicator transition feature
    features.add("cliqueCp3C");
    
    return features;
  }
}
