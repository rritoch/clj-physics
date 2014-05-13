(ns
  ^{
     :author "Ralph Ritoch <rritoch@gmail.com>"
     :doc "Folder Select Panel"
   } com.vnetpublishing.widgets.FolderSelectPanel
  
  (:gen-class
    :name com.vnetpublishing.widgets.FolderSelectPanel
    :state state
    :init init
    :prefix -
    :extends javax.swing.JPanel
    :implements [javax.swing.Scrollable]
    :methods [
      [render [] void]
      [getTree [] javax.swing.JTree]
    ]
  )
  (:require [com.vnetpublishing.widgets.ImagePreviewPanel] 
            [com.vnetpublishing.swing.event.FilesystemTreeSelectionListener]
  )
)

(defn -getPreferredScrollableViewportSize
  [this]
  (.getPreferredSize this)
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

(defn -render
  [this]
  (let [
       state (.state this)
       fileSystemView (javax.swing.filechooser.FileSystemView/getFileSystemView) 
       root (javax.swing.tree.DefaultMutableTreeNode.)
       treeModel (javax.swing.tree.DefaultTreeModel. root)
       roots (.getRoots fileSystemView)
       roots-len (alength roots)
     ]
     (.setLayout this (java.awt.GridLayout.))
     (loop [root-ptr 0]
       (if (= root-ptr roots-len)
         nil
         (recur 
           (let 
             [root-fh (aget roots root-ptr)
              node (javax.swing.tree.DefaultMutableTreeNode. root-fh)
              files (.getFiles fileSystemView root-fh true)
              files-len (alength files)
             ]
             (.add root node)
             ;Need to loop through files!
             (loop [fptr 0]
               (if (= fptr files-len)
                 nil
                 (recur
                   (let [fh (aget files fptr)] 
                     (if (.isDirectory fh)
                       (let [child-node (javax.swing.tree.DefaultMutableTreeNode. fh)]
                          (.add node child-node)
                       )
                     )
                     (+ 1 fptr)
                   )
                 )
               )
             )
             (+ 1 root-ptr)
           )
         )
       )
     )
    
     (let [tree (javax.swing.JTree. treeModel)
         fileTreeCellRenderer (com.vnetpublishing.swing.tree.FileTreeCellRenderer.)
         treeSelectionListener (com.vnetpublishing.swing.event.FilesystemTreeSelectionListener. this tree)
       ]
       
       (.addTreeSelectionListener tree treeSelectionListener)
       (.setRootVisible tree false)
       
       (.setCellRenderer tree fileTreeCellRenderer)
       (.expandRow tree 0)

       (.setSelectionMode (.getSelectionModel tree) javax.swing.tree.TreeSelectionModel/SINGLE_TREE_SELECTION)
       
       (let [treeScroll (javax.swing.JScrollPane. tree)]
          (.add this treeScroll)
          (swap! (.state this) assoc :treeScroll treeScroll)
          (swap! (.state this) assoc :tree tree)
       )
    )
  )
)

(defn -getTree
  [this]
  (:tree (deref (.state this)))
)

(defn -init
  []
  [  [] (atom {})]
)


