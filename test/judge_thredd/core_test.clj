(ns judge-thredd.core-test
  (:require [judge-thredd.core :refer :all]
            [clojure.test :refer :all]))

(deftest -main-test
  (is (= :step-3-profit!
         (-main "example-irc-log-2015-08-07.txt"))))

