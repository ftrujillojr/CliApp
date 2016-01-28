A Java Command Line Application example

This file shows how to => Create_a_Maven_Archetype_from_an_existing_project.pdf

* CliWrapper wraps Apache CLI with additional functionality that is missing.

* RegExp is just for me as I do not like to clutter up my code with 5 lines of
  code just to do a regex pattern match.  Plus, I want something that works and is 
  unit tested.


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
|-- nb-configuration.xml
|-- pom.xml
|-- README.txt
`-- src
    |-- main
    |   |-- java
    |   |   `-- fjt
    |   |       |-- comparators
    |   |       |   |-- CalendarComparator.java
    |   |       |   `-- CalendarIterator.java
    |   |       |-- database
    |   |       |   |-- CommonDBAbstract.java
    |   |       |   |-- ForiegnKeys.java
    |   |       |   `-- MySQLImpl.java
    |   |       |-- exceptions
    |   |       |   |-- CalendarUtilsException.java
    |   |       |   |-- CliWrapperException.java
    |   |       |   |-- JsonUtilsException.java
    |   |       |   |-- MySqlBuildException.java
    |   |       |   |-- RSAException.java
    |   |       |   |-- SysUtilsException.java
    |   |       |   `-- UnsignedBigIntUtilsException.java
    |   |       |-- interfaces
    |   |       |   `-- CommonDBInterface.java
    |   |       |-- support
    |   |       |   |-- CliWrapper.java
    |   |       |   |-- Latch.java
    |   |       |   |-- RSA.java
    |   |       |   `-- SetOps.java
    |   |       |-- top
    |   |       |   `-- CliAppMain.java
    |   |       `-- utils
    |   |           |-- CalendarUtils.java
    |   |           |-- ConvertUtils.java
    |   |           |-- JsonUtils.java
    |   |           |-- RegExp.java
    |   |           |-- SHAUtils.java
    |   |           |-- SysUtils.java
    |   |           `-- UnsignedBigIntUtils.java
    |   `-- resources
    `-- test
        |-- java
        |   `-- fjt
        |       `-- test
        |           |-- PersonDeserializer.java
        |           |-- Person.java
        |           |-- PersonSortByAgeSalaryComparator.java
        |           |-- TemplateTest.java
        |           |-- TestCalendarUtils.java
        |           |-- TestCliWrapper.java
        |           |-- TestJsonUtils.java
        |           |-- TestMySQLImpl.java
        |           |-- TestRegExp.java
        |           |-- TestRSA.java
        |           |-- TestSetOps.java
        |           |-- TestSHAUtils.java
        |           |-- TestSorting.java
        |           |-- TestSysUtils.java
        |           `-- TestUnsignedBigIntUtils.java
        `-- resources
            |-- compact.json
            `-- pretty.json

=======================================================================================

$ java -jar target/CliApp-1.0.jar --help

usage: CliApp [--debug <LEVEL>] [-h]

This app is a base line app to building Java CLI applications.

    --debug <LEVEL>   Debug  1,2,3,...
 -h,--help            This help message

Please report any issues to me@somecompany.org


