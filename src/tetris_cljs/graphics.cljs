(ns tetris-cljs.graphics)

(defn get-canvas []
  (.getElementById js/document "cvs"))

(defn get-context []
  (.getContext (get-canvas) "2d"))

(defn clear-canvas []
  (do (.save (get-context))
      (.clearRect (get-context))
      (.restore (get-context))))

(defn fitCanvasToWindow []
  (let [canvas (get-canvas)
        newWidth (/ (* (.innerHeight js/window) (.width canvas)) (.height canvas))]
    (aset canvas "style" "width"
      (if (< (.innerWidth js/window) newWidth)
        "100%"
        (str (- newWidth 20) "px")))))

(defn color [r g b a]
  (str "rgb(" r "," g "," b "," a ")"))

(def red "rgb(200,0,0)")
; (def draw-functions
;   { :fill-rect .fillRect
;     :stroke-rect .strokeRect
;     :fill-text .fillText
;     :stroke-text .strokeText})

(defn draw-rect-old [x y w h]
  (let [ctx (get-context)]
    (aset ctx "fillStyle" "rgb(200,0,0)")
    (.fillRect ctx x y w h)))

(defn draw-rect [args fill-color stroke-color line-width]
  (let [ctx (get-context)]
    (do
      (.save ctx)
      (aset ctx "fillStyle" fill-color)
      (aset ctx "strokeStyle" stroke-color)
      (aset ctx "lineWidth" line-width)
      (apply #(.fillRect ctx %1 %2 %3 %4) args)
      (apply #(.strokeRect ctx %1 %2 %3 %4) args)
      (.restore ctx))))
