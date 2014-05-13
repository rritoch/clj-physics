(ns Physics.core 
  (:gen-class)
  (:require [Physics config obj desktop])
  (:import [Physics config desktop])
)


(defn banner []
 (let [appinfo (Physics.obj.info.)]
    (println (str (.app-name appinfo) " v" (.version appinfo)))
    (println (.copyright appinfo))
 )
)
(defn -main [& args]
  (banner)
  ;(Physics.desktop/create)
)


