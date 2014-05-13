(ns
  ^{
     :author "Ralph Ritoch <rritoch@gmail.com>"
     :doc "Image Viewer"
   } Physics.Views.ImageViewer
  
  (:gen-class
    :name Physics.Views.ImageViewer
    :state state
    :init init
    :prefix -
    :extends javax.swing.JInternalFrame
    :implements [java.awt.event.ActionListener
       javax.swing.event.ListSelectionListener
    ]
    :constructors {[clojure.lang.APersistentMap] [String boolean boolean boolean boolean]}
    :methods [
      [getImagePanel [] com.vnetpublishing.widgets.ImagePanel]
      [render [javax.swing.JFrame clojure.lang.APersistentMap] void]
      [getScrollPane [] javax.swing.JScrollPane]
      [getFilter [] com.vnetpublishing.datatypes.ImageFilter]
    ]
  )
  (:require 
    [com.vnetpublishing.layouts.CenteredScrollPaneLayout]
    [com.vnetpublishing.swing.event.FrameSupervisor]
  )
)


(defn -actionPerformed
  [this event]
  
  (let [cmd (.getActionCommand event)]
    (cond
      (= cmd "file-open") (.selectImage (.getImagePanel this))
      (= cmd "file-exit") 
        ;(.dispatchEvent this (java.awt.event.WindowEvent. this java.awt.event.WindowEvent/WINDOW_CLOSING))
        (.setClosed this true)
      :else
      nil
    )
  )
)

(defn -getImagePanel
  [this]
  (:image-panel (deref (.state this))) 
)

(defn -getFilter
  [this]
  (:image-filter-list-panel (deref (.state this)))
)

(defn -valueChanged
  [this e]  
  (let [image-list (.getSource e)
      min-index (.getMinSelectionIndex image-list)
      adj (.getValueIsAdjusting e)
      image-panel (:image-panel (deref (.state this)))
    ]
    
    (if (> min-index -1)
      (do 
        (.loadImageFile image-panel (.getElementAt (.getModel image-list) min-index))
      )
    )
  )
)

(defn -getScrollPane
  [this]
  (:scroll-pane (deref (.state this))) 
)


(defn -render
  [this parent cfg]
  (let [self this
      ;sv (com.vnetpublishing.swing.event.FrameSupervisor. self)
    ]
    (if (not (:image-panel (deref (.state this))))
      (let [

          image-panel (com.vnetpublishing.widgets.ImagePanel.)
          scroll-pane (javax.swing.JScrollPane. image-panel)
          file-menu (javax.swing.JMenu. "File")
          file-menuitem-open (javax.swing.JMenuItem. "Open")
          file-menuitem-exit (javax.swing.JMenuItem. "Exit")
          menu-bar (javax.swing.JMenuBar.)
          image-filter-list-panel (com.vnetpublishing.widgets.ImageFilterListPanel.)
          folder-select-panel (com.vnetpublishing.widgets.FolderSelectPanel.)
          control-pane (javax.swing.JTabbedPane.)
          split-plane (javax.swing.JSplitPane. javax.swing.JSplitPane/HORIZONTAL_SPLIT control-pane scroll-pane)
          image-list-panel (com.vnetpublishing.widgets.ImageListPanel.)
          images-panel (javax.swing.JSplitPane. javax.swing.JSplitPane/VERTICAL_SPLIT folder-select-panel image-list-panel)
        ]
      
        (.render image-panel scroll-pane)
        (.render image-filter-list-panel parent (:filter-list cfg))
        (.render folder-select-panel)
        (.render image-list-panel)
      
        (.addTreeSelectionListener (.getTree folder-select-panel) image-list-panel)
        (.addListSelectionListener (.getJList image-list-panel) this)
      
        (.setActionCommand file-menuitem-open "file-open")
        (.addActionListener file-menuitem-open this)
      
        (.setActionCommand file-menuitem-exit "file-exit")
        (.addActionListener file-menuitem-exit this)      
      
        (.add file-menu file-menuitem-open)
        (.add file-menu file-menuitem-exit)
        (.add menu-bar file-menu)
        (.setJMenuBar this menu-bar)
      
        (.addTab control-pane "Images" nil images-panel "Images")
        (.addTab control-pane "Filters" nil image-filter-list-panel "Image filters")
      
      
        (.setLayout scroll-pane (com.vnetpublishing.layouts.CenteredScrollPaneLayout.))
        ;(.setDefaultCloseOperation this javax.swing.JFrame/DISPOSE_ON_CLOSE)
        (.add this split-plane)
      
        (.setFilter image-panel image-filter-list-panel)
      
        (swap! (.state this) assoc :scroll-pane scroll-pane)
        (swap! (.state this) assoc :image-filter-list-panel image-filter-list-panel)
        (swap! (.state this) assoc :image-panel image-panel)

      )
    )
  
  
    (if (:title cfg) (.setTitle this (:title cfg)))
  
    (.pack this)
  
    (if (:width cfg)
      (let [size (.getSize this)]
        (.setSize size (double (:width cfg)) (.getHeight size))
        (.setSize this size)
      )
    )
  
    (if (:height cfg)
      (let [size (.getSize this)]
        (.setSize size (.getWidth size) (double (:height cfg)))
        (.setSize this size)
      )
    )
  
    (.setVisible this true)
    ;(.activate sv)
    nil
  )
)

(defn -init
  [cfg]
  [ [(if (:title cfg) (:title cfg) "") true true true true] (atom {})]
)

