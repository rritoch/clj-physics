(ns
  ^{
     :author "Ralph Ritoch <rritoch@gmail.com>"
     :doc "Filter Selection Dialog Panel"
   } com.vnetpublishing.widgets.FilterSelectDialogPanel
  
  (:gen-class
    :name com.vnetpublishing.widgets.FilterSelectDialogPanel
    :state state
    :init init
    :prefix -
    :extends javax.swing.JPanel
    :implements [java.awt.event.ActionListener]
    :methods [
      [render [javax.swing.JDialog clojure.lang.PersistentVector clojure.lang.IFn clojure.lang.PersistentVector] void]
      [setContent [javax.swing.JPanel] void]
    ]
  )
)

(defn -actionPerformed
  [this event]
  
  (let [
    s (deref (.state this))
    cmd (.getActionCommand event)]
    (cond
      (= cmd "button-cancel") (.dispose (:dialog s))
      (= cmd "button-ok") (do
        (apply (:callback-ok s) (into (:callback-extra-args s) [(:current-filter s)] ))
        (.dispose (:dialog s))
      )
      (= cmd "button-next") (let [selected-index (.getSelectedIndex (:filter-jlist s))]
        (if (= -1 selected-index)
          (javax.swing.JOptionPane/showMessageDialog (:dialog s) "Please select a filter or cancel")
          (let [filt (clojure.lang.Reflector/invokeConstructor (resolve (symbol (:class (get (:available-filters s) selected-index)))) (into-array Object []))
              filt-panel (javax.swing.JPanel.)
              button-panel (javax.swing.JPanel.)
              button-cancel (javax.swing.JButton. "Cancel")
              button-ok (javax.swing.JButton. "OK")
              cfg-panel (.createConfigurePanel filt)
              cfg-panel-wrapper (javax.swing.JPanel.)
              button-panel-layout (javax.swing.BoxLayout. button-panel javax.swing.BoxLayout/X_AXIS)
              filt-panel-layout (javax.swing.BoxLayout. filt-panel javax.swing.BoxLayout/Y_AXIS)
              title-jlabel (javax.swing.JLabel. (.name filt))
            ]
            
            
            (.setLayout filt-panel filt-panel-layout)
            (.setLayout button-panel button-panel-layout)
            
            (.add button-panel button-cancel)
            (.add button-panel (javax.swing.Box/createRigidArea (java.awt.Dimension. 5 0)))
            (.add button-panel button-ok)
            
            (.add cfg-panel-wrapper cfg-panel)
            
            (.setPreferredSize cfg-panel-wrapper (java.awt.Dimension. 387 200))
            (.setMaximumSize cfg-panel-wrapper (java.awt.Dimension. 387 200))
            
            (.add filt-panel title-jlabel)
            (.add filt-panel (javax.swing.Box/createRigidArea (java.awt.Dimension. 0 5)))
            (.add filt-panel cfg-panel-wrapper)
            (.add filt-panel (javax.swing.Box/createRigidArea (java.awt.Dimension. 0 5)))
            (.add filt-panel button-panel)
            
            (.setActionCommand button-cancel "button-cancel")
            (.addActionListener button-cancel this)
    
            (.setActionCommand button-ok "button-ok")
            (.addActionListener button-ok this)
            
            
            (.setContent this filt-panel)
            (swap! (.state this) assoc :current-filter filt)
            
            
          )
        )
      )
      :else
      nil
    )
  )
)

(defn -setContent
  [this panel]
  (.removeAll this)
  (.add this panel)
  (.revalidate this)
  (.repaint this)
)

(defn -render
  [this dlg available-filters callback-ok callback-extra-args]
  
  (swap! (.state this) assoc :dialog dlg)
  (swap! (.state this) assoc :available-filters available-filters)
  
  (let [
      button-next (javax.swing.JButton. "Next")
      button-cancel (javax.swing.JButton. "Cancel")
      button-panel (javax.swing.JPanel.)
      button-panel-layout (javax.swing.BoxLayout. button-panel javax.swing.BoxLayout/X_AXIS)
      filter-jlist (javax.swing.JList. (into-array String (map (fn [x] (:name x)) available-filters)))
      select-panel (javax.swing.JPanel.)
      panel-layout (javax.swing.BoxLayout. this javax.swing.BoxLayout/Y_AXIS)
    ]
    
    ;(println available-filters)
    
    (.setActionCommand button-cancel "button-cancel")
    (.addActionListener button-cancel this)
    
    (.setActionCommand button-next "button-next")
    (.addActionListener button-next this)


    ;(.setBorder button-cancel (javax.swing.BorderFactory/createEmptyBorder 5 5 5 5))
    ;(.setBorder button-next (javax.swing.BorderFactory/createEmptyBorder 5 5 5 5))
    
    (.add button-panel button-cancel)
    (.add button-panel (javax.swing.Box/createRigidArea (java.awt.Dimension. 5 0)))
    (.add button-panel button-next)

    
    (.setPreferredSize filter-jlist (java.awt.Dimension. 387 200))
    (.setSelectionMode filter-jlist javax.swing.ListSelectionModel/SINGLE_SELECTION)
    (.add select-panel filter-jlist)
    (.add select-panel (javax.swing.Box/createRigidArea (java.awt.Dimension. 0 5)))
    
    ;(.add this pane-title)
    (.add select-panel button-panel)
    
    (.setLayout button-panel button-panel-layout)
    (.setLayout this panel-layout)
    (.setPreferredSize this (java.awt.Dimension. 427 320))
    (swap! (.state this) assoc :select-panel select-panel)
    (swap! (.state this) assoc :filter-jlist filter-jlist)
    
    (swap! (.state this) assoc :callback-ok callback-ok)
    (swap! (.state this) assoc :callback-extra-args callback-extra-args)
    
    
    (.setContent this select-panel)
  )
)

(defn -init
  []
  [[] (atom {})]
)

