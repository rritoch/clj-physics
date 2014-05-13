(ns
  ^{
     :author "Ralph Ritoch <rritoch@gmail.com>"
     :doc "Image List Panel"
   } com.vnetpublishing.widgets.ImageListPanel
  
  (:gen-class
    :name com.vnetpublishing.widgets.ImageListPanel
    :state state
    :init init
    :prefix -
    :extends javax.swing.JPanel
    :implements [ 
      javax.swing.event.TreeSelectionListener
      javax.swing.ListCellRenderer
    ]
    :methods [
      [addImage [java.io.File] void]
      [render [] void]
      [getListModel [] javax.swing.DefaultListModel]
      [getJList [] javax.swing.JList]
      [getImagePreview [Integer java.io.File] com.vnetpublishing.widgets.ImagePreviewPanel]
    ]
  )
  (:require [com.vnetpublishing.layouts.CenteredScrollPaneLayout])
)

(defn -valueChanged
  [this tse]
  (let [s (deref (.state this))
      node (.getLastPathComponent (.getPath tse))
      folder (.getUserObject node)
      image-list-model (:image-list-model s)
      worker (com.vnetpublishing.swing.ImageListPanelSwingWorker. this folder)
    ]
    (.removeAllElements image-list-model)
    (swap! (.state this) assoc :image-previews {})
    (.execute worker)
  )
)

(defn -addImage
  [this file]
  (let [s (deref (.state this))
      image-list-model (:image-list-model s)
      index (.getSize image-list-model)
      image-list (:image-list s)
    ]
    (.addElement image-list-model file)
    (.getImagePreview this index file)
    ;(.revalidate (.getTopLevelAncestor image-list))
    ;(.repaint (.getTopLevelAncestor image-list))
    ;(.revalidate image-list)
    ;(.repaint image-list)
    ;(println "-addImage complete")
  )

)

(defn -getJList
  [this]
  (:image-list (deref (.state this)))
)

(defn -getListModel
  [this]
  (:image-list-model (deref (.state this)))
)

(defn -getImagePreview
  [this index file]
  (let [s (deref (.state this))
       image-previews (:image-previews s)
       image-preview (get image-previews index)
    ]
    (if image-preview
      (do
        image-preview
      )
      (let [p (com.vnetpublishing.widgets.ImagePreviewPanel.)]
        (.render p)
        (.setImageFile p file)
        (swap! (.state this) assoc :image-previews (merge (:image-previews (deref (.state this))) {index p}))
        p
      )
    )
  )
)


(defn -getListCellRendererComponent
  [this my-list value index selected has-focus]
 (let [img (.getImagePreview this index value)]
   ;(println "-getListCellRendererComponent")
   (if selected
      (do
        (.setBackground img (.getSelectionBackground my-list))
        (.setForeground img (.getSelectionForeground my-list))
      )
      (do
        (.setBackground img (.getBackground my-list))
        (.setForeground img (.getForeground my-list))
      )
    )
    img
  )
)

(defn -render
  [this]
 (let [s (deref (.state this))
      scrollpane (:scrollpane s)
      image-list (:image-list s)
     ;container (:container s)
     ;container-layout (javax.swing.BoxLayout. container javax.swing.BoxLayout/Y_AXIS)
   ]
   (.setLayout this (java.awt.GridLayout.))
   ;(.setLayout container container-layout)
   (.setLayout scrollpane (com.vnetpublishing.layouts.CenteredScrollPaneLayout.))
   (.add this scrollpane)
   (.setViewportView scrollpane image-list)
   ;(.add this container)
   (.setCellRenderer image-list this)
 )
)

(defn -init
  []
  (let [
     image-list-model  (javax.swing.DefaultListModel.)
     image-list (javax.swing.JList. image-list-model)
     scrollpane (javax.swing.JScrollPane. image-list)
     image-preview (com.vnetpublishing.widgets.ImagePreviewPanel.)
    ]
    (.setSelectionMode image-list javax.swing.ListSelectionModel/SINGLE_SELECTION)
    (.render image-preview)
     
    [  [] (atom {:image-list image-list :image-list-model image-list-model :scrollpane scrollpane})]
  )
)

