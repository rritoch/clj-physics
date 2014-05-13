(ns
  ^{
     :author "Ralph Ritoch <rritoch@gmail.com>"
     :doc "Red Channel filter"
   } com.vnetpublishing.filters.redchannel
   
  (:gen-class
    :name com.vnetpublishing.filters.redchannel
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
  "Red Channel"
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
       pixels-len (* w h)
       _ (let [g (.createGraphics dest)] (.drawImage g work-image 0 0 (java.awt.Color/BLACK) nil) (.dispose g))
       r (.getRaster dest)
       pixels (.getPixels r 0 0 w h (int-array (* 4 pixels-len)))
   ]
   
   (loop [ctr 0]
     (if (== ctr pixels-len)
       (.setPixels r 0 0 w h pixels)
       (recur 
          (do
             ;(aset pixels (+ 0 (* ctr 4)) 0)
             (aset-int pixels (+ 1 (* ctr 4)) 0)
             (aset-int pixels (+ 2 (* ctr 4)) 0)
             ;(aset pixels (+ 3 (* ctr 4)) 0)
             (+ ctr 1)
          )
       )
     )
   )
 )
 dest
)

(defn -transform
  [this src target]
  (.filter this src src)
  src
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
