(ns judge-thredd.core
  (:require [clojure.pprint :as pp]
            [clojure.string :as s]
            [judge-thredd.chat-logs :refer [irc-log-messages]]))

(defn wide-print
  "Customize pprint to print (short) messages in easily-scanned manner."
  [messages]
  (binding [pp/*print-right-margin* 150]
    (pp/pprint messages)))

(defn cosmetic-display
  "Print messages to STDOUT."
  [messages]
  (prn :message-count (count messages))
  (prn :first-10-messages)
  (wide-print (take 10 messages)))

(defn -main
  "Goal: produce a data structure supporting a threaded display
   as described in README.

   Current status: merely prints IRC messages for your inspection."
  [filename]
  (cosmetic-display (irc-log-messages filename)))
