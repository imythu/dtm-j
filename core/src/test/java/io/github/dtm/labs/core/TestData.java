package io.github.dtm.labs.core;

/**
 * @author zhuhf
 */
public class TestData {

    public String id;

    public TestData(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TestData{" +
                "id='" + id + '\'' +
                '}';
    }
}
