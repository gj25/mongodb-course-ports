## Setup

This tutorial for php version 5.4.+ , there is a built in developer server http://php.net/manual/en/features.commandline.webserver.php

# Run

1. hw1-2 homework
$ cd 'your_course_dir'
$ php hw1-2.php

2. hw1-3 homework
You must run the server from cli, router.php is a simple router for built in server only
$ cd 'your_course_dir'
$ php -S localhost:8080 router.php

When call from other console window
$ curl http://localhost:8088/hw1/50

If you prefer to use apache or nginx (or else) web server, you must setup routes for it
