(ns
  ^{
     :author "Ralph Ritoch <rritoch@gmail.com>"
     :doc "Filesystem Tree Selection Listener"
   } com.vnetpublishing.swing.event.FilesystemTreeSelectionListener
  (:gen-class
     :name com.vnetpublishing.swing.event.FilesystemTreeSelectionListener
     :state state
     :init init
     :prefix -
     :implements [javax.swing.event.TreeSelectionListener]
     :constructors {[javax.swing.JComponent javax.swing.JTree] []}
  )
  (:require [com.vnetpublishing.swing.FilesystemTreeSelectionListenerSwingWorker])
)

; see: http://codereview.stackexchange.com/questions/4446/file-browser-gui

(defn -valueChanged
  [this tse]
  (let [s (deref (.state this))
      node (.getLastPathComponent (.getPath tse))
      tree (:tree s)
      fileSystemView (:fileSystemView s)
      worker (com.vnetpublishing.swing.FilesystemTreeSelectionListenerSwingWorker. node tree)
    ]
    (.setEnabled tree false)
    (.execute worker)
  )
)

(defn -init
  [parent tree]
  [[] (atom {:fileSystemView (javax.swing.filechooser.FileSystemView/getFileSystemView) :parent parent :tree tree})]
)

