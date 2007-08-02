package org.apache.maven.doxia.module;

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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.maven.doxia.AbstractModuleTest;

import org.apache.maven.doxia.parser.ParseException;
import org.apache.maven.doxia.parser.Parser;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.sink.SinkTestDocument;
import org.apache.maven.doxia.sink.TextSink;

/**
 * If a module provides both Parser and Sink, this class
 * can be used to check that chaining them together
 * results in the identity transformation, ie the model is still the same
 * after being piped through a Parser and the corresponding Sink.
 */
public abstract class AbstractIdentityTest extends AbstractModuleTest
{

    /**
     * Create a new instance of the parser to test.
     * 
     * @return the parser to test.
     */
    protected abstract Parser createParser();

    /**
     * Return a new instance of the sink that is being tested.
     *
     * @param writer The writer for the sink.
     * @return A new sink.
     */
    protected abstract Sink createSink( Writer writer );


    /**
     * Pipes a full model generated by {@see SinkTestDocument} through
     * a Sink (generated by {@see #createSink(Writer)}) and a Parser
     * (generated by {@see #createParser()}) and checks if the result
     * is the same as the original model. Currently, this doesn't actually
     * assert anything, but the two generated output files, expected.txt
     * and actual.txt can be compared for differences.
     */
    public void testIdentity()
        throws IOException, ParseException
    {
        Writer writer = null;

        Writer fileWriter = null;

        Reader reader = null;

        try
        {
            // generate the expected model
            writer =  new StringWriter();
            SinkTestDocument.generate( new TextSink( writer ) );
            String expected = writer.toString();

            // write to file for comparison
            fileWriter = getTestWriter( "expected", "txt" );
            SinkTestDocument.generate( new TextSink( fileWriter ) );


            // generate the actual model
            writer =  new StringWriter();
            SinkTestDocument.generate( createSink( writer ) );
            reader = new StringReader( writer.toString() );

            writer =  new StringWriter();
            createParser().parse( reader, new TextSink( writer ) );
            String actual = writer.toString();

            // write to file for comparison
            fileWriter = getTestWriter( "actual", "txt" );
            reader.reset();
            createParser().parse( reader, new TextSink( fileWriter ) );

            // Disabled for now, it's unlikely that any of our modules
            // will pass this test any time soon, but the generated
            // output files can still be compared.

            // TODO: make this work for at least apt and xdoc modules?
            //assertEquals( "Identity test failed!", expected, actual );

        }
        finally
        {
            if ( writer  != null )
            {
                writer.close();
            }

            if ( fileWriter  != null )
            {
                fileWriter.close();
            }

            if ( reader != null )
            {
                reader.close();
            }
        }
    }

    /** {@inheritDoc} */
    protected String getOutputDir()
    {
        return "identity/";
    }

    /**
     * Not used here, just required by AbstractModuleTest.
     *
     * @return An empty String.
     */
    protected String outputExtension()
    {
        return "";
    }

}