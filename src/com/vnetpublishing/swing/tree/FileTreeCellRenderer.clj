(ns
  ^{
     :author "Ralph Ritoch <rritoch@gmail.com>"
     :doc "Swing File Tree Cell Renderer"
   } com.vnetpublishing.swing.tree.FileTreeCellRenderer
  
  (:gen-class
    :name com.vnetpublishing.swing.tree.FileTreeCellRenderer
    :state state
    :init init
    :prefix -
    :extends javax.swing.tree.DefaultTreeCellRenderer
  )
)

(defn -getTreeCellRendererComponent
  [this tree node selected expanded leaf row hasFocus]
  (let [
    s (deref (.state this))
    obj (.getUserObject node)
    label (:label s)
    ;label (javax.swing.JLabel. "X")
    fileSystemView (:fileSystemView s)
  ]
    
  ;(.setOpaque label true)

  (cond
    (instance? java.io.File obj)
      (let [name (.getSystemDisplayName fileSystemView obj)] 
        (.setIcon label (.getSystemIcon fileSystemView obj))
        (.setText label name)
        (.setToolTipText label (.getPath obj))
      )
    (not obj)
      (do
        (.setText label "nil")
      )
    :else
      (let [name (str (.toString obj) " ???")]
        (.setText label name)
      )
  )

    (if selected
      (do 
         (.setBackground label (.getBackgroundSelectionColor this))
         (.setForeground label (.getTextSelectionColor this))
        )
      (do
         (.setBackground label (.getBackgroundNonSelectionColor this))
         (.setForeground label (.getTextNonSelectionColor this))
      )
    )
    
    label
  )
)

(defn -init
  []
  (let [label (javax.swing.JLabel.)
      fileSystemView (javax.swing.filechooser.FileSystemView/getFileSystemView)
    ]
    (.setOpaque label true)
    [[] (atom { :label label :fileSystemView fileSystemView})]
  )
)
