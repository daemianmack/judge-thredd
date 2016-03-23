(ns judge-thredd.core
  (:require [clojure.pprint :as pp]
            [clojure.string :as s]
            [judge-thredd.chat-logs :refer [irc-log-messages]]))

(defn wide-print
  [messages]
  (binding [pp/*print-right-margin* 150]
    (pp/pprint messages)))

(defn cosmetic-display
  [messages]
  (prn :message-count (count messages))
  (prn :first-10-messages)
  (wide-print (take 10 messages)))

(defn -main
  [filename]
  (cosmetic-display (irc-log-messages filename)))
