(ns
  ^{
     :author "Ralph Ritoch <rritoch@gmail.com>"
     :doc "Image Preview Panel"
   } com.vnetpublishing.widgets.ImagePreviewPanel
  
  (:gen-class
    :name com.vnetpublishing.widgets.ImagePreviewPanel
    :state state
    :init init
    :prefix -
    :extends javax.swing.JPanel
    :implements [
       java.beans.PropertyChangeListener
     ]
    :methods [
      [render [] void]
      [scaleImage [] void]
      [setImageFile [java.io.File] void]
      [getFile [] java.io.File]
    ]
  )
)

(defn -propertyChange
  [this e]
  (if (= (.getPropertyName e) javax.swing.JFileChooser/SELECTED_FILE_CHANGED_PROPERTY)
    (let [selection (.getNewValue e)]
      (if selection
         (.setImageFile this selection)
      )
    )
  )
)

(defn -setImageFile
  [this selection]
  (let [name (.getAbsolutePath selection)]
    (if (and name
        (or 
          (.endsWith (.toLowerCase name) ".jpg")
          (.endsWith (.toLowerCase name) ".jpeg")
          (.endsWith (.toLowerCase name) ".gif")
          (.endsWith (.toLowerCase name) ".png")
        )
      )
            
      (let [icon (javax.swing.ImageIcon. name)]
        
        (swap! (.state this) assoc :image (.getImage icon))
        (swap! (.state this) assoc :file selection)
        
        
        (.scaleImage this)
        (.repaint this)
      )
    )
  )
)

(defn -getFile
  [this]
  (:file (deref (.state this)))
)

(defn -scaleImage
  [this]
  (let [image (:image (deref (.state this)))
      width (cond 
              (= (.getWidth image this) (.getHeight image this))
              155
              (> (.getWidth image this) (.getHeight image this))
              155
              (> (.getHeight image this) 150)
              (int (* (.getWidth image this) (/ (double 155) (.getHeight image this))))
              :else
              (int (.getWidth image this))
      )
      height (cond 
              (= (.getWidth image this) (.getHeight image this))
                (int (* (.getHeight image) (/ (double 155) (.getWidth image this))))
              (> (.getWidth image this) (.getHeight image this))
                (int (* (.getHeight image) (/ (double 155) (.getWidth image this))))
              (> (.getHeight image this) 150)
                155
              :else
              (int (.getHeight image this))
      )
      
     ]
    (reset! (.state this) (assoc (deref (.state this)) :image (.getScaledInstance image width height java.awt.Image/SCALE_DEFAULT)))
  )
)

(defn -render
  [this]
  (.setPreferredSize this (java.awt.Dimension. 155 155))
)


(defn -paintComponent
  [this g]
  (.setColor g (.getBackground this))
  (.fillRect g 0 0 (.getWidth this) (.getHeight this))
  (let [s (deref (.state this))
      image (:image s)
      ;file (:file s)
    ]
    (if image
      (let [x (int (+ 5 ( - (/ (.getWidth this) 2)  (/ (.getWidth image) 2)))) 
        y (int ( - (/ (.getHeight this) 2)  (/ (.getHeight image) 2)))]
        ;(println (str "ImagePreviewPanel:paintComponent " file " " (.getWidth image nil) "," (.getHeight image nil) " " x "," y))
        (.drawImage 
          g 
          image 
          x
          y
            this
        )
      )
      ;(println "ImagePreviewPanel:paintComponent-miss")
    )
  )
)



(defn -init
  []
  [[] (atom {})]
)
