(ns
  ^{
     :author "Ralph Ritoch <rritoch@gmail.com>"
     :doc "Filter Configuration Dialog"
   } com.vnetpublishing.widgets.FilterConfigDialog
  
  (:gen-class
    :name com.vnetpublishing.widgets.FilterConfigDialog
    :state state
    :init init
    :prefix -
    :implements [java.awt.event.ActionListener]
    :extends javax.swing.JDialog
    :methods [
      [render [] void]
      [setConfigurePanel [javax.swing.JPanel] void]
    ]
  )
)

(defn -actionPerformed
  [this event]
  (let [cmd (.getActionCommand event)
      s (deref (.state this))
    ]
    (cond
     (= cmd "button-ok")
      (.setVisible this false)
    )
  )
)

(defn -setConfigurePanel
  [this cfg-panel]
  (let [s (deref (.state this))
     config-panel (:config-panel s)
    ]
    (.removeAll config-panel)
    (.add config-panel cfg-panel)
  )
)

(defn -render
  [this]
  
  (let [config-panel (javax.swing.JPanel.)
        button-panel (javax.swing.JPanel.)
        button-ok (javax.swing.JButton. "OK")
      ]
    (.setActionCommand button-ok "button-ok")
    (.addActionListener button-ok this)
    (.add button-panel button-ok)
    (.add this config-panel)
    (.add this button-panel)
    (swap! (.state this) assoc :config-panel config-panel)
  )
)

(defn -init
  [owner]
  [[owner] (atom {:owner owner})]
)
