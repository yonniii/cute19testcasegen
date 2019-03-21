# cute19testcasegen


This is a test case generator for CUTE19 language. Originally its goal is to try to make it easier for TAs to grade assigments of "Introduction to Programming Language." (Spring 2019, Dept. of Computer Science and Engineering, CNU)

The generated test cases will be output to the standard output by default.

Currently (3-16-2019) it does not always generate type-correct S-expressions (For instance, it would generate "(+ #F #T)" and "(cond 1 3 6)".) However, it can handle the number of arguments that used in the expression. (For instance, it would not generate "(> 1)".

Next version will be with support of configuration files to preset the probabilities of each gramatic feature in the testcases. (Currently, you need to hack the program to change the probabilities used in the generation process.)
