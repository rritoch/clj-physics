(ns Physics.desktop  
  (:gen-class)
  (:import [javax.swing JFrame JLabel JButton]
           [java.awt.event WindowListener]
           [Physics.config]
           [Physics.obj.info])
)

(def frame 
  (let [appinfo (Physics.obj.info.)]
    (JFrame. (str (.app-name appinfo) " v" (.version appinfo)))
  )
)

(defn close []
  (if frame 
    (do 
      (.dispatchEvent frame (new java.awt.event.WindowEvent frame java.awt.event.WindowEvent/WINDOW_CLOSING))
      (def frame nil)
    )
  )
)

(defn create []
  (if frame 
    (close)
  )
  (let [appinfo (Physics.obj.info.)]
    (def frame (JFrame. (str (.app-name appinfo) " v" (.version appinfo))))
    (doto frame
      (.setDefaultCloseOperation JFrame/DISPOSE_ON_CLOSE)
      (.setSize 
          (get Physics.config/desktop :Physics.config/width) 
          (get Physics.config/desktop :Physics.config/height)
        )
      (.setVisible true)
    )
  )
)


