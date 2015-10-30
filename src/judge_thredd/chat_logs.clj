(ns judge-thredd.chat-logs
  (:require [clojure.java.io :as io]
            [clj-time.core :as tc]
            [clj-time.format :as tf]
            [clojure.string :refer [split]]))


;; Avoid ugly object printing in recent Clojure versions.
;; Given an instance of org.joda.time.DateTime, print...
;;   <2015-08-07T08:56:00.000Z>
;; instead of...
;;   #object[org.joda.time.DateTime 0x361c294e "2015-08-07T08:56:00.000Z"]
(defmethod clojure.core/print-method org.joda.time.DateTime
  [date writer]
  (.write writer (str "<" (.toString date) ">")))

(def yyyy-MM-DD->datetime (tf/formatters :year-month-day))
(def hh-mm-ss->datetime (tf/formatters :hour-minute-second))


(defprotocol ChatReader
  (->messages [this source]))

(defn filename->datetime
  "Given input...
     example-chat-2015-08-07.txt
   output...
     <2015-08-07T00:00:00.000Z>"
  [filename]
  (let [yyyy-MM-DD (first (re-seq #"\d{4}-\d{2}-\d{2}" filename))]
    (tf/parse yyyy-MM-DD->datetime yyyy-MM-DD)))

(defn raw-time->datetime-map
  "Given input...
     \"[08:45:00]\"
   output...
     {:years 1970, :months 1, :days 1, :hours 8, :minutes 45, :seconds 0} "
  [raw-time]
  (->> (subs raw-time 1 (dec (count raw-time)))
       (tf/parse hh-mm-ss->datetime)
       tf/instant->map))

(defn apply-offset
  "Given a datetime and a raw message timestamp, return a datetime offset by that timestamp.
   Given input...
     <2015-08-07T00:00:00.000Z> \"[08:45:00]\"
   output...
     <2015-08-07T08:45:00.000Z>"
  [datetime raw-time]
  (let [{:keys [hours minutes seconds]} (raw-time->datetime-map raw-time)]
    (tc/plus datetime
             (tc/hours hours)
             (tc/minutes minutes)
             (tc/seconds seconds))))

(defn drop-colon
  [speaker]
  (subs speaker 0 (dec (count speaker))))

(defrecord IrcFreenodeLog []
  ChatReader
  (->messages [this source]
              (let [offset-for (partial apply-offset (filename->datetime source))]
                (with-open [rdr (io/reader (io/resource source))]
                  (doall
                   (for [line (line-seq rdr)
                         :let [[raw-time speaker text] (split line #" " 3)]]
                     [(offset-for raw-time)
                      (drop-colon speaker)
                      text]))))))

(defn messages
  [filename]
  (->messages (IrcFreenodeLog.) filename))
