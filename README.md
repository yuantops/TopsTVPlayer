TopsTVPlayer
============

Android video player which plays real-time live video stream and on demand video stream, supporting HTTP/RTSP protocol.

##Notes:

- (2015.09.13) Update: Basic features have finished developing in this version. 

- This is the **Android client part** of a complete online video serving system. This player fetches JSON-format data from the web server, parsing it and then playing the vod/real-time video stream from the retrieved stream url. It does NOT support playing local videos.

- (2016.04.20) Update: web server for this project can be found at [TopsAPIServer](https://github.com/yuantops/TopsAPIServer). **Server part** of this online video system includes: a media server (Helix), a web api server (Tomcat) and a database server (MySQL). Deployments of all those servers are requisites to make this app work.

- Deployment guides (Chinese version) of the server can be found at [this link](http://blog.yuantops.com/tech/write-your-own-vod-system).

##Projects/Libraries used by this project:
- [ActionbarSherlock](http://actionbarsherlock.com/)
- [Vitamio SDK](https://www.vitamio.org/en/)
- [Android Volley](https://github.com/mcxiaoke/android-volley)
