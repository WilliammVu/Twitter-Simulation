# Twitter Simulation - Full-Stack Web Application

A complete Twitter/X clone built with Spring Boot backend and vanilla JavaScript frontend. This project demonstrates full CRUD operations, user authentication, social networking features, and modern web design.

## Features

### Backend (Spring Boot)
- RESTful API with proper HTTP status codes and error handling
- User authentication (login/signup/logout)
- Tweet management (create, like, unlike, retweet, unretweet)
- Social features (follow/unfollow, feed generation, user search)
- Suggested friends algorithm based on mutual connections
- CORS enabled for local development

### Frontend (HTML/CSS/JavaScript)
- Modern, responsive UI inspired by Twitter/X
- Dark mode interface
- Real-time character counter for tweets (280 max)
- User search functionality
- Suggested friends sidebar
- Profile pages with follower/following stats
- Timestamp display ("2m ago", "3h ago", etc.)
- User avatars with initials
- Mobile-responsive design

## Technology Stack

- **Backend**: Java 21+, Spring Boot 3.3.5, Maven
- **Frontend**: HTML5, CSS3, JavaScript (ES6+)
- **Architecture**: MVC pattern with RESTful API

## Project Structure

```
Twitter-Simulation/
├── src/
│   └── main/
│       ├── java/com/twitter/simulation/
│       │   ├── TwitterSimulationApplication.java  # Main Spring Boot app
│       │   ├── controller/                         # REST controllers
│       │   │   ├── AuthController.java
│       │   │   ├── TweetController.java
│       │   │   └── UserController.java
│       │   ├── service/                            # Business logic
│       │   │   └── TwitterService.java
│       │   ├── dto/                                # Data Transfer Objects
│       │   │   ├── ApiResponse.java
│       │   │   ├── LoginRequest.java
│       │   │   ├── SignupRequest.java
│       │   │   ├── TweetRequest.java
│       │   │   ├── TweetResponse.java
│       │   │   └── UserResponse.java
│       │   ├── models/                             # Domain models
│       │   │   ├── Twitter.java
│       │   │   ├── User.java
│       │   │   └── Tweet.java
│       │   └── util/                               # Utility classes
│       │       └── OrderedTweetSet.java
│       └── resources/
│           ├── application.properties              # App configuration
│           └── static/                             # Frontend files
│               ├── index.html                      # Login/signup page
│               ├── home.html                       # Home feed
│               ├── profile.html                    # User profile
│               ├── css/
│               │   └── styles.css                  # All styles
│               └── js/
│                   ├── auth.js                     # Login/signup logic
│                   ├── home.js                     # Home feed logic
│                   └── profile.js                  # Profile page logic
└── pom.xml                                         # Maven configuration
```

## Prerequisites

- Java Development Kit (JDK) 21 or higher (tested with Java 25)
- Maven 3.6 or higher

## Setup and Installation

### 1. Clone the repository
```bash
git clone <your-repo-url>
cd Twitter-Simulation
```

### 2. Build the project
```bash
mvn clean install
```

### 3. Run the application
```bash
mvn spring-boot:run
```

Alternatively, you can run the compiled JAR:
```bash
java -jar target/twitter-simulation-1.0.0.jar
```

### 4. Access the application
Open your browser and navigate to:
```
http://localhost:8080
```

The application will automatically open on the login/signup page.

## Usage Guide

### Creating an Account
1. Click "Sign up" on the login page
2. Choose a username (3-20 characters, must start with a letter)
3. Create a password (6-16 characters with at least one digit and special character)
4. Click "Create Account"

### Using the Application

#### Home Feed
- View tweets from users you follow
- Post new tweets (max 280 characters)
- Like/unlike tweets
- Retweet/unretweet tweets
- Search for users
- View suggested friends

#### Profile Page
- View user's tweets, follower count, and following count
- Follow/unfollow users
- View your own profile

#### Navigation
- Click the Twitter logo or "Home" to return to your feed
- Click "Profile" to view your profile
- Click any username to view that user's profile
- Use the search box to find users by username

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/signup` - Create new account
- `POST /api/auth/logout` - Logout
- `GET /api/auth/me` - Get current user

### Tweets
- `POST /api/tweets` - Post a tweet
- `GET /api/tweets/feed` - Get user's feed
- `POST /api/tweets/{id}/like` - Like a tweet
- `DELETE /api/tweets/{id}/like` - Unlike a tweet
- `POST /api/tweets/{id}/retweet` - Retweet
- `DELETE /api/tweets/{id}/retweet` - Unretweet

### Users
- `GET /api/users/search?query={username}` - Search for user
- `GET /api/users/{username}` - Get user profile
- `GET /api/users/{username}/tweets` - Get user's tweets
- `POST /api/users/{username}/follow` - Follow user
- `DELETE /api/users/{username}/follow` - Unfollow user
- `GET /api/users/suggested` - Get suggested friends

## Password Requirements

- Length: 6-16 characters
- Must contain at least one digit (0-9)
- Must contain at least one special character (!@#$%^&*, etc.)

## Username Requirements

- Length: 3-20 characters
- Must start with a letter
- Can contain letters, numbers, hyphens (-), and underscores (_)
- Cannot have consecutive special characters
- Cannot end with a special character

## Development Notes

### Hot Reload
The application uses Spring Boot DevTools for automatic restart during development. Any changes to Java files will trigger a restart.

### CORS Configuration
CORS is configured to allow requests from `http://localhost:8080` and `http://127.0.0.1:8080`. Modify `application.properties` or `TwitterSimulationApplication.java` to add additional origins.

### Database
Currently, the application uses in-memory storage. All data is lost when the application stops. To persist data, you would need to integrate a database like PostgreSQL or MySQL.

## Troubleshooting

### Port 8080 is already in use
If port 8080 is already in use, you can change it in `src/main/resources/application.properties`:
```properties
server.port=8081
```

### Build fails
Make sure you have JDK 21 or higher:
```bash
java -version
```

If you see warnings about `sun.misc.Unsafe`, these are normal with newer Java versions and won't affect functionality.

### Frontend not loading
Ensure the static files are in `src/main/resources/static/` and rebuild the project:
```bash
mvn clean install
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- UI inspired by Twitter/X
- Built as a school project for Data Structures and Algorithms class
