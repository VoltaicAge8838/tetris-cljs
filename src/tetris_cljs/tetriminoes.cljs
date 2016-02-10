(ns tetris-cljs.tetriminoes)

(def tiles-wide 12)
(def tiles-high 22)

(defn make-block []
  {})

(defn make-empty-block []
  { :is-empty true})

(defn make-border-block []
  { :is-empty false
    :color [0 0 0 0]})

(defn make-grid []
  (for [x (range tiles-wide)]
    (for [y (range tiles-high)]
      (if (or (= x 0)
              (= x (- tiles-wide 1))
              (= y (- tiles-high 1)))
        (make-border-block)
        (make-empty-block)))))

(defn make-game-state [color-list width height x-offset y-offset]
  { :level 0
    :score 0
    :mode :drop ;; modes: drop, cascade, clear, game-over, prompt-highscore
    :lines-cleared 0
    :grid (make-grid)
    :active-block {}
    :next-block {}
    :level-speed 1
    :time 0
    :level-lines-cleared 0
    :total-lines-cleared 0
    :clear-line-counter 0
    :width width
    :height height
    :hard-drop false
    :soft-drop false})

(defn get-block-width [width]
  (/ width (- tiles-wide 2)))

(defn get-block-height [height]
  (/ height (- tiles-high 2)))
