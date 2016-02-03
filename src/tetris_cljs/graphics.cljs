(ns tetris-cljs.graphics)

(def canvas (.getElementById js/document "cvs"))

(def context (.getContext canvas "2d"))

(defn clear-canvas []
  (do (.save context)
      (.clearRect context)
      (.restore context)))

(defn fitCanvasToWindow []
  (let [newWidth (/ (* (.innerHeight js/window) (.width canvas)) (.height canvas))]
    (aset canvas "style" "width"
      (if (< (.innerWidth js/window) newWidth)
        "100%"
        (str (- newWidth 20) "px")))))

(defn color [r g b a]
  (str "rgb(" r "," g "," b "," a ")"))

(def red "rgb(200,0,0)")

(def draw-type
  { :rect [ #(.fillRect context %1 %2 %3 %4)
            #(.strokeRect context %1 %2 %3 %4)]
    :text [ #(.fillText context %1 %2 %3)
            #(.strokeText context %1 %2 %3)]})

(defn draw [type args fill-color stroke-color line-width]
  (do
    (.save context)
    (aset context "fillStyle" fill-color)
    (aset context "strokeStyle" stroke-color)
    (aset context "lineWidth" line-width)
    (aset context "font" "12px Arial")
    (doall (map #(apply % args) (type draw-type)))
    (.restore context)))
