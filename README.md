🎧 SoundSaga – AudioBook Player (Android)

SoundSaga is an Android audiobook player application developed as part of CSC 392/492 – Mobile Applications Development II at DePaul University.
The app allows users to browse, play, and resume audiobooks with chapter-level control, playback speed options, and persistent listening progress.

📱 Features
📚 Main Library (MainActivity)

Splash screen on app launch using SplashScreen API

Displays available audiobooks in a GridLayout

2 columns in portrait mode

4 columns in landscape mode

Each audiobook shows:

Cover image

Title and author (scrolling marquee text)

Long-press on a book displays detailed information:

Book image, title, author, release date

Language, total duration, number of chapters

▶️ Audio Playback (AudioBookActivity)

Automatically starts playback when a book is selected

Supports resume playback from last saved position

Chapter navigation:

Swipe left/right between chapters

Use previous/next arrows

Playback controls:

Play / Pause

Skip backward / forward 15 seconds

SeekBar with current time and total chapter duration

Playback speed options:

0.75x, 1.0x, 1.1x, 1.25x, 1.5x, 1.75x, 2.0x

Automatically advances to the next chapter when one finishes

Saves progress when leaving the activity

📖 My Books (MyBooksActivity)

Displays audiobooks currently in progress

Sorted by most recently played

Shows:

Book image, title, author

Current chapter

Playback progress and last played date/time

Tap to resume playback

Long-press to delete an entry with confirmation dialog

Displays an AlertDialog when no books are in progress

🌐 Data Source

Audiobook data is fetched using Volley from a remote JSON API:

Contains audiobook metadata and chapter-level MP3 URLs

🛠️ Technologies & Libraries Used

Java (Android)

Volley – network requests

MediaPlayer – audio playback

ViewPager2 – chapter swiping

RecyclerView & GridLayoutManager

PopupMenu

AlertDialog

Custom Fonts

OnCompletionListener

OnPageChangeCallback

⚠️ Error Handling

Gracefully handles:

No internet connection

Invalid or unreachable URLs

Audio playback failures

Displays appropriate alerts and safely returns users to the main screen when needed

📐 Supported Configurations

Minimum SDK: API 29

Tested on:

1080 × 1920 (Pixel, Pixel 2)

1080 × 2400 (Pixel 7, Pixel 8)

Fully responsive portrait and landscape layouts

🎓 Academic Context

This project was developed strictly following the course requirements and Android concepts covered in class for CSC 392/492 – Mobile Applications Development II.
