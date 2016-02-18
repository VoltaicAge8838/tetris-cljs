(ns tetris-cljs.tetriminoes)

(def tiles-wide 12)
(def tiles-high 22)

(defn get-block-width [width]
  (/ width (- tiles-wide 2)))

(defn get-block-height [height]
  (/ height (- tiles-high 2)))

(defn get-level-speed [lvl]
  (* 900 (- 1 (/ lvl (+ 2 lvl)))))

(def piece-types
  { :Z [[[1 1] [0 1] [0 0] [-1 0]]
        [[0 1] [0 0] [1 0] [1 -1]]]
    :S [[[-1 1] [0 1] [0 0] [1 0]]
        [[1 1] [0 0] [1 0] [0 -1]]]
    :O [[[0 1] [-1 1] [-1 0] [0 0]]]
    :I [[[-1 0] [0 0] [1 0] [-2 0]]
        [[0 2] [0 1] [0 0] [0 -1]]]
    :T [[[0 1] [-1 0] [0 0] [1 0]]
        [[0 1] [0 0] [-1 0] [0 -1]]
        [[-1 0] [0 0] [1 0] [0 -1]]
        [[0 1] [1 0] [0 0] [0 -1]]]
    :L [[[-1 1] [-1 0] [0 0] [1 0]]
        [[0 1] [0 0] [0 -1] [-1 -1]]
        [[-1 0] [0 0] [1 0] [1 -1]]
        [[1 1] [0 1] [0 0] [0 -1]]]
    :J [[[1 1] [-1 0] [0 0] [1 0]]
        [[0 1] [0 0] [-1 1] [0 -1]]
        [[-1 0] [0 0] [1 0] [-1 -1]]
        [[0 1] [0 0] [1 -1] [0 -1]]]})

; (def directions
;   { :up [0 -1]
;     :down [0 1]
;     :left [-1 0]
;     :right [1 0]})

(defn random-pieces-list []
  (shuffle (vals piece-types)))

(defn neighbor-direction [base n?]
  (let [dif [ (- (first base) (first n?))
              (- (last base) (last n?))]]
    (#{[0 -1] [0 1] [-1 0] [1 0]} dif)))

(defn to-neighbors-lists [coord-list]
  (for [x coord-list]
    (for [y coord-list
          :let [dir (neighbor-direction x y)]
          :when dir]
      dir)))

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

(defn compute-score [lines-cleared level]
  (apply * 50 (+ level 1) (range 2 lines-cleared)))

(defn make-block [neighbors color]
  { :group neighbors
    :color color
    :is-empty false})

(defn make-piece [grid x y type & [rotation]]
  (let [c (-> colors-list vals rand-nth)
        p (type piece-types)
        r (->> p count (mod rotation))
        n (to-neighbors-lists (p r))]
    (map #(make-block % c) n)))

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
    :piece-queue (random-pieces-list)
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
