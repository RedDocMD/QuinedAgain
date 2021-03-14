# Quine McCluskey solver

This program performs the QM method of minimisation. It is probably neither efficient, nor a good solver.
It merely follows the manual way of doing QM algorithm, and outputs those steps. It does the final set-cover
problem by brute-forcing all combinations.

## Building and Running

You must have Apache Maven and JDK 11+ installed. Then run
```shell
git clone https://github.com/RedDocMD/QuinedAgain
cd QuinedAgain
mvn package
java -jar target/mainModule-1.0-SNAPSHOT-jar-with-dependencies.jar
```