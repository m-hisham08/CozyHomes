# CozyHomes E-commerce Platform

CozyHomes is a robust e-commerce platform for home decor and furnishings, not limited to those items, built with Spring Boot and other technologies. This platform offers a seamless shopping experience with features like user authentication, product management, shopping cart functionality, secure payments, and more.

## Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Configuration](#configuration)
  - [Database Setup](#database-setup)
  - [Redis Setup](#redis-setup)
  - [Kafka Setup](#kafka-setup)
  - [Cloudinary Setup](#cloudinary-setup)
  - [Stripe Setup](#stripe-setup)
  - [Email Configuration](#email-configuration)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Deployment](#deployment)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)

## Features
- User authentication and authorization with JWT
- Shopping cart functionality with real-time updates
- Order processing and management
- Secure payment integration with Stripe
- User review system
- Image upload and management via Cloudinary
- Email notifications using Kafka for asynchronous processing
- Caching with Redis for improved performance
- Comprehensive auditing of entity changes
- Promotional email scheduling

## Technologies Used
- Java 17+
- Spring Boot 3.x
- Spring Security 6.x for authentication and authorization
- Spring Data JPA for data persistence
- PostgreSQL as the primary database
- Redis for caching
- Apache Kafka for message queuing
- Cloudinary for image management
- Stripe for payment processing
- JSON Web Tokens (JWT) for stateless authentication
- Maven for dependency management and build automation
- JUnit and Mockito for unit and integration testing

## Project Structure
The project follows a modular architecture with clear separation of concerns:

```
src/main/java/com/hisham/HomeCentre/
├── config/           # Configuration classes
├── constants/        # Application constants
├── controllers/      # REST API endpoints
├── exceptions/       # Custom exception handlers
├── filters/          # Custom filters (e.g., JWT)
├── models/           # Entity and domain models
├── payloads/         # Request/Response DTOs
├── repositories/     # Data access layer
├── schedules/        # Scheduled tasks
├── security/         # Security configurations
├── services/         # Business logic implementation
└── utils/            # Utility classes
```

For a detailed file structure, please refer to the project files.

## Getting Started

### Prerequisites
Ensure you have the following installed:
- Java Development Kit (JDK) 17 or higher
- Maven 3.6+
- PostgreSQL 8.0+
- Redis 6.0+
- Apache Kafka 2.8+
- Git

### Installation
1. Clone the repository:
   ```
   git clone https://github.com/yourusername/CozyHomes.git
   ```
2. Navigate to the project directory:
   ```
   cd CozyHomes
   ```
3. Install dependencies:
   ```
   mvn clean install
   ```

## Configuration

### Database Setup
1. Create a MySQL database for the application.
2. Update the `application-dev.yml` and `application-prod.yml` files with your database credentials.

### Redis Setup
1. Install and start Redis server.
2. Update the Redis configuration in the application properties files.

### Kafka Setup
1. Install and configure Apache Kafka.
2. Start Zookeeper:
    ```
    bin/zookeeper-server-start.sh config/zookeeper.properties
    ```
3. Start Kafka Server:
    ```
    bin/kafka-server-start.sh config/server.properties
    ```
4. Create a topic named `email-topic` for email notifications:
    ```
    bin/kafka-topics.sh --create --topic email --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
    ```
5. Update the Kafka properties in the application configuration.

### Cloudinary Setup
1. Sign up for a Cloudinary account.
2. Obtain your Cloud Name, API key, and API secret.
3. Add these to your environment variables or application properties.

### Stripe Setup
1. Create a Stripe account and get your API keys.
2. Add the Stripe secret key to your environment variables or application properties.

### Email Configuration
1. Set up an email account for sending notifications.
2. Update the email configuration in the application properties.

Populate the `application-dev.yml` and `application-prod.yml` files based on the provided placeholders OR simply set the following environment variables:

```
export DATABASE_HOST=your_db_host
export DATABASE_PORT=your_db_port
export DATABASE_NAME=your_db_name
export DATABASE_USERNAME=your_db_username
export DATABASE_PASSWORD=your_db_password
export KAFKA_BOOTSTRAP_SERVER=your_kafka_server
export EMAIL_ADDRESS=your_email@example.com
export EMAIL_PASSWORD=your_email_password
export REDIS_HOST=your_redis_host
export REDIS_PORT=your_redis_port
export REDIS_PASSWORD=your_redis_password
export JWT_SECRET=your_jwt_secret
export JWT_EXPIRATION_TIME_IN_MS=3600000
export CLOUDINARY_CLOUD_NAME=your_cloud_name
export CLOUDINARY_API_KEY=your_api_key
export CLOUDINARY_API_SECRET=your_api_secret
export STRIPE_API_SECRET=your_stripe_secret
```

## Running the Application
To run the application in development mode:

```
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

For production:

```
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

The application will start on `http://localhost:8080`.

## API Endpoints
The application exposes the following main API endpoints:

- Authentication: `/api/v1/auth/**`
- Products: `/api/v1/products/**`
- Categories: `/api/v1/categories/**`
- Cart: `/api/v1/cart/**`
- Orders: `/api/v1/orders/**`
- Reviews: `/api/v1/reviews/**`
- Payments: `/api/v1/payments/**`
- Uploads: `/api/payments/**`

For a complete list of endpoints and their usage, please refer the Swagger UI documentation provided at `http://localhost:8080/swagger-ui/index.html`

## Testing
Run the tests using:

```
mvn test
```

This will execute unit tests for repositories, services, and controllers.

## Deployment
For deployment:

1. Build the application:
   ```
   mvn clean package
   ```
2. This generates a JAR file in the `target` directory.
3. Deploy this JAR file to your hosting platform (e.g., AWS, Heroku, DigitalOcean).
4. Ensure all environment variables are properly set in your production environment.

## Troubleshooting
- If you encounter database connection issues, verify your PostgreSQL server is running and the credentials are correct.
- For Redis connection problems, check if the Redis server is active and the configuration is correct.
- If Kafka consumers are not receiving messages, ensure the Kafka server is running and the topic is created.
- For Cloudinary upload issues, verify your API credentials and network connectivity.
- If emails are not being sent, check your email server configuration and credentials.

## Contributing
You can contribute to the project by following these steps:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

For additional support or questions, please open an issue or contact me at mohammadhisham852@gmail.com
