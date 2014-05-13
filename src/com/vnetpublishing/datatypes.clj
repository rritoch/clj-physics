(ns
  ^{
     :author "Ralph Ritoch <rritoch@gmail.com>"
     :doc "Data Types"
   } com.vnetpublishing.datatypes
)

(definterface imagemap
  (^java.awt.Image getImage [])
)

(gen-interface
 :name com.vnetpublishing.datatypes.ImageFilter
 :extends [java.awt.image.BufferedImageOp]
 :methods [[name [] String]
           [getConfigurePanel [] javax.swing.JPanel]
           [transform [java.awt.image.BufferedImage javax.swing.JComponent] java.awt.image.BufferedImage]
          ]
)
