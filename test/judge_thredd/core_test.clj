(ns judge-thredd.core-test
  (:require [judge-thredd.core :refer :all]
            [clojure.test :refer :all]))

(def test-conversation
  [[0 "alice"   "good morning"]
   [1  "bill"   "alice: good morning"]
   [5  "chuck"  "anybody up for breakfast burritos?"]
   [7  "daisy"  "chuck: i loathe oxygen!"]
   [90 "edward" "alice: morning!"]])

(def test-conversation-result
  {"alice" [{:members #{"alice" "bill" "edward"}
             :start 0
             :stop 90
             :msgs [[0   "alice" "good morning"]
                    [1    "bill" "alice: good morning"]
                    [90 "edward" "alice: morning!"]]}]
   "chuck" [{:members #{"chuck" "daisy"}
             :start 5
             :stop  7
             :msgs [[5 "chuck" "anybody up for breakfast burritos?"]
                    [7 "daisy" "chuck: i love breakfast burritos!"]]}]})

(deftest do-it-test
  (is (= test-conversation-result
         (do-it test-conversation))))
