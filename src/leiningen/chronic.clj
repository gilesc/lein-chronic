(ns leiningen.chronic
  (:import
   it.sauronsoftware.cron4j.Scheduler)
  (:use
   [leiningen.core :only [make-groups task-not-found apply-task]]))

(defn chronic [project]
  (let [scheduler (Scheduler.)]
    (doseq [task (:chronic project)
            :let [f (fn []
                      (doseq [[subtask & args] (make-groups (.split (:command task) " "))]
                        (if (:name task)
                          (println (str "Executing: " (:name task))))
                        (apply-task subtask project args task-not-found)))]]
      (.schedule scheduler (task :schedule) f))
    (.start scheduler)
    (.join (Thread/currentThread))))
