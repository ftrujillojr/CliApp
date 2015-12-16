A Java Command Line Application example

This file shows how to => Create_a_Maven_Archetype_from_an_existing_project.pdf


pom.xml
       JUnit                        v4.11
       Apache commons-cli           v1.3
       exec-maven-plug              v1.4.0
       
       maven-compiler-plugin v3.3
            |--- Java v1.8, -Xlint:all, show warnings and deprecation.
       maven-dependency-plugin v2.10 
            |-- This is what copies dependent jars into final jar.
       maven-jar-plugin v2.6
            |--- This allows command line execution of jar.
       maven-surefire-plugin v2.12.4
            |--- To run test suite.  (or turn if off)


/home/ftrujillo/NetBeansProjects/CliApp
|-- Create_a_Maven_Archetype_from_an_existing_project.pdf
|-- nbactions.xml
|-- pom.xml
|-- README.txt
`-- src
    |-- main
    |   |-- java
    |   |   `-- org
    |   |       `-- nve
    |   |           `-- cliapp
    |   |               |-- CliWrapperException.java
    |   |               |-- CliWrapper.java
    |   |               |-- Latch.java
    |   |               |-- Main.java
    |   |               `-- RegExp.java
    |   `-- resources
    `-- test
        `-- java
            `-- org
                `-- nve
                    `-- cliapp
                        |-- TemplateTest.java
                        `-- TestCliWrapper.java



=======================================================================================

$ java -jar target/CliApp-1.0.jar --help

usage: CliApp [--debug <LEVEL>] [-h]

This app is a base line app to building Java CLI applications.

    --debug <LEVEL>   Debug  1,2,3,...
 -h,--help            This help message

Please report any issues to me@somecompany.org


