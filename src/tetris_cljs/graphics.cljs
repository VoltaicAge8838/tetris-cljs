(ns tetris-cljs.graphics)

(def canvas (.getElementById js/document "cvs"))

(def context (.getContext canvas "2d"))

(def colors-list
  { :red [255 0 0]
    :green [0 255 0]
    :blue [0 0 255]
    :yellow [255 255 0]
    :cyan [0 255 255]
    :violet [255 0 255]
    :grey [200 200 200]})

(defn random-color []
  (rand-nth (vals colors-list)))

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

(defn color [r g b & [a]]
  (str "rgba(" r "," g "," b "," (or a "1") ")"))

(defn color-shadow [r g b]
  (color r g b "0.2"))

(def draw-type
  { :rect [ #(.strokeRect context %1 %2 %3 %4)
            #(.fillRect context %1 %2 %3 %4)]
    :text [ #(.strokeText context %1 %2 %3)
            #(.fillText context %1 %2 %3)]})

(defn draw [type args fill-color stroke-color line-width]
  (do
    (.save context)
    (aset context "fillStyle" fill-color)
    (aset context "strokeStyle" stroke-color)
    (aset context "lineWidth" line-width)
    (aset context "font" "12px Arial")
    (doall (map #(apply % args) (type draw-type)))
    (.restore context)))
