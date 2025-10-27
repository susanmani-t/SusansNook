# Susan's Nook - Personal Productivity & Wellness App

A thoughtfully designed Android application that combines personal organization with daily inspiration, featuring a calming green aesthetic for a peaceful user experience.

## 📱 Features

### 🏠 Main Dashboard
- **Random Image Display**: Personalized photo rotation from your curated collection
- **Daily Inspirational Quotes**: Fresh motivational quotes that update at midnight via Quotable API
- **Task Management**: Integrated todo list with completion tracking and persistence
- **Quick Navigation**: Easy access to diary and games sections

### 📷 Photo Management
- **Personal Gallery**: Add and manage your favorite images
- **Smart Display**: Random selection ensures variety in your daily view
- **Easy Editing**: Floating action button for quick gallery access

### 📔 Digital Diary
- **Entry Management**: Create and view diary entries with date organization
- **Chronological Display**: Entries displayed in reverse chronological order
- **Persistent Storage**: All entries saved locally for privacy

### ✅ Task Management
- **Intuitive Interface**: Clean, card-based todo list design
- **Task Operations**: Add, complete, and delete tasks with visual feedback
- **Strike-through Completion**: Visual indication of completed tasks
- **Persistent Data**: Tasks saved locally with automatic synchronization

### 🎮 Games Section
- **Future Expansion**: Placeholder for upcoming entertainment features

## 🎨 Design Philosophy

Susan's Nook employs a **calm green color palette** designed to promote tranquility and focus:

- **Primary Green** (`#464b37`): Main UI elements and buttons
- **Background Sage** (`#848871`): Comfortable background tone
- **Dark Accent** (`#3f3f3f`): Card backgrounds and surfaces
- **Deep Forest** (`#262b15`): Input fields and darker accents
- **Light Sage** (`#afb4ad`): Primary text and icons

This palette creates a cohesive, nature-inspired interface that reduces eye strain and promotes a sense of calm during use.

## 🛠️ Technical Implementation

### Architecture
- **Language**: Java
- **UI Framework**: Android Material Design Components
- **Layout**: ConstraintLayout for responsive design
- **Data Persistence**: SharedPreferences and local file storage

### Key Components
- **RecyclerView**: Efficient list rendering for todos and galleries
- **Retrofit**: HTTP client for quote API integration
- **Material Cards**: Modern, elevated UI components
- **ShapeableImageView**: Rounded image displays

### Data Management
- **TodoStorage**: Local persistence for task data
- **ImageStorage**: File-based image URI management
- **Quote Caching**: Daily quote storage with SharedPreferences

## 📋 Installation

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK API level 21+ (Android 5.0)
- Gradle 7.0+

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/susans-nook.git
   cd susans-nook
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory

3. **Build the project**
   ```bash
   ./gradlew build
   ```

4. **Run on device or emulator**
   - Connect Android device or start emulator
   - Click "Run" in Android Studio

## 🔧 Configuration

### API Configuration
The app uses the [Quotable API](https://quotable.io) for daily inspirational quotes. No API key required.

### Permissions
The following permissions are used:
- `INTERNET`: For fetching daily quotes
- `READ_EXTERNAL_STORAGE`: For accessing user photos (if needed)

## 📖 Usage Guide

### Getting Started
1. **Launch the app** - You'll see the main dashboard with a placeholder for your first image
2. **Add photos** - Tap the edit button (pencil icon) to add personal images
3. **Create tasks** - Use the todo section to add daily tasks
4. **Write diary entries** - Access the diary via the bottom navigation

### Daily Workflow
- **Morning**: Check your daily quote and review tasks
- **Throughout the day**: Mark tasks as complete, add new ones as needed
- **Evening**: Write in your diary to reflect on the day

### Task Management
- **Add tasks**: Type in the input field and press the add button or Enter
- **Complete tasks**: Tap the checkbox to mark items as done
- **Delete tasks**: Use the delete button (trash icon) on each item

## 🤝 Contributing

We welcome contributions to Susan's Nook! Please follow these guidelines:

### Development Setup
1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Make your changes
4. Test thoroughly on multiple devices
5. Commit with clear messages: `git commit -m 'Add amazing feature'`
6. Push to your branch: `git push origin feature/amazing-feature`
7. Open a Pull Request

### Code Style
- Follow Android development best practices
- Use meaningful variable and method names
- Comment complex logic
- Maintain the established green color palette

### Bug Reports
When reporting bugs, please include:
- Device model and Android version
- Steps to reproduce
- Expected vs. actual behavior
- Screenshots if applicable

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Quotable API** for providing inspirational quotes
- **Material Design** for UI component guidelines
- **Android Community** for development resources and support

---

*Susan's Nook - Your personal sanctuary for productivity and peace of mind.*

- Full disclosure: this README was AI generated
