(ns 
  ^{
     :author "Ralph Ritoch <rritoch@gmail.com>"
     :doc "Image Filter List Panel"
   }

   com.vnetpublishing.widgets.ImageFilterListPanel
  
  (:gen-class
    :name com.vnetpublishing.widgets.ImageFilterListPanel
    :state state
    :init init
    :prefix -
    :extends javax.swing.JPanel
    :implements [java.awt.event.ActionListener com.vnetpublishing.datatypes.ImageFilter]
    :methods [
      [render [java.awt.Component clojure.lang.Atom] void]
      [addFilter [com.vnetpublishing.datatypes.ImageFilter] void]
      [getConfigureDialog [javax.swing.JPanel] com.vnetpublishing.widgets.FilterConfigDialog]
    ]
  )
  (:require [com.vnetpublishing.widgets.FilterConfigDialog])
)

(defn add-filter
  [me filt]
  (.addFilter me filt)
)

(defn -name
  [this]
  "Filter List"
)

(defn -createConfigurePanel
  [this]
  (let [configure-panel (javax.swing.JPanel.)]
    configure-panel
  )
)

(defn -getConfigurePanel
  [this]
  (if (:configure-panel (deref (.state this)))
    (:configure-panel (deref (.state this)))
    (reset! (.state this) (assoc (deref (.state this)) :configure-panel (.createConfigurePanel this)))
  )
)

(defn -getConfigureDialog
  [this cfg-panel]
  
  (let [s (deref (.state this))
        dlg (if (:configure-dialog s)
        (:configure-dialog s)
        (let [dlg-n (com.vnetpublishing.widgets.FilterConfigDialog. (:parent s))]
          (.render dlg-n)
          (reset! (.state this) (assoc (deref (.state this)) :configure-dialog dlg-n))
          (.pack dlg-n)
          (.setSize dlg-n (java.awt.Dimension. 400 300))
          dlg-n
        )
      )
    ]
    (.setConfigurePanel dlg cfg-panel)
    dlg
  )
)

(defn -createCompatibleDestImage
  [this src destCM]
  
  (let [writable-raster (.createCompatibleWritableRaster destCM ((int (.getWidth src)) (int (.getHeight src))))
        is-raster-premultiplied false
        properties (java.util.Hashtable.)
     ]
    (java.awt.image.BufferedImage. destCM writable-raster is-raster-premultiplied properties)
  )
)

(defn -filter
  [this src dest]
  (let [
       w (int (.getWidth dest))
       h (int (.getHeight dest))
       work-image (.getScaledInstance src w h java.awt.Image/SCALE_SMOOTH)
       pixels-len (* w h)
       _ (let [g (.createGraphics dest)] (.drawImage g work-image 0 0 (java.awt.Color/BLACK) nil) (.dispose g))
       r (.getRaster dest)
       pixels (.getPixels r 0 0 w h (int-array (* 4 pixels-len)))
       _ (.setPixels r 0 0 w h pixels)
       filter-list-items (deref (:filter-list-items (deref (.state this))))
       len (count filter-list-items)
    ]
    (loop [ctr 0]
      (if (= ctr len)
        dest
        (recur
          (do
            (.filter (get filter-list-items ctr) dest dest)
            (+ ctr 1)
          )
        )
      )
    )
  )
)

(defn -transform 
  [this src target]
    (let [
       dest (java.awt.image.BufferedImage. (.getWidth src) (.getHeight src) java.awt.image.BufferedImage/TYPE_INT_ARGB)
       w (int (.getWidth dest))
       h (int (.getHeight dest))
       work-image (.getScaledInstance src w h java.awt.Image/SCALE_SMOOTH)
       pixels-len (* w h)
       _ (let [g (.createGraphics dest)] (.drawImage g work-image 0 0 (java.awt.Color/BLACK) nil) (.dispose g))
       r (.getRaster dest)
       pixels (.getPixels r 0 0 w h (int-array (* 4 pixels-len)))
       _ (.setPixels r 0 0 w h pixels)
       filter-list-items (deref (:filter-list-items (deref (.state this))))
       len (count filter-list-items)
    ]
    (loop [ctr 0 out dest]
      (if (= ctr len)
        out
        (recur
          (+ ctr 1)
          (.transform (get filter-list-items ctr) out target)
        )
      )
    )
  )
)

(defn -getBounds2D
  [this src]
  (java.awt.Rectangle. 0 0 (.getWidth src) (.getHeight src))
)

(defn -getPoint2D
  [this srcPt dstPt]
  (.setLocation dstPt (.getX srcPt) (.getY srcPt))
  dstPt
)

(defn -getRenderingHints
  []
  nil
)



(defn -addFilter
  [this filt]
  
  (let [s (deref (.state this))
       filter-list-items (:filter-list-items s)
       ;filter-list-jlist (:filter-list-jlist s)
       filter-list-model (:filter-list-model s)
       parent-frame (:parent s)
     ]
    
    (.addElement filter-list-model (.name filt))
    (swap! filter-list-items into [filt])
    
    (.revalidate this)
    (.repaint parent-frame)
  )
)
 

