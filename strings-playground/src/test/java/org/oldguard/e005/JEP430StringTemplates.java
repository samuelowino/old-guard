package org.oldguard.e005;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Resources<br/>
 *
 * <a href="https://openjdk.org/jeps/430">JEP 430: String Templates (Preview)</a><br/>
 * <a href="https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html">Oracle JDBC Guide</a><br/>
 * <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/StringBuilder.html">StringBuilder ref docs</a>
 */
public class JEP430StringTemplates {

    @DisplayName("Java provides several mechanisms for string composition. " +
            "String Builder | String Buffer | Concatenation")
    @Test
    public void stringBuilderVsString(){
    }

    @DisplayName("Formatting strings with `formatted` and C-Style String.format %s|%f|%d")
    @Test
    public void stringCompositionWithFormatting(){
    }

    /**
     * Discussion: JEP 430: Template Expressions
     *
     * 1. STR.string - Template Expressions
     * 2. JDBC PreparedStatement
     * 3. Multiline template processor
     *
     * The code here can be run on playground or jdk 21 with preview flag enabled
     */

    @DisplayName("Safe SQL Queries with preparedStatement")
    @Test
    public void stringJDBCPreparedStatement(){
        //
    }


}
