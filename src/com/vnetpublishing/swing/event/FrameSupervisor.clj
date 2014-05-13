(ns
  ^{
     :author "Ralph Ritoch <rritoch@gmail.com>"
     :doc "Swing Frame Supervisor"
   } com.vnetpublishing.swing.event.FrameSupervisor
  
  (:gen-class
    :name com.vnetpublishing.swing.event.FrameSupervisor
    :state state
    :init init
    :prefix - 
    :implements [java.awt.event.WindowListener]
    :extends Object
    :constructors {[javax.swing.JFrame] []}
    :methods [
      [activate [] void]
    ]
  )
)

(defn -windowActivated 
  [this e]
)

(defn -windowClosed 
  [this e]
  (let [state (.state this)
       s (deref state)
       timer (:timer s)
       scheduler (:scheduler s)
       
    ]
    (if timer 
      (do
        (swap! state assoc :timer nil)
        (.cancel timer true)
      )
    )
    (if scheduler 
      (do
        (swap! state assoc :scheduler nil)
        (.shutdown scheduler)
      )
    )
  )
)

(defn -windowClosing 
  [this e]
)
(defn -windowDeactivated
  [this e]
)

(defn -windowDeiconified
  [this e]
)

(defn -windowIconified 
  [this e]
)
(defn -windowOpened
  [this e]
)
  
(defn -activate
  [this]
  (let [s (deref (.state this))
       frame (:frame s)
    ]
    (.addWindowListener frame this)
    
    (if (:timer s)
      nil
      (let [
          scheduler (java.util.concurrent.Executors/newScheduledThreadPool 1)
          timer (.scheduleAtFixedRate scheduler
            (proxy [Runnable] [] 
             (run [] 
               (javax.swing.SwingUtilities/invokeLater 
                 (proxy [Runnable] [] 
                   (run [] 
                     (do
                       (try
                          (.revalidate frame)
                          (catch Throwable t nil)
                       )
                       (.repaint frame)
                     )
                   )
                 )
               )
             )
            )
            2000 
            40
            java.util.concurrent.TimeUnit/MILLISECONDS
          )
        ]
        (swap! (.state this) assoc :timer timer)
        (swap! (.state this) assoc :scheduler scheduler)
      ) 
    )
  )
)

(defn -init
  [frame]
  [[] (atom {:frame frame})]
)


