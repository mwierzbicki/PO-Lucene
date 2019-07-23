package pl.edu.mimuw.mw406415.Indexer.LangDetector;

import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageResult;

import java.util.HashSet;

public class LangDetector {
    static private OptimaizeLangDetector detector;
    static public String detect(String text) {
        if (detector == null) {
            try {
                detector = new OptimaizeLangDetector();
                HashSet<String> supportedLangs = new HashSet<>();
                supportedLangs.add("en");
                supportedLangs.add("pl");
                detector.loadModels(supportedLangs);
            } catch (Exception e) {
                System.err.println("FATAL: Cannot open language recognition models");
                System.exit(1);
            }
        }
        LanguageResult result = detector.detect(text);
        if (result.isLanguage("pl")) {
            return "pl";
        } else {
            return "en";
        }
    }
}
