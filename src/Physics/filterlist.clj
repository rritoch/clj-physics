(ns
  ^{
     :author "Ralph Ritoch <rritoch@gmail.com>"
     :doc "Filter List"
   } Physics.filterlist
  (:gen-class)
)

(def filters (atom [
   {
      :name "Red filter"
      :class "com.vnetpublishing.filters.redchannel"
   }
   {
      :name "Fit"
      :class "com.vnetpublishing.filters.fit"
   }
]))


