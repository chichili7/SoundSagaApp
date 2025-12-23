🎧 SoundSaga – AudioBook Player (Android)

SoundSaga is an Android audiobook player application developed as part of
CSC 392/492 – Mobile Applications Development II at DePaul University.

The app enables users to browse, play, and resume audiobooks with chapter-level navigation, playback speed control, and persistent listening progress.

📱 Application Overview

SoundSaga consists of three core screens:

MainActivity – Browse the audiobook library

AudioBookActivity – Play and control audiobook playback

MyBooksActivity – Resume audiobooks currently in progress

📚 Main Library – MainActivity

Splash screen on app launch using SplashScreen API

Displays available audiobooks using RecyclerView + GridLayoutManager

2 columns in portrait mode

4 columns in landscape mode

Each audiobook entry includes:

Cover image

Title (scrolling marquee text)

Author (scrolling marquee text)

Long-press on a book displays a detailed dialog with:

Book image

Title and author

Publication date

Language

Total duration

Number of chapters

My Books icon in the top-right:

Opens MyBooksActivity

Displays an AlertDialog if no books are in progress

▶️ Audio Playback – AudioBookActivity

Automatically starts playback when a book is selected

Resumes playback from the last saved position when launched from My Books

Displays:

Audiobook cover image

Audiobook title (scrolling marquee)

Chapter title (scrolling marquee)

📖 Chapter Navigation

Swipe left/right to move between chapters

Tap previous / next arrows to change chapters

Chapter playback always starts from the beginning

Automatically advances to the next chapter when one finishes

🎚️ Playback Controls

Play / Pause

Skip backward / forward 15 seconds

SeekBar with:

Current playback time

Total chapter duration

⚡ Playback Speed Options

Available via popup menu:

0.75x

1.0x

1.1x

1.25x

1.5x

1.75x

2.0x

Playback speed is remembered when resuming the audiobook.

💾 State Persistence

Leaving the activity:

Stops playback

Saves chapter and progress

Updates My Books

Returns to MainActivity

📖 My Books – MyBooksActivity

Displays audiobooks currently in progress

Sorted by most recently played

Each entry shows:

Book image

Title and author

Current chapter

Current playback time / total chapter duration

Last played date and time

Tap an entry to resume playback

Long-press to delete an entry with confirmation dialog

🌐 Data Source

Audiobook data is fetched using Volley

Remote JSON API provides:

Audiobook metadata

Chapter information

MP3 streaming URLs

🛠️ Technologies & Libraries Used

Java (Android)

Volley – Network requests

MediaPlayer – Audio playback

RecyclerView & GridLayoutManager

ViewPager2 – Chapter swiping

PopupMenu

AlertDialog

OnCompletionListener

OnPageChangeCallback

Custom Fonts

⚠️ Error Handling

The app gracefully handles:

No internet connection

Invalid or unreachable URLs

Audio playback failures

User-friendly alerts are displayed, and the app safely navigates back to the main screen when required.

📐 Supported Configurations

Minimum SDK: API 29

Tested Emulators:

1080 × 1920 – Pixel, Pixel 2

1080 × 2400 – Pixel 7, Pixel 8

Fully responsive portrait and landscape layouts

🚀 How to Run the Project

Clone the repository:

git clone https://github.com/your-username/soundsaga-audiobook-player.git


Open the project in Android Studio

Sync Gradle files

Run on an emulator or physical device (API 29+)

🎓 Academic Context

This project was developed in strict accordance with the requirements and Android concepts covered in
CSC 392/492 – Mobile Applications Development II at DePaul University.

📌 Notes

All UI behavior, assets, and functionality follow the assignment specifications

Focused on clean architecture, smooth playback, and robust state handling
