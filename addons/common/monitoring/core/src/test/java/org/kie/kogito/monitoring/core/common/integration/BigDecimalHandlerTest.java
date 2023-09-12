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
package org.kie.kogito.monitoring.core.common.integration;

import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.KogitoGAV;
import org.kie.kogito.monitoring.core.common.system.metrics.dmnhandlers.BigDecimalHandler;
import org.kie.kogito.monitoring.core.common.system.metrics.dmnhandlers.DecisionConstants;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

import ch.obermuhlner.math.big.stream.BigDecimalStream;

import static org.assertj.core.api.Assertions.assertThat;

public class BigDecimalHandlerTest extends AbstractQuantilesTest<BigDecimalHandler> {
    @BeforeEach
    public void setUp() {
        dmnType = "bigdecimal";
        registry = new SimpleMeterRegistry();
        handler = new BigDecimalHandler(dmnType, KogitoGAV.EMPTY_GAV, registry);
    }

    @AfterEach
    public void destroy() {
        registry.clear();
    }

    @Test
    public void givenSomeSamplesWhenQuantilesAreCalculatedThenTheQuantilesAreCorrect() {
        // Act
        BigDecimalStream.range(BigDecimal.valueOf(1), BigDecimal.valueOf(10001), BigDecimal.ONE, MathContext.DECIMAL64).forEach(x -> handler.record("decision", ENDPOINT_NAME, x));
        assertThat(registry.find(dmnType + DecisionConstants.DECISIONS_NAME_SUFFIX).summary().max()).isGreaterThanOrEqualTo(10000);
        assertThat(registry.find(dmnType + DecisionConstants.DECISIONS_NAME_SUFFIX).summary().mean()).isPositive();
    }
}
