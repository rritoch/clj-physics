(ns
  ^{
     :author "Ralph Ritoch <rritoch@gmail.com>"
     :doc "Physics Application"
   } Physics.core 
  (:gen-class)
  (:require [Physics config obj desktop filterlist]
       [Physics.Views ImageViewer]
       )
  (:import [Physics config desktop])
)


(defn exit
  []
  (System/exit 0)
)

(defn banner []
(let [appinfo (Physics.obj.info.)]
    (println (str (.app-name appinfo) " v" (.version appinfo)))
    (println (.copyright appinfo))
)
)

(defn -main [& args]
  (banner)
  (Physics.desktop/start-desktop)
)
