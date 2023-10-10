# Messcode: Encrypted Messenger Application

Messcode is a secure messenger application designed for sending encrypted messages between individuals and groups. It offers a public chat accessible to everyone and includes essential features such as user registration, login authentication, and user management, including profile management, group creation, addition and removal of users from groups, and password changes.

## Architecture

Messcode is built using the MVVM (Model-View-ViewModel) architectural pattern, enhancing modularity and maintainability. It also employs the Factory Method creational pattern for object instantiation.

### MVVM Architecture

- **ViewHandler**: Responsible for creating views, instantiating view controllers, and view models.
- **View Controllers**: Handle view representation.
- **View Models**: Manage presentation and UI logic, connected through data binding.
- **Model**: Contains the core business logic of the application.

### Networking

Networking is a crucial component of Messcode, and it comprises two primary classes:

- **ClientSocketHandler**: Manages the sending and receiving of packets to/from the server.
- **SocketClient**: Accepts client connections and establishes communication with the server.

On the server side, two classes are significant:

- **ConnectionPool**: Keeps track of users and facilitates various user-related functions.
- **ServerSocketHandler**: Listens for incoming packets from clients and responds accordingly.

### Security

Messcode prioritizes security and ensures encrypted communication using the SHA-512 encryption algorithm with password salting. This approach guarantees the confidentiality and integrity of user data during transmission.

## Database

Messcode employs PostgreSQL as its relational database management system, with pgAdmin4 for database administration. JDBC is utilized for seamless communication with the database.

The following tables are used in the database:

- **last_seen**: Tracks user last seen timestamps.
- **account**: Stores user account information.
- **private_messages**: Manages private message data.
- **group_messages**: Stores group message data.
- **public_messages**: Contains public chat messages.
- **project_members**: Keeps records of project members.
- **projects**: Stores information about projects and groups.

## Getting Started

To get started with Messcode, follow these steps:

1. Clone the repository.
2. Install the required dependencies.
3. Set up and configure your PostgreSQL database.
4. Compile and run the application.

### Prerequisites

- Java version 11 or higher must be installed on your device.

### Locating JAR Files

- You can find the JAR files in the "out/artifacts/..." directory.

### Database Setup

To use Messcode, you need to create a local database with the following parameters:

- **Database Name**: MessCode
- **Username**: postgres
- **Password**: chickenattack777

You can restore the database from the provided backup file named "UN-defined-messcode.tz."

### Running the Application

1. Start the server by executing the following command using your Java 11 (or higher) installation:
   
   ```shell
   your_11_java_version\bin\java.exe -jar MessCodeServer.jar

## Contributors

- [Farkas Noémi](https://github.com/yourusername)
- [Olivér Izsák](https://github.com/anotherusername)
- [Kami](https://github.com/anotherusername)
- [Roma](https://github.com/anotherusername)
- [Zerg](https://github.com/anotherusername)

![image](https://github.com/Team-Un-Defined/MessCode/assets/71821927/741be8e8-c0e6-4d88-8d75-f3e899e8e92f)
