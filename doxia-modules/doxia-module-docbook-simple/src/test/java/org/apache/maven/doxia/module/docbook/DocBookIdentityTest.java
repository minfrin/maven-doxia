package org.apache.maven.doxia.module.docbook;

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

import java.io.Writer;

import org.apache.maven.doxia.module.AbstractIdentityTest;
import org.apache.maven.doxia.parser.Parser;
import org.apache.maven.doxia.sink.Sink;


/**
 * Check that piping a full model through a DocBookParser and a DocBookSink
 * leaves the model unchanged. The test is done in AbstractIdentityTest.
 */
public class DocBookIdentityTest extends AbstractIdentityTest
{
    /** {@inheritDoc} */
    protected Sink createSink( Writer writer )
    {
        return new DocBookSink( writer, "UTF-8" );
    }

    /** {@inheritDoc} */
    protected Parser createParser()
    {
        return new DocBookParser();
    }
}
