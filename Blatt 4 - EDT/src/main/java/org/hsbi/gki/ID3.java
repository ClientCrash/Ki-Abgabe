package org.hsbi.gki;

import java.util.Hashtable;
import java.util.List;

import aima.core.learning.framework.Attribute;
import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.DataSetSpecification;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.StringAttribute;
import aima.core.learning.framework.StringAttributeSpecification;
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

    static final StringAttributeSpecification ALTER =
            new StringAttributeSpecification("alter", new String[]{">=35", "<35"});
    static final StringAttributeSpecification EINKOMMEN =
            new StringAttributeSpecification("einkommen", new String[]{"low", "high"});
    static final StringAttributeSpecification BILDUNG =
            new StringAttributeSpecification("bildung", new String[]{"abi", "bachelor", "master"});
    static final StringAttributeSpecification KANIDAT =
            new StringAttributeSpecification("kanidat", new String[]{"O", "M"});

    static DataSet getData() {

        List<Example> daten = List.of(
                buildExample(">=35", "high", "abi", "O"),
                buildExample("<35", "low", "master", "O"),
                buildExample(">=35", "high", "bachelor", "M"),
                buildExample(">=35", "low", "abi", "M"),
                buildExample(">=35", "high", "master", "O"),
                buildExample("<35", "high", "bachelor", "O"),
                buildExample("<35", "low", "abi", "M")
        );

        DataSetSpecification spec = new DataSetSpecification();
        spec.defineStringAttribute("alter", new String[]{">=35", "<35"});
        spec.defineStringAttribute("einkommen", new String[]{"low", "high"});
        spec.defineStringAttribute("bildung", new String[]{"abi", "bachelor", "master"});
        spec.defineStringAttribute("kanidat", new String[]{"O", "M"});
        spec.setTarget("kanidat");

        DataSet ds = new DataSet(spec);
        for (var entry : daten) {
            ds.add(entry);
        }

        return ds;
    }

    static Example buildExample(String a, String inc, String edu, String can) {
        Hashtable<String, Attribute> daten = new Hashtable<>();
        daten.put("alter", new StringAttribute(a, ALTER));
        daten.put("einkommen", new StringAttribute(inc, EINKOMMEN));
        daten.put("bildung", new StringAttribute(edu, BILDUNG));
        daten.put("kanidat", new StringAttribute(can, KANIDAT));

        Example bsp = new Example(daten, new StringAttribute(can, KANIDAT));

        return bsp;
    }
}