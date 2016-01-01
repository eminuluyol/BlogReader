# BlogReader
These are final version project files of the Team Treehouse's Build a Blog Reader App in Android, provides to show [Team Treehouse Blog Page](http://blog.teamtreehouse.com/) in your application  It is a really helpful tutorial for understading the
base logic of [Android Life Cycle](http://developer.android.com/training/basics/activity-lifecycle/index.html). It provides the sufficient
information about getting the [JSON](http://stackoverflow.com/questions/383692/what-is-json-and-why-would-i-use-it) data from server and how to handle it.
I didn't follow the same path that the tutor instructs, tried to write a [Custom Adapter](http://stackoverflow.com/questions/8166497/custom-adapter-for-list-view) which will be always needed to make your own design.
You can learn how to use [WebView] (http://developer.android.com/reference/android/webkit/WebView.html) as well.


## Features

With the app, you can:
* Fetch the blog topic title and show them in a ListView. Then if you click on it you can show the detail web page in WebView Activity.


## How to Work with the Source
This app uses **Team Treehouse's** api to fetch data from server. You can query it like that **http://blog.teamtreehouse.com/api/get_recent_summary/?count=" + NUMBER_OF_POSTS**
It provides you the recent topics on blog and **NUMBER_OF_POSTS** part is a integer value that up to you. It specifies the number of posts that will be shown.

## Screens
![ScreenShot](http://i65.tinypic.com/245wu34.jpg)
![ScreenShot](http://i67.tinypic.com/eimbyf.jpg)


## Libraries

* [OverscrollScale](https://github.com/dodola/OverscrollScale)
* [CircularProgressBar](https://github.com/lopspower/CircularProgressBar)
