(ns
  ^{
     :author "Ralph Ritoch <rritoch@gmail.com>"
     :doc "Centered Swing Scroll Pane Layout"
   } com.vnetpublishing.layouts.CenteredScrollPaneLayout
  
  (:gen-class
     :name com.vnetpublishing.layouts.CenteredScrollPaneLayout
     :prefix -
     :extends javax.swing.ScrollPaneLayout
     :exposes-methods {layoutContainer superLayoutContainer}
  )
)

(defn -layoutContainer
  [this parent]
  (.superLayoutContainer this parent)
  (let [viewport (.getViewport this)
      view (.getView viewport)
    ]
    (if view
      (let [viewport-size (.getSize viewport)
          view-size (.getSize view)
        ]
        (if (or (> (.getWidth viewport-size) (.getWidth view-size)) (> (.getHeight viewport-size) (.getHeight view-size)))
          (let [spaceX (/ (-  (.getWidth viewport-size) (.getWidth view-size)) 2)
              spaceY (/ (-  (.getHeight viewport-size) (.getHeight view-size)) 2)
            ]
            (.setLocation viewport (int (if (> 0 spaceX) 0 spaceX)) (int (if(> 0 spaceY) 0 spaceY)))
            (.setSize viewport (- (.getWidth viewport-size) (if(> 0 spaceX) 0 spaceX)) (- (.getHeight viewport-size) (if(> 0 spaceY) 0 spaceY)))
          )
        )
      )
    )
  )
)
