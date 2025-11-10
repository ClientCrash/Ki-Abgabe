package org.hsbi.gki;

import java.util.Hashtable;
import java.util.List;



import aima.core.learning.framework.Attribute;
import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.DataSetSpecification;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.NumericAttribute;
import aima.core.learning.framework.NumericAttributeSpecification;
import aima.core.learning.learners.DecisionTreeLearner;

public class ID3 {

    public static void main(String[] args) {
        new ID3();
    }

    ID3() {
        DataSet ds = getData();

        DecisionTreeLearner dtl = new DecisionTreeLearner();
        dtl.train(ds); // Uses ID3
    }

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

}