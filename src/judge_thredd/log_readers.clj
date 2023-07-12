(ns judge-thredd.log-readers
  (:require [clojure.java.io :as io]
            [clj-time.core :as tc]
            [clj-time.coerce :refer [to-date]]
            [clj-time.format :as tf]
            [clojure.string :refer [split]]))


(def yyyy-MM-DD->datetime (tf/formatters :year-month-day))
(def hh-mm-ss->datetime (tf/formatters :hour-minute-second))


(defprotocol ChatReader
  (->messages [this source]))

(defn filename->datetime
  "Given input...
     \"some-irc-log-file-2015-08-07.txt\"
   output...
     #object[org.joda.time.DateTime \"2015-08-07T00:00:00.000Z\"]"
  [filename]
  (let [yyyy-MM-DD (re-find #"\d{4}-\d{2}-\d{2}" filename)]
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
     #object[org.joda.time.DateTime \"2015-08-07T00:00:00.000Z\"] \"[08:45:00]\"
   output...
     #object[org.joda.time.DateTime \"2015-08-07T08:45:00.000Z\"]"
  [datetime raw-time]
  (let [{:keys [hours minutes seconds]} (raw-time->datetime-map raw-time)]
    (to-date (tc/plus datetime
                      (tc/hours hours)
                      (tc/minutes minutes)
                      (tc/seconds seconds)))))

(defn drop-colon
  [speaker]
  (subs speaker 0 (dec (count speaker))))

(defrecord IrcFreenodeLog []
  ChatReader
  (->messages [this source-file]
              (let [offset-for (partial apply-offset (filename->datetime source-file))]
                (with-open [rdr (io/reader (io/resource source-file))]
                  (doall
                   (for [line (line-seq rdr)
                         :let [[raw-time speaker text] (split line #" " 3)]]
                     [(offset-for raw-time)
                      (drop-colon speaker)
                      text]))))))

(defn irc-messages
  [irc-log-resource]
  (->messages (IrcFreenodeLog.) irc-log-resource))
