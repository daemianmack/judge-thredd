(defproject judge-thredd "0.1.0-SNAPSHOT"
  :description "An exercise: consume an IRC log and attempt to produce a data structure that groups the disparate IRC messages by thread."
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-time "0.10.0"]]
  :main judge-thredd.core)
