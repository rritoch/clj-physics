(ns
  ^{
     :author "Ralph Ritoch <rritoch@gmail.com>"
     :doc "Image List Panel Swing Worker"
   } com.vnetpublishing.swing.ImageListPanelSwingWorker
  
  (:gen-class
    :name com.vnetpublishing.swing.ImageListPanelSwingWorker
    :state state
    :init init
    :prefix -
    :extends javax.swing.SwingWorker
    :constructors {[javax.swing.JComponent java.io.File] []}
    :exposes-methods {publish superPublish}
  )
  (:require [com.vnetpublishing.widgets.ImagePreviewPanel])
)

(defn -doInBackground [this]
  (let [s (deref (.state this))
      fileSystemView (javax.swing.filechooser.FileSystemView/getFileSystemView)
      folder (:folder s)
    ]
    (if (and (instance? java.io.File folder) (.isDirectory folder))
      (let [files (.getFiles fileSystemView folder true)
          files-len (alength files)
        ]
        (do
          (loop [ctr 0]
            (if (= ctr files-len)
              nil
              (recur
                (let [child-file (aget files ctr)
                  name (.getAbsolutePath child-file)
                ]
                  (if 
                  (and
                    (.isFile child-file)
                    name
                    (or 
                        (.endsWith (.toLowerCase name) ".jpg")
                        (.endsWith (.toLowerCase name) ".jpeg")
                        (.endsWith (.toLowerCase name) ".gif")
                        (.endsWith (.toLowerCase name) ".png")
                      )
                   )
                     (.superPublish this (to-array [child-file]))
                  )
                  (+ 1 ctr)
                )
              )
            )
          )
        )
      )
    )
  )
)

(defn -done [this]
  (let [s (deref (.state this))
      image-list-panel (:image-list-panel s)
      
    ]
    
    ;(println "ImageListPanelSwingWorker:-done")
  )
)

(defn -process 
  [this chunks]
  (let [s (deref (.state this))
        image-list-panel (:image-list-panel s)
        len (count chunks)
    ]
    (loop [ctr 0]
      (if (= ctr len)
        nil
        (recur
          (let [file (nth chunks ctr)
       ;component (com.vnetpublishing.widgets.ImagePreviewPanel.)
     ]
     ;(.render component)
     ;(.setImageFile component file)
     ;(.addImage image-list-panel component)
     (.addImage image-list-panel file)
            (+ ctr 1)
          )
        )
      )
    )
  )
)

(defn -init
  [image-list-panel folder]
  [[] (atom {:image-list-panel image-list-panel :folder folder})]
)
