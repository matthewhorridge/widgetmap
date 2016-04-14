package edu.stanford.protege.widgetmap.server.node;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22/02/16
 */
public enum  SerializationVocabulary {

    NODE("node"),

    ID("id"),

    WEIGHT("weight"),

    CHILDREN("children"),

    DIRECTION("direction"),

    COLUMN("COLUMN"),

    ROW("ROW");



    private final String vocabulary;



    SerializationVocabulary(String vocabulary) {
        this.vocabulary = vocabulary;
    }

    public String getVocabulary() {
        return vocabulary;
    }
}