(defn -actionPerformed
  [this event]
  
  (let [cmd (.getActionCommand event)
      s (deref (.state this))
    ]
    (cond
     (= cmd "button-new") 
       (let [dlg (javax.swing.JDialog. (:parent s) "Create Filter")
             frame (com.vnetpublishing.widgets.FilterSelectDialogPanel.)
             ]
         (.render frame dlg (if (:available-filters s) (deref (:available-filters s))) add-filter [this])
         (.add dlg frame);
         (.pack dlg)
         (.setVisible dlg true)
       )
     (= cmd "button-delete")
       (let [filter-list-model (:filter-list-model s)
             filter-list-items (:filter-list-items s)
             filter-list-jlist (:filter-list-jlist s)
             selected (.getMinSelectionIndex filter-list-jlist) 
             parent (:parent s)
          ]
          (if (= -1 selected)
            (javax.swing.JOptionPane/showMessageDialog parent "No filters selected")
            (do
              (.remove filter-list-model selected)
              (swap! filter-list-items (fn [v i] (into (subvec v 0 i) (subvec v (+ 1 i)))) selected)
            )
          )
       )
       (= cmd "button-up")
       (let [filter-list-model (:filter-list-model s)
             filter-list-items (:filter-list-items s)
             filter-list-jlist (:filter-list-jlist s)
             selected (.getMinSelectionIndex filter-list-jlist) 
             parent (:parent s)
          ]
          (if (= -1 selected)
            (javax.swing.JOptionPane/showMessageDialog parent "No filters selected")
            (if (> selected 0)
              (let [elem (.get filter-list-model selected)]
                (.remove filter-list-model selected)
                ;(println (str "ELEM = " elem))
                (.insertElementAt filter-list-model elem (- selected 1))
                (swap! filter-list-items (fn [v i] 
                    (into
                      (into (subvec v 0 (- i 1)) (subvec v i (+ 1 i)))
                      (into (subvec v (- i 1) i) (subvec v (+ 1 i)))
                    )
                ) 
                selected
               )
              )
            )
          )
       )
       (= cmd "button-down")
       (let [filter-list-model (:filter-list-model s)
             filter-list-items (:filter-list-items s)
             filter-list-jlist (:filter-list-jlist s)
             selected (.getMinSelectionIndex filter-list-jlist) 
             parent (:parent s)
          ]
          (if (= -1 selected)
            (javax.swing.JOptionPane/showMessageDialog parent "No filters selected")
            (if (> (- (.getSize filter-list-model) 1) selected)
              (let [elem (.get filter-list-model selected)]
                (.remove filter-list-model selected)
                (.insertElementAt filter-list-model elem (+ selected 1))
                (swap! filter-list-items (fn [v i] 
                    (into
                     (into (subvec v 0 i) (subvec v (+ i 1) (+ i 2)))
                     (into (subvec v i (+ 1 i)) (subvec v (+ 2 i)))
                    )
                ) 
                selected
               )
              )
            )
          )
       )
       
       (= cmd "button-edit")
         (let [filter-list-model (:filter-list-model s)
             filter-list-items (deref (:filter-list-items s))
             filter-list-jlist (:filter-list-jlist s)
             selected (.getMinSelectionIndex filter-list-jlist) 
             parent (:parent s)
          ]
          (if (= -1 selected)
            (javax.swing.JOptionPane/showMessageDialog parent "No filters selected")
            (let [
               selected-filter (nth filter-list-items selected)
               cfg-panel (.getConfigurePanel selected-filter)
               dlg (.getConfigureDialog this cfg-panel)
              ]
              (.setVisible dlg true)
            )
           )
         )
      :else
      nil
    )
  )
)

(defn -render
  [this parent-frame available-filters]
  (let [
      button-new (javax.swing.JButton. "New")
      button-edit (javax.swing.JButton. "Edit")
      button-delete (javax.swing.JButton. "Delete")
      button-up (javax.swing.JButton. "Up")
      button-down (javax.swing.JButton. "Down")
      button-panel (javax.swing.JPanel.)
      ;pane-title (javax.swing.JLabel. "Filters")
      filter-list-model (javax.swing.DefaultListModel.)
      filter-list-jlist (javax.swing.JList. filter-list-model)
      
      button-panel-layout (javax.swing.BoxLayout. button-panel javax.swing.BoxLayout/X_AXIS)
      panel-layout (javax.swing.BoxLayout. this javax.swing.BoxLayout/Y_AXIS)
    ]
    
    (swap! (.state this) assoc :parent parent-frame)
    (swap! (.state this) assoc :available-filters available-filters)
    
    (.setActionCommand button-new "button-new")
    (.addActionListener button-new this)
    (.setActionCommand button-edit "button-edit")
    (.addActionListener button-edit this)
    (.setActionCommand button-delete "button-delete")
    (.addActionListener button-delete this)
    (.setActionCommand button-up "button-up")
    (.addActionListener button-up this)
    (.setActionCommand button-down "button-down")
    (.addActionListener button-down this)
    
    (.add button-panel button-new)
    (.add button-panel button-edit)
    (.add button-panel button-delete)
    (.add button-panel button-up)
    (.add button-panel button-down)
    
    ;(.add this pane-title)
    (.add this button-panel)
    (.add this (javax.swing.Box/createRigidArea (java.awt.Dimension. 0 5)))
    
    (.add this filter-list-jlist)
    (.setLayout button-panel button-panel-layout)
    (.setLayout this panel-layout)
    (.setPreferredSize this (java.awt.Dimension. 120 200))
    
    (swap! (.state this) assoc :filter-list-jlist filter-list-jlist)
    (swap! (.state this) assoc :filter-list-model filter-list-model)
    (swap! (.state this) assoc :filter-list-items (atom []))
  )
)

(defn -init
  []
  [[] (atom {})]
)

