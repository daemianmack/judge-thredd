(ns judge-thredd.core-test
  (:require [judge-thredd.core :as sut]
            [clojure.test :refer [deftest is testing]]))

(deftest -main-test
  (testing "A breaking test."
   (is (= :step-3-profit!
          (sut/-main "easy-goal-example-irc-log-2015-08-07.txt")))))

