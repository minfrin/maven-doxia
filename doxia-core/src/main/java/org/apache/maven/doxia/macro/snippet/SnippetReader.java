package org.apache.maven.doxia.macro.snippet;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SnippetReader
{
    private static final String EOL = System.getProperty( "line.separator" );

    private URL source;

    public SnippetReader( URL source )
    {
        this.source = source;
    }

    public StringBuffer readSnippet( String snippetId )
        throws IOException
    {
        List lines = readLines( snippetId );
        int minIndent = minIndent( lines );
        StringBuffer result = new StringBuffer();
        for ( Iterator iterator = lines.iterator(); iterator.hasNext(); )
        {
            String line = (String) iterator.next();
            result.append( line.substring( minIndent ) );
            result.append( EOL );
        }
        return result;
    }

    int minIndent( List lines )
    {
        int minIndent = Integer.MAX_VALUE;
        for ( Iterator iterator = lines.iterator(); iterator.hasNext(); )
        {
            String line = (String) iterator.next();
            minIndent = Math.min( minIndent, indent( line ) );
        }
        return minIndent;
    }

    int indent( String line )
    {
        char[] chars = line.toCharArray();
        int indent = 0;
        for ( ; indent < chars.length; indent++ )
        {
            if ( chars[indent] != ' ' )
            {
                break;
            }
        }
        return indent;
    }

    private List readLines( String snippetId )
        throws IOException
    {
        BufferedReader reader = new BufferedReader( new InputStreamReader( source.openStream() ) );
        List lines = new ArrayList();
        try
        {
            boolean capture = false;
            String line;
            while ( ( line = reader.readLine() ) != null )
            {
                if ( isStart( snippetId, line ) )
                {
                    capture = true;
                }
                else if ( isEnd( snippetId, line ) )
                {
                    break;
                }
                else if ( capture )
                {
                    lines.add( line );
                }
            }
        }
        finally
        {
            reader.close();
        }
        return lines;
    }

    protected boolean isStart( String snippetId, String line )
    {
        return isDemarcator( snippetId, "START", line );
    }

    protected boolean isDemarcator( String snippetId, String what, String line )
    {
        String upper = line.toUpperCase();
        return upper.indexOf( what.toUpperCase() ) != -1 && upper.indexOf( "SNIPPET" ) != -1 &&
            line.indexOf( snippetId ) != -1;
    }

    protected boolean isEnd( String snippetId, String line )
    {
        return isDemarcator( snippetId, "END", line );
    }
}
