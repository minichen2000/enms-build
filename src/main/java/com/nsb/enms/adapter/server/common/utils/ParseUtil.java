package com.nsb.enms.adapter.server.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseUtil
{
    private static Pattern pattern = Pattern.
            compile( "=\\s*(\\w+.*?)\\s+" );

    private static Pattern pattern1 = Pattern
            .compile( "=\\s*\\w*\\s*(.*)\\s+" );

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

    public static String parseMultiValueWithNoBlank( String line )
    {
        line = line.substring( line.indexOf( "{{" ) + 1,
            line.lastIndexOf( "}}" ) );
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

    public static List<String> parseList( String line )
    {
        List<String> list = new ArrayList<String>();
        String[] values = line.split( "} } }," );

        for( int i = 0; i < values.length; i++ )
        {
            System.out.println( values[i] );
            if( i == 0 )
            {
                values[i] += "} }";
            }
            list.add( parseAttrWithMultiValue( values[i] ) );
        }
        return list;
    }

    public static void main( String[] args )
    {
        String str2 = "equipmentExpected=type ETH-ATX ,";
        System.out.println( parseAttr1( str2 ) );

        String str3 = "alarmStatus=activeReportable_Major (4) ,";
        System.out.println( parseAttr( str3 ) );

        System.out.println( parseAttrWithSingleValue(
            "availabilityStatus= { notInstalled } ," ) );
        
        Pattern pattern = Pattern
                .compile( "objectClass\\s*\\{\\s*(vc.*?)\\s*\\},\\s*concatenationLevel\\s*onlyMaxLevelGiven\\s*(\\d+)\\s*\\}" );
        Matcher match = pattern.matcher( "dataTPList= { { objectClass { vc4XVirtualTTPBidirectional }, concatenationLevel onlyMaxLevelGiven 1 }, { objectClass { vc12XVirtualTTPBidirectional }, concatenationLevel onlyMaxLevelGiven 50 }, { objectClass { vc3XVirtualTTPBidirectional }, concatenationLevel onlyMaxLevelGiven 3 }, { objectClass { vc12TTPBidirectionalR1 }, concatenationLevel onlyMaxLevelGiven 1 }, { objectClass { vc3TTPBidirectionalR1 }, concatenationLevel onlyMaxLevelGiven 1 }, { objectClass { vc4TTPBidirectionalR1 }, concatenationLevel onlyMaxLevelGiven 1 } } ," );
        while (match.find())
        {
            System.out.println( match.group( 1 ) + "=" + match.group( 2 ) );
        }
    }
}
