package org.oldguard.e005;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JEP378TextBlocks {

    /**
     * Resources:<br/>
     *
     *  <a href="https://docs.oracle.com/en/java/javase/21/text-blocks/index.html">Jim Laskey & Stuart Marks:Programmer's Guide to Text Blocks</a>
     *  <br/>
     *  <a href="JEP 378 https://openjdk.org/jeps/378">JEP 378</a>
     *
     */
    @Test
    @DisplayName("""
            A text block is an alternative form of Java string representation that
            can be used anywhere a traditional double quoted
            string literal can be used
            """)
    public void textBlocks(){
        // Strings are immutable
        String message = "Good morning";

        String lovingMessage = message.replace(" ", "❤️"); //creates a new instance

        System.out.println(lovingMessage);

        //text block is an alternative form of Java string representation

        System.out.println("=======================");

        var longMessage = """
                😡
                Morning
                
                This is to inform you once and for all
                
                We are done!!!
                
                Cheer!😏
                """;

        var angrierText = longMessage.replace("We are done!!","You can forget those date nights");

        System.out.println(angrierText);

        //incidental white space
        var coolMessage = """
                Meet me at the garage""";

        System.out.println(coolMessage);

        //object produced from a text block is a java.lang.String

        //text blocks may be intermixed with string literals in a string concatenation expression

        var concatenatedMessage = "==>" + coolMessage + " : This is a coll message";

        System.out.println(concatenatedMessage);

        //Text blocks may be used as a method argument

        //String methods may be applied to a text block

        var htmlText = """
                <html></html>
                """;

        //A text block can be used in place of a string literal to improve
        //the readability and clarity of the code...JSON...HTML

        //You can't put a text block on a single line

        var somejson = "some json text";

        //The contents of the text block cannot follow the three opening double-quotes
        //On the same line

        //represent a multi-line string with the final `\n`: \ntext1\ntext2\ntext3\n

        var finalExample = """
                This is the final example
                It is the last one""";

        System.out.println(finalExample);
        System.out.println(finalExample);

        //represent a multi-line string without the final `\n`: \ntext1\ntext2\ntext3


        /**
         * =================================
         *  Incidental White Space:
         *  <a href="JEP 378 https://openjdk.org/jeps/378">
         *      JEP 378</a>
         * =================================
         *
         * <html>
         *    <body>
         *       <p>Hello HTML Text.</p>
         *    </body>
         * </html>
         *
         * Indentation visualised [.]
         *
         * String html = """
         * ········<html>
         * ········    <body>
         * ········        <p>Hello World.</p>
         * ········    </body>
         * ········</html>
         * ········""";
         */


        //New String methods
        // String.formatted

        var oldCar = String.format("Mileage %,2f", 45_000.76);

        System.out.println(oldCar);

        var toyotaOld = """
                Mileage %,2f %s
                """.formatted(45_000.76, "Km");

        System.out.println(toyotaOld);

        //Use Java String.trim() to remove whitespaces at the start or end of String

        var multiLineText = """
                1. Jame Odio
                2. Peter Luna
                3. Maxwell
                """.trim();

        System.out.println(multiLineText);
        System.out.println(multiLineText);
    }

    @DisplayName("Escape sequence \\t \\n \\b")
    @Test
    public void escapeSequence() {
        var tableRow = "Column1\t\tColumn2\t\tColumn3\n";
        var tableRow1 = "ColVal1\t\tColVal2\t\tColVal3";
        System.out.println(tableRow);
        System.out.println(tableRow1);

        var movieActor = new Person("Bruce Wayne", 87);

        System.out.println("Actor: " + movieActor.name() + " " + movieActor.age());

        var objectMapper = new ObjectMapper();
        try {
            var json = objectMapper.writeValueAsString(movieActor);
            System.out.println(json);

            System.out.println("{\n" +
                    "   \"name\":\"Bruce Wayne\",\n" +
                    "   \"age\":87\n" +
                    "}");

            var prettyJson = """
                    {
                       "name":"Bruce Wayne",
                       "age":87
                    }
                    """;

        } catch (JsonProcessingException ex){
            ex.printStackTrace();
        }
    }

    // Reference: https://openjdk.org/jeps/378
    record Person(String name, Integer age) { }


}
