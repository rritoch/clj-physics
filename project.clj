(defproject Physics "0.1.0"
  :description "Physics Studio"
  :url "http://www.vnetpublishing.com"
  :license {:name "VNetPL"
            :url "http://www.vnetpublishing.com/Legal/Licenses/2010/10/vnetlpl.txt"}
  :dependencies [
                 [org.clojure/clojure "1.5.1"];,
                 ;[com.vnetpublishing/clash "0.0.1"]
                 ;[org.jocl/CL "0.1.9"]
  ]
  :aot :all
  :jar-name "Physics-raw.jar"
  :uberjar-name "Physics.jar"
  :main Physics.core
)
