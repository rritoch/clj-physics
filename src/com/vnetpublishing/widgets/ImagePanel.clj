(ns
  ^{
     :author "Ralph Ritoch <rritoch@gmail.com>"
     :doc "Image Panel"
   }  com.vnetpublishing.widgets.ImagePanel
  
  (:gen-class
    :name com.vnetpublishing.widgets.ImagePanel
    :state state
    :init init
    :prefix -
    :extends javax.swing.JPanel
    :implements [javax.swing.Scrollable]
    :exposes-methods {paintComponent superPaintComponent}
    :methods [
      [setImage [java.awt.image.BufferedImage] void]
      [loadImage [String] void]
      [loadImageFile [java.io.File] void]
      [selectImage [] void]
      [setFilter [com.vnetpublishing.datatypes.ImageFilter] void]
      [getFilter [] com.vnetpublishing.datatypes.ImageFilter]
      [render [javax.swing.JComponent] void]
    ]
  )
  (:require [com.vnetpublishing.widgets.ImagePreviewPanel])
)

(defn -getPreferredSize
  [this]
  (let [s (deref (.state this))
      image (:image s)
      filter (:filter s)
    ]
    (if image
      (if filter
        (let [image-out (.transform filter image (:target s))]
          (if image-out 
            (java.awt.Dimension. (.getWidth image-out) (.getHeight image-out))
            (java.awt.Dimension. 0 0)
          )
        )
        (java.awt.Dimension. (.getWidth image) (.getHeight image))
      )
      (java.awt.Dimension. 0 0)
    )
  )
)


(defn -getPreferredScrollableViewportSize
  [this]
  
  (let [s (deref (.state this))
      image (:image s)
    ]
    (if image
      (let [image-out (.transform filter image (:target s))]
        (java.awt.Dimension. (.getWidth image-out) (.getHeight image-out))
      )
      (java.awt.Dimension. 0 0)
    )
  )
)

(defn -getScrollableBlockIncrement
  [this visibleRect orientation direction]
  (if (= orientation javax.swing.SwingConstants/HORIZONTAL)
    (- (.width visibleRect) 1)
    (- (.height visibleRect) 1)
  )
)

(defn -getScrollableTracksViewportHeight
  [this]
  false
)

(defn -getScrollableTracksViewportWidth
  [this]
  false
)

(defn -getScrollableUnitIncrement
  [this visibleRect orientation direction]
  1
)

(defn -setImage
  [this img]
  (reset! (.state this) (assoc (deref (.state this)) :image img))
  ;(.revalidate this)
  ;(.repaint this)
)

(defn -setFilter
  [this filter]
  (reset! (.state this) (assoc (deref (.state this)) :filter filter))
  (.revalidate this)
)

(defn -getFilter
  [this]
  (:filter (deref (.state this)))
)


(defn -loadImageFile
  [this file]
  (try
    (let [img (javax.imageio.ImageIO/read file)]
      (.setImage this img)
      (.revalidate this)
      (.repaint this)
    )
    (catch Throwable t nil)
  )
)

(defn -loadImage
  [this path]
  (try
    (let [img (javax.imageio.ImageIO/read (java.io.File. path))]
      (.setImage this img)
    )
    (catch Throwable t nil)
  )
)

(defn -paintComponent
  [this g]
  (.superPaintComponent this g)
  (let [s (deref (.state this))
        image-in (:image s)
       filter (:filter s)
     ]
    
    (cond
      (and filter image-in) 
        (let [image-out (.transform filter image-in (:target s))]
          
          (.drawImage g image-out 0 0 nil)
        )
      image-in 
        (.drawImage g  image-in 0 0 nil)
      :else nil
    )
  )
)

(defn -selectImage
  [this]
  (let [r (.showOpenDialog (:file-chooser (deref (.state this))) this)]
    (if (= r javax.swing.JFileChooser/APPROVE_OPTION)
      (try 
         (let [img (javax.imageio.ImageIO/read (.getSelectedFile (:file-chooser (deref (.state this)))))]
           (.setImage this img)
         )
        (catch Throwable t)
      )
    )
  )
)

(defn -render 
  [this target]
  (swap! (.state this) assoc :target target)
)

(defn -init
  []
  (let [fc (javax.swing.JFileChooser. (java.io.File. (str (System/getProperty "user.home") java.io.File/separator "Pictures")))
        p (com.vnetpublishing.widgets.ImagePreviewPanel.)
     ]
    (.render p)
    (.setAccessory fc p)
    (.addPropertyChangeListener fc p)
    [  [] (atom {:image nil :file-chooser fc })]
  )
)

