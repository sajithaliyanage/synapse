/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.synapse.commons.evaluators.source;

import junit.framework.TestCase;
import org.apache.synapse.commons.evaluators.EvaluatorContext;
import org.apache.synapse.commons.evaluators.EvaluatorException;
import org.apache.synapse.commons.evaluators.EvaluatorConstants;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.HashMap;

public class SourceTextRetrieverTest extends TestCase {

    public void testURLTextRetriever() throws EvaluatorException {
        try {
            URI uri = new URI("http://host:9000/path");
            EvaluatorContext context = new EvaluatorContext(uri.toString(), null);

            URLTextRetriever txtRtvr = new URLTextRetriever();
            assertEquals(uri.toString(), txtRtvr.getSourceText(context));

            txtRtvr.setFragment(EvaluatorConstants.URI_FRAGMENTS.host.name());
            assertEquals(uri.getHost(), txtRtvr.getSourceText(context));

            txtRtvr.setFragment(EvaluatorConstants.URI_FRAGMENTS.port.name());
            assertEquals(String.valueOf(uri.getPort()), txtRtvr.getSourceText(context));

            txtRtvr.setFragment(EvaluatorConstants.URI_FRAGMENTS.protocol.name());
            assertEquals(uri.getScheme(), txtRtvr.getSourceText(context));

            txtRtvr.setFragment(EvaluatorConstants.URI_FRAGMENTS.path.name());
            assertEquals(uri.getPath(), txtRtvr.getSourceText(context));
        } catch (URISyntaxException ignore) {

        }
    }

    public void testHeaderTextRetriever() throws EvaluatorException {
        Map<String,String> headers = new HashMap<String,String>();
        headers.put("key1", "value1");
        headers.put("key2", "value2");
        EvaluatorContext context = new EvaluatorContext(null, headers);

        HeaderTextRetriever txtRtvr = new HeaderTextRetriever("key1");
        assertEquals(headers.get("key1"), txtRtvr.getSourceText(context));

        txtRtvr = new HeaderTextRetriever("bogusKey");
        assertNull(txtRtvr.getSourceText(context));
    }

    public void testParameterTextRetriever() throws EvaluatorException {
        String url = "http://host:9000/path?key1=value1&key2=value2";
        EvaluatorContext context = new EvaluatorContext(url, null);

        ParameterTextRetriever txtRtvr = new ParameterTextRetriever("key1");
        assertEquals("value1", txtRtvr.getSourceText(context));

        txtRtvr = new ParameterTextRetriever("key2");
        assertEquals("value2", txtRtvr.getSourceText(context));

        txtRtvr = new ParameterTextRetriever("bogusKey");
        assertNull(txtRtvr.getSourceText(context));
    }

}
