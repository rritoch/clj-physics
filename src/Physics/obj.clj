(ns
  ^{
     :author "Ralph Ritoch <rritoch@gmail.com>"
     :doc "Physics Objects"
   } Physics.obj
  (:require [com.vnetpublishing obj])
)

(defrecord info [] com.vnetpublishing.obj.ApplicationInfo
  (^{:type String} author [this] "Ralph Ritoch")
  (^{:type String} author-email [this] "Ralph Ritoch <rritoch@gmail.com>")
  (^{:type String} copyright [this] "Copyright \u00A9 2014 Ralph Ritoch. All rights reserved.")
  (^{:type String} app-name [this] "Physics")
  (^{:type String} version [this] "0.0.1-alpha")
)
