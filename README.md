# Luminar

Luminar is a native Android application designed to help users manage their daily tasks and build consistent habits. It features a calendar-centric interface for organizing both singular and recurring tasks, complete with categories and custom reminders to keep users on track. The application utilizes Firebase for its backend services, including user authentication and data storage.

## Features

- **User Authentication:** Secure registration and login using Firebase Authentication.
- **Task Management:** Create, update, and delete tasks. Users can add details like notes, categories, and due dates.
- **Calendar View:** Visualize all tasks on a monthly calendar and view a list of tasks for any selected day.
- **Recurring Tasks:** Support for creating tasks that repeat daily, weekly, monthly, or yearly.
- **Task Categorization:** Assign tasks to predefined categories (e.g., Personal, Work, Health), each with a distinct color for easy identification.
- **Custom Reminders:** Schedule local notifications to get timely reminders. Non-recurrent tasks trigger a reminder one day before the due date, while recurrent tasks provide a 30-minute warning.
- **Notification Center:** A dedicated screen to view a history of all task-related notifications.

## Tech Stack

- **Frontend:** Android (Java), Android SDK
- **Backend & Services:**
  - **Firebase Realtime Database:** For storing user data, tasks, and notifications.
  - **Firebase Authentication:** For managing user accounts (email and password).
  - **Firebase Cloud Messaging (FCM):** For handling push notifications.
- **Core Android Components:** `AlarmManager` and `BroadcastReceiver` for scheduling and displaying local task reminders.

## Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

- Android Studio
- An Android device or emulator

### Installation

1.  Clone the repository:
    ```sh
    git clone https://github.com/alphvmane/luminar.git
    ```
2.  Open the project in Android Studio.
3.  Allow Gradle to sync and download the required dependencies.
4.  Build and run the application on your chosen device or emulator. The required `google-services.json` file is included in the repository.

## Project Structure

The project is organized into several key packages:

-   `java/com/example/luminar`: Contains the main `Activity` classes that control the user interface, including `CalendarActivity`, `AddTaskActivity`, and `LoginActivity`.
-   `model`: Defines the data models for the application, such as `Task` (and its subclasses `NonRecurrentTask`, `RecurrentTask`), `User`, `Category`, and `Notification`. These classes represent the data structure used in the Firebase Realtime Database.
-   `services`: Manages background processes, primarily for notifications. This includes `NotificationScheduler` for setting alarms, `TaskReminderReceiver` for handling alarm intents, and `FCM` for Firebase Cloud Messaging events.
-   `res/layout`: Contains all the XML layout files for the application's activities and UI components.
-   `res/drawable`: Includes custom shapes, icons, and other visual assets.

## Contributors

- **Derrick Mangari:** Designed the Firebase Realtime Database schema, created the data models, and implemented Firebase Cloud Messaging.
- **Melinda Tran:** Designed the model structure, front-end and backend of Calendar page.
