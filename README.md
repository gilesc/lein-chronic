# lein-chronic

Schedule leiningen tasks for timed execution.

Often, it's useful to be able to run Clojure tasks on a timer, but
adding these tasks to crontab or Jenkins/Hudson unfortunately
separates the timing configuration from the rest of the program,
making it less portable and harder to install.

lein-chronic takes advantage of the fact that leiningen runs in a
different JVM process than many of the jobs it spawns (particularly
"lein run"), so the lightweight leiningen process can be run
continually and spawn potentially very resource-intensive Clojure
functions only when necessary.

## Usage

First, add lein-chronic to your :dev-dependencies, and add a :chronic
section to your project.clj, like so:

        (ns myproj
          :dependencies [[org.clojure.clojure "1.2.1"]]
          :dev-dependencies [[lein-chronic "1.0.0-SNAPSHOT]]
          :chronic 
          [{:name "taskA"
            :schedule "*/5 * * * *"
            :command "run -m myproj.a"}
           {:name "taskB"
            :schedule "* 23 ? * MON-FRI"
            :command "uberjar"}])

The :chronic entry is a vector of maps, each map describing a task to
be performed on a regular basis.  The task name is optional, and will
just be used to log when tasks begin.  The :schedule command uses
[Cron syntax](http://en.wikipedia.org/wiki/Cron#Examples_2) to define
when tasks will be executed, and the :command entry is a string
describing any command that would normally be passed to leiningen
manually.  To start the lein-chronic process, simply run:

           lein chronic

The lein chronic process will now run until you kill it. In the above
examples, "taskA" would run the main method of the myproj.a namespace
every 5 minutes, while taskB would create an uberjar at 11PM every
weekday night.

## TODO

*  Incorporate trampolining, so that the leiningen process could be
   stopped and restarted with different settings, without affecting
   tasks already running.  
*  Notify when tasks are completed as well as when they begin.
*  Sanitize input a little better.


## License

Copyright (C) 2011 Cory Giles

Distributed under the Eclipse Public License, the same as Clojure.
