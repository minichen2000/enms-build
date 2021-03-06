package com.nsb.enms.adapter.server.common.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtil
{
    private static final Logger logger = LogManager
            .getLogger( JsonUtil.class );

    private static final ObjectMapper objectMapper;

    static
    {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion( JsonInclude.Include.NON_NULL );
        objectMapper.configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
        objectMapper.disable( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS );
        objectMapper.enable( SerializationFeature.WRITE_ENUMS_USING_TO_STRING );
        objectMapper
                .enable( DeserializationFeature.READ_ENUMS_USING_TO_STRING );
        DateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" );
        dateFormat.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
        objectMapper.setDateFormat( dateFormat );
    }

    public static <T> String entity2Json( T entity )
    {
        try
        {
            return objectMapper.writeValueAsString( entity );
        }
        catch( JsonGenerationException e )
        {
            logger.error( e.getMessage(), e );
        }
        catch( JsonMappingException e )
        {
            logger.error( e.getMessage(), e );
        }
        catch( IOException e )
        {
            logger.error( e.getMessage(), e );
        }
        return null;
    }

    public static <T> T json2Entity( String json, Class<T> clazz )
    {
        try
        {
            return objectMapper.readValue( json, clazz );
        }
        catch( JsonParseException e )
        {
            e.printStackTrace();
        }
        catch( JsonMappingException e )
        {
            e.printStackTrace();
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
        return null;
    }
}
