(ns com.vnetpublishing.widgets.ImagePanel
  (:gen-class
    :name com.vnetpublishing.widgets.ImagePanel
    :extends javax.swing.JPanel
    :exposes-methods {paintComponent super-paintComponent}
    :methods [
      [setImage [java.awt.image.BufferedImage] void]
      [loadImage [String] void]
      [selectImage [] void]
    ]
  )
)

(defn -setImage
  [this img]
  (reset! (.state this) (assoc (deref (.state this)) :image img))
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
  (.super-paintComponent g)
  (if (:image (deref (.state this))) (.drawImage 0 0 nil))
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

(defn -init
  []
  [[] (atom {:image nil :file-chooser (javax.swing.JFileChooser.)})]
)

