package org.hsbi.gki;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.learning.framework.Attribute;
import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.DataSetSpecification;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.NumericAttribute;
import aima.core.learning.framework.NumericAttributeSpecification;

public class CAL3 {
    static final NumericAttributeSpecification ALTER = new NumericAttributeSpecification("alter");
    static final NumericAttributeSpecification EINKOMMEN = new NumericAttributeSpecification("einkommen");
    static final NumericAttributeSpecification BILDUNG = new NumericAttributeSpecification("bildung");
    static final NumericAttributeSpecification KANIDAT = new NumericAttributeSpecification("kanidat");

    static DataSet getData() {
        
        List<Example> daten = List.of(
            buildExample(0, 1, 0, 1),
            buildExample(1, 0,2, 1),
            buildExample(0, 1, 1, 2),
            buildExample(0, 0, 0, 2),
            buildExample(0, 1, 2, 1),
            buildExample(1, 1, 1, 1),
            buildExample(1, 0, 0, 2)
            );
        
            DataSetSpecification spec = new DataSetSpecification();
        spec.defineNumericAttribute("alter"); // 0 = >= 35, 1= < 35
        spec.defineNumericAttribute("einkommen"); // 0 = low, 1 = high
        spec.defineNumericAttribute("bildung");// abi = 0, bachelor = 1, master = 2
        spec.defineNumericAttribute("kanidat"); // O = 1, M = 2
        spec.setTarget("kanidat");

        
            
        DataSet ds = new DataSet(spec);
        for (var entry : daten) {
            ds.add(entry);
        }

        return ds;
    }
    
    static Example buildExample(int a, int inc, int edu, int can) {
        Hashtable<String, Attribute> daten = new Hashtable<>();
        daten.put("alter", (Attribute) new NumericAttribute(a, ALTER));
        daten.put("einkommen", (Attribute) new NumericAttribute(inc, EINKOMMEN));
        daten.put("bildung", (Attribute) new NumericAttribute(edu, BILDUNG));
        daten.put("kanidat", (Attribute) new NumericAttribute(can, KANIDAT));

        Example bsp = new Example(daten, new NumericAttribute(can, KANIDAT));

        return bsp;
    }
    

    public static void main(String[] args) {
        DataSet ds = getData();

        CAL3 cal = new CAL3(4, .7, ds.getAttributeNames());

        List<Bsp> beispielDaten = Bsp.fromDataSet(ds);
        
        for (int i = 0; i < beispielDaten.size(); i++) {
            cal.lernen(beispielDaten.get(i));
        }
    }

    private final Tree root = new Tree();
    private final double schwelle1, schwelle2;
    private final List<String> merkmale;

    public CAL3(double schwelle1, double schwelle2, List<String> merkmale) {
        this.schwelle1 = schwelle1;
        this.schwelle2 = schwelle2;
        this.merkmale = merkmale;
    }

    public void lernen(Bsp bsp) {
        root.lernen(bsp, new HashSet<>(), schwelle1, schwelle2, merkmale);
    }

    public String vorhersagen(Bsp bsp) {
        return root.vorhersagen(bsp);
    }

    static class Tree {
        boolean isLeaf = true;
        String endClass;
        HashMap<String, Integer> klassenZaehler = new HashMap<>();

        String teilungsMerkmal;
        HashMap<Double, Tree> Nodes;

        void lernen(Bsp bsp, Set<String> used, double s1, double s2, List<String> alleMerkmale) {
            if (!isLeaf) {
                double wert = bsp.holeMerkmal(teilungsMerkmal);
                Tree kind = Nodes.get(wert);
                if (kind == null) {
                    kind = new Tree();
                    Nodes.put(wert, kind);
                }
                Set<String> newUsed = kopiere(used);
                newUsed.add(teilungsMerkmal);
                kind.lernen(bsp, newUsed, s1, s2, alleMerkmale);
                return;
            }

            if (endClass != null)
                return;

            String klasse = bsp.zielKlasse();
            Integer alter = klassenZaehler.get(klasse);
            if (alter == null) {
                klassenZaehler.put(klasse, 1);
            } else {
                klassenZaehler.put(klasse, alter + 1);
            }
            
            int gesamt = 0;
            for (Integer anzahl : klassenZaehler.values()) {
                gesamt += anzahl;
            }

            if (gesamt < s1)
                return;

            String maxKlasse = null;
            int maxAnzahl = 0;
            for (Map.Entry<String, Integer> eintrag : klassenZaehler.entrySet()) {
                if (eintrag.getValue() > maxAnzahl) {
                    maxAnzahl = eintrag.getValue();
                    maxKlasse = eintrag.getKey();
                }
            }

            double anteil = (double) maxAnzahl / gesamt;

            if (anteil >= s2) {
                endClass = maxKlasse;
                klassenZaehler = null;
                return;
            }

            String naechstes = findeNaechstesMerkmal(used, alleMerkmale);
            if (naechstes == null)
                return;

            teilungsMerkmal = naechstes;
            isLeaf = false;
            Nodes = new HashMap<>();

            double wert = bsp.holeMerkmal(teilungsMerkmal);
            Tree neuesKind = new Tree();
            neuesKind.klassenZaehler.put(klasse, 1);
            Nodes.put(wert, neuesKind);
        }

        String vorhersagen(Bsp bsp) {
            if (!isLeaf) {
                Tree kind = Nodes.get(bsp.holeMerkmal(teilungsMerkmal));
                if (kind == null) {
                    return null;
                }
                return kind.vorhersagen(bsp);
            }
            if (endClass != null)
                return endClass;
            if (klassenZaehler.isEmpty())
                return null;
            
            String beste = null;
            int maxWert = 0;
            for (Map.Entry<String, Integer> eintrag : klassenZaehler.entrySet()) {
                if (eintrag.getValue() > maxWert) {
                    maxWert = eintrag.getValue();
                    beste = eintrag.getKey();
                }
            }
            return beste;
        }

        private static Set<String> kopiere(Set<String> alt) {
            Set<String> neu = new HashSet<>();
            for (String s : alt) {
                neu.add(s);
            }
            return neu;
        }

        private static String findeNaechstesMerkmal(Set<String> benutzt, List<String> alle) {
            for (int i = 0; i < alle.size(); i++) {
                String merkmal = alle.get(i);
                if (!benutzt.contains(merkmal)) {
                    return merkmal;
                }
            }
            return null;
        }
    }

    static class Bsp {
        String zielWert = "";
        HashMap<String, Double> daten = new HashMap<>();

        public double holeMerkmal(String name) {
            return daten.get(name);
        }

        public String zielKlasse() {
            return zielWert;
        }

        static List<Bsp> fromDataSet(DataSet ds) {
            List<Bsp> beispiele = new ArrayList<>();

            String ziel = ds.getTargetAttributeName();

            for (int i = 0; i < ds.examples.size(); i++) {
                var e = ds.examples.get(i);
                Bsp aktuell = new Bsp();
                aktuell.zielWert = ziel;
                List<String> attrs = ds.specification.getAttributeNames();
                for (int j = 0; j < attrs.size(); j++) {
                    String attr = attrs.get(j);
                    aktuell.daten.put(attr, e.getAttributeValueAsDouble(attr));
                }
                beispiele.add(aktuell);
            }
            return beispiele;
        }
    }
}
