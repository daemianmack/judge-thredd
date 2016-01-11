(ns judge-thredd.core-test
  (:require [judge-thredd.core :refer :all]
            [clojure.test :refer :all]))

(def test-conversation
  [[0 "alice"  "good morning"]
   [1  "bill"  "alice: good morning"]
   [5  "chuck" "anybody up for breakfast burritos?"]
   [7  "daisy" "chuck: i love breakfast burritos!"]
   [90 "eddie" "alice: morning!"]])

(def test-conversation-result nil)

(deftest main-test
  (is (= test-conversation-result
         (-main test-conversation))))
