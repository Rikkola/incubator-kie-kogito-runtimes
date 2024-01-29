package org.kie.kogito.yard;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractYaRDModels implements YaRDModels {

    protected Map<String, YaRDModel> content = new HashMap<>();

    @Override
    public YaRDModel getModel(String name) {
        return content.get(name);
    }

    protected void add(InputStreamReader ier) {
        final String yaml = new BufferedReader(ier).lines().collect(Collectors.joining("\n"));

        final YaRDModel model = new YaRDModel(yaml);
        content.put(model.getName(), model);
    }

    protected static java.io.InputStreamReader readResource(InputStream stream) {
        if (org.kie.kogito.internal.RuntimeEnvironment.isJdk()) {
            return new java.io.InputStreamReader(stream);
        }
        try {
            byte[] bytes = org.drools.util.IoUtils.readBytesFromInputStream(stream);
            java.io.ByteArrayInputStream byteArrayInputStream = new java.io.ByteArrayInputStream(bytes);
            return new java.io.InputStreamReader(byteArrayInputStream);
        } catch (java.io.IOException e) {
            throw new java.io.UncheckedIOException(e);
        }
    }
}
