(ns judge-thredd.log-readers-test
  (:require [judge-thredd.log-readers :as log-readers]
            [clojure.test :refer [deftest is testing]]))


(def message-triples
  [[#inst "2015-08-07T08:45:00.000-00:00" "Alice" "Good morning!"]
   [#inst "2015-08-07T08:47:00.000-00:00" "Brian" "Alice: Good morning!"]
   [#inst "2015-08-07T08:52:00.000-00:00" "Chuck" "Anybody up for breakfast burritos?"]
   [#inst "2015-08-07T08:53:00.000-00:00" "Daisy" "Chuck: I love those"]
   [#inst "2015-08-07T08:54:00.000-00:00" "Eddie" "Alice: morning!"]
   [#inst "2015-08-07T08:55:00.000-00:00" "Daisy" "Are they in the kitchen?"]
   [#inst "2015-08-07T08:56:00.000-00:00" "Chuck" "Daisy: yes"]])

(deftest irc-messages-test
  (is (= message-triples (log-readers/irc-messages "example-irc-log-2015-08-07.txt"))))
