# Red Badger code challenge
The solution is a standalone java application that receives a file location as input
and produces the result on the standard output

It is a maven solution so the only thing to do to package the jar and run the tests is:

`mvn install`

After that, the jar (named test-1.0-SNAPSHOT.jar) would be in target folder, to execute
the jar you just need to run:

`java -jar ./target/test-1.0-SNAPSHOT.jar {fileLocation}`

The solution provides the proposed input in a file so if you want to run that input just
execute this:

`java -jar ./target/test-1.0-SNAPSHOT.jar src/main/resources/test.txt` 