(ns bufferfn-op.core
  (:gen-class)
  (:use cascalog.api))

(def records
  [{:url "google.com" :type {:a "site" :random "01"}}
   {:url "yahoo.com" :type {:a "site" :random nil}}
   {:url "ask.com" :type {:a "site" :random "01"}}
   {:url "bing.net" :type {:a "website" :random nil}}
   {:url "altavista.net" :type {:a "website" :random nil}}
   ])

(defn parse-extension
  "Returns a string composed of the last dot of input and any following characters
  Return 'none' if no extension in input"
  [input]
  (try
    (let [regex (re-pattern ".*(\\.[^.]+)$")
          extension (->> input (re-matches regex) second)]
      (if (not extension) "none" extension))
    (catch Exception e "none")))

(defn make-bucket
  [op]
  (bufferfn  [items]
            (->> items
                 (group-by op))))

(def make-bucket-url
  (make-bucket #(-> % (get :url) parse-extension)))

(defn cascalog-make-bucket-run []
   (?<- (stdout)
       [?id ?group]
       (records :> ?records)
       (make-bucket-url ?records :> ?id ?group)))

(defbufferfn bucket-url [items]
  (->> items
       (group-by #(-> % (get :url) parse-extension))))

(defn cascalog-bucket-url-run []
  (?<- (stdout)
       [?id ?group]
       (records :> ?records)
       (bucket-url ?records :> ?id ?group)))

(defn clojure-run []
  (bucket-url records))

(defn -main
  []
  (println "### Press enter to do cascalog using explicit bucket-url...")
  (read-line)
  (cascalog-bucket-url-run)
  (println "#This works")

  (println "### Press enter to do clojure run without cascalog...")
  (read-line)
  (println (clojure-run))
  (println "# This works too")

  (println "### Press enter to do cascalog run using generic make-bucket function...")
  (read-line)
  (cascalog-make-bucket-run)
  (println "# This doesn't work see 'Unable to resolve symbol: op in this context' error"))
