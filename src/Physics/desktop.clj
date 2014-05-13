(ns
  ^{
     :author "Ralph Ritoch <rritoch@gmail.com>"
     :doc "Physics Desktop"
   } Physics.desktop  
  (:gen-class
    :name Physics.desktop
    :state state
    :init init
    :prefix -
    :extends javax.swing.JFrame
    :implements [java.awt.event.ActionListener
       javax.swing.event.ListSelectionListener
    ]
    :methods [
      [render [] void]
      [openImageViewer [] void]
      [addOpenableView [String String java.awt.event.ActionListener] void]
    ]
  )
  (:require [Physics.Views.ImageViewer]
            [Physics.filterlist])
)

(def desktops (atom []))

(defn getDesktop
  [n]
  (nth (deref desktops)) n
)

(defn start-desktop
  []
  (let [desktop (Physics.desktop.)]
    (.render desktop)
    (swap! desktops into [desktop])
  )
)

(defn -openImageViewer
  [this]
  (let [
      desktop (:desktop (deref (.state this)))
      imageviewer (Physics.Views.ImageViewer. {:title "Image Viewer"})
    ]
    (.render imageviewer this {:filter-list Physics.filterlist/filters :width 400 :height 200})
    (.add desktop imageviewer (- javax.swing.JLayeredPane/DEFAULT_LAYER 1))
  )
)

(defn -actionPerformed
  [this event]
  
  (let [cmd (.getActionCommand event)]
    (cond
      (= cmd "file-close") (.dispatchEvent this (java.awt.event.WindowEvent. this java.awt.event.WindowEvent/WINDOW_CLOSING))
      (= cmd "file-open-imageviewer") (.openImageViewer this)
      (= cmd "file-new-window") (start-desktop)
      :else
      nil
    )
  )
)

(defn -addOpenableView
  [this id title handler]
  (let [action (str "file-open-" id)
      menu-item (javax.swing.JMenuItem. title)
      menu (:file-menu-new (deref (.state this)))
    ]
    (.setActionCommand menu-item action)
    (.addActionListener menu-item handler)
    (.add menu menu-item)
  )
)

(defn -render 
  [this]
  (let [self this
       sv (com.vnetpublishing.swing.event.FrameSupervisor. self)
       desktop (javax.swing.JDesktopPane.)
       file-menu (javax.swing.JMenu. "File")
       file-menu-new (javax.swing.JMenu. "New") 
       file-menuitem-close (javax.swing.JMenuItem. "Close")
       file-menuitem-new-window (javax.swing.JMenuItem. "Window")
       menu-bar (javax.swing.JMenuBar.)
     ]
    
    (swap! (.state this) assoc :file-menu-new file-menu-new)
    
    (.setActionCommand file-menuitem-new-window "file-new-window")
    (.addActionListener file-menuitem-new-window this)
    (.add file-menu-new file-menuitem-new-window)
    
    (.addOpenableView this "imageviewer" "Image Viewier" this)
    
    (.add file-menu file-menu-new)
    
    (.setActionCommand file-menuitem-close "file-close")
    (.addActionListener file-menuitem-close this)
    (.add file-menu file-menuitem-close)
    
    (.add menu-bar file-menu)
    
    (.setJMenuBar this menu-bar)
        
    (.setOpaque desktop false)
    (.add (.getContentPane this) desktop java.awt.BorderLayout/CENTER)
    (.setDesktopManager desktop (javax.swing.DefaultDesktopManager.))
    
     (.setDefaultCloseOperation this javax.swing.JFrame/DISPOSE_ON_CLOSE)
    
    (.show this)
    (swap! (.state this) assoc :desktop desktop)
    (.setSize this (java.awt.Dimension. 640 480))
    (.activate sv)
  )
)

(defn -init
  []
  [[""] (atom {})]
)


