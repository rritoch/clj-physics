(ns
  ^{
     :author "Ralph Ritoch <rritoch@gmail.com>"
     :doc "Filesystem Tree Selection Listener Swing Worker"
   }  com.vnetpublishing.swing.FilesystemTreeSelectionListenerSwingWorker
  
  (:gen-class
    :name com.vnetpublishing.swing.FilesystemTreeSelectionListenerSwingWorker
    :state state
    :init init
    :prefix -
    :extends javax.swing.SwingWorker
    :constructors {[javax.swing.tree.MutableTreeNode javax.swing.JTree] []}
    :exposes-methods {publish superPublish}
  )
)

(defn -doInBackground [this]
  (let [s (deref (.state this))
      fileSystemView (javax.swing.filechooser.FileSystemView/getFileSystemView)
      node (:node s)
      file (.getUserObject node)
    ]
    (if (and (instance? java.io.File file) (.isDirectory file))
      (let [files (.getFiles fileSystemView file true)
          files-len (alength files)
        ]
        (if (.isLeaf node)
          (do
            (loop [ctr 0]
              (if (= ctr files-len)
                nil
                (recur
                  (let [child-file (aget files ctr)]
                    (if (.isDirectory child-file) 
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
)

(defn -done [this]
  (let [s (deref (.state this))
      tree (:tree s)
    ]
    ;progressBar.setIndeterminate(false);
    ;progressBar.setVisible(false);
    (.setEnabled tree true) 
  )
)

(defn -process 
  [this chunks]
  (let [s (deref (.state this))
        node (:node s)
        len (count chunks)
    ]
    (loop [ctr 0]
      (if (= ctr len)
        nil
        (recur
          (let [file (nth chunks ctr)] 
            (.add node (javax.swing.tree.DefaultMutableTreeNode. file))
            (+ ctr 1)
          )
        )
      )
    )
  )
)

(defn -init
  [node tree]
  [[] (atom {:node node :tree tree})]
)
