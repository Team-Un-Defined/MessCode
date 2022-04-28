##### MessCode - Secure Encrypted Messenger

###### Video Presentation Link - https://vimeo.com/703456436/670b161522

In order to run the program, Java version 11 or higher must be installed on the device.

It is also necessary to locally create the database with the following parameters:
> name: MessCode

> username: postgres

> password: chickenattack777

And restore it from database backup file “UN-defined-messcode.tz”.

The program can be run using the two included JAR files, a server-side and a client program:

Firstly start-up the server:

> your_11_java_version\bin\java.exe -jar MessCodeServer.jar

Then run client-side application:

> your_11_java_version\bin\java.exe -jar MessCodeClient.jar

You can find jar files in "out/artifacts/...".

When running client you must have config file in the same file path as executable.

To be able to effectively test the messenger it is recommended to run three client instances, with the employer, project manager, and worker accounts.

Accounts' information:

> email: xfarkasn@stuba.sk ; password: a

> email: xkiss@stuba.sk ; password: a