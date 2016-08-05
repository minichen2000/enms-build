package com.nsb.enms.restful.adapter.server.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseUtil
{
    private static Pattern pattern = Pattern.compile( "=\\s*(\\w+.*?)\\s+" );

    private static Pattern pattern1 = Pattern
            .compile( "=\\s*\\w*\\s*(\\w+)\\s*" );

    // private static Pattern pattern3 = Pattern.compile( "=\\s*(\\d+.\\w*)\\s*"
    // );

    private static Pattern pattern4 = Pattern
            .compile( "=\\s*\\w*_*(\\w+)\\s*" );

    public static String parseAttrWithSingleValue( String line )
    {
        return line
                .substring( line.indexOf( "{" ) + 1, line.lastIndexOf( "}" ) )
                .trim();
    }

    public static String parseAttrWithMultiValue( String line )
    {
        line = line.substring( line.indexOf( "{ {" ) + 1,
            line.lastIndexOf( "} }" ) );
        String[] elements = line.split( "\\s*," );
        StringBuilder result = new StringBuilder();
        for( String element : elements )
        {
            result.append(
                element.replace( "{", "" ).replace( "}", "" ).trim() + "/" );
        }
        return result.deleteCharAt( result.length() - 1 ).toString();
    }

    public static String parseAttr( String line )
    {
        Matcher matcher = pattern.matcher( line );
        if( matcher.find() )
        {
            return matcher.group( 1 );
        }
        return "";
    }

    public static String parseAttr1( String line )
    {
        Matcher matcher = pattern1.matcher( line );
        if( matcher.find() )
        {
            return matcher.group( 1 );
        }
        return "";
    }

    // public static String parseRelease( String line )
    // {
    // Matcher matcher = pattern3.matcher( line );
    // if( matcher.find() )
    // {
    // return matcher.group( 1 );
    // }
    // return "";
    // }

    public static boolean parseBooleanAttr( String line )
    {
        Matcher matcher = pattern4.matcher( line );
        if( matcher.find() )
        {
            if( "true".equals( matcher.group( 1 ) ) )
            {
                return true;
            }
        }
        return false;
    }

    public static void main( String[] args )
    {
        String string = "userLabel=unlock (1) ,";
        System.out.println( parseAttr( string ) );
    }
}
