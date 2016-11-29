# engine-rules
Java 8 engine rules with streams and lambda functions

(Work in progress, not in final release)

An experimental rules engine using lambdas and streams from Java 8.
The main idea is to wrap every business rule as a lambda function in a stream, and the engine
creates a single business rule as a reduction of all the business rules defined per channel of execution, as another stream.

We define a generic list of items, which are processed through the rules stream, applying all the rules in a chained way.
As a result, we get an update list of items.

We can define every rule with his own ordering, priority of execution, and the posibility of activation.

It's possible to upload new rules dynamically from an AWS S3 storage, and handle in real time the rules activation with
a console (this console is in development and is not included in this project)







