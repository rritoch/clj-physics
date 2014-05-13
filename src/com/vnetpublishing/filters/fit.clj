(ns
  ^{
     :author "Ralph Ritoch <rritoch@gmail.com>"
     :doc "Fit image filter"
   } com.vnetpublishing.filters.fit
  
   (:gen-class
     :name com.vnetpublishing.filters.fit
     :state state
     :init init
     :prefix -
     :implements [com.vnetpublishing.datatypes.ImageFilter]
     :methods [
       [createConfigurePanel [] javax.swing.JPanel]
     ]
  )
  (:require [com.vnetpublishing.datatypes])
)

(defn -name
  [this]
  "Fit"
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
    (let [panel (.createConfigurePanel this)]
      (reset! (.state this) (assoc (deref (.state this)) :configure-panel panel))
      panel
    )
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
       g (.createGraphics dest) 
    ]
    (.drawImage g work-image 0 0 (java.awt.Color/BLACK) nil) 
    (.dispose g)
  )
  dest
)

(defn -transform
  [this src target]
  
  (let [tw (max 0 (- (.getWidth target) 10))
      th (max 0 (- (.getHeight target) 10))
      sw (.getWidth src)
      sh (.getHeight src)
      ah (if (> sw tw) (* tw (if (> sw 0) (/ sh sw) 0)) sh)
      aw (if (> sw tw) tw sw)
      bh (if (> ah th) th ah)
      bw (if (> ah th) (* th (if (> sh 0) (/ sw sh) 0)) aw)
      work-image (.getScaledInstance src bw bh java.awt.Image/SCALE_SMOOTH)
      dest (java.awt.image.BufferedImage. bw bh java.awt.image.BufferedImage/TYPE_INT_ARGB)
      g (.createGraphics dest) 
    ]
    (.drawImage g work-image 0 0 (java.awt.Color/BLACK) nil) 
    (.dispose g)
    dest
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

(defn -init
  []
  [[] (atom {})]
)
