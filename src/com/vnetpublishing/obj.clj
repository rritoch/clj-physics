(ns
  ^{
     :author "Ralph Ritoch <rritoch@gmail.com>"
     :doc "Objects"
   } com.vnetpublishing.obj)

(defprotocol ApplicationInfo
  (^{:type String} author [obj])
  (^{:type String} author-email [obj])
  (^{:type String} copyright [obj])
  (^{:type String} app-name [obj])
  (^{:type String} version [obj])
)