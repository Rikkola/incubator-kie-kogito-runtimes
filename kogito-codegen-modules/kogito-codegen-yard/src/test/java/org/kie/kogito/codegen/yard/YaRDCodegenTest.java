/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.kogito.codegen.yard;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.Properties;

import org.drools.codegen.common.GeneratedFile;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.kie.kogito.codegen.api.AddonsConfig;
import org.kie.kogito.codegen.api.ApplicationSection;
import org.kie.kogito.codegen.api.context.KogitoBuildContext;
import org.kie.kogito.codegen.core.io.CollectedResourceProducer;

import com.github.javaparser.ast.CompilationUnit;

public class YaRDCodegenTest {

    @ParameterizedTest
    @MethodSource("org.kie.kogito.codegen.api.utils.KogitoContextTestUtils#contextBuilders")
    public void testTwoFiles(KogitoBuildContext.Builder contextBuilder) {
        YaRDCodegen codeGenerator = getYaRDCodegen("src/test/resources/yard", contextBuilder);
        Collection<GeneratedFile> generate = codeGenerator.generate();
        Iterator<GeneratedFile> iterator = generate.iterator();

        Optional<ApplicationSection> section = codeGenerator.section();

        CompilationUnit compilationUnit = section.get().compilationUnit();
        String string1 = compilationUnit.toString();
        while (iterator.hasNext()) {
            System.out.println(new String(iterator.next().contents()));
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        }
        System.out.println(string1);
        //        RuntimeException re = Assertions.assertThrows(RuntimeException.class, codeGenerator::generate);
        //        assertEquals("Model name should not be empty", re.getMessage());
    }

    private KogitoBuildContext.Builder stronglyTypedContext(KogitoBuildContext.Builder builder) {
        Properties properties = new Properties();
        //        properties.put(YaRDCodegen.STRONGLY_TYPED_CONFIGURATION_KEY, Boolean.TRUE.toString());
        builder.withApplicationProperties(properties);
        return builder;
    }

    protected YaRDCodegen getYaRDCodegen(String sourcePath, KogitoBuildContext.Builder contextBuilder) {
        return getYaRDCodegen(sourcePath, AddonsConfig.DEFAULT, contextBuilder);
    }

    protected YaRDCodegen getYaRDCodegen(String sourcePath, AddonsConfig addonsConfig, KogitoBuildContext.Builder contextBuilder) {
        KogitoBuildContext context = stronglyTypedContext(contextBuilder)
                .withAddonsConfig(addonsConfig)
                .build();

        return YaRDCodegen.ofCollectedResources(context,
                CollectedResourceProducer.fromPaths(Paths.get(sourcePath).toAbsolutePath()));
    }
}
