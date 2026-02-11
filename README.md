# Football Stadium Booking System

## About
A Java application for managing football stadium bookings where players can organize matches and stadium owners can manage their venues.

## Features
- **Players & Stadium Owners** - Two types of users with different capabilities
- **Stadium Management** - Manage stadiums with pricing, capacity, and availability
- **Scheduling System** - Weekly schedules with time slots and conflict detection
- **Friendship System** - Connect with other players
- **Chat System** - Message your friends
- **Search** - Find available players and stadiums by location and time
- **Booking** - Create bookings, invite players, automatic confirmation

## Technologies
- Java 17
- JUnit 5 (Testing)
- IntelliJ IDEA

## How to Run

**Run the application:**
- Open in IntelliJ IDEA
- Run `src/Main.java`

**Run tests:**
- Right-click `test` folder → Run 'All Tests'
- 260+ tests covering all functionality

## Project Structure
```
src/        - Source code (Main.java + packages)
test/       - JUnit tests (10 test files)
out/        - Compiled classes
```

## Tests
All components tested with 260+ tests:
- Valid and invalid inputs
- Edge cases and boundary conditions  
- Business rules (e.g., can't message non-friends)
- Integration scenarios
