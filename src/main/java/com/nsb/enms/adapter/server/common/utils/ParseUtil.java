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

    private static Pattern pattern2 = Pattern
            .compile( "boardType\\s*\\{\\s*(type.*?)\\s*\\}[,|\\s*\\},]" );

//    private static Pattern pattern3 = Pattern
//            .compile( "=\\s*\\w*_*(\\w+)\\s*" );

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

    public static List<String> parseAllowedEquipmentType( String line )
    {
        Matcher matcher = pattern2.matcher( line );
        List<String> values = new ArrayList<String>();
        String value = "";
        while( matcher.find() )
        {
            value = matcher.group( 1 );
            String[] elements = value.split( ",\\s*" );
            for( String element : elements )
            {
                values.add( element.replace( "type", "" ).trim() );
            }
        }
        return values;
    }

//    public static boolean parseBooleanAttr( String line )
//    {
//        Matcher matcher = pattern3.matcher( line );
//        if( matcher.find() )
//        {
//            if( "true".equals( matcher.group( 1 ) ) )
//            {
//                return true;
//            }
//        }
//        return false;
//    }

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
        String string = "allowedEquipmentType= { boardCase { boardType { type P4S1-4E } }, boardCase { boardType { type BCE-E1T, type ISABCEE1 }, compatibles { type BCE-E1T, type ISABCEE1 } }, boardCase { boardType { type BCE-ETHT, type BCE-ETH }, compatibles { type BCE-ETHT, type BCE-ETH } }, boardCase { boardType { type ASI-MB2, type ASI-MB }, compatibles { type ASI-MB2, type ASI-MB } }, boardCase { boardType { type P8S1-4E } }, boardCase { boardType { type P8DCN } }, boardCase { boardType { type ES16B, type ISA-ES16 }, compatibles { type ES16B, type ISA-ES16 } }, boardCase { boardType { type COWLA2 } }, boardCase { boardType { type P4OC3 } }, boardCase { boardType { type 4XANYC } }, boardCase { boardType { type P4S4N } }, boardCase { boardType { type ES1-8FXB, type ES1-8FX }, compatibles { type ES1-8FXB, type ES1-8FX } }, boardCase { boardType { type ES4-8FEB, type ES4-8FE }, compatibles { type ES4-8FEB, type ES4-8FE } }, boardCase { boardType { type ES1-8FEB, type ES1-8FE }, compatibles { type ES1-8FEB, type ES1-8FE } }, boardCase { boardType { type PREA1GBE } }, boardCase { boardType { type PREA4ETH } }, boardCase { boardType { type ATM8x8 } }, boardCase { boardType { type ATM4x4D3 } }, boardCase { boardType { type ATM4x4V2 } }, boardCase { boardType { type ATM4x4 } }, boardCase { boardType { type GETH-MB } }, boardCase { boardType { type ETH-MB } }, boardCase { boardType { type CO-16, type L-162ND, type L-162N, type L-162, type L-161ND, type L-161N, type L-161, type S-161ND, type S-161N, type S-161, type I-161ND, type L-42N, type L-41N, type S-41N }, compatibles { type CO-16, type L-162ND, type L-162N, type L-162, type L-161ND, type L-161N, type L-161, type S-161ND, type S-161N, type S-161, type I-161ND, type L-42N, type L-41N, type S-41N } }, boardCase { boardType { type P4ES1N } }, boardCase { boardType { type P4E4N } }, boardCase { boardType { type P4S1N } }, boardCase { boardType { type P3E3/T3 } }, boardCase { boardType { type P63E1N-M4, type P63E1N, type P63E1 }, compatibles { type P63E1N-M4, type P63E1N, type P63E1 } }, boardCase { boardType { type P21E1N-M4, type P21E1N, type P21E1 }, compatibles { type P21E1N-M4, type P21E1N, type P21E1 } }, boardCase { boardType { noType NULL } } } ,";
        System.out.println( string );
        List<String> vaList = parseAllowedEquipmentType( string );
        for( String val : vaList )
        {
            System.out.println( val );
        }

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
