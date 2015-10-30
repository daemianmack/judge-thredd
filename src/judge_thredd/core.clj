(ns judge-thredd.core
  (:require [clojure.pprint :as pp]
            [clojure.string :as s]
            [judge-thredd.chat-logs :refer [messages]]))

(defn -main
  [filename]
  (prn :message-count (count (messages filename)))
  (prn :first-10-messages)
  (binding [pp/*print-right-margin* 150]
    (pp/pprint (take 10 (messages filename)))))
